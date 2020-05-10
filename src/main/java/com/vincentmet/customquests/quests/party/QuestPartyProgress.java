package com.vincentmet.customquests.quests.party;

import com.google.gson.*;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.vincentmet.customquests.lib.*;
import com.vincentmet.customquests.quests.*;
import com.vincentmet.customquests.quests.progress.*;
import com.vincentmet.customquests.quests.quest.Quest;
import java.util.*;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class QuestPartyProgress implements IJsonProvider{
    private List<Integer> completedQuestsIds;
    private List<QuestStatus> questStatuses;

    public QuestPartyProgress(List<Integer> completedQuestsIds, List<QuestStatus> questStatuses){
        this.completedQuestsIds = completedQuestsIds;
        this.questStatuses = questStatuses;
        List<Integer> allQuestIds = new ArrayList<>();
        for(Quest quest : Ref.ALL_QUESTS.values()){
            allQuestIds.add(quest.getId());
        }
        List<Integer> allQuestStatusIds = new ArrayList<>();
        for(QuestStatus quest : questStatuses){
            allQuestStatusIds.add(quest.getQuestId());
        }

        List<Integer> nonPresentQuestList = new ArrayList<>(allQuestIds);
        nonPresentQuestList.removeAll(allQuestStatusIds);

        for(int questId : nonPresentQuestList){
            this.questStatuses.add(new QuestStatus(questId, false, new ArrayList<>()));
        }
        Ref.shouldSaveNextTick = true;
    }

    public static boolean areAllRequirementsCompleted(String uuid, int questId){
        boolean isCompleted = true;
        for(Party party : Ref.ALL_QUESTING_PARTIES) {
            if(party.getPartyMembers().stream().anyMatch(s -> s.equals(uuid))){
                for (QuestStatus questStatus : party.getProgress().getQuestStatuses()) {
                    if(questStatus.getQuestId() == questId){
                        int countQrs = 0;
                        for(QuestRequirementStatus qrs : questStatus.getQuestRequirementStatuses()){
                            if(!isRequirementCompleted(uuid, questId, countQrs)){
                                isCompleted = false;
                            }
                            countQrs++;
                        }
                    }
                }
            }
        }
        return isCompleted;
    }

    public static boolean isRequirementCompleted(String uuid, int questId, int reqId){
        boolean isCompleted = true;
        for(Party party : Ref.ALL_QUESTING_PARTIES){
            if(party.getPartyMembers().stream().anyMatch(s -> s.equals(uuid))){
                for(QuestStatus questStatus : party.getProgress().getQuestStatuses()){
                    if(questStatus.getQuestId() == questId){
                        for(QuestRequirementStatus reqStatus : questStatus.getQuestRequirementStatuses()){
                            if(reqStatus.getRequirementId() == reqId){
                                int countSubReqProgress = 0;
                                for(Map.Entry<Integer, QuestSubrequirementStatus> subReqProgress : reqStatus.getProgress().entrySet()){
                                    int countSubReq = 0;
                                    for(IQuestRequirement subReq : Quest.getQuestFromId(questId).getRequirements().get(reqId).getSubRequirements()){
                                        if(countSubReq == countSubReqProgress){
                                            if(subReqProgress.getValue().getValue() < subReq.getCompletionNumber()){
                                                isCompleted = false;
                                            }
                                        }
                                        countSubReq++;
                                    }
                                    countSubReqProgress++;
                                }
                            }
                        }
                    }
                }
            }
        }
        return isCompleted;
    }

    public static int getItemCountLeftToHandIn(String uuid, int questId, int reqId, int subReqId){
        for(Party party : Ref.ALL_QUESTING_PARTIES){
            if(party.getPartyMembers().stream().anyMatch(s -> s.equals(uuid))){
                int currentCount = party.getProgress().getQuestStatuses().get(questId).getQuestRequirementStatuses().get(reqId).getProgress(subReqId);
                int questCompletionCount = Quest.getQuestFromId(questId).getRequirements().get(reqId).getSubRequirements().get(subReqId).getCompletionNumber();
                return questCompletionCount - currentCount;
            }
        }
        return 0;
    }

    public static boolean isRewardClaimed(String uuid, int questId){
        for(Party party : Ref.ALL_QUESTING_PARTIES){
            if(party.getPartyMembers().stream().anyMatch(s -> s.equals(uuid))){
                for(QuestStatus reqStatus : party.getProgress().getQuestStatuses()){
                    if(reqStatus.getQuestId() == questId){
                        return reqStatus.isClaimed();
                    }
                }
            }
        }
        return false;
    }

    public static void setRewardsClaimed(String uuid, int questId){
        for(Party party : Ref.ALL_QUESTING_PARTIES){
            if(party.getPartyMembers().stream().anyMatch(s -> s.equals(uuid))){
                for(QuestStatus reqStatus : party.getProgress().getQuestStatuses()){
                    if(reqStatus.getQuestId() == questId){
                        reqStatus.setClaimed(true);
                    }
                }
            }
        }
    }

    public static void setPlayerProgressToCompleted(String uuid, int questId, int reqId, int subReqId){
        for(Party party : Ref.ALL_QUESTING_PARTIES) {
            if(party.getPartyMembers().stream().anyMatch(s -> s.equals(uuid))){
                for(QuestStatus status : party.getProgress().getQuestStatuses()) {
                    if(status.getQuestId() == questId){
                        int reqCount = 0;
                        for(QuestRequirementStatus reqStatus : status.getQuestRequirementStatuses()){
                            if(reqCount == reqId){
                                reqStatus.setProgress(subReqId, Quest.getQuestFromId(questId).getRequirements().get(reqId).getSubRequirements().get(subReqId).getCompletionNumber());
                            }
                            reqCount++;
                        }
                    }
                }
            }
        }
    }

    public static void addPlayerProgress(String uuid, int questId, int reqId, int subReqId, int amount){
        for(Party party : Ref.ALL_QUESTING_PARTIES) {
            if(party.getPartyMembers().stream().anyMatch(s -> s.equals(uuid))){
                for(QuestStatus status : party.getProgress().getQuestStatuses()) {
                    if(status.getQuestId() == questId){
                        int reqCount = 0;
                        for(QuestRequirementStatus reqStatus : status.getQuestRequirementStatuses()){
                            if(reqCount == reqId){
                                reqStatus.setProgress(subReqId, reqStatus.getProgress(subReqId) + amount);
                            }
                            reqCount++;
                        }
                    }
                }
            }
        }
    }

    public List<Integer> getCompletedQuestsIds() {
        return completedQuestsIds;
    }

    public List<QuestStatus> getQuestStatuses() {
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

    public void deleteCompletedQuest(int questId){
        this.completedQuestsIds.remove(questId);
        Ref.shouldSaveNextTick = true;
    }

    public void setQuestStatuses(List<QuestStatus> questStatuses) {
        this.questStatuses = questStatuses;
        Ref.shouldSaveNextTick = true;
    }

    public void addQuestStatus(QuestStatus questStatus){
        this.questStatuses.add(questStatus);
        Ref.shouldSaveNextTick = true;
    }

    public void deleteQuestStatus(QuestStatus questStatus){
        this.questStatuses.remove(questStatus);
        Ref.shouldSaveNextTick = true;
    }

    public JsonObject getJson(){
        JsonObject json = new JsonObject();
        JsonArray completedQuestArray = new JsonArray();
        for(int completedQuestId : completedQuestsIds){
            completedQuestArray.add(completedQuestId);
        }
        json.add("completed_quests", completedQuestArray);
        JsonArray questStatusArray = new JsonArray();
        for(QuestStatus questStatusId : questStatuses){
            questStatusArray.add(questStatusId.getJson());
        }
        json.add("quest_status", questStatusArray);
        return json;
    }
}
