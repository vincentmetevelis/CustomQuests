package com.vincentmet.customquests.lib.handlers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.lib.Utils;
import com.vincentmet.customquests.quests.Quest;
import com.vincentmet.customquests.quests.QuestLine;
import com.vincentmet.customquests.quests.QuestUserProgress;
import com.vincentmet.customquests.quests.party.Party;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class JsonHandler {
    private static JsonObject jsonContainerQuests;
    private static JsonObject jsonContainerQuestbook;
    private static JsonObject jsonContainerQuestingProgress;
    private static JsonObject jsonContainerQuestingParties;
    private static final JsonParser PARSER = new JsonParser();

    public static void loadJson(Path questsLocation, Path questBookLocation, Path questingProgressLocation, Path questingPartiesLocation) {
        loadQuests(questsLocation);
        loadQuestbook(questBookLocation);
        loadQuestingProgress(questingProgressLocation);
        loadQuestingParties(questingPartiesLocation);
    }

    public static void loadQuests(Path questsLocation) {
        try {
            jsonContainerQuests = PARSER.parse(Files.newBufferedReader(questsLocation)).getAsJsonObject();
        } catch (IOException e) {
            Utils.writeTo(questsLocation, Utils.getDefaultQuestsJson());
            loadQuests(questsLocation);
        }
    }

    public static void loadQuestbook(Path questBookLocation) {
        try {
            jsonContainerQuestbook = PARSER.parse(Files.newBufferedReader(questBookLocation)).getAsJsonObject();
        } catch (IOException e) {
            Utils.writeTo(questBookLocation, Utils.getDefaultQuestBookJson());
            loadQuestbook(questBookLocation);
        }
    }

    public static void loadQuestingProgress(Path questingProgressLocation) {
        try {
            jsonContainerQuestingProgress = PARSER.parse(Files.newBufferedReader(questingProgressLocation)).getAsJsonObject();
        } catch (IOException e) {
            Utils.writeTo(questingProgressLocation, Utils.getDefaultQuestingProgressJson());
            loadQuestingProgress(questingProgressLocation);
        }
    }

    public static void loadQuestingParties(Path questingPartiesLocation) {
        try {
            jsonContainerQuestingParties = PARSER.parse(Files.newBufferedReader(questingPartiesLocation)).getAsJsonObject();
        } catch (IOException e) {
            Utils.writeTo(questingPartiesLocation, Utils.getDefaultQuestingPartiesJson());
            loadQuestingParties(questingPartiesLocation);
        }
    }

    public static void writeAll(Path questsLocation, Path questBookLocation, Path questingProgressLocation, Path questingPartiesLocation) {
        writeQuests(questsLocation);
        writeQuestbook(questBookLocation);
        writeQuestingProgress(questingProgressLocation);
        writeQuestingParties(questingPartiesLocation);
    }

    public static void writeQuests(Path questsLocation) {
        JsonObject json = new JsonObject();
        JsonArray questArray = new JsonArray();
        for (Quest quest : Ref.ALL_QUESTS) {
            questArray.add(quest.getJson());
        }
        json.add("quests", questArray);
        Utils.writeTo(questsLocation, json.toString());
    }

    public static void writeQuestbook(Path questBookLocation) {
        JsonObject json = new JsonObject();
        json.addProperty("title", Ref.ALL_QUESTBOOK.getTitle());
        json.addProperty("text", Ref.ALL_QUESTBOOK.getDescription());
        JsonArray questlineArray = new JsonArray();
        for (QuestLine questLine : Ref.ALL_QUESTBOOK.getQuestlines()) {
            questlineArray.add(questLine.getJson());
        }
        json.add("questlines", questlineArray);
        Utils.writeTo(questBookLocation, json.toString());
    }

    public static void writeQuestingProgress(Path questingProgressLocation) {
        JsonObject json = new JsonObject();
        JsonArray playerArray = new JsonArray();
        for (Map.Entry<String, QuestUserProgress> users : Ref.ALL_QUESTING_PROGRESS.entrySet()) {
            playerArray.add(users.getValue().getJson());
        }
        json.add("players", playerArray);
        Utils.writeTo(questingProgressLocation, json.toString());
    }

    public static void writeQuestingParties(Path questingPartiesLocation) {
        JsonObject json = new JsonObject();
        JsonArray partyArray = new JsonArray();
        for (Party party : Ref.ALL_QUESTING_PARTIES) {
            partyArray.add(party.getJson());
        }
        json.add("parties", partyArray);
        Utils.writeTo(questingPartiesLocation, json.toString());
    }

    public static JsonObject getQuestbookJson() {
        return jsonContainerQuestbook;
    }

    public static JsonObject getQuestsJson() {
        return jsonContainerQuests;
    }

    public static JsonObject getQuestingProgressJson() {
        return jsonContainerQuestingProgress;
    }

    public static JsonObject getQuestingPartiesJson() {
        return jsonContainerQuestingParties;
    }
}