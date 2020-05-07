package com.vincentmet.customquests.network.packets;

import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.lib.handlers.PacketHandler;
import com.vincentmet.customquests.quests.*;
import java.util.Map;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.PacketDistributor;

public class PacketHelper {
    public static void sendAllProgressUpdatePackets(ServerPlayerEntity playerEntity){
        for(Map.Entry<String, QuestUserProgress> user : Ref.ALL_QUESTING_PROGRESS.entrySet()){
            PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> playerEntity), new MessageUpdateQuestUserProgressServerToClient(user.getKey()));
            for(Map.Entry<Integer, QuestStatus> status : user.getValue().getQuestStatuses().entrySet()){
                PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> playerEntity), new MessageUpdateSingleQuestProgressServerToClient(user.getKey(), status.getValue().getQuestId()));
            }
        }
    }
    public static void sendAllQuestUpdatePackets(ServerPlayerEntity playerEntity){
        for(Quest quest : Ref.ALL_QUESTS.values()){
            PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> playerEntity), new MessageUpdateSingleQuestServerToClient(quest.getId()));
        }
    }
}
