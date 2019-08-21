package com.vincentmet.customquests.lib.handlers;

import com.vincentmet.customquests.BaseClass;
import com.vincentmet.customquests.Objects;
import com.vincentmet.customquests.items.ItemQuestingDevice;
import com.vincentmet.customquests.lib.Ref;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Ref.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EventHandler {
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event){
        event.getRegistry().registerAll(
                Objects.Items.itemQuestingDevice = new ItemQuestingDevice(new Item.Properties().maxStackSize(1).group(Objects.ItemGroups.tabCustomQuests)).setRegistryName("item_questing_device")
        );
    }

    @SubscribeEvent
    public static void registerContainer(final RegistryEvent.Register<ContainerType<?>> event) {

    }

    @SubscribeEvent
    public static void registerTileEntity(final RegistryEvent.Register<TileEntityType<?>> event) {

    }
}