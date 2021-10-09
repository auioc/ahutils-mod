package org.auioc.mods.ahutils.server.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class ServerConfig {
    public static ForgeConfigSpec CONFIG;

    public static BooleanValue ClientCrashCmdServerOnly;

    public static BooleanValue EnableAddrLimiter;
    public static IntValue MaxPlayerPreAddr;
    public static BooleanValue BypassLocalAddress;
    public static BooleanValue BypassLanAddress;

    static {
        ForgeConfigSpec.Builder b = new ForgeConfigSpec.Builder();

        {
            b.push("command").comment("Restart is required");
            {
                b.push("crash");
                {
                    b.push("client");
                    ClientCrashCmdServerOnly = b.define("server_only", true);
                    b.pop();
                }
                b.pop();
            }
            b.pop();
        }

        {
            b.push("addrlimiter").comment("Restart is required");
            EnableAddrLimiter = b.define("enable", true);
            MaxPlayerPreAddr = b.defineInRange("max_player_pre_address", 1, 1, Integer.MAX_VALUE);
            BypassLocalAddress = b.define("bypass_local_address", true);
            BypassLanAddress = b.define("bypass_lan_address", true);
            b.pop();
        }

        CONFIG = b.build();
    }
}
