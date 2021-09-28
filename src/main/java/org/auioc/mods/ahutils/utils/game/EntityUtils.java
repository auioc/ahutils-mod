package org.auioc.mods.ahutils.utils.game;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
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

    public static Vector3d[] getEntityViewRay(Entity entity, double rayLength) {
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



    public static BlockRayTraceResult getBlockRayTraceResult(Entity entity, double rayLength, BlockMode blockMode, FluidMode fluidMode) {
        Vector3d[] viewRay = getEntityViewRay(entity, rayLength);
        RayTraceContext rayCtx = new RayTraceContext(viewRay[0], viewRay[1], blockMode, fluidMode, entity);
        return entity.level.clip(rayCtx);
    }

    @Nullable
    public static EntityRayTraceResult getEntityRayTraceResult(Entity entity, double rayLength, float pickRadiusAddition) {
        Vector3d[] viewRay = getEntityViewRay(entity, rayLength);

        Vector3d to = viewRay[1];
        BlockRayTraceResult rayHitBlock = getBlockRayTraceResult(entity, rayLength, BlockMode.COLLIDER, FluidMode.NONE);
        if (rayHitBlock.getType() != RayTraceResult.Type.MISS) {
            to = rayHitBlock.getLocation();
        }

        return getEntityHitResult(entity, viewRay[0], to, pickRadiusAddition);
    }



    @Nullable
    public static EntityRayTraceResult getEntityHitResult(Entity entity, Vector3d from, Vector3d to, float pickRadiusAddition) {
        return getEntityHitResult(entity.level, entity, from, to, entity.getBoundingBox().expandTowards(to).inflate(1.0D), EntityPredicates.NO_SPECTATORS, pickRadiusAddition);
    }

    @Nullable
    public static EntityRayTraceResult getEntityHitResult(Entity entity, Vector3d from, Vector3d to) {
        return getEntityHitResult(entity, from, to, 0.0F);
    }

    public static BlockRayTraceResult getBlockRayTraceResult(Entity entity, double rayLength) {
        return getBlockRayTraceResult(entity, rayLength, BlockMode.OUTLINE, FluidMode.ANY);
    }

    public static EntityRayTraceResult getEntityRayTraceResult(Entity entity, double rayLength) {
        return getEntityRayTraceResult(entity, rayLength, 0.0F);
    }



    public static int rayHitEntityOrBlockOrMiss(
        Entity entity, double rayLength,
        float pickRadiusAddition, BlockMode blockMode, FluidMode fluidMode,
        Function<EntityRayTraceResult, Integer> e, Function<BlockRayTraceResult, Integer> b, Function<BlockRayTraceResult, Integer> m
    ) {
        EntityRayTraceResult rayHitEntity = getEntityRayTraceResult(entity, rayLength, pickRadiusAddition);
        if (rayHitEntity != null) {
            return e.apply(rayHitEntity);
        }

        BlockRayTraceResult rayHitBlock = getBlockRayTraceResult(entity, rayLength, blockMode, fluidMode);
        if (rayHitBlock.getType() != RayTraceResult.Type.MISS) {
            return b.apply(rayHitBlock);
        } else {
            return m.apply(rayHitBlock);
        }
    }

    public static int rayHitEntityOrBlock(Entity entity, double rayLength, Function<EntityRayTraceResult, Integer> e, Function<BlockRayTraceResult, Integer> b) {
        return rayHitEntityOrBlockOrMiss(entity, rayLength, 0.0F, BlockMode.OUTLINE, FluidMode.ANY, e, b, m -> 0);
    }

    public static int rayHitLivingEntityOrBlock(Entity entity, double rayLength, Function<EntityRayTraceResult, Integer> e, Function<BlockRayTraceResult, Integer> b) {
        return rayHitEntityOrBlock(
            entity, rayLength,
            (r) -> {
                if (r.getEntity() instanceof LivingEntity) {
                    return e.apply(r);
                } else {
                    return 0;
                }
            }, b
        );
    }

}
