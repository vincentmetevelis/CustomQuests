package com.vincentmet.customquests.network.packets;

import com.google.gson.*;
import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.lib.handlers.StructureHandler;
import com.vincentmet.customquests.quests.Quest;
import java.util.function.Supplier;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class MessageUpdateQuestsServerToClient {
    public JsonObject json;

    public MessageUpdateQuestsServerToClient(){

    }

    public MessageUpdateQuestsServerToClient(JsonObject json){
        this.json = json;//json only used for reading, not for writing
    }

    public static void encode(MessageUpdateQuestsServerToClient packet, PacketBuffer buffer){
        JsonObject json = new JsonObject();
        JsonArray questArray = new JsonArray();
        for(Quest quest : Ref.ALL_QUESTS.values()){
            questArray.add(quest.getJson());
        }
        json.add("quests", questArray);
        buffer.writeString(json.toString());
    }

    public static MessageUpdateQuestsServerToClient decode(PacketBuffer buffer) {
        String questString = buffer.readString();
        JsonObject json = new JsonParser().parse(questString).getAsJsonObject();
        return new MessageUpdateQuestsServerToClient(json);
    }

    public static void handle(final MessageUpdateQuestsServerToClient message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            StructureHandler.initQuests(message.json);
        });
        ctx.get().setPacketHandled(true);
    }
}
