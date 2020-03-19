package com.vincentmet.customquests.lib.handlers;

import com.mojang.datafixers.util.Pair;
import com.vincentmet.customquests.Objects;
import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.lib.Triple;
import com.vincentmet.customquests.lib.Utils;
import com.vincentmet.customquests.network.packets.MessageUpdateQuestPartiesServerToClient;
import com.vincentmet.customquests.network.packets.MessageUpdateQuestbookServerToClient;
import com.vincentmet.customquests.network.packets.MessageUpdateQuestsServerToClient;
import com.vincentmet.customquests.network.packets.PacketHelper;
import com.vincentmet.customquests.quests.Quest;
import com.vincentmet.customquests.quests.QuestRequirementType;
import com.vincentmet.customquests.quests.QuestUserProgress;
import com.vincentmet.customquests.screens.ScreenQuestingDevice;
import com.vincentmet.customquests.screens.questingdeveicesubscreens.SubScreenQuestDetails;
import com.vincentmet.customquests.screens.questingdeveicesubscreens.SubScreenQuestlines;
import com.vincentmet.customquests.screens.questingdeveicesubscreens.SubScreensQuestingDevice;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.*;

@Mod.EventBusSubscriber(modid = Ref.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EventHandler {
    private static long lastMillis = System.currentTimeMillis();
    private static Pair<Integer, Long> lastKey;
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event){
        event.getRegistry().registerAll(
                Objects.Items.itemQuestingDevice
        );
    }

    @SubscribeEvent
    public static void onItemCraft(PlayerEvent.ItemCraftedEvent event){
        List<Triple<Integer, Integer, Integer>> activeItemCraftQuestIds = Quest.getActiveQuestsWithType(Utils.simplifyUUID(event.getPlayer().getUniqueID()), QuestRequirementType.CRAFTING_DETECT);
        for(Triple<Integer, Integer, Integer> questAndReqAndSubReqId : activeItemCraftQuestIds){
            ItemStack itemStack = Quest.getItemstackForCraftingDetect(questAndReqAndSubReqId.getLeft(), questAndReqAndSubReqId.getMiddle(), questAndReqAndSubReqId.getRight());
            if(event.getCrafting().getItem() == itemStack.getItem() && event.getCrafting().getTag() == itemStack.getTag()){
                QuestUserProgress.addPlayerProgress(Utils.simplifyUUID(event.getPlayer().getUniqueID()), questAndReqAndSubReqId.getLeft(), questAndReqAndSubReqId.getMiddle(), questAndReqAndSubReqId.getRight(), event.getCrafting().getCount());
            }
        }
    }

    @SubscribeEvent
    public static void onEntityKill(LivingDeathEvent event){
        if(event.getSource().getTrueSource() instanceof PlayerEntity) {
            List<Triple<Integer, Integer, Integer>> activeEntityKillQuestIds = Quest.getActiveQuestsWithType(Utils.simplifyUUID(event.getSource().getTrueSource().getUniqueID()), QuestRequirementType.KILL_MOB);
            for (Triple<Integer, Integer, Integer> questAndReqAndSubReqId : activeEntityKillQuestIds) {
                Pair<EntityType, Integer> mobAmount = Quest.getMobAmountForMobKill(questAndReqAndSubReqId.getLeft(), questAndReqAndSubReqId.getMiddle(), questAndReqAndSubReqId.getRight());
                if (event.getEntity().getType() == mobAmount.getFirst() && event.getSource().getTrueSource() instanceof PlayerEntity) {
                    QuestUserProgress.addPlayerProgress(Utils.simplifyUUID(event.getSource().getTrueSource().getUniqueID()), questAndReqAndSubReqId.getLeft(), questAndReqAndSubReqId.getMiddle(), questAndReqAndSubReqId.getRight(), 1);
                    Ref.shouldSaveNextTick = true;
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event){
        if(ConfigHandler.giveDeviceOnLogin.get()){
            if(!event.getPlayer().inventory.hasItemStack(new ItemStack(Objects.Items.itemQuestingDevice))) {
                event.getPlayer().inventory.addItemStackToInventory(new ItemStack(Objects.Items.itemQuestingDevice, 1));
            }
        }else if(ConfigHandler.giveDeviceOnFirstLogin.get()){
            if(QuestUserProgress.getUserProgressForUuid(Utils.simplifyUUID(event.getPlayer().getUniqueID()))==null){
                if(!event.getPlayer().inventory.hasItemStack(new ItemStack(Objects.Items.itemQuestingDevice))){
                    event.getPlayer().inventory.addItemStackToInventory(new ItemStack(Objects.Items.itemQuestingDevice, 1));
                }
            }
        }
        if(QuestUserProgress.getUserProgressForUuid(Utils.simplifyUUID(event.getPlayer().getUniqueID()))==null){
            QuestUserProgress newQup = new QuestUserProgress(Utils.simplifyUUID(event.getPlayer().getUniqueID()), Ref.ERR_MSG_INT_INVALID_JSON, new ArrayList<>(), new HashMap<>());
            Ref.ALL_QUESTING_PROGRESS.put(newQup.getUuid(), newQup);
            Ref.shouldSaveNextTick = true;
        }

        PacketHelper.sendAllProgressUpdatePackets((ServerPlayerEntity)event.getPlayer());
        PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(()->(ServerPlayerEntity)event.getPlayer()), new MessageUpdateQuestsServerToClient());
        PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(()->(ServerPlayerEntity)event.getPlayer()), new MessageUpdateQuestbookServerToClient());
        PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(()->(ServerPlayerEntity)event.getPlayer()), new MessageUpdateQuestPartiesServerToClient());
    }

    @SubscribeEvent
    public static void onPlayerLoggingOff(PlayerEvent.PlayerLoggedOutEvent event){
        if(event.getPlayer().world.isRemote){
            Ref.ALL_QUESTING_PROGRESS = new HashMap<>();
            Ref.ALL_QUESTING_PARTIES = new ArrayList<>();
            ScreenQuestingDevice.setActiveScreen(SubScreensQuestingDevice.QUESTLINES);
            SubScreenQuestDetails.setActiveQuest(-1);
        }
    }

    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event){
        if(!event.world.isRemote) {
            if(Ref.shouldSaveNextTick){
                JsonHandler.writeAll(Ref.questsLocation, Ref.questBookLocation, Ref.questingProgressLocation, Ref.questingPartiesLocation);
                for(PlayerEntity player : event.world.getPlayers()){
                    PacketHelper.sendAllProgressUpdatePackets((ServerPlayerEntity)player);
                }
                Ref.shouldSaveNextTick = false;
            }
        }else{
            Ref.shouldSaveNextTick = false;
        }
        if (event.world.getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE)) {
            if (event.world.getGameTime() % 80 == 0) {
                executeWorldTickLogic(event.world);
            }
        } else {
            if (System.currentTimeMillis() > lastMillis + 4000) {
                lastMillis = System.currentTimeMillis();
                executeWorldTickLogic(event.world);
            }
        }
    }

    private static void executeWorldTickLogic(World world){ //Separate method to avoid code duplication
        for(PlayerEntity playerEntity : world.getPlayers()){
            List<Triple<Integer, Integer, Integer>> activeLocationTrackingQuestIds = Quest.getActiveQuestsWithType(Utils.simplifyUUID(playerEntity.getUniqueID()), QuestRequirementType.TRAVEL_TO);
            for(Triple<Integer, Integer, Integer> questAndReqAndSubReqId : activeLocationTrackingQuestIds){
                Triple<String, BlockPos, Integer> dimPosRadius = Quest.getDimPosRadius(questAndReqAndSubReqId.getLeft(), questAndReqAndSubReqId.getMiddle(), questAndReqAndSubReqId.getRight());
                if(Quest.isPlayerInRadius(playerEntity, dimPosRadius)){
                    QuestUserProgress.setPlayerProgressToCompleted(Utils.simplifyUUID(playerEntity.getUniqueID()), questAndReqAndSubReqId.getLeft(), questAndReqAndSubReqId.getMiddle(), questAndReqAndSubReqId.getRight());
                    Ref.shouldSaveNextTick = true;
                }
            }

            List<Triple<Integer, Integer, Integer>> activeItemDetectQuestIds = Quest.getActiveQuestsWithType(Utils.simplifyUUID(playerEntity.getUniqueID()), QuestRequirementType.ITEM_DETECT);
            for(Triple<Integer, Integer, Integer> questAndReqAndSubReqId : activeItemDetectQuestIds){
                ItemStack itemStack = Quest.getItemstackForItemDetect(questAndReqAndSubReqId.getLeft(), questAndReqAndSubReqId.getMiddle(), questAndReqAndSubReqId.getRight());
                int correctItemInInvCount = 0;
                for(ItemStack mainInventoryStack : playerEntity.inventory.mainInventory){
                    if(mainInventoryStack.getItem() == itemStack.getItem()){
                        correctItemInInvCount += mainInventoryStack.getCount();
                    }
                }
                if(itemStack.getCount() <= correctItemInInvCount){
                    QuestUserProgress.setPlayerProgressToCompleted(Utils.simplifyUUID(playerEntity.getUniqueID()), questAndReqAndSubReqId.getLeft(), questAndReqAndSubReqId.getMiddle(), questAndReqAndSubReqId.getRight());
                    Ref.shouldSaveNextTick = true;
                }
            }

            for(Quest quest : Ref.ALL_QUESTS){
                if(QuestUserProgress.areAllRequirementsCompleted(Utils.simplifyUUID(playerEntity.getUniqueID()), quest.getId())){
                    for(Map.Entry<String, QuestUserProgress> userprogress : Ref.ALL_QUESTING_PROGRESS.entrySet()){
                        if(userprogress.getKey().equals(Utils.simplifyUUID(playerEntity.getUniqueID()))){
                            userprogress.getValue().addCompletedQuest(quest.getId(), world, playerEntity);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onWorldStart(WorldEvent.Load event){
        if(event.getWorld() instanceof ServerWorld){
            Ref.currWorldDir = ((ServerWorld)event.getWorld()).getSaveHandler().getWorldDirectory().toPath();
            if(!event.getWorld().isRemote()){
                JsonHandler.loadJson(
                        Ref.questsLocation = FMLPaths.CONFIGDIR.get().resolve("Quests.json"),
                        Ref.questBookLocation = FMLPaths.CONFIGDIR.get().resolve("QuestsBook.json"),
                        Ref.questingProgressLocation = Ref.currWorldDir.resolve("QuestingProgress.json"),
                        Ref.questingPartiesLocation = Ref.currWorldDir.resolve("QuestingParties.json")
                );
                StructureHandler.initQuests(JsonHandler.getQuestsJson());
                StructureHandler.initQuestbook(JsonHandler.getQuestbookJson());
                StructureHandler.initQuestingProgress(JsonHandler.getQuestingProgressJson());
                StructureHandler.initQuestingParties(JsonHandler.getQuestingPartiesJson());
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onKeyPress(InputEvent.KeyInputEvent event){
        if(Objects.KeyBindings.OPEN_QUESTINGDEVICE.isPressed()){
            Minecraft.getInstance().displayGuiScreen(new ScreenQuestingDevice());
        }
    }

    @SubscribeEvent
    public static void onKeyHold(InputEvent.KeyInputEvent event){
        if(lastKey == null){
            lastKey = new Pair<>(event.getKey(), new Date().getTime());
        }
        if(new Date().getTime() == lastKey.getSecond() - 1000){
            //todo
        }
    }
}