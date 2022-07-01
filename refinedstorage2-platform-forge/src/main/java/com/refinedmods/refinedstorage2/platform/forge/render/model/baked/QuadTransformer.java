package com.refinedmods.refinedstorage2.platform.forge.render.model.baked;

import com.refinedmods.refinedstorage2.platform.common.util.BiDirection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import javax.annotation.Nullable;

import com.mojang.math.Matrix4f;
import com.mojang.math.Transformation;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraftforge.client.model.pipeline.BakedQuadBuilder;
import net.minecraftforge.client.model.pipeline.TRSRTransformer;

public final class QuadTransformer {
    private QuadTransformer() {
    }

    public static List<BakedQuad> transformSideAndRotate(final Function<Direction, List<BakedQuad>> quadGetter,
                                                         final BiDirection direction,
                                                         @Nullable final Direction side) {
        final Transformation transformation = new Transformation(null, direction.getQuaternion(), null, null);

        final List<BakedQuad> quads = quadGetter.apply(transformSide(side, transformation.getMatrix()));
        final List<BakedQuad> rotated = new ArrayList<>(quads.size());

        for (final BakedQuad quad : quads) {
            final BakedQuadBuilder builder = new BakedQuadBuilder(quad.getSprite());
            final TRSRTransformer transformer = new TRSRTransformer(builder, transformation.blockCenterToCorner());

            quad.pipe(transformer);

            builder.setQuadOrientation(rotate(quad.getDirection(), transformation.getMatrix()));

            rotated.add(builder.build());
        }

        return Collections.unmodifiableList(rotated);
    }

    @Nullable
    private static Direction transformSide(@Nullable final Direction facing, final Matrix4f mat) {
        for (final Direction face : Direction.values()) {
            if (rotate(face, mat) == facing) {
                return face;
            }
        }
        return null;
    }

    private static Direction rotate(final Direction facing, final Matrix4f mat) {
        final Vec3i dir = facing.getNormal();
        final Vector4f vec = new Vector4f(dir.getX(), dir.getY(), dir.getZ(), 1);
        vec.transform(mat);
        return Direction.getNearest(vec.x(), vec.y(), vec.z());
    }

    public static List<BakedQuad> translate(final List<BakedQuad> quads, final Vector3f translation) {
        final Transformation transformation = new Transformation(translation, null, null, null);

        final List<BakedQuad> translated = new ArrayList<>(quads.size());

        for (final BakedQuad quad : quads) {
            final BakedQuadBuilder builder = new BakedQuadBuilder(quad.getSprite());
            final TRSRTransformer transformer = new TRSRTransformer(builder, transformation.blockCenterToCorner());

            quad.pipe(transformer);

            translated.add(builder.build());
        }

        return Collections.unmodifiableList(translated);
    }
}
