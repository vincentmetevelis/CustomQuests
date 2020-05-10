package com.vincentmet.customquests.lib;

import com.mojang.datafixers.util.Pair;
import com.vincentmet.customquests.quests.*;
import com.vincentmet.customquests.quests.party.Party;
import java.nio.file.Path;
import java.util.*;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.*;
import org.lwjgl.glfw.GLFW;

public class Ref {
    public static final String MODID = "customquests";
    
    public static final ResourceLocation IMAGE_BUTTON_MODULAR = new ResourceLocation(Ref.MODID, "textures/gui/button_template.png");
    public static final ResourceLocation IMAGE_BUTTON_HEXAGON_UNPRESSED = new ResourceLocation(MODID, "textures/gui/button_hexagon_unpressed.png");
    public static final ResourceLocation IMAGE_BUTTON_HEXAGON_PRESSED = new ResourceLocation(MODID, "textures/gui/button_hexagon_pressed.png");
    public static final ResourceLocation IMAGE_BUTTON_HEXAGON_DISABLED = new ResourceLocation(MODID, "textures/gui/button_hexagon_disabled.png");
    public static final ResourceLocation IMAGE_BUTTON_HEXAGON_COMPLETED = new ResourceLocation(MODID, "textures/gui/button_hexagon_completed.png");
    public static final ResourceLocation IMAGE_BUTTON_HEXAGON_UNCLAIMED = new ResourceLocation(MODID, "textures/gui/button_hexagon_unclaimed.png");
    public static final ResourceLocation IMAGE_BUTTON_QUESTLINE_UNPRESSED = new ResourceLocation(MODID, "textures/gui/button_rect_unpressed.png");
    public static final ResourceLocation IMAGE_BUTTON_QUESTLINE_PRESSED = new ResourceLocation(MODID, "textures/gui/button_rect_pressed.png");
    public static final ResourceLocation IMAGE_BUTTON_QUESTLINE_DISABLED = new ResourceLocation(MODID, "textures/gui/button_rect_disabled.png");
    public static final ResourceLocation IMAGE_BUTTON_SQUARE_UNPRESSED = new ResourceLocation(MODID, "textures/gui/button_square_unpressed.png");
    public static final ResourceLocation IMAGE_BUTTON_SQUARE_PRESSED = new ResourceLocation(MODID, "textures/gui/button_square_pressed.png");
    public static final ResourceLocation IMAGE_BUTTON_SQUARE_DISABLED = new ResourceLocation(MODID, "textures/gui/button_square_disabled.png");
    public static final ResourceLocation IMAGE_BUTTON_CLAIM_REWARD_UNPRESSED = new ResourceLocation(MODID, "textures/gui/button_rect_small_unpressed.png");//also used for hand in button for "hand-in" quests
    public static final ResourceLocation IMAGE_BUTTON_CLAIM_REWARD_DISABLED = new ResourceLocation(MODID, "textures/gui/button_rect_small_disabled.png");//also used for hand in button for "hand-in" quests
    public static final ResourceLocation IMAGE_BUTTON_CLAIM_REWARD_PRESSED = new ResourceLocation(MODID, "textures/gui/button_rect_small_pressed.png");//also used for hand in button for "hand-in" quests
    public static final ResourceLocation IMAGE_COLOR_BLACK = new ResourceLocation(MODID, "textures/gui/black.png");
    public static final ResourceLocation IMAGE_COLOR_RED = new ResourceLocation(MODID, "textures/gui/red.png");
    public static final ResourceLocation IMAGE_COLOR_GREEN = new ResourceLocation(MODID, "textures/gui/green.png");
    public static final ResourceLocation IMAGE_COLOR_YELLOW = new ResourceLocation(MODID, "textures/gui/yellow.png");
    public static final ResourceLocation IMAGE_COLOR_WHITE = new ResourceLocation(MODID, "textures/gui/white.png");
    public static final ResourceLocation IMAGE_ITEMBOX_BACKGROUND = new ResourceLocation(MODID, "textures/gui/item_background.png");
    public static final ResourceLocation IMAGE_ICON_HOME = new ResourceLocation(MODID, "textures/gui/home_icon.png");
    public static final ResourceLocation IMAGE_ICON_BACK = new ResourceLocation(MODID, "textures/gui/back_icon.png");
    public static final int GUI_QUESTING_LINE_THICKNESS = 1;
    public static final int GUI_QUESTING_MARGIN_LEFT = 200;
    public static final int GUI_QUESTING_MARGIN_TOP = 20;
    public static final int GUI_QUESTLINES_MARGIN_LEFT = 20;
    public static final int GUI_QUESTLINES_MARGIN_TOP = 20;

    public static final String ERR_MSG_STR_INVALID_JSON = "INVALID JSON";
    public static final int ERR_MSG_INT_INVALID_JSON = -1337;
    public static final boolean ERR_MSG_BOOL_INVALID_JSON = false;
    public static boolean shouldSaveNextTick = false;
    public static int currentFocussedTextfield = -1;
    public static Path questBookLocation;
    public static Path questsLocation;
    public static Path questingProgressLocation;
    public static Path questingPartiesLocation;
    public static Path currWorldDir;
    public static QuestMenu ALL_QUESTBOOK;
    public static Map<Integer, Quest> ALL_QUESTS = new HashMap<>();
    public static Map<String, QuestUserProgress> ALL_QUESTING_PROGRESS = new HashMap<>();
    public static List<Party> ALL_QUESTING_PARTIES = new ArrayList<>();

    @OnlyIn(Dist.CLIENT)
    public static FontRenderer FONT_RENDERER;

    public static final Map<Integer, Pair<Character, Character>> KEY_MAPPINGS = new HashMap<>();
    static {
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_A, new Pair<>('a', 'A'));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_B, new Pair<>('b', 'B'));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_C, new Pair<>('c', 'C'));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_D, new Pair<>('d', 'D'));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_E, new Pair<>('e', 'E'));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_F, new Pair<>('f', 'F'));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_G, new Pair<>('g', 'G'));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_H, new Pair<>('h', 'H'));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_I, new Pair<>('i', 'I'));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_J, new Pair<>('j', 'J'));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_K, new Pair<>('k', 'K'));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_L, new Pair<>('l', 'L'));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_M, new Pair<>('m', 'M'));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_N, new Pair<>('n', 'N'));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_O, new Pair<>('o', 'O'));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_P, new Pair<>('p', 'P'));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_Q, new Pair<>('q', 'Q'));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_R, new Pair<>('r', 'R'));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_S, new Pair<>('s', 'S'));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_T, new Pair<>('t', 'T'));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_U, new Pair<>('u', 'U'));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_V, new Pair<>('v', 'V'));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_W, new Pair<>('w', 'W'));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_X, new Pair<>('x', 'X'));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_Y, new Pair<>('y', 'Y'));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_Z, new Pair<>('z', 'Z'));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_0, new Pair<>('0', ')'));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_1, new Pair<>('1', '!'));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_2, new Pair<>('2', '@'));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_3, new Pair<>('3', '#'));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_4, new Pair<>('4', '$'));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_5, new Pair<>('5', '%'));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_6, new Pair<>('6', '^'));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_7, new Pair<>('7', '&'));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_8, new Pair<>('8', '*'));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_9, new Pair<>('9', '('));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_KP_0, new Pair<>('0', '0'));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_KP_1, new Pair<>('1', '1'));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_KP_2, new Pair<>('2', '2'));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_KP_3, new Pair<>('3', '3'));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_KP_4, new Pair<>('4', '4'));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_KP_5, new Pair<>('5', '5'));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_KP_6, new Pair<>('6', '6'));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_KP_7, new Pair<>('7', '7'));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_KP_8, new Pair<>('8', '8'));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_KP_9, new Pair<>('9', '9'));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_KP_ADD, new Pair<>('+', '+'));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_KP_SUBTRACT, new Pair<>('-', '-'));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_KP_DIVIDE, new Pair<>('/', '/'));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_KP_MULTIPLY, new Pair<>('*', '*'));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_KP_EQUAL, new Pair<>('=', '='));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_KP_DECIMAL, new Pair<>('.', '.'));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_BACKSLASH, new Pair<>('\\', '|'));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_GRAVE_ACCENT, new Pair<>('`', '~'));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_APOSTROPHE, new Pair<>('\'', '\"'));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_COMMA, new Pair<>(',', '<'));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_PERIOD, new Pair<>('.', '>'));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_SLASH, new Pair<>('/', '?'));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_EQUAL, new Pair<>('=', '+'));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_MINUS, new Pair<>('-', '_'));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_SEMICOLON, new Pair<>(';', ':'));
        KEY_MAPPINGS.put(GLFW.GLFW_KEY_SPACE, new Pair<>(' ', ' '));
    }
}