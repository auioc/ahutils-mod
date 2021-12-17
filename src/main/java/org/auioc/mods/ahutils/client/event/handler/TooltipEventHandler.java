package org.auioc.mods.ahutils.client.event.handler;

import org.auioc.mods.ahutils.client.config.ClientConfig;
import org.lwjgl.glfw.GLFW;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.InputMappings;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
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
            CompoundNBT nbt = itemStack.getTag();
            ITextComponent nbtTooltip = new StringTextComponent("NBT:").setStyle(Style.EMPTY.withColor(TextFormatting.DARK_GRAY))
                .append(new StringTextComponent("").setStyle(Style.EMPTY.withColor(TextFormatting.WHITE)).append(nbt.getPrettyDisplay()));
            addLine(event, nbtTooltip);
        }

        if ((itemStack.getItem().getTags()).size() > 0) {
            addLine(event, new StringTextComponent("Tags:").setStyle(Style.EMPTY.withColor(TextFormatting.DARK_GRAY)));
            for (ResourceLocation tag : itemStack.getItem().getTags()) {
                addLine(event, new StringTextComponent("    " + tag).setStyle(Style.EMPTY.withColor(TextFormatting.DARK_GRAY)));
            }
        }

    }

    private static void addLine(ItemTooltipEvent event, ITextComponent tooltip) {
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
        return InputMappings.isKeyDown(mc.getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT) ||
            InputMappings.isKeyDown(mc.getWindow().getWindow(), GLFW.GLFW_KEY_RIGHT_SHIFT);
    }

}
