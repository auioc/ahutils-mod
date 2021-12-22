package org.auioc.mods.ahutils.server.command.impl;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.auioc.mods.ahutils.utils.game.AddrUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;

public class IpLookupCommand {

    public static final LiteralArgumentBuilder<CommandSourceStack> BUILDER =
        literal("ip")
            .requires(source -> source.hasPermission(3))
            .then(
                literal("player").then(
                    argument("player", EntityArgument.player()).executes(ctx -> lookupPLayerIp(ctx, 1))
                )
            );

    private static int lookupPLayerIp(CommandContext<CommandSourceStack> ctx, int mode) throws CommandSyntaxException {
        CommandSourceStack source = ctx.getSource();

        ServerPlayer player = EntityArgument.getPlayer(ctx, "player");
        String ip = AddrUtils.getPlayerIp(player);

        switch (mode) {
            case 1: {
                source.sendSuccess(((MutableComponent) player.getDisplayName()).append(": " + ip), false);
                break;
            }
        }

        return Command.SINGLE_SUCCESS;
    }
}
