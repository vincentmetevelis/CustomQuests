package com.vincentmet.customquests.quests;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface IQuestReward extends IJsonProvider {
    void executeReward(PlayerEntity player);
    String toString();
    ItemStack getItemStack();
}
