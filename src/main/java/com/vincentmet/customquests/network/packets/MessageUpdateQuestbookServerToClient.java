package com.vincentmet.customquests.network.packets;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.lib.handlers.StructureHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;
import java.util.logging.Logger;

public class MessageUpdateQuestbookServerToClient {
    public JsonObject json;

    public MessageUpdateQuestbookServerToClient(){

    }

    public MessageUpdateQuestbookServerToClient(JsonObject json){
        this.json = json;//json only used for reading, not for writing
    }

    public static void encode(MessageUpdateQuestbookServerToClient packet, PacketBuffer buffer){
        buffer.writeString(Ref.ALL_QUESTBOOK.getJson().toString());
    }

    public static MessageUpdateQuestbookServerToClient decode(PacketBuffer buffer) {
        String questString = buffer.readString();
        JsonObject json = new JsonParser().parse(questString).getAsJsonObject();
        return new MessageUpdateQuestbookServerToClient(json);
    }

    public static void handle(final MessageUpdateQuestbookServerToClient message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            StructureHandler.initQuestbook(message.json);
        });
        ctx.get().setPacketHandled(true);
    }
}
