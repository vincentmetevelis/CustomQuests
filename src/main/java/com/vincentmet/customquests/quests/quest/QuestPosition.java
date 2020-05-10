package com.vincentmet.customquests.quests.quest;

import com.google.gson.JsonArray;
import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.quests.IJsonArrayProvider;

public class QuestPosition implements IJsonArrayProvider {
    private int x;
    private int y;

    public QuestPosition(int x, int y){
        this.x = x;
        this.y = y;
    }

    public JsonArray getJson(){
        JsonArray json = new JsonArray();
        json.add(x);
        json.add(y);
        return json;
    }

    public void setX(int x) {
        this.x = x;
        Ref.shouldSaveNextTick = true;
    }

    public void setY(int y) {
        this.y = y;
        Ref.shouldSaveNextTick = true;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}