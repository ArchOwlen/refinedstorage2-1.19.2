package com.refinedmods.refinedstorage2.platform.common.support.resource;

import com.refinedmods.refinedstorage2.platform.api.support.resource.FluidResource;
import com.refinedmods.refinedstorage2.platform.api.support.resource.ResourceAmountTemplate;
import com.refinedmods.refinedstorage2.platform.api.support.resource.ResourceFactory;
import com.refinedmods.refinedstorage2.platform.common.Platform;
import com.refinedmods.refinedstorage2.platform.common.storage.channel.StorageChannelTypes;

import java.util.Optional;

import net.minecraft.world.item.ItemStack;

public class FluidResourceFactory implements ResourceFactory<FluidResource> {
    @Override
    public Optional<ResourceAmountTemplate<FluidResource>> create(final ItemStack stack) {
        return Platform.INSTANCE.getContainedFluid(stack).map(result -> new ResourceAmountTemplate<>(
            result.fluid().getResource(),
            Platform.INSTANCE.getBucketAmount(),
            StorageChannelTypes.FLUID
        ));
    }

    @Override
    public boolean isValid(final Object resource) {
        return resource instanceof FluidResource;
    }
}
