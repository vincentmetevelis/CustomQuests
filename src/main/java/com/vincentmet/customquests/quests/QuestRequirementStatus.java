package com.vincentmet.customquests.quests;

import com.google.gson.JsonArray;
import com.vincentmet.customquests.lib.Ref;

import java.util.ArrayList;
import java.util.List;

public class QuestRequirementStatus {
    private int questId;
    private int id;
    private List<Integer> progress;

    public QuestRequirementStatus(int questId, int id, List<Integer> progress){
        this.questId = id;
        this.id = id;
        this.progress = progress;
        int howManyToGenerate = Quest.getQuestFromId(questId).getRequirements().get(id).getSubRequirements().size() - progress.size();
        int progressSize = progress.size();
        for(int i=progressSize;i<progressSize+howManyToGenerate;i++){
            this.progress.add(0);
        }
        Ref.shouldSaveNextTick = true;
    }

    public List<Integer> getProgress() {
        return progress;
    }

    public int getId() {
        return id;
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
