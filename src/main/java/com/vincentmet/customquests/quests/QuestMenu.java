package com.vincentmet.customquests.quests;

import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;

public class QuestMenu {
    private String title;
    private String description;
    private List<QuestLine> questlines;

    public QuestMenu(String title, String description , List<QuestLine> questlines){
        this.title =  title;
        this.description = description;
        this.questlines = questlines;
    }

    public List<QuestLine> getQuestlines() {
        return questlines;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public void setQuestlines(List<QuestLine> questlines) {
        this.questlines = questlines;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}