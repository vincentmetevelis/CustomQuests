package com.vincentmet.customquests;

import com.vincentmet.customquests.items.ItemQuestingDevice;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class Objects {
    private Objects(){}
    public static class Items {
        private Items(){}
        public static final Item itemQuestingDevice = new ItemQuestingDevice(new Item.Properties().maxStackSize(1).group(Objects.ItemGroups.tabCustomQuests)).setRegistryName("item_questing_device");
    }

    public static class ItemGroups{
        private ItemGroups(){}
        public static final ItemGroup tabCustomQuests = new ItemGroup("tab_customquests") {
            @Override
            public ItemStack createIcon() {
                return new ItemStack(Objects.Items.itemQuestingDevice);
            }
        };
    }
}
