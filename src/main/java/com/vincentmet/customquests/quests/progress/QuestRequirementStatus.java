package com.vincentmet.customquests.quests.progress;

import com.google.gson.JsonArray;
import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.quests.IJsonArrayProvider;
import com.vincentmet.customquests.quests.quest.Quest;

import java.util.List;
import java.util.Map;

public class QuestRequirementStatus implements IJsonArrayProvider {
    private int questId;
    private int requirementId;
    private Map<Integer, QuestSubrequirementStatus> progress;

    public QuestRequirementStatus(int questId, int requirementId, Map<Integer, QuestSubrequirementStatus> progress){
        this.questId = questId;
        this.requirementId = requirementId;
        this.progress = progress;
        int howManyToGenerate = (Quest.getQuestFromId(questId)==null || Quest.getQuestFromId(questId).getRequirements()==null)?0:Quest.getQuestFromId(questId).getRequirements().get(requirementId).getSubRequirements().size() - progress.size();
        int progressSize = progress.size();
        for(int i=progressSize;i<progressSize+howManyToGenerate;i++){
            this.progress.put(i, new QuestSubrequirementStatus(i, 0));
        }
        Ref.shouldSaveNextTick = true;
    }

    public Map<Integer, QuestSubrequirementStatus> getProgress() {
        return progress;
    }

    public int getRequirementId() {
        return requirementId;
    }

    public int getQuestId() {
        return questId;
    }

    public int getProgress(int index){
        return progress.get(index).getValue();
    }

    public void setProgress(int index, int progress){
        this.progress.put(index, new QuestSubrequirementStatus(index, progress));
        Ref.shouldSaveNextTick = true;
    }

    public JsonArray getJson(){
        JsonArray json = new JsonArray();
        for(Map.Entry<Integer, QuestSubrequirementStatus> progressEntry : progress.entrySet()){
            json.add(progressEntry.getValue().getJson());
        }
        return json;
    }
}
