package com.vincentmet.customquests.screens.elements.labels;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.vincentmet.customquests.lib.*;
import com.vincentmet.customquests.screens.elements.IQuestingGuiElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.*;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public class ItemBox implements IQuestingGuiElement {
    private Screen root;
    public static final int WIDTH = 18;
    public static final int HEIGHT = 18;
    private int x;
    private int y;
    private ItemStack itemstack;

    public ItemBox(Screen root, int posX, int posY, ItemStack itemstack){
        this.root = root;
        this.x = posX;
        this.y = posY;
        this.itemstack = itemstack;
    }

    @Override
    public void update(PlayerEntity player, double mouseX, double mouseY, int width, int height) {

    }

    @Override
    public void render(MatrixStack stack, PlayerEntity player, double mouseX, double mouseY) {
        root.getMinecraft().getTextureManager().bindTexture(Ref.IMAGE_ITEMBOX_BACKGROUND);
        root.blit(stack, x, y, 0, 0, WIDTH, HEIGHT);
        GL11.glPushMatrix();
        RenderHelper.enableStandardItemLighting();
        Minecraft.getInstance().getItemRenderer().renderItemIntoGUI(itemstack, x+1, y+1);
        RenderHelper.disableStandardItemLighting();
        GL11.glPopMatrix();
    }

    @Override
    public void onClick(PlayerEntity player, double mouseX, double mouseY) {
        //todo JEI support here
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