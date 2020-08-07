package com.vincentmet.customquests.screens.elements.labels;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.vincentmet.customquests.lib.*;
import com.vincentmet.customquests.screens.elements.IQuestingGuiElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextProperties;
import net.minecraftforge.api.distmarker.*;

@OnlyIn(Dist.CLIENT)
public class MultilineLabel implements IQuestingGuiElement {
    private Screen root;
    private int x;
    private int y;
    private String text;
    private int color;
    private int maxWidth;

    public MultilineLabel(Screen root, int posX, int posY, String text, int color, int maxWidth){
        this.root = root;
        this.x = posX;
        this.y = posY;
        this.text = Utils.colorify(text);//fixme formatting resets every new line, make it so that it gets the active formatting elements from the last string after splitting it.
        this.color = color;
        this.maxWidth = maxWidth;
    }

    @Override
    public void update(PlayerEntity player, double mouseX, double mouseY, int width, int height) {

    }

    @Override
    public void render(MatrixStack stack, PlayerEntity player, double mouseX, double mouseY) {
        int currentHeight = y;
        for(ITextProperties line : Minecraft.getInstance().fontRenderer.func_238425_b_(ITextProperties.func_240652_a_(text), maxWidth)){
            root.drawString(stack, Minecraft.getInstance().fontRenderer, line, x, currentHeight, color);
            currentHeight += Minecraft.getInstance().fontRenderer.FONT_HEIGHT;
        }
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
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public boolean isVisible() {
        return true;
    }
}