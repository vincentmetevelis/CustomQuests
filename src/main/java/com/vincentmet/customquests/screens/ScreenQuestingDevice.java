package com.vincentmet.customquests.screens;

import com.vincentmet.customquests.screens.elements.*;
import com.vincentmet.customquests.screens.elements.buttons.ButtonQuestlines;
import com.vincentmet.customquests.screens.questingdeveicesubscreens.*;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import java.util.*;

@OnlyIn(Dist.CLIENT)
public class ScreenQuestingDevice extends Screen {
    private PlayerEntity player;
    //private ButtonHome buttonHome = new ButtonHome(this);
    private ButtonQuestlines buttonQuestlines = new ButtonQuestlines(this);
    //private ButtonParties buttonParties = new ButtonParties(this);
    //private ButtonSettings buttonSettings = new ButtonSettings(this);

    private static Map<SubScreensQuestingDevice, IQuestingGuiElement> screens = new EnumMap<>(SubScreensQuestingDevice.class);
    private static SubScreensQuestingDevice activeScreen = SubScreensQuestingDevice.QUESTLINES;

    public ScreenQuestingDevice(ITextComponent titleIn, PlayerEntity player) {
        super(titleIn);
        this.player = player;

        //screens.put(SubScreensQuestingDevice.MAIN_MENU, new SubScreenMainMenu(this));
        //screens.put(SubScreensQuestingDevice.PARTY_SCREEN, new SubScreenParties(this));
        screens.put(SubScreensQuestingDevice.QUESTLINES, new SubScreenQuestlines(this));
        //screens.put(SubScreensQuestingDevice.SETTINGS, new SubScreenSettings(this));
        screens.put(SubScreensQuestingDevice.QUEST_DETAILS, new SubScreenQuestDetails(this));
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
        //this.buttonHome.update(player, mouseX, mouseY, 0, 0);
        //this.buttonHome.render(player, mouseX, mouseY);
        this.buttonQuestlines.update(player, mouseX, mouseY, 0, 0);
        this.buttonQuestlines.render(player, mouseX, mouseY);
        //this.buttonParties.update(player, mouseX, mouseY, 0, 0);
        //this.buttonParties.render(player, mouseX, mouseY);
        //this.buttonSettings.update(player, mouseX, mouseY, 0, 0);
        //this.buttonSettings.render(player, mouseX, mouseY);

        //ButtonDrawer.draw(200, 200, 50, 50, ButtonDrawer.ButtonState.PRESSED);

        super.render(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton){
        screens.entrySet()
                .stream()
                .filter(subScreensQuestingDeviceIQuestingGuiElementEntry ->
                        subScreensQuestingDeviceIQuestingGuiElementEntry.getKey().equals(activeScreen)
                )
                .forEach(subScreensQuestingDeviceIQuestingGuiElementEntry ->
                        subScreensQuestingDeviceIQuestingGuiElementEntry.getValue().onClick(player, mouseX, mouseY)
                )
        ;

        //this.buttonHome.onClick(player, mouseX, mouseY);
        this.buttonQuestlines.onClick(player, mouseX, mouseY);
        //this.buttonParties.onClick(player, mouseX, mouseY);
        //this.buttonSettings.onClick(player, mouseX, mouseY);

        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public static SubScreensQuestingDevice getActiveScreen() {
        return activeScreen;
    }

    public static void setActiveScreen(SubScreensQuestingDevice activeScreen) {
        ScreenQuestingDevice.activeScreen = activeScreen;
    }


}