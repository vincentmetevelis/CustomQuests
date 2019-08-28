package com.vincentmet.customquests.quests;

import javafx.util.Pair;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class QuestRequirement {
    private QuestRequirementType requirementType;
    private List<IQuestRequirement> subRequirements;

    public QuestRequirement(QuestRequirementType type, List<IQuestRequirement> subRequirements){
        this.requirementType = type;
        this.subRequirements = subRequirements;
    }

    public static class KillMob implements IQuestRequirement{
        private int amount;
        private EntityType entity;
        public KillMob(EntityType entity, int amount){
            this.amount = amount;
            this.entity = entity;
        }

        @Override
        public String toString() {
            return "Kill Entity";
        }

        @Override
        public String getLabelText() {
            return null;
        }
    }

    public static class ItemDetect implements IQuestRequirement{
        private ItemStack itemStack;
        public ItemDetect(ItemStack itemStack){
            this.itemStack = itemStack;
        }

        @Override
        public String toString() {
            return "Detect Item";
        }

        @Override
        public String getLabelText() {
            return null;
        }
    }

    public static class ItemCraft implements IQuestRequirement{
        private ItemStack itemStack;
        public ItemCraft(ItemStack itemStack){
            this.itemStack = itemStack;
        }

        @Override
        public String toString() {
            return "Craft Item";
        }

        @Override
        public String getLabelText() {
            return null;
        }
    }

    public static class ItemSubmit implements IQuestRequirement{
        private ItemStack itemStack;
        public ItemSubmit(ItemStack itemStack){
            this.itemStack = itemStack;
        }

        @Override
        public String toString() {
            return "Submit Item";
        }

        @Override
        public String getLabelText() {
            return null;
        }
    }

    public static class TravelTo implements IQuestRequirement{
        private int dimId;
        private BlockPos blockPos;
        public TravelTo(int dimId, BlockPos blockPos){
            this.dimId = dimId;
            this.blockPos = blockPos;
        }

        @Override
        public String toString() {
            return "Travel To";
        }

        @Override
        public String getLabelText() {
            return null;
        }
    }

    public void setRequirement(List<IQuestRequirement> subRequirements) {
        this.subRequirements = subRequirements;
    }

    public void setType(QuestRequirementType type) {
        this.requirementType = type;
    }

    public List<IQuestRequirement> getRequirement() {
        return subRequirements;
    }

    public QuestRequirementType getType() {
        return requirementType;
    }
}