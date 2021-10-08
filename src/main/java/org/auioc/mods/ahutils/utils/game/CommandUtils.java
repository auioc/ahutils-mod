package org.auioc.mods.ahutils.utils.game;

import java.lang.reflect.Field;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.ICommandSource;
import net.minecraft.util.text.TranslationTextComponent;

public interface CommandUtils {

    TranslationTextComponent INTERNAL_ERROR_MESSAGE = TextUtils.getI18nText("ahutils.command.failed.internal");

    SimpleCommandExceptionType INTERNAL_ERROR = new SimpleCommandExceptionType(INTERNAL_ERROR_MESSAGE);
    SimpleCommandExceptionType NOT_SERVER_ERROR = new SimpleCommandExceptionType(TextUtils.getI18nText("ahutils.command.failed.not_server"));
    SimpleCommandExceptionType NOT_DEDICATED_SERVER_ERROR = new SimpleCommandExceptionType(TextUtils.getI18nText("ahutils.command.failed.not_dedicated_server"));
    SimpleCommandExceptionType REFLECTION_ERROR = new SimpleCommandExceptionType(TextUtils.getI18nText("ahutils.command.failed.reflection"));

    static ICommandSource getPrivateSource(CommandSource source) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Field privateSourceField = CommandSource.class.getDeclaredField("source");
        privateSourceField.setAccessible(true);
        return (ICommandSource) privateSourceField.get(source);
    }

}
