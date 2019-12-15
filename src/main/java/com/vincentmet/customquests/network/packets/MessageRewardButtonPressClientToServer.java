package com.vincentmet.customquests.network.packets;

import com.vincentmet.customquests.lib.Utils;
import com.vincentmet.customquests.quests.Quest;
import com.vincentmet.customquests.quests.QuestUserProgress;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import java.util.function.Supplier;

public class MessageRewardButtonPressClientToServer {
    public int questId;

    public MessageRewardButtonPressClientToServer(int questId){
        this.questId = questId;
    }

    public static void encode(MessageRewardButtonPressClientToServer packet, PacketBuffer buffer){
        buffer.writeInt(packet.questId);
    }

    public static MessageRewardButtonPressClientToServer decode(PacketBuffer buffer) {
        return new MessageRewardButtonPressClientToServer(buffer.readInt());
    }

    public static void handle(final MessageRewardButtonPressClientToServer message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            QuestUserProgress.getUserProgressForUuid(Utils.simplifyUUID(ctx.get().getSender().getUniqueID())).getQuestStatuses().stream().filter(questStatus -> questStatus.getQuestId() == message.questId).forEach(questStatus -> {
                if(!questStatus.isClaimed()){
                    Quest.getQuestFromId(message.questId).getRewards().forEach(reward -> reward.getReward().executeReward(ctx.get().getSender()));
                    questStatus.setClaimed(true);
                }
            });
        });
        ctx.get().setPacketHandled(true);
    }
}
