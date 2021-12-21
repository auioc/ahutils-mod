package org.auioc.mods.ahutils.server.command;

import static org.auioc.mods.ahutils.utils.game.TextUtils.getStringText;
import java.util.Collection;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.auioc.mods.ahutils.AHUtils;
import org.auioc.mods.ahutils.common.command.argument.DamageSourceArgument;
import org.auioc.mods.ahutils.common.network.PacketHandler;
import org.auioc.mods.ahutils.server.config.ServerConfig;
import org.auioc.mods.ahutils.utils.LogUtil;
import org.auioc.mods.ahutils.utils.game.CommandUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;

public abstract class ServerCommandHandler {

    private static final boolean ClientCrashCmdServerOnly = ServerConfig.ClientCrashCmdServerOnly.get();

    public static int getModVersion(CommandContext<CommandSourceStack> ctx) {
        String mainVersion = AHUtils.MAIN_VERSION;
        String fullVersion = AHUtils.FULL_VERSION;

        MutableComponent message = getStringText("");
        message.append(getStringText("[AHUtils] ").withStyle(ChatFormatting.AQUA));

        if (mainVersion.equals("0") && fullVersion.equals("0")) {
            message.append(getStringText("Could not read the mod version.").withStyle(ChatFormatting.YELLOW));
            message.append(getStringText("\nIf this is a development environment you can ignore this message.").withStyle(ChatFormatting.GRAY));
        } else {
            message.append(getStringText("Version: " + mainVersion + " (" + fullVersion + ")"));
        }

        ctx.getSource().sendSuccess(message, false);
        return Command.SINGLE_SUCCESS;
    }

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

    public static int hurtEntity(CommandContext<CommandSourceStack> ctx, int mode) throws CommandSyntaxException {
        Collection<? extends Entity> targets = EntityArgument.getEntities(ctx, "targets");
        float damage = FloatArgumentType.getFloat(ctx, "damage");

        DamageSource source = DamageSource.GENERIC;
        switch (mode) {
            case 1: {
                source = DamageSourceArgument.getDamageSource(ctx, "source");
                break;
            }
        }

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
