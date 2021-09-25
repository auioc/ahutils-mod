package org.auioc.mods.ahutils.api.item;

import java.util.function.Supplier;
import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;

public class HItemTier implements IItemTier {

    private int level = 0;
    private int durability = 1;
    private float speed = 1.0F;
    private float attackDamageBonus = 0.0F;
    private int enchantmentValue = 1;
    private LazyValue<Ingredient> repairIngredient = new LazyValue<>(null);

    public HItemTier() {}

    public HItemTier setLevel(int level) {
        this.level = level;
        return this;
    }

    public HItemTier setDurability(int durability) {
        this.durability = durability;
        return this;
    }

    public HItemTier setSpeed(float speed) {
        this.speed = speed;
        return this;
    }

    public HItemTier setAttackDamageBonus(float attackDamageBonus) {
        this.attackDamageBonus = attackDamageBonus;
        return this;
    }

    public HItemTier setEnchantmentValue(int enchantmentValue) {
        this.enchantmentValue = enchantmentValue;
        return this;
    }

    public HItemTier setRepairIngredient(Supplier<Ingredient> repairIngredient) {
        this.repairIngredient = new LazyValue<>(repairIngredient);
        return this;
    }


    @Override
    public int getUses() {
        return this.durability;
    }

    @Override
    public float getSpeed() {
        return this.speed;
    }

    @Override
    public float getAttackDamageBonus() {
        return this.attackDamageBonus;
    }

    @Override
    public int getLevel() {
        return this.level;
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

}
