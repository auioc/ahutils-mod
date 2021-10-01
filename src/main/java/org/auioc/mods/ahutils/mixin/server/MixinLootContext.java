package org.auioc.mods.ahutils.mixin.server;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.loot.LootContext;
import net.minecraft.util.ResourceLocation;

@Mixin(value = LootContext.class)
public abstract class MixinLootContext {

    // @org.spongepowered.asm.mixin.Debug(export = true, print = true)
    @Inject(
        method = "Lnet/minecraft/loot/LootContext;setQueriedLootTableId(Lnet/minecraft/util/ResourceLocation;)V",
        at = @At(value = "HEAD"),
        require = 1,
        cancellable = true,
        remap = false
    )
    private void setQueriedLootTableId(ResourceLocation queriedLootTableId, CallbackInfo ci) {
        ci.cancel();
        this.queriedLootTableId = queriedLootTableId;
    }


    @Shadow
    private ResourceLocation queriedLootTableId;

}
