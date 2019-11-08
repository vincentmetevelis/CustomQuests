package com.vincentmet.customquests.network.packets;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.lib.handlers.StructureHandler;
import com.vincentmet.customquests.quests.Quest;
import com.vincentmet.customquests.quests.QuestUserProgress;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageQuestUserProgressServerToClient {
    public JsonObject json;

    public MessageQuestUserProgressServerToClient(){

    }

    public MessageQuestUserProgressServerToClient(String json){
        this.json = new JsonParser().parse(json).getAsJsonObject();
    }

    public static void encode(MessageQuestUserProgressServerToClient packet, PacketBuffer buffer){
        JsonObject json = new JsonObject();
        JsonArray jsonPlayersArray = new JsonArray();
        for(QuestUserProgress userprogress : Ref.ALL_QUESTING_PROGRESS){
            jsonPlayersArray.add(userprogress.getJson());
        }
        json.add("players", jsonPlayersArray);
        buffer.writeString(json.toString());
    }

    public static MessageQuestUserProgressServerToClient decode(PacketBuffer buffer){
        return new MessageQuestUserProgressServerToClient(buffer.readString());
    }

    public static void handle(final MessageQuestUserProgressServerToClient message, Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(()->{
            StructureHandler.initQuestingProgress(message.json);
        });
        ctx.get().setPacketHandled(true);
    }
}
