package com.vincentmet.customquests.screens.elements.labels;

import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.screens.elements.IQuestingGuiElement;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import com.vincentmet.customquests.lib.Utils;

@OnlyIn(Dist.CLIENT)
public class Label implements IQuestingGuiElement {
    private Screen root;
    private int x;
    private int y;
    private String text;
    private int color;

    public Label(Screen root, String text, int x, int y, int color, boolean xCentered, boolean yCentered){
        this.root = root;
        this.text = Utils.colorify(text);
        this.x = x;
        this.y = y;
        this.color = color;
        if(xCentered){
            this.x -= (Ref.FONT_RENDERER.getStringWidth(this.text)>>1);
        }
        if(yCentered){
            this.y -= (Ref.FONT_RENDERER.FONT_HEIGHT>>1);
        }
    }

    @Override
    public void update(PlayerEntity player, double mouseX, double mouseY, int width, int height) {

    }

    @Override
    public void render(PlayerEntity player, double mouseX, double mouseY) {
        root.drawString(Ref.FONT_RENDERER, this.text, this.x, this.y, this.color);
    }

    @Override
    public void onClick(PlayerEntity player, double mouseX, double mouseY) {

    }

    @Override
    public int getWidth() {
        return Ref.FONT_RENDERER.getStringWidth(text);
    }

    @Override
    public int getHeight() {
        return Ref.FONT_RENDERER.FONT_HEIGHT;
    }

    @Override
    public boolean isVisible() {
        return true;
    }
}