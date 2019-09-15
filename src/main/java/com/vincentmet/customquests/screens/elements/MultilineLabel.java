package com.vincentmet.customquests.screens.elements;

import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.screens.ScreenQuestingDevice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MultilineLabel implements IQuestingGuiElement {
    private int x;
    private int y;
    private String text;
    private int color;
    private int maxWidth;

    public MultilineLabel(int posX, int posY, String text, int color, int maxWidth){
        this.x = posX;
        this.y = posY;
        this.text = text;
        this.color = color;this.maxWidth = maxWidth;
    }
    @Override
    public <T extends ScreenQuestingDevice> void render(T gui, double mouseX, double mouseY) {
        List<String> lines = new ArrayList<>();
        List<String> spaceSplittedText = Arrays.asList(text.split(" "));

        String lastLine = "";
        boolean isFirstWord = true;
        for(String word : spaceSplittedText){
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
            gui.drawString(Ref.FONT_RENDERER, line, x, currentHeight, color);
            currentHeight += Ref.FONT_RENDERER.FONT_HEIGHT;
        }
    }

    @Override
    public <T extends ScreenQuestingDevice> void onClick(T gui, double mouseX, double mouseY) {

    }
}
