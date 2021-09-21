package org.auioc.mods.ahutils.utils.game;

import java.util.Optional;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceContext.BlockMode;
import net.minecraft.util.math.RayTraceContext.FluidMode;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public interface EntityUtils {

    static Vector3d[] getEntityViewRay(Entity entity, double rayLength) {
        Vector3d entityViewVector = entity.getViewVector(1.0F);
        Vector3d rayPath = entityViewVector.scale(rayLength);
        Vector3d from = entity.getEyePosition(1.0F);
        Vector3d to = from.add(rayPath);
        return new Vector3d[] {from, to};
    }

    @Nullable
    public static EntityRayTraceResult getEntityHitResult(World world, Entity entity, Vector3d from, Vector3d to, AxisAlignedBB aabb, Predicate<Entity> predicate, float pickRadiusAddition) {
        double d0 = Double.MAX_VALUE;
        Entity targetEntity = null;

        for (Entity entity1 : world.getEntities(targetEntity, aabb, predicate)) {
            AxisAlignedBB entity1aabb = entity1.getBoundingBox().inflate((double) (entity1.getPickRadius() + pickRadiusAddition));
            Optional<Vector3d> optional = entity1aabb.clip(from, to);
            if (optional.isPresent()) {
                double d1 = from.distanceToSqr(optional.get());
                if (d1 < d0) {
                    targetEntity = entity1;
                    d0 = d1;
                }
            }
        }

        return targetEntity == null ? null : new EntityRayTraceResult(targetEntity);
    }



    static BlockRayTraceResult getBlockRayTraceResult(Entity entity, double rayLength, BlockMode blockMode, FluidMode fluidMode) {
        Vector3d[] viewRay = getEntityViewRay(entity, rayLength);
        RayTraceContext rayCtx = new RayTraceContext(viewRay[0], viewRay[1], blockMode, fluidMode, entity);
        BlockRayTraceResult rayHitBlock = entity.level.clip(rayCtx);
        return rayHitBlock;
    }

    static EntityRayTraceResult getEntityRayTraceResult(Entity entity, double rayLength) {
        Vector3d[] viewRay = getEntityViewRay(entity, rayLength);

        Vector3d to = viewRay[1];
        BlockRayTraceResult rayHitBlock = getBlockRayTraceResult(entity, rayLength, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE);
        if (rayHitBlock.getType() != RayTraceResult.Type.MISS) {
            to = rayHitBlock.getLocation();
        }

        EntityRayTraceResult rayHitEntity = ProjectileHelper.getEntityHitResult(
            entity.level, entity, viewRay[0], to, entity.getBoundingBox().expandTowards(to).inflate(1.0D), EntityPredicates.NO_SPECTATORS
        );

        return rayHitEntity;
    }

    static BlockRayTraceResult getBlockRayTraceResult(Entity entity, double rayLength) {
        return getBlockRayTraceResult(entity, rayLength, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.ANY);
    }

}
