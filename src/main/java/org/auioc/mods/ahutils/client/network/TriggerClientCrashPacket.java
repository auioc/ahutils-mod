package org.auioc.mods.ahutils.client.network;

import org.auioc.mods.ahutils.api.network.IHPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReport;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent.Context;


public class TriggerClientCrashPacket implements IHPacket {
    private final int mode;

    public TriggerClientCrashPacket(int mode) {
        this.mode = mode;
    }

    @Override
    public void handle(Context ctx) {
        crash(mode);
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeInt(mode);
    }

    public static TriggerClientCrashPacket decode(PacketBuffer buffer) {
        return new TriggerClientCrashPacket(buffer.readInt());
    }

    @OnlyIn(Dist.CLIENT)
    private static void crash(int mode) {
        System.err.println("Debug crash triggered manually by the server. (" + mode + ")");
        switch (mode) {
            case 0: {
                Minecraft.crash(new CrashReport("Debug crash triggered manually by the server", new Throwable()));
                break;
            }
            case 1: {
                System.exit(-1);
                break;
            }
            // case 2: {
            //     Object[] o = null;
            //     while (true) {
            //         o = new Object[] {o};
            //     }
            // }
        }
    }
}