package com.vincentmet.customquests.lib.handlers;

import com.vincentmet.customquests.Objects;
import com.vincentmet.customquests.items.ItemQuestingDevice;
import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.lib.Triple;
import com.vincentmet.customquests.quests.*;
import javafx.util.Pair;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Ref.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EventHandler {
    private static long lastMillis = System.currentTimeMillis();
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event){
        event.getRegistry().registerAll(
                Objects.Items.itemQuestingDevice = new ItemQuestingDevice(new Item.Properties().maxStackSize(1).group(Objects.ItemGroups.tabCustomQuests)).setRegistryName("item_questing_device")
        );
    }

    @SubscribeEvent
    public static void onItemCraft(PlayerEvent.ItemCraftedEvent event){
        List<Triple<Integer, Integer, Integer>> activeItemCraftQuestIds = Quest.getActiveQuestsWithType(event.getPlayer().getUniqueID().toString().replaceAll("-", ""), QuestRequirementType.CRAFTING_DETECT);

        for(Triple<Integer, Integer, Integer> questAndReqAndSubReqId : activeItemCraftQuestIds){
            ItemStack itemStack = Quest.getItemstackForCraftingDetect(questAndReqAndSubReqId.getLeft(), questAndReqAndSubReqId.getMiddle(), questAndReqAndSubReqId.getRight());
            if(event.getCrafting().getItem() == itemStack.getItem() && event.getCrafting().getTag() == itemStack.getTag()){
                QuestUserProgress.addPlayerProgress(event.getPlayer().getUniqueID().toString().replaceAll("-", ""), questAndReqAndSubReqId.getLeft(), questAndReqAndSubReqId.getMiddle(), questAndReqAndSubReqId.getRight(), event.getCrafting().getCount());
            }
        }
    }

    @SubscribeEvent
    public static void onEntityKill(LivingDeathEvent event){
        if(event.getSource().getTrueSource() instanceof PlayerEntity) {
            List<Triple<Integer, Integer, Integer>> activeEntityKillQuestIds = Quest.getActiveQuestsWithType(event.getSource().getTrueSource().getUniqueID().toString().replaceAll("-", ""), QuestRequirementType.KILL_MOB);
            for (Triple<Integer, Integer, Integer> questAndReqAndSubReqId : activeEntityKillQuestIds) {
                Pair<EntityType, Integer> mobAmount = Quest.getMobAmountForMobKill(questAndReqAndSubReqId.getLeft(), questAndReqAndSubReqId.getMiddle(), questAndReqAndSubReqId.getRight());
                if (event.getEntity().getType() == mobAmount.getKey()) {
                    if (event.getSource().getTrueSource() instanceof PlayerEntity) {
                        QuestUserProgress.addPlayerProgress(event.getSource().getTrueSource().getUniqueID().toString().replaceAll("-", ""), questAndReqAndSubReqId.getLeft(), questAndReqAndSubReqId.getMiddle(), questAndReqAndSubReqId.getRight(), 1);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event){
        if(!event.getPlayer().inventory.hasItemStack(new ItemStack(Objects.Items.itemQuestingDevice))){
            event.getPlayer().inventory.addItemStackToInventory(new ItemStack(Objects.Items.itemQuestingDevice, 1));
        }

        if(QuestUserProgress.getUserProgressForUuid(event.getPlayer().getUniqueID().toString().replaceAll("-", ""))==null){
            Ref.ALL_QUESTING_PROGRESS.add(new QuestUserProgress(event.getPlayer().getUniqueID().toString().replaceAll("-", ""), new ArrayList<>(), new ArrayList<>()));
            Ref.shouldSaveNextTick = true;
        }
    }

    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event){
        if(Ref.shouldSaveNextTick){
            JsonHandler.writeJson();
            Ref.shouldSaveNextTick = false;
        }

        if(event.world.getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE)){
            if(event.world.getGameTime() % 20 == 0){
                executeWorldTickLogic(event.world);
            }
        }else{
            if(System.currentTimeMillis() > lastMillis + 1000){
                lastMillis = System.currentTimeMillis();
                executeWorldTickLogic(event.world);
            }
        }
    }
    private static void executeWorldTickLogic(World world){ //Separate method to avoid code duplication
        for(PlayerEntity playerEntity : world.getPlayers()){
            List<Triple<Integer, Integer, Integer>> activeLocationTrackingQuestIds = Quest.getActiveQuestsWithType(playerEntity.getUniqueID().toString().replaceAll("-", ""), QuestRequirementType.TRAVEL_TO);
            for(Triple<Integer, Integer, Integer> questAndReqAndSubReqId : activeLocationTrackingQuestIds){
                Triple<String, BlockPos, Integer> dimPosRadius = Quest.getDimPosRadius(questAndReqAndSubReqId.getLeft(), questAndReqAndSubReqId.getMiddle(), questAndReqAndSubReqId.getRight());
                if(Quest.isPlayerInRadius(playerEntity, dimPosRadius)){
                    QuestUserProgress.setPlayerProgressToCompleted(playerEntity.getUniqueID().toString().replaceAll("-", ""), questAndReqAndSubReqId.getLeft(), questAndReqAndSubReqId.getMiddle(), questAndReqAndSubReqId.getRight());
                }
            }

            List<Triple<Integer, Integer, Integer>> activeItemDetectQuestIds = Quest.getActiveQuestsWithType(playerEntity.getUniqueID().toString().replaceAll("-", ""), QuestRequirementType.ITEM_DETECT);
            for(Triple<Integer, Integer, Integer> questAndReqAndSubReqId : activeItemDetectQuestIds){
                ItemStack itemStack = Quest.getItemstackForItemDetect(questAndReqAndSubReqId.getLeft(), questAndReqAndSubReqId.getMiddle(), questAndReqAndSubReqId.getRight());
                int correctItemInInvCount = 0;
                for(ItemStack mainInventoryStack : playerEntity.inventory.mainInventory){
                    if(mainInventoryStack.getItem() == itemStack.getItem()){
                        correctItemInInvCount += mainInventoryStack.getCount();
                    }
                }
                if(itemStack.getCount() <= correctItemInInvCount){
                    QuestUserProgress.setPlayerProgressToCompleted(playerEntity.getUniqueID().toString().replaceAll("-", ""), questAndReqAndSubReqId.getLeft(), questAndReqAndSubReqId.getMiddle(), questAndReqAndSubReqId.getRight());
                }
            }
            for(Quest quest : Ref.ALL_QUESTS){
                if(QuestUserProgress.areAllRequirementsCompleted(playerEntity.getUniqueID().toString().replaceAll("-", ""), quest.getId())){
                    for(QuestUserProgress userprogress : Ref.ALL_QUESTING_PROGRESS){
                        if(userprogress.getUuid().equals(playerEntity.getUniqueID().toString().replaceAll("-", ""))){
                            userprogress.addCompletedQuest(quest.getId(), world, playerEntity);
                            Ref.shouldSaveNextTick = true;
                        }
                    }
                }
            }
        }
    }
}