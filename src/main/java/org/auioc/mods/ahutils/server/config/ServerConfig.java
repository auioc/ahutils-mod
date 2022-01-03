package org.auioc.mods.ahutils.server.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;

public class ServerConfig {
    public static ForgeConfigSpec CONFIG;

    public static BooleanValue ClientCrashCmdServerOnly;

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

        CONFIG = b.build();
    }
}
