package org.auioc.mods.ahutils.utils.game;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

@Deprecated(since = "3.1.3")
public interface I18nUtils {
    static ITextComponent getTranslatedText(String key) {
        return new TranslationTextComponent(key);
    }

    static ITextComponent getTranslatedText(String key, Object... arguments) {
        return new TranslationTextComponent(key, arguments);
    }
}
