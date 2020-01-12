package com.vincentmet.customquests.screens.elements.buttons;

import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.lib.Utils;
import com.vincentmet.customquests.lib.handlers.PacketHandler;
import com.vincentmet.customquests.network.packets.MessageHandInButtonPressClientToServer;
import com.vincentmet.customquests.quests.*;
import com.vincentmet.customquests.screens.elements.IQuestingGuiElement;
import com.vincentmet.customquests.screens.elements.labels.Label;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ButtonHandInRequirement implements IQuestingGuiElement {
    private Screen root;
    public static final int WIDTH = 64;
    public static final int HEIGHT = 20;
    private int x;
    private int y;
    private int questId;
    private int questReqId;
    private QuestRequirement questRequirement;
    private String textNotHandedIn = "Hand in";
    private String textHandedIn = "Handed in!";

    public ButtonHandInRequirement(Screen root, int posX, int posY, int questId, int questReqId, QuestRequirement questRequirements){
        this.root = root;
        this.x = posX;
        this.y = posY;
        this.questId = questId;
        this.questReqId = questReqId;
        this.questRequirement = questRequirements;
    }

    @Override
    public void update(PlayerEntity player, double mouseX, double mouseY, int width, int height) {

    }

    @Override
    public void render(PlayerEntity player, double mouseX, double mouseY) {
        String text = QuestUserProgress.isRequirementCompleted(Utils.simplifyUUID(player.getUniqueID()), questId, questReqId) ? textHandedIn : textNotHandedIn;
        if(questRequirement.getType() == QuestRequirementType.ITEM_DELIVER && !QuestUserProgress.isRequirementCompleted(Utils.simplifyUUID(player.getUniqueID()), questId, questReqId)){
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
        if(questRequirement.getType() == QuestRequirementType.ITEM_DELIVER && !QuestUserProgress.isRequirementCompleted(Utils.simplifyUUID(player.getUniqueID()), questId, questReqId)){
            if(Utils.isMouseInBounds(mouseX, mouseY, x, y, x+WIDTH, y+HEIGHT)){
                PacketHandler.CHANNEL.sendToServer(new MessageHandInButtonPressClientToServer(questId, questReqId));
            }
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