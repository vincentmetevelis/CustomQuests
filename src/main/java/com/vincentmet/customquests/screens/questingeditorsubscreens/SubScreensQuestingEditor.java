package com.vincentmet.customquests.screens.questingeditorsubscreens;

public enum SubScreensQuestingEditor { //todo implement this
    ALL_QUESTLINES(), // Questingdevice title, description and list of all quests
    ALL_QUESTS(), // Questline title, description and list off all quests withing the questline
    SINGLE_QUEST(), // Quest title, text, position, and list of requirements and rewards
    ALL_REQUIREMENTS(), // Requirement type, list of subrequirements
    SINGLE_SUBREQUIREMENT(), // Subrequirement content that depends on requirement type (i.e. item, amount and nbt)
    ALL_REWARDS(), // List of all individual rewards
    SINGLE_REWARD(), // Reward type, button to content sub-page
    SINGLE_REWARD_DETAILS(); // Reward content (i.e. command, item, etc)

    SubScreensQuestingEditor(){

    }
}