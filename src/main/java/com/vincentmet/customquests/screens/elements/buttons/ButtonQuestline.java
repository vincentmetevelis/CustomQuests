package com.vincentmet.customquests.screens.elements.buttons;

import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.lib.Utils;
import com.vincentmet.customquests.quests.QuestLine;
import com.vincentmet.customquests.screens.ScreenQuestingDevice;
import com.vincentmet.customquests.screens.elements.IQuestingGuiElement;
import com.vincentmet.customquests.screens.elements.labels.Label;
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

    public ButtonQuestline(Screen root, int posX, int posY, QuestLine questline){
        this.root = root;
        this.x = posX;
        this.y = posY;
        this.questLine = questline;
    }

    @Override
    public void update(IQuestingGuiElement parent, PlayerEntity player, double mouseX, double mouseY, int width, int height) {

    }

    @Override
    public void render(IQuestingGuiElement parent, PlayerEntity player, double mouseX, double mouseY) {
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


        new Label(
                root,
                x+(WIDTH/2)-Ref.FONT_RENDERER.getStringWidth(questLine.getTitle())/2,
                y+(HEIGHT/2)-Ref.FONT_RENDERER.FONT_HEIGHT/2,
                questLine.getTitle(),
                0xFFFFFF,
                false,
                false
        ).render(this, player, mouseX, mouseY);
    }

    @Override
    public void onClick(IQuestingGuiElement parent, PlayerEntity player, double mouseX, double mouseY) {
        if(Utils.isMouseInBounds(mouseX, mouseY, x, y, x+WIDTH, y+HEIGHT)){
            ScreenQuestingDevice.activeQuestline = questLine.getId();
            ScreenQuestingDevice.activeQuest = -1;//todo move these vars the subscreen, instead of Screen
        }
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