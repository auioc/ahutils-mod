package org.auioc.mods.ahutils.utils.game;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

@Deprecated(since = "3.1.3")
public interface I18nUtils {
    static Component getTranslatedText(String key) {
        return new TranslatableComponent(key);
    }

    static Component getTranslatedText(String key, Object... arguments) {
        return new TranslatableComponent(key, arguments);
    }
}
