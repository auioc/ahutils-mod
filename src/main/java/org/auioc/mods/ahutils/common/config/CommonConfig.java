package org.auioc.mods.ahutils.common.config;

import java.util.ArrayList;
import java.util.List;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class CommonConfig {
    public static ForgeConfigSpec CONFIG;


    public static BooleanValue EnablePhysicsExcalibur;
    public static BooleanValue PhysicsExcaliburCreativeOnly;


    public static BooleanValue LightBlockDefaultStateVisible;
    public static IntValue LightBlockDefaultStateLevel;

    public static ConfigValue<List<? extends String>> DeloggerBasicFilter;
    public static ConfigValue<List<? extends String>> DeloggerRegexFilter;

    static {
        ForgeConfigSpec.Builder b = new ForgeConfigSpec.Builder();

        {
            b.push("utils");

            {
                b.push("delogger");

                {
                    b.push("filter");
                    DeloggerBasicFilter = b.define("basic", new ArrayList<String>());
                    DeloggerRegexFilter = b.define("regex", new ArrayList<String>());
                    b.pop();
                }

                b.pop();
            }

            b.pop();
        }

        CONFIG = b.build();
    }
}

