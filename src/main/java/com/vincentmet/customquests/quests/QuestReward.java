package com.vincentmet.customquests.quests;

import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;

public class QuestReward {
    private QuestRewardType type;
    private IQuestReward reward;

    public QuestReward(QuestRewardType type, IQuestReward reward){
        this.type = type;
        this.reward = reward;
    }

    public QuestRewardType getType() {
        return type;
    }

    public IQuestReward getReward() {
        return reward;
    }

    public static class Items implements IQuestReward{
        private ItemStack items;
        public Items(ItemStack items){
            this.items = items;
        }

        @Override
        public void executeReward() {

        }

        @Override
        public String toString() {
            return items.toString();
        }
    }

    public static class Command implements IQuestReward{
        private String command;
        public Command(String command){
            this.command = command;
        }

        @Override
        public void executeReward() {

        }

        @Override
        public String toString() {
            return command;
        }
    }

    public static class SpawnEntity implements IQuestReward{
        private EntityType entity;
        public SpawnEntity(EntityType entity){
            this.entity = entity;
        }

        @Override
        public void executeReward() {

        }

        @Override
        public String toString() {
            return entity.toString();
        }
    }
}