package org.auioc.mods.ahutils.server.addrlimiter;

import org.auioc.mods.ahutils.server.event.impl.ServerLoginEvent;
import org.auioc.mods.ahutils.utils.game.AddrUtils;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class AddrHandler {

    private static AddrHandler instance;

    private AddrHandler() {}

    public static AddrHandler getInstance() {
        if (AddrHandler.instance == null) {
            AddrHandler.instance = new AddrHandler();
        }
        return AddrHandler.instance;
    }

    private static final AddrManager limiter = AddrManager.getInstance();
    private static final boolean enable = true;

    public void playerAttemptLogin(final ServerLoginEvent event) {
        /*@formatter:off*/if(!enable){return;}/*@formatter:on*/
        if (!limiter.check(AddrUtils.getIp(event.getNetworkManager()), Util.NIL_UUID)) {
            event.cancel(getMessage());
        }
    }

    public void playerLogin(final ServerPlayerEntity player) {
        /*@formatter:off*/if(!enable){return;}/*@formatter:on*/
        if (!limiter.check(AddrUtils.getPlayerIp(player), player.getUUID())) {
            player.connection.disconnect((ITextComponent) new StringTextComponent(getMessage()));
        } else {
            limiter.add(AddrUtils.getPlayerIp(player), player.getUUID());
        }
    }

    public void playerLogout(final ServerPlayerEntity player) {
        /*@formatter:off*/if(!enable){return;}/*@formatter:on*/
        limiter.remove(AddrUtils.getPlayerIp(player), player.getUUID());
    }

    public boolean refreshAddrManager(PlayerList playerList) {
        /*@formatter:off*/if(!enable){return false;}/*@formatter:on*/
        limiter.clear();
        (playerList.getPlayers()).forEach((player) -> {
            limiter.add(AddrUtils.getPlayerIp(player), player.getUUID());
        });
        return true;
    }


    private static String getMessage() {
        return String.format(
            "The number of players with the same IP address has reached the limit. §4You can only connect %s times with the same IP!",
            limiter.getmaxPlayersPreAddr()
        );
    }

}
