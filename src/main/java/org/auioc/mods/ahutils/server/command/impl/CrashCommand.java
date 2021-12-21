package org.auioc.mods.ahutils.server.command.impl;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;
import java.util.Collection;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.auioc.mods.ahutils.common.network.PacketHandler;
import org.auioc.mods.ahutils.server.config.ServerConfig;
import org.auioc.mods.ahutils.utils.LogUtil;
import org.auioc.mods.ahutils.utils.game.CommandUtils;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class CrashCommand {

    private static final boolean ClientCrashCmdServerOnly = ServerConfig.ClientCrashCmdServerOnly.get();

    public static final LiteralArgumentBuilder<CommandSourceStack> BUILDER =
        literal("crash")
            .requires(source -> source.hasPermission(4)).then(
                literal("client").then(
                    argument("targets", GameProfileArgument.gameProfile())
                        .executes(ctx -> triggerClientCrash(ctx, 0))
                        .then(literal("debug").executes(ctx -> triggerClientCrash(ctx, 0)))
                        .then(literal("exit").executes(ctx -> triggerClientCrash(ctx, 1)))
                )
            );

    private static int triggerClientCrash(CommandContext<CommandSourceStack> ctx, int mode) throws CommandSyntaxException {
        CommandSourceStack source = ctx.getSource();
        if (ClientCrashCmdServerOnly) {
            CommandSource privateSource;
            try {
                privateSource = CommandUtils.getPrivateSource(source);
            } catch (Exception e) {
                throw CommandUtils.REFLECTION_ERROR.create();
            }

            if (!(privateSource instanceof MinecraftServer)) {
                throw CommandUtils.NOT_SERVER_ERROR.create();
            }
        }

        Collection<GameProfile> targets = GameProfileArgument.getGameProfiles(ctx, "targets");
        String sourceName = source.getTextName() + ((source.getEntity() != null) ? "(" + source.getEntity().getStringUUID() + ")" : "");

        for (GameProfile gameprofile : targets) {
            ServerPlayer player = source.getServer().getPlayerList().getPlayer(gameprofile.getId());
            PacketHandler.sendTo(player, new org.auioc.mods.ahutils.client.network.TriggerClientCrashPacket(mode));

            LogUtil.warn(
                LogUtil.getMarker("TriggerClientCrash"), String.format(
                    "Send packet with mode %d to %s(%s) by %s",
                    mode, player.getName().getString(), player.getStringUUID(), sourceName
                )
            );
        }

        return Command.SINGLE_SUCCESS;
    }

}
