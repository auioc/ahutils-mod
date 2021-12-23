package org.auioc.mods.ahutils.server.command.impl;

import static net.minecraft.commands.Commands.literal;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import org.auioc.mods.ahutils.server.addrlimiter.AddrlimiterCommandHandler;
import net.minecraft.commands.CommandSourceStack;

public class AddrlimiterCommand {

    public static final LiteralArgumentBuilder<CommandSourceStack> BUILDER =
        literal("addrlimiter")
            .requires(source -> source.hasPermission(3))
            .then(literal("enable").executes(ctx -> AddrlimiterCommandHandler.switchStatus(ctx, true)))
            .then(literal("disable").executes(ctx -> AddrlimiterCommandHandler.switchStatus(ctx, false)))
            .then(
                literal("dump")
                    .then(literal("json").executes(ctx -> AddrlimiterCommandHandler.dump(ctx, 1)))
                    .then(literal("list").executes(ctx -> AddrlimiterCommandHandler.dump(ctx, 2)))
                    .then(
                        literal("file")
                            .requires(source -> (source.getEntity() == null))
                            .executes(ctx -> AddrlimiterCommandHandler.dump(ctx, 3))
                    )
            )
            .then(
                literal("refresh")
                    .requires(source -> source.hasPermission(4))
                    .executes(AddrlimiterCommandHandler::refresh)
            );

}
