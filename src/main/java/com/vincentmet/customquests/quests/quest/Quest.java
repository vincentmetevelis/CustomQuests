package com.vincentmet.customquests.quests.quest;

import com.google.gson.*;
import com.mojang.datafixers.util.Pair;
import com.vincentmet.customquests.lib.*;
import com.vincentmet.customquests.quests.*;
import com.vincentmet.customquests.quests.book.QuestLine;
import com.vincentmet.customquests.quests.progress.QuestUserProgress;
import com.vincentmet.customquests.quests.quest.*;
import java.util.*;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.ForgeRegistries;

public class Quest implements IJsonProvider{
    private int id;
    private String title;
    private String description;
    private Item icon;
    private List<Integer> dependencyIds;
    private List<QuestRequirement> requirements;
    private List<QuestReward> rewards;
    private QuestPosition position;

    public Quest(int id, String title, String description, Item icon, List<Integer> dependencyIds, List<QuestRequirement> requirements, List<QuestReward> rewards, QuestPosition position){
        this.id = id;
        this.title = title;
        this.description = description;
        this.icon = icon;
        this.dependencyIds = dependencyIds;
        this.requirements = requirements;
        this.rewards = rewards;
        this.position = position;
    }

    public JsonObject getJson(){
        JsonObject json = new JsonObject();
        json.addProperty("id", id);
        json.addProperty("icon", ForgeRegistries.ITEMS.getKey(icon).toString());
        json.addProperty("title", title);
        json.addProperty("text", description);
        JsonArray dependencyArray = new JsonArray();
        for(int questId : dependencyIds){
            dependencyArray.add(questId);
        }
        json.add("dependencies", dependencyArray);
        JsonArray requirementArray = new JsonArray();
        for(QuestRequirement requirement : requirements){
            requirementArray.add(requirement.getJson());
        }
        json.add("requirements", requirementArray);
        JsonArray rewardArray = new JsonArray();
        for(QuestReward reward : rewards){
            rewardArray.add(reward.getJson());
        }
        json.add("rewards", rewardArray);
        json.add("position", position.getJson());
        return json;
    }

    public static Quest getQuestFromId(int id){
        for(int i=0; i<Ref.ALL_QUESTS.size(); i++){
            if(Ref.ALL_QUESTS.get(i).id == id){
                return Ref.ALL_QUESTS.get(i);
            }
        }
        return null;
    }

    public static boolean isQuestCompletedForPlayer(String playerUuid, int questId){
        for(Map.Entry<String, QuestUserProgress> userprogress : Ref.ALL_QUESTING_PROGRESS.entrySet()){
            if(userprogress.getKey().equals(playerUuid)){
                for(int id : userprogress.getValue().getCompletedQuestsIds()){
                    if(questId == id){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static List<Integer> getUncompletedQuests(String uuid){ // Both active and locked quests
        List<Integer> list = new ArrayList<>();
        for(Quest quest : Ref.ALL_QUESTS.values()){
            if(!Quest.isQuestCompletedForPlayer(uuid, quest.getId())){
                list.add(quest.getId());
            }
        }
        return list;
    }

    public static List<Integer> getCompletedQuests(String uuid){
        List<Integer> list = new ArrayList<>();
        for(Quest quest : Ref.ALL_QUESTS.values()){
            if(Quest.isQuestCompletedForPlayer(uuid, quest.getId())){
                list.add(quest.getId());
            }
        }
        return list;
    }

    public static List<Integer> getLockedQuests(String uuid){ // no completed nor active quests, only those the player doesn't have access to yes
        List<Integer> list = new ArrayList<>();
        for (Quest quest : Ref.ALL_QUESTS.values()) {
            if(Quest.isQuestLocked(uuid, quest.getId())){
                list.add(quest.getId());
            }
        }
        return list;
    }

    public static boolean isQuestLocked(String uuid, int questId){
        for (Map.Entry<String, QuestUserProgress> userProgress : Ref.ALL_QUESTING_PROGRESS.entrySet()) {
            if(userProgress.getValue().getUuid().equals(uuid)){
                if (Quest.isQuestCompletedForPlayer(uuid, questId) || Quest.hasQuestUncompletedDependenciesForPlayer(uuid, questId)){
                    return true;
                }
            }
        }
        return false;
    }

    public static List<Integer> getActiveQuests(String uuid){
        List<Integer> list = new ArrayList<>();
        for (Quest quest : Ref.ALL_QUESTS.values()) {
            if(Quest.isQuestActive(uuid, quest.getId())){
                list.add(quest.getId());
            }
        }
        return list;
    }

    public static boolean isQuestActive(String uuid, int questId){
        for (Map.Entry<String, QuestUserProgress> userProgress : Ref.ALL_QUESTING_PROGRESS.entrySet()) {
            if(userProgress.getValue().getUuid().equals(uuid)){
                if (!Quest.isQuestCompletedForPlayer(uuid, questId) && !Quest.isQuestLocked(uuid, questId)){
                    return true;
                }
            }
        }
        return false;
    }

    public static List<Triple<Integer, Integer, Integer>> getActiveQuestsWithType(String uuid, QuestRequirementType type){
        List<Triple<Integer, Integer, Integer>> list = new ArrayList<>();
        for(int questId : Quest.getActiveQuests(uuid)){
            int reqCount = 0;
            for(QuestRequirement questRequirement : Quest.getQuestFromId(questId).getRequirements()){
                if(questRequirement.getType() == type){
                    int subReqCount=0;
                    for(IQuestRequirement iqr : questRequirement.getSubRequirements()){
                        list.add(new Triple<>(questId, reqCount, subReqCount));
                        subReqCount++;
                    }
                }
                reqCount++;
            }
        }
        return list;
    }

    public static boolean hasQuestUncompletedDependenciesForPlayer(String uuid, int questId){
        boolean areAllDependenciesCompleted = true;
        for(int dependencyId : Quest.getQuestFromId(questId).getDependencies()){ // get dependencies for current quest
            if(!isQuestCompletedForPlayer(uuid, dependencyId)){
                areAllDependenciesCompleted = false;
            }
        }
        return !areAllDependenciesCompleted;
    }

    public static boolean areQuestsInSameQuestline(int quest1, int quest2){
        return Quest.getQuestFromId(quest1).getQuestline() == Quest.getQuestFromId(quest2).getQuestline();
    }

    public static boolean isPlayerInRadius(PlayerEntity player, Triple<String, BlockPos, Integer> dimPosRadius){
        BlockPos qp = dimPosRadius.getMiddle();
        BlockPos pp = player.getPosition();
        int r = dimPosRadius.getRight();
        return player.world.getDimension().getType().toString().substring(14, player.world.getDimension().getType().toString().length()-1).equals(dimPosRadius.getLeft()) && qp.getX() <= pp.getX()+r && qp.getX() >= pp.getX()-r && qp.getY() <= pp.getY()+r && qp.getY() >= pp.getY()-r && qp.getZ() <= pp.getZ()+r && qp.getZ() >= pp.getZ()-r;
    }

    public static Triple<String, BlockPos, Integer> getDimPosRadius(int questId, int reqId, int subReqId){
        int reqCount = 0;
        for(QuestRequirement questRequirement : Quest.getQuestFromId(questId).getRequirements()){
            if(reqCount == reqId){
                if(questRequirement.getType() == QuestRequirementType.TRAVEL_TO) {
                    int subReqCount = 0;
                    for (IQuestRequirement questSubRequirements : questRequirement.getSubRequirements()) {
                        if(subReqCount == subReqId){
                            QuestRequirement.TravelTo subReqTT = ((QuestRequirement.TravelTo)questSubRequirements);
                            return new Triple<>(subReqTT.getDim(), subReqTT.getBlockPos(), subReqTT.getRadius());
                        }
                        subReqCount++;
                    }
                }
            }
            reqCount++;
        }
        return new Triple<>("minecraft:overworld", new BlockPos(0, 0, 0), 0);
    }

    public static ItemStack getItemstackForCraftingDetect(int questId, int reqId, int subReqId){
        int reqCount = 0;
        for(QuestRequirement questRequirement : Quest.getQuestFromId(questId).getRequirements()){
            if(reqCount == reqId){
                if(questRequirement.getType() == QuestRequirementType.CRAFTING_DETECT) {
                    int subReqCount = 0;
                    for (IQuestRequirement questSubRequirements : questRequirement.getSubRequirements()) {
                        if(subReqCount == subReqId){
                            QuestRequirement.ItemCraft subReqC = ((QuestRequirement.ItemCraft)questSubRequirements);
                            return subReqC.getItemStack();
                        }
                        subReqCount++;
                    }
                }
            }
            reqCount++;
        }
        return null;
    }

    public static ItemStack getItemstackForItemDetect(int questId, int reqId, int subReqId){
        int reqCount = 0;
        for(QuestRequirement questRequirement : Quest.getQuestFromId(questId).getRequirements()){
            if(reqCount == reqId){
                if(questRequirement.getType() == QuestRequirementType.ITEM_DETECT) {
                    int subReqCount = 0;
                    for (IQuestRequirement questSubRequirements : questRequirement.getSubRequirements()) {
                        if(subReqCount == subReqId){
                            QuestRequirement.ItemDetect subReqD = ((QuestRequirement.ItemDetect)questSubRequirements);
                            return subReqD.getItemStack();
                        }
                        subReqCount++;
                    }
                }
            }
            reqCount++;
        }
        return null;
    }

    public static ItemStack getItemstackForItemHandIn(int questId, int reqId, int subReqId){
        int reqCount = 0;
        for(QuestRequirement questRequirement : Quest.getQuestFromId(questId).getRequirements()){
            if(reqCount == reqId){
                if(questRequirement.getType() == QuestRequirementType.ITEM_DELIVER) {
                    int subReqCount = 0;
                    for (IQuestRequirement questSubRequirements : questRequirement.getSubRequirements()) {
                        if(subReqCount == subReqId){
                            QuestRequirement.ItemSubmit subReqS = ((QuestRequirement.ItemSubmit)questSubRequirements);
                            return subReqS.getItemStack();
                        }
                        subReqCount++;
                    }
                }
            }
            reqCount++;
        }
        return null;
    }

    public static Pair<EntityType, Integer> getMobAmountForMobKill(int questId, int reqId, int subReqId){
        int reqCount = 0;
        for(QuestRequirement questRequirement : Quest.getQuestFromId(questId).getRequirements()){
            if(reqCount == reqId){
                if(questRequirement.getType() == QuestRequirementType.KILL_MOB) {
                    int subReqCount = 0;
                    for (IQuestRequirement questSubRequirements : questRequirement.getSubRequirements()) {
                        if(subReqCount == subReqId){
                            QuestRequirement.KillMob subReqKM = ((QuestRequirement.KillMob)questSubRequirements);
                            return new Pair<>(subReqKM.getEntity(), subReqKM.getAmount());
                        }
                        subReqCount++;
                    }
                }
            }
            reqCount++;
        }
        return null;
    }

    public int getQuestline(){
        for(Map.Entry<Integer, QuestLine> questline : Ref.ALL_QUESTBOOK.getQuestlines().entrySet()){
            for(int questId : questline.getValue().getQuests()){
                if(Quest.getQuestFromId(questId).getId() == this.getId()){
                    return questline.getValue().getId();
                }
            }
        }
        return Ref.ERR_MSG_INT_INVALID_JSON;
    }

    public void setTitle(String title) {
        this.title = title;
        Ref.shouldSaveNextTick = true;
    }

    public void setDescription(String description) {
        this.description = description;
        Ref.shouldSaveNextTick = true;
    }

    public void setRequirement(List<QuestRequirement> requirement) {
        this.requirements = requirement;
        Ref.shouldSaveNextTick = true;
    }

    public void setDependencies(List<Integer> dependencyIds) {
        this.dependencyIds = dependencyIds;
        Ref.shouldSaveNextTick = true;
    }

    public void setIcon(Item icon) {
        this.icon = icon;
        Ref.shouldSaveNextTick = true;
    }

    public void setPosition(QuestPosition position) {
        this.position = position;
        Ref.shouldSaveNextTick = true;
    }

    public void setRewards(List<QuestReward> rewards) {
        this.rewards = rewards;
        Ref.shouldSaveNextTick = true;
    }

    public void addReward(QuestReward reward){
        this.rewards.add(reward);
        Ref.shouldSaveNextTick = true;
    }

    public void deleteReward(QuestReward reward){
        this.rewards.remove(reward);
        Ref.shouldSaveNextTick = true;
    }

    public int getId(){
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Item getIcon() {
        return icon;
    }

    public List<Integer> getDependencies() {
        return dependencyIds;
    }

    public List<QuestRequirement> getRequirements() {
        return requirements;
    }

    public List<QuestReward> getRewards() {
        return rewards;
    }

    public QuestPosition getPosition() {
        return position;
    }
}