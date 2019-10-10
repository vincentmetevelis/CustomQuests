package com.vincentmet.customquests.quests;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;

public interface IQuestRequirement {
    String toString();
    String getLabelText();
    ItemStack getItemStack();
    JsonObject getJson();
    int getCompletionNumber();
}
