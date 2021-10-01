package org.auioc.mods.ahutils.mixin.server;

import java.util.UUID;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;

@Mixin(value = ServerPlayerEntity.class)
public abstract class MixinServerPlayerEntity {

    // @org.spongepowered.asm.mixin.Debug(export = true, print = true)
    @Inject(
        method = "Lnet/minecraft/entity/player/ServerPlayerEntity;sendMessage(Lnet/minecraft/util/text/ITextComponent;Lnet/minecraft/util/text/ChatType;Ljava/util/UUID;)V",
        at = @At(value = "HEAD"),
        require = 1,
        allow = 1,
        cancellable = true,
        remap = false
    )
    private void onSendMessage(ITextComponent p_241151_1_, ChatType p_241151_2_, UUID p_241151_3_, CallbackInfo ci) {
        if (org.auioc.mods.ahutils.server.event.ServerEventRegistry.postServerPlayerEntitySendMessageEvent(p_241151_1_, p_241151_2_, p_241151_3_)) {
            ci.cancel();
        }
    }

}
