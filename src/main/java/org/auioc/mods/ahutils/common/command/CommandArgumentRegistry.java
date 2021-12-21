package org.auioc.mods.ahutils.common.command;

import org.auioc.mods.ahutils.common.command.argument.DamageSourceArgument;
import org.auioc.mods.ahutils.common.command.argument.EntityDamageSourceArgument;
import net.minecraft.commands.synchronization.ArgumentTypes;
import net.minecraft.commands.synchronization.EmptyArgumentSerializer;

public class CommandArgumentRegistry {
    public static void register() {
        ArgumentTypes.register("ahutils:damage_source", DamageSourceArgument.class, new EmptyArgumentSerializer<>(DamageSourceArgument::damageSource));
        ArgumentTypes.register("ahutils:entity_damage_source", EntityDamageSourceArgument.class, new EmptyArgumentSerializer<>(EntityDamageSourceArgument::damageSource));
    }
}
