package com.vincentmet.customquests.screens.elements.labels;

import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.screens.ScreenQuestingDevice;
import com.vincentmet.customquests.screens.elements.IQuestingGuiElement;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class MultilineLabel implements IQuestingGuiElement {
    private Screen root;
    private int x;
    private int y;
    private String text;
    private int color;
    private int maxWidth;

    public MultilineLabel(Screen root, int posX, int posY, String text, int color, int maxWidth){
        this.root = root;
        this.x = posX;
        this.y = posY;
        this.text = text;
        this.color = color;this.maxWidth = maxWidth;
    }

    @Override
    public void update(IQuestingGuiElement gui, PlayerEntity player, double mouseX, double mouseY, int width, int height) {

    }

    @Override
    public void render(IQuestingGuiElement gui, PlayerEntity player, double mouseX, double mouseY) {
        List<String> lines = new ArrayList<>();
        List<String> spaceSplitText = Arrays.asList(text.split(" "));

        String lastLine = "";
        boolean isFirstWord = true;
        for(String word : spaceSplitText){
            if(Ref.FONT_RENDERER.getStringWidth(lastLine + " " + word) <= maxWidth){
                if(isFirstWord){
                    lastLine += word;
                    isFirstWord = false;
                }else{
                    lastLine += (" " + word);
                }
            }else{
                lines.add(lastLine);
                lastLine = word;
            }
        }
        lines.add(lastLine);

        int currentHeight = y;
        for(String line : lines){
            root.drawString(Ref.FONT_RENDERER, line, x, currentHeight, color);
            currentHeight += Ref.FONT_RENDERER.FONT_HEIGHT;
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