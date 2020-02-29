package com.vincentmet.customquests.network.packets;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.lib.handlers.StructureHandler;
import com.vincentmet.customquests.quests.party.Party;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageUpdateQuestPartiesServerToClient {
    public JsonObject json;

    public MessageUpdateQuestPartiesServerToClient(){

    }

    public MessageUpdateQuestPartiesServerToClient(JsonObject json){
        this.json = json;//json only used for reading, not for writing
    }

    public static void encode(MessageUpdateQuestPartiesServerToClient packet, PacketBuffer buffer){
        JsonObject json = new JsonObject();
        JsonArray partyArray = new JsonArray();
        for(Party party : Ref.ALL_QUESTING_PARTIES){
            partyArray.add(party.getJson());
        }
        json.add("parties", partyArray);
        buffer.writeString(json.toString());
    }

    public static MessageUpdateQuestPartiesServerToClient decode(PacketBuffer buffer) {
        String questPartiesString = buffer.readString();
        JsonObject json = new JsonParser().parse(questPartiesString).getAsJsonObject();
        return new MessageUpdateQuestPartiesServerToClient(json);
    }

    public static void handle(final MessageUpdateQuestPartiesServerToClient message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            StructureHandler.initQuestingParties(message.json);
            Ref.shouldSaveNextTick = false;
        });
        ctx.get().setPacketHandled(true);
    }
}
