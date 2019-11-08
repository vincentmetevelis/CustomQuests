package com.vincentmet.customquests.network.packets;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.lib.handlers.StructureHandler;
import com.vincentmet.customquests.quests.Quest;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import java.util.function.Supplier;

public class MessageQuestsServerToClient {
    public JsonObject json;

    public MessageQuestsServerToClient(){

    }

    public MessageQuestsServerToClient(String json){
        this.json = new JsonParser().parse(json).getAsJsonObject();
    }

    public static void encode(MessageQuestsServerToClient packet, PacketBuffer buffer){
        JsonObject json = new JsonObject();
        JsonArray jsonQuestArray = new JsonArray();
        for(Quest quest : Ref.ALL_QUESTS){
                jsonQuestArray.add(quest.getJson());
                }
                json.add("quests", jsonQuestArray);
                buffer.writeString(json.toString());
                }

public static MessageQuestsServerToClient decode(PacketBuffer buffer){
        return new MessageQuestsServerToClient(buffer.readString());
        }

public static void handle(final MessageQuestsServerToClient message, Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(()->{
        StructureHandler.initQuests(message.json);
        });
        ctx.get().setPacketHandled(true);
        }
        }
