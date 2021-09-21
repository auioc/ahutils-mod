package org.auioc.mods.ahutils.utils.game;

import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceContext.BlockMode;
import net.minecraft.util.math.RayTraceContext.FluidMode;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;

public interface EntityUtils {

    static ImmutableMap<String, Vector3d> getEntityViewRay(Entity entity, double rayLength) {
        Vector3d entityViewVector = entity.getViewVector(0);
        Vector3d rayPath = entityViewVector.scale(rayLength);
        Vector3d from = entity.getEyePosition(0);
        Vector3d to = from.add(rayPath);
        return new ImmutableMap.Builder<String, Vector3d>()
            .put("from", from)
            .put("to", to)
            .build();
    }

    static BlockRayTraceResult getBlockRayTraceResult(Entity entity, double rayLength, BlockMode blockMode, FluidMode fluidMode) {
        Map<String, Vector3d> viewRay = getEntityViewRay(entity, rayLength);
        RayTraceContext rayCtx = new RayTraceContext(viewRay.get("from"), viewRay.get("to"), blockMode, fluidMode, entity);
        BlockRayTraceResult rayHitBlock = entity.level.clip(rayCtx);
        return rayHitBlock;
    }

    static EntityRayTraceResult getEntityRayTraceResult(Entity entity, double rayLength, double aabbInflate) {
        Map<String, Vector3d> viewRay = getEntityViewRay(entity, rayLength);

        Vector3d to = viewRay.get("to");
        BlockRayTraceResult rayHitBlock = getBlockRayTraceResult(entity, rayLength, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE);
        if (rayHitBlock.getType() != RayTraceResult.Type.MISS) {
            to = rayHitBlock.getLocation();
        }

        EntityRayTraceResult rayHitEntity = ProjectileHelper.getEntityHitResult(
            entity.level, entity, viewRay.get("from"), to, entity.getBoundingBox().expandTowards(to).inflate(aabbInflate - ((double) 0.3F)), EntityPredicates.NO_SPECTATORS
        );

        return rayHitEntity;
    }

    static BlockRayTraceResult getBlockRayTraceResult(Entity entity, double rayLength) {
        return getBlockRayTraceResult(entity, rayLength, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.ANY);
    }

    static EntityRayTraceResult getEntityRayTraceResult(Entity entity, double rayLength) {
        return getEntityRayTraceResult(entity, rayLength, 0.0D);
    }

}
