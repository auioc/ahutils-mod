package org.auioc.mods.ahutils.server.addrlimiter;

import org.auioc.mods.ahutils.server.config.ServerConfig;
import org.auioc.mods.ahutils.server.event.impl.ServerLoginEvent;
import org.auioc.mods.ahutils.utils.game.AddrUtils;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class AddrHandler {

    private static final boolean enable = ServerConfig.EnableAddrLimiter.get();
    private static final AddrManager limiter = AddrManager.getInstance();

    public static void playerAttemptLogin(final ServerLoginEvent event) {
        /*@formatter:off*/if(!enable){return;}/*@formatter:on*/
        if (!limiter.check(AddrUtils.getIp(event.getNetworkManager()), Util.NIL_UUID)) {
            event.cancel(getMessage());
        }
    }

    public static void playerLogin(final ServerPlayerEntity player) {
        /*@formatter:off*/if(!enable){return;}/*@formatter:on*/
        if (!limiter.check(AddrUtils.getPlayerIp(player), player.getUUID())) {
            player.connection.disconnect((ITextComponent) new StringTextComponent(getMessage()));
        } else {
            limiter.add(AddrUtils.getPlayerIp(player), player.getUUID());
        }
    }

    public static void playerLogout(final ServerPlayerEntity player) {
        /*@formatter:off*/if(!enable){return;}/*@formatter:on*/
        limiter.remove(AddrUtils.getPlayerIp(player), player.getUUID());
    }

    public static void refreshAddrManager(PlayerList playerList) {
        /*@formatter:off*/if(!enable){return;}/*@formatter:on*/
        limiter.clear();
        (playerList.getPlayers()).forEach((player) -> {
            limiter.add(AddrUtils.getPlayerIp(player), player.getUUID());
        });
    }


    public static boolean isEnabled() {
        return enable;
    }

    private static String getMessage() {
        return String.format(
            "The number of players with the same IP address has reached the limit. §4You can only connect %s times with the same IP!",
            limiter.getmaxPlayersPreAddr()
        );
    }

}
