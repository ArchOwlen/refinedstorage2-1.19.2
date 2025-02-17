package com.refinedmods.refinedstorage2.platform.forge.support.containermenu;

import com.refinedmods.refinedstorage2.platform.common.support.containermenu.ExtendedMenuProvider;
import com.refinedmods.refinedstorage2.platform.common.support.containermenu.MenuOpener;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraftforge.network.NetworkHooks;

public class MenuOpenerImpl implements MenuOpener {
    @Override
    public void openMenu(final ServerPlayer player, final MenuProvider menuProvider) {
        if (menuProvider instanceof ExtendedMenuProvider extendedMenuProvider) {
            NetworkHooks.openScreen(
                player,
                menuProvider,
                buf -> extendedMenuProvider.writeScreenOpeningData(player, buf)
            );
        } else {
            player.openMenu(menuProvider);
        }
    }
}
