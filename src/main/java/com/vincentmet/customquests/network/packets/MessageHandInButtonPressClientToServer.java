package com.vincentmet.customquests.network.packets;

import com.vincentmet.customquests.lib.Utils;
import com.vincentmet.customquests.quests.IQuestRequirement;
import com.vincentmet.customquests.quests.progress.ProgressHelper;
import com.vincentmet.customquests.quests.quest.Quest;
import java.util.function.Supplier;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class MessageHandInButtonPressClientToServer {
    public int questId;
    public int subQuestId;

    public MessageHandInButtonPressClientToServer(int questId, int subQuestId){
        this.questId = questId;
        this.subQuestId = subQuestId;
    }

    public static void encode(MessageHandInButtonPressClientToServer packet, PacketBuffer buffer){
        buffer.writeInt(packet.questId);
        buffer.writeInt(packet.subQuestId);
    }

    public static MessageHandInButtonPressClientToServer decode(PacketBuffer buffer) {
        return new MessageHandInButtonPressClientToServer(buffer.readInt(), buffer.readInt());
    }

    public static void handle(final MessageHandInButtonPressClientToServer message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            int countSubReq = 0;
            for(IQuestRequirement iqr : Quest.getQuestFromId(message.questId).getRequirements().get(message.subQuestId).getSubRequirements()){
                ItemStack itemStack = Quest.getItemstackForItemHandIn(message.questId, message.subQuestId, countSubReq);
                int slotIndexCount = 0;
                for(ItemStack mainInventoryStack : ctx.get().getSender().inventory.mainInventory){
                    if(mainInventoryStack.getItem() == itemStack.getItem()){
                        int itemCountLeftToHandIn = ProgressHelper.getItemCountLeftToHandIn(Utils.simplifyUUID(ctx.get().getSender().getUniqueID()), message.questId, message.subQuestId, countSubReq);
                        System.out.println("Submitted item");
                        if(itemCountLeftToHandIn < mainInventoryStack.getCount()){
                            ProgressHelper.addPlayerProgress(Utils.simplifyUUID(ctx.get().getSender().getUniqueID()), message.questId, message.subQuestId, countSubReq, itemCountLeftToHandIn);
                            ctx.get().getSender().inventory.getStackInSlot(slotIndexCount).setCount(ctx.get().getSender().inventory.getStackInSlot(slotIndexCount).getCount() - itemCountLeftToHandIn);
                        }else{
                            ProgressHelper.addPlayerProgress(Utils.simplifyUUID(ctx.get().getSender().getUniqueID()), message.questId, message.subQuestId, countSubReq, mainInventoryStack.getCount());
                            ctx.get().getSender().inventory.removeStackFromSlot(slotIndexCount);
                        }
                    }
                    slotIndexCount++;
                }
                countSubReq++;
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
