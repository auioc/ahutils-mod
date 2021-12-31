package org.auioc.mods.ahutils.server.command.impl;

import static net.minecraft.commands.Commands.literal;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import org.auioc.mods.ahutils.utils.game.EffectUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.entity.player.Player;

public class HealCommand {

    public static final LiteralArgumentBuilder<CommandSourceStack> BUILDER =
        literal("heal")
            .requires(source -> source.hasPermission(3))
            .executes(ctx -> heal(ctx.getSource().getPlayerOrException(), false))
            .then(
                literal("removeAllEffect")
                    .executes(ctx -> heal(ctx.getSource().getPlayerOrException(), true))
            );

    private static int heal(Player player, boolean removeAllEffect) {
        player.setHealth((player.getMaxHealth()));
        player.getFoodData().eat(20, 20);

        if (removeAllEffect) {
            player.removeAllEffects();
        } else {
            EffectUtils.removeEffect(player, EffectUtils.IS_NOT_BENEFICIAL);
        }

        return Command.SINGLE_SUCCESS;
    }

}
