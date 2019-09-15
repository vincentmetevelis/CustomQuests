package com.vincentmet.customquests.screens.elements;

import com.vincentmet.customquests.screens.ScreenQuestingDevice;

public interface IQuestingGuiElement {
    <T extends ScreenQuestingDevice> void render(T gui, double mouseX, double mouseY);
    <T extends ScreenQuestingDevice> void onClick(T gui, double mouseX, double mouseY);
}
