package com.vincentmet.customquests.quests;

import com.vincentmet.customquests.lib.Utils;

import java.util.List;

public class QuestUserProgress {
    private String uuid;
    private List<Integer> completedQuestsIds;

    public QuestUserProgress(String uuid, List<Integer> completedQuestsIds){
        this.uuid = uuid;
        this.completedQuestsIds = completedQuestsIds;
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
}
