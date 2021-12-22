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
import net.minecraft.network.chat.TranslatableComponent;

public final class AddrlimiterCommandHandler {

    private static final Marker marker = LogUtil.getMarker("AddrLimiter");

    private static final String getKey(String key) {
        return "ahutils.addrlimiter.command." + key;
    }

    private static final TranslatableComponent getI18nText(String key) {
        return TextUtils.getI18nText(getKey(key));
    }

    private static final MutableComponent prefix() {
        return TextUtils.getStringText("").append(TextUtils.getStringText("[AddrLimiter] ").withStyle(ChatFormatting.AQUA));
    }

    public static final SimpleCommandExceptionType NOT_ENABLED = new SimpleCommandExceptionType(prefix().append(getI18nText("not_enabled")));


    public static final int dumpAddrlimiterMap(CommandContext<CommandSourceStack> ctx, int mode) throws CommandSyntaxException {
        if (!AddrHandler.isEnabled()) {
            throw NOT_ENABLED.create();
        }
        CommandSourceStack source = ctx.getSource();
        AddrManager addrManager = AddrManager.getInstance();
        if (mode == 1 || mode == 2) {
            Component message = (mode == 1) ? addrManager.toJsonText() : addrManager.toChatMessage(source.getServer().getPlayerList());
            if (source.getEntity() != null) {
                source.sendSuccess(((mode == 1) ? prefix() : TextUtils.getStringText("")).append(message), false);
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

    public static final int refreshAddrlimiter(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        if (!AddrHandler.isEnabled()) {
            throw NOT_ENABLED.create();
        }
        CommandSourceStack source = ctx.getSource();
        source.sendSuccess(prefix().append(getI18nText("refresh.start")), true);
        AddrHandler.refreshAddrManager(source.getServer().getPlayerList());
        source.sendSuccess(prefix().append(getI18nText("refresh.success").withStyle(ChatFormatting.GREEN)), true);

        return Command.SINGLE_SUCCESS;
    }

}
