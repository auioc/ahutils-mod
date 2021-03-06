package org.auioc.mods.ahutils.utils.game;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public interface EntityUtils {

    static Vec3[] getEntityViewRay(Entity entity, double rayLength) {
        Vec3 entityViewVector = entity.getViewVector(1.0F);
        Vec3 rayPath = entityViewVector.scale(rayLength);
        Vec3 from = entity.getEyePosition(1.0F);
        Vec3 to = from.add(rayPath);
        return new Vec3[] {from, to};
    }

    @Nullable
    static EntityHitResult getEntityHitResult(Level level, Entity entity, Vec3 from, Vec3 to, AABB aabb, Predicate<Entity> predicate, float pickRadiusAddition) {
        double d0 = Double.MAX_VALUE;
        Entity targetEntity = null;

        for (Entity entity1 : level.getEntities(targetEntity, aabb, predicate)) {
            AABB entity1aabb = entity1.getBoundingBox().inflate((double) (entity1.getPickRadius() + pickRadiusAddition));
            Optional<Vec3> optional = entity1aabb.clip(from, to);
            if (optional.isPresent()) {
                double d1 = from.distanceToSqr(optional.get());
                if (d1 < d0) {
                    targetEntity = entity1;
                    d0 = d1;
                }
            }
        }

        return targetEntity == null ? null : new EntityHitResult(targetEntity);
    }



    static BlockHitResult getBlockHitResult(Entity entity, double rayLength, ClipContext.Block blockMode, ClipContext.Fluid fluidMode) {
        Vec3[] viewRay = getEntityViewRay(entity, rayLength);
        ClipContext rayCtx = new ClipContext(viewRay[0], viewRay[1], blockMode, fluidMode, entity);
        return entity.level.clip(rayCtx);
    }

    @Nullable
    static EntityHitResult getEntityHitResult(Entity entity, double rayLength, float pickRadiusAddition, boolean blockMode) {
        Vec3[] viewRay = getEntityViewRay(entity, rayLength);

        Vec3 to = viewRay[1];
        if (blockMode) {
            BlockHitResult rayHitBlock = getBlockHitResult(entity, rayLength, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE);
            if (rayHitBlock.getType() != HitResult.Type.MISS) {
                to = rayHitBlock.getLocation();
            }
        }

        return getEntityHitResult(entity, viewRay[0], to, pickRadiusAddition);
    }



    @Nullable
    static EntityHitResult getEntityHitResult(Entity entity, Vec3 from, Vec3 to, float pickRadiusAddition) {
        return getEntityHitResult(entity.level, entity, from, to, entity.getBoundingBox().expandTowards(to).inflate(1.0D), EntitySelector.NO_SPECTATORS, pickRadiusAddition);
    }

    @Nullable
    static EntityHitResult getEntityHitResult(Entity entity, Vec3 from, Vec3 to) {
        return getEntityHitResult(entity, from, to, 0.0F);
    }


    static BlockHitResult getBlockHitResult(Entity entity, double rayLength) {
        return getBlockHitResult(entity, rayLength, ClipContext.Block.OUTLINE, ClipContext.Fluid.ANY);
    }

    static EntityHitResult getEntityHitResult(Entity entity, double rayLength) {
        return getEntityHitResult(entity, rayLength, 0.0F, false);
    }



    static int rayHitEntityOrBlockOrMiss(
        Entity entity, double rayLength,
        float pickRadiusAddition, ClipContext.Block blockMode, ClipContext.Fluid fluidMode,
        Function<EntityHitResult, Integer> e, Function<BlockHitResult, Integer> b, Function<BlockHitResult, Integer> m
    ) {
        EntityHitResult rayHitEntity = getEntityHitResult(entity, rayLength, pickRadiusAddition, false);
        if (rayHitEntity != null) {
            return e.apply(rayHitEntity);
        }

        BlockHitResult rayHitBlock = getBlockHitResult(entity, rayLength, blockMode, fluidMode);
        if (rayHitBlock.getType() != HitResult.Type.MISS) {
            return b.apply(rayHitBlock);
        } else {
            return m.apply(rayHitBlock);
        }
    }

    static int rayHitEntityOrBlock(Entity entity, double rayLength, Function<EntityHitResult, Integer> e, Function<BlockHitResult, Integer> b) {
        return rayHitEntityOrBlockOrMiss(entity, rayLength, 0.0F, ClipContext.Block.OUTLINE, ClipContext.Fluid.ANY, e, b, m -> 0);
    }

    static int rayHitLivingEntityOrBlock(Entity entity, double rayLength, Function<EntityHitResult, Integer> e, Function<BlockHitResult, Integer> b) {
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
