package org.auioc.mods.ahutils.mixin.server;

import java.util.ArrayList;
import java.util.List;
import com.mojang.datafixers.util.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

@Mixin(value = LivingEntity.class)
public class MixinLivingEntity {

    // @org.spongepowered.asm.mixin.Debug(export = true, print = true)
    @Redirect(
        method = "Lnet/minecraft/world/entity/LivingEntity;eat(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/ItemStack;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;addEatEffect(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;)V",
            ordinal = 0
        ),
        require = 1,
        allow = 1
    )
    private void onEatAddEffect(LivingEntity thisClass, ItemStack food, Level level, LivingEntity entity) {
        if (!level.isClientSide) {
            List<MobEffectInstance> effects = new ArrayList<>();
            for (Pair<MobEffectInstance, Float> pair : food.getItem().getFoodProperties().getEffects()) {
                if (pair.getFirst() != null && level.random.nextFloat() < pair.getSecond()) {
                    effects.add(new MobEffectInstance(pair.getFirst()));
                }
            }
            effects = org.auioc.mods.ahutils.server.event.ServerEventRegistry.postLivingEatAddEffectEvent(entity, food, effects);
            for (MobEffectInstance instance : effects) {
                entity.addEffect(instance);
            }
        }
    }

}
