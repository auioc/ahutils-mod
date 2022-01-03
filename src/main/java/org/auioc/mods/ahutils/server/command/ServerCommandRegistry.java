package org.auioc.mods.ahutils.server.command;

import static net.minecraft.commands.Commands.literal;
import java.util.List;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.CommandNode;
import org.auioc.mods.ahutils.AHUtils;
import org.auioc.mods.ahutils.server.command.impl.VersionCommand;
import net.minecraft.commands.CommandSourceStack;

public class ServerCommandRegistry {

    public static CommandNode<CommandSourceStack> getRootNode(CommandDispatcher<CommandSourceStack> dispatcher) {
        CommandNode<CommandSourceStack> node = dispatcher.findNode(List.of("ah"));
        if (node == null) {
            node = dispatcher.register(literal("ah"));
        }
        return node;
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        getRootNode(dispatcher).addChild(
            literal(AHUtils.MOD_ID).executes((ctx) -> VersionCommand.getModVersion(ctx, AHUtils.MAIN_VERSION, AHUtils.FULL_VERSION, AHUtils.MOD_NAME))
                .build()
        );
    }

}
