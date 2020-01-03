package com.vincentmet.customquests.screens.elements;

import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.lib.Utils;
import com.vincentmet.customquests.quests.party.Party;
import com.vincentmet.customquests.screens.ScreenQuestingDevice;
import com.vincentmet.customquests.screens.elements.labels.Label;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ButtonParty implements IQuestingGuiElement {
    private Screen root;
    public static final int WIDTH = 150;
    public static final int HEIGHT = 20;
    private int x;
    private int y;
    private Party party;

    public ButtonParty(Screen root, int posX, int posY, Party party){
        this.root = root;
        this.x = posX;
        this.y = posY;
        this.party = party;
    }

    @Override
    public void update(IQuestingGuiElement gui, PlayerEntity player, double mouseX, double mouseY, int width, int height) {

    }

    @Override
    public void render(IQuestingGuiElement gui, PlayerEntity player, double mouseX, double mouseY) {
        if(party.isOpenForEveryone() || party.getPartyMembers().contains(Utils.simplifyUUID(player.getUniqueID())) || party.getCreator().equals(Utils.simplifyUUID(player.getUniqueID()))){
            if(Utils.isMouseInBounds(mouseX, mouseY, x, y, x + WIDTH, y + HEIGHT)){
                root.getMinecraft().getTextureManager().bindTexture(Ref.IMAGE_BUTTON_QUESTLINE_PRESSED);
            }else{
                root.getMinecraft().getTextureManager().bindTexture(Ref.IMAGE_BUTTON_QUESTLINE_UNPRESSED);
            }
        }else{
            root.getMinecraft().getTextureManager().bindTexture(Ref.IMAGE_BUTTON_QUESTLINE_DISABLED);
        }
        root.blit(x, y, 0, 0, WIDTH, HEIGHT);


        new Label(
                root,
                x+(WIDTH/2)-Ref.FONT_RENDERER.getStringWidth(party.getPartyName())/2,
                y+(HEIGHT/2)-Ref.FONT_RENDERER.FONT_HEIGHT/2,
                party.getPartyName(),
                0xFFFFFF,
                false,
                false
        ).render(this, player, mouseX, mouseY);
    }

    @Override
    public void onClick(IQuestingGuiElement gui, PlayerEntity player, double mouseX, double mouseY) {
        if(Utils.isMouseInBounds(mouseX, mouseY, x, y, x+WIDTH, y+HEIGHT)){
            ScreenQuestingDevice.activeParty = party.getId();
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