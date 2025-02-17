package com.refinedmods.refinedstorage2.platform.fabric.importer;

import com.refinedmods.refinedstorage2.api.core.Action;
import com.refinedmods.refinedstorage2.api.network.node.importer.ImporterSource;
import com.refinedmods.refinedstorage2.api.storage.Actor;
import com.refinedmods.refinedstorage2.platform.api.exporter.AmountOverride;
import com.refinedmods.refinedstorage2.platform.fabric.storage.FabricStorageExtractableStorage;
import com.refinedmods.refinedstorage2.platform.fabric.storage.FabricStorageInsertableStorage;

import java.util.Collections;
import java.util.Iterator;
import java.util.function.Function;

import net.fabricmc.fabric.api.lookup.v1.block.BlockApiCache;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;

import static com.google.common.collect.Iterators.filter;
import static com.google.common.collect.Iterators.transform;

class FabricStorageImporterSource<T, P> implements ImporterSource<T> {
    private final BlockApiCache<Storage<P>, Direction> cache;
    private final Function<P, T> fromPlatformMapper;
    private final FabricStorageInsertableStorage<T, P> insertTarget;
    private final FabricStorageExtractableStorage<T, P> extractTarget;
    private final Direction direction;

    FabricStorageImporterSource(final BlockApiLookup<Storage<P>, Direction> lookup,
                                final Function<P, T> fromPlatformMapper,
                                final Function<T, P> toPlatformMapper,
                                final ServerLevel serverLevel,
                                final BlockPos pos,
                                final Direction direction,
                                final AmountOverride amountOverride) {
        this.cache = BlockApiCache.create(lookup, serverLevel, pos);
        this.fromPlatformMapper = fromPlatformMapper;
        this.insertTarget = new FabricStorageInsertableStorage<>(
            lookup,
            toPlatformMapper,
            serverLevel,
            pos,
            direction,
            AmountOverride.NONE
        );
        this.extractTarget = new FabricStorageExtractableStorage<>(
            lookup,
            toPlatformMapper,
            serverLevel,
            pos,
            direction,
            amountOverride
        );
        this.direction = direction;
    }

    @Override
    public Iterator<T> getResources() {
        final Storage<P> storage = cache.find(direction);
        if (storage == null) {
            return Collections.emptyListIterator();
        }
        final Iterator<StorageView<P>> iterator = storage.iterator();
        return transform(
            filter(iterator, storageView -> !storageView.isResourceBlank()),
            storageView -> fromPlatformMapper.apply(storageView.getResource())
        );
    }

    @Override
    public long extract(final T resource, final long amount, final Action action, final Actor actor) {
        return extractTarget.extract(resource, amount, action, actor);
    }

    @Override
    public long insert(final T resource, final long amount, final Action action, final Actor actor) {
        return insertTarget.insert(resource, amount, action, actor);
    }
}
