package org.auioc.mods.ahutils.server.command;

import static net.minecraft.commands.Commands.literal;
import com.mojang.brigadier.CommandDispatcher;
import org.auioc.mods.ahutils.AHUtils;
import org.auioc.mods.ahutils.server.command.impl.VersionCommand;
import net.minecraft.commands.CommandSourceStack;

public class ServerCommandRegistry {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            literal(AHUtils.MOD_ID)
                .executes(VersionCommand::getModVersion)
                .then(literal("version").executes(VersionCommand::getModVersion))
        );
    }

}
