package org.auioc.mods.ahutils.server.command.impl;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;
import static org.auioc.mods.ahutils.utils.game.TextUtils.getStringText;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.auioc.mods.ahutils.api.exception.HSimpleException;
import org.auioc.mods.ahutils.utils.game.AddrUtils;
import org.auioc.mods.ahutils.utils.network.AddressUtils;
import org.auioc.mods.ahutils.utils.network.IpLookup;
import net.minecraft.ChatFormatting;
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
                    argument("player", EntityArgument.player()).executes(ctx -> h(ctx, false, false, false))
                        .then(
                            literal("+asn").executes(ctx -> h(ctx, true, false, false))
                                .then(
                                    literal("+country").executes(ctx -> h(ctx, true, true, false))
                                        .then(literal("+city").executes(ctx -> h(ctx, true, true, true)))
                                )
                                .then(literal("+city").executes(ctx -> h(ctx, true, false, true)))
                        )
                        .then(
                            literal("+country").executes(ctx -> h(ctx, false, true, false))
                                .then(literal("+city").executes(ctx -> h(ctx, false, true, true)))
                        )
                        .then(literal("+city").executes(ctx -> h(ctx, false, false, true)))
                )
            );

    private static int h(CommandContext<CommandSourceStack> ctx, boolean asn, boolean country, boolean city) throws CommandSyntaxException {
        CommandSourceStack source = ctx.getSource();

        ServerPlayer player = EntityArgument.getPlayer(ctx, "player");
        String ip = AddrUtils.getPlayerIp(player);

        MutableComponent message = getStringText("").append(player.getDisplayName()).append(": " + ip + " ");

        if (AddressUtils.isLocalAddress(ip)) {
            message.append("(Local)");
        } else if (AddressUtils.isLanAddress(ip)) {
            message.append("(LAN)");
        } else {
            if (!(asn | country | city)) {
            } else {
                message.append("(");
                try {
                    if (asn) {
                        message.append("ASN: " + IpLookup.lookupAsn(ip, true) + (country | city ? ", " : ""));
                    }
                    if (country) {
                        message.append("Country: " + IpLookup.lookupCountry(ip, true) + (city ? ", " : ""));
                    }
                    if (city) {
                        message.append("City: ");
                        try {
                            message.append(IpLookup.lookupCity(ip, true));
                        } catch (HSimpleException e) {
                            message.append(getStringText("Unknown").withStyle(ChatFormatting.ITALIC));
                        }
                    }
                } catch (Exception e) {
                    source.sendFailure(getStringText("Lookup IP location failed: " + e.getMessage()));
                    return Command.SINGLE_SUCCESS;
                }
                message.append(")");
            }
        }

        source.sendSuccess(message, false);

        return Command.SINGLE_SUCCESS;
    }
}
