package com.vincentmet.customquests.screens.elements;

import net.minecraft.entity.player.PlayerEntity;

public interface IQuestingGuiElement {
    int getWidth();
    int getHeight();
    boolean isVisible();
    void update(PlayerEntity player, double mouseX, double mouseY, int width, int height);
    void render(PlayerEntity player, double mouseX, double mouseY);
    void onClick(PlayerEntity player, double mouseX, double mouseY);
}