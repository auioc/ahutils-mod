package org.auioc.mods.ahutils;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.auioc.mods.ahutils.api.data.Tuple;
import org.auioc.mods.ahutils.client.config.ClientConfig;
import org.auioc.mods.ahutils.client.event.ClientEventHandler;
import org.auioc.mods.ahutils.common.command.CommandArgumentRegistry;
import org.auioc.mods.ahutils.common.itemgroup.ItemGroupRegistry;
import org.auioc.mods.ahutils.common.network.PacketHandler;
import org.auioc.mods.ahutils.server.event.ServerEventHandler;
import org.auioc.mods.ahutils.server.loot.GlobalLootModifierRegistry;
import org.auioc.mods.ahutils.utils.LogUtil;
import org.auioc.mods.ahutils.utils.java.JarUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(AHUtils.MOD_ID)
@SuppressWarnings("unused")
public class AHUtils {

    public static final String MOD_ID = "ahutils";
    public static final String MOD_NAME = "AHUtils";
    public static String MAIN_VERSION;
    public static String FULL_VERSION;

    public static final Logger LOGGER = LogUtil.getNamedLogger("AHUtils");
    private static final Marker CORE = LogUtil.getMarker("CORE");

    public AHUtils() {
        Tuple<String, String> version = JarUtils.getModVersion(getClass(), MOD_NAME);
        MAIN_VERSION = version.getA();
        FULL_VERSION = version.getB();
        LOGGER.info(CORE, "Version: " + MAIN_VERSION + " (" + FULL_VERSION + ")");


        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.CONFIG);

        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        final IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

        modSetup(modEventBus);
        forgeSetup(forgeEventBus);

        final ClientSideOnlySetup ClientSideOnlySetup = new ClientSideOnlySetup(modEventBus, forgeEventBus);
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientSideOnlySetup::modSetup);
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientSideOnlySetup::forgeSetup);
    }

    private void modSetup(final IEventBus modEventBus) {
        modEventBus.register(PacketHandler.class);
        CommandArgumentRegistry.register();
        modEventBus.register(GlobalLootModifierRegistry.class);
    }

    private void forgeSetup(final IEventBus forgeEventBus) {
        ItemGroupRegistry.register();
        forgeEventBus.register(ServerEventHandler.class);
    }


    private class ClientSideOnlySetup {
        private final IEventBus modEventBus;
        private final IEventBus forgeEventBus;

        public ClientSideOnlySetup(final IEventBus modEventBus, final IEventBus forgeEventBus) {
            this.modEventBus = modEventBus;
            this.forgeEventBus = forgeEventBus;
        }

        public void modSetup() {}

        public void forgeSetup() {
            forgeEventBus.register(ClientEventHandler.class);
        }
    }

}
