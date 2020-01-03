package com.vincentmet.customquests.screens.questingdeveicesubscreens;

import com.vincentmet.customquests.screens.elements.IQuestingGuiElement;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;

public class SubScreenSettings implements IQuestingGuiElement {
    private Screen root;
    private int lastWidth = 0;
    private int lastHeight = 0;

    public SubScreenSettings(Screen root){
        this.root = root;

    }

    @Override
    public int getWidth() {
        return lastWidth;
    }

    @Override
    public int getHeight() {
        return lastHeight;
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public void update(IQuestingGuiElement gui, PlayerEntity player, double mouseX, double mouseY, int width, int height) {
        this.lastWidth = width;
        this.lastHeight = height;
    }

    @Override
    public void render(IQuestingGuiElement gui, PlayerEntity player, double mouseX, double mouseY) {

    }

    @Override
    public void onClick(IQuestingGuiElement gui, PlayerEntity player, double mouseX, double mouseY) {

    }
}