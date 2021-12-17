package org.auioc.mods.ahutils.utils.game;

import java.util.Random;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.ForgeRegistries;

public interface EnchUtils {
    static Enchantment getEnchantment(String id) {
        return ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation(id));
    }

    static void enchantAll(ListTag enchantments) {
        for (int i = 0; i < enchantments.size(); i++) {
            CompoundTag nbt = enchantments.getCompound(i);
            nbt.putShort("lvl", (short) (nbt.getShort("lvl") + 1));
        }
    }

    static void enchantOne(ListTag enchantments, int index) {
        CompoundTag nbt = enchantments.getCompound(index);
        nbt.putShort("lvl", (short) (nbt.getShort("lvl") + 1));
    }

    static void enchantRandom(ListTag enchantments) {
        int enchCount = enchantments.size();
        int index = (new Random()).nextInt((enchCount - 0) + 1) + 0;
        CompoundTag nbt = enchantments.getCompound(index);
        nbt.putShort("lvl", (short) (nbt.getShort("lvl") + 1));
    }
}
