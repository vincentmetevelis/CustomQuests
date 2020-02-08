package com.vincentmet.customquests.screens.elements;

import com.mojang.datafixers.util.Pair;
import com.vincentmet.customquests.lib.*;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.player.PlayerEntity;
import org.lwjgl.glfw.GLFW;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class TextField implements IQuestingGuiElement{
    private static final int BORDER = 1;
    private static final int MARGIN_CURSOR_TOP = 2;
    private static final int MARGIN_CURSOR_LEFT = 2;

    private int x;
    private int y;
    private int width;
    private int height;
    private int heightInLines;
    private List<List<Pair<Character, Integer>>> text = new ArrayList<>(); // first list for each line, second list for each character, pair for character, and int for corresponding font char width
    private int focusId;
    private Vec2i cursorPos = new Vec2i(0, 0);
    private int colorBorder = 0xFF888888;
    private int colorInside = 0xFF000000;
    private Supplier<Boolean> isFocussed = ()->this.focusId == Ref.currentFocussedTextfield;

    public TextField(int x, int y, int width, int heightInLines, int colorInside, int colorBorder, List<List<Pair<Character, Integer>>> initialText, int focusId){
        this.x = x;
        this.y = y;
        this.width = width;
        this.heightInLines = heightInLines;
        this.height = heightInLines * Ref.FONT_RENDERER.FONT_HEIGHT + 5;
        this.colorInside = colorInside;
        this.colorBorder = colorBorder;
        if(initialText.isEmpty()){
            initialText.add(new ArrayList<>());
        }else{
            this.text = initialText;
        }
        this.focusId = focusId;
    }

    public TextField(int x, int y, int width, int heightInLines, List<List<Pair<Character, Integer>>> initialText, int focusId){
        this.x = x;
        this.y = y;
        this.width = width;
        this.heightInLines = heightInLines;
        this.height = heightInLines * Ref.FONT_RENDERER.FONT_HEIGHT + 5;
        if(initialText.isEmpty()){
            initialText.add(new ArrayList<>());
        }else{
            this.text = initialText;
        }
        this.focusId = focusId;
    }

    public void update(PlayerEntity player, double mouseX, double mouseY, int x, int y){
        this.x = x;
        this.y = y;
    }

    public void render(PlayerEntity player, double mouseX, double mouseY){
        AbstractGui.fill(x, y, x+width + BORDER, y+height + BORDER, colorBorder);
        AbstractGui.fill(x + BORDER, y + BORDER, x+width, y+height, colorInside);

        for(int i=0;i<text.size();i++){
            char lastC = '\u0000';//NULL
            int xPosInPx = x + MARGIN_CURSOR_LEFT + 1;
            for(Pair<Character, Integer> pair : this.text.get(i)){
                char character = pair.getFirst();
                int charLength = pair.getSecond();
                if(character != '\u00a7' && lastC != '\u00a7'){//§
                    Ref.FONT_RENDERER.drawString(String.copyValueOf(new char[]{character}), xPosInPx, y+MARGIN_CURSOR_TOP+2 + Ref.FONT_RENDERER.FONT_HEIGHT*i, 0xFFFFFF);
                    xPosInPx+=charLength;
                }
                lastC = character;
            }
        }
        //todo check for colorify thins here somewhere
        if(this.isFocussed.get()){
            if(cursorPos != null){
                final CharContainer lastC = new CharContainer('\u0000');//NULL
                IntCounter accumulatedOffsetCounter = new IntCounter();
                IntCounter tempCursorPos = new IntCounter();
                text.get(cursorPos.getY()).forEach(characterIntegerPair -> {
                    char character = characterIntegerPair.getFirst();
                    int charLength = characterIntegerPair.getSecond();
                    if(character != '\u00a7' && lastC.getChar() != '\u00a7') {//§
                        if(tempCursorPos.getValue()<cursorPos.getX()){
                            accumulatedOffsetCounter.add(charLength);
                        }
                    }
                    tempCursorPos.count();
                    lastC.setChar(character);
                });
                int xPosInPx = x + MARGIN_CURSOR_LEFT + accumulatedOffsetCounter.getValue();
                if(System.currentTimeMillis()%1000>=500){
                    AbstractGui.fill(xPosInPx, y + MARGIN_CURSOR_TOP + cursorPos.getY()*Ref.FONT_RENDERER.FONT_HEIGHT, xPosInPx + 1, y + 2*MARGIN_CURSOR_TOP + cursorPos.getY()*Ref.FONT_RENDERER.FONT_HEIGHT + Ref.FONT_RENDERER.FONT_HEIGHT, 0xFFAAAAAA);
                }
            }
        }
    }

    public void onClick(PlayerEntity player, double mouseX, double mouseY){
        if(Utils.isMouseInBounds(mouseX, mouseY, x, y, x+width, y+height)){
            Ref.currentFocussedTextfield = this.focusId;
            double localMouseX = mouseX - x;
            double localMouseY = mouseY - y;
            int whichLine = (int)Math.floor((localMouseY - MARGIN_CURSOR_TOP) / Ref.FONT_RENDERER.FONT_HEIGHT);
            if(whichLine>=this.heightInLines){
                whichLine = heightInLines-1;
            }
            cursorPos.setY(whichLine);

            IntCounter tempOffsetCount = new IntCounter();
            IntCounter tempCursorPosCount = new IntCounter();
            this.text.get(cursorPos.getY()).forEach(characterIntegerPair -> {
                if(tempOffsetCount.getValue()<=localMouseX){
                    tempCursorPosCount.count();
                }
                tempOffsetCount.add(characterIntegerPair.getSecond());
            });
            if(tempOffsetCount.getValue() > localMouseX){
                cursorPos.setX(tempCursorPosCount.getValue()-1);
            }else{
                this.cursorPos.setX(tempCursorPosCount.getValue());
            }
        }
    }

    public void onKeyPress(int key, int mod){
        if(this.isFocussed.get()){
            if(key == GLFW.GLFW_KEY_LEFT){
                if(cursorPos.getX() - 1 >= 0 && cursorPos.getX()-1 < text.get(cursorPos.getY()).size()){
                    if(this.text.get(cursorPos.getY()).get(cursorPos.getX()-2).getFirst() != '\u00a7'){
                        this.cursorPos.add(-1, 0);
                    }else{
                        this.cursorPos.add(-3, 0);
                    }
                }
            }
            if(key == GLFW.GLFW_KEY_RIGHT){
                if(cursorPos.getX() + 1 >= 0 && cursorPos.getX() < text.get(cursorPos.getY()).size()){
                    if(this.text.get(cursorPos.getY()).get(cursorPos.getX()+1).getFirst() != '\u00a7'){
                        this.cursorPos.add(1, 0);
                    }else{
                        this.cursorPos.add(3, 0);
                    }
                }
            }
            if(key == GLFW.GLFW_KEY_UP){
                if(cursorPos.getY() - 1 >=0 && cursorPos.getY()-1 < text.size()){
                    if(text.get(cursorPos.getY()-1).size() >= cursorPos.getX()){
                        this.cursorPos.add(0, -1);
                    }
                }/*else if(cursorPos.getX() - 1 >= 0){ // FIXME
                    this.cursorPos.add(-1, 0).setX(text.get(cursorPos.getY()).size());
                }*/
            }
            if(key == GLFW.GLFW_KEY_DOWN){
                if(cursorPos.getY() + 1 >= 0 && cursorPos.getY()+1 < text.size()){
                    if(text.get(cursorPos.getY()+1).size() >= cursorPos.getX()){
                        this.cursorPos.add(0, 1);
                    }
                }
            }
            if(key == GLFW.GLFW_KEY_BACKSPACE){
                if(cursorPos.getX()>0){
                    this.cursorPos.sub(1, 0);
                    this.text.get(cursorPos.getY()).remove(cursorPos.getX());
                }
            }
            if(key == GLFW.GLFW_KEY_DELETE){
                if(cursorPos.getX()<this.text.get(cursorPos.getY()).size()){
                    this.text.get(cursorPos.getY()).remove(cursorPos.getX());
                }
            }
            if(key == GLFW.GLFW_KEY_ENTER || key == GLFW.GLFW_KEY_KP_ENTER){
                List<Pair<Character, Integer>> txtForNewLine = new ArrayList<>();
                IntCounter counterNewline = new IntCounter();
                text.get(cursorPos.getY()).forEach(characterIntegerPair -> {
                    if(counterNewline.getValue()>=cursorPos.getX()){
                        txtForNewLine.add(characterIntegerPair);
                    }
                    counterNewline.count();
                });
                this.text.add(cursorPos.getY() + 1, txtForNewLine);

                List<Pair<Character, Integer>> txtForOldLine = new ArrayList<>();
                IntCounter counterOldline = new IntCounter();
                text.get(cursorPos.getY()).forEach(characterIntegerPair -> {
                    if(counterOldline.getValue()<cursorPos.getX()){
                        txtForOldLine.add(characterIntegerPair);
                    }
                    counterOldline.count();
                });
                this.text.set(cursorPos.getY(), txtForOldLine);
                this.cursorPos.set(0, cursorPos.getY() + 1);
            }
            if(Ref.KEY_MAPPINGS.keySet().contains(key)){
                if((mod & 0x0001) == 0x0001){
                    insertCharAtPos(Ref.KEY_MAPPINGS.get(key).getSecond());//Uppercase
                }else{
                    insertCharAtPos(Ref.KEY_MAPPINGS.get(key).getFirst());//Lowercase
                }
            }
        }
    }

    @Override
    public void onMouseScroll(double mouseX, double mouseY, MouseDirection direction) {

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
        return false;
    }

    private void insertCharAtPos(char c){
        Vec2i cursorPos = this.cursorPos;
        this.text.get(cursorPos.getY()).add(cursorPos.getX(), new Pair<>(c, Ref.FONT_RENDERER.getStringWidth(String.copyValueOf(new char[]{c}))));
        this.cursorPos.add(1, 0);
    }

    public List<List<Pair<Character, Integer>>> getText() {
        return text;
    }

    public static List<Pair<Character, Integer>> stringToCharIntList(String line){
        List<Pair<Character, Integer>> result = new ArrayList<>();
        for(char c : line.toCharArray()){
            result.add(new Pair<>(c, Ref.FONT_RENDERER.getStringWidth(String.copyValueOf(new char[]{c}))));
        }
        return result;
    }
}