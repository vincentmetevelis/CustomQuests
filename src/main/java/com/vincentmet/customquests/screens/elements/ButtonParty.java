package com.vincentmet.customquests.screens.elements;

import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.lib.Utils;
import com.vincentmet.customquests.quests.party.Party;
import com.vincentmet.customquests.screens.ScreenQuestingDevice;
import com.vincentmet.customquests.screens.elements.labels.Label;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ButtonParty implements IQuestingGuiElement {
    public static final int WIDTH = 150;
    public static final int HEIGHT = 20;
    private int x;
    private int y;
    private Party party;

    public ButtonParty(int posX, int posY, Party party){
        this.x = posX;
        this.y = posY;
        this.party = party;
    }

    @Override
    public <T extends ScreenQuestingDevice> void render(T gui, PlayerEntity player, double mouseX, double mouseY) {
        if(party.isOpenForEveryone() || party.getPartyMembers().contains(Utils.simplifyUUID(player.getUniqueID())) || party.getCreator().equals(Utils.simplifyUUID(player.getUniqueID()))){
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
                x+(WIDTH/2)-Ref.FONT_RENDERER.getStringWidth(party.getPartyName())/2,
                y+(HEIGHT/2)-Ref.FONT_RENDERER.FONT_HEIGHT/2,
                party.getPartyName(),
                0xFFFFFF,
                false,
                false
        ).render(gui, player, mouseX, mouseY);
    }

    @Override
    public <T extends ScreenQuestingDevice> void onClick(T gui, PlayerEntity player, double mouseX, double mouseY) {
        if(Utils.isMouseInBounds(mouseX, mouseY, x, y, x+WIDTH, y+HEIGHT)){
            ScreenQuestingDevice.activeParty = party.getId();
        }
    }
}
