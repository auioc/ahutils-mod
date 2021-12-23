package org.auioc.mods.ahutils.server.addrlimiter;

import static org.auioc.mods.ahutils.AHUtils.LOGGER;
import java.io.File;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import org.apache.logging.log4j.Marker;
import org.auioc.mods.ahutils.utils.LogUtil;
import org.auioc.mods.ahutils.utils.game.TextUtils;
import org.auioc.mods.ahutils.utils.java.FileUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public final class AddrlimiterCommandHandler {

    private static final Marker marker = LogUtil.getMarker("AddrLimiter");

    private static final String getKey(String key) {
        return "ahutils.addrlimiter.command." + key;
    }

    private static final MutableComponent prefix() {
        return TextUtils.empty().append(TextUtils.getStringText("[AddrLimiter] ").withStyle(ChatFormatting.AQUA));
    }

    private static final MutableComponent getI18nText(String key) {
        return prefix().append(TextUtils.getI18nText(getKey(key)));
    }

    public static final SimpleCommandExceptionType NOT_ENABLED = new SimpleCommandExceptionType(getI18nText("not_enabled"));
    public static final SimpleCommandExceptionType ALREADY_ENABLED = new SimpleCommandExceptionType(getI18nText("already_enabled"));
    public static final SimpleCommandExceptionType ALREADY_DISABLED = new SimpleCommandExceptionType(getI18nText("already_disabled"));

    public static final int dump(CommandContext<CommandSourceStack> ctx, int mode) throws CommandSyntaxException {
        if (!AddrHandler.isEnabled()) {
            throw NOT_ENABLED.create();
        }

        CommandSourceStack source = ctx.getSource();

        AddrManager addrManager = AddrManager.getInstance();

        if (mode == 1 || mode == 2) {
            Component message = (mode == 1) ? addrManager.toJsonText() : addrManager.toChatMessage(source.getServer().getPlayerList());
            if (source.getEntity() != null) {
                source.sendSuccess(((mode == 1) ? prefix() : TextUtils.empty()).append(message), false);
            } else {
                LOGGER.info(marker, message.getString());
            }
        } else if (mode == 3) {
            try {
                File file = FileUtils.writeText("dumps", "addrlimiter.json", new StringBuffer().append(addrManager.toJsonString()));
                source.sendSuccess(prefix().append(TextUtils.getI18nText(getKey("dump.success"), file)), false);
            } catch (Exception e) {
                throw new SimpleCommandExceptionType(prefix().append(TextUtils.getI18nText(getKey("dump.failed"), e.getMessage()))).create();
            }
        }

        return Command.SINGLE_SUCCESS;
    }

    public static final int refresh(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        if (!AddrHandler.isEnabled()) {
            throw NOT_ENABLED.create();
        }

        CommandSourceStack source = ctx.getSource();

        source.sendSuccess(getI18nText("refresh.start"), true);
        AddrHandler.refreshAddrManager(source.getServer().getPlayerList());
        source.sendSuccess(getI18nText("refresh.success").withStyle(ChatFormatting.GREEN), true);

        return Command.SINGLE_SUCCESS;
    }

    public static final int switchStatus(CommandContext<CommandSourceStack> ctx, boolean status) throws CommandSyntaxException {
        CommandSourceStack source = ctx.getSource();

        MutableComponent message;

        if (status) {
            if (AddrHandler.isEnabled()) {
                throw ALREADY_ENABLED.create();
            }
            AddrHandler.enable(source.getServer().getPlayerList());
            message = getI18nText("enable");
        } else {
            if (!AddrHandler.isEnabled()) {
                throw ALREADY_DISABLED.create();
            }
            AddrHandler.disable();
            message = getI18nText("disable");
        }

        source.sendSuccess(message, true);

        return Command.SINGLE_SUCCESS;
    }

}
