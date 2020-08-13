package com.vincentmet.customquests.quests.progress;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.vincentmet.customquests.lib.IntCounter;
import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.quests.IJsonProvider;
import com.vincentmet.customquests.quests.quest.Quest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuestStatus implements IJsonProvider {
    private int questId;
    private boolean claimed;
    private List<QuestRequirementStatus> questRequirementStatuses;

    public QuestStatus(int questId, boolean claimed, List<QuestRequirementStatus> questRequirementStatuses){
        this.questId = questId;
        this.claimed = claimed;
        this.questRequirementStatuses = questRequirementStatuses;
        int questReqStat = this.questRequirementStatuses.size();
        int howManyToGenerate = (Quest.getQuestFromId(questId)==null || Quest.getQuestFromId(questId).getRequirements()==null)?0:Quest.getQuestFromId(questId).getRequirements().size() - questReqStat;
        for(int i=questReqStat;i<questReqStat+howManyToGenerate;i++){
            this.questRequirementStatuses.add(new QuestRequirementStatus(questId, i, new HashMap<>()));
        }
        Ref.shouldSaveNextTick = true;
    }

    public void completeAllRequirements(){
        Quest.getQuestFromId(questId).getRequirements()
                .forEach(questRequirement -> { // quest - req
                    questRequirement.getSubRequirements()
                            .forEach(questSubRequirement -> { //quest - subreq
                                getQuestRequirementStatuses()
                                        .stream()
                                        .filter(questRequirementStatus -> questRequirementStatus.getQuestId() == questId)
                                        .forEach(questRequirementStatus -> { // progress - req
                                            questRequirementStatus.getProgress().entrySet()
                                                    .stream()
                                                    .filter(integerQuestSubrequirementStatusEntry -> integerQuestSubrequirementStatusEntry.getKey() == questSubRequirement.getCompletionNumber())
                                                    .forEach(integerQuestSubrequirementStatusEntry -> { // progress - subreq
                                                        integerQuestSubrequirementStatusEntry.getValue().setComplete(questSubRequirement.getCompletionNumber());
                                                    });
                                        });
                            });
                });
    }

    public int getQuestId() {
        return questId;
    }

    public boolean isClaimed() {
        return claimed;
    }

    public List<QuestRequirementStatus> getQuestRequirementStatuses() {
        return questRequirementStatuses;
    }

    public void setClaimed(boolean claimed) {
        this.claimed = claimed;
        Ref.shouldSaveNextTick = true;
    }

    public void setQuestId(int questId) {
        this.questId = questId;
        Ref.shouldSaveNextTick = true;
    }

    public void setQuestRequirementStatuses(List<QuestRequirementStatus> questRequirementStatuses) {
        this.questRequirementStatuses = questRequirementStatuses;
        Ref.shouldSaveNextTick = true;
    }

    public void addQuestRequirementStatus(QuestRequirementStatus questRequirementStatus){
        this.questRequirementStatuses.add(questRequirementStatus);
        Ref.shouldSaveNextTick = true;
    }

    public void deleteQuestRequirementStatus(QuestRequirementStatus questRequirementStatus){
        this.questRequirementStatuses.remove(questRequirementStatus);
        Ref.shouldSaveNextTick = true;
    }

    public JsonObject getJson(){
        JsonObject json = new JsonObject();
        json.addProperty("id", questId);
        json.addProperty("claimed", claimed);
        JsonArray questRequirementStatusArray = new JsonArray();
        for(QuestRequirementStatus questRequirementStatus : questRequirementStatuses){
            questRequirementStatusArray.add(questRequirementStatus.getJson());
        }
        json.add("requirement_completion", questRequirementStatusArray);
        return json;
    }

    @Override
    public String toString() {
        return getJson().toString();
    }
}