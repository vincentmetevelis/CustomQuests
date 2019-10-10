package com.vincentmet.customquests.screens;

import com.vincentmet.customquests.lib.LineColor;
import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.lib.Utils;
import com.vincentmet.customquests.quests.*;
import com.vincentmet.customquests.screens.elements.*;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.List;

public class ScreenQuestingDevice extends Screen {
    private PlayerEntity player;
    public static int activeQuestline = 0;
    public static int activeQuest = -1;
    public static int activeSubRequirement = 0;

    //todo replace next 2 vars with clean code
    private static int rewardButtonX;
    private static int rewardButtonY;

    public ScreenQuestingDevice(ITextComponent titleIn, PlayerEntity player) {
        super(titleIn);
        this.player = player;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        final int windowCenterX = this.width / 2;
        final int windowCenterY = this.height / 2;
        final int contentPosX = Ref.GUI_QUESTING_MARGIN_LEFT;
        final int contentPosY = Ref.GUI_QUESTING_MARGIN_TOP;
        final int contentAreaWidth = this.width - contentPosX;
        final int contentAreaHeight = this.height - contentPosY;
        final int contentAreaCenterX = contentPosX + contentAreaWidth / 2;
        final int contentAreaCenterY = contentPosY + contentAreaHeight / 2;
        final int contentAreaWidth1procent = (int)(contentAreaWidth * 0.01);
        final int contentAreaHeight1procent = (int)(contentAreaHeight* 0.01);
        final int nonTextHeight = 20;

        this.renderBackground();

        for(IQuestingGuiElement guiElement : Ref.ALL_GUI_ELEMENTS){
            guiElement.render(this, player, mouseX, mouseY);
        }

        int currentQuestlinesGuiHeight = 0;
        for(QuestLine questline : Ref.ALL_QUESTBOOK.getQuestlines()){
            new ButtonQuestline(Ref.GUI_QUESTLINES_MARGIN_LEFT, Ref.GUI_QUESTLINES_MARGIN_TOP + currentQuestlinesGuiHeight, questline).render(this, player, mouseX, mouseY);
            currentQuestlinesGuiHeight += 25;
        }

        if(activeQuest >= 0){
            int spacingIdRequirements = 0;
            int spacingIdRewards = 0;
            new Label(
                    contentAreaCenterX-(font.getStringWidth(Quest.getQuestFromId(activeQuest).getTitle())/2),
                    5,
                    Quest.getQuestFromId(activeQuest).getTitle(),
                    0xFF00FF,
                    false,
                    false
            ).render(this, player, mouseX, mouseY);
            //left side of the line
            new MultilineLabel(
                    contentPosX,
                    contentPosY,
                    Quest.getQuestFromId(activeQuest).getDescription(),
                    0xFFFF00,
                    (int)(contentAreaWidth*0.5)
            ).render(this, player, mouseX, mouseY);
            //right side of the line
            new Label(
                    contentAreaCenterX + contentAreaWidth1procent*2,
                    contentPosY,
                    "Requirements:",
                    0xFF000F,
                    false,
                    false
            ).render(this, player, mouseX, mouseY);
            spacingIdRequirements++;
            for(QuestRequirement requirement : Quest.getQuestFromId(activeQuest).getRequirements()){
                new Label(
                        contentAreaCenterX + contentAreaWidth1procent*2,
                        contentPosY + 7 + nonTextHeight * spacingIdRequirements,//the "6" is for putting the label a bit more centered in the Y gap
                        requirement.getType().toString() + ":",
                        0xFF00FF,
                        false,
                        false
                ).render(this, player, mouseX, mouseY);
                spacingIdRequirements++;
                for(IQuestRequirement subRequirement : requirement.getSubRequirements()){
                    new ItemBoxAndText(
                            contentAreaCenterX + contentAreaWidth1procent*4,
                            contentPosY + nonTextHeight * spacingIdRequirements,
                            subRequirement.getItemStack(),
                            subRequirement.getLabelText(),
                            0xFFFFFF,
                            false
                    ).render(this, player, mouseX, mouseY);
                    spacingIdRequirements++;
                }
            }

            new Label(
                    contentAreaCenterX + contentAreaWidth1procent*2,
                    contentAreaCenterY + 7,
                    "Rewards:",
                    0xFF000F,
                    false,
                    false
            ).render(this, player, mouseX, mouseY);
            for(QuestReward reward : Quest.getQuestFromId(activeQuest).getRewards()){
                new ItemBoxAndText(
                        contentAreaCenterX + contentAreaWidth1procent*4,
                        contentAreaCenterY + nonTextHeight + nonTextHeight * spacingIdRewards,
                        reward.getReward().getItemStack(),
                        reward.getReward().toString(),
                        0xFFFFFF,
                        false
                ).render(this, player, mouseX, mouseY);
                spacingIdRewards++;
            }
            List<IQuestReward> rewards = new ArrayList<>();
            for(QuestReward questReward : Quest.getQuestFromId(activeQuest).getRewards()){
                rewards.add(questReward.getReward());
            }
            new ButtonClaimReward(
                    ScreenQuestingDevice.rewardButtonX = contentAreaCenterX + contentAreaWidth1procent*4,
                    ScreenQuestingDevice.rewardButtonY = contentAreaCenterY + nonTextHeight + nonTextHeight * spacingIdRewards,
                    activeQuest,
                    rewards
            ).render(this, player, mouseX, mouseY);

            new Line(
                    contentAreaCenterX,
                    contentAreaCenterY,
                    this.width - contentAreaWidth1procent*2,
                    contentAreaCenterY,
                    LineColor.WHITE,
                    Ref.GUI_QUESTING_LINE_THICKNESS
            ).render(this, player, mouseX, mouseY);
            new Line(
                    contentAreaCenterX,
                    contentPosY,
                    contentAreaCenterX,
                    this.height - contentAreaHeight1procent*2,
                    LineColor.WHITE,
                    Ref.GUI_QUESTING_LINE_THICKNESS
            ).render(this, player, mouseX, mouseY);
        }else{
            new Label(
                    windowCenterX,
                    5,
                    Ref.ALL_QUESTBOOK.getTitle(),
                    0xFFFFFF,
                    false,
                    false
            ).render(this, player, mouseX, mouseY);
            for(int questId : Ref.ALL_QUESTBOOK.getQuestlines().get(activeQuestline).getQuests()){
                Quest quest = Quest.getQuestFromId(questId);
                LineColor lineColor = LineColor.BLACK;
                for(int dependencyId : quest.getDependencies()){
                    Quest qd = Quest.getQuestFromId(dependencyId);
                    if(Quest.hasQuestUncompletedDependenciesForPlayer(Utils.getUUID("vincentmet"), quest.getId())){
                        lineColor = LineColor.RED;
                    }else if(Quest.isQuestCompletedForPlayer(Utils.getUUID("vincentmet"), qd.getId()) && Quest.isQuestCompletedForPlayer(Utils.getUUID("vincentmet"), quest.getId())){
                        lineColor = LineColor.GREEN;
                    }else if(Quest.isQuestCompletedForPlayer(Utils.getUUID("vincentmet"), qd.getId())){
                        lineColor = LineColor.YELLOW;
                    }
                    if(Quest.areQuestsInSameQuestline(questId, dependencyId)) {
                        new Line(
                                quest.getPosition(),
                                qd.getPosition(),
                                lineColor,
                                Ref.GUI_QUESTING_LINE_THICKNESS
                        ).render(this, player, mouseX, mouseY);
                    }
                }
            }

            for(int questId : Ref.ALL_QUESTBOOK.getQuestlines().get(activeQuestline).getQuests()){
                Quest quest = Quest.getQuestFromId(questId);
                new ButtonQuest(
                        Ref.GUI_QUESTING_MARGIN_LEFT + quest.getPosition().getX(),
                        Ref.GUI_QUESTING_MARGIN_TOP + quest.getPosition().getY(),
                        quest,
                        Utils.getUUID("vincentmet")
                ).render(this, player, mouseX, mouseY);
            }
        }
        super.render(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton){
        for(IQuestingGuiElement guiElement : Ref.ALL_GUI_ELEMENTS){
            guiElement.onClick(this, player, mouseX, mouseY);
        }

        int currentQuestlinesGuiHeight = 0;
        for(QuestLine questline : Ref.ALL_QUESTBOOK.getQuestlines()){
            new ButtonQuestline(Ref.GUI_QUESTLINES_MARGIN_LEFT, Ref.GUI_QUESTLINES_MARGIN_TOP + currentQuestlinesGuiHeight, questline).onClick(this, player, mouseX, mouseY);
            currentQuestlinesGuiHeight += 25;
        }
        for(int questId : Ref.ALL_QUESTBOOK.getQuestlines().get(activeQuestline).getQuests()){
            Quest quest = Quest.getQuestFromId(questId);
            new ButtonQuest(
                    Ref.GUI_QUESTING_MARGIN_LEFT + quest.getPosition().getX(),
                    Ref.GUI_QUESTING_MARGIN_TOP + quest.getPosition().getY(),
                    quest,
                    Utils.getUUID("vincentmet")
            ).onClick(this, player, mouseX, mouseY);
        }

        if(activeQuest >= 0){
            List<IQuestReward> rewards = new ArrayList<>();
            for(QuestReward questReward : Quest.getQuestFromId(activeQuest).getRewards()){
                rewards.add(questReward.getReward());
            }
            new ButtonClaimReward(
                    ScreenQuestingDevice.rewardButtonX,
                    ScreenQuestingDevice.rewardButtonY,
                    activeQuest,
                    rewards
            ).onClick(this, player, mouseX, mouseY);
        }

        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}