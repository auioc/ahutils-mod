package org.auioc.mods.ahutils.mixin.server;

import java.util.ArrayList;
import java.util.List;
import com.mojang.datafixers.util.Pair;
import org.auioc.mods.ahutils.server.event.ServerEventFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

@Mixin(value = LivingEntity.class)
public class MixinLivingEntity {

    // @org.spongepowered.asm.mixin.Debug(export = true, print = true)
    @Overwrite()
    private void addEatEffect(ItemStack stack, Level level, LivingEntity entity) {
        if (!level.isClientSide() && stack.isEdible()) {
            List<MobEffectInstance> effects = new ArrayList<>();

            for (Pair<MobEffectInstance, Float> pair : stack.getItem().getFoodProperties().getEffects()) {
                if (pair.getFirst() != null && level.random.nextFloat() < pair.getSecond()) {
                    effects.add(new MobEffectInstance(pair.getFirst()));
                }
            }

            effects = ServerEventFactory.fireLivingEatAddEffectEvent(entity, stack, effects);

            for (MobEffectInstance instance : effects) {
                entity.addEffect(instance);
            }
        }
    }

}
