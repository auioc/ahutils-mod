package org.auioc.mods.ahutils.server.command;

import java.util.Collection;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.auioc.mods.ahutils.common.command.argument.DamageSourceArgument;
import org.auioc.mods.ahutils.common.network.PacketHandler;
import org.auioc.mods.ahutils.server.config.ServerConfig;
import org.auioc.mods.ahutils.utils.LogUtil;
import org.auioc.mods.ahutils.utils.game.CommandUtils;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;

public abstract class ServerCommandHandler {

    private static final boolean ClientCrashCmdServerOnly = ServerConfig.ClientCrashCmdServerOnly.get();

    public static int triggerClientCrash(CommandContext<CommandSourceStack> ctx, int mode) throws CommandSyntaxException {
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

    public static int hurtEntity(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        Collection<? extends Entity> targets = EntityArgument.getEntities(ctx, "targets");
        DamageSource source = DamageSourceArgument.getDamageSource(ctx, "source");
        float damage = FloatArgumentType.getFloat(ctx, "damage");

        for (Entity entity : targets) {
            LogUtil.info(
                String.format(
                    "Entity %s has been hurt by the hurt command, %s, damage: %f",
                    entity.toString(), source.toString(), damage
                )
            );

            entity.hurt(source, damage);
        }

        return Command.SINGLE_SUCCESS;
    }

}