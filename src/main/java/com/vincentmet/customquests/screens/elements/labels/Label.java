package com.vincentmet.customquests.screens.elements.labels;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.vincentmet.customquests.lib.*;
import com.vincentmet.customquests.screens.elements.IQuestingGuiElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.*;

@OnlyIn(Dist.CLIENT)
public class Label implements IQuestingGuiElement {
    private Screen root;
    private int x = 0;
    private int y = 0;
    private String text = "";
    private int color = 0xFFFFFF;

    public Label(Screen root, String text, int x, int y, int color, boolean xCentered, boolean yCentered){
        this.root = root;
        this.text = Utils.colorify(text);
        this.x = x;
        this.y = y;
        this.color = color;
        if(xCentered){
            this.x -= (Minecraft.getInstance().fontRenderer.getStringWidth(this.text)>>1);
        }
        if(yCentered){
            this.y -= (Minecraft.getInstance().fontRenderer.FONT_HEIGHT>>1);
        }
    }

    @Override
    public void update(PlayerEntity player, double mouseX, double mouseY, int width, int height) {

    }

    @Override
    public void render(MatrixStack stack, PlayerEntity player, double mouseX, double mouseY) {
        Minecraft.getInstance().fontRenderer.drawStringWithShadow(stack, this.text, this.x, this.y, this.color);
    }

    @Override
    public void onClick(PlayerEntity player, double mouseX, double mouseY) {

    }

    @Override
    public void onKeyPress(int key, int mod) {

    }

    @Override
    public void onMouseScroll(double mouseX, double mouseY, MouseDirection direction) {

    }

    @Override
    public int getWidth() {
        return Minecraft.getInstance().fontRenderer.getStringWidth(text);
    }

    @Override
    public int getHeight() {
        return Minecraft.getInstance().fontRenderer.FONT_HEIGHT;
    }

    @Override
    public boolean isVisible() {
        return true;
    }
}