package com.vincentmet.customquests.network.packets;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.quests.QuestUserProgress;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class MessageUpdateQuestUserProgressServerToClient {
    public String uuid;//used for writing only

    public JsonObject json;//used for reading only

    public MessageUpdateQuestUserProgressServerToClient(String uuid){
        this.uuid = uuid;
    }

    public MessageUpdateQuestUserProgressServerToClient(JsonObject json){
        this.json = json;
    }

    public static void encode(MessageUpdateQuestUserProgressServerToClient packet, PacketBuffer buffer){
        JsonObject json = new JsonObject();
        json.addProperty("uuid", packet.uuid);
        JsonArray completedIds = new JsonArray();
        for(int completedId : Ref.ALL_QUESTING_PROGRESS.get(packet.uuid).getCompletedQuestsIds()){
            completedIds.add(completedId);
        }
        json.add("completed_quests", completedIds);
        buffer.writeString(json.toString());
    }

    public static MessageUpdateQuestUserProgressServerToClient decode(PacketBuffer buffer) {
        String questProgressString = buffer.readString();
        JsonObject json = new JsonParser().parse(questProgressString).getAsJsonObject();
        return new MessageUpdateQuestUserProgressServerToClient(json);
    }

    public static void handle(final MessageUpdateQuestUserProgressServerToClient message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            String uuid = message.json.get("uuid").getAsString();
            JsonArray completedQuestIds = message.json.get("completed_quests").getAsJsonArray();
            List<Integer> ids = new ArrayList<>();
            for(JsonElement idElement : completedQuestIds){
                int id = idElement.getAsInt();
                ids.add(id);
            }
            Ref.ALL_QUESTING_PROGRESS.computeIfAbsent(uuid, uuidLambda -> {
                return new QuestUserProgress(uuidLambda, Ref.ERR_MSG_INT_INVALID_JSON, new ArrayList<>(), new HashMap<>());
            }).setCompletedQuestsIds(ids);
            Ref.shouldSaveNextTick = false;
        });
        ctx.get().setPacketHandled(true);
    }
}