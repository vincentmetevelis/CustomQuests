package com.vincentmet.customquests.screens.elements;

import com.vincentmet.customquests.lib.MouseDirection;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.List;

public class ScrollableList implements IQuestingGuiElement{
    private int offset = 0;
    private int maxScrollHeight = 0; // in entries, not px
    private int width = 0;
    private int x;
    private int y;
    private int heightUpButton = 20;
    private int heightDownButton = 20;
    private int sizePerItem = 30;
    private List<IQuestingGuiElement> entries = new ArrayList<>();

    public ScrollableList(int x, int y){

    }

    @Override
    public void update(PlayerEntity player, double mouseX, double mouseY, int width, int height) {

    }

    @Override
    public void render(PlayerEntity player, double mouseX, double mouseY) {

    }

    @Override
    public void onClick(PlayerEntity player, double mouseX, double mouseY) {

    }

    @Override
    public void onKeyPress(int key, int mod) {

    }

    @Override
    public void onMouseScroll(double mouseX, double mouseY, MouseDirection direction) {
        if(direction == MouseDirection.DOWN){
            if(offset + 1 <= maxScrollHeight){
                offset+=1;
            }
        }else{
            if(offset - 1 >= 0){
                offset-=1;
            }
        }
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return heightUpButton + entries.size()*sizePerItem + heightDownButton;
    }

    @Override
    public boolean isVisible() {
        return false;
    }
}
