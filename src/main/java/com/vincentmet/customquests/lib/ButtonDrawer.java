package com.vincentmet.customquests.lib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ButtonDrawer {
    public static void draw(int x, int y, int width, int height, ButtonState state){
        Minecraft.getInstance().getTextureManager().bindTexture(Ref.IMAGE_BUTTON_MODULAR);
        //draw -> x, y, u, v, width, height, texSizeX, texSizeY
        AbstractGui.blit(x, y, 0, 0, 2, 2, 512, 512); // Left Top corner
        AbstractGui.blit(x, y + height - 2, 0, 5, 2, 2, 512, 512); // Left Bottom corner
        AbstractGui.blit(x + height - 2, y, 5, 0, 2, 2, 512, 512); // Right Top corner
        AbstractGui.blit(x + height - 2, y + height - 2, 5, 5, 2, 2, 512, 512); // Right Bottom corner

        for(int x1=2;x1<width-2;x1++){
            AbstractGui.blit(x + x1, y, 3, 0, 1, 2, 512, 512); // Top border
        }
        for(int x2=2;x2<width-2;x2++){
            AbstractGui.blit(x + x2, y+height-2, 3, 5, 1, 2, 512, 512); // Top border
        }
        for(int y1=2;y1<width-2;y1++){
            AbstractGui.blit(x, y + y1, 0, 3, 2, 1, 512, 512); // Top border
        }
        for(int y2=2;y2<width-2;y2++){
            AbstractGui.blit(x+width-2, y + y2, 5, 3, 2, 1, 512, 512); // Top border
        }

        AbstractGui.blit(x+2, y+2, 0, 256, width-4, height-4, 512, 512);
    }

    public enum ButtonState{
        UNPRESSED(),
        PRESSED(),
        DISABLED()
    }
}