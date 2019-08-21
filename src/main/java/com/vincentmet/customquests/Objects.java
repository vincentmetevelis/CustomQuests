package com.vincentmet.customquests;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class Objects {
    public static class Items {
        public static Item itemQuestingDevice;
    }

    public static class ItemGroups{
        public static final ItemGroup tabCustomQuests = new ItemGroup("tab_customquests") {
            @Override
            public ItemStack createIcon() {
                return new ItemStack(Objects.Items.itemQuestingDevice);
            }
        };
    }

    public static class Containers{

    }

    public static class TileEntities{

    }
}
