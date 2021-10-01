package org.auioc.mods.ahutils.mixin.server;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.client.CHandshakePacket;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

@Mixin(value = ServerLifecycleHooks.class)
public abstract class MixinServerLifecycleHooks {

    // @org.spongepowered.asm.mixin.Debug(export = true, print = true)
    @Inject(
        method = "Lnet/minecraftforge/fml/server/ServerLifecycleHooks;handleServerLogin(Lnet/minecraft/network/handshake/client/CHandshakePacket;Lnet/minecraft/network/NetworkManager;)Z",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/network/handshake/client/CHandshakePacket;getIntention()Lnet/minecraft/network/ProtocolType;", ordinal = 0),
        require = 1,
        allow = 1,
        cancellable = true,
        remap = false
    )
    private static void onServerLogin(final CHandshakePacket packet, final NetworkManager manager, CallbackInfoReturnable<Boolean> cir) {
        if (org.auioc.mods.ahutils.server.event.ServerEventRegistry.postServerLoginEvent(packet, manager)) {
            cir.setReturnValue(false);
        }
    }

}
