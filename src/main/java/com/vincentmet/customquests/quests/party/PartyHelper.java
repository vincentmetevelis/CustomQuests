package com.vincentmet.customquests.quests.party;

import net.minecraft.entity.player.PlayerEntity;
import java.util.List;

public class PartyHelper {
    public static List<Party> partyList;

    public static void createParty(PlayerEntity creator, String partyName, boolean shareLoot, boolean openForEveryone){
        partyList.add(new Party(creator, partyName, shareLoot, openForEveryone));
    }

    public static Party getParty(PlayerEntity player){
        for(Party party:partyList) {
            if(player == party.getCreator()){
                return party;
            }
        }
        return null;
    }

    public static void deleteParty(PlayerEntity player){
        for(Party party:partyList){
            if(player == party.getCreator()){
                partyList.remove(partyList.indexOf(party));
            }
        }
    }
}