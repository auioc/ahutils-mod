package org.auioc.mods.ahutils.server.command.impl;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;
import static org.auioc.mods.ahutils.AHUtils.LOGGER;
import java.util.Collection;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.apache.logging.log4j.Marker;
import org.auioc.mods.ahutils.common.command.argument.DamageSourceArgument;
import org.auioc.mods.ahutils.common.command.argument.EntityDamageSourceArgument;
import org.auioc.mods.ahutils.common.command.argument.IndirectEntityDamageSourceArgument;
import org.auioc.mods.ahutils.utils.LogUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;

public class HurtCommand {

    private static final Marker marker = LogUtil.getMarker("HurtCommand");

    public static final LiteralArgumentBuilder<CommandSourceStack> BUILDER =
        literal("hurt")
            .requires(source -> source.hasPermission(3)).then(
                argument("targets", EntityArgument.entities()).then(
                    argument("damage", FloatArgumentType.floatArg(0F))
                        .then(
                            literal("common").then(
                                argument("source", DamageSourceArgument.damageSource())
                                    .executes(ctx -> hurtEntity(ctx, 1))
                            )
                        )
                        .then(
                            literal("entity")
                                .then(
                                    argument("source", EntityDamageSourceArgument.damageSource()).then(
                                        argument("from", EntityArgument.entity())
                                            .executes(ctx -> hurtEntity(ctx, 2))
                                    )
                                )
                        )
                        .then(
                            literal("indirectEntity").then(
                                argument("source", IndirectEntityDamageSourceArgument.damageSource()).then(
                                    argument("from", EntityArgument.entity())
                                        .executes(ctx -> hurtEntity(ctx, 3))
                                        .then(
                                            argument("owner", EntityArgument.entity())
                                                .executes(ctx -> hurtEntity(ctx, 4))
                                        )
                                )

                            )
                        )
                )
            );

    private static int hurtEntity(CommandContext<CommandSourceStack> ctx, int mode) throws CommandSyntaxException {
        Collection<? extends Entity> targets = EntityArgument.getEntities(ctx, "targets");
        float damage = FloatArgumentType.getFloat(ctx, "damage");

        DamageSource source = DamageSource.GENERIC;
        switch (mode) {
            case 1: {
                source = DamageSourceArgument.getDamageSource(ctx, "source");
                break;
            }
            case 2: {
                Entity from = EntityArgument.getEntity(ctx, "from");
                source = EntityDamageSourceArgument.getDamageSource(ctx, "source").apply(from);
                break;
            }
            case 3: {
                Entity from = EntityArgument.getEntity(ctx, "from");
                source = IndirectEntityDamageSourceArgument.getDamageSource(ctx, "source").apply(from, null);
                break;
            }
            case 4: {
                Entity from = EntityArgument.getEntity(ctx, "from");
                Entity owner = EntityArgument.getEntity(ctx, "owner");
                source = IndirectEntityDamageSourceArgument.getDamageSource(ctx, "source").apply(from, owner);
                break;
            }
        }

        for (Entity entity : targets) {
            LOGGER.info(
                marker,
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
