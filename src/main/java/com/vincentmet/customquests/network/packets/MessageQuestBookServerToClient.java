package com.vincentmet.customquests.network.packets;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.lib.handlers.StructureHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageQuestBookServerToClient {
    public JsonObject json;

    public MessageQuestBookServerToClient(){

    }

    public MessageQuestBookServerToClient(String json){
        this.json = new JsonParser().parse(json).getAsJsonObject();
    }

    public static void encode(MessageQuestBookServerToClient packet, PacketBuffer buffer){
        buffer.writeString(Ref.ALL_QUESTBOOK.getJson().toString());
    }

    public static MessageQuestBookServerToClient decode(PacketBuffer buffer){
        return new MessageQuestBookServerToClient(buffer.readString());
    }

    public static void handle(final MessageQuestBookServerToClient message, Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(()->{
            StructureHandler.initQuestbook(message.json);
        });
        ctx.get().setPacketHandled(true);
    }
}
