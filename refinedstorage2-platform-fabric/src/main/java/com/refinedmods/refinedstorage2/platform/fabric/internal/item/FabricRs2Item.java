package com.refinedmods.refinedstorage2.platform.fabric.internal.item;

import com.refinedmods.refinedstorage2.api.stack.item.Rs2Item;

import net.minecraft.item.Item;

public record FabricRs2Item(Item item) implements Rs2Item {
    @Override
    public int getMaxAmount() {
        return item.getMaxCount();
    }

    @Override
    public int getId() {
        return Item.getRawId(item);
    }

    @Override
    public String getName() {
        return item.getName().getString();
    }

    @Override
    public String toString() {
        return item.toString();
    }
}
