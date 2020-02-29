package com.vincentmet.customquests.screens.elements.buttons;

import com.vincentmet.customquests.lib.MouseDirection;
import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.lib.Utils;
import com.vincentmet.customquests.quests.Quest;
import com.vincentmet.customquests.quests.QuestUserProgress;
import com.vincentmet.customquests.screens.ScreenQuestingDevice;
import com.vincentmet.customquests.screens.elements.IQuestingGuiElement;
import com.vincentmet.customquests.screens.questingdeveicesubscreens.SubScreenQuestDetails;
import com.vincentmet.customquests.screens.questingdeveicesubscreens.SubScreensQuestingDevice;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ButtonQuest implements IQuestingGuiElement {
    private Screen root;
    public static final int WIDTH = 32;
    public static final int HEIGHT = 32;
    private int x;
    private int y;
    private Quest quest;
    private String uuid;

    public ButtonQuest(Screen root, int posX, int posY, Quest quest, String uuid){
        this.root = root;
        this.x = posX;
        this.y = posY;
        this.quest = quest;
        this.uuid = uuid;
    }

    @Override
    public void update(PlayerEntity player, double mouseX, double mouseY, int width, int height) {

    }

    @Override
    public void render(PlayerEntity player, double mouseX, double mouseY) {
        if(Quest.isQuestCompletedForPlayer(uuid, quest.getId()) && !QuestUserProgress.isRewardClaimed(uuid, quest.getId())){
            root.getMinecraft().getTextureManager().bindTexture(Ref.IMAGE_BUTTON_HEXAGON_UNCLAIMED);
        }else if(Quest.isQuestCompletedForPlayer(uuid, quest.getId())){
            root.getMinecraft().getTextureManager().bindTexture(Ref.IMAGE_BUTTON_HEXAGON_COMPLETED);
        }else if(Quest.hasQuestUncompletedDependenciesForPlayer(uuid, quest.getId())){
            root.getMinecraft().getTextureManager().bindTexture(Ref.IMAGE_BUTTON_HEXAGON_DISABLED);
        }else{
            if(Utils.isMouseInBounds(mouseX, mouseY, x, y, x+WIDTH, y+HEIGHT)){
                root.getMinecraft().getTextureManager().bindTexture(Ref.IMAGE_BUTTON_HEXAGON_PRESSED);
            }else{
                root.getMinecraft().getTextureManager().bindTexture(Ref.IMAGE_BUTTON_HEXAGON_UNPRESSED);
            }
        }
        root.blit(x, y, 0, 0, WIDTH, HEIGHT);
        RenderHelper.enableGUIStandardItemLighting();
        root.getMinecraft().getItemRenderer().renderItemIntoGUI(new ItemStack(quest.getIcon()), x + 8, y + 7);

    }

    @Override
    public void onClick(PlayerEntity player, double mouseX, double mouseY) {
        if(!Quest.hasQuestUncompletedDependenciesForPlayer(uuid, quest.getId())){
            if(Utils.isMouseInBounds(mouseX, mouseY, x, y, x+WIDTH, y+HEIGHT)){
                SubScreenQuestDetails.setActiveQuest(quest.getId());
                ScreenQuestingDevice.setActiveScreen(SubScreensQuestingDevice.QUEST_DETAILS);
            }
        }
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
}
