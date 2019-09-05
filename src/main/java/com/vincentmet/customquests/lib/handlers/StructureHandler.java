package com.vincentmet.customquests.lib.handlers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.lib.converters.ConverterHelper;
import com.vincentmet.customquests.quests.*;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class StructureHandler {
    public static void initQuestbook(){
        JsonObject json = JsonHandler.getQuestbookJson();

        List<QuestLine> questLineList = new ArrayList<>();
        for(JsonElement jsonQuestlineElement : ConverterHelper.QuestBook.getQuestLines(json)){
            JsonObject jsonQuestline = jsonQuestlineElement.getAsJsonObject();
            questLineList.add(new QuestLine(
                    ConverterHelper.QuestBook.Questlines.getId(jsonQuestline),
                    ConverterHelper.QuestBook.Questlines.getTitle(jsonQuestline),
                    ConverterHelper.QuestBook.Questlines.getText(jsonQuestline),
                    ConverterHelper.QuestBook.Questlines.getQuestIds(jsonQuestline)
            ));

        }

        Ref.ALL_QUESTBOOK = new QuestMenu(
                ConverterHelper.QuestBook.getTitle(json),
                ConverterHelper.QuestBook.getText(json),
                questLineList
        );
    }

    public static void initQuests() {
        JsonObject json = JsonHandler.getQuestsJson();

        List<Quest> questsList = new ArrayList<>();
        for (JsonElement jsonQuestElement : json.get("quests").getAsJsonArray()) {
            JsonObject jsonQuest = jsonQuestElement.getAsJsonObject();

            List<QuestRequirement> questRequirementList = new ArrayList<>();
            List<QuestReward> questRewardList = new ArrayList<>();

            for (JsonElement jsonQuestRequirementElement : ConverterHelper.Quests.getRequirements(jsonQuest)) {
                JsonObject jsonQuestRequirement = jsonQuestRequirementElement.getAsJsonObject();

                switch(ConverterHelper.Quests.Requirements.getType(jsonQuestRequirement)){
                    case ITEM_DETECT:
                        List<IQuestRequirement> subQuestRequirements = new ArrayList<>();
                        for(JsonElement jsonQuestSubRequirementElement : ConverterHelper.Quests.Requirements.getSubRequirements(jsonQuestRequirement)){
                            JsonObject jsonQuestSubRequirement = jsonQuestSubRequirementElement.getAsJsonObject();

                            subQuestRequirements.add(new QuestRequirement.ItemDetect(
                                    new ItemStack(
                                            ConverterHelper.Quests.Requirements.ItemsDetect.getSubRequirementItem(jsonQuestSubRequirement),
                                            ConverterHelper.Quests.Requirements.ItemsDetect.getSubRequirementAmount(jsonQuestSubRequirement),
                                            ConverterHelper.Quests.Requirements.ItemsDetect.getSubRequirementNbt(jsonQuestSubRequirement)
                                    )
                            ));
                        }
                        questRequirementList.add(new QuestRequirement(
                                QuestRequirementType.ITEM_DETECT,
                                subQuestRequirements
                        ));
                        break;
                    case TRAVEL_TO:

                        break;
                    case CRAFTING_DETECT:

                        break;
                    case ITEM_DELIVER:

                        break;
                    case KILL_MOB:
                        /*List<IQuestRequirement> subQuestRequirements = new ArrayList<>();
                        for(JsonElement jsonQuestSubRequirementElement : ConverterHelper.Quests.Requirements.getSubRequirements(jsonQuestRequirement)){
                            JsonObject jsonQuestSubRequirement = jsonQuestSubRequirementElement.getAsJsonObject();

                            subQuestRequirements.add(new QuestRequirement.ItemDetect(
                                    new ItemStack(
                                            ConverterHelper.Quests.Requirements.ItemsDetect.getSubRequirementItem(jsonQuestSubRequirement),
                                            ConverterHelper.Quests.Requirements.ItemsDetect.getSubRequirementAmount(jsonQuestSubRequirement),
                                            ConverterHelper.Quests.Requirements.ItemsDetect.getSubRequirementNbt(jsonQuestSubRequirement)
                                    )
                            ));
                        }

                        questRequirementList.add(new QuestRequirement(
                                QuestRequirementType.KILL_MOB,
                                subQuestRequirements
                        ));*/
                        break;
                }
            }
            for (JsonElement jsonQuestRewardElement : ConverterHelper.Quests.getRewards(jsonQuest)) {
                JsonObject jsonQuestReward = jsonQuestRewardElement.getAsJsonObject();

                switch (ConverterHelper.Quests.Rewards.getType(jsonQuestReward)) {
                    default:
                    case ITEMS:
                        questRewardList.add(new QuestReward(
                                QuestRewardType.ITEMS,
                                new QuestReward.ReceiveItems(new ItemStack(
                                        ConverterHelper.Quests.Rewards.Items.getItem(jsonQuestReward),
                                        ConverterHelper.Quests.Rewards.Items.getAmount(jsonQuestReward),
                                        ConverterHelper.Quests.Rewards.Items.getNbt(jsonQuestReward)
                                ))
                        ));
                        break;
                    case COMMAND:
                        questRewardList.add(new QuestReward(
                                QuestRewardType.COMMAND,
                                new QuestReward.Command(
                                        ConverterHelper.Quests.Rewards.Command.getCommand(jsonQuestReward)
                                )
                        ));
                        break;
                    case SPAWN_ENTITY:
                        questRewardList.add(new QuestReward(
                                QuestRewardType.SPAWN_ENTITY,
                                new QuestReward.SpawnEntity(
                                        ConverterHelper.Quests.Rewards.SpawnEntity.getEntity(jsonQuestReward)
                                )
                        ));
                        break;
                }
            }
            questsList.add(new Quest(
                    ConverterHelper.Quests.getId(jsonQuest),
                    ConverterHelper.Quests.getTitle(jsonQuest),
                    ConverterHelper.Quests.getText(jsonQuest),
                    ConverterHelper.Quests.getIcon(jsonQuest),
                    ConverterHelper.Quests.Dependencies.getDependencyQuestIds(jsonQuest),
                    questRequirementList,
                    questRewardList,
                    ConverterHelper.Quests.GuiPosition.getGuiPosition(jsonQuest)
            ));
        }


        Ref.ALL_QUESTS = questsList;
    }

    public static void initQuestingProgress(){
        JsonObject jsonProgress = JsonHandler.getQuestingProgressJson();
        List<QuestUserProgress> questUserProgressList = new ArrayList<>();

        for(JsonElement jsonUserProgressElement : ConverterHelper.QuestProgress.getPlayers(jsonProgress)){
            JsonObject jsonUserProgress = jsonUserProgressElement.getAsJsonObject();

            List<QuestStatus> questStatusList = new ArrayList<>();
            for(JsonElement jsonQuestStatusElement : ConverterHelper.QuestProgress.Players.getQuestStatus(jsonUserProgress)){
                JsonObject jsonQuestStatus = jsonQuestStatusElement.getAsJsonObject();

                List<QuestRequirementStatus> questStatusRequirementList = new ArrayList<>();
                for(JsonElement jsonQuestRequirementStatusElement : ConverterHelper.QuestProgress.Players.QuestStatus.getRequirementCompletion(jsonQuestStatus)){
                    questStatusRequirementList.add(new QuestRequirementStatus(
                            ConverterHelper.QuestProgress.Players.QuestStatus.RequirementCompletion.getSubRequirementCompletionStatusList(jsonQuestRequirementStatusElement.getAsJsonArray())
                    ));
                }

                questStatusList.add(new QuestStatus(
                        ConverterHelper.QuestProgress.Players.QuestStatus.getId(jsonQuestStatus),
                        ConverterHelper.QuestProgress.Players.QuestStatus.getIsClaimed(jsonQuestStatus),
                        questStatusRequirementList
                ));
            }
            questUserProgressList.add(new QuestUserProgress(
                    ConverterHelper.QuestProgress.Players.getUUID(jsonUserProgress),
                    ConverterHelper.QuestProgress.Players.getFinishedQuests(jsonUserProgress),
                    questStatusList
            ));
        }

        Ref.ALL_QUESTING_PROGRESS = questUserProgressList;
    }
}