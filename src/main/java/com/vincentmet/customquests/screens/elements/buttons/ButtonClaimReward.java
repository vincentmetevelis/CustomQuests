package com.vincentmet.customquests.screens.elements.buttons;

import com.vincentmet.customquests.lib.MouseDirection;
import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.lib.Utils;
import com.vincentmet.customquests.lib.handlers.PacketHandler;
import com.vincentmet.customquests.network.packets.MessageRewardButtonPressClientToServer;
import com.vincentmet.customquests.quests.QuestUserProgress;
import com.vincentmet.customquests.screens.elements.IQuestingGuiElement;
import com.vincentmet.customquests.screens.elements.labels.Label;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ButtonClaimReward implements IQuestingGuiElement {
    private Screen root;
    public static final int WIDTH = 64;
    public static final int HEIGHT = 20;
    private int x;
    private int y;
    private int quest;
    private String textUnclaimed = "Claim";
    private String textClaimed = "Claimed!";

    public ButtonClaimReward(Screen root, int posX, int posY, int quest){
        this.root = root;
        this.x = posX;
        this.y = posY;
        this.quest = quest;
    }

    @Override
    public void update(PlayerEntity player, double mouseX, double mouseY, int width, int height) {

    }

    @Override
    public void render(PlayerEntity player, double mouseX, double mouseY) {
        String text = QuestUserProgress.isRewardClaimed(Utils.simplifyUUID(player.getUniqueID()), quest) ? textClaimed : textUnclaimed;
        if(!QuestUserProgress.isRewardClaimed(Utils.simplifyUUID(player.getUniqueID()), quest) && QuestUserProgress.areAllRequirementsCompleted(Utils.simplifyUUID(player.getUniqueID()), quest)){
            if(Utils.isMouseInBounds(mouseX, mouseY, x, y, x + WIDTH, y + HEIGHT)){
                root.getMinecraft().getTextureManager().bindTexture(Ref.IMAGE_BUTTON_CLAIM_REWARD_PRESSED);
            }else{
                root.getMinecraft().getTextureManager().bindTexture(Ref.IMAGE_BUTTON_CLAIM_REWARD_UNPRESSED);
            }
        }else{
            root.getMinecraft().getTextureManager().bindTexture(Ref.IMAGE_BUTTON_CLAIM_REWARD_DISABLED);
        }
        root.blit(x, y, 0, 0, WIDTH, HEIGHT);


        new Label(
                root,
                text,
                x+(WIDTH/2)-Ref.FONT_RENDERER.getStringWidth(text)/2,
                y+(HEIGHT/2)-Ref.FONT_RENDERER.FONT_HEIGHT/2,
                0xFFFFFF,
                false,
                false
        ).render(player, mouseX, mouseY);
    }

    @Override
    public void onClick(PlayerEntity player, double mouseX, double mouseY) {
        if(!QuestUserProgress.isRewardClaimed(Utils.simplifyUUID(player.getUniqueID()), quest) && QuestUserProgress.areAllRequirementsCompleted(Utils.simplifyUUID(player.getUniqueID()), quest)){
            if(Utils.isMouseInBounds(mouseX, mouseY, x, y, x+WIDTH, y+HEIGHT)){
                if(!QuestUserProgress.isRewardClaimed(Utils.simplifyUUID(player.getUniqueID()), quest) && QuestUserProgress.areAllRequirementsCompleted(Utils.simplifyUUID(player.getUniqueID()), quest)){
                    PacketHandler.CHANNEL.sendToServer(new MessageRewardButtonPressClientToServer(quest));
                }
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