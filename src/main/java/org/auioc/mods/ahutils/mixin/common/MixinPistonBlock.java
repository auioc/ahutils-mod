package org.auioc.mods.ahutils.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.block.BlockState;
import net.minecraft.block.PistonBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(value = PistonBlock.class)
public abstract class MixinPistonBlock {

    // @org.spongepowered.asm.mixin.Debug(export = true, print = true)
    @Inject(
        method = "Lnet/minecraft/block/PistonBlock;isPushable(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/Direction;ZLnet/minecraft/util/Direction;)Z",
        at = @At(value = "HEAD"),
        require = 1,
        allow = 1,
        cancellable = true,
        remap = false
    )
    private static void onCheckPushable(BlockState p_185646_0_, World p_185646_1_, BlockPos p_185646_2_, Direction p_185646_3_, boolean p_185646_4_, Direction p_185646_5_, CallbackInfoReturnable<Boolean> cir) {
        if (org.auioc.mods.ahutils.common.event.CommonEventRegistry.postPistonCheckPushableEvent(p_185646_0_, p_185646_1_, p_185646_2_, p_185646_3_, p_185646_4_, p_185646_5_)) {
            cir.setReturnValue(false);
        }
    }

}
