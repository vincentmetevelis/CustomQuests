package com.vincentmet.customquests.network.packets;

import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.lib.handlers.PacketHandler;
import com.vincentmet.customquests.quests.progress.QuestStatus;
import com.vincentmet.customquests.quests.progress.QuestUserProgress;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.Map;

public class PacketHelper {
    public static void sendAllProgressUpdatePackets(ServerPlayerEntity playerEntity){
        for(Map.Entry<String, QuestUserProgress> user : Ref.ALL_QUESTING_PROGRESS.entrySet()){
            PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> playerEntity), new MessageUpdateQuestUserProgressServerToClient(user.getKey()));
            for(Map.Entry<Integer, QuestStatus> status : user.getValue().getQuestStatuses().entrySet()){
                PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> playerEntity), new MessageUpdateSingleQuestProgressServerToClient(user.getKey(), status.getValue().getQuestId()));
            }
        }
    }
}
