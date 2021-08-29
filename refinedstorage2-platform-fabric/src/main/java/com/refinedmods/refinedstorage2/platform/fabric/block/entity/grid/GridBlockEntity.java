package com.refinedmods.refinedstorage2.platform.fabric.block.entity.grid;

import com.refinedmods.refinedstorage2.api.grid.GridWatcher;
import com.refinedmods.refinedstorage2.api.grid.search.GridSearchBoxMode;
import com.refinedmods.refinedstorage2.api.grid.search.GridSearchBoxModeRegistry;
import com.refinedmods.refinedstorage2.api.grid.view.GridSize;
import com.refinedmods.refinedstorage2.api.grid.view.GridSortingDirection;
import com.refinedmods.refinedstorage2.api.grid.view.GridSortingType;
import com.refinedmods.refinedstorage2.api.network.node.grid.GridNetworkNode;
import com.refinedmods.refinedstorage2.api.stack.Rs2Stack;
import com.refinedmods.refinedstorage2.api.storage.channel.StorageChannelType;
import com.refinedmods.refinedstorage2.platform.fabric.Rs2Config;
import com.refinedmods.refinedstorage2.platform.fabric.Rs2Mod;
import com.refinedmods.refinedstorage2.platform.fabric.block.entity.FabricNetworkNodeContainerBlockEntity;
import com.refinedmods.refinedstorage2.platform.fabric.util.PacketUtil;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public abstract class GridBlockEntity<T extends Rs2Stack> extends FabricNetworkNodeContainerBlockEntity<GridNetworkNode<T>> implements ExtendedScreenHandlerFactory {
    private static final String TAG_SORTING_DIRECTION = "sd";
    private static final String TAG_SORTING_TYPE = "st";
    private static final String TAG_SIZE = "s";
    private static final String TAG_SEARCH_BOX_MODE = "sbm";

    private final StorageChannelType<T> type;

    public GridBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state, StorageChannelType<T> type) {
        super(blockEntityType, pos, state);
        this.type = type;
    }

    @Override
    protected GridNetworkNode<T> createNode(BlockPos pos, NbtCompound tag) {
        GridNetworkNode<T> grid = new GridNetworkNode<>(
                GridSearchBoxModeRegistry.INSTANCE.getDefault(),
                Rs2Config.get().getGrid().getEnergyUsage(),
                type
        );

        if (tag != null) {
            if (tag.contains(TAG_SORTING_DIRECTION)) {
                grid.setSortingDirection(GridSettings.getSortingDirection(tag.getInt(TAG_SORTING_DIRECTION)));
            }

            if (tag.contains(TAG_SORTING_TYPE)) {
                grid.setSortingType(GridSettings.getSortingType(tag.getInt(TAG_SORTING_TYPE)));
            }

            if (tag.contains(TAG_SIZE)) {
                grid.setSize(GridSettings.getSize(tag.getInt(TAG_SIZE)));
            }

            if (tag.contains(TAG_SEARCH_BOX_MODE)) {
                grid.setSearchBoxMode(GridSearchBoxModeRegistry.INSTANCE.get(tag.getInt(TAG_SEARCH_BOX_MODE)));
            }
        }

        return grid;
    }

    @Override
    public Text getDisplayName() {
        return Rs2Mod.createTranslation("block", "grid");
    }

    @Override
    public NbtCompound writeNbt(NbtCompound tag) {
        tag.putInt(TAG_SORTING_DIRECTION, GridSettings.getSortingDirection(getContainer().getNode().getSortingDirection()));
        tag.putInt(TAG_SORTING_TYPE, GridSettings.getSortingType(getContainer().getNode().getSortingType()));
        tag.putInt(TAG_SIZE, GridSettings.getSize(getContainer().getNode().getSize()));
        tag.putInt(TAG_SEARCH_BOX_MODE, GridSearchBoxModeRegistry.INSTANCE.getId(getContainer().getNode().getSearchBoxMode()));
        return super.writeNbt(tag);
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBoolean(getContainer().getNode().isActive());
        buf.writeInt(GridSettings.getSortingDirection(getSortingDirection()));
        buf.writeInt(GridSettings.getSortingType(getSortingType()));
        buf.writeInt(GridSettings.getSize(getSize()));
        buf.writeInt(GridSearchBoxModeRegistry.INSTANCE.getId(getSearchBoxMode()));

        buf.writeInt(getContainer().getNode().getStackCount());
        getContainer().getNode().forEachStack((stack, trackerEntry) -> {
            writeStack(buf, stack);
            PacketUtil.writeTrackerEntry(buf, trackerEntry);
        });
    }

    protected abstract void writeStack(PacketByteBuf buf, T stack);

    public GridSortingType getSortingType() {
        return getContainer().getNode().getSortingType();
    }

    public void setSortingType(GridSortingType sortingType) {
        getContainer().getNode().setSortingType(sortingType);
        markDirty();
    }

    public GridSortingDirection getSortingDirection() {
        return getContainer().getNode().getSortingDirection();
    }

    public void setSortingDirection(GridSortingDirection sortingDirection) {
        getContainer().getNode().setSortingDirection(sortingDirection);
        markDirty();
    }

    public GridSearchBoxMode getSearchBoxMode() {
        return getContainer().getNode().getSearchBoxMode();
    }

    public void setSearchBoxMode(GridSearchBoxMode searchBoxMode) {
        getContainer().getNode().setSearchBoxMode(searchBoxMode);
        markDirty();
    }

    public GridSize getSize() {
        return getContainer().getNode().getSize();
    }

    public void setSize(GridSize size) {
        getContainer().getNode().setSize(size);
        markDirty();
    }

    public void addWatcher(GridWatcher watcher) {
        getContainer().getNode().addWatcher(watcher);
    }

    public void removeWatcher(GridWatcher watcher) {
        getContainer().getNode().removeWatcher(watcher);
    }
}
