package com.vincentmet.customquests.quests.progress;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.lib.Utils;
import com.vincentmet.customquests.quests.IJsonProvider;
import com.vincentmet.customquests.quests.IQuestRequirement;
import com.vincentmet.customquests.quests.quest.Quest;
import com.vincentmet.customquests.quests.party.Party;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuestUserProgress implements IJsonProvider {
    private String uuid;
    private int partyId;
    private List<Integer> completedQuestsIds;
    private Map<Integer, QuestStatus> questStatuses;

    public QuestUserProgress(String uuid, int partyId, List<Integer> completedQuestsIds, Map<Integer, QuestStatus> questStatuses){
        this.uuid = uuid;
        this.partyId = partyId;
        this.completedQuestsIds = completedQuestsIds;
        this.questStatuses = questStatuses;
        List<Integer> allQuestIds = new ArrayList<>();
        for(Quest quest : Ref.ALL_QUESTS){
            allQuestIds.add(quest.getId());
        }
        List<Integer> allQuestStatusIds = new ArrayList<>();
        for(Map.Entry<Integer, QuestStatus> quest : questStatuses.entrySet()){
            allQuestStatusIds.add(quest.getValue().getQuestId());
        }

        List<Integer> nonPresentQuestList = new ArrayList<>(allQuestIds);
        nonPresentQuestList.removeAll(allQuestStatusIds);

        for(int questId : nonPresentQuestList){
            this.questStatuses.put(questId, new QuestStatus(questId, false, new ArrayList<>()));
        }
        Ref.shouldSaveNextTick = true;
    }



    public String getUuid() {
        return uuid;
    }

    public String getUsername(){
        return Utils.getDisplayName(uuid);
    }

    public int getPartyId() {
        return partyId;
    }

    public void setPartyId(int partyId) {
        this.partyId = partyId;
    }

    public Party getParty(){
        for(Party party : Ref.ALL_QUESTING_PARTIES){
            if(party.getId() == partyId){
                return party;
            }
        }
        return null;
    }

    public List<Integer> getCompletedQuestsIds() {
        return completedQuestsIds;
    }

    public Map<Integer, QuestStatus> getQuestStatuses() {
        return questStatuses;
    }

    public void addCompletedQuest(int questId, World world, PlayerEntity player){
        if(!this.completedQuestsIds.contains(questId)){
            this.completedQuestsIds.add(questId);
            Ref.shouldSaveNextTick = true;
            final CommandDispatcher<CommandSource> dispatcher = world.getServer().getCommandManager().getDispatcher();
            try {
                String title = Utils.getFormattedText(".quest_completed");
                dispatcher.execute("title " + player.getDisplayName().getString() + " title \"" + title + "\"", world.getServer().getCommandSource().withFeedbackDisabled());
                dispatcher.execute("title " + player.getDisplayName().getString() + " subtitle \"" + Quest.getQuestFromId(questId).getTitle() + "\"", world.getServer().getCommandSource().withFeedbackDisabled());
            } catch (CommandSyntaxException e) {
                e.printStackTrace();
            }
        }
    }
    public void addCompletedQuest(int questId){
        if(!this.completedQuestsIds.contains(questId)){
            this.completedQuestsIds.add(questId);
            Ref.shouldSaveNextTick = true;
        }
    }


    
    public void deleteCompletedQuest(int questId){
        this.completedQuestsIds.remove(questId);
        Ref.shouldSaveNextTick = true;
    }

    public void setQuestStatuses(Map<Integer, QuestStatus> questStatuses) {
        this.questStatuses = questStatuses;
        Ref.shouldSaveNextTick = true;
    }

    public void addQuestStatus(QuestStatus questStatus){
        this.questStatuses.put(questStatus.getQuestId(), questStatus);
        Ref.shouldSaveNextTick = true;
    }

    public void deleteQuestStatus(QuestStatus questStatus){
        this.questStatuses.remove(questStatus);
        Ref.shouldSaveNextTick = true;
    }

    public JsonObject getJson(){
        JsonObject json = new JsonObject();
        json.addProperty("uuid", uuid);
        JsonArray completedQuestArray = new JsonArray();
        for(int completedQuestId : completedQuestsIds){
            completedQuestArray.add(completedQuestId);
        }
        json.add("completed_quests", completedQuestArray);
        JsonArray questStatusArray = new JsonArray();
        for(Map.Entry<Integer, QuestStatus> questStatusId : questStatuses.entrySet()){
            questStatusArray.add(questStatusId.getValue().getJson());
        }
        json.add("quest_status", questStatusArray);
        return json;
    }

    @Override
    public String toString() {
        return getJson().toString();
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setCompletedQuestsIds(List<Integer> completedQuestsIds) {
        this.completedQuestsIds = completedQuestsIds;
    }
}
