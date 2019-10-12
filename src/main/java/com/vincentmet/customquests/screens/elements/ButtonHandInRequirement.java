package com.vincentmet.customquests.screens.elements;

import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.lib.Triple;
import com.vincentmet.customquests.lib.Utils;
import com.vincentmet.customquests.quests.*;
import com.vincentmet.customquests.screens.ScreenQuestingDevice;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;

public class ButtonHandInRequirement implements IQuestingGuiElement {
    public static final int WIDTH = 64;
    public static final int HEIGHT = 20;
    private int x;
    private int y;
    private int questId;
    private int questReqId;
    private QuestRequirement questRequirement;
    private String text = "Hand in";

    public ButtonHandInRequirement(int posX, int posY, int questId, int questReqId, QuestRequirement questRequirements){
        this.x = posX;
        this.y = posY;
        this.questId = questId;
        this.questReqId = questReqId;
        this.questRequirement = questRequirements;
    }

    @Override
    public <T extends ScreenQuestingDevice> void render(T gui, PlayerEntity player, double mouseX, double mouseY) {
        if(questRequirement.getType() == QuestRequirementType.ITEM_DELIVER){
            if(Utils.isMouseInBounds(mouseX, mouseY, x, y, x + WIDTH, y + HEIGHT)){
                gui.getMinecraft().getTextureManager().bindTexture(Ref.IMAGE_BUTTON_CLAIM_REWARD_PRESSED);
            }else{
                gui.getMinecraft().getTextureManager().bindTexture(Ref.IMAGE_BUTTON_CLAIM_REWARD_UNPRESSED);
            }
        }else{
            gui.getMinecraft().getTextureManager().bindTexture(Ref.IMAGE_BUTTON_CLAIM_REWARD_DISABLED);
        }
        gui.blit(x, y, 0, 0, WIDTH, HEIGHT);


        new Label(
                x+(WIDTH/2)-Ref.FONT_RENDERER.getStringWidth(text)/2,
                y+(HEIGHT/2)-Ref.FONT_RENDERER.FONT_HEIGHT/2,
                text,
                0xFFFFFF,
                false,
                false
        ).render(gui, player, mouseX, mouseY);
    }

    @Override
    public <T extends ScreenQuestingDevice> void onClick(T gui, PlayerEntity player, double mouseX, double mouseY) {
        if(questRequirement.getType() == QuestRequirementType.ITEM_DELIVER){
            if(Utils.isMouseInBounds(mouseX, mouseY, x, y, x+WIDTH, y+HEIGHT)){
                int countSubReq = 0;
                for(IQuestRequirement iqr : questRequirement.getSubRequirements()){
                    ItemStack itemStack = Quest.getItemstackForItemHandIn(questId, questReqId, countSubReq);
                    for(ItemStack mainInventoryStack : player.inventory.mainInventory){
                        if(mainInventoryStack.getItem() == itemStack.getItem()){
                            QuestUserProgress.addPlayerProgress(Utils.getUUID("vincentmet"), questId, questReqId, countSubReq, mainInventoryStack.getCount());
                            System.out.println("Submitted item");
                            //player.inventory.mainInventory.remove(mainInventoryStack); todo fix this ++ check for quest completion & disable handin buttons for that & don't consume more items than required by quest
                        }
                    }
                    countSubReq++;
                }

            }
        }
    }
}
