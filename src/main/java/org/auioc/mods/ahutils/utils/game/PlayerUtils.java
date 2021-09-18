package org.auioc.mods.ahutils.utils.game;

import javax.annotation.Nullable;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public interface PlayerUtils {

    @SuppressWarnings("deprecation")
    static void giveItem(ServerPlayerEntity player, Item item, @Nullable CompoundNBT nbt, int count) {
        int i = count;
        while (i > 0) {
            int j = Math.min(item.getMaxStackSize(), i);
            i -= j;
            ItemStack itemStack = ItemUtils.createItemStack(item, nbt, j);
            boolean flag = player.inventory.add(itemStack);
            ItemEntity itementity = player.drop(itemStack, false);
            if (flag && itemStack.isEmpty()) {
                itemStack.setCount(1);
                if (itementity != null) {
                    itementity.makeFakeItem();
                }
                player.inventoryMenu.broadcastChanges();
            } else {
                if (itementity != null) {
                    itementity.setNoPickUpDelay();
                    itementity.setOwner(player.getUUID());
                }
            }
        }
    }

    static void giveItem(ServerPlayerEntity player, ItemStack itemStack) {
        giveItem(player, itemStack.getItem(), itemStack.getTag(), itemStack.getCount());
    }

    static String toString(PlayerEntity player) {
        return String.format(
            "%s(%s) at %s in %s",
            player.getName().getString(),
            player.getStringUUID(),
            player.position().toString(),
            (player.level == null) ? "~NULL~" : player.level.toString()
        );
    }

    static EntityRayTraceResult getEntityRayTraceResult(PlayerEntity player, double rayLength) {
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
            level, player, from, to, player.getBoundingBox().expandTowards(to).inflate(1.0D), EntityPredicates.NO_SPECTATORS
        );

        return rayHitEntity;
    }

}
