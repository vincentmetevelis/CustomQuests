package com.vincentmet.customquests;

import com.vincentmet.customquests.items.ItemQuestingDevice;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class Objects {
    public static class Items {
        public static final Item itemQuestingDevice = new ItemQuestingDevice(new Item.Properties().maxStackSize(1).group(Objects.ItemGroups.tabCustomQuests)).setRegistryName("item_questing_device");
    }

    public static class ItemGroups{
        public static final ItemGroup tabCustomQuests = new ItemGroup("tab_customquests") {
            @Override
            public ItemStack createIcon() {
                return new ItemStack(Objects.Items.itemQuestingDevice);
            }
        };
    }

    public static class KeyBindings{
        public static final KeyBinding OPEN_QUESTINGDEVICE = new KeyBinding(
                "Open Questing Device",
                KeyConflictContext.GUI,
                InputMappings.Type.KEYSYM,
                GLFW.GLFW_KEY_GRAVE_ACCENT,
                "CustomQuests"
        );
    }
}
