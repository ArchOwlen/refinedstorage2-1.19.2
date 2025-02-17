package com.refinedmods.refinedstorage2.platform.forge.datagen;

import com.refinedmods.refinedstorage2.platform.forge.datagen.loot.LootTableProvider;
import com.refinedmods.refinedstorage2.platform.forge.datagen.recipe.RecoloringRecipeProvider;
import com.refinedmods.refinedstorage2.platform.forge.datagen.tag.BlockTagsProvider;
import com.refinedmods.refinedstorage2.platform.forge.datagen.tag.ItemTagGenerator;

import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataGenerator.PackGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import static com.refinedmods.refinedstorage2.platform.common.util.IdentifierUtil.MOD_ID;

@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    private DataGenerators() {
    }

    @SubscribeEvent
    public static void onGatherData(final GatherDataEvent dataEvent) {
        registerBlockModelProviders(dataEvent.getGenerator(), dataEvent.getExistingFileHelper());
        registerItemModelProviders(dataEvent.getGenerator(), dataEvent.getExistingFileHelper());
        registerBlockStateProviders(dataEvent.getGenerator(), dataEvent.getExistingFileHelper());
        registerLootTableProviders(dataEvent.getGenerator());
        registerRecipeProviders(dataEvent.getGenerator());
        registerTagProviders(
            dataEvent.getGenerator(),
            dataEvent.getLookupProvider(),
            dataEvent.getExistingFileHelper()
        );
    }

    // this function has to happen after the models, since the jsons refer to files generated there
    private static void registerBlockStateProviders(final DataGenerator generator,
                                                    final ExistingFileHelper existingFileHelper) {
        final PackGenerator mainPack = generator.getVanillaPack(true);

        mainPack.addProvider(output -> new BlockStateProvider(output, existingFileHelper));
    }

    private static void registerBlockModelProviders(final DataGenerator generator,
                                                    final ExistingFileHelper existingFileHelper) {
        final PackGenerator mainPack = generator.getVanillaPack(true);

        mainPack.addProvider(output -> new BlockModelProvider(output, existingFileHelper));
    }

    private static void registerItemModelProviders(final DataGenerator generator,
                                                   final ExistingFileHelper existingFileHelper) {
        final PackGenerator mainPack = generator.getVanillaPack(true);

        mainPack.addProvider(output -> new ItemModelProvider(output, existingFileHelper));
    }

    private static void registerLootTableProviders(final DataGenerator generator) {
        final PackGenerator mainPack = generator.getVanillaPack(true);

        mainPack.addProvider(LootTableProvider::new);
    }

    private static void registerRecipeProviders(final DataGenerator generator) {
        final PackGenerator mainPack = generator.getVanillaPack(true);

        mainPack.addProvider(RecoloringRecipeProvider::new);
    }

    private static void registerTagProviders(
        final DataGenerator generator,
        final CompletableFuture<HolderLookup.Provider> lookupProvider,
        final ExistingFileHelper existingFileHelper
    ) {
        final PackGenerator mainPack = generator.getVanillaPack(true);
        final BlockTagsProvider blockTagsProvider = mainPack.addProvider(
            output -> new BlockTagsProvider(output, lookupProvider, existingFileHelper)
        );
        mainPack.addProvider(output -> new ItemTagGenerator(
            output,
            lookupProvider,
            blockTagsProvider,
            existingFileHelper
        ));
    }
}
