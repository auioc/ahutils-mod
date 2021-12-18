package org.auioc.mods.ahutils.common.itemgroup;

import org.auioc.mods.ahutils.common.itemgroup.impl.McHiddenItemsGroup;
import net.minecraft.world.item.CreativeModeTab;

public class ItemGroupRegistry {

    public static void register() {}

    // public static ItemGroup ahuItemGroup = new AhUItemGroup();
    public static CreativeModeTab mcHiddenItemsGroup = new McHiddenItemsGroup();

}
