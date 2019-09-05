package com.vincentmet.customquests.quests;

public enum QuestRequirementType {
    ITEM_DETECT("Item(s) to detect"),
    ITEM_DELIVER("Item(s) to hand in"),
    CRAFTING_DETECT("Item(s) to craft"),
    KILL_MOB("Mob(s) to kill"),
    TRAVEL_TO("Travel to location(s)"),
    RF_DELIVER("RF to hand in"),
    RF_GENERATE("RF to generate"),
    BLOCK_MINE("Block(s) to mine");

    String description;
    QuestRequirementType(String description){
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}