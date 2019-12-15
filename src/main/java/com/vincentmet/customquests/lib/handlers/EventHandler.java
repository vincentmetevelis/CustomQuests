package com.vincentmet.customquests.lib.handlers;

import com.google.gson.JsonObject;
import com.vincentmet.customquests.Objects;
import com.vincentmet.customquests.items.ItemQuestingDevice;
import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.lib.Triple;
import com.vincentmet.customquests.lib.Utils;
import com.vincentmet.customquests.network.packets.*;
import com.vincentmet.customquests.quests.*;
import javafx.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;
import org.lwjgl.system.CallbackI;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

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
                if (event.getEntity().getType() == mobAmount.getKey()) {
                    if (event.getSource().getTrueSource() instanceof PlayerEntity) {
                        QuestUserProgress.addPlayerProgress(Utils.simplifyUUID(event.getSource().getTrueSource().getUniqueID()), questAndReqAndSubReqId.getLeft(), questAndReqAndSubReqId.getMiddle(), questAndReqAndSubReqId.getRight(), 1);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event){
        if(!event.getPlayer().inventory.hasItemStack(new ItemStack(Objects.Items.itemQuestingDevice))) {
            event.getPlayer().inventory.addItemStackToInventory(new ItemStack(Objects.Items.itemQuestingDevice, 1));
        }

        if(QuestUserProgress.getUserProgressForUuid(Utils.simplifyUUID(event.getPlayer().getUniqueID()))==null){
            Ref.ALL_QUESTING_PROGRESS.add(new QuestUserProgress(Utils.simplifyUUID(event.getPlayer().getUniqueID()), new ArrayList<>(), new ArrayList<>()));
            Ref.shouldSaveNextTick = true;
        }

        PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(()->(ServerPlayerEntity)event.getPlayer()), new MessageUpdateQuestsServerToClient());
        PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(()->(ServerPlayerEntity)event.getPlayer()), new MessageUpdateQuestbookServerToClient());
        PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(()->(ServerPlayerEntity)event.getPlayer()), new MessageUpdateQuestProgressServerToClient());
    }

    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event){
        if(Ref.shouldSaveNextTick){
            if(!event.world.isRemote){
                JsonHandler.writeAll(Ref.questsLocation, Ref.questBookLocation, Ref.questingProgressLocation);
                for(PlayerEntity player : event.world.getPlayers()){
                    PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(()->(ServerPlayerEntity)player), new MessageUpdateQuestProgressServerToClient(new JsonObject()));
                }
            }
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
        if(!world.isRemote){
            Ref.shouldSaveNextTick = true;
            for(PlayerEntity playerEntity : world.getPlayers()){
                Quest.getActiveQuests(Utils.simplifyUUID(playerEntity.getUniqueID())).stream().forEach((in)->{System.out.print(", " + in);});
                System.out.println();
                List<Triple<Integer, Integer, Integer>> activeLocationTrackingQuestIds = Quest.getActiveQuestsWithType(Utils.simplifyUUID(playerEntity.getUniqueID()), QuestRequirementType.TRAVEL_TO);
                for(Triple<Integer, Integer, Integer> questAndReqAndSubReqId : activeLocationTrackingQuestIds){
                    Triple<String, BlockPos, Integer> dimPosRadius = Quest.getDimPosRadius(questAndReqAndSubReqId.getLeft(), questAndReqAndSubReqId.getMiddle(), questAndReqAndSubReqId.getRight());
                    if(Quest.isPlayerInRadius(playerEntity, dimPosRadius)){
                        QuestUserProgress.setPlayerProgressToCompleted(Utils.simplifyUUID(playerEntity.getUniqueID()), questAndReqAndSubReqId.getLeft(), questAndReqAndSubReqId.getMiddle(), questAndReqAndSubReqId.getRight());
                        System.out.println("In radius of: " + dimPosRadius);
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
                    }
                }

                for(Quest quest : Ref.ALL_QUESTS){
                    if(QuestUserProgress.areAllRequirementsCompleted(Utils.simplifyUUID(playerEntity.getUniqueID()), quest.getId())){
                        for(QuestUserProgress userprogress : Ref.ALL_QUESTING_PROGRESS){
                            if(userprogress.getUuid().equals(Utils.simplifyUUID(playerEntity.getUniqueID()))){
                                userprogress.addCompletedQuest(quest.getId(), world, playerEntity);
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onWorldStart(WorldEvent.Load event){
        if(event.getWorld() instanceof ServerWorld){
            if(Ref.currWorldDir == null){
                Ref.currWorldDir = ((ServerWorld)event.getWorld()).getSaveHandler().getWorldDirectory().getAbsolutePath();
            }
            if(!event.getWorld().isRemote()){
                JsonHandler.loadJson(
                        Ref.questsLocation = FMLPaths.CONFIGDIR.get().toString() + "\\Quests.json",
                        Ref.questBookLocation = FMLPaths.CONFIGDIR.get().toString() + "\\QuestsBook.json",
                        Ref.questingProgressLocation = Ref.currWorldDir + "\\QuestingProgress.json"
                );
                StructureHandler.initQuests(JsonHandler.getQuestsJson());
                StructureHandler.initQuestbook(JsonHandler.getQuestbookJson());
                StructureHandler.initQuestingProgress(JsonHandler.getQuestingProgressJson());
            }
        }
    }
}