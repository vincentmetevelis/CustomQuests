package com.vincentmet.customquests.screens.elements;

import net.minecraft.entity.player.PlayerEntity;

public interface IQuestingGuiElement {
    int getWidth();
    int getHeight();
    boolean isVisible();
    void update(IQuestingGuiElement gui, PlayerEntity player, double mouseX, double mouseY, int width, int height);
    void render(IQuestingGuiElement gui, PlayerEntity player, double mouseX, double mouseY);
    void onClick(IQuestingGuiElement gui, PlayerEntity player, double mouseX, double mouseY);
}