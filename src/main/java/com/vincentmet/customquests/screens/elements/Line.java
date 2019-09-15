package com.vincentmet.customquests.screens.elements;

import com.vincentmet.customquests.lib.LineColor;
import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.lib.Utils;
import com.vincentmet.customquests.quests.Quest;
import com.vincentmet.customquests.quests.QuestPosition;
import com.vincentmet.customquests.screens.ScreenQuestingDevice;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class Line implements IQuestingGuiElement {
    private int x;
    private int y;
    private double angle;
    private int length;
    private LineColor color;
    private int thickness;

    public Line(int posX1, int posY1, int posX2, int posY2, LineColor color, int thickness){
        this.x = posX1;
        this.y = posY1;
        int dx = posX1 - posX2;
        int dy = posY1 - posY2;
        this.length = (int)Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
        this.angle = Math.toDegrees(Math.atan2(dy,dx))+180;
        this.color = color;
        this.thickness = thickness;
    }

    public Line(int posX, int posY, double angle, int length, LineColor color, int thickness){
        this.x = posX;
        this.y = posY;
        this.angle = angle;
        this.length = length;
        this.color = color;
        this.thickness = thickness;
    }

    public Line(QuestPosition quest, QuestPosition dependency, LineColor color, int thickness){
        int iconWidthCentered = (ButtonQuest.WIDTH / 2) - (int)Math.floor((float)thickness/2);
        int iconHeightCentered = (ButtonQuest.HEIGHT / 2) - (int)Math.floor((float)thickness/2);

        int dx = quest.getX() - dependency.getX();
        int dy = quest.getY() - dependency.getY();
        this.length = (int)Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
        this.angle = Math.toDegrees(Math.atan2(dy,dx))+180;
        this.color = color;
        this.thickness = thickness;

        this.x = Ref.GUI_QUESTING_MARGIN_LEFT + iconWidthCentered + quest.getX();
        this.y = Ref.GUI_QUESTING_MARGIN_TOP + iconHeightCentered + quest.getY();
    }

    @Override
    public <T extends ScreenQuestingDevice> void render(T gui, double mouseX, double mouseY) {
        GL11.glPushMatrix();
        GL11.glTranslatef(this.x, this.y, 0);
        GL11.glRotated(this.angle, 0, 0, 1);
        GL11.glTranslatef(-this.x, -this.y, 0);
        gui.getMinecraft().getTextureManager().bindTexture(this.color.getResourceLocation());
        gui.blit(this.x, this.y, 0, 0, this.length, this.thickness);
        GL11.glPopMatrix();
    }

    @Override
    public <T extends ScreenQuestingDevice> void onClick(T gui, double mouseX, double mouseY) {

    }
}
