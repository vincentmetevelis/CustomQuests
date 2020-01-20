package com.vincentmet.customquests.screens.questingdeveicesubscreens;

import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.quests.party.Party;
import com.vincentmet.customquests.screens.elements.ButtonParty;
import com.vincentmet.customquests.screens.elements.ButtonPartySetting;
import com.vincentmet.customquests.screens.elements.IQuestingGuiElement;
import com.vincentmet.customquests.screens.elements.labels.Label;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;

public class SubScreenParties implements IQuestingGuiElement {//todo
    private Screen root;
    private static final int MARGIN_PARTY_BUTTONS_LEFT = 20;
    private static final int MARGIN_PARTY_BUTTONS_TOP = 20;
    private static final int MARGIN_PARTY_BUTTONS_RIGHT = 20;
    private int width = 0;
    private int height = 0;
    private static int activeParty = -1;
    private Label title;

    public SubScreenParties(Screen root){
        this.root = root;
    }

    @Override
    public void update(PlayerEntity player, double mouseX, double mouseY, int width, int height) {
        this.width = width;
        this.height = height;
        reloadTitle();
        this.title.update(player, mouseX, mouseY, 0, 0);
    }

    @Override
    public void render(PlayerEntity player, double mouseX, double mouseY) {
        this.title.render(player, mouseX, mouseY);

        int currentPartyGuiHeight = 0;
        for(Party party : Ref.ALL_QUESTING_PARTIES){
            new ButtonParty(root, Ref.GUI_QUESTLINES_MARGIN_LEFT, Ref.GUI_QUESTLINES_MARGIN_TOP + currentPartyGuiHeight, party).render(player, mouseX, mouseY);
            currentPartyGuiHeight += 25;
        }
        if(activeParty>=0){
            int currentPartySettingsGuiHeight = 0;
            new ButtonPartySetting(root, Ref.GUI_QUESTING_MARGIN_LEFT, Ref.GUI_QUESTLINES_MARGIN_TOP + currentPartySettingsGuiHeight, "Join").render(player, mouseX, mouseY);
            currentPartySettingsGuiHeight += 25;
        }
    }

    @Override
    public void onClick(PlayerEntity player, double mouseX, double mouseY) {
        this.title.onClick(player, mouseX, mouseY);
        int currentPartyGuiHeight = 0;
        for(Party party : Ref.ALL_QUESTING_PARTIES){
            new ButtonParty(root, Ref.GUI_QUESTLINES_MARGIN_LEFT, Ref.GUI_QUESTLINES_MARGIN_TOP + currentPartyGuiHeight, party).onClick(player, mouseX, mouseY);
            currentPartyGuiHeight += 25;
        }
    }

    @Override
    public void onKeyPress(int key, int mod) {

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
        return true;
    }

    public void reloadTitle(){
        this.title = new Label(
                root,
                "Parties",
                (MARGIN_PARTY_BUTTONS_LEFT + ButtonParty.WIDTH + MARGIN_PARTY_BUTTONS_RIGHT) + (this.width - (MARGIN_PARTY_BUTTONS_LEFT + ButtonParty.WIDTH + MARGIN_PARTY_BUTTONS_RIGHT)>>1),
                MARGIN_PARTY_BUTTONS_TOP>>1,
                0xFFFFFF,
                true,
                true
        );
    }

    public static void setActiveParty(int activeParty) {
        SubScreenParties.activeParty = activeParty;
    }

    public static int getActiveParty() {
        return SubScreenParties.activeParty;
    }
}