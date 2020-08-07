package com.vincentmet.customquests.screens.elements.buttons;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.vincentmet.customquests.lib.*;
import com.vincentmet.customquests.lib.handlers.PacketHandler;
import com.vincentmet.customquests.network.packets.MessageRewardButtonPressClientToServer;
import com.vincentmet.customquests.quests.progress.ProgressHelper;
import com.vincentmet.customquests.screens.elements.IQuestingGuiElement;
import com.vincentmet.customquests.screens.elements.labels.Label;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.*;

@OnlyIn(Dist.CLIENT)
public class ButtonClaimReward implements IQuestingGuiElement {
    private Screen root;
    public static final int WIDTH = 64;
    public static final int HEIGHT = 20;
    private int x;
    private int y;
    private int quest;
    private String textUnclaimed = Utils.getFormattedText(".label.unclaimed");
    private String textClaimed = Utils.getFormattedText(".label.claimed");

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
    public void render(MatrixStack stack, PlayerEntity player, double mouseX, double mouseY) {
        String text = ProgressHelper.isRewardClaimed(Utils.simplifyUUID(player.getUniqueID()), quest) ? textClaimed : textUnclaimed;
        if(!ProgressHelper.isRewardClaimed(Utils.simplifyUUID(player.getUniqueID()), quest) && ProgressHelper.areAllRequirementsCompleted(Utils.simplifyUUID(player.getUniqueID()), quest)){
            if(Utils.isMouseInBounds(mouseX, mouseY, x, y, x + WIDTH, y + HEIGHT)){
                root.getMinecraft().getTextureManager().bindTexture(Ref.IMAGE_BUTTON_CLAIM_REWARD_PRESSED);
            }else{
                root.getMinecraft().getTextureManager().bindTexture(Ref.IMAGE_BUTTON_CLAIM_REWARD_UNPRESSED);
            }
        }else{
            root.getMinecraft().getTextureManager().bindTexture(Ref.IMAGE_BUTTON_CLAIM_REWARD_DISABLED);
        }
        root.blit(stack, x, y, 0, 0, WIDTH, HEIGHT);


        new Label(
                root,
                text,
                x+(WIDTH/2) - Minecraft.getInstance().fontRenderer.getStringWidth(text) / 2,
                y+(HEIGHT/2)-Minecraft.getInstance().fontRenderer.FONT_HEIGHT/2,
                0xFFFFFF,
                false,
                false
        ).render(stack, player, mouseX, mouseY);
    }

    @Override
    public void onClick(PlayerEntity player, double mouseX, double mouseY) {
        if(!ProgressHelper.isRewardClaimed(Utils.simplifyUUID(player.getUniqueID()), quest) && ProgressHelper.areAllRequirementsCompleted(Utils.simplifyUUID(player.getUniqueID()), quest)){
            if(Utils.isMouseInBounds(mouseX, mouseY, x, y, x+WIDTH, y+HEIGHT)){
                if(!ProgressHelper.isRewardClaimed(Utils.simplifyUUID(player.getUniqueID()), quest) && ProgressHelper.areAllRequirementsCompleted(Utils.simplifyUUID(player.getUniqueID()), quest)){
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