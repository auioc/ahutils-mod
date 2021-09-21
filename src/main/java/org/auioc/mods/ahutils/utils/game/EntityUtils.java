package org.auioc.mods.ahutils.utils.game;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public interface EntityUtils {

    static EntityRayTraceResult getEntityRayTraceResult(PlayerEntity player, double rayLength, double aabbInflate) {
        World level = player.level;

        Vector3d playerViewVector = player.getViewVector(0);
        Vector3d rayPath = playerViewVector.scale(rayLength);

        Vector3d from = player.getEyePosition(0);
        Vector3d to = from.add(rayPath);

        RayTraceContext rayCtx = new RayTraceContext(
            from, to, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, player
        );
        BlockRayTraceResult rayHitBlock = level.clip(rayCtx);
        if (rayHitBlock.getType() != RayTraceResult.Type.MISS) {
            to = rayHitBlock.getLocation();
        }

        EntityRayTraceResult rayHitEntity = ProjectileHelper.getEntityHitResult(
            level, player, from, to, player.getBoundingBox().expandTowards(to).inflate(aabbInflate - ((double) 0.3F)), EntityPredicates.NO_SPECTATORS
        );

        return rayHitEntity;
    }

    static EntityRayTraceResult getEntityRayTraceResult(PlayerEntity player, double rayLength) {
        return getEntityRayTraceResult(player, rayLength, 0.0D);
    }

}
