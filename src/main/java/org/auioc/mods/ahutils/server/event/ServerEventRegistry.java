package org.auioc.mods.ahutils.server.event;

import static org.auioc.mods.ahutils.AHUtils.LOGGER;
import java.util.UUID;
import org.apache.logging.log4j.Marker;
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

    private static final Marker marker = LogUtil.getMarker("ServerHooks");

    // Return true if the event was Cancelable cancelled

    public static boolean postServerLoginEvent(final ClientIntentionPacket packet, final Connection manager) {
        ServerLoginEvent event = new ServerLoginEvent(packet, manager);
        boolean cancelled = MinecraftForge.EVENT_BUS.post(event);
        if (cancelled) {
            TextComponent message = new TextComponent(event.getMessage());
            manager.send(new ClientboundLoginDisconnectPacket(message));
            manager.disconnect(message);
            LOGGER.info(
                LogUtil.getMarker("ServerLogin").addParents(marker),
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
