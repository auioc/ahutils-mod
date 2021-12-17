package org.auioc.mods.ahutils.utils.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public interface EffectUtils {

    @Nullable
    static Effect getEffect(int id) {
        return Effect.byId(id);
    }

    @Nullable
    static Effect getEffect(String id) {
        return ForgeRegistries.POTIONS.getValue(new ResourceLocation(id));
    }

    @Nullable
    static Effect getEffect(ResourceLocation id) {
        return ForgeRegistries.POTIONS.getValue(id);
    }


    static List<Effect> getEffects(@Nullable EffectType type) {
        Collection<Effect> effects = ForgeRegistries.POTIONS.getValues();
        List<Effect> effectsList = new ArrayList<>();
        for (Effect effect : effects) {
            if (type == null || effect.getCategory() == type) {
                effectsList.add(effect);
            }
        }
        return effectsList;
    }

    static List<Effect> getHarmfulEffects() {
        return getEffects(EffectType.HARMFUL);
    }

    static List<Effect> getBeneficialEffects() {
        return getEffects(EffectType.BENEFICIAL);
    }

    static List<Effect> getNeutralEffects() {
        return getEffects(EffectType.NEUTRAL);
    }

    static List<Effect> getAllEffects() {
        return getEffects(null);
    }



    static EffectInstance getEffectInstance(Effect effect, int duration, int amplifier, boolean ambient, boolean visible, boolean showIcon) {
        return new EffectInstance(effect, duration, amplifier, ambient, visible, showIcon);
    }

    @Nullable
    static EffectInstance getEffectInstance(ResourceLocation id, int duration, int amplifier, boolean ambient, boolean visible, boolean showIcon) {
        Effect effect = getEffect(id);
        if (effect != null) {
            return getEffectInstance(effect, duration, amplifier, ambient, visible, showIcon);
        }
        return null;
    }

    @Nullable
    static EffectInstance getEffectInstance(String id, int duration, int amplifier, boolean ambient, boolean visible, boolean showIcon) {
        return getEffectInstance(new ResourceLocation(id), duration, amplifier, ambient, visible, showIcon);
    }


    @Nullable
    static EffectInstance getEffectInstance(CompoundNBT effect_nbt) {
        if (effect_nbt.contains("id", 8) && effect_nbt.contains("duration", 3) && effect_nbt.contains("amplifier", 3)) {
            return getEffectInstance(effect_nbt.getString("id"), effect_nbt.getInt("duration"), effect_nbt.getInt("amplifier"), true, true, true);
        }
        return null;
    }



    static void addEffect(LivingEntity entity, int id, int duration, int amplifier) {
        entity.addEffect(new EffectInstance(getEffect(id), duration, amplifier, true, true, true));
    }

    static boolean addEffect(LivingEntity entity, String id, int duration, int amplifier) {
        EffectInstance effect = getEffectInstance(id, duration, amplifier, true, true, true);
        if (effect != null) {
            return entity.addEffect(effect);
        }
        return false;
    }

}
