package com.vincentmet.customquests.screens.elements.buttons;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.vincentmet.customquests.lib.*;
import com.vincentmet.customquests.quests.book.QuestLine;
import com.vincentmet.customquests.quests.quest.Quest;
import com.vincentmet.customquests.screens.elements.IQuestingGuiElement;
import com.vincentmet.customquests.screens.elements.labels.Label;
import com.vincentmet.customquests.screens.questingdeveicesubscreens.SubScreenQuestDetails;
import com.vincentmet.customquests.screens.questingdeveicesubscreens.questlines.QuestingWeb;
import java.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.*;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public class ButtonQuestline implements IQuestingGuiElement {
    private Screen root;
    public static final int WIDTH = 150;//todo make this variable, and stitch/blit button texture accordingly
    public static final int HEIGHT = 20;
    private int x;
    private int y;
    private QuestLine questLine;
    private Label label;

    public ButtonQuestline(Screen root, int x, int y, QuestLine questline){
        this.root = root;
        this.x = x;
        this.y = y;
        this.questLine = questline;
    }

    @Override
    public void update(PlayerEntity player, double mouseX, double mouseY, int width, int height) {
        reloadLabel();
        label.update(player, mouseX, mouseY, 0, 0);
    }

    @Override
    public void render(MatrixStack stack, PlayerEntity player, double mouseX, double mouseY) {
        if(QuestLine.isQuestlineUnlocked(Utils.simplifyUUID(player.getUniqueID()), questLine.getId())){
            if(Utils.isMouseInBounds(mouseX, mouseY, x, y, x + WIDTH, y + HEIGHT)){
                Minecraft.getInstance().getTextureManager().bindTexture(Ref.IMAGE_BUTTON_QUESTLINE_PRESSED);
            }else{
                Minecraft.getInstance().getTextureManager().bindTexture(Ref.IMAGE_BUTTON_QUESTLINE_UNPRESSED);
            }
        }else{
            Minecraft.getInstance().getTextureManager().bindTexture(Ref.IMAGE_BUTTON_QUESTLINE_DISABLED);
        }
        root.blit(stack, x, y, 0, 0, WIDTH, HEIGHT);
        reloadLabel();
        label.render(stack, player, mouseX, mouseY);

        if(!QuestLine.isQuestlineUnlocked(Utils.simplifyUUID(player.getUniqueID()), questLine.getId()) && Utils.isMouseInBounds(mouseX, mouseY, x, y, x + WIDTH, y + HEIGHT)){
            List<Integer> missingIds = new ArrayList<>();
            questLine.getQuests().forEach(questId->{
                missingIds.addAll(Quest.getQuestFromId(questId).getDependencies());
            });

            GL11.glPushMatrix();
            root.renderTooltip(stack, new StringTextComponent("Complete the quest(s): " + missingIds.toString().replace("[", "\'").replace("]", "\'") + " to unlock this questline!"), (int)mouseX, (int)mouseY);
            GL11.glPopMatrix();
            RenderHelper.disableStandardItemLighting();
        }
    }

    @Override
    public void onClick(PlayerEntity player, double mouseX, double mouseY) {
        if(Utils.isMouseInBounds(mouseX, mouseY, x, y, x+WIDTH, y+HEIGHT)){
            QuestingWeb.setActiveQuestline(questLine.getId());
            SubScreenQuestDetails.setActiveQuest(-1);
        }
        label.onClick(player, mouseX, mouseY);
    }

    @Override
    public void onKeyPress(int key, int mod) {

    }

    @Override
    public void onMouseScroll(double mouseX, double mouseY, MouseDirection direction) {

    }

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    public void reloadLabel(){
        this.label = new Label(
                root,
                questLine.getTitle(),
                x + (WIDTH/2),
                y+(HEIGHT/2),
                0xFFFFFF,
                true,
                true
        );
    }
}