package org.auioc.mods.ahutils.utils.game;

import java.lang.reflect.Field;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.TranslatableComponent;

public interface CommandUtils {

    TranslatableComponent INTERNAL_ERROR_MESSAGE = TextUtils.getI18nText("ahutils.command.failed.internal");

    SimpleCommandExceptionType INTERNAL_ERROR = new SimpleCommandExceptionType(INTERNAL_ERROR_MESSAGE);
    SimpleCommandExceptionType NOT_SERVER_ERROR = new SimpleCommandExceptionType(TextUtils.getI18nText("ahutils.command.failed.not_server"));
    SimpleCommandExceptionType NOT_DEDICATED_SERVER_ERROR = new SimpleCommandExceptionType(TextUtils.getI18nText("ahutils.command.failed.not_dedicated_server"));
    SimpleCommandExceptionType REFLECTION_ERROR = new SimpleCommandExceptionType(TextUtils.getI18nText("ahutils.command.failed.reflection"));

    static CommandSource getPrivateSource(CommandSourceStack sourceStack) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Field privateSourceField = CommandSourceStack.class.getDeclaredField("source");
        privateSourceField.setAccessible(true);
        return (CommandSource) privateSourceField.get(sourceStack);
    }

}
