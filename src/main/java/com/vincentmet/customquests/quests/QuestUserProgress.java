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

    public static boolean areAllRequirementsCompleted(int questId, String uuid){//todo
        return false;
    }

    public static boolean isRewardClaimed(int questId, String uuid){//todo
        return false;
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
        this.completedQuestsIds.add(questId);
        Ref.shouldSaveNextTick = true;
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
