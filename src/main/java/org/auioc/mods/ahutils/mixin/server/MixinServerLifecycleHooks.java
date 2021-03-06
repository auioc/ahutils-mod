package org.auioc.mods.ahutils.mixin.server;

import org.auioc.mods.ahutils.server.event.ServerEventFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.handshake.ClientIntentionPacket;
import net.minecraftforge.server.ServerLifecycleHooks;

@Mixin(value = ServerLifecycleHooks.class)
public abstract class MixinServerLifecycleHooks {

    // @org.spongepowered.asm.mixin.Debug(export = true, print = true)
    @Inject(
        method = "Lnet/minecraftforge/server/ServerLifecycleHooks;handleServerLogin(Lnet/minecraft/network/protocol/handshake/ClientIntentionPacket;Lnet/minecraft/network/Connection;)Z",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/handshake/ClientIntentionPacket;getIntention()Lnet/minecraft/network/ConnectionProtocol;", ordinal = 0),
        require = 1,
        allow = 1,
        cancellable = true
    )
    private static void onServerLogin(final ClientIntentionPacket packet, final Connection manager, CallbackInfoReturnable<Boolean> cir) {
        if (ServerEventFactory.fireServerLoginEvent(packet, manager)) {
            cir.setReturnValue(false);
        }
    }

}
