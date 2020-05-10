package com.vincentmet.customquests.network.packets;

import com.google.gson.*;
import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.lib.handlers.StructureHandler;
import com.vincentmet.customquests.quests.Quest;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class MessageUpdateSingleQuestServerToClient{
    public int questId;//used for writing only

    public JsonObject json;//used for reading only

    public MessageUpdateSingleQuestServerToClient(int questId){
        this.questId = questId;
    }

    public MessageUpdateSingleQuestServerToClient(JsonObject json){
        this.json = json;
    }

    public static void encode(MessageUpdateSingleQuestServerToClient packet, PacketBuffer buffer){
        JsonObject json = new JsonObject();
        json.addProperty("questid", packet.questId);
        for(Quest quest : Ref.ALL_QUESTS.values().stream().filter(quest -> quest.getId()==packet.questId).collect(Collectors.toList())){
            json.add("content", quest.getJson());
        }
        buffer.writeString(json.toString());
    }

    public static MessageUpdateSingleQuestServerToClient decode(PacketBuffer buffer) {
        String questProgressString = buffer.readString();
        JsonObject json = new JsonParser().parse(questProgressString).getAsJsonObject();
        return new MessageUpdateSingleQuestServerToClient(json);
    }

    public static void handle(final MessageUpdateSingleQuestServerToClient message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            int questId = message.json.get("questid").getAsInt();
            JsonObject json = message.json.get("content").getAsJsonObject();
            if(Ref.ALL_QUESTS.values().stream().noneMatch(quest -> quest.getId()==questId)){
                Ref.ALL_QUESTS.put(questId, StructureHandler.initSingleQuest(json));
            }
            Ref.shouldSaveNextTick = false;
        });
        ctx.get().setPacketHandled(true);
    }
}
