package com.vincentmet.customquests.quests;

import com.google.gson.JsonArray;
import com.vincentmet.customquests.lib.Ref;

import java.util.ArrayList;
import java.util.List;

public class QuestRequirementStatus {
    private int questId;
    private int requirementId;
    private List<Integer> progress;

    public QuestRequirementStatus(int questId, int requirementId, List<Integer> progress){
        this.questId = questId;
        this.requirementId = requirementId;
        this.progress = progress;
        int howManyToGenerate = Quest.getQuestFromId(questId).getRequirements().get(requirementId).getSubRequirements().size() - progress.size();
        int progressSize = progress.size();
        for(int i=progressSize;i<progressSize+howManyToGenerate;i++){
            this.progress.add(0);
        }
        Ref.shouldSaveNextTick = true;
    }

    public List<Integer> getProgress() {
        return progress;
    }

    public int getRequirementId() {
        return requirementId;
    }

    public int getQuestId() {
        return questId;
    }

    public int getProgress(int index){
        return progress.get(index);
    }

    public void setProgress(int index, int progress){
        this.progress.set(index, progress);
        Ref.shouldSaveNextTick = true;
    }

    public JsonArray getJson(){
        JsonArray json = new JsonArray();
        for(int progressEntry : progress){
            json.add(progressEntry);
        }
        return json;
    }
}
