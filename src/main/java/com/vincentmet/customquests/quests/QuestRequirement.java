package com.vincentmet.customquests.quests;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.vincentmet.customquests.lib.Ref;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class QuestRequirement {
    private QuestRequirementType requirementType;
    private List<IQuestRequirement> subRequirements;

    public QuestRequirement(QuestRequirementType type, List<IQuestRequirement> subRequirements){
        this.requirementType = type;
        this.subRequirements = subRequirements;
    }

    public JsonObject getJson(){
        JsonObject json = new JsonObject();
        switch(requirementType){
            case ITEM_DELIVER:
                json.addProperty("type", "ITEM_DELIVER");
                break;
            case ITEM_DETECT:
                json.addProperty("type", "ITEM_DETECT");
                break;
            case CRAFTING_DETECT:
                json.addProperty("type", "CRAFTING_DETECT");
                break;
            case TRAVEL_TO:
                json.addProperty("type", "TRAVEL_TO");
                break;
            case KILL_MOB:
                json.addProperty("type", "KILL_MOB");
                break;
            case BLOCK_MINE:
                json.addProperty("type", "ITEM_DELIVER");
                break;
            case RF_DELIVER:
                json.addProperty("type", "RF_DELIVER");
                break;
            case RF_GENERATE:
                json.addProperty("type", "RF_GENERATE");
                break;
        }
        JsonArray subRequirementsArray = new JsonArray();
        for(IQuestRequirement subQuestRequirement: subRequirements){
            subRequirementsArray.add(subQuestRequirement.getJson());
        }
        json.add("sub_requirements", subRequirementsArray);
        return json;
    }

    public static class KillMob implements IQuestRequirement{
        private int amount;
        private EntityType entity;
        public KillMob(EntityType entity, int amount){
            this.amount = amount;
            this.entity = entity;
        }

        @Override
        public JsonObject getJson() {
            JsonObject json = new JsonObject();
            json.addProperty("entity", ForgeRegistries.ENTITIES.getKey(entity).toString());
            json.addProperty("amount", amount);
            return json;
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

        @Override
        public int getCompletionNumber() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
            Ref.shouldSaveNextTick = true;
        }

        public void setEntity(EntityType entity) {
            this.entity = entity;
            Ref.shouldSaveNextTick = true;
        }
    }

    public static class ItemDetect implements IQuestRequirement{
        private ItemStack itemStack;
        public ItemDetect(ItemStack itemStack){
            this.itemStack = itemStack;
        }

        @Override
        public JsonObject getJson() {
            JsonObject json = new JsonObject();
            json.addProperty("item", ForgeRegistries.ITEMS.getKey(itemStack.getItem()).toString());
            json.addProperty("amount", itemStack.getCount());
            json.addProperty("nbt", itemStack.getTag()==null?"{}":itemStack.getTag().toString());
            return json;
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

        @Override
        public int getCompletionNumber() {
            return itemStack.getCount();
        }

        public void setItemStack(ItemStack itemStack) {
            this.itemStack = itemStack;
            Ref.shouldSaveNextTick = true;
        }
    }

    public static class ItemCraft implements IQuestRequirement{
        private ItemStack itemStack;
        public ItemCraft(ItemStack itemStack){
            this.itemStack = itemStack;
        }

        @Override
        public JsonObject getJson() {
            JsonObject json = new JsonObject();
            json.addProperty("item", ForgeRegistries.ITEMS.getKey(itemStack.getItem()).toString());
            json.addProperty("amount", itemStack.getCount());
            json.addProperty("nbt", itemStack.getTag()==null?"{}":itemStack.getTag().toString());
            return json;
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

        @Override
        public int getCompletionNumber() {
            return itemStack.getCount();
        }

        public void setItemStack(ItemStack itemStack) {
            this.itemStack = itemStack;
            Ref.shouldSaveNextTick = true;
        }
    }

    public static class ItemSubmit implements IQuestRequirement{
        private ItemStack itemStack;
        public ItemSubmit(ItemStack itemStack){
            this.itemStack = itemStack;
        }

        @Override
        public JsonObject getJson() {
            JsonObject json = new JsonObject();
            json.addProperty("item", ForgeRegistries.ITEMS.getKey(itemStack.getItem()).toString());
            json.addProperty("amount", itemStack.getCount());
            json.addProperty("nbt", itemStack.getTag()==null?"{}":itemStack.getTag().toString());
            return json;
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

        @Override
        public int getCompletionNumber() {
            return itemStack.getCount();
        }

        public void setItemStack(ItemStack itemStack) {
            this.itemStack = itemStack;
            Ref.shouldSaveNextTick = true;
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
        public JsonObject getJson() {
            JsonObject json = new JsonObject();
            json.addProperty("dim", dim);
            json.addProperty("x", blockPos.getX());
            json.addProperty("y", blockPos.getY());
            json.addProperty("z", blockPos.getZ());
            json.addProperty("radius", radius);
            return json;
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

        @Override
        public int getCompletionNumber() {
            return 1;
        }

        public String getDim() {
            return dim;
        }

        public BlockPos getBlockPos() {
            return blockPos;
        }

        public int getRadius() {
            return radius;
        }

        public void setBlockPos(BlockPos blockPos) {
            this.blockPos = blockPos;
            Ref.shouldSaveNextTick = true;
        }

        public void setDim(String dim) {
            this.dim = dim;
            Ref.shouldSaveNextTick = true;
        }

        public void setRadius(int radius) {
            this.radius = radius;
            Ref.shouldSaveNextTick = true;
        }
    }

    public void setRequirements(List<IQuestRequirement> subRequirements) {
        this.subRequirements = subRequirements;
        Ref.shouldSaveNextTick = true;
    }

    public void addRequirement(IQuestRequirement subRequirement){
        this.subRequirements.add(subRequirement);
        Ref.shouldSaveNextTick = true;
    }

    public void deleteRequirement(IQuestRequirement subRequirement){
        this.subRequirements.remove(subRequirement);
        Ref.shouldSaveNextTick = true;
    }

    public void setType(QuestRequirementType type) {
        this.requirementType = type;
        Ref.shouldSaveNextTick = true;
    }

    public List<IQuestRequirement> getSubRequirements() {
        return subRequirements;
    }

    public QuestRequirementType getType() {
        return requirementType;
    }

    @Override
    public String toString() {
        return requirementType + " + " + subRequirements;
    }
}