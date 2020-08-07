package com.vincentmet.customquests.screens.questingeditorsubscreens;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.datafixers.util.Pair;
import com.vincentmet.customquests.lib.*;
import com.vincentmet.customquests.screens.elements.*;
import java.util.*;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;

public class SubScreenAllQuestlines implements IQuestingGuiElement {
    private Screen root;
    public static final int MARGIN = 20;
    private int width = 0;
    private int height = 0;
    private TextField txt;
    private ScrollableList scrollableList = new ScrollableList(0, 0);

    public SubScreenAllQuestlines(Screen root){
        this.root = root;
        List<List<Pair<Character, Integer>>> lines = new ArrayList<>();
        lines.add(TextField.stringToCharIntList(Utils.getFormattedText(".editor.example_text0")));
        lines.add(TextField.stringToCharIntList(Utils.getFormattedText(".editor.example_text1")));
        lines.add(TextField.stringToCharIntList(Utils.getFormattedText(".editor.example_text2")));
        lines.add(TextField.stringToCharIntList(Utils.getFormattedText(".editor.example_text3")));
        lines.add(TextField.stringToCharIntList(Utils.getFormattedText(".editor.example_text3")));
        lines.add(TextField.stringToCharIntList(Utils.getFormattedText(".editor.example_text3")));
        lines.add(TextField.stringToCharIntList(Utils.getFormattedText(".editor.example_text3")));
        this.txt = new TextField(root.width>>2, root.height>>2, 300, 7, lines, 0);
    }

    @Override
    public void update(PlayerEntity player, double mouseX, double mouseY, int width, int height) {
        this.width = width;
        this.height = height;
        this.txt.update(player, mouseX, mouseY, root.width>>2, root.height>>2);
        this.scrollableList.update(player, mouseX, mouseY, root.width>>2, root.height>>2);
    }

    @Override
    public void render(MatrixStack stack, PlayerEntity player, double mouseX, double mouseY) {
        this.txt.render(stack, player, mouseX, mouseY);
        this.scrollableList.render(stack, player, mouseX, mouseY);
    }

    @Override
    public void onClick(PlayerEntity player, double mouseX, double mouseY) {
        this.txt.onClick(player, mouseX, mouseY);
        this.scrollableList.onClick(player, mouseX, mouseY);
    }

    @Override
    public void onKeyPress(int key, int mod) {
        this.txt.onKeyPress(key, mod);
        this.scrollableList.onKeyPress(key, mod);
    }

    @Override
    public void onMouseScroll(double mouseX, double mouseY, MouseDirection direction) {
        this.txt.onMouseScroll(mouseX, mouseY, direction);
        this.scrollableList.onMouseScroll(mouseX, mouseY, direction);
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public boolean isVisible() {
        return true;
    }
}
