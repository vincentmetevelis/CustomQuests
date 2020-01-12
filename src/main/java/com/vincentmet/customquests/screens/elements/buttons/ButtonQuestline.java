package com.vincentmet.customquests.screens.elements.buttons;

import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.lib.Utils;
import com.vincentmet.customquests.quests.QuestLine;
import com.vincentmet.customquests.screens.elements.IQuestingGuiElement;
import com.vincentmet.customquests.screens.elements.labels.Label;
import com.vincentmet.customquests.screens.questingdeveicesubscreens.SubScreenQuestDetails;
import com.vincentmet.customquests.screens.questingdeveicesubscreens.questlines.QuestingWeb;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

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
    public void render(PlayerEntity player, double mouseX, double mouseY) {
        if(QuestLine.isQuestlineUnlocked(Utils.simplifyUUID(player.getUniqueID()), questLine.getId())){
            if(Utils.isMouseInBounds(mouseX, mouseY, x, y, x + WIDTH, y + HEIGHT)){
                Minecraft.getInstance().getTextureManager().bindTexture(Ref.IMAGE_BUTTON_QUESTLINE_PRESSED);
            }else{
                Minecraft.getInstance().getTextureManager().bindTexture(Ref.IMAGE_BUTTON_QUESTLINE_UNPRESSED);
            }
        }else{
            Minecraft.getInstance().getTextureManager().bindTexture(Ref.IMAGE_BUTTON_QUESTLINE_DISABLED);
        }
        root.blit(x, y, 0, 0, WIDTH, HEIGHT);
        label.render(player, mouseX, mouseY);
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