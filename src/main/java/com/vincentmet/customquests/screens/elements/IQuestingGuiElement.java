package com.vincentmet.customquests.screens.elements;

import net.minecraft.entity.player.PlayerEntity;

public interface IQuestingGuiElement {
    void update(PlayerEntity player, double mouseX, double mouseY, int width, int height);
    void render(PlayerEntity player, double mouseX, double mouseY);
    void onClick(PlayerEntity player, double mouseX, double mouseY);
    void onKeyPress(int key, int mod);
    int getWidth();
    int getHeight();
    boolean isVisible();//no use yet for this one
}