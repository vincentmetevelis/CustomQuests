package com.vincentmet.customquests.screens.elements.buttons;

import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.lib.Utils;
import com.vincentmet.customquests.quests.QuestLine;
import com.vincentmet.customquests.screens.ScreenQuestingDevice;
import com.vincentmet.customquests.screens.elements.IQuestingGuiElement;
import com.vincentmet.customquests.screens.elements.labels.Label;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ButtonQuestline implements IQuestingGuiElement {
    public static final int WIDTH = 150;
    public static final int HEIGHT = 20;
    private int x;
    private int y;
    private QuestLine questLine;

    public ButtonQuestline(int posX, int posY, QuestLine questline){
        this.x = posX;
        this.y = posY;
        this.questLine = questline;
    }

    @Override
    public <T extends ScreenQuestingDevice> void render(T gui, PlayerEntity player, double mouseX, double mouseY) {
        if(QuestLine.isQuestlineUnlocked(Utils.simplifyUUID(player.getUniqueID()), questLine.getId())){
            if(Utils.isMouseInBounds(mouseX, mouseY, x, y, x + WIDTH, y + HEIGHT)){
                gui.getMinecraft().getTextureManager().bindTexture(Ref.IMAGE_BUTTON_QUESTLINE_PRESSED);
            }else{
                gui.getMinecraft().getTextureManager().bindTexture(Ref.IMAGE_BUTTON_QUESTLINE_UNPRESSED);
            }
        }else{
            gui.getMinecraft().getTextureManager().bindTexture(Ref.IMAGE_BUTTON_QUESTLINE_DISABLED);
        }
        gui.blit(x, y, 0, 0, WIDTH, HEIGHT);


        new Label(
                x+(WIDTH/2)-Ref.FONT_RENDERER.getStringWidth(questLine.getTitle())/2,
                y+(HEIGHT/2)-Ref.FONT_RENDERER.FONT_HEIGHT/2,
                questLine.getTitle(),
                0xFFFFFF,
                false,
                false
        ).render(gui, player, mouseX, mouseY);
    }

    @Override
    public <T extends ScreenQuestingDevice> void onClick(T gui, PlayerEntity player, double mouseX, double mouseY) {
        if(Utils.isMouseInBounds(mouseX, mouseY, x, y, x+WIDTH, y+HEIGHT)){
            ScreenQuestingDevice.activeQuestline = questLine.getId();
            ScreenQuestingDevice.activeQuest = -1;
        }
    }
}
