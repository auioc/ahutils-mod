package org.auioc.mods.ahutils.server.event;

import java.util.UUID;
import org.auioc.mods.ahutils.server.event.impl.ServerLoginEvent;
import org.auioc.mods.ahutils.server.event.impl.ServerPlayerEntitySendMessageEvent;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.client.CHandshakePacket;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.MinecraftForge;

public class ServerEventRegistry {

    // Return true if the event was Cancelable cancelled

    public static boolean postServerLoginEvent(final CHandshakePacket packet, final NetworkManager manager) {
        return MinecraftForge.EVENT_BUS.post(new ServerLoginEvent(packet, manager));
    }

    public static boolean postServerPlayerEntitySendMessageEvent(ITextComponent message, ChatType type, UUID uuid) {
        return MinecraftForge.EVENT_BUS.post(new ServerPlayerEntitySendMessageEvent(message, type, uuid));
    }

}
