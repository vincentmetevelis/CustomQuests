package com.vincentmet.customquests.screens.elements;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.vincentmet.customquests.lib.MouseDirection;
import net.minecraft.entity.player.PlayerEntity;

public interface IQuestingGuiElement {
    void update(PlayerEntity player, double mouseX, double mouseY, int width, int height);
    void render(MatrixStack stack, PlayerEntity player, double mouseX, double mouseY);
    void onClick(PlayerEntity player, double mouseX, double mouseY);
    void onKeyPress(int key, int mod);
    void onMouseScroll(double mouseX, double mouseY, MouseDirection direction);
    int getWidth();
    int getHeight();
    boolean isVisible();//no use yet for this one
}