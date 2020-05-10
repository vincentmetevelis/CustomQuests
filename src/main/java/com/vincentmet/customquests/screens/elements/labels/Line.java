package com.vincentmet.customquests.screens.elements.labels;

import com.vincentmet.customquests.lib.LineColor;
import com.vincentmet.customquests.lib.MouseDirection;
import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.quests.quest.QuestPosition;
import com.vincentmet.customquests.screens.elements.IQuestingGuiElement;
import com.vincentmet.customquests.screens.elements.buttons.ButtonQuest;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public class Line implements IQuestingGuiElement {
    private Screen root;
    private int x;
    private int y;
    private double angle;
    private int length;
    private LineColor color;
    private int thickness;

    public Line(Screen root, int posX1, int posY1, int posX2, int posY2, LineColor color, int thickness){
        this.root = root;
        this.x = posX1;
        this.y = posY1;
        int dx = posX1 - posX2;
        int dy = posY1 - posY2;
        this.length = (int)Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
        this.angle = Math.toDegrees(Math.atan2(dy,dx))+180;
        this.color = color;
        this.thickness = thickness;
    }

    public Line(Screen root, int posX, int posY, double angle, int length, LineColor color, int thickness){
        this.root = root;
        this.x = posX;
        this.y = posY;
        this.angle = angle;
        this.length = length;
        this.color = color;
        this.thickness = thickness;
    }

    public Line(Screen root, QuestPosition quest, QuestPosition dependency, LineColor color, int thickness){
        this.root = root;
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
    public void update(PlayerEntity player, double mouseX, double mouseY, int width, int height) {

    }

    @Override
    public void render(PlayerEntity player, double mouseX, double mouseY) {
        GL11.glPushMatrix();
        GL11.glTranslatef(this.x, this.y, 0);
        GL11.glRotated(this.angle, 0, 0, 1);
        GL11.glTranslatef(-this.x, -this.y, 0);
        root.getMinecraft().getTextureManager().bindTexture(this.color.getResourceLocation());
        root.blit(this.x, this.y, 0, 0, this.length, this.thickness);
        GL11.glPopMatrix();
    }

    @Override
    public void onClick(PlayerEntity player, double mouseX, double mouseY) {

    }

    @Override
    public void onKeyPress(int key, int mod) {

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
        return true;
    }
}