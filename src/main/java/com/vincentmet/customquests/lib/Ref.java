package com.vincentmet.customquests.lib;

import com.vincentmet.customquests.quests.Quest;
import com.vincentmet.customquests.quests.QuestMenu;
import com.vincentmet.customquests.quests.QuestUserProgress;
import com.vincentmet.customquests.screens.elements.IQuestingGuiElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class Ref {
    public static final String MODID = "customquests";
    public static final String NAME = "Custom Quests";
    public static final String VERSION = "1.14.4-0.1";
    public static final String RESOURCE_PATH = MODID + ":";
    
    public static final ResourceLocation IMAGE_BUTTON_HEXAGON_UNPRESSED = new ResourceLocation(MODID, "textures/gui/button_hexagon_unpressed.png");
    public static final ResourceLocation IMAGE_BUTTON_HEXAGON_PRESSED = new ResourceLocation(MODID, "textures/gui/button_hexagon_pressed.png");
    public static final ResourceLocation IMAGE_BUTTON_HEXAGON_DISABLED = new ResourceLocation(MODID, "textures/gui/button_hexagon_disabled.png");
    public static final ResourceLocation IMAGE_BUTTON_HEXAGON_COMPLETED = new ResourceLocation(MODID, "textures/gui/button_hexagon_completed.png");
    public static final ResourceLocation IMAGE_BUTTON_QUESTLINE_UNPRESSED = new ResourceLocation(MODID, "textures/gui/button_rect_unpressed.png");
    public static final ResourceLocation IMAGE_BUTTON_QUESTLINE_PRESSED = new ResourceLocation(MODID, "textures/gui/button_rect_pressed.png");
    public static final ResourceLocation IMAGE_BUTTON_QUESTLINE_DISABLED = new ResourceLocation(MODID, "textures/gui/button_rect_disabled.png");
    public static final ResourceLocation IMAGE_BUTTON_CLAIM_REWARD_UNPRESSED = new ResourceLocation(MODID, "textures/gui/button_rect_small_unpressed.png");//also used for hand in button for "hand-in" quests
    public static final ResourceLocation IMAGE_BUTTON_CLAIM_REWARD_DISABLED = new ResourceLocation(MODID, "textures/gui/button_rect_small_disabled.png");//also used for hand in button for "hand-in" quests
    public static final ResourceLocation IMAGE_BUTTON_CLAIM_REWARD_PRESSED = new ResourceLocation(MODID, "textures/gui/button_rect_small_pressed.png");//also used for hand in button for "hand-in" quests
    public static final ResourceLocation IMAGE_COLOR_BLACK = new ResourceLocation(MODID, "textures/gui/black.png");
    public static final ResourceLocation IMAGE_COLOR_RED = new ResourceLocation(MODID, "textures/gui/red.png");
    public static final ResourceLocation IMAGE_COLOR_GREEN = new ResourceLocation(MODID, "textures/gui/green.png");
    public static final ResourceLocation IMAGE_COLOR_YELLOW = new ResourceLocation(MODID, "textures/gui/yellow.png");
    public static final ResourceLocation IMAGE_COLOR_WHITE = new ResourceLocation(MODID, "textures/gui/white.png");
    public static final ResourceLocation IMAGE_ITEMBOX_BACKGROUND = new ResourceLocation(MODID, "textures/gui/item_background.png");

    public static final int GUI_QUESTING_LINE_THICKNESS = 1;
    public static final int GUI_QUESTING_MARGIN_LEFT = 200;
    public static final int GUI_QUESTING_MARGIN_TOP = 20;
    public static final int GUI_QUESTLINES_MARGIN_LEFT = 20;
    public static final int GUI_QUESTLINES_MARGIN_TOP = 20;

    public static final String ERR_MSG_STR_INVALID_JSON = "INVALID JSON";
    public static final int ERR_MSG_INT_INVALID_JSON = -1337;
    public static final boolean ERR_MSG_BOOL_INVALID_JSON = false;
    public static boolean shouldSaveNextTick = false;
    public static String questBookLocation = "";
    public static String questsLocation = "";
    public static String questingProgressLocation = "";
    public static ResourceLocation defaultQuestBookLocation = new ResourceLocation(MODID, "json/default_questbook.json");
    public static ResourceLocation defaultQuestsLocation = new ResourceLocation(MODID, "json/default_quests.json");
    public static ResourceLocation defaultQuestingProgressLocation = new ResourceLocation(MODID, "json/default_questing_progress.json");
    public static QuestMenu ALL_QUESTBOOK;
    public static List<Quest> ALL_QUESTS;
    public static List<QuestUserProgress> ALL_QUESTING_PROGRESS;
    public static List<IQuestingGuiElement> ALL_GUI_ELEMENTS = new ArrayList<>();

    public static final FontRenderer FONT_RENDERER = Minecraft.getInstance().fontRenderer;
}