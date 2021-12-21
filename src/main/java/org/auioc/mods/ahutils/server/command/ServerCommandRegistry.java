package org.auioc.mods.ahutils.server.command;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import org.auioc.mods.ahutils.AHUtils;
import org.auioc.mods.ahutils.common.command.argument.DamageSourceArgument;
import org.auioc.mods.ahutils.common.command.argument.EntityDamageSourceArgument;
import org.auioc.mods.ahutils.common.command.argument.IndirectEntityDamageSourceArgument;
import org.auioc.mods.ahutils.server.addrlimiter.AddrlimiterCommandHandler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.GameProfileArgument;

public class ServerCommandRegistry {

    private static final LiteralArgumentBuilder<CommandSourceStack> addrlimiterCommandNode =
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
            );

    private static final LiteralArgumentBuilder<CommandSourceStack> crashCommandNode =
        literal("crash")
            .requires(source -> source.hasPermission(4)).then(
                literal("client").then(
                    argument("targets", GameProfileArgument.gameProfile())
                        .executes(ctx -> ServerCommandHandler.triggerClientCrash(ctx, 0))
                        .then(literal("debug").executes(ctx -> ServerCommandHandler.triggerClientCrash(ctx, 0)))
                        .then(literal("exit").executes(ctx -> ServerCommandHandler.triggerClientCrash(ctx, 1)))
                )
            );

    private static final LiteralArgumentBuilder<CommandSourceStack> hurtCommandNode =
        literal("hurt")
            .requires(source -> source.hasPermission(3)).then(
                argument("targets", EntityArgument.entities()).then(
                    argument("damage", FloatArgumentType.floatArg(0F))
                        .then(
                            literal("common").then(
                                argument("source", DamageSourceArgument.damageSource())
                                    .executes(ctx -> ServerCommandHandler.hurtEntity(ctx, 1))
                            )
                        )
                        .then(
                            literal("entity")
                                .then(
                                    argument("source", EntityDamageSourceArgument.damageSource()).then(
                                        argument("from", EntityArgument.entity())
                                            .executes(ctx -> ServerCommandHandler.hurtEntity(ctx, 2))
                                    )
                                )
                        )
                        .then(
                            literal("indirectEntity").then(
                                argument("source", IndirectEntityDamageSourceArgument.damageSource()).then(
                                    argument("from", EntityArgument.entity())
                                        .executes(ctx -> ServerCommandHandler.hurtEntity(ctx, 3))
                                        .then(
                                            argument("owner", EntityArgument.entity())
                                                .executes(ctx -> ServerCommandHandler.hurtEntity(ctx, 4))
                                        )
                                )

                            )
                        )
                )
            );

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            literal(AHUtils.MOD_ID)
                .executes(ServerCommandHandler::getModVersion)
                .then(literal("version").executes(ServerCommandHandler::getModVersion))
                .then(addrlimiterCommandNode)
                .then(crashCommandNode)
                .then(hurtCommandNode)

        );
    }

}
