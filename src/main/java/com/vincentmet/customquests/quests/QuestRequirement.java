package com.vincentmet.customquests.quests;

import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.ForgeRegistries;

import java.awt.*;
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
            return entity.getName().getString();
        }

        @Override
        public ItemStack getItemStack() {
            return new ItemStack(Items.DIAMOND_SWORD);
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
            return itemStack.toString();
        }

        @Override
        public ItemStack getItemStack() {
            return itemStack;
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
            return itemStack.toString();
        }

        @Override
        public ItemStack getItemStack() {
            return itemStack;
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
            return itemStack.toString();
        }

        @Override
        public ItemStack getItemStack() {
            return itemStack;
        }
    }

    public static class TravelTo implements IQuestRequirement{
        private String dim;
        private BlockPos blockPos;
        private int radius;
        public TravelTo(String dim, BlockPos blockPos, int radius){
            this.dim = dim;
            this.blockPos = blockPos;
            this.radius = radius;
        }

        @Override
        public String toString() {
            return "Travel To";
        }

        @Override
        public String getLabelText() {
            return dim + " - " + blockPos.toString();
        }

        @Override
        public ItemStack getItemStack() {
            return new ItemStack(Items.COMPASS);
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