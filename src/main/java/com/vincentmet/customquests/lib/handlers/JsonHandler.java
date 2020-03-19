package com.vincentmet.customquests.lib.handlers;

import com.google.gson.GsonBuilder;
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
import java.nio.charset.StandardCharsets;
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
            StringBuilder res = new StringBuilder();
            Files.readAllLines(questsLocation, StandardCharsets.UTF_8).forEach(res::append);
            jsonContainerQuests = PARSER.parse(res.toString()).getAsJsonObject();
        } catch (IOException e) {
            Utils.writeTo(questsLocation, Utils.getDefaultQuestsJson());
            loadQuests(questsLocation);
        }
    }

    public static void loadQuestbook(Path questBookLocation) {
        try {
            StringBuilder res = new StringBuilder();
            Files.readAllLines(questBookLocation, StandardCharsets.UTF_8).forEach(res::append);
            jsonContainerQuestbook = PARSER.parse(res.toString()).getAsJsonObject();
        } catch (IOException e) {
            Utils.writeTo(questBookLocation, Utils.getDefaultQuestBookJson());
            loadQuestbook(questBookLocation);
        }
    }

    public static void loadQuestingProgress(Path questingProgressLocation) {
        try {
            StringBuilder res = new StringBuilder();
            Files.readAllLines(questingProgressLocation, StandardCharsets.UTF_8).forEach(res::append);
            jsonContainerQuestingProgress = PARSER.parse(res.toString()).getAsJsonObject();
        } catch (IOException e) {
            e.printStackTrace();
            Utils.writeTo(questingProgressLocation, Utils.getDefaultQuestingProgressJson());
            loadQuestingProgress(questingProgressLocation);
        }
    }

    public static void loadQuestingParties(Path questingPartiesLocation) {
        try {
            StringBuilder res = new StringBuilder();
            Files.readAllLines(questingPartiesLocation, StandardCharsets.UTF_8).forEach(res::append);
            jsonContainerQuestingParties = PARSER.parse(res.toString()).getAsJsonObject();
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
        Utils.writeTo(questsLocation, new GsonBuilder().setPrettyPrinting().create().toJson(json));
    }

    public static void writeQuestbook(Path questBookLocation) {
        JsonObject json = new JsonObject();
        json.addProperty("title", Ref.ALL_QUESTBOOK.getTitle());
        json.addProperty("text", Ref.ALL_QUESTBOOK.getDescription());
        JsonArray questlineArray = new JsonArray();
        for (Map.Entry<Integer, QuestLine> questLine : Ref.ALL_QUESTBOOK.getQuestlines().entrySet()) {
            questlineArray.add(questLine.getValue().getJson());
        }
        json.add("questlines", questlineArray);
        Utils.writeTo(questBookLocation, new GsonBuilder().setPrettyPrinting().create().toJson(json));
    }

    public static void writeQuestingProgress(Path questingProgressLocation) {
        JsonObject json = new JsonObject();
        JsonArray playerArray = new JsonArray();
        for (Map.Entry<String, QuestUserProgress> users : Ref.ALL_QUESTING_PROGRESS.entrySet()) {
            playerArray.add(users.getValue().getJson());
        }
        json.add("players", playerArray);
        Utils.writeTo(questingProgressLocation, new GsonBuilder().setPrettyPrinting().create().toJson(json));
    }

    public static void writeQuestingParties(Path questingPartiesLocation) {
        JsonObject json = new JsonObject();
        JsonArray partyArray = new JsonArray();
        for (Party party : Ref.ALL_QUESTING_PARTIES) {
            partyArray.add(party.getJson());
        }
        json.add("parties", partyArray);
        Utils.writeTo(questingPartiesLocation, new GsonBuilder().setPrettyPrinting().create().toJson(json));
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