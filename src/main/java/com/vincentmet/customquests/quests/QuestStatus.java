package com.vincentmet.customquests.quests;

import java.util.List;

public class QuestStatus {
    private int questId;
    private boolean claimed;
    private List<QuestRequirementStatus> questRequirementStatuses;

    public QuestStatus(int questId, boolean claimed, List<QuestRequirementStatus> questRequirementStatuses){
        this.questId = questId;
        this.claimed = claimed;
        this.questRequirementStatuses = questRequirementStatuses;
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
        this.claimed = claimed; //todo update json file
    }

    public void setQuestId(int questId) {
        this.questId = questId; // todo update json file
    }

    public void setQuestRequirementStatuses(List<QuestRequirementStatus> questRequirementStatuses) {
        this.questRequirementStatuses = questRequirementStatuses; //todo update json file
    }
}
