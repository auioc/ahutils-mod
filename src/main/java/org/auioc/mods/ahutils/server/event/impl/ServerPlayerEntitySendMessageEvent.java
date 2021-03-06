package org.auioc.mods.ahutils.server.event.impl;

import java.util.UUID;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class ServerPlayerEntitySendMessageEvent extends Event {
    private final Component message;
    private final ChatType chatType;
    private final UUID uuid;

    public ServerPlayerEntitySendMessageEvent(Component message, ChatType chatType, UUID uuid) {
        super();
        this.message = message;
        this.chatType = chatType;
        this.uuid = uuid;
    }

    public Component getMessage() {
        return this.message;
    }

    public ChatType getChatType() {
        return this.chatType;
    }

    public UUID getUuid() {
        return this.uuid;
    }
}
