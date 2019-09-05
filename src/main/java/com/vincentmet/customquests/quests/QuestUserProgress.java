package com.vincentmet.customquests.quests;

import com.vincentmet.customquests.lib.Utils;

import java.util.List;

public class QuestUserProgress {
    private String uuid;
    private List<Integer> completedQuestsIds;
    private  List<QuestStatus> questStatuses;

    public QuestUserProgress(String uuid, List<Integer> completedQuestsIds, List<QuestStatus> questStatuses){
        this.uuid = uuid;
        this.completedQuestsIds = completedQuestsIds;
        this.questStatuses = questStatuses;
    }

    public String getUuid() {
        return uuid;
    }

    public String getUserName(){
        return Utils.getDisplayName(uuid);
    }

    public List<Integer> getCompletedQuestsIds() {
        return completedQuestsIds;
    }

    public List<QuestStatus> getQuestStatuses() {
        return questStatuses;
    }

    public void setCompletedQuestsIds(List<Integer> completedQuestsIds) {
        this.completedQuestsIds = completedQuestsIds; //todo write to json file + helper functions
    }

    public void setQuestStatuses(List<QuestStatus> questStatuses) {
        this.questStatuses = questStatuses; //todo write to json file + helper functions
    }
}
