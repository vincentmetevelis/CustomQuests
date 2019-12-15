package com.vincentmet.customquests.network.packets;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.lib.handlers.StructureHandler;
import com.vincentmet.customquests.quests.QuestUserProgress;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import java.util.function.Supplier;

public class MessageUpdateQuestProgressServerToClient {
    public JsonObject json;

    public MessageUpdateQuestProgressServerToClient(){

    }

    public MessageUpdateQuestProgressServerToClient(JsonObject json){
        this.json = json;//json only used for reading, not for writing
    }

    public static void encode(MessageUpdateQuestProgressServerToClient packet, PacketBuffer buffer){
        JsonObject json = new JsonObject();
        JsonArray playerArray = new JsonArray();
        for(QuestUserProgress user : Ref.ALL_QUESTING_PROGRESS){
            playerArray.add(user.getJson());
        }
        json.add("players", playerArray);
        buffer.writeString(json.toString());
    }

    public static MessageUpdateQuestProgressServerToClient decode(PacketBuffer buffer) {
        String questProgressString = buffer.readString();
        JsonObject json = new JsonParser().parse(questProgressString).getAsJsonObject();
        return new MessageUpdateQuestProgressServerToClient(json);
    }

    public static void handle(final MessageUpdateQuestProgressServerToClient message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            StructureHandler.initQuestingProgress(message.json);
        });
        ctx.get().setPacketHandled(true);
    }
}
