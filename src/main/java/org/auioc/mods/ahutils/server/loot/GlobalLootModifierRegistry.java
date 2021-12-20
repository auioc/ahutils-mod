package org.auioc.mods.ahutils.server.loot;

import org.auioc.mods.ahutils.AHUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class GlobalLootModifierRegistry {

    @SubscribeEvent
    public static void registerModifierSerializer(RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
        IForgeRegistry<GlobalLootModifierSerializer<?>> registry = event.getRegistry();

        registry.register(
            (new HLootInjector.Serializer()).setRegistryName(new ResourceLocation(AHUtils.MOD_ID, "loot_injector"))
        );
    }

}
