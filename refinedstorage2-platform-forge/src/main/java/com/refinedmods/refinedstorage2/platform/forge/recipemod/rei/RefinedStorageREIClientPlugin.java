package com.refinedmods.refinedstorage2.platform.forge.recipemod.rei;

import com.refinedmods.refinedstorage2.platform.api.PlatformApi;
import com.refinedmods.refinedstorage2.platform.api.recipemod.IngredientConverter;
import com.refinedmods.refinedstorage2.platform.common.content.BlockColorMap;
import com.refinedmods.refinedstorage2.platform.common.content.Blocks;
import com.refinedmods.refinedstorage2.platform.common.content.ContentIds;
import com.refinedmods.refinedstorage2.platform.common.content.Items;
import com.refinedmods.refinedstorage2.platform.common.content.Tags;
import com.refinedmods.refinedstorage2.platform.common.support.AbstractBaseScreen;
import com.refinedmods.refinedstorage2.platform.common.support.ColorableBlock;
import com.refinedmods.refinedstorage2.platform.common.support.energy.EnergyItemHelperImpl;

import java.util.function.Supplier;
import java.util.stream.Collectors;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.entry.CollapsibleEntryRegistry;
import me.shedaniel.rei.api.client.registry.screen.ExclusionZones;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.client.registry.transfer.TransferHandlerRegistry;
import me.shedaniel.rei.api.common.entry.comparison.ItemComparatorRegistry;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.forge.REIPluginClient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import static com.refinedmods.refinedstorage2.platform.common.util.IdentifierUtil.createIdentifier;
import static com.refinedmods.refinedstorage2.platform.common.util.IdentifierUtil.createTranslation;

@REIPluginClient
public class RefinedStorageREIClientPlugin implements REIClientPlugin {
    @Override
    public void registerScreens(final ScreenRegistry registry) {
        final IngredientConverter converter = PlatformApi.INSTANCE.getIngredientConverter();
        registry.registerFocusedStack(new GridFocusedStackProvider(converter));
        registry.registerFocusedStack(new ResourceFocusedStackProvider(converter));
        registry.registerDraggableStackVisitor(new DraggableStackVisitorImpl(converter));
    }

    public static void registerIngredientConverters() {
        PlatformApi.INSTANCE.registerIngredientConverter(new GridResourceIngredientConverter());
        PlatformApi.INSTANCE.registerIngredientConverter(new ResourceIngredientConverter());
    }

    @Override
    public void registerTransferHandlers(final TransferHandlerRegistry registry) {
        registry.register(new CraftingGridTransferHandler());
    }

    @Override
    public void registerItemComparators(final ItemComparatorRegistry registry) {
        Items.INSTANCE.getControllers().stream().map(Supplier::get).forEach(registry::registerNbt);
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void registerCollapsibleEntries(final CollapsibleEntryRegistry registry) {
        groupItems(registry, Blocks.INSTANCE.getCable(), ContentIds.CABLE, Tags.CABLES);
        groupItems(registry, Blocks.INSTANCE.getGrid(), ContentIds.GRID, Tags.GRIDS);
        groupItems(registry, Blocks.INSTANCE.getCraftingGrid(), ContentIds.CRAFTING_GRID, Tags.CRAFTING_GRIDS);
        groupItems(registry, Blocks.INSTANCE.getImporter(), ContentIds.IMPORTER, Tags.IMPORTERS);
        groupItems(registry, Blocks.INSTANCE.getExporter(), ContentIds.EXPORTER, Tags.EXPORTERS);
        groupItems(registry, Blocks.INSTANCE.getDetector(), ContentIds.DETECTOR, Tags.DETECTORS);
        groupItems(registry, Blocks.INSTANCE.getDestructor(), ContentIds.DESTRUCTOR, Tags.DESTRUCTORS);
        groupItems(registry, Blocks.INSTANCE.getConstructor(), ContentIds.CONSTRUCTOR, Tags.CONSTRUCTORS);
        groupItems(registry, Blocks.INSTANCE.getExternalStorage(), ContentIds.EXTERNAL_STORAGE, Tags.EXTERNAL_STORAGES);
        groupItems(registry, Blocks.INSTANCE.getController(), ContentIds.CONTROLLER, Tags.CONTROLLERS);
        registry.group(
            createIdentifier("fully_charged_controller"),
            createTranslation("block", "controller.rei_fully_charged"),
            EnergyItemHelperImpl.createAllAtEnergyCapacity(Items.INSTANCE.getControllers())
                .map(EntryStacks::of).collect(Collectors.toList())
        );
        groupItems(
            registry,
            Blocks.INSTANCE.getCreativeController(),
            ContentIds.CREATIVE_CONTROLLER,
            Tags.CREATIVE_CONTROLLERS
        );
        groupItems(
            registry,
            Blocks.INSTANCE.getWirelessTransmitter(),
            ContentIds.WIRELESS_TRANSMITTER,
            Tags.WIRELESS_TRANSMITTERS
        );
        groupItems(registry, Blocks.INSTANCE.getNetworkReceiver(), ContentIds.NETWORK_RECEIVER, Tags.NETWORK_RECEIVERS);
        groupItems(
            registry,
            Blocks.INSTANCE.getNetworkTransmitter(),
            ContentIds.NETWORK_TRANSMITTER,
            Tags.NETWORK_TRANSMITTERS
        );
    }

    @Override
    public void registerExclusionZones(final ExclusionZones zones) {
        zones.register(AbstractBaseScreen.class, new ExclusionZonesProviderImpl());
    }

    @SuppressWarnings("UnstableApiUsage")
    private static void groupItems(
        final CollapsibleEntryRegistry registry,
        final BlockColorMap<? extends ColorableBlock<? extends Block>> blocks,
        final ResourceLocation itemIdentifier,
        final TagKey<Item> tag
    ) {
        registry.group(
            itemIdentifier,
            blocks.getDefault().getName(),
            EntryIngredients.ofItemTag(tag)
        );
    }
}
