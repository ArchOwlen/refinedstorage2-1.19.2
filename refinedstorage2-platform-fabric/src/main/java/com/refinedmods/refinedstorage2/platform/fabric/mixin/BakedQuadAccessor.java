package com.refinedmods.refinedstorage2.platform.fabric.mixin;

import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.texture.Sprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BakedQuad.class)
public interface BakedQuadAccessor {
    @Accessor
    Sprite getSprite();
}
