package org.auioc.mods.ahutils.utils.game;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import javax.annotation.Nullable;
import io.netty.channel.local.LocalAddress;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.NetworkManager;

public interface AddrUtils {

    @Nullable
    static String getIp(NetworkManager connection) {
        SocketAddress addr = connection.getRemoteAddress();
        if (addr instanceof InetSocketAddress) {
            return ((InetSocketAddress) addr).getAddress().getHostAddress();
        } else if (addr instanceof LocalAddress) {
            return ((LocalAddress) addr).toString();
        }
        return null;
    }

    @Nullable
    static String getPlayerIp(ServerPlayerEntity player) {
        return getIp(player.connection.getConnection());
    }


}
