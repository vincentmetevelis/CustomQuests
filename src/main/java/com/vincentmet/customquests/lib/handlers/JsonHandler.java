package com.vincentmet.customquests.lib.handlers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.lib.Utils;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

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

    public static void writeJson(){ //todo

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