package org.auioc.mods.ahutils.common.config;

import java.util.List;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class CommonConfig {

    public static ForgeConfigSpec CONFIG;

    public static ConfigValue<String> GeoIP2ApiUrl;
    public static ConfigValue<List<? extends String>> GeoIP2AvailableDatabases;

    static {
        ForgeConfigSpec.Builder b = new ForgeConfigSpec.Builder();

        {
            b.push("utils");

            {
                b.push("ip_lookup");
                {
                    b.push("geoip2");
                    GeoIP2ApiUrl = b.define("api_url", "http://127.0.0.1:80/geoip2/");
                    GeoIP2AvailableDatabases = b.define("available_databases", List.of("asn", "country", "city"));
                    b.pop();
                }
                b.pop();
            }

            b.pop();
        }

        CONFIG = b.build();
    }

}
