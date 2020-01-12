package com.vincentmet.customquests.screens.elements.buttons;

import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.lib.Utils;
import com.vincentmet.customquests.screens.ScreenQuestingDevice;
import com.vincentmet.customquests.screens.SubScreensQuestingDevice;
import com.vincentmet.customquests.screens.elements.IQuestingGuiElement;
import com.vincentmet.customquests.screens.questingdeveicesubscreens.SubScreenQuestDetails;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ButtonHome implements IQuestingGuiElement {
    private Screen root;
    private static int x = 0;
    private static int y = 0;
    public static final int WIDTH = 20;
    public static final int HEIGHT = 20;

    public ButtonHome(Screen root){
        this.root = root;
    }

    @Override
    public void update(PlayerEntity player, double mouseX, double mouseY, int width, int height) {
    }

    @Override
    public void render(PlayerEntity player, double mouseX, double mouseY) {
        if(Utils.isMouseInBounds(mouseX, mouseY, x, y, x+WIDTH, y+HEIGHT)){
            root.getMinecraft().getTextureManager().bindTexture(Ref.IMAGE_BUTTON_SQUARE_PRESSED);
        }else{
            root.getMinecraft().getTextureManager().bindTexture(Ref.IMAGE_BUTTON_SQUARE_UNPRESSED);
        }
        root.blit(x, y, 0, 0, WIDTH, HEIGHT);
        root.getMinecraft().getTextureManager().bindTexture(Ref.IMAGE_ICON_HOME);
        root.blit(x, y, 0, 0, WIDTH, HEIGHT);
    }

    @Override
    public void onClick(PlayerEntity player, double mouseX, double mouseY) {
        if(Utils.isMouseInBounds(mouseX, mouseY, x, y, x+WIDTH, y+HEIGHT)){
            SubScreenQuestDetails.setActiveQuest(-1);
            ScreenQuestingDevice.setActiveScreen(SubScreensQuestingDevice.MAIN_MENU);
        }
    }

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }

    @Override
    public boolean isVisible() {
        return true;
    }
}
