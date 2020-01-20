package com.vincentmet.customquests.lib;

import net.minecraft.client.gui.AbstractGui;
import org.lwjgl.glfw.GLFW;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class TextField {
    private static final int BORDER = 1;
    private static final int MARGIN_CURSOR_TOP = 2;
    private static final int MARGIN_CURSOR_LEFT = 2;

    private int x;
    private int y;
    private int width;
    private int height;
    private List<String> text = new ArrayList<>();
    private int focusId;
    private Vec2i cursorPos = new Vec2i(0, 0);
    private int colorBorder = 0xFF888888;
    private int colorInside = 0xFF000000;
    private Supplier<Boolean> isFocussed = ()->this.focusId == Ref.currentFocussedTextfield;

    public TextField(int x, int y, int width, int heightInLines, int colorInside, int colorBorder, List<String> initialText, int focusId){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = heightInLines * Ref.FONT_RENDERER.FONT_HEIGHT + 5;
        this.colorInside = colorInside;
        this.colorBorder = colorBorder;
        if(initialText.isEmpty()){
            initialText.add("");
        }else{
            this.text = initialText;
        }
        this.focusId = focusId;
    }

    public TextField(int x, int y, int width, int heightInLines, List<String> initialText, int focusId){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = heightInLines * Ref.FONT_RENDERER.FONT_HEIGHT + 5;
        if(initialText.isEmpty()){
            initialText.add("");
        }else{
            this.text = initialText;
        }
        this.focusId = focusId;
    }

    public void render(){
        AbstractGui.fill(x, y, x+width + BORDER, y+height + BORDER, colorBorder);
        AbstractGui.fill(x + BORDER, y + BORDER, x+width, y+height, colorInside);

        for(int i=0;i<text.size();i++){
            Ref.FONT_RENDERER.drawString(text.get(i), x+MARGIN_CURSOR_LEFT+1, y+MARGIN_CURSOR_TOP+2 + Ref.FONT_RENDERER.FONT_HEIGHT*i, 0xFFFFFF);
        }
        /*if(this.isFocussed.test()){*/
        if(cursorPos != null){
            for(int i=0;i<text.size();i++){
                if(i==cursorPos.getY()){
                    int xPosInPx = x + MARGIN_CURSOR_LEFT + Ref.FONT_RENDERER.getStringWidth(getFirstNCharsOfString(text.get(i), cursorPos.getX()));
                    if(System.currentTimeMillis()%1000>=500){
                        AbstractGui.fill(xPosInPx, y + MARGIN_CURSOR_TOP + i*Ref.FONT_RENDERER.FONT_HEIGHT, xPosInPx + 1, y + 2*MARGIN_CURSOR_TOP + i*Ref.FONT_RENDERER.FONT_HEIGHT + Ref.FONT_RENDERER.FONT_HEIGHT, 0xFFAAAAAA);
                    }
                }
            }
        }
        /*}*/
    }

    public void onClick(double mouseX, double mouseY){

    }

    public void onKeyPress(int key, int mod){
        if(key == GLFW.GLFW_KEY_LEFT){
            if(cursorPos.getX() - 1 >= 0 && cursorPos.getX()-1 < text.get(cursorPos.getY()).length()){
                this.cursorPos.add(-1, 0);
            }
        }
        if(key == GLFW.GLFW_KEY_RIGHT){
            if(cursorPos.getX() + 1 >= 0 && cursorPos.getX() < text.get(cursorPos.getY()).length()){
                this.cursorPos.add(1, 0);
            }
        }
        if(key == GLFW.GLFW_KEY_UP){
            if(cursorPos.getY() - 1 >=0 && cursorPos.getY()-1 < text.size()){
                if(text.get(cursorPos.getY()-1).length() >= cursorPos.getX()){
                    this.cursorPos.add(0, -1);
                }
            }
        }
        if(key == GLFW.GLFW_KEY_DOWN){
            if(cursorPos.getY() + 1 >= 0 && cursorPos.getY()+1 < text.size()){
                if(text.get(cursorPos.getY()+1).length() >= cursorPos.getX()){
                    this.cursorPos.add(0, 1);
                }
            }
        }
    }

    public static String getFirstNCharsOfString(String text, int n){
        String[] chars = text.split("");
        StringBuilder newString = new StringBuilder();
        for(int j=0;j<n;j++) newString.append(chars[j]);
        return newString.toString();
    }
}