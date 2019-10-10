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
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

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
        List<ItemStack> itemsToCheckFor = new ArrayList<>(); //todo set this list to all the active quests that require crafting detection, perhaps make this a Pair<> so the questID can be tracked
        if(itemsToCheckFor.contains(event.getCrafting())){
            //todo set player questing progress to complete for the quest
        }
    }

    @SubscribeEvent
    public static void onItemPickup(PlayerEvent.ItemPickupEvent event){
        List<Triple<Integer, Integer, Integer>> itemsToCheckFor = Quest.getActiveQuestsWithType(Utils.getUUID("vincentmet"), QuestRequirementType.ITEM_DETECT); //todo set this list to all the active quests that require item detection, perhaps make this a Pair<> so the questID can be tracked
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
            for(PlayerEntity playerEntity : event.world.getPlayers()){
                List<Triple<Integer, Integer, Integer>> activeLocationTrackingQuestIds = Quest.getActiveQuestsWithType(Utils.getUUID("vincentmet"), QuestRequirementType.TRAVEL_TO/*playerEntity.getUniqueID().toString().replace("-", "")*/);
                for(Triple<Integer, Integer, Integer> questAndReqAndSubReqId : activeLocationTrackingQuestIds){
                    Triple<String, BlockPos, Integer> dimPosRadius = getDimPosRadius(Utils.getUUID("vincentmet"), questAndReqAndSubReqId.getLeft(), questAndReqAndSubReqId.getMiddle(), questAndReqAndSubReqId.getRight());
                    if(isPlayerInRadius(playerEntity, dimPosRadius)){
                        playerEntity.sendMessage(new TranslationTextComponent("In radius of Quest: " + questAndReqAndSubReqId.getLeft() + "; Requirement: " + questAndReqAndSubReqId.getMiddle() + "; Subrequirement: " + questAndReqAndSubReqId.getRight() + "; @ " + dimPosRadius));
                        setPlayerProgressToCompleted(Utils.getUUID("vincentmet"), questAndReqAndSubReqId.getLeft(), questAndReqAndSubReqId.getMiddle(), questAndReqAndSubReqId.getRight(), QuestRequirementType.TRAVEL_TO);
                    }
                }
            }
        }
    }
    //////////////////////////////////To Move
    private static boolean isPlayerInRadius(PlayerEntity player, Triple<String, BlockPos, Integer> dimPosRadius){
        BlockPos qp = dimPosRadius.getMiddle();
        BlockPos pp = player.getPosition();
        int r = dimPosRadius.getRight();
        if(/*todo fix dimension condition*//*player.world.getDimension().getType().toString() == dimPosRadius.getLeft() && */qp.getX() <= pp.getX()+r && qp.getX() >= pp.getX()-r && qp.getY() <= pp.getY()+r && qp.getY() >= pp.getY()-r && qp.getZ() <= pp.getZ()+r && qp.getZ() >= pp.getZ()-r){
            return true;
        }else{
            return false;
        }
    }

    private static void setPlayerProgressToCompleted(String uuid, int questId, int reqId, int subReqId, QuestRequirementType type){
        for(QuestUserProgress userprogress : Ref.ALL_QUESTING_PROGRESS) {
            if(userprogress.getUuid().equals(uuid)) {
                for(QuestStatus status : userprogress.getQuestStatuses()) {
                    if(status.getQuestId() == questId){
                        int reqCount = 0;
                        for(QuestRequirementStatus reqStatus : status.getQuestRequirementStatuses()){
                            if(reqCount == reqId){
                                switch(type){
                                    case TRAVEL_TO:
                                        reqStatus.setProgress(subReqId, 1);
                                        break;
                                    case KILL_MOB:
                                        //reqStatus.setProgress(subReqId, 0);
                                        break;
                                    case ITEM_DELIVER:

                                        break;
                                    case CRAFTING_DETECT:

                                        break;
                                    case ITEM_DETECT:

                                        break;
                                    case BLOCK_MINE:

                                        break;
                                    case RF_DELIVER:

                                        break;
                                    case RF_GENERATE:

                                        break;
                                }
                            }
                            reqCount++;
                        }
                    }
                }
            }
        }
    }

    private static Triple<String, BlockPos, Integer> getDimPosRadius(String uuid, int questId, int reqId, int subReqId){
        int reqCount = 0;
        for(QuestRequirement questRequirement : Quest.getQuestFromId(questId).getRequirements()){
            if(reqCount == reqId){
                if(questRequirement.getType() == QuestRequirementType.TRAVEL_TO) {
                    int subReqCount = 0;
                    for (IQuestRequirement questSubRequirements : questRequirement.getSubRequirements()) {
                        if(subReqCount == subReqId){
                            QuestRequirement.TravelTo subReqTT = ((QuestRequirement.TravelTo)questSubRequirements);
                            return new Triple<String, BlockPos, Integer>(subReqTT.getDim(), subReqTT.getBlockPos(), subReqTT.getRadius());
                        }
                        subReqCount++;
                    }
                }
            }
            reqCount++;
        }
        return new Triple<>("minecraft:overworld", new BlockPos(0, 0, 0), 0);
    }
    /////////////////////////////////End To Move
}