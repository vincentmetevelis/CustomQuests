package com.vincentmet.customquests.screens;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.vincentmet.customquests.lib.MouseDirection;
import com.vincentmet.customquests.screens.elements.IQuestingGuiElement;
import com.vincentmet.customquests.screens.questingeditorsubscreens.*;
import java.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

public class ScreenQuestingEditor extends Screen {
    private static PlayerEntity player = Minecraft.getInstance().player;
    private static Map<SubScreensQuestingEditor, IQuestingGuiElement> screens = new EnumMap<>(SubScreensQuestingEditor.class);
    private static SubScreensQuestingEditor activeScreen = SubScreensQuestingEditor.ALL_QUESTLINES;

    public ScreenQuestingEditor() {
        super(new TranslationTextComponent(""));

        screens.put(SubScreensQuestingEditor.ALL_QUESTLINES, new SubScreenAllQuestlines(this));
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(stack);
        screens.entrySet()
                .stream()
                .filter(subScreensQuestingDeviceIQuestingGuiElementEntry ->
                        subScreensQuestingDeviceIQuestingGuiElementEntry.getKey().equals(activeScreen)
                )
                .forEach(subScreensQuestingDeviceIQuestingGuiElementEntry -> {
                    subScreensQuestingDeviceIQuestingGuiElementEntry.getValue().update(player, mouseX, mouseY, this.width, this.height);
                    subScreensQuestingDeviceIQuestingGuiElementEntry.getValue().render(stack, player, mouseX, mouseY);
                })
        ;
        super.render(stack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int partialTicks) {
        screens.entrySet()
                .stream()
                .filter(subScreensQuestingDeviceIQuestingGuiElementEntry ->
                        subScreensQuestingDeviceIQuestingGuiElementEntry.getKey().equals(activeScreen)
                )
                .forEach(subScreensQuestingDeviceIQuestingGuiElementEntry ->
                        subScreensQuestingDeviceIQuestingGuiElementEntry.getValue().onClick(player, mouseX, mouseY)
                )
        ;
        return super.mouseClicked(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean keyPressed(int glfwKey, int scanCode, int modifierKey) { //------- mod-key: 0x0001=Shift; 0x0010=Ctrl; 0x0100=Alt; 0x1000=WinKey;
        screens.entrySet()
                .stream()
                .filter(subScreensQuestingDeviceIQuestingGuiElementEntry ->
                        subScreensQuestingDeviceIQuestingGuiElementEntry.getKey().equals(activeScreen)
                )
                .forEach(subScreensQuestingDeviceIQuestingGuiElementEntry ->
                        subScreensQuestingDeviceIQuestingGuiElementEntry.getValue().onKeyPress(glfwKey, modifierKey)
                )
        ;
        return super.keyPressed(glfwKey, scanCode, modifierKey);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double direction) { // direction: down = -1; up = +1;
        screens.entrySet()
                .stream()
                .filter(subScreensQuestingDeviceIQuestingGuiElementEntry ->
                        subScreensQuestingDeviceIQuestingGuiElementEntry.getKey().equals(activeScreen)
                )
                .forEach(subScreensQuestingDeviceIQuestingGuiElementEntry ->
                        subScreensQuestingDeviceIQuestingGuiElementEntry.getValue().onMouseScroll(mouseX, mouseY, direction>0? MouseDirection.UP:MouseDirection.DOWN)
                )
        ;
        return super.mouseScrolled(mouseX, mouseY, direction);
    }

    @Override
    public boolean keyReleased(int glfwKey, int scanCode, int modifier) {
        return false;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public static SubScreensQuestingEditor getActiveScreen() {
        return activeScreen;
    }

    public static void setActiveScreen(SubScreensQuestingEditor activeScreen) {
        ScreenQuestingEditor.activeScreen = activeScreen;
    }
}