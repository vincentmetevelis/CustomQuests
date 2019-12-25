package com.vincentmet.customquests.quests;

import net.minecraft.item.ItemStack;

public interface IQuestRequirement extends IJsonProvider{
    String toString();
    String getLabelText();
    ItemStack getItemStack();
    int getCompletionNumber();
}
