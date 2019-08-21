package com.vincentmet.customquests.quests;

import com.vincentmet.customquests.lib.Ref;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class Quest {
    private int id;
    private String title;
    private String description;
    private Item icon;
    private List<Integer> dependencyIds;
    private List<QuestRequirement> requirements;
    private List<QuestReward> rewards;
    private QuestPosition position;

    public Quest(int id, String title, String description, Item icon, List<Integer> dependencyIds, List<QuestRequirement> requirements, List<QuestReward> rewards, QuestPosition position){
        this.id = id;
        this.title = title;
        this.description = description;
        this.icon = icon;
        this.dependencyIds = dependencyIds;
        this.requirements = requirements;
        this.rewards = rewards;
        this.position = position;
    }

    public static Quest getQuestFromId(int id){
        for(int i=0; i<Ref.ALL_QUESTS.size(); i++){
            if(Ref.ALL_QUESTS.get(i).id == id){
                return Ref.ALL_QUESTS.get(i);
            }
        }
        return null;
    }

    public static boolean isQuestCompletedForPlayer(String playerUuid, int questId){
        for(QuestUserProgress userprogress : Ref.ALL_QUESTING_PROGRESS){
            if(userprogress.getUuid().equals(playerUuid)){
                for(int id : userprogress.getCompletedQuestsIds()){
                    if(questId == id){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean hasUnclaimedRewardsForPlayer(String playerUuid, int questId){
        if(isQuestCompletedForPlayer(playerUuid, questId)){
            if(true) {//todo
                return false;
            }
        }
        return false;
    }

    public static boolean hasQuestUncompletedDependenciesForPlayer(String uuid, int questId){
        if(Quest.getQuestFromId(questId).getDependencies().isEmpty()){
            return false;
        }/*else if(){

        }*/
        return true;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRequirement(List<QuestRequirement> requirement) {
        this.requirements = requirement;
    }

    public void setDependencies(List<Integer> dependencyIds) {
        this.dependencyIds = dependencyIds;
    }

    public void setIcon(Item icon) {
        this.icon = icon;
    }

    public void setPosition(QuestPosition position) {
        this.position = position;
    }

    public void setRewards(List<QuestReward> rewards) {
        this.rewards = rewards;
    }

    public int getId(){
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Item getIcon() {
        return icon;
    }

    public List<Integer> getDependencies() {
        return dependencyIds;
    }

    public List<QuestRequirement> getRequirements() {
        return requirements;
    }

    public List<QuestReward> getRewards() {
        return rewards;
    }

    public QuestPosition getPosition() {
        return position;
    }
}