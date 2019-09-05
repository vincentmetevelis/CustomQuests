package com.vincentmet.customquests.quests;

import java.util.List;

public class QuestRequirementStatus {
    private List<Integer> progress;

    public QuestRequirementStatus(List<Integer> progress){
        this.progress = progress;
    }

    public List<Integer> getProgress() {
        return progress;
    }

    public void setProgress(List<Integer> progress) {
        this.progress = progress;
        //todo write to json file
    }
}
