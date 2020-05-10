package com.vincentmet.customquests.quests.party;

import com.google.gson.JsonObject;
import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.quests.IJsonProvider;
import com.vincentmet.customquests.quests.progress.QuestUserProgress;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Nullable
public class Party implements IJsonProvider {
    private int id;
    private String creator;
    private String partyName;
    private boolean lootShared;
    private boolean ffa;
    private QuestPartyProgress progress;

    public Party(int id, String creator, String partyName, boolean lootShared, boolean ffa, QuestPartyProgress progress){
        this.id = id;
        this.creator = creator;
        this.partyName = partyName;
        this.lootShared = lootShared;
        this.ffa = ffa;
        this.progress = progress;
    }

    public int getId() {
        return id;
    }

    public String getCreator(){
        return creator;
    }

    public String getPartyName(){
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public boolean isLootShared(){
        return lootShared;
    }

    public void setLootShared(boolean shareLoot) {
        this.lootShared = shareLoot;
    }

    public boolean isOpenForEveryone(){
        return ffa;
    }

    public QuestPartyProgress getProgress() {
        return progress;
    }

    public List<String> getPartyMembers() {
        List<String> uuids = new ArrayList<>();
        for(Map.Entry<String, QuestUserProgress> questUserProgress : Ref.ALL_QUESTING_PROGRESS.entrySet()){
            if(questUserProgress.getValue().getPartyId() == id){
                uuids.add(questUserProgress.getValue().getUuid());
            }
        }
        return uuids;
    }

    public JsonObject getJson(){
        JsonObject json = new JsonObject();
        json.addProperty("id", id);
        json.addProperty("creator", creator);
        json.addProperty("name", partyName);
        json.addProperty("lootShared", lootShared);
        json.addProperty("ffa", ffa);
        json.add("progress", progress.getJson());
        return json;
    }

    private static int nextId(){
        return Ref.ALL_QUESTING_PARTIES.stream()
                .mapToInt(Party::getId)
                .reduce(0, (left, right) -> left == right ? left + 1 : left);
    }

    public static Party createNewParty(String creatorUuid, String partyName){
        Party party = new Party(nextId(), creatorUuid, partyName, false, false, new QuestPartyProgress(new ArrayList<>(), new ArrayList<>()));
        Ref.ALL_QUESTING_PARTIES.add(party);
        return party;
    }
}