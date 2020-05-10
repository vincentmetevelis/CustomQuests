package com.vincentmet.customquests.quests.progress;

import com.google.gson.*;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.vincentmet.customquests.lib.*;
import com.vincentmet.customquests.quests.*;
import com.vincentmet.customquests.quests.party.Party;
import com.vincentmet.customquests.quests.quest.Quest;
import java.util.*;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class QuestUserProgress implements IJsonProvider{
    private String uuid;
    private int partyId;
    private List<Integer> completedQuestsIds;
    private Map<Integer, QuestStatus> questStatuses;

    public QuestUserProgress(String uuid, int partyId, List<Integer> completedQuestsIds, Map<Integer, QuestStatus> questStatuses){
        this.uuid = uuid;
        this.partyId = partyId;
        this.completedQuestsIds = completedQuestsIds;
        this.questStatuses = questStatuses;
        List<Integer> allQuestIds = new ArrayList<>();
        for(Quest quest : Ref.ALL_QUESTS.values()){
            allQuestIds.add(quest.getId());
        }
        List<Integer> allQuestStatusIds = new ArrayList<>();
        for(Map.Entry<Integer, QuestStatus> quest : questStatuses.entrySet()){
            allQuestStatusIds.add(quest.getValue().getQuestId());
        }

        List<Integer> nonPresentQuestList = new ArrayList<>(allQuestIds);
        nonPresentQuestList.removeAll(allQuestStatusIds);

        for(int questId : nonPresentQuestList){
            this.questStatuses.put(questId, new QuestStatus(questId, false, new ArrayList<>()));
        }
        Ref.shouldSaveNextTick = true;
    }

    public static boolean areAllRequirementsCompleted(String uuid, int questId){
        boolean isCompleted = true;
        for(Map.Entry<String, QuestUserProgress> userprogress : Ref.ALL_QUESTING_PROGRESS.entrySet()) {
            if (userprogress.getValue().getUuid().equals(uuid)) {
                for (Map.Entry<Integer, QuestStatus> playerStatus : userprogress.getValue().getQuestStatuses().entrySet()) {
                    if(playerStatus.getValue().getQuestId() == questId){
                        int countQrs = 0;
                        for(QuestRequirementStatus qrs : playerStatus.getValue().getQuestRequirementStatuses()){
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
        for(Map.Entry<String, QuestUserProgress> userProgress : Ref.ALL_QUESTING_PROGRESS.entrySet()){
            if(userProgress.getValue().getUuid().equals(uuid)){
                return userProgress.getValue();
            }
        }
        return null;
    }

    public static boolean isRequirementCompleted(String uuid, int questId, int reqId){
        boolean isCompleted = true;
        for(Map.Entry<String, QuestUserProgress> userprogress : Ref.ALL_QUESTING_PROGRESS.entrySet()){
            if(userprogress.getKey().equals(uuid)){ // get player
                for(Map.Entry<Integer, QuestStatus> playerStatus : userprogress.getValue().getQuestStatuses().entrySet()){
                    if(playerStatus.getKey() == questId){ //then for the quest id
                        for(QuestRequirementStatus reqStatus : playerStatus.getValue().getQuestRequirementStatuses()){
                            if(reqStatus.getRequirementId() == reqId){ // with the requirement id
                                int countSubReqProgress = 0;
                                for(Map.Entry<Integer, QuestSubrequirementStatus> subReqProgress : reqStatus.getProgress().entrySet()){ //get sub requirements
                                    int countSubReq = 0;
                                    for(IQuestRequirement subReq : Quest.getQuestFromId(questId).getRequirements().get(reqId).getSubRequirements()){ // then get the sub requirement from ids // make sure the subrequirements are the same
                                        if(countSubReq == countSubReqProgress){
                                            if(subReqProgress.getValue().getValue() < subReq.getCompletionNumber()){
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
        for(Map.Entry<String, QuestUserProgress> progress : Ref.ALL_QUESTING_PROGRESS.entrySet()){
            if(progress.getValue().getUuid().equals(uuid)){
                int currentCount = progress.getValue().getQuestStatuses().get(questId).getQuestRequirementStatuses().get(reqId).getProgress(subReqId);
                int questCompletionCount = Quest.getQuestFromId(questId).getRequirements().get(reqId).getSubRequirements().get(subReqId).getCompletionNumber();
                return questCompletionCount - currentCount;
            }
        }
        return 0;
    }

    public static boolean isRewardClaimed(String uuid, int questId){
        for(Map.Entry<String, QuestUserProgress> progress : Ref.ALL_QUESTING_PROGRESS.entrySet()){
            if(progress.getValue().getUuid().equals(uuid)){
                for(Map.Entry<Integer, QuestStatus> reqStatus : progress.getValue().getQuestStatuses().entrySet()){
                    if(reqStatus.getValue().getQuestId() == questId){
                        return reqStatus.getValue().isClaimed();
                    }
                }
            }
        }
        return false;
    }

    public static void setRewardsClaimed(String uuid, int questId){
        for(Map.Entry<String, QuestUserProgress> progress : Ref.ALL_QUESTING_PROGRESS.entrySet()){
            if(progress.getValue().getUuid().equals(uuid)){
                for(Map.Entry<Integer, QuestStatus> reqStatus : progress.getValue().getQuestStatuses().entrySet()){
                    if(reqStatus.getValue().getQuestId() == questId){
                        reqStatus.getValue().setClaimed(true);
                    }
                }
            }
        }
    }

    public static void setPlayerProgressToCompleted(String uuid, int questId, int reqId, int subReqId){
        for(Map.Entry<String, QuestUserProgress> userprogress : Ref.ALL_QUESTING_PROGRESS.entrySet()) {
            if(userprogress.getValue().getUuid().equals(uuid)) {
                for(Map.Entry<Integer, QuestStatus> status : userprogress.getValue().getQuestStatuses().entrySet()) {
                    if(status.getValue().getQuestId() == questId){
                        int reqCount = 0;
                        for(QuestRequirementStatus reqStatus : status.getValue().getQuestRequirementStatuses()){
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
        for(Map.Entry<String, QuestUserProgress> userprogress : Ref.ALL_QUESTING_PROGRESS.entrySet()) {
            if(userprogress.getValue().getUuid().equals(uuid)) {
                for(Map.Entry<Integer, QuestStatus> status : userprogress.getValue().getQuestStatuses().entrySet()) {
                    if(status.getValue().getQuestId() == questId){
                        int reqCount = 0;
                        for(QuestRequirementStatus reqStatus : status.getValue().getQuestRequirementStatuses()){
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

    public int getPartyId() {
        return partyId;
    }

    public void setPartyId(int partyId) {
        this.partyId = partyId;
    }

    public Party getParty(){
        for(Party party : Ref.ALL_QUESTING_PARTIES){
            if(party.getId() == partyId){
                return party;
            }
        }
        return null;
    }

    public List<Integer> getCompletedQuestsIds() {
        return completedQuestsIds;
    }

    public Map<Integer, QuestStatus> getQuestStatuses() {
        return questStatuses;
    }

    public void addCompletedQuest(int questId, World world, PlayerEntity player){
        if(!this.completedQuestsIds.contains(questId)){
            this.completedQuestsIds.add(questId);
            Ref.shouldSaveNextTick = true;
            final CommandDispatcher<CommandSource> dispatcher = world.getServer().getCommandManager().getDispatcher();
            try {
                String title = Utils.getFormattedText(".quest_completed");
                dispatcher.execute("title " + player.getDisplayName().getString() + " title \"" + title + "\"", world.getServer().getCommandSource().withFeedbackDisabled());
                dispatcher.execute("title " + player.getDisplayName().getString() + " subtitle \"" + Quest.getQuestFromId(questId).getTitle() + "\"", world.getServer().getCommandSource().withFeedbackDisabled());
            } catch (CommandSyntaxException e) {
                e.printStackTrace();
            }
        }
    }
    public void addCompletedQuest(int questId){
        if(!this.completedQuestsIds.contains(questId)){
            this.completedQuestsIds.add(questId);
            Ref.shouldSaveNextTick = true;
        }
    }


    
    public void deleteCompletedQuest(int questId){
        this.completedQuestsIds.remove(questId);
        Ref.shouldSaveNextTick = true;
    }

    public void setQuestStatuses(Map<Integer, QuestStatus> questStatuses) {
        this.questStatuses = questStatuses;
        Ref.shouldSaveNextTick = true;
    }

    public void addQuestStatus(QuestStatus questStatus){
        this.questStatuses.put(questStatus.getQuestId(), questStatus);
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
        for(Map.Entry<Integer, QuestStatus> questStatusId : questStatuses.entrySet()){
            questStatusArray.add(questStatusId.getValue().getJson());
        }
        json.add("quest_status", questStatusArray);
        return json;
    }

    @Override
    public String toString() {
        return getJson().toString();
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setCompletedQuestsIds(List<Integer> completedQuestsIds) {
        this.completedQuestsIds = completedQuestsIds;
    }
}
