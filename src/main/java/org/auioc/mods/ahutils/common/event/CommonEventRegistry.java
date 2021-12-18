package org.auioc.mods.ahutils.common.event;

import org.auioc.mods.ahutils.common.event.impl.PistonCheckPushableEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;

public class CommonEventRegistry {

    @Deprecated(since = "3.1.1")
    public static boolean postPistonAddBlockLineEvent(BlockState blockState, Level level, BlockPos blockPos, Direction direction) {
        return MinecraftForge.EVENT_BUS.post(new org.auioc.mods.ahutils.common.event.impl.PistonAddBlockLineEvent(blockState, level, blockPos, direction));
    }

    public static boolean postPistonCheckPushableEvent(BlockState blockState, Level level, BlockPos blockPos, Direction pushDirection, boolean p_60209_, Direction p_60210_) {
        return MinecraftForge.EVENT_BUS.post(new PistonCheckPushableEvent(blockState, level, blockPos, pushDirection, p_60209_, p_60210_));
    }

}
