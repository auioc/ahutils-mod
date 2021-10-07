package org.auioc.mods.ahutils.server.command;

import static net.minecraft.command.Commands.argument;
import static net.minecraft.command.Commands.literal;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import org.auioc.mods.ahutils.AhUtils;
import org.auioc.mods.ahutils.common.command.argument.DamageSourceArgument;
import org.auioc.mods.ahutils.server.addrlimiter.AddrlimiterCommandHandler;
import net.minecraft.command.CommandSource;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.GameProfileArgument;

public class ServerCommandRegistry {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
            literal(AhUtils.MOD_ID)
                .executes(ctx -> Command.SINGLE_SUCCESS)

                .then(
                    literal("crash")
                        .requires(source -> source.hasPermission(4))
                        .then(
                            literal("client")
                                .then(
                                    argument("targets", GameProfileArgument.gameProfile())
                                        .executes(ctx -> ServerCommandHandler.triggerClientCrash(ctx, 0))
                                        .then(literal("debug").executes(ctx -> ServerCommandHandler.triggerClientCrash(ctx, 0)))
                                        .then(literal("exit").executes(ctx -> ServerCommandHandler.triggerClientCrash(ctx, 1)))
                                        .then(literal("unsafe").executes(ctx -> ServerCommandHandler.triggerClientCrash(ctx, 2)))
                                )
                        )
                )

                .then(
                    literal("hurt")
                        .requires(source -> source.hasPermission(3))
                        .then(
                            argument("targets", EntityArgument.entities())
                                .then(
                                    argument("source", DamageSourceArgument.damageSource())
                                        .then(
                                            argument("damage", FloatArgumentType.floatArg(0F)).executes(ServerCommandHandler::hurtEntity)
                                        )
                                )

                        )
                )

                .then(
                    literal("addrlimiter")
                        .requires(source -> source.hasPermission(3))
                        .then(
                            literal("dump")
                                .then(literal("json").executes(ctx -> AddrlimiterCommandHandler.dumpAddrlimiterMap(ctx, 1)))
                                .then(literal("list").executes(ctx -> AddrlimiterCommandHandler.dumpAddrlimiterMap(ctx, 2)))
                                .then(
                                    literal("file")
                                        .requires(source -> (source.getEntity() == null))
                                        .executes(ctx -> AddrlimiterCommandHandler.dumpAddrlimiterMap(ctx, 3))
                                )
                        )
                        .then(
                            literal("refresh")
                                .requires(source -> source.hasPermission(4))
                                .executes(AddrlimiterCommandHandler::refreshAddrlimiter)
                        )
                )
        );
    }

}
