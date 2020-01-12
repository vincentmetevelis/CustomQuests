package com.vincentmet.customquests.lib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ButtonDrawer {
    public static void draw(Screen gui, int x, int y, int width, int height, ButtonState state){
        Minecraft.getInstance().getTextureManager().bindTexture(Ref.IMAGE_BUTTON_MODULAR);
        gui.blit(x, y, 0, 0, width, height);
    }

    public enum ButtonState{
        UNPRESSED(),
        PRESSED(),
        DISABLED()
    }
}