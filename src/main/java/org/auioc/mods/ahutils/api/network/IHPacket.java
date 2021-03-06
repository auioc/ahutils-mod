package org.auioc.mods.ahutils.api.network;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent.Context;

public interface IHPacket {

    void handle(Context ctx);

    void encode(FriendlyByteBuf buffer);

    static <PACKET extends IHPacket> void handle(final PACKET message, Supplier<Context> context) {
        Context ctx = context.get();
        ctx.enqueueWork(() -> message.handle(ctx));
        ctx.setPacketHandled(true);
    }

}
