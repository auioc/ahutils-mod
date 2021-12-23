package org.auioc.mods.ahutils.server.command.impl;

import static org.auioc.mods.ahutils.utils.game.TextUtils.emptyText;
import static org.auioc.mods.ahutils.utils.game.TextUtils.getStringText;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import org.auioc.mods.ahutils.AHUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.MutableComponent;

public class VersionCommand {

    public static int getModVersion(CommandContext<CommandSourceStack> ctx) {
        String mainVersion = AHUtils.MAIN_VERSION;
        String fullVersion = AHUtils.FULL_VERSION;

        MutableComponent message = emptyText();
        message.append(getStringText("[AHUtils] ").withStyle(ChatFormatting.AQUA));

        if (mainVersion.equals("0") && fullVersion.equals("0")) {
            message.append(getStringText("Could not read the mod version.").withStyle(ChatFormatting.YELLOW));
            message.append(getStringText("\nIf this is a development environment you can ignore this message.").withStyle(ChatFormatting.GRAY));
        } else {
            message.append(getStringText("Version: " + mainVersion + " (" + fullVersion + ")"));
        }

        ctx.getSource().sendSuccess(message, false);
        return Command.SINGLE_SUCCESS;
    }

}
