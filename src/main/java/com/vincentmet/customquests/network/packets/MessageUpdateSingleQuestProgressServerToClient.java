package com.vincentmet.customquests.network.packets;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.lib.handlers.StructureHandler;
import com.vincentmet.customquests.quests.progress.ProgressHelper;
import com.vincentmet.customquests.quests.progress.QuestStatus;
import com.vincentmet.customquests.quests.progress.QuestUserProgress;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class MessageUpdateSingleQuestProgressServerToClient {
    public String uuid;//used for writing only
    public int questId;//used for writing only

    public JsonObject json;//used for reading only

    public MessageUpdateSingleQuestProgressServerToClient(String uuid, int questId){
        this.uuid = uuid;
        this.questId = questId;
    }

    public MessageUpdateSingleQuestProgressServerToClient(JsonObject json){
        this.json = json;
    }

    public static void encode(MessageUpdateSingleQuestProgressServerToClient packet, PacketBuffer buffer){
        JsonObject json = new JsonObject();
        json.addProperty("uuid", packet.uuid);
        json.addProperty("questid", packet.questId);
        for(Map.Entry<Integer, QuestStatus> questStatus : ProgressHelper.getUserProgressForUuid(packet.uuid).getQuestStatuses().entrySet()){
            if(questStatus.getValue().getQuestId() == packet.questId){
                json.add("content", questStatus.getValue().getJson());
            }
        }
        buffer.writeString(json.toString());
    }

    public static MessageUpdateSingleQuestProgressServerToClient decode(PacketBuffer buffer) {
        String questProgressString = buffer.readString();
        JsonObject json = new JsonParser().parse(questProgressString).getAsJsonObject();
        return new MessageUpdateSingleQuestProgressServerToClient(json);
    }

    public static void handle(final MessageUpdateSingleQuestProgressServerToClient message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            String uuid = message.json.get("uuid").getAsString();
            int questId = message.json.get("questid").getAsInt();
            JsonObject json = message.json.get("content").getAsJsonObject();
            Ref.ALL_QUESTING_PROGRESS.computeIfAbsent(uuid, uuidLambda -> {
                return new QuestUserProgress(uuidLambda, Ref.ERR_MSG_INT_INVALID_JSON, new ArrayList<>(), new HashMap<>());
            }).getQuestStatuses().put(questId, StructureHandler.initSingleQuestProgressForUser(json));//todo
            Ref.shouldSaveNextTick = false;
        });
        ctx.get().setPacketHandled(true);
    }
}
