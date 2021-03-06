package org.auioc.mods.ahutils.client.event.handler;

import com.mojang.blaze3d.platform.InputConstants;
import org.auioc.mods.ahutils.client.config.ClientConfig;
import org.lwjgl.glfw.GLFW;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

@OnlyIn(Dist.CLIENT)
public class TooltipEventHandler {

    private static Minecraft mc = Minecraft.getInstance();

    public static void handle(ItemTooltipEvent event) {
        if (!ClientConfig.EnableAdvancedTooltip.get()) {
            return;
        }

        ItemStack itemStack = event.getItemStack();

        if (itemStack.isEmpty()) {
            return;
        }

        if (itemStack.hasTag()) {
            CompoundTag nbt = itemStack.getTag();
            Component nbtTooltip = new TextComponent("NBT:").setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_GRAY))
                .append(new TextComponent("").setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE)).append(NbtUtils.toPrettyComponent(nbt)));
            addLine(event, nbtTooltip);
        }

        if ((itemStack.getItem().getTags()).size() > 0) {
            addLine(event, new TextComponent("Tags:").setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_GRAY)));
            for (ResourceLocation tag : itemStack.getItem().getTags()) {
                addLine(event, new TextComponent("    " + tag).setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_GRAY)));
            }
        }

    }

    private static void addLine(ItemTooltipEvent event, Component tooltip) {
        if (ClientConfig.AdvancedTooltipOnlyOnDebug.get() && !isDebugMode()) {
            return;
        }
        if (ClientConfig.AdvancedTooltipOnlyOnShift.get() && !isShiftKeyDown()) {
            return;
        }
        event.getToolTip().add(tooltip);
    }

    private static boolean isDebugMode() {
        return mc.options.advancedItemTooltips;
    }

    private static boolean isShiftKeyDown() {
        return InputConstants.isKeyDown(mc.getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT) ||
            InputConstants.isKeyDown(mc.getWindow().getWindow(), GLFW.GLFW_KEY_RIGHT_SHIFT);
    }

}
