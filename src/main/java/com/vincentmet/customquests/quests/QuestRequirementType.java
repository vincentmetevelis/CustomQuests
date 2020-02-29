package com.vincentmet.customquests.quests;

import com.vincentmet.customquests.lib.Utils;

public enum QuestRequirementType {
    ITEM_DETECT("item_detect"),
    ITEM_DELIVER("item_deliver"),
    CRAFTING_DETECT("crafting_detect"),
    KILL_MOB("kill_mob"),
    TRAVEL_TO("travel_to"),
    CHECK_BOX("check_box"),
    RF_DELIVER("rf_deliver"),
    RF_GENERATE("rf_generate"),
    BLOCK_MINE("block_mine");

    String description;
    QuestRequirementType(String text){
        this.description = Utils.getFormattedText(".requirement_type." + text);
    }

    @Override
    public String toString() {
        return description;
    }
}