package com.refinedmods.refinedstorage2.core.storage;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

import com.refinedmods.refinedstorage2.core.list.StackList;
import com.refinedmods.refinedstorage2.core.list.item.ItemStackList;
import com.refinedmods.refinedstorage2.core.util.Action;
import net.minecraft.item.ItemStack;

public class CompositeItemStorage implements Storage<ItemStack> {
    private final TreeSet<Storage<ItemStack>> sources = new TreeSet<>(createComparator());
    private final StackList<ItemStack> list;

    public static CompositeItemStorage emptyStorage() {
        return new CompositeItemStorage(Collections.emptyList(), new ItemStackList());
    }

    public CompositeItemStorage(List<Storage<ItemStack>> sources, StackList<ItemStack> list) {
        this.sources.addAll(sources);
        this.list = list;

        fillListFromSources();
    }

    private void fillListFromSources() {
        sources.forEach(source -> source.getStacks().forEach(stack -> list.add(stack, stack.getCount())));
    }

    @Override
    public Optional<ItemStack> extract(ItemStack template, int amount, Action action) {
        int extracted = extractFromStorages(template, amount, action);
        if (action == Action.EXECUTE && extracted > 0) {
            list.remove(template, extracted);
        }

        if (extracted == 0) {
            return Optional.empty();
        } else {
            ItemStack result = template.copy();
            result.setCount(extracted);
            return Optional.of(result);
        }
    }

    private int extractFromStorages(ItemStack template, int amount, Action action) {
        int remaining = amount;
        for (Storage<ItemStack> source : sources) {
            Optional<ItemStack> stack = source.extract(template, remaining, action);
            if (stack.isPresent()) {
                remaining -= stack.get().getCount();
                if (remaining == 0) {
                    break;
                }
            }
        }

        return amount - remaining;
    }

    @Override
    public Optional<ItemStack> insert(ItemStack template, int amount, Action action) {
        int remainder = insertIntoStorages(template, amount, action);

        if (action == Action.EXECUTE) {
            int inserted = amount - remainder;
            if (inserted > 0) {
                list.add(template, inserted);
            }
        }

        if (remainder == 0) {
            return Optional.empty();
        } else {
            ItemStack remainderStack = template.copy();
            remainderStack.setCount(remainder);
            return Optional.of(remainderStack);
        }
    }

    private int insertIntoStorages(ItemStack template, int amount, Action action) {
        int remainder = amount;
        for (Storage<ItemStack> source : sources) {
            Optional<ItemStack> remainderStack = source.insert(template, remainder, action);
            if (!remainderStack.isPresent()) {
                remainder = 0;
                break;
            }
            remainder = remainderStack.get().getCount();
        }
        return remainder;
    }

    @Override
    public Collection<ItemStack> getStacks() {
        return list.getAll();
    }

    @Override
    public int getStored() {
        return sources.stream().mapToInt(Storage::getStored).sum();
    }

    private static Comparator<Storage<ItemStack>> createComparator() {
        return (s1, s2) -> Integer.compare(getPriority(s2), getPriority(s1));
    }

    private static int getPriority(Storage<ItemStack> storage) {
        if (storage instanceof Priority) {
            return ((Priority) storage).getPriority();
        }
        return 0;
    }
}
