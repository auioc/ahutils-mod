package org.auioc.mods.ahutils.server.event;

import org.auioc.mods.ahutils.server.addrlimiter.AddrHandler;
import org.auioc.mods.ahutils.server.command.ServerCommandRegistry;
import org.auioc.mods.ahutils.server.event.impl.ServerLoginEvent;
import org.auioc.mods.ahutils.utils.LogUtil;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.server.level.ServerPlayer;
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
        ConnectionProtocol intention = event.getPacket().getIntention();
        if (intention == ConnectionProtocol.STATUS) {
            LogUtil.info(
                "ServerHooks",
                LogUtil.getMarker("ServerListPing"),
                String.format("[%s] <-> InitialHandler has pinged", event.getNetworkManager().getRemoteAddress())
            );
            return;
        }
        if (intention == ConnectionProtocol.LOGIN) {
            AddrHandler.playerAttemptLogin(event);
            return;
        }
    }

    @SubscribeEvent
    public static void playerLoggedIn(final PlayerEvent.PlayerLoggedInEvent event) {
        AddrHandler.playerLogin((ServerPlayer) event.getPlayer());

    }

    @SubscribeEvent
    public static void playerLoggedOut(final PlayerEvent.PlayerLoggedOutEvent event) {
        AddrHandler.playerLogout((ServerPlayer) event.getPlayer());
    }

}
