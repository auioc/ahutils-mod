package org.auioc.mods.ahutils.server.addrlimiter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.google.gson.Gson;
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
    private static final int maxPlayersPreAddr = 1;
    private static final boolean bypassLocalAddress = true;

    public int getmaxPlayersPreAddr() {
        return maxPlayersPreAddr;
    }

    public void add(String addr, UUID uuid) {
        List<UUID> list = this.map.getOrDefault(addr, new ArrayList<UUID>());
        list.add(uuid);
        this.map.put(addr, list);
        return;
    }

    public void remove(String addr, UUID uuid) {
        if (this.map.containsKey(addr)) {
            List<UUID> list = this.map.get(addr);
            if (list.size() <= 1) {
                this.map.remove(addr);
            } else {
                list.remove(uuid);
                this.map.put(addr, list);
            }
        }
        return;
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
        for (Map.Entry<String, List<UUID>> e : this.map.entrySet()) {
            String addr = e.getKey();
            List<UUID> uuids = e.getValue();
            boolean lastEntry = (entryIndex == map.size() - 1);
            entryIndex++;
            StringTextComponent l = new StringTextComponent("\n  " + (lastEntry ? "┗ " : "┣ ") + addr);
            l.append(new StringTextComponent(" (" + uuids.size() + ")").withStyle(Style.EMPTY.withColor(TextFormatting.GRAY)));
            for (int i = 0; i < uuids.size(); i++) {
                UUID uuid = uuids.get(i);
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
        if (errorOffline > 0) {
            m.append(new StringTextComponent(String.format("\n WARNING: Detected %d non-online players in the list.", errorOffline)).withStyle(Style.EMPTY.withColor(TextFormatting.YELLOW)));
        }
        return m;
    }

}
