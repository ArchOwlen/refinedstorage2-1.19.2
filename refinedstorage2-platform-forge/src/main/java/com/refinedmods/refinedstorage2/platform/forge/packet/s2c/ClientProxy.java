package com.refinedmods.refinedstorage2.platform.forge.packet.s2c;

import java.util.Optional;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

final class ClientProxy {
    private ClientProxy() {
    }

    // This method exists to avoid classloading errors on LocalPlayer.
    static Optional<Player> getPlayer() {
        return Optional.ofNullable(Minecraft.getInstance().player);
    }
}
