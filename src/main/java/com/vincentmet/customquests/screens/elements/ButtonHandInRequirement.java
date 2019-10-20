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
    private String textNotHandedIn = "Hand in";
    private String textHandedIn = "Handed in!";

    public ButtonHandInRequirement(int posX, int posY, int questId, int questReqId, QuestRequirement questRequirements){
        this.x = posX;
        this.y = posY;
        this.questId = questId;
        this.questReqId = questReqId;
        this.questRequirement = questRequirements;
    }

    @Override
    public <T extends ScreenQuestingDevice> void render(T gui, PlayerEntity player, double mouseX, double mouseY) {
        String text = QuestUserProgress.isRequirementCompleted(player.getUniqueID().toString().replaceAll("-", ""), questId, questReqId) ? textHandedIn : textNotHandedIn;
        if(questRequirement.getType() == QuestRequirementType.ITEM_DELIVER && !QuestUserProgress.isRequirementCompleted(player.getUniqueID().toString().replaceAll("-", ""), questId, questReqId)){
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
        if(questRequirement.getType() == QuestRequirementType.ITEM_DELIVER && !QuestUserProgress.isRequirementCompleted(player.getUniqueID().toString().replaceAll("-", ""), questId, questReqId)){
            if(Utils.isMouseInBounds(mouseX, mouseY, x, y, x+WIDTH, y+HEIGHT)){
                int countSubReq = 0;
                for(IQuestRequirement iqr : questRequirement.getSubRequirements()){
                    ItemStack itemStack = Quest.getItemstackForItemHandIn(questId, questReqId, countSubReq);
                    int slotIndexCount = 0;
                    for(ItemStack mainInventoryStack : player.inventory.mainInventory){
                        if(mainInventoryStack.getItem() == itemStack.getItem()){
                            int itemCountLeftToHandIn = QuestUserProgress.getItemCountLeftToHandIn(player.getUniqueID().toString().replaceAll("-", ""), questId, questReqId, countSubReq);
                            System.out.println("Submitted item");
                            if(itemCountLeftToHandIn < mainInventoryStack.getCount()){
                                QuestUserProgress.addPlayerProgress(player.getUniqueID().toString().replaceAll("-", ""), questId, questReqId, countSubReq, itemCountLeftToHandIn);
                                player.inventory.getStackInSlot(slotIndexCount).setCount(player.inventory.getStackInSlot(slotIndexCount).getCount() - itemCountLeftToHandIn);
                            }else{
                                QuestUserProgress.addPlayerProgress(player.getUniqueID().toString().replaceAll("-", ""), questId, questReqId, countSubReq, mainInventoryStack.getCount());
                                player.inventory.removeStackFromSlot(slotIndexCount);
                            }
                        }
                        slotIndexCount++;
                    }
                    countSubReq++;
                }

            }
        }
    }
}
