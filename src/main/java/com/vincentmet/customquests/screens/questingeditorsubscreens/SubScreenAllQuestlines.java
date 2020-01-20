package com.vincentmet.customquests.screens.questingeditorsubscreens;

import com.vincentmet.customquests.lib.TextField;
import com.vincentmet.customquests.screens.elements.IQuestingGuiElement;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.List;

public class SubScreenAllQuestlines implements IQuestingGuiElement {
    private Screen root;
    public static final int MARGIN = 20;
    private int width = 0;
    private int height = 0;
    private TextField txt;

    public SubScreenAllQuestlines(Screen root){
        this.root = root;
        List<String> lines = new ArrayList<>();
        lines.add("Some very long text");
        lines.add("Short Line");
        lines.add("Lorum ipsum dolor si amet bla bla bla----------");
        lines.add("And another short line");
        lines.add("And another short line");
        lines.add("And another short line");
        lines.add("And another short line");//todo fix overflow
        this.txt = new TextField(root.width>>1, root.height>>1, 300, 7, lines, 0);
    }

    @Override
    public void update(PlayerEntity player, double mouseX, double mouseY, int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void render(PlayerEntity player, double mouseX, double mouseY) {
        this.txt.render();
    }

    @Override
    public void onClick(PlayerEntity player, double mouseX, double mouseY) {
        this.txt.onClick(mouseX, mouseY);
    }

    @Override
    public void onKeyPress(int key, int mod) {
        this.txt.onKeyPress(key, mod);
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

    private boolean isFocussed(){
        return true;
    }
}
