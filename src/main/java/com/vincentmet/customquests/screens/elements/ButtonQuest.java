package com.vincentmet.customquests.screens.elements;

import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.lib.Utils;
import com.vincentmet.customquests.quests.Quest;
import com.vincentmet.customquests.screens.ScreenQuestingDevice;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class ButtonQuest implements IQuestingGuiElement {
    public static final int WIDTH = 32;
    public static final int HEIGHT = 32;
    private int x;
    private int y;
    private Quest quest;
    private String uuid;

    public ButtonQuest(int posX, int posY, Quest quest, String uuid){
        this.x = posX;
        this.y = posY;
        this.quest = quest;
        this.uuid = uuid;
    }

    @Override
    public <T extends ScreenQuestingDevice> void render(T gui, double mouseX, double mouseY) {
        if(Quest.isQuestCompletedForPlayer(uuid, quest.getId())){
            gui.getMinecraft().getTextureManager().bindTexture(Ref.IMAGE_BUTTON_HEXAGON_COMPLETED);
        }else if(Quest.hasQuestUncompletedDependenciesForPlayer(uuid, quest.getId())){
            gui.getMinecraft().getTextureManager().bindTexture(Ref.IMAGE_BUTTON_HEXAGON_DISABLED);
        }else{
            if(Utils.isMouseInBounds(mouseX, mouseY, x, y, x+WIDTH, y+HEIGHT)){
                gui.getMinecraft().getTextureManager().bindTexture(Ref.IMAGE_BUTTON_HEXAGON_PRESSED);
            }else{
                gui.getMinecraft().getTextureManager().bindTexture(Ref.IMAGE_BUTTON_HEXAGON_UNPRESSED);
            }
        }
        gui.blit(x, y, 0, 0, WIDTH, HEIGHT);
        Minecraft.getInstance().getItemRenderer().renderItemIntoGUI(new ItemStack(quest.getIcon()), x + 8, y + 7);
    }

    @Override
    public <T extends ScreenQuestingDevice> void onClick(T gui, double mouseX, double mouseY) {
        if(!Quest.hasQuestUncompletedDependenciesForPlayer(uuid, quest.getId())){
            if(Utils.isMouseInBounds(mouseX, mouseY, x, y, x+WIDTH, y+HEIGHT)){
                ScreenQuestingDevice.activeQuest = quest.getId();
            }
        }
    }
}
