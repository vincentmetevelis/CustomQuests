package com.vincentmet.customquests.quests.progress;

import com.google.gson.JsonObject;
import com.vincentmet.customquests.quests.IJsonProvider;

public class QuestSubrequirementStatus implements IJsonProvider {
    private int id;
    private int value;

    public QuestSubrequirementStatus(int id){
        this(id, 0);
    }

    public QuestSubrequirementStatus(int id, int value){
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setComplete(int completionNumber){
        this.value = completionNumber;
    }

    public void reset(){
        this.value = 0;
    }

    @Override
    public JsonObject getJson() {
        JsonObject json = new JsonObject();
        json.addProperty("id", id);
        json.addProperty("value", value);
        return json;
    }
}
