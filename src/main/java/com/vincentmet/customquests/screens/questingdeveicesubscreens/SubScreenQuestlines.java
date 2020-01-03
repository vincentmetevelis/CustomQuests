package com.vincentmet.customquests.screens.questingdeveicesubscreens;

import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.quests.QuestLine;
import com.vincentmet.customquests.screens.ScreenQuestingDevice;
import com.vincentmet.customquests.screens.elements.IQuestingGuiElement;
import com.vincentmet.customquests.screens.elements.buttons.ButtonQuestline;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.List;

public class SubScreenQuestlines implements IQuestingGuiElement {
    private Screen root;
    private static int MARGIN_QUESTLINE_BUTTONS_LEFT = 20;
    private static int MARGIN_QUESTLINE_BUTTONS_TOP = 20;
    private int width = 0;
    private int height = 0;
    private List<ButtonQuestline> questlineButtons = new ArrayList<>();

    public SubScreenQuestlines(Screen root){
        this.root = root;
        reloadQuestlines();
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
    public void update(IQuestingGuiElement gui, PlayerEntity player, double mouseX, double mouseY, int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void render(IQuestingGuiElement gui, PlayerEntity player, double mouseX, double mouseY) {
        questlineButtons.forEach(buttonQuestline -> buttonQuestline.render(this, player, mouseX, mouseY));
    }

    @Override
    public void onClick(IQuestingGuiElement gui, PlayerEntity player, double mouseX, double mouseY) {
        questlineButtons.forEach(buttonQuestline -> buttonQuestline.onClick(this, player, mouseX, mouseY));
    }

    public void reloadQuestlines(){
        questlineButtons.clear();
        int currentQuestlinesGuiHeight = 0;
        for (QuestLine questline : Ref.ALL_QUESTBOOK.getQuestlines()) {
            questlineButtons.add(new ButtonQuestline(root, MARGIN_QUESTLINE_BUTTONS_LEFT, MARGIN_QUESTLINE_BUTTONS_TOP + currentQuestlinesGuiHeight++ * 25, questline));
        }
    }
}