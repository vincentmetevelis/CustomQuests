package com.vincentmet.customquests.quests;

import java.util.List;

public class QuestLine {
    private int id;
    private String title;
    private String description;
    private List<Integer> quests;

    public QuestLine(int id, String title, String description, List<Integer> questIds){
        this.id = id;
        this.title = title;
        this.description = description;
        this.quests = questIds;
    }
    
    public void setId(int id){
        this.id = id;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setQuests(List<Integer> quests) {
        this.quests = quests;
    }
    
    public int getId(){
        return id;
    }
    
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<Integer> getQuests() {
        return quests;
    }
}