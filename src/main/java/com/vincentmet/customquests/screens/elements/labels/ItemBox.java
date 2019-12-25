package com.vincentmet.customquests.screens.elements.labels;

import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.screens.ScreenQuestingDevice;
import com.vincentmet.customquests.screens.elements.IQuestingGuiElement;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ItemBox implements IQuestingGuiElement {
    public static final int WIDTH = 18;
    public static final int HEIGHT = 18;
    private int x;
    private int y;
    private ItemStack itemstack;

    public ItemBox(int posX, int posY, ItemStack itemstack){
        this.x = posX;
        this.y = posY;
        this.itemstack = itemstack;
    }

    @Override
    public <T extends ScreenQuestingDevice> void render(T gui, PlayerEntity player, double mouseX, double mouseY) {
        gui.getMinecraft().getTextureManager().bindTexture(Ref.IMAGE_ITEMBOX_BACKGROUND);
        gui.blit(x, y, 0, 0, WIDTH, HEIGHT);
        Minecraft.getInstance().getItemRenderer().renderItemIntoGUI(itemstack, x+1, y+1);
    }

    @Override
    public <T extends ScreenQuestingDevice> void onClick(T gui, PlayerEntity player, double mouseX, double mouseY) {
        //todo JEI support here
    }
}
