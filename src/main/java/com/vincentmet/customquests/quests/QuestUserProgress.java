package com.vincentmet.customquests.quests;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.lib.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandSource;
import net.minecraft.command.ICommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.codehaus.plexus.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class QuestUserProgress {
    private String uuid;
    private List<Integer> completedQuestsIds;
    private List<QuestStatus> questStatuses;

    public QuestUserProgress(String uuid, List<Integer> completedQuestsIds, List<QuestStatus> questStatuses){
        this.uuid = uuid;
        this.completedQuestsIds = completedQuestsIds;
        this.questStatuses = questStatuses;
        List<Integer> allQuestIds = new ArrayList<>();
        for(Quest quest : Ref.ALL_QUESTS){
            allQuestIds.add(quest.getId());
        }
        List<Integer> allQuestStatusIds = new ArrayList<>();
        for(QuestStatus quest : questStatuses){
            allQuestStatusIds.add(quest.getQuestId());
        }

        List<Integer> nonPresentQuestList = new ArrayList<>(allQuestIds);
        nonPresentQuestList.removeAll(allQuestStatusIds);

        for(int questId : nonPresentQuestList){
            this.questStatuses.add(new QuestStatus(questId, false, new ArrayList<>()));
        }
        Ref.shouldSaveNextTick = true;
    }

    public static boolean areAllRequirementsCompleted(String uuid, int questId){
        boolean isCompleted = true;
        for(QuestUserProgress userprogress : Ref.ALL_QUESTING_PROGRESS) {
            if (userprogress.uuid.equals(uuid)) { // get player
                for (QuestStatus playerStatus : userprogress.questStatuses) {
                    if(playerStatus.getQuestId() == questId){
                        int countQrs = 0;
                        for(QuestRequirementStatus qrs : playerStatus.getQuestRequirementStatuses()){
                            if(!isRequirementCompleted(uuid, questId, countQrs)){
                                isCompleted = false;
                            }
                            countQrs++;
                        }
                    }
                }
            }
        }
        return isCompleted;
    }

    public static QuestUserProgress getUserProgressForUuid(String uuid){
        for(QuestUserProgress userProgress : Ref.ALL_QUESTING_PROGRESS){
            if(userProgress.getUuid().equals(uuid)){
                return userProgress;
            }
        }
        return null;
    }

    public static boolean isRequirementCompleted(String uuid, int questId, int reqId){
        boolean isCompleted = true;
        for(QuestUserProgress userprogress : Ref.ALL_QUESTING_PROGRESS){
            if(userprogress.uuid.equals(uuid)){ // get player
                for(QuestStatus playerStatus : userprogress.getQuestStatuses()){
                    if(playerStatus.getQuestId() == questId){ //then for the quest id
                        for(QuestRequirementStatus reqStatus : playerStatus.getQuestRequirementStatuses()){
                            if(reqStatus.getRequirementId() == reqId){ // with the requirement id
                                int countSubReqProgress = 0;
                                for(int subReqProgress : reqStatus.getProgress()){ //get sub requirements
                                    int countSubReq = 0;
                                    for(IQuestRequirement subReq : Quest.getQuestFromId(questId).getRequirements().get(reqId).getSubRequirements()){ // then get the sub requirement from ids // make sure the subrequirements are the same
                                        if(countSubReq == countSubReqProgress){
                                            if(subReqProgress < subReq.getCompletionNumber()){
                                                isCompleted = false;
                                            }
                                        }
                                        countSubReq++;
                                    }
                                    countSubReqProgress++;
                                }
                            }
                        }
                    }
                }
            }
        }
        return isCompleted;
    }

    public static int getItemCountLeftToHandIn(String uuid, int questId, int reqId, int subReqId){
        for(QuestUserProgress progress : Ref.ALL_QUESTING_PROGRESS){
            if(progress.getUuid().equals(uuid)){
                int currentCount = progress.getQuestStatuses().get(questId).getQuestRequirementStatuses().get(reqId).getProgress(subReqId);
                int questCompletionCount = Quest.getQuestFromId(questId).getRequirements().get(reqId).getSubRequirements().get(subReqId).getCompletionNumber();
                return questCompletionCount - currentCount;
            }
        }
        return 0;
    }

    public static boolean isRewardClaimed(String uuid, int questId){
        for(QuestUserProgress progress : Ref.ALL_QUESTING_PROGRESS){
            if(progress.getUuid().equals(uuid)){
                for(QuestStatus reqStatus : progress.questStatuses){
                    if(reqStatus.getQuestId() == questId){
                        return reqStatus.isClaimed();
                    }
                }
            }
        }
        return false;
    }

    public static void setRewardsClaimed(String uuid, int questId){
        for(QuestUserProgress progress : Ref.ALL_QUESTING_PROGRESS){
            if(progress.getUuid().equals(uuid)){
                for(QuestStatus reqStatus : progress.questStatuses){
                    if(reqStatus.getQuestId() == questId){
                        reqStatus.setClaimed(true);
                    }
                }
            }
        }
    }

    public static void setPlayerProgressToCompleted(String uuid, int questId, int reqId, int subReqId){
        for(QuestUserProgress userprogress : Ref.ALL_QUESTING_PROGRESS) {
            if(userprogress.getUuid().equals(uuid)) {
                for(QuestStatus status : userprogress.getQuestStatuses()) {
                    if(status.getQuestId() == questId){
                        int reqCount = 0;
                        for(QuestRequirementStatus reqStatus : status.getQuestRequirementStatuses()){
                            if(reqCount == reqId){
                                reqStatus.setProgress(subReqId, Quest.getQuestFromId(questId).getRequirements().get(reqId).getSubRequirements().get(subReqId).getCompletionNumber());
                            }
                            reqCount++;
                        }
                    }
                }
            }
        }
    }

    public static void addPlayerProgress(String uuid, int questId, int reqId, int subReqId, int amount){
        for(QuestUserProgress userprogress : Ref.ALL_QUESTING_PROGRESS) {
            if(userprogress.getUuid().equals(uuid)) {
                for(QuestStatus status : userprogress.getQuestStatuses()) {
                    if(status.getQuestId() == questId){
                        int reqCount = 0;
                        for(QuestRequirementStatus reqStatus : status.getQuestRequirementStatuses()){
                            if(reqCount == reqId){
                                reqStatus.setProgress(subReqId, reqStatus.getProgress(subReqId) + amount);
                            }
                            reqCount++;
                        }
                    }
                }
            }
        }
    }

    public String getUuid() {
        return uuid;
    }

    public String getUsername(){
        return Utils.getDisplayName(uuid);
    }

    public List<Integer> getCompletedQuestsIds() {
        return completedQuestsIds;
    }

    public List<QuestStatus> getQuestStatuses() {
        return questStatuses;
    }

    public void addCompletedQuest(int questId, World world, PlayerEntity player){ //todo this also triggers on single-requirement completion
        if(!this.completedQuestsIds.contains(questId)){
            this.completedQuestsIds.add(questId);
            Ref.shouldSaveNextTick = true;
            final CommandDispatcher<CommandSource> dispatcher = world.getServer().getCommandManager().getDispatcher();
            try {
                dispatcher.execute("title " + player.getDisplayName().getString() + " title \"QUEST COMPLETED!\"", new CommandSource(ICommandSource.field_213139_a_, null, null, (ServerWorld) world, 5, "CustomQuests", new TranslationTextComponent("Custom Quests"), world.getServer(), null));
            } catch (CommandSyntaxException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void deleteCompletedQuest(int questId){
        this.completedQuestsIds.remove(questId);
        Ref.shouldSaveNextTick = true;
    }

    public void setQuestStatuses(List<QuestStatus> questStatuses) {
        this.questStatuses = questStatuses;
        Ref.shouldSaveNextTick = true;
    }

    public void addQuestStatus(QuestStatus questStatus){
        this.questStatuses.add(questStatus);
        Ref.shouldSaveNextTick = true;
    }

    public void deleteQuestStatus(QuestStatus questStatus){
        this.questStatuses.remove(questStatus);
        Ref.shouldSaveNextTick = true;
    }

    public JsonObject getJson(){
        JsonObject json = new JsonObject();
        json.addProperty("uuid", uuid);
        JsonArray completedQuestArray = new JsonArray();
        for(int completedQuestId : completedQuestsIds){
            completedQuestArray.add(completedQuestId);
        }
        json.add("completed_quests", completedQuestArray);
        JsonArray questStatusArray = new JsonArray();
        for(QuestStatus questStatusId : questStatuses){
            questStatusArray.add(questStatusId.getJson());
        }
        json.add("quest_status", questStatusArray);
        return json;
    }
}
