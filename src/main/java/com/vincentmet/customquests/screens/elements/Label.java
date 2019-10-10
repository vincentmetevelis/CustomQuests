package com.vincentmet.customquests.screens.elements;

import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.screens.ScreenQuestingDevice;
import net.minecraft.entity.player.PlayerEntity;

public class Label implements IQuestingGuiElement {
    private int x;
    private int y;
    private String text;
    private int color;
    private boolean xCentered;

    public Label(int posX, int posY, String text, int color, boolean xCentered, boolean yCentered){
        this.x = posX;
        this.y = posY;
        this.text = text;
        this.color = color;
        this.xCentered = xCentered;
        if(xCentered){
            x -= (Ref.FONT_RENDERER.getStringWidth(text)/2);
        }
        if(yCentered){
            y -= (Ref.FONT_RENDERER.FONT_HEIGHT/2);
        }
    }

    @Override
    public <T extends ScreenQuestingDevice> void render(T gui, PlayerEntity player, double mouseX, double mouseY) {
        gui.drawString(Ref.FONT_RENDERER, text, x, y, color);
    }

    @Override
    public <T extends ScreenQuestingDevice> void onClick(T gui, PlayerEntity player, double mouseX, double mouseY) {

    }
}
