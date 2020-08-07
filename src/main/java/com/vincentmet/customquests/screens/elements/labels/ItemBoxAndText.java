package com.vincentmet.customquests.screens.elements.labels;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.vincentmet.customquests.lib.MouseDirection;
import com.vincentmet.customquests.screens.elements.IQuestingGuiElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.*;

@OnlyIn(Dist.CLIENT)
public class ItemBoxAndText implements IQuestingGuiElement {
    private Screen root;
    private int x;
    private int y;
    private ItemStack itemstack;
    private String text;
    private int color;
    private boolean centered;

    public ItemBoxAndText(Screen root, int posX, int posY, ItemStack itemstack, String text, int color, boolean xCentered){
        this.root = root;
        this.x = posX;
        this.y = posY;
        this.itemstack = itemstack;
        this.text = text;
        this.color = color;
        this.centered = xCentered;
    }

    @Override
    public void update(PlayerEntity player, double mouseX, double mouseY, int width, int height) {

    }

    @Override
    public void render(MatrixStack stack, PlayerEntity player, double mouseX, double mouseY) {
        if(centered){
            int itembox_width = 18;
            int margin = 2;
            int text_width = Minecraft.getInstance().fontRenderer.getStringWidth(text);
            int half_total_width = (itembox_width + margin + text_width) / 2;
            new ItemBox(root, x - half_total_width, y, itemstack).render(stack, player, mouseX, mouseY);
            new Label(root, text, x - half_total_width + itembox_width + margin, y + (18/2 - 8/2), color, false, false).render(stack, player, mouseX, mouseY);
        }else{
            new ItemBox(root, x, y, itemstack).render(stack, player, mouseX, mouseY);
            new Label(root, text, x + 20, y + (18/2 - 8/2), color, false, false).render(stack, player, mouseX, mouseY);
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