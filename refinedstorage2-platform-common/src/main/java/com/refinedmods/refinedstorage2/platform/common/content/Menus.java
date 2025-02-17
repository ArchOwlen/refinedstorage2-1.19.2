package com.refinedmods.refinedstorage2.platform.common.content;

import com.refinedmods.refinedstorage2.platform.common.constructordestructor.ConstructorContainerMenu;
import com.refinedmods.refinedstorage2.platform.common.constructordestructor.DestructorContainerMenu;
import com.refinedmods.refinedstorage2.platform.common.controller.ControllerContainerMenu;
import com.refinedmods.refinedstorage2.platform.common.detector.DetectorContainerMenu;
import com.refinedmods.refinedstorage2.platform.common.exporter.ExporterContainerMenu;
import com.refinedmods.refinedstorage2.platform.common.grid.CraftingGridContainerMenu;
import com.refinedmods.refinedstorage2.platform.common.grid.GridContainerMenu;
import com.refinedmods.refinedstorage2.platform.common.grid.WirelessGridContainerMenu;
import com.refinedmods.refinedstorage2.platform.common.iface.InterfaceContainerMenu;
import com.refinedmods.refinedstorage2.platform.common.importer.ImporterContainerMenu;
import com.refinedmods.refinedstorage2.platform.common.networking.NetworkTransmitterContainerMenu;
import com.refinedmods.refinedstorage2.platform.common.storage.diskdrive.DiskDriveContainerMenu;
import com.refinedmods.refinedstorage2.platform.common.storage.externalstorage.ExternalStorageContainerMenu;
import com.refinedmods.refinedstorage2.platform.common.storage.storageblock.FluidStorageBlockContainerMenu;
import com.refinedmods.refinedstorage2.platform.common.storage.storageblock.ItemStorageBlockContainerMenu;
import com.refinedmods.refinedstorage2.platform.common.storagemonitor.StorageMonitorContainerMenu;
import com.refinedmods.refinedstorage2.platform.common.upgrade.RegulatorUpgradeContainerMenu;
import com.refinedmods.refinedstorage2.platform.common.wirelesstransmitter.WirelessTransmitterContainerMenu;

import java.util.Objects;
import java.util.function.Supplier;
import javax.annotation.Nullable;

import net.minecraft.world.inventory.MenuType;

public final class Menus {
    public static final Menus INSTANCE = new Menus();

    @Nullable
    private Supplier<MenuType<DiskDriveContainerMenu>> diskDrive;
    @Nullable
    private Supplier<MenuType<GridContainerMenu>> grid;
    @Nullable
    private Supplier<MenuType<CraftingGridContainerMenu>> craftingGrid;
    @Nullable
    private Supplier<MenuType<WirelessGridContainerMenu>> wirelessGrid;
    @Nullable
    private Supplier<MenuType<ControllerContainerMenu>> controller;
    @Nullable
    private Supplier<MenuType<ItemStorageBlockContainerMenu>> itemStorage;
    @Nullable
    private Supplier<MenuType<FluidStorageBlockContainerMenu>> fluidStorage;
    @Nullable
    private Supplier<MenuType<ImporterContainerMenu>> importer;
    @Nullable
    private Supplier<MenuType<ExporterContainerMenu>> exporter;
    @Nullable
    private Supplier<MenuType<InterfaceContainerMenu>> iface;
    @Nullable
    private Supplier<MenuType<ExternalStorageContainerMenu>> externalStorage;
    @Nullable
    private Supplier<MenuType<DetectorContainerMenu>> detector;
    @Nullable
    private Supplier<MenuType<DestructorContainerMenu>> destructor;
    @Nullable
    private Supplier<MenuType<ConstructorContainerMenu>> constructor;
    @Nullable
    private Supplier<MenuType<RegulatorUpgradeContainerMenu>> regulatorUpgrade;
    @Nullable
    private Supplier<MenuType<WirelessTransmitterContainerMenu>> wirelessTransmitter;
    @Nullable
    private Supplier<MenuType<StorageMonitorContainerMenu>> storageMonitor;
    @Nullable
    private Supplier<MenuType<NetworkTransmitterContainerMenu>> networkTransmitter;

    private Menus() {
    }

    public MenuType<DiskDriveContainerMenu> getDiskDrive() {
        return Objects.requireNonNull(diskDrive).get();
    }

    public MenuType<GridContainerMenu> getGrid() {
        return Objects.requireNonNull(grid).get();
    }

    public MenuType<CraftingGridContainerMenu> getCraftingGrid() {
        return Objects.requireNonNull(craftingGrid).get();
    }

    public void setCraftingGrid(final Supplier<MenuType<CraftingGridContainerMenu>> supplier) {
        this.craftingGrid = supplier;
    }

    public MenuType<WirelessGridContainerMenu> getWirelessGrid() {
        return Objects.requireNonNull(wirelessGrid).get();
    }

    public void setWirelessGrid(final Supplier<MenuType<WirelessGridContainerMenu>> supplier) {
        this.wirelessGrid = supplier;
    }

    public MenuType<ControllerContainerMenu> getController() {
        return Objects.requireNonNull(controller).get();
    }

    public void setDiskDrive(final Supplier<MenuType<DiskDriveContainerMenu>> supplier) {
        this.diskDrive = supplier;
    }

    public void setGrid(final Supplier<MenuType<GridContainerMenu>> supplier) {
        this.grid = supplier;
    }

    public void setController(final Supplier<MenuType<ControllerContainerMenu>> supplier) {
        this.controller = supplier;
    }

    public MenuType<ItemStorageBlockContainerMenu> getItemStorage() {
        return Objects.requireNonNull(itemStorage).get();
    }

    public void setItemStorage(final Supplier<MenuType<ItemStorageBlockContainerMenu>> supplier) {
        this.itemStorage = supplier;
    }

    public MenuType<FluidStorageBlockContainerMenu> getFluidStorage() {
        return Objects.requireNonNull(fluidStorage).get();
    }

    public void setFluidStorage(final Supplier<MenuType<FluidStorageBlockContainerMenu>> supplier) {
        this.fluidStorage = supplier;
    }

    public MenuType<ImporterContainerMenu> getImporter() {
        return Objects.requireNonNull(importer).get();
    }

    public void setImporter(final Supplier<MenuType<ImporterContainerMenu>> supplier) {
        this.importer = supplier;
    }

    public MenuType<ExporterContainerMenu> getExporter() {
        return Objects.requireNonNull(exporter).get();
    }

    public void setExporter(final Supplier<MenuType<ExporterContainerMenu>> supplier) {
        this.exporter = supplier;
    }

    public MenuType<InterfaceContainerMenu> getInterface() {
        return Objects.requireNonNull(iface).get();
    }

    public void setInterface(final Supplier<MenuType<InterfaceContainerMenu>> supplier) {
        this.iface = supplier;
    }

    public MenuType<ExternalStorageContainerMenu> getExternalStorage() {
        return Objects.requireNonNull(externalStorage).get();
    }

    public void setExternalStorage(final Supplier<MenuType<ExternalStorageContainerMenu>> supplier) {
        this.externalStorage = supplier;
    }

    public MenuType<DetectorContainerMenu> getDetector() {
        return Objects.requireNonNull(detector).get();
    }

    public void setDetector(final Supplier<MenuType<DetectorContainerMenu>> supplier) {
        this.detector = supplier;
    }

    public MenuType<DestructorContainerMenu> getDestructor() {
        return Objects.requireNonNull(destructor).get();
    }

    public void setDestructor(final Supplier<MenuType<DestructorContainerMenu>> supplier) {
        this.destructor = supplier;
    }

    public MenuType<ConstructorContainerMenu> getConstructor() {
        return Objects.requireNonNull(constructor).get();
    }

    public void setConstructor(final Supplier<MenuType<ConstructorContainerMenu>> supplier) {
        this.constructor = supplier;
    }

    public MenuType<RegulatorUpgradeContainerMenu> getRegulatorUpgrade() {
        return Objects.requireNonNull(regulatorUpgrade).get();
    }

    public void setRegulatorUpgrade(final Supplier<MenuType<RegulatorUpgradeContainerMenu>> supplier) {
        this.regulatorUpgrade = supplier;
    }

    public MenuType<WirelessTransmitterContainerMenu> getWirelessTransmitter() {
        return Objects.requireNonNull(wirelessTransmitter).get();
    }

    public void setWirelessTransmitter(final Supplier<MenuType<WirelessTransmitterContainerMenu>> supplier) {
        this.wirelessTransmitter = supplier;
    }

    public MenuType<StorageMonitorContainerMenu> getStorageMonitor() {
        return Objects.requireNonNull(storageMonitor).get();
    }

    public void setStorageMonitor(final Supplier<MenuType<StorageMonitorContainerMenu>> supplier) {
        this.storageMonitor = supplier;
    }

    public MenuType<NetworkTransmitterContainerMenu> getNetworkTransmitter() {
        return Objects.requireNonNull(networkTransmitter).get();
    }

    public void setNetworkTransmitter(final Supplier<MenuType<NetworkTransmitterContainerMenu>> supplier) {
        this.networkTransmitter = supplier;
    }
}
