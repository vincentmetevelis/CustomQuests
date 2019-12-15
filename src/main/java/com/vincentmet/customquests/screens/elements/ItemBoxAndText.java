package com.vincentmet.customquests.screens.elements;

import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.screens.ScreenQuestingDevice;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ItemBoxAndText implements IQuestingGuiElement {
    private int x;
    private int y;
    private ItemStack itemstack;
    private String text;
    private int color;
    private boolean centered;

    public ItemBoxAndText(int posX, int posY, ItemStack itemstack, String text, int color, boolean xCentered){
        this.x = posX;
        this.y = posY;
        this.itemstack = itemstack;
        this.text = text;
        this.color = color;
        this.centered = xCentered;
    }

    @Override
    public <T extends ScreenQuestingDevice> void render(T gui, PlayerEntity player, double mouseX, double mouseY) {
        if(centered){
            int itembox_width = 18;
            int margin = 2;
            int text_width = Ref.FONT_RENDERER.getStringWidth(text);
            int half_total_width = (itembox_width + margin + text_width) / 2;
            new ItemBox(x - half_total_width, y, itemstack).render(gui, player, mouseX, mouseY);
            new Label(x - half_total_width + itembox_width + margin, y + (18/2 - 8/2), text, color, false, false).render(gui, player, mouseX, mouseY);
        }else{
            new ItemBox(x, y, itemstack).render(gui, player, mouseX, mouseY);
            new Label(x + 20, y + (18/2 - 8/2), text, color, false, false).render(gui, player, mouseX, mouseY);
        }
    }

    @Override
    public <T extends ScreenQuestingDevice> void onClick(T gui, PlayerEntity player, double mouseX, double mouseY) {

    }
}
