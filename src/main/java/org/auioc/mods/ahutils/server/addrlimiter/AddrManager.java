package org.auioc.mods.ahutils.server.addrlimiter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.google.gson.Gson;
import org.auioc.mods.ahutils.server.config.ServerConfig;
import org.auioc.mods.ahutils.utils.java.NetUtils;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

public class AddrManager {

    private static AddrManager instance;

    private AddrManager() {}

    public static AddrManager getInstance() {
        if (AddrManager.instance == null) {
            AddrManager.instance = new AddrManager();
        }
        return AddrManager.instance;
    }

    private final Map<String, List<UUID>> map = Collections.synchronizedMap(new HashMap<String, List<UUID>>());
    private static final int maxPlayersPreAddr = ServerConfig.MaxPlayerPreAddr.get();
    private static final boolean bypassLocalAddress = ServerConfig.BypassLocalAddress.get();

    public int getmaxPlayersPreAddr() {
        return maxPlayersPreAddr;
    }

    protected void add(String addr, UUID uuid) {
        List<UUID> list = this.map.getOrDefault(addr, new ArrayList<UUID>());
        list.add(uuid);
        this.map.put(addr, list);
    }

    protected void remove(String addr, UUID uuid) {
        if (this.map.containsKey(addr)) {
            List<UUID> list = this.map.get(addr);
            if (list.size() <= 1) {
                this.map.remove(addr);
            } else {
                list.remove(uuid);
                this.map.put(addr, list);
            }
        }
    }

    public boolean check(String addr, UUID uuid) {
        if (this.map.containsKey(addr)) {
            if (bypassLocalAddress && NetUtils.isLocalAddress(addr)) {
                return true;
            }
            if ((this.map.get(addr)).size() > (maxPlayersPreAddr - 1)) {
                return false;
            }
        }
        return true;
    }

    protected void clear() {
        this.map.clear();
    }


    public String toJsonString() {
        return (new Gson()).toJson(this.map);
    }

    public StringTextComponent toJsonText() {
        return new StringTextComponent((new Gson()).toJson(this.map));
    }

    public ITextComponent toChatMessage(PlayerList playerList) {
        StringTextComponent m = new StringTextComponent("");

        m.append(new StringTextComponent("[AddrLimiter]").withStyle(Style.EMPTY.withColor(TextFormatting.AQUA)));
        m.append(new StringTextComponent("\n Current player list").withStyle(Style.EMPTY.withColor(TextFormatting.DARK_AQUA)));

        if (map.isEmpty()) {
            return m.append(new StringTextComponent("\n  ┗ No data is recorded in the addrlimiter.").withStyle(Style.EMPTY.withColor(TextFormatting.YELLOW)));
        }

        int entryIndex = 0;
        int errorOffline = 0;
        List<UUID> uuidsAll = new ArrayList<UUID>();
        for (Map.Entry<String, List<UUID>> e : this.map.entrySet()) {
            String addr = e.getKey();
            List<UUID> uuids = e.getValue();

            boolean lastEntry = (entryIndex == map.size() - 1);
            entryIndex++;

            StringTextComponent l = new StringTextComponent("\n  " + (lastEntry ? "┗ " : "┣ ") + addr);
            if (NetUtils.isLocalAddress(addr)) {
                l.append(new StringTextComponent(" (Local)").withStyle(Style.EMPTY.withColor(TextFormatting.GRAY)));
            }
            l.append(new StringTextComponent(" (" + uuids.size() + ")").withStyle(Style.EMPTY.withColor(TextFormatting.GRAY)));

            for (int i = 0; i < uuids.size(); i++) {
                UUID uuid = uuids.get(i);
                uuidsAll.add(uuid);
                ServerPlayerEntity player = playerList.getPlayer(uuid);

                String p = String.format("\n  %s  %s ", (lastEntry ? " " : "┃"), (i == uuids.size() - 1) ? "┗" : "┣");
                if (player != null) {
                    l.append(new StringTextComponent(p).append(player.getDisplayName()));
                } else {
                    l.append(new StringTextComponent(p).append(new StringTextComponent(uuid.toString()).withStyle(Style.EMPTY.withColor(TextFormatting.GRAY).withItalic(true))));
                    errorOffline++;
                }
            }

            m.append(l);
        }

        StringTextComponent e = (StringTextComponent) new StringTextComponent("").withStyle(Style.EMPTY.withColor(TextFormatting.YELLOW));
        if (errorOffline > 0) {
            e.append(new StringTextComponent(String.format("\n WARNING: Detected %d non-online players in the list.", errorOffline)));
        }
        if (uuidsAll.size() > uuidsAll.stream().distinct().count()) {
            e.append(new StringTextComponent(String.format("\n WARNING: Detected %d duplicate players in the list.", uuidsAll.size() - uuidsAll.stream().distinct().count())));
        }
        if (!e.getSiblings().isEmpty()) {
            e.append(new StringTextComponent("\n  (Try to run \"refresh\" command to fix these errors.)").withStyle(Style.EMPTY.withColor(TextFormatting.GREEN)));
        }
        m.append(e);

        return m;
    }

}
