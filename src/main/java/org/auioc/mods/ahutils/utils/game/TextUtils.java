package org.auioc.mods.ahutils.utils.game;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public interface TextUtils {

    static void chat(PlayerEntity player, String message) {
        player.sendMessage(new StringTextComponent(message), Util.NIL_UUID);
    }

    static void chat(PlayerEntity player, ITextComponent message) {
        player.sendMessage(message, Util.NIL_UUID);
    }

    static StringTextComponent getStringText(String text) {
        return new StringTextComponent(text);
    }

    static TranslationTextComponent getI18nText(String key) {
        return new TranslationTextComponent(key);
    }

    static TranslationTextComponent getI18nText(String key, Object... arguments) {
        return new TranslationTextComponent(key, arguments);
    }

}
