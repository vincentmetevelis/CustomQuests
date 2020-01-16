package com.vincentmet.customquests.lib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ButtonDrawer {
    public static void draw(int x, int y, int width, int height, ButtonTexture tex) {
        ResourceLocation texture = tex.get();
        int texU = tex.getU();
        int texV = tex.getV();
        int texWidth = tex.getWidth();
        int texHeight = tex.getHeight();
        int texP = tex.getBorderSize(); // P for Padding

        Minecraft.getInstance().getTextureManager().bindTexture(texture);

        // blit -> x, y, u, v, width, height, texSizeX, texSizeY

        // Left Top corner
        AbstractGui.blit(x, y, texU, texV, texP, texP, texWidth, texHeight);

        // Left Bottom corner
        AbstractGui.blit(x, y + height - texP, texU, texHeight - texP, texP, texP, texWidth, texHeight);

        // Right Top corner
        AbstractGui.blit(x + width - texP, y, texWidth - texP, texV, texP, texP, texWidth, texHeight);

        // Right Bottom corner
        AbstractGui.blit(x + width - texP, y + height - texP, texWidth - texP, texHeight - texP, texP, texP, texWidth, texHeight);

        int innerWidth = texWidth - 2 * texP;
        int innerHeight = texHeight - 2 * texP;
        int right = x + width - texP;
        int bottom = y + height - texP;

        // Top and Bottom Edges
        for (int left = x + texP; left < right; left += innerWidth) {
            // Top
            AbstractGui.blit(left, y, texP, 0, Math.min(innerWidth, right - left), texP, texWidth, texHeight);
            // Bottom
            AbstractGui.blit(left, y + height - texP, texP, texHeight - texP, Math.min(innerWidth, right - left), texP, texWidth, texHeight);
        }

        // Left and Right Edges
        for (int top = y + texP; top < bottom; top += innerHeight) {
            // Left
            AbstractGui.blit(x, top, 0, texP, texP, Math.min(innerHeight, bottom - top), texWidth, texHeight);
            // Right
            AbstractGui.blit(x + width - texP, top, texWidth - texP, texP, texP, Math.min(innerHeight, bottom - top), texWidth, texHeight);
        }

        // Fill the Middle
        for (int left = x + texP; left < right; left += innerWidth) {
            for (int top = y + texP; top < bottom; top += innerHeight) {
                AbstractGui.blit(left, top, texP, texP, Math.min(innerWidth, right - left), Math.min(innerHeight, bottom - top), texWidth, texHeight);
            }
        }
    }

    public enum ButtonState {
        UNPRESSED(), PRESSED(), DISABLED()
    }

    public static class ButtonTexture {
        public static final ButtonTexture DEFAULT = new ButtonTexture(new ResourceLocation(Ref.MODID, "textures/gui/button_scalable.png"), 0, 0, 16, 16, 4);
        private final ResourceLocation texture;
        private final int u;
        private final int v;
        private final int width;
        private final int height;
        private final int borderSize;

        public ButtonTexture(ResourceLocation texture, int u, int v, int width, int height, int borderSize) {
            this.texture = texture;
            this.u = u;
            this.v = v;
            this.width = width;
            this.height = height;
            this.borderSize = borderSize;
        }

        public ResourceLocation get() {
            return this.texture;
        }

        public int getU() {
            return this.u;
        }

        public int getV() {
            return this.v;
        }

        public int getWidth() {
            return this.width;
        }

        public int getHeight() {
            return this.height;
        }

        public int getBorderSize() {
            return this.borderSize;
        }
    }
}