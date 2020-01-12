package com.vincentmet.customquests.screens.questingdeveicesubscreens;

import com.vincentmet.customquests.lib.LineColor;
import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.quests.*;
import com.vincentmet.customquests.screens.elements.IQuestingGuiElement;
import com.vincentmet.customquests.screens.elements.buttons.ButtonClaimReward;
import com.vincentmet.customquests.screens.elements.buttons.ButtonHandInRequirement;
import com.vincentmet.customquests.screens.elements.labels.ItemBoxAndText;
import com.vincentmet.customquests.screens.elements.labels.Label;
import com.vincentmet.customquests.screens.elements.labels.Line;
import com.vincentmet.customquests.screens.elements.labels.MultilineLabel;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.List;

public class SubScreenQuestDetails implements IQuestingGuiElement {
    private Screen root;

    private static final int MARGIN_CONTENT_TOP = 20;
    private static final int MARGIN_CONTENT_BOTTOM = 20;
    private static final int MARGIN_CONTENT_LEFT = 20;
    private static final int MARGIN_CONTENT_RIGHT = 20;

    private static final int MARGIN_CLAIM_BUTTON_LEFT = 10;
    private static final int MARGIN_CLAIM_BUTTON_TOP = 10;

    private int width = 0;
    private int height = 0;
    private static int activeQuest = -1;

    //todo replace next 3 vars with clean code
    private static int rewardButtonX;
    private static int rewardButtonY;
    private static int nonTextHeight = 20;

    public SubScreenQuestDetails(Screen root){
        this.root = root;
    }

    @Override
    public void update(PlayerEntity player, double mouseX, double mouseY, int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void render(PlayerEntity player, double mouseX, double mouseY) {
        if (activeQuest >= 0) {//todo add everything the vars instead of creating new ones
            int spacingIdRequirements = 0;
            int spacingIdRewards = 0;
            new Label(
                    root,
                    Quest.getQuestFromId(activeQuest).getTitle(),
                    width >> 1,
                    MARGIN_CONTENT_TOP>>1,
                    0xFFFFFF,
                    true,
                    true
            ).render(player, mouseX, mouseY);
            //left side of the line
            new MultilineLabel(
                    root,
                    MARGIN_CONTENT_LEFT,
                    MARGIN_CONTENT_TOP + MARGIN_CLAIM_BUTTON_TOP,
                    Quest.getQuestFromId(activeQuest).getDescription(),
                    0xFFFFFF,
                    (width>>1) - 2*MARGIN_CLAIM_BUTTON_LEFT
            ).render(player, mouseX, mouseY);
            //right side of the line
            new Label(
                    root,
                    "Requirements:",
                    (width>>1) + MARGIN_CLAIM_BUTTON_LEFT,
                    MARGIN_CONTENT_TOP + MARGIN_CLAIM_BUTTON_TOP,
                    0xFFFFFF,
                    false,
                    false
            ).render(player, mouseX, mouseY);
            spacingIdRequirements++;
            int countReq = 0;
            for (QuestRequirement requirement : Quest.getQuestFromId(activeQuest).getRequirements()) {
                int handInOffsetY = MARGIN_CONTENT_TOP + MARGIN_CLAIM_BUTTON_TOP + 7 + nonTextHeight * spacingIdRequirements;
                new Label(
                        root,
                        requirement.getType().toString() + ":",
                        (width>>1) + MARGIN_CLAIM_BUTTON_LEFT,
                        MARGIN_CONTENT_TOP + MARGIN_CLAIM_BUTTON_TOP + 7 + nonTextHeight * spacingIdRequirements,//the "7" is for putting the label a bit more centered in the Y gap
                        0xFFFFFF,
                        false,
                        false
                ).render(player, mouseX, mouseY);
                spacingIdRequirements++;
                for (IQuestRequirement subRequirement : requirement.getSubRequirements()) {
                    new ItemBoxAndText(
                            root,
                            (width>>1) + MARGIN_CLAIM_BUTTON_LEFT * 2,
                            MARGIN_CONTENT_TOP + MARGIN_CLAIM_BUTTON_TOP + nonTextHeight * spacingIdRequirements,
                            subRequirement.getItemStack(),
                            subRequirement.getLabelText(),
                            0xFFFFFF,
                            false
                    ).render(player, mouseX, mouseY);
                    spacingIdRequirements++;
                }
                if (requirement.getType() == QuestRequirementType.ITEM_DELIVER) {
                    new ButtonHandInRequirement(
                            root,
                            this.width - 64 - 5,
                            handInOffsetY,
                            activeQuest,
                            countReq,
                            requirement
                    ).render(player, mouseX, mouseY);
                }
                countReq++;
            }

            new Label(
                    root,
                    "Rewards:",
                    (width>>1) + MARGIN_CLAIM_BUTTON_LEFT,
                    ((this.height-MARGIN_CONTENT_TOP)>>1) + 7,
                    0xFFFFFF,
                    false,
                    false
            ).render(player, mouseX, mouseY);
            for (QuestReward reward : Quest.getQuestFromId(activeQuest).getRewards()) {
                new ItemBoxAndText(
                        root,
                        (width>>1) + MARGIN_CLAIM_BUTTON_LEFT,
                        ((this.height-MARGIN_CONTENT_TOP)>>1) + nonTextHeight + nonTextHeight * spacingIdRewards,
                        reward.getReward().getItemStack(),
                        reward.getReward().toString(),
                        0xFFFFFF,
                        false
                ).render(player, mouseX, mouseY);
                spacingIdRewards++;
            }
            List<IQuestReward> rewards = new ArrayList<>();
            for (QuestReward questReward : Quest.getQuestFromId(activeQuest).getRewards()) {
                rewards.add(questReward.getReward());
            }
            new ButtonClaimReward(
                    root,
                    rewardButtonX = (width>>1) + MARGIN_CLAIM_BUTTON_LEFT,
                    rewardButtonY = ((this.height-MARGIN_CONTENT_TOP)>>1) + nonTextHeight + nonTextHeight * spacingIdRewards,
                    activeQuest
            ).render(player, mouseX, mouseY);

            new Line(
                    root,
                    this.width>>1,
                    (this.height - MARGIN_CONTENT_TOP)>>1,
                    this.width - MARGIN_CONTENT_RIGHT,
                    (this.height - MARGIN_CONTENT_TOP)>>1,
                    LineColor.WHITE,
                    Ref.GUI_QUESTING_LINE_THICKNESS
            ).render(player, mouseX, mouseY);
            new Line(
                    root,
                    width>>1,
                    MARGIN_CONTENT_TOP,
                    width>>1,
                    this.height - MARGIN_CONTENT_BOTTOM,
                    LineColor.WHITE,
                    Ref.GUI_QUESTING_LINE_THICKNESS
            ).render(player, mouseX, mouseY);
        }
    }

    @Override
    public void onClick(PlayerEntity player, double mouseX, double mouseY) {//todo add everything the vars instead of creating new ones
        if(activeQuest >= 0){
            int spacingIdRequirements = 0;
            int spacingIdRewards = 0;
            int countReq = 0;
            spacingIdRequirements++;
            for(QuestRequirement requirement : Quest.getQuestFromId(activeQuest).getRequirements()){
                int handInOffsetY = MARGIN_CONTENT_TOP + 7 + nonTextHeight * spacingIdRequirements;
                spacingIdRequirements++;
                for(IQuestRequirement subRequirement : requirement.getSubRequirements()){
                    spacingIdRequirements++;
                }
                if(requirement.getType() == QuestRequirementType.ITEM_DELIVER){
                    new ButtonHandInRequirement(
                            root,
                            this.width - 64 -5,
                            handInOffsetY,
                            activeQuest,
                            countReq,
                            requirement
                    ).onClick(player, mouseX, mouseY);
                }
                countReq++;
            }


            List<IQuestReward> rewards = new ArrayList<>();
            for(QuestReward questReward : Quest.getQuestFromId(activeQuest).getRewards()){
                rewards.add(questReward.getReward());
            }
            new ButtonClaimReward(
                    root,
                    rewardButtonX,
                    rewardButtonY,
                    activeQuest
            ).onClick(player, mouseX, mouseY);
        }
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

    public static int getActiveQuest() {
        return SubScreenQuestDetails.activeQuest;
    }

    public static void setActiveQuest(int activeQuest) {
        SubScreenQuestDetails.activeQuest = activeQuest;
    }
}