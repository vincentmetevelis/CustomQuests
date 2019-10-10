package com.vincentmet.customquests.screens.elements;

import com.vincentmet.customquests.screens.ScreenQuestingDevice;
import net.minecraft.entity.player.PlayerEntity;

public interface IQuestingGuiElement {
    <T extends ScreenQuestingDevice> void render(T gui, PlayerEntity player, double mouseX, double mouseY);
    <T extends ScreenQuestingDevice> void onClick(T gui, PlayerEntity player, double mouseX, double mouseY);
}
