package org.auioc.mods.ahutils.server.event;

import java.util.UUID;
import org.auioc.mods.ahutils.server.event.impl.ServerLoginEvent;
import org.auioc.mods.ahutils.server.event.impl.ServerPlayerEntitySendMessageEvent;
import org.auioc.mods.ahutils.utils.LogUtil;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.handshake.ClientIntentionPacket;
import net.minecraft.network.protocol.login.ClientboundLoginDisconnectPacket;
import net.minecraftforge.common.MinecraftForge;

public class ServerEventRegistry {

    // Return true if the event was Cancelable cancelled

    public static boolean postServerLoginEvent(final ClientIntentionPacket packet, final Connection manager) {
        ServerLoginEvent event = new ServerLoginEvent(packet, manager);
        boolean cancelled = MinecraftForge.EVENT_BUS.post(event);
        if (cancelled) {
            TextComponent message = new TextComponent(event.getMessage());
            manager.send(new ClientboundLoginDisconnectPacket(message));
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

    public static boolean postServerPlayerEntitySendMessageEvent(Component message, ChatType type, UUID uuid) {
        return MinecraftForge.EVENT_BUS.post(new ServerPlayerEntitySendMessageEvent(message, type, uuid));
    }

}
