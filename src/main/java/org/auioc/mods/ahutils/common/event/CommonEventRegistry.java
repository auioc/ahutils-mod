package org.auioc.mods.ahutils.common.event;

import java.util.List;
import org.auioc.mods.ahutils.common.event.impl.LivingEatAddEffectEvent;
import org.auioc.mods.ahutils.common.event.impl.PistonCheckPushableEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;

public class CommonEventRegistry {

    private static final IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

    @Deprecated(since = "3.1.1")
    public static boolean postPistonAddBlockLineEvent(BlockState blockState, Level level, BlockPos blockPos, Direction direction) {
        return forgeEventBus.post(new org.auioc.mods.ahutils.common.event.impl.PistonAddBlockLineEvent(blockState, level, blockPos, direction));
    }

    public static boolean postPistonCheckPushableEvent(BlockState blockState, Level level, BlockPos blockPos, Direction pushDirection, boolean p_60209_, Direction p_60210_) {
        return forgeEventBus.post(new PistonCheckPushableEvent(blockState, level, blockPos, pushDirection, p_60209_, p_60210_));
    }

    public static boolean postLivingEatAddEffectEvent(LivingEntity entity, ItemStack food, List<MobEffectInstance> effects) {
        return forgeEventBus.post(new LivingEatAddEffectEvent(entity, food, effects));
    }

}
