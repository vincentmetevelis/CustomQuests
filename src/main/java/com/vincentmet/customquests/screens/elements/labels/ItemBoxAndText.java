package com.vincentmet.customquests.screens.elements.labels;

import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.screens.ScreenQuestingDevice;
import com.vincentmet.customquests.screens.elements.IQuestingGuiElement;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

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
    public void update(IQuestingGuiElement gui, PlayerEntity player, double mouseX, double mouseY, int width, int height) {

    }

    @Override
    public void render(IQuestingGuiElement gui, PlayerEntity player, double mouseX, double mouseY) {
        if(centered){
            int itembox_width = 18;
            int margin = 2;
            int text_width = Ref.FONT_RENDERER.getStringWidth(text);
            int half_total_width = (itembox_width + margin + text_width) / 2;
            new ItemBox(root, x - half_total_width, y, itemstack).render(this, player, mouseX, mouseY);
            new Label(root, x - half_total_width + itembox_width + margin, y + (18/2 - 8/2), text, color, false, false).render(this, player, mouseX, mouseY);
        }else{
            new ItemBox(root, x, y, itemstack).render(this, player, mouseX, mouseY);
            new Label(root, x + 20, y + (18/2 - 8/2), text, color, false, false).render(this, player, mouseX, mouseY);
        }
    }

    @Override
    public void onClick(IQuestingGuiElement gui, PlayerEntity player, double mouseX, double mouseY) {

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