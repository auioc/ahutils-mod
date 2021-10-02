package org.auioc.mods.ahutils.server.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ServerConfig {
    public static ForgeConfigSpec CONFIG;

    public static ForgeConfigSpec.BooleanValue EnableAddrLimiter;
    public static ForgeConfigSpec.IntValue MaxPlayerPreAddr;
    public static ForgeConfigSpec.BooleanValue BypassLocalAddress;

    static {
        ForgeConfigSpec.Builder b = new ForgeConfigSpec.Builder();

        {
            b.push("addrlimiter").comment("Restart is required");
            EnableAddrLimiter = b.define("enable", true);
            MaxPlayerPreAddr = b.defineInRange("max_player_pre_address", 1, 1, Integer.MAX_VALUE);
            BypassLocalAddress = b.define("bypass_local_address", true);
            b.pop();
        }

        CONFIG = b.build();
    }
}
