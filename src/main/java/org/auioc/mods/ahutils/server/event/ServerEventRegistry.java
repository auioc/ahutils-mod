package org.auioc.mods.ahutils.server.event;

import java.util.UUID;
import org.auioc.mods.ahutils.server.event.impl.ServerLoginEvent;
import org.auioc.mods.ahutils.server.event.impl.ServerPlayerEntitySendMessageEvent;
import org.auioc.mods.ahutils.utils.LogUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.client.CHandshakePacket;
import net.minecraft.network.login.server.SDisconnectLoginPacket;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.MinecraftForge;

public class ServerEventRegistry {

    // Return true if the event was Cancelable cancelled

    public static boolean postServerLoginEvent(final CHandshakePacket packet, final NetworkManager manager) {
        ServerLoginEvent event = new ServerLoginEvent(packet, manager);
        boolean cancelled = MinecraftForge.EVENT_BUS.post(event);
        if (cancelled) {
            StringTextComponent message = new StringTextComponent(event.getMessage());
            manager.send(new SDisconnectLoginPacket(message));
            manager.disconnect(message);
            LogUtil.info(
                "ServerHooks",
                LogUtil.getMarker("ServerLogin"),
                String.format("Disconnecting %s connection attempt from %s: %s", event.getPacket().getIntention(), event.getNetworkManager().getRemoteAddress(), event.getMessage())
            );
            return true;
        }
        return false;
    }

    public static boolean postServerPlayerEntitySendMessageEvent(ITextComponent message, ChatType type, UUID uuid) {
        return MinecraftForge.EVENT_BUS.post(new ServerPlayerEntitySendMessageEvent(message, type, uuid));
    }

}
