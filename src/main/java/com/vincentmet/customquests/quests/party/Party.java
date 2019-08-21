package com.vincentmet.customquests.quests.party;

import net.minecraft.entity.player.PlayerEntity;

import javax.annotation.Nullable;

@Nullable
public class Party {
    private PlayerEntity creator;
    private String partyName;
    private boolean shareLoot;
    private boolean openForEveryone;

    public Party(PlayerEntity creator, String partyName, boolean shareLoot, boolean openForEveryone){
        this.creator = creator;
        this.partyName = partyName;
        this.shareLoot = shareLoot;
        this.openForEveryone = openForEveryone;
    }

    public PlayerEntity getCreator(){
        return creator;
    }

    public String getPartyName(){
        return partyName;
    }

    public boolean isLootShared(){
        return shareLoot;
    }

    public boolean isOpenForEveryone(){
        return openForEveryone;
    }
}