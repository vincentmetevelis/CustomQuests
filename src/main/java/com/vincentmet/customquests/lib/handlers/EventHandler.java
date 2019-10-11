package com.vincentmet.customquests.lib.handlers;

import com.google.gson.JsonObject;
import com.vincentmet.customquests.Objects;
import com.vincentmet.customquests.items.ItemQuestingDevice;
import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.lib.Triple;
import com.vincentmet.customquests.lib.Utils;
import com.vincentmet.customquests.lib.converters.ConverterHelper;
import com.vincentmet.customquests.quests.*;
import javafx.util.Pair;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.registries.ForgeRegistries;

import javax.rmi.CORBA.Util;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Ref.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EventHandler {//todo check if onCrafted event exists and check for item in inventory in the world tick eventhandler
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event){
        event.getRegistry().registerAll(
                Objects.Items.itemQuestingDevice = new ItemQuestingDevice(new Item.Properties().maxStackSize(1).group(Objects.ItemGroups.tabCustomQuests)).setRegistryName("item_questing_device")
        );
    }

    @SubscribeEvent
    public static void onItemCraft(PlayerEvent.ItemCraftedEvent event){
        List<Triple<Integer, Integer, Integer>> activeItemCraftQuestIds = Quest.getActiveQuestsWithType(Utils.getUUID("vincentmet"), QuestRequirementType.CRAFTING_DETECT);

        for(Triple<Integer, Integer, Integer> questAndReqAndSubReqId : activeItemCraftQuestIds){
            ItemStack itemStack = Quest.getItemstackForCraftingDetect(questAndReqAndSubReqId.getLeft(), questAndReqAndSubReqId.getMiddle(), questAndReqAndSubReqId.getRight());
            if(event.getCrafting().getItem() == itemStack.getItem()/* && event.getCrafting().getTag() == itemStack.getTag()*/){
                QuestUserProgress.addPlayerProgress(Utils.getUUID("vincentmet"), questAndReqAndSubReqId.getLeft(), questAndReqAndSubReqId.getMiddle(), questAndReqAndSubReqId.getRight(), event.getCrafting().getCount());
                Ref.shouldSaveNextTick = true;
            }
        }
    }

    @SubscribeEvent
    public static void onItemPickup(PlayerEvent.ItemPickupEvent event){
        List<Triple<Integer, Integer, Integer>> itemsToCheckFor = Quest.getActiveQuestsWithType(Utils.getUUID("vincentmet"), QuestRequirementType.ITEM_DETECT);
        if(itemsToCheckFor.contains(event.getStack())){
            //todo set player questing progress to complete for the quest
        }
    }

    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event){
        if(Ref.shouldSaveNextTick){
            JsonHandler.writeJson();
            Ref.shouldSaveNextTick = false;
        }

        if(event.world.getGameTime() % 20 == 0){//todo take into account the gamerule doDaylightCycle
            for(Quest quest : Ref.ALL_QUESTS){
                if(QuestUserProgress.areAllRequirementsCompleted(Utils.getUUID("vincentmet"), quest.getId())){
                    for(QuestUserProgress userprogress : Ref.ALL_QUESTING_PROGRESS){
                        if(userprogress.getUuid().equals(Utils.getUUID("vincentmet"))){
                            userprogress.addCompletedQuest(quest.getId());
                            Ref.shouldSaveNextTick = true;
                        }
                    }
                }
            }

            for(PlayerEntity playerEntity : event.world.getPlayers()){
                List<Triple<Integer, Integer, Integer>> activeLocationTrackingQuestIds = Quest.getActiveQuestsWithType(Utils.getUUID("vincentmet"), QuestRequirementType.TRAVEL_TO/*playerEntity.getUniqueID().toString().replace("-", "")*/);
                for(Triple<Integer, Integer, Integer> questAndReqAndSubReqId : activeLocationTrackingQuestIds){
                    Triple<String, BlockPos, Integer> dimPosRadius = Quest.getDimPosRadius(questAndReqAndSubReqId.getLeft(), questAndReqAndSubReqId.getMiddle(), questAndReqAndSubReqId.getRight());
                    if(Quest.isPlayerInRadius(playerEntity, dimPosRadius)){
                        playerEntity.sendMessage(new TranslationTextComponent("In radius of Quest: " + questAndReqAndSubReqId.getLeft() + "; Requirement: " + questAndReqAndSubReqId.getMiddle() + "; Subrequirement: " + questAndReqAndSubReqId.getRight() + "; @ " + dimPosRadius));
                        QuestUserProgress.setPlayerProgressToCompleted(Utils.getUUID("vincentmet"), questAndReqAndSubReqId.getLeft(), questAndReqAndSubReqId.getMiddle(), questAndReqAndSubReqId.getRight());
                    }
                }
            }
        }
    }
}