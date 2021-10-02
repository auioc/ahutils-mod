package org.auioc.mods.ahutils.utils.game;

import java.net.InetSocketAddress;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.NetworkManager;

public interface AddrUtils {

    static String getIp(NetworkManager connection) {
        return ((InetSocketAddress) connection.getRemoteAddress()).getAddress().getHostAddress();
    }

    static String getPlayerIp(ServerPlayerEntity player) {
        return getIp(player.connection.getConnection());
    }


}
