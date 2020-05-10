package com.vincentmet.customquests.quests;

import com.google.gson.*;
import com.vincentmet.customquests.lib.Ref;
import java.util.*;

public class QuestLine implements IJsonProvider{
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

    public JsonObject getJson(){
        JsonObject json = new JsonObject();
        json.addProperty("id", id);
        json.addProperty("title", title);
        json.addProperty("text", description);
        JsonArray questArray = new JsonArray();
        for(int questId : quests){
            questArray.add(questId);
        }
        json.add("quests", questArray);
        return json;
    }

    public static List<Integer> getUnlockedQuestlines(String uuid){
        List<Integer> completedQuestIds = Quest.getCompletedQuests(uuid);
        List<Integer> unlockedQuestlines = new ArrayList<>();
        for(Quest quest : Ref.ALL_QUESTS.values()){
            if(quest.getDependencies().isEmpty()){
                unlockedQuestlines.add(quest.getQuestline());
            }
            for(int dependency : quest.getDependencies()){
                if(completedQuestIds.contains(dependency)){
                    unlockedQuestlines.add(quest.getQuestline());
                }
            }
        }
        return unlockedQuestlines;
    }

    public static boolean isQuestlineUnlocked(String uuid, int questlineId){
        return getUnlockedQuestlines(uuid).contains(questlineId);
    }
    
    public void setTitle(String title) {
        this.title = title;
        Ref.shouldSaveNextTick = true;
    }

    public void setDescription(String description) {
        this.description = description;
        Ref.shouldSaveNextTick = true;
    }

    public void setQuests(List<Integer> quests) {
        this.quests = quests;
        Ref.shouldSaveNextTick = true;
    }

    public void addQuest(int questId){
        this.quests.add(questId);
        Ref.shouldSaveNextTick = true;
    }

    public void removeQuest(int questId){
        this.quests.remove(questId);
        Ref.shouldSaveNextTick = true;
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