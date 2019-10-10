package com.vincentmet.customquests.lib.handlers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.lib.Utils;
import com.vincentmet.customquests.quests.Quest;
import com.vincentmet.customquests.quests.QuestLine;
import com.vincentmet.customquests.quests.QuestUserProgress;
import net.minecraft.client.renderer.entity.model.RendererModel;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class JsonHandler {
    private static JsonObject jsonContainerQuests;
    private static JsonObject jsonContainerQuestbook;
    private static JsonObject jsonContainerQuestingProgress;

    public static void loadJson() {
        try{
            JsonParser parser = new JsonParser();
            jsonContainerQuests = (JsonObject)parser.parse(new FileReader(Ref.questsLocation));
        }catch(IOException e){
            try{
                FileWriter file = new FileWriter(Ref.questsLocation);
                file.write(Utils.getDefaultQuestsJson());
                file.flush();
                file.close();
                JsonParser parser = new JsonParser();
                jsonContainerQuests = (JsonObject)parser.parse(new FileReader(Ref.questsLocation));
            }catch(IOException e1){
                e1.printStackTrace();
            }
        }

        try{
            JsonParser parser = new JsonParser();
            jsonContainerQuestbook = (JsonObject)parser.parse(new FileReader(Ref.questBookLocation));
        }catch(IOException e){
            try{
                FileWriter file = new FileWriter(Ref.questBookLocation);
                file.write(Utils.getDefaultQuestBookJson());
                file.flush();
                file.close();
                JsonParser parser = new JsonParser();
                jsonContainerQuestbook = (JsonObject)parser.parse(new FileReader(Ref.questBookLocation));
            }catch(IOException e1){
                e1.printStackTrace();
            }
        }

        try{
            JsonParser parser = new JsonParser();
            jsonContainerQuestingProgress = (JsonObject)parser.parse(new FileReader(Ref.questingProgressLocation));
        }catch(IOException e){
            try{
                FileWriter file = new FileWriter(Ref.questingProgressLocation);
                file.write(Utils.getDefaultQuestingProgressJson());
                file.flush();
                file.close();
                JsonParser parser = new JsonParser();
                jsonContainerQuestingProgress = (JsonObject)parser.parse(new FileReader(Ref.questingProgressLocation));
            }catch(IOException e1){
                e1.printStackTrace();
            }
        }
    }

    public static void writeJson(){
        try{
            FileWriter file = new FileWriter(Ref.questsLocation);

            JsonObject json = new JsonObject();
            JsonArray questArray = new JsonArray();
            for(Quest quest : Ref.ALL_QUESTS){
                questArray.add(quest.getJson());
            }
            json.add("quests", questArray);
            file.write(json.toString());

            file.flush();
            file.close();
        }catch(IOException e){
            e.printStackTrace();
        }

        try{
            FileWriter file = new FileWriter(Ref.questBookLocation);

            JsonObject json = new JsonObject();
            json.addProperty("title", Ref.ALL_QUESTBOOK.getTitle());
            json.addProperty("text", Ref.ALL_QUESTBOOK.getDescription());
            JsonArray questlineArray = new JsonArray();
            for(QuestLine questLine : Ref.ALL_QUESTBOOK.getQuestlines()){
                questlineArray.add(questLine.getJson());
            }
            json.add("questlines", questlineArray);
            file.write(json.toString());

            file.flush();
            file.close();
        }catch(IOException e){
            e.printStackTrace();
        }

        try{
            FileWriter file = new FileWriter(Ref.questingProgressLocation);

            JsonObject json = new JsonObject();
            JsonArray playerArray = new JsonArray();
            for(QuestUserProgress users: Ref.ALL_QUESTING_PROGRESS){
                playerArray.add(users.getJson());
            }
            json.add("players", playerArray);
            file.write(json.toString());

            file.flush();
            file.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static JsonObject getQuestbookJson(){
        return jsonContainerQuestbook;
    }

    public static JsonObject getQuestsJson(){
        return jsonContainerQuests;
    }

    public static JsonObject getQuestingProgressJson(){
        return jsonContainerQuestingProgress;
    }
}