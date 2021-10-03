package org.auioc.mods.ahutils.server.addrlimiter;

import java.io.File;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import org.auioc.mods.ahutils.utils.LogUtil;
import org.auioc.mods.ahutils.utils.java.FileUtils;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public abstract class AddrlimiterCommandHandler {

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
