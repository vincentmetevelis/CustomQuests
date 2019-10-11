package com.vincentmet.customquests.quests;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.lib.Utils;
import org.codehaus.plexus.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        for(QuestUserProgress userprogress : Ref.ALL_QUESTING_PROGRESS){
            if(userprogress.uuid.equals(uuid)){
                for(QuestStatus reqStatus : userprogress.questStatuses){
                    if(reqStatus.getQuestId() == questId){
                        for(QuestRequirementStatus subReqStatus : reqStatus.getQuestRequirementStatuses()){
                            for(int subReqProgress : subReqStatus.getProgress()){

                                for(QuestRequirement req : Quest.getQuestFromId(questId).getRequirements()){
                                    for(IQuestRequirement subReq : req.getSubRequirements()){
                                        if(subReqProgress < subReq.getCompletionNumber()){
                                            isCompleted = false;
                                        }
                                    }
                                }

                            }
                        }
                    }
                }
            }
        }
        return isCompleted;
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

    public void setCompletedQuestsIds(List<Integer> completedQuestsIds) {
        this.completedQuestsIds = completedQuestsIds;
        Ref.shouldSaveNextTick = true;
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
