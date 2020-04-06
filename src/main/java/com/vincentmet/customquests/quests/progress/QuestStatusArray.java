package com.vincentmet.customquests.quests.progress;

import com.google.gson.JsonArray;
import com.vincentmet.customquests.quests.IJsonArrayProvider;

import java.util.HashMap;
import java.util.Map;

public class QuestStatusArray implements IJsonArrayProvider {
    private Map<Integer, QuestSubrequirementStatus> map = new HashMap<>();
    
    public QuestStatusArray(QuestSubrequirementStatus... subrequirements){
        for (QuestSubrequirementStatus subrequirement : subrequirements) {
            map.put(subrequirement.getId(), subrequirement);
        }
    }

    public Map<Integer, QuestSubrequirementStatus> getSubrequirements(){
        return map;
    }

    public void completeAll(){
        
    }

    @Override
    public JsonArray getJson() {
        JsonArray json = new JsonArray();
        for (Map.Entry<Integer, QuestSubrequirementStatus> integerQuestSubrequirementStatusEntry : map.entrySet()) {
            json.add(integerQuestSubrequirementStatusEntry.getValue().getJson());
        }
        return json;
    }
}
