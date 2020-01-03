package com.vincentmet.customquests.screens;

import com.vincentmet.customquests.lib.LineColor;
import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.lib.Utils;
import com.vincentmet.customquests.quests.*;
import com.vincentmet.customquests.quests.party.Party;
import com.vincentmet.customquests.screens.elements.*;
import com.vincentmet.customquests.screens.elements.buttons.ButtonClaimReward;
import com.vincentmet.customquests.screens.elements.buttons.ButtonHandInRequirement;
import com.vincentmet.customquests.screens.elements.buttons.ButtonQuest;
import com.vincentmet.customquests.screens.elements.buttons.ButtonQuestline;
import com.vincentmet.customquests.screens.elements.labels.ItemBoxAndText;
import com.vincentmet.customquests.screens.elements.labels.Label;
import com.vincentmet.customquests.screens.elements.labels.Line;
import com.vincentmet.customquests.screens.elements.labels.MultilineLabel;
import com.vincentmet.customquests.screens.questingdeveicesubscreens.*;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import java.util.*;

@OnlyIn(Dist.CLIENT)
public class ScreenQuestingDevice extends Screen {
    private PlayerEntity player;
    public static int activeQuestline = 0;
    public static int activeQuest = -1;
    public static boolean isPartyScreenOpen = false;
    public static int activeParty = -1;

    //todo replace next 2 vars with clean code
    private static int rewardButtonX;
    private static int rewardButtonY;

    public static Map<String, IQuestingGuiElement> screens = new HashMap<>();
    public static SubScreensQuestingDevice activeScreen = SubScreensQuestingDevice.QUESTLINES;

    public ScreenQuestingDevice(ITextComponent titleIn, PlayerEntity player) {
        super(titleIn);
        this.player = player;

        screens.put("menu", new SubScreenMainMenu(this));
        screens.put("parties", new SubScreenParties(this));
        screens.put("questlines", new SubScreenQuestlines(this));
        screens.put("settings", new SubScreenSettings(this));
        screens.put("quest_details", new SubScreenQuestDetails(this));
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        switch(activeScreen){
            case MAIN_MENU:
                screens.entrySet().stream().filter(stringIQuestingSubScreenEntry -> stringIQuestingSubScreenEntry.getKey().equals("menu")).forEach(stringIQuestingSubScreenEntry -> {
                    stringIQuestingSubScreenEntry.getValue().update(null, player, mouseX, mouseY, this.width, this.height);
                    stringIQuestingSubScreenEntry.getValue().render(null, player, mouseX, mouseY);
                });
                break;
            case PARTY_SCREEN:
                screens.entrySet().stream().filter(stringIQuestingSubScreenEntry -> stringIQuestingSubScreenEntry.getKey().equals("parties")).forEach(stringIQuestingSubScreenEntry -> {
                    stringIQuestingSubScreenEntry.getValue().update(null, player, mouseX, mouseY, this.width, this.height);
                    stringIQuestingSubScreenEntry.getValue().render(null, player, mouseX, mouseY);
                });
                break;
            case QUESTLINES:
                screens.entrySet().stream().filter(stringIQuestingSubScreenEntry -> stringIQuestingSubScreenEntry.getKey().equals("questlines")).forEach(stringIQuestingSubScreenEntry -> {
                    stringIQuestingSubScreenEntry.getValue().update(null, player, mouseX, mouseY, this.width, this.height);
                    stringIQuestingSubScreenEntry.getValue().render(null, player, mouseX, mouseY);
                });
                break;
            case SETTINGS:
                screens.entrySet().stream().filter(stringIQuestingSubScreenEntry -> stringIQuestingSubScreenEntry.getKey().equals("settings")).forEach(stringIQuestingSubScreenEntry -> {
                    stringIQuestingSubScreenEntry.getValue().update(null, player, mouseX, mouseY, this.width, this.height);
                    stringIQuestingSubScreenEntry.getValue().render(null, player, mouseX, mouseY);
                });
                break;
            case QUEST_DETAILS:
                screens.entrySet().stream().filter(stringIQuestingSubScreenEntry -> stringIQuestingSubScreenEntry.getKey().equals("quest_details")).forEach(stringIQuestingSubScreenEntry -> {
                    stringIQuestingSubScreenEntry.getValue().update(null, player, mouseX, mouseY, this.width, this.height);
                    stringIQuestingSubScreenEntry.getValue().render(null, player, mouseX, mouseY);
                });
                break;
        }


















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

        new ButtonParties(this, 0, 0).render(null, player, mouseX, mouseY);

        if(isPartyScreenOpen) {
            //todo party stuff drawing
            new Label(
                    this,
                    windowCenterX,
                    5,
                    "Parties",
                    0xFFFFFF,
                    true,
                    false
            ).render(null, player, mouseX, mouseY);

            int currentPartyGuiHeight = 0;
            for(Party party : Ref.ALL_QUESTING_PARTIES){
                new ButtonParty(this, Ref.GUI_QUESTLINES_MARGIN_LEFT, Ref.GUI_QUESTLINES_MARGIN_TOP + currentPartyGuiHeight, party).render(null, player, mouseX, mouseY);
                currentPartyGuiHeight += 25;
            }
            if(activeParty>=0){
                int currentPartySettingsGuiHeight = 0;
                new ButtonPartySetting(this, Ref.GUI_QUESTING_MARGIN_LEFT, Ref.GUI_QUESTLINES_MARGIN_TOP + currentPartySettingsGuiHeight, "Join").render(null, player, mouseX, mouseY);
                currentPartySettingsGuiHeight += 25;
            }
        }else {
            if (activeQuest >= 0) {
                int spacingIdRequirements = 0;
                int spacingIdRewards = 0;
                new Label(
                        this,
                        contentAreaCenterX - (font.getStringWidth(Quest.getQuestFromId(activeQuest).getTitle()) / 2),
                        5,
                        Quest.getQuestFromId(activeQuest).getTitle(),
                        0xFF00FF,
                        false,
                        false
                ).render(null, player, mouseX, mouseY);
                //left side of the line
                new MultilineLabel(
                        this,
                        contentPosX,
                        contentPosY,
                        Quest.getQuestFromId(activeQuest).getDescription(),
                        0xFFFF00,
                        (int) (contentAreaWidth * 0.5)
                ).render(null, player, mouseX, mouseY);
                //right side of the line
                new Label(
                        this,
                        contentAreaCenterX + contentAreaWidth1procent * 2,
                        contentPosY,
                        "Requirements:",
                        0xFF000F,
                        false,
                        false
                ).render(null, player, mouseX, mouseY);
                spacingIdRequirements++;
                int countReq = 0;
                for (QuestRequirement requirement : Quest.getQuestFromId(activeQuest).getRequirements()) {
                    int handInOffsetY = contentPosY + 7 + nonTextHeight * spacingIdRequirements;
                    new Label(
                            this,
                            contentAreaCenterX + contentAreaWidth1procent * 2,
                            contentPosY + 7 + nonTextHeight * spacingIdRequirements,//the "7" is for putting the label a bit more centered in the Y gap
                            requirement.getType().toString() + ":",
                            0xFF00FF,
                            false,
                            false
                    ).render(null, player, mouseX, mouseY);
                    spacingIdRequirements++;
                    for (IQuestRequirement subRequirement : requirement.getSubRequirements()) {
                        new ItemBoxAndText(
                                this,
                                contentAreaCenterX + contentAreaWidth1procent * 4,
                                contentPosY + nonTextHeight * spacingIdRequirements,
                                subRequirement.getItemStack(),
                                subRequirement.getLabelText(),
                                0xFFFFFF,
                                false
                        ).render(null, player, mouseX, mouseY);
                        spacingIdRequirements++;
                    }
                    if (requirement.getType() == QuestRequirementType.ITEM_DELIVER) {
                        new ButtonHandInRequirement(
                                this,
                                this.width - 64 - 5,
                                handInOffsetY,
                                activeQuest,
                                countReq,
                                requirement
                        ).render(null, player, mouseX, mouseY);
                    }
                    countReq++;
                }

                new Label(
                        this,
                        contentAreaCenterX + contentAreaWidth1procent * 2,
                        contentAreaCenterY + 7,
                        "Rewards:",
                        0xFF000F,
                        false,
                        false
                ).render(null, player, mouseX, mouseY);
                for (QuestReward reward : Quest.getQuestFromId(activeQuest).getRewards()) {
                    new ItemBoxAndText(
                            this,
                            contentAreaCenterX + contentAreaWidth1procent * 4,
                            contentAreaCenterY + nonTextHeight + nonTextHeight * spacingIdRewards,
                            reward.getReward().getItemStack(),
                            reward.getReward().toString(),
                            0xFFFFFF,
                            false
                    ).render(null, player, mouseX, mouseY);
                    spacingIdRewards++;
                }
                List<IQuestReward> rewards = new ArrayList<>();
                for (QuestReward questReward : Quest.getQuestFromId(activeQuest).getRewards()) {
                    rewards.add(questReward.getReward());
                }
                new ButtonClaimReward(
                        this,
                        ScreenQuestingDevice.rewardButtonX = contentAreaCenterX + contentAreaWidth1procent * 4,
                        ScreenQuestingDevice.rewardButtonY = contentAreaCenterY + nonTextHeight + nonTextHeight * spacingIdRewards,
                        activeQuest
                ).render(null, player, mouseX, mouseY);

                new Line(
                        this,
                        contentAreaCenterX,
                        contentAreaCenterY,
                        this.width - contentAreaWidth1procent * 2,
                        contentAreaCenterY,
                        LineColor.WHITE,
                        Ref.GUI_QUESTING_LINE_THICKNESS
                ).render(null, player, mouseX, mouseY);
                new Line(
                        this,
                        contentAreaCenterX,
                        contentPosY,
                        contentAreaCenterX,
                        this.height - contentAreaHeight1procent * 2,
                        LineColor.WHITE,
                        Ref.GUI_QUESTING_LINE_THICKNESS
                ).render(null, player, mouseX, mouseY);
            } else {
                new Label(
                        this,
                        windowCenterX,
                        5,
                        Ref.ALL_QUESTBOOK.getTitle(),
                        0xFFFFFF,
                        false,
                        false
                ).render(null, player, mouseX, mouseY);
                for (int questId : Ref.ALL_QUESTBOOK.getQuestlines().get(activeQuestline).getQuests()) {
                    Quest quest = Quest.getQuestFromId(questId);
                    LineColor lineColor = LineColor.BLACK;
                    for (int dependencyId : quest.getDependencies()) {
                        Quest qd = Quest.getQuestFromId(dependencyId);
                        if (Quest.hasQuestUncompletedDependenciesForPlayer(Utils.simplifyUUID(player.getUniqueID()), quest.getId())) {
                            lineColor = LineColor.RED;
                        } else if (Quest.isQuestCompletedForPlayer(Utils.simplifyUUID(player.getUniqueID()), qd.getId()) && Quest.isQuestCompletedForPlayer(Utils.simplifyUUID(player.getUniqueID()), quest.getId())) {
                            lineColor = LineColor.GREEN;
                        } else if (Quest.isQuestCompletedForPlayer(Utils.simplifyUUID(player.getUniqueID()), qd.getId())) {
                            lineColor = LineColor.YELLOW;
                        }
                        if (Quest.areQuestsInSameQuestline(questId, dependencyId)) {
                            new Line(
                                    this,
                                    quest.getPosition(),
                                    qd.getPosition(),
                                    lineColor,
                                    Ref.GUI_QUESTING_LINE_THICKNESS
                            ).render(null, player, mouseX, mouseY);
                        }
                    }
                }

                for (int questId : Ref.ALL_QUESTBOOK.getQuestlines().get(activeQuestline).getQuests()) {
                    Quest quest = Quest.getQuestFromId(questId);
                    new ButtonQuest(
                            this,
                            Ref.GUI_QUESTING_MARGIN_LEFT + quest.getPosition().getX(),
                            Ref.GUI_QUESTING_MARGIN_TOP + quest.getPosition().getY(),
                            quest,
                            Utils.simplifyUUID(player.getUniqueID())
                    ).render(null, player, mouseX, mouseY);
                }
            }
        }
        super.render(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton){
        switch(activeScreen){
            case MAIN_MENU:
                screens.entrySet().stream().filter(stringIQuestingSubScreenEntry -> stringIQuestingSubScreenEntry.getKey().equals("menu")).forEach(stringIQuestingSubScreenEntry -> {
                    stringIQuestingSubScreenEntry.getValue().onClick(null, player, mouseX, mouseY);
                });
                break;
            case PARTY_SCREEN:
                screens.entrySet().stream().filter(stringIQuestingSubScreenEntry -> stringIQuestingSubScreenEntry.getKey().equals("parties")).forEach(stringIQuestingSubScreenEntry -> {
                    stringIQuestingSubScreenEntry.getValue().onClick(null, player, mouseX, mouseY);
                });
                break;
            case QUESTLINES:
                screens.entrySet().stream().filter(stringIQuestingSubScreenEntry -> stringIQuestingSubScreenEntry.getKey().equals("questlines")).forEach(stringIQuestingSubScreenEntry -> {
                    stringIQuestingSubScreenEntry.getValue().onClick(null, player, mouseX, mouseY);
                });
                break;
            case SETTINGS:
                screens.entrySet().stream().filter(stringIQuestingSubScreenEntry -> stringIQuestingSubScreenEntry.getKey().equals("settings")).forEach(stringIQuestingSubScreenEntry -> {
                    stringIQuestingSubScreenEntry.getValue().onClick(null, player, mouseX, mouseY);
                });
                break;
            case QUEST_DETAILS:
                screens.entrySet().stream().filter(stringIQuestingSubScreenEntry -> stringIQuestingSubScreenEntry.getKey().equals("quest_details")).forEach(stringIQuestingSubScreenEntry -> {
                    stringIQuestingSubScreenEntry.getValue().onClick(null, player, mouseX, mouseY);
                });
                break;
        }
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

        new ButtonParties(this, 0, 0).onClick(null, player, mouseX, mouseY);
        if(isPartyScreenOpen){
            //todo party stuff handling
            int currentPartyGuiHeight = 0;
            for(Party party : Ref.ALL_QUESTING_PARTIES){
                new ButtonParty(this, Ref.GUI_QUESTLINES_MARGIN_LEFT, Ref.GUI_QUESTLINES_MARGIN_TOP + currentPartyGuiHeight, party).onClick(null, player, mouseX, mouseY);
                currentPartyGuiHeight += 25;
            }
        }else{
            for(int questId : Ref.ALL_QUESTBOOK.getQuestlines().get(activeQuestline).getQuests()){
                Quest quest = Quest.getQuestFromId(questId);
                new ButtonQuest(
                        this,
                        Ref.GUI_QUESTING_MARGIN_LEFT + quest.getPosition().getX(),
                        Ref.GUI_QUESTING_MARGIN_TOP + quest.getPosition().getY(),
                        quest,
                        Utils.simplifyUUID(player.getUniqueID())
                ).onClick(null, player, mouseX, mouseY);
            }

            if(activeQuest >= 0){
                int spacingIdRequirements = 0;
                int spacingIdRewards = 0;
                int countReq = 0;
                spacingIdRequirements++;
                for(QuestRequirement requirement : Quest.getQuestFromId(activeQuest).getRequirements()){
                    int handInOffsetY = contentPosY + 7 + nonTextHeight * spacingIdRequirements;
                    spacingIdRequirements++;
                    for(IQuestRequirement subRequirement : requirement.getSubRequirements()){
                        spacingIdRequirements++;
                    }
                    if(requirement.getType() == QuestRequirementType.ITEM_DELIVER){
                        new ButtonHandInRequirement(
                                this,
                                this.width - 64 -5,
                                handInOffsetY,
                                activeQuest,
                                countReq,
                                requirement
                        ).onClick(null, player, mouseX, mouseY);
                    }
                    countReq++;
                }


                List<IQuestReward> rewards = new ArrayList<>();
                for(QuestReward questReward : Quest.getQuestFromId(activeQuest).getRewards()){
                    rewards.add(questReward.getReward());
                }
                new ButtonClaimReward(
                        this,
                        ScreenQuestingDevice.rewardButtonX,
                        ScreenQuestingDevice.rewardButtonY,
                        activeQuest
                ).onClick(null, player, mouseX, mouseY);
            }
        }
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}