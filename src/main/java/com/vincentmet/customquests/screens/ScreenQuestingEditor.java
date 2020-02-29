package com.vincentmet.customquests.screens;

import com.vincentmet.customquests.BaseClass;
import com.vincentmet.customquests.lib.MouseDirection;
import com.vincentmet.customquests.screens.elements.IQuestingGuiElement;
import com.vincentmet.customquests.screens.questingeditorsubscreens.SubScreenAllQuestlines;
import com.vincentmet.customquests.screens.questingeditorsubscreens.SubScreensQuestingEditor;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.EnumMap;
import java.util.Map;

public class ScreenQuestingEditor extends Screen {
    private static PlayerEntity player = BaseClass.proxy.getClientPlayer();
    private static Map<SubScreensQuestingEditor, IQuestingGuiElement> screens = new EnumMap<>(SubScreensQuestingEditor.class);
    private static SubScreensQuestingEditor activeScreen = SubScreensQuestingEditor.ALL_QUESTLINES;

    public ScreenQuestingEditor() {
        super(new TranslationTextComponent(""));

        screens.put(SubScreensQuestingEditor.ALL_QUESTLINES, new SubScreenAllQuestlines(this));
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        screens.entrySet()
                .stream()
                .filter(subScreensQuestingDeviceIQuestingGuiElementEntry ->
                        subScreensQuestingDeviceIQuestingGuiElementEntry.getKey().equals(activeScreen)
                )
                .forEach(subScreensQuestingDeviceIQuestingGuiElementEntry -> {
                    subScreensQuestingDeviceIQuestingGuiElementEntry.getValue().update(player, mouseX, mouseY, this.width, this.height);
                    subScreensQuestingDeviceIQuestingGuiElementEntry.getValue().render(player, mouseX, mouseY);
                })
        ;
        super.render(mouseX, mouseY, partialTicks);
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