package com.vincentmet.customquests.screens.questingdeveicesubscreens.questlines;

import com.vincentmet.customquests.lib.LineColor;
import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.lib.Utils;
import com.vincentmet.customquests.quests.Quest;
import com.vincentmet.customquests.screens.elements.IQuestingGuiElement;
import com.vincentmet.customquests.screens.elements.buttons.ButtonQuest;
import com.vincentmet.customquests.screens.elements.labels.Line;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.List;

public class QuestingWeb implements IQuestingGuiElement {
    private Screen root;
    private int width = 0;
    private int height = 0;
    private static int activeQuestline = 0;
    private List<Line> lines = new ArrayList<>();
    private List<ButtonQuest> questButtonList = new ArrayList<>();

    public QuestingWeb(Screen root){
        this.root = root;
    }

    @Override
    public void update(PlayerEntity player, double mouseX, double mouseY, int width, int height) {
        this.width = width;
        this.height = height;
        reloadLines(player);
        reloadQuestButtons(player);
    }

    @Override
    public void render(PlayerEntity player, double mouseX, double mouseY) {
        lines.forEach(line -> line.render(player, mouseX, mouseY));
        questButtonList.forEach(buttonQuest -> buttonQuest.render(player, mouseX, mouseY));
    }

    @Override
    public void onClick(PlayerEntity player, double mouseX, double mouseY) {
        lines.forEach(line -> line.onClick(player, mouseX, mouseY));
        questButtonList.forEach(buttonQuest -> buttonQuest.onClick(player, mouseX, mouseY));
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

    public static int getActiveQuestline() {
        return activeQuestline;
    }

    public static void setActiveQuestline(int activeQuestline) {
        QuestingWeb.activeQuestline = activeQuestline;
    }

    public void reloadLines(PlayerEntity player){
        this.lines.clear();
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
                    this.lines.add(new Line(
                            root,
                            quest.getPosition(),
                            qd.getPosition(),
                            lineColor,
                            Ref.GUI_QUESTING_LINE_THICKNESS
                    ));
                }
            }
        }
    }

    public void reloadQuestButtons(PlayerEntity player){
        this.questButtonList.clear();

        for (int questId : Ref.ALL_QUESTBOOK.getQuestlines().get(activeQuestline).getQuests()) {
            Quest quest = Quest.getQuestFromId(questId);
            this.questButtonList.add(new ButtonQuest(
                    root,
                    Ref.GUI_QUESTING_MARGIN_LEFT + quest.getPosition().getX(),
                    Ref.GUI_QUESTING_MARGIN_TOP + quest.getPosition().getY(),
                    quest,
                    Utils.simplifyUUID(player.getUniqueID())
            ));
        }
    }
}
