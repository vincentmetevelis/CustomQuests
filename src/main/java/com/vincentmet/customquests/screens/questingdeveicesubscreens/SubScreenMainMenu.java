package com.vincentmet.customquests.screens.questingdeveicesubscreens;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.vincentmet.customquests.lib.MouseDirection;
import com.vincentmet.customquests.screens.elements.IQuestingGuiElement;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;

public class SubScreenMainMenu implements IQuestingGuiElement {
    private Screen root;
    private int width = 0;
    private int height = 0;

    public SubScreenMainMenu(Screen root){
        this.root = root;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public void update(PlayerEntity player, double mouseX, double mouseY, int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void render(MatrixStack stack, PlayerEntity player, double mouseX, double mouseY) {
    }

    @Override
    public void onClick(PlayerEntity player, double mouseX, double mouseY) {

    }

    @Override
    public void onKeyPress(int key, int mod) {

    }

    @Override
    public void onMouseScroll(double mouseX, double mouseY, MouseDirection direction) {

    }
}