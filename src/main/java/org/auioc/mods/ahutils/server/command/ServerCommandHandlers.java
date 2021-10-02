package org.auioc.mods.ahutils.server.command;

import java.io.File;
import java.util.Collection;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import org.auioc.mods.ahutils.common.command.argument.DamageSourceArgument;
import org.auioc.mods.ahutils.common.network.PacketHandler;
import org.auioc.mods.ahutils.server.addrlimiter.AddrHandler;
import org.auioc.mods.ahutils.server.addrlimiter.AddrManager;
import org.auioc.mods.ahutils.utils.LogUtil;
import org.auioc.mods.ahutils.utils.java.FileUtils;
import net.minecraft.command.CommandSource;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.GameProfileArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public abstract class ServerCommandHandlers {

    public static int triggerClientCrash(CommandContext<CommandSource> ctx, int mode) throws CommandSyntaxException {
        Collection<GameProfile> targets = GameProfileArgument.getGameProfiles(ctx, "targets");

        for (GameProfile gameprofile : targets) {
            ServerPlayerEntity player = ctx.getSource().getServer().getPlayerList().getPlayer(gameprofile.getId());
            PacketHandler.sendTo(player, new org.auioc.mods.ahutils.client.network.TriggerCrashPacket(mode));

            LogUtil.info(
                String.format(
                    "Send TriggerCrashPacket with mode %d to player %s (%s)",
                    mode, player.getName().getString(), player.getStringUUID()
                )
            );
        }

        return Command.SINGLE_SUCCESS;
    }

    public static int hurtEntity(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
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

    public static int dumpAddrlimiterMap(CommandContext<CommandSource> ctx, int mode) throws CommandSyntaxException {
        if (!AddrHandler.isEnabled()) {
            throw new SimpleCommandExceptionType(new StringTextComponent("§b[AddrLimiter]§r §cNot enabled!")).create();
        }
        CommandSource source = ctx.getSource();
        AddrManager addrManager = AddrManager.getInstance();
        if (mode == 1 || mode == 2) {
            ITextComponent message = (mode == 1) ? addrManager.toJsonText() : addrManager.toChatMessage(source.getServer().getPlayerList());
            if (source.getEntity() != null) {
                source.sendSuccess(new StringTextComponent((mode == 1) ? "§b[AddrLimiter]§r " : "").append(message), false);
            } else {
                LogUtil.info(LogUtil.getMarker("AddrLimiter"), message.getString());
            }
        } else if (mode == 3) {
            try {
                File file = FileUtils.writeText("dumps", "addrlimiter.json", new StringBuffer().append(addrManager.toJsonString()));
                source.sendSuccess(new StringTextComponent("[AddrLimiter] Successful dump data to file: " + file), false);
            } catch (Exception e) {
                throw new SimpleCommandExceptionType(new StringTextComponent("[AddrLimiter] Cannot dump data to file: " + e.getMessage())).create();
            }
        }

        return Command.SINGLE_SUCCESS;
    }

    public static int refreshAddrlimiter(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        if (!AddrHandler.isEnabled()) {
            throw new SimpleCommandExceptionType(new StringTextComponent("§b[AddrLimiter]§r §cNot enabled!")).create();
        }
        CommandSource source = ctx.getSource();
        source.sendSuccess(new StringTextComponent("§b[AddrLimiter]§r Start refreshing..."), true);
        AddrHandler.refreshAddrManager(source.getServer().getPlayerList());
        source.sendSuccess(new StringTextComponent("§b[AddrLimiter]§r §aRefresh successfully."), true);

        return Command.SINGLE_SUCCESS;
    }

}
