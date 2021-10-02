package org.auioc.mods.ahutils.server.addrlimiter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.google.gson.Gson;
import org.auioc.mods.ahutils.utils.java.NetUtils;

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

    public void add(String addr, UUID uuid) {
        if (bypassLocalAddress && NetUtils.isLocalAddress(addr)) {
            return;
        }
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
            if ((this.map.get(addr)).size() > (maxPlayersPreAddr - 1)) {
                return false;
            }
        }
        return true;
    }


    public String toJsonString() {
        return (new Gson()).toJson(this.map);
    }

    public int getmaxPlayersPreAddr() {
        return maxPlayersPreAddr;
    }

}
