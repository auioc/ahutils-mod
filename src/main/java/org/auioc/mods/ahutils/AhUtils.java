package org.auioc.mods.ahutils;

import java.net.URL;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import org.auioc.mods.ahutils.utils.LogUtil;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(AhUtils.MOD_ID)
@SuppressWarnings("unused")
public class AhUtils {
    public static final String MOD_ID = "ahutils";
    public static String MAIN_VERSION = "";
    public static String FULL_VERSION = "";

    public AhUtils() {
        try {
            String pth = getClass().getResource(getClass().getSimpleName() + ".class").toString();
            Attributes attrs = new Manifest(new URL(pth.substring(0, pth.lastIndexOf("!") + 1) + "/META-INF/MANIFEST.MF").openStream()).getMainAttributes();
            MAIN_VERSION = attrs.getValue("Implementation-Version");
            FULL_VERSION = attrs.getValue("AHUtils-Version");
            LogUtil.warn("[AHUtils] Version: " + MAIN_VERSION + " (" + FULL_VERSION + ")");
        } catch (Exception e) {
            LogUtil.warn("[AHUtils] MANIFEST.MF could not be read. If this is a development environment you can ignore this message.");
        }

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, org.auioc.mods.ahutils.common.config.CommonConfig.CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, org.auioc.mods.ahutils.client.config.ClientConfig.CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, org.auioc.mods.ahutils.server.config.ServerConfig.CONFIG);

        org.auioc.mods.ahutils.utils.delogger.Delogger.init();

        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        final IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

        final ClientSideOnlySetup ClientSideOnlySetup = new ClientSideOnlySetup(modEventBus, forgeEventBus);

        modSetup(modEventBus);
        forgeSetup(forgeEventBus);
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientSideOnlySetup::modSetup);
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientSideOnlySetup::forgeSetup);
    }

    private void modSetup(final IEventBus modEventBus) {
        modEventBus.register(org.auioc.mods.ahutils.common.network.PacketHandler.class);
        org.auioc.mods.ahutils.common.command.CommandArgumentRegistry.register();
        modEventBus.register(org.auioc.mods.ahutils.server.loot.GlobalLootModifierRegistry.class);
    }

    private void forgeSetup(final IEventBus forgeEventBus) {
        org.auioc.mods.ahutils.common.itemgroup.ItemGroupRegistry.register();
        forgeEventBus.register(org.auioc.mods.ahutils.server.event.ServerEventHandler.class);
        forgeEventBus.register(org.auioc.mods.ahutils.common.event.CommonEventHandler.class);
    }

    public class ClientSideOnlySetup {
        private final IEventBus modEventBus;
        private final IEventBus forgeEventBus;

        public ClientSideOnlySetup(IEventBus modEventBus, IEventBus forgeEventBus) {
            this.modEventBus = modEventBus;
            this.forgeEventBus = forgeEventBus;
        }

        public void modSetup() {}

        public void forgeSetup() {
            forgeEventBus.register(org.auioc.mods.ahutils.client.event.ClientEventHandler.class);
        }
    }
}
