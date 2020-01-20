package com.vincentmet.customquests.lib.handlers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.lib.converters.ConverterHelper;
import com.vincentmet.customquests.quests.*;
import com.vincentmet.customquests.quests.party.Party;
import com.vincentmet.customquests.quests.party.QuestPartyProgress;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StructureHandler {
    public static void initQuestingParties(JsonObject json){
        List<Party> questPartyList = new ArrayList<>();

        for(JsonElement jsonPartyElement : ConverterHelper.Parties.getParties(json)) {
            JsonObject jsonParty = jsonPartyElement.getAsJsonObject();
            questPartyList.add(initSingleParty(jsonParty));
        }
        Ref.ALL_QUESTING_PARTIES = questPartyList;
    }

    public static Party initSingleParty(JsonObject json){
        return new Party(
                ConverterHelper.Parties.Party.getId(json),
                ConverterHelper.Parties.Party.getCreator(json),
                ConverterHelper.Parties.Party.getPartyName(json),
                ConverterHelper.Parties.Party.isLootShared(json),
                ConverterHelper.Parties.Party.isFFA(json),
                initSinglePartyProgress(ConverterHelper.Parties.Party.getPartyProgress(json))
        );
    }

    public static QuestPartyProgress initSinglePartyProgress(JsonObject json){
        List<QuestStatus> questStatusList = new ArrayList<>();
        for(JsonElement jsonQuestStatusElement : ConverterHelper.QuestProgress.Players.getQuestStatus(json)){
            JsonObject jsonQuestStatus = jsonQuestStatusElement.getAsJsonObject();
            questStatusList.add(initSingleQuestProgressForUser(jsonQuestStatus));
        }
        return new QuestPartyProgress(
                ConverterHelper.QuestProgress.Players.getFinishedQuests(json),
                questStatusList
        );
    }

    public static void initQuestbook(JsonObject json){
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

    public static void initQuests(JsonObject json) {
        List<Quest> questsList = new ArrayList<>();
        for (JsonElement jsonQuestElement : json.get("quests").getAsJsonArray()) {
            JsonObject jsonQuest = jsonQuestElement.getAsJsonObject();
            questsList.add(initSingleQuest(jsonQuest));
        }
        Ref.ALL_QUESTS = questsList;
    }

    public static Quest initSingleQuest(JsonObject jsonQuest){
        List<QuestRequirement> questRequirementList = new ArrayList<>();
        List<QuestReward> questRewardList = new ArrayList<>();

        for (JsonElement jsonQuestRequirementElement : ConverterHelper.Quests.getRequirements(jsonQuest)) {
            JsonObject jsonQuestRequirement = jsonQuestRequirementElement.getAsJsonObject();

            switch(ConverterHelper.Quests.Requirements.getType(jsonQuestRequirement)){
                case ITEM_DETECT:
                    List<IQuestRequirement> subQuestRequirementsItemDetect = new ArrayList<>();
                    for(JsonElement jsonQuestSubRequirementElement : ConverterHelper.Quests.Requirements.getSubRequirements(jsonQuestRequirement)){
                        JsonObject jsonQuestSubRequirement = jsonQuestSubRequirementElement.getAsJsonObject();

                        subQuestRequirementsItemDetect.add(new QuestRequirement.ItemDetect(
                                new ItemStack(
                                        ConverterHelper.Quests.Requirements.ItemsDetect.getItem(jsonQuestSubRequirement),
                                        ConverterHelper.Quests.Requirements.ItemsDetect.getAmount(jsonQuestSubRequirement),
                                        ConverterHelper.Quests.Requirements.ItemsDetect.getNbt(jsonQuestSubRequirement)
                                )
                        ));
                    }
                    questRequirementList.add(new QuestRequirement(
                            QuestRequirementType.ITEM_DETECT,
                            subQuestRequirementsItemDetect
                    ));
                    break;
                case TRAVEL_TO:
                    List<IQuestRequirement> subQuestRequirementsTravelTo = new ArrayList<>();
                    for(JsonElement jsonQuestSubRequirementElement : ConverterHelper.Quests.Requirements.getSubRequirements(jsonQuestRequirement)){
                        JsonObject jsonQuestSubRequirement = jsonQuestSubRequirementElement.getAsJsonObject();

                        subQuestRequirementsTravelTo.add(new QuestRequirement.TravelTo(
                                ConverterHelper.Quests.Requirements.TravelTo.getDimension(jsonQuestSubRequirement),
                                new BlockPos(
                                        ConverterHelper.Quests.Requirements.TravelTo.getX(jsonQuestSubRequirement),
                                        ConverterHelper.Quests.Requirements.TravelTo.getY(jsonQuestSubRequirement),
                                        ConverterHelper.Quests.Requirements.TravelTo.getZ(jsonQuestSubRequirement)
                                ),
                                ConverterHelper.Quests.Requirements.TravelTo.getRadius(jsonQuestSubRequirement)
                        ));
                    }
                    questRequirementList.add(new QuestRequirement(
                            QuestRequirementType.TRAVEL_TO,
                            subQuestRequirementsTravelTo
                    ));
                    break;
                case CRAFTING_DETECT:
                    List<IQuestRequirement> subQuestRequirementsCraftingDetect = new ArrayList<>();
                    for(JsonElement jsonQuestSubRequirementElement : ConverterHelper.Quests.Requirements.getSubRequirements(jsonQuestRequirement)){
                        JsonObject jsonQuestSubRequirement = jsonQuestSubRequirementElement.getAsJsonObject();

                        subQuestRequirementsCraftingDetect.add(new QuestRequirement.ItemCraft(
                                new ItemStack(
                                        ConverterHelper.Quests.Requirements.ItemsCraft.getItem(jsonQuestSubRequirement),
                                        ConverterHelper.Quests.Requirements.ItemsCraft.getAmount(jsonQuestSubRequirement),
                                        ConverterHelper.Quests.Requirements.ItemsCraft.getNbt(jsonQuestSubRequirement)
                                )
                        ));
                    }
                    questRequirementList.add(new QuestRequirement(
                            QuestRequirementType.CRAFTING_DETECT,
                            subQuestRequirementsCraftingDetect
                    ));
                    break;
                case ITEM_DELIVER:
                    List<IQuestRequirement> subQuestRequirementsItemDeliver = new ArrayList<>();
                    for(JsonElement jsonQuestSubRequirementElement : ConverterHelper.Quests.Requirements.getSubRequirements(jsonQuestRequirement)){
                        JsonObject jsonQuestSubRequirement = jsonQuestSubRequirementElement.getAsJsonObject();

                        subQuestRequirementsItemDeliver.add(new QuestRequirement.ItemSubmit(
                                new ItemStack(
                                        ConverterHelper.Quests.Requirements.ItemsRetrieve.getItem(jsonQuestSubRequirement),
                                        ConverterHelper.Quests.Requirements.ItemsRetrieve.getAmount(jsonQuestSubRequirement),
                                        ConverterHelper.Quests.Requirements.ItemsRetrieve.getNbt(jsonQuestSubRequirement)
                                )
                        ));
                    }
                    questRequirementList.add(new QuestRequirement(
                            QuestRequirementType.ITEM_DELIVER,
                            subQuestRequirementsItemDeliver
                    ));
                    break;
                case KILL_MOB:
                    List<IQuestRequirement> subQuestRequirementsKillMob = new ArrayList<>();
                    for(JsonElement jsonQuestSubRequirementElement : ConverterHelper.Quests.Requirements.getSubRequirements(jsonQuestRequirement)){
                        JsonObject jsonQuestSubRequirement = jsonQuestSubRequirementElement.getAsJsonObject();

                        subQuestRequirementsKillMob.add(new QuestRequirement.KillMob(
                                ForgeRegistries.ENTITIES.getValue(ConverterHelper.Quests.Requirements.KillMob.getEntity(jsonQuestSubRequirement).getRegistryName()),
                                ConverterHelper.Quests.Requirements.KillMob.getAmount(jsonQuestSubRequirement)
                        ));
                    }

                    questRequirementList.add(new QuestRequirement(
                            QuestRequirementType.KILL_MOB,
                            subQuestRequirementsKillMob
                    ));
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
                                    ConverterHelper.Quests.Rewards.SpawnEntity.getEntity(jsonQuestReward),
                                    ConverterHelper.Quests.Rewards.SpawnEntity.getAmount(jsonQuestReward)
                            )
                    ));
                    break;
            }
        }
        return new Quest(
                ConverterHelper.Quests.getId(jsonQuest),
                ConverterHelper.Quests.getTitle(jsonQuest),
                ConverterHelper.Quests.getText(jsonQuest),
                ConverterHelper.Quests.getIcon(jsonQuest),
                ConverterHelper.Quests.Dependencies.getDependencyQuestIds(jsonQuest),
                questRequirementList,
                questRewardList,
                ConverterHelper.Quests.GuiPosition.getGuiPosition(jsonQuest)
        );
    }

    public static void initQuestingProgress(JsonObject jsonProgress){
        Map<String, QuestUserProgress> questUserProgressList = new HashMap<>();

        for(JsonElement jsonUserProgressElement : ConverterHelper.QuestProgress.getPlayers(jsonProgress)){
            JsonObject jsonUserProgress = jsonUserProgressElement.getAsJsonObject();
            QuestUserProgress questUserProgress = initSingleQuestingUserProgress(jsonUserProgress);
            questUserProgressList.put(questUserProgress.getUuid(), questUserProgress);
        }

        Ref.ALL_QUESTING_PROGRESS = questUserProgressList;
    }

    public static QuestUserProgress initSingleQuestingUserProgress(JsonObject jsonUserProgress){
        Map<Integer, QuestStatus> questStatusList = new HashMap<>();
        for(JsonElement jsonQuestStatusElement : ConverterHelper.QuestProgress.Players.getQuestStatus(jsonUserProgress)){
            JsonObject jsonQuestStatus = jsonQuestStatusElement.getAsJsonObject();
            QuestStatus newQs = initSingleQuestProgressForUser(jsonQuestStatus);
            questStatusList.put(newQs.getQuestId(), newQs);
        }
        return new QuestUserProgress(
                ConverterHelper.QuestProgress.Players.getUUID(jsonUserProgress),
                ConverterHelper.QuestProgress.Players.getPartyId(jsonUserProgress),
                ConverterHelper.QuestProgress.Players.getFinishedQuests(jsonUserProgress),
                questStatusList
        );
    }

    public static QuestStatus initSingleQuestProgressForUser(JsonObject jsonQuestStatus){
        List<QuestRequirementStatus> questStatusRequirementList = new ArrayList<>();
        int reqCount = 0;
        for(JsonElement jsonQuestRequirementStatusElement : ConverterHelper.QuestProgress.Players.QuestStatus.getRequirementCompletion(jsonQuestStatus)){
            questStatusRequirementList.add(new QuestRequirementStatus(
                    ConverterHelper.QuestProgress.Players.QuestStatus.getId(jsonQuestStatus),
                    reqCount,
                    ConverterHelper.QuestProgress.Players.QuestStatus.RequirementCompletion.getSubRequirementCompletionStatusList(jsonQuestRequirementStatusElement.getAsJsonArray())
            ));
            reqCount++;
        }

        return new QuestStatus(
                ConverterHelper.QuestProgress.Players.QuestStatus.getId(jsonQuestStatus),
                ConverterHelper.QuestProgress.Players.QuestStatus.getIsClaimed(jsonQuestStatus),
                questStatusRequirementList
        );
    }
}