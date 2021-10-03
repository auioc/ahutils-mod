package org.auioc.mods.ahutils.server.addrlimiter;

import java.io.File;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import org.auioc.mods.ahutils.utils.LogUtil;
import org.auioc.mods.ahutils.utils.game.TextUtils;
import org.auioc.mods.ahutils.utils.java.FileUtils;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public final class AddrlimiterCommandHandler {

    private static final String getKey(String key) {
        return "ahutils.addrlimiter.command." + key;
    }

    private static final TranslationTextComponent getI18nText(String key) {
        return TextUtils.getI18nText(getKey(key));
    }

    private static final IFormattableTextComponent prefix() {
        return TextUtils.getStringText("").append(getI18nText("prefix").withStyle(Style.EMPTY.withColor(TextFormatting.AQUA)));
    }

    public static final SimpleCommandExceptionType NOT_ENABLED = new SimpleCommandExceptionType(prefix().append(getI18nText("not_enabled")));


    public static final int dumpAddrlimiterMap(CommandContext<CommandSource> ctx, int mode) throws CommandSyntaxException {
        if (!AddrHandler.isEnabled()) {
            throw NOT_ENABLED.create();
        }
        CommandSource source = ctx.getSource();
        AddrManager addrManager = AddrManager.getInstance();
        if (mode == 1 || mode == 2) {
            ITextComponent message = (mode == 1) ? addrManager.toJsonText() : addrManager.toChatMessage(source.getServer().getPlayerList());
            if (source.getEntity() != null) {
                source.sendSuccess(((mode == 1) ? prefix() : TextUtils.getStringText("")).append(message), false);
            } else {
                LogUtil.info(LogUtil.getMarker("AddrLimiter"), message.getString());
            }
        } else if (mode == 3) {
            try {
                File file = FileUtils.writeText("dumps", "addrlimiter.json", new StringBuffer().append(addrManager.toJsonString()));
                source.sendSuccess(prefix().append(TextUtils.getI18nText(getKey("dump.success"), file)), false);
            } catch (Exception e) {
                throw new SimpleCommandExceptionType(prefix().append(TextUtils.getI18nText(getKey("dump.failure"), e.getMessage()))).create();
            }
        }

        return Command.SINGLE_SUCCESS;
    }

    public static final int refreshAddrlimiter(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        if (!AddrHandler.isEnabled()) {
            throw NOT_ENABLED.create();
        }
        CommandSource source = ctx.getSource();
        source.sendSuccess(prefix().append(getI18nText("refresh.start")), true);
        AddrHandler.refreshAddrManager(source.getServer().getPlayerList());
        source.sendSuccess(prefix().append(getI18nText("refresh.success").withStyle(Style.EMPTY.withColor(TextFormatting.GREEN))), true);

        return Command.SINGLE_SUCCESS;
    }

}
