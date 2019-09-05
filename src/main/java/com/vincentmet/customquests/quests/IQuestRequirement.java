package com.vincentmet.customquests.quests;

import net.minecraft.item.ItemStack;

public interface IQuestRequirement {
    String toString();
    String getLabelText();
    ItemStack getItemStack();
}
