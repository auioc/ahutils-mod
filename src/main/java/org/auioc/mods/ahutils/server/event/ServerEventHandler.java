package org.auioc.mods.ahutils.server.event;

import org.auioc.mods.ahutils.server.addrlimiter.AddrHandler;
import org.auioc.mods.ahutils.server.command.ServerCommandRegistry;
import org.auioc.mods.ahutils.server.event.impl.ServerLoginEvent;
import org.auioc.mods.ahutils.utils.LogUtil;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.ProtocolType;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ServerEventHandler {

    @SubscribeEvent
    public static void registerCommands(final RegisterCommandsEvent event) {
        ServerCommandRegistry.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onServerLogin(final ServerLoginEvent event) {
        ProtocolType intention = event.getPacket().getIntention();
        if (intention == ProtocolType.STATUS) {
            LogUtil.info(
                "ServerHooks",
                LogUtil.getMarker("ServerListPing"),
                String.format("[%s] <-> InitialHandler has pinged", event.getNetworkManager().getRemoteAddress())
            );
            return;
        }
        if (intention == ProtocolType.LOGIN) {
            AddrHandler.playerAttemptLogin(event);
            return;
        }
    }

    @SubscribeEvent
    public static void playerLoggedIn(final PlayerEvent.PlayerLoggedInEvent event) {
        AddrHandler.playerLogin((ServerPlayerEntity) event.getPlayer());

    }

    @SubscribeEvent
    public static void playerLoggedOut(final PlayerEvent.PlayerLoggedOutEvent event) {
        AddrHandler.playerLogout((ServerPlayerEntity) event.getPlayer());
    }

}
