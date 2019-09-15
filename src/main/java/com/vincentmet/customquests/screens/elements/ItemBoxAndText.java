package com.vincentmet.customquests.screens.elements;

import com.vincentmet.customquests.screens.ScreenQuestingDevice;
import net.minecraft.item.ItemStack;

public class ItemBoxAndText implements IQuestingGuiElement {
    private int x;
    private int y;
    private ItemStack itemstack;
    private String text;
    private int color;

    public ItemBoxAndText(int posX, int posY, ItemStack itemstack, String text, int color){ //todo add centered bool option?
        this.x = posX;
        this.y = posY;
        this.itemstack = itemstack;
        this.text = text;
        this.color = color;
    }

    @Override
    public <T extends ScreenQuestingDevice> void render(T gui, double mouseX, double mouseY) {
        new ItemBox(x, y, itemstack).render(gui, mouseX, mouseY);
        new Label(x + 20, y + (18/2 - 8/2), text, color, false, false).render(gui, mouseX, mouseY);
    }

    @Override
    public <T extends ScreenQuestingDevice> void onClick(T gui, double mouseX, double mouseY) {

    }
}
