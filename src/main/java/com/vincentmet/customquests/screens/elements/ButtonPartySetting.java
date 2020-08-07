package com.vincentmet.customquests.screens.elements;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.vincentmet.customquests.lib.*;
import com.vincentmet.customquests.screens.elements.labels.Label;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.*;

@OnlyIn(Dist.CLIENT)
public class ButtonPartySetting implements IQuestingGuiElement {
    private Screen root;
    public static final int WIDTH = 64;
    public static final int HEIGHT = 20;
    private int x;
    private int y;
    private String text;

    public ButtonPartySetting(Screen root, int posX, int posY, String text){
        this.root = root;
        this.x = posX;
        this.y = posY;
        this.text = text;
    }

    @Override
    public void update(PlayerEntity player, double mouseX, double mouseY, int width, int height) {

    }

    @Override
    public void render(MatrixStack stack, PlayerEntity player, double mouseX, double mouseY) {
        if(Utils.isMouseInBounds(mouseX, mouseY, x, y, x + WIDTH, y + HEIGHT)){
            root.getMinecraft().getTextureManager().bindTexture(Ref.IMAGE_BUTTON_CLAIM_REWARD_PRESSED);
        }else{
            root.getMinecraft().getTextureManager().bindTexture(Ref.IMAGE_BUTTON_CLAIM_REWARD_UNPRESSED);
        }
        root.blit(stack, x, y, 0, 0, WIDTH, HEIGHT);


        new Label(
                root,
                text,
                x+(WIDTH/2) - Minecraft.getInstance().fontRenderer.getStringWidth(text) / 2,
                y+(HEIGHT/2)-Minecraft.getInstance().fontRenderer.FONT_HEIGHT/2,
                0xFFFFFF,
                false,
                false
        ).render(stack, player, mouseX, mouseY);
    }

    @Override
    public void onClick(PlayerEntity player, double mouseX, double mouseY) {
        if(Utils.isMouseInBounds(mouseX, mouseY, x, y, x+WIDTH, y+HEIGHT)){

        }
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