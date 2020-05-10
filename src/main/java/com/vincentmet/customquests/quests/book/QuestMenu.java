package com.vincentmet.customquests.quests.book;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.quests.IJsonProvider;

import java.util.Map;

public class QuestMenu implements IJsonProvider {
    private String title;
    private String description;
    private Map<Integer, QuestLine> questlines;

    public QuestMenu(String title, String description , Map<Integer, QuestLine> questlines){
        this.title =  title;
        this.description = description;
        this.questlines = questlines;
    }

    public JsonObject getJson(){
        JsonObject json = new JsonObject();
        json.addProperty("title", title);
        json.addProperty("text", description);
        JsonArray jsonQuestlineArray = new JsonArray();
        for(Map.Entry<Integer, QuestLine> questline : questlines.entrySet()){
            jsonQuestlineArray.add(questline.getValue().getJson());
        }
        json.add("questlines", jsonQuestlineArray);
        return json;
    }

    public Map<Integer, QuestLine> getQuestlines() {
        return questlines;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public void setQuestlines(Map<Integer, QuestLine> questlines) {
        this.questlines = questlines;
        Ref.shouldSaveNextTick = true;
    }

    public void addQuestline(QuestLine questline){
        this.questlines.put(questline.getId(), questline);
        Ref.shouldSaveNextTick = true;
    }

    public void setDescription(String description) {
        this.description = description;
        Ref.shouldSaveNextTick = true;
    }

    public void setTitle(String title) {
        this.title = title;
        Ref.shouldSaveNextTick = true;
    }
}