package com.vincentmet.customquests.network.packets;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.lib.handlers.PacketHandler;
import com.vincentmet.customquests.lib.handlers.StructureHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.naming.spi.DirectoryManager;
import java.util.function.Supplier;

public class MessageNotifyServer {
    public JsonObject json;

    public MessageNotifyServer(){}
    public static void encode(MessageNotifyServer packet, PacketBuffer buffer){}

    public static MessageNotifyServer decode(PacketBuffer buffer){
        return new MessageNotifyServer();
    }

    public static void handle(final MessageNotifyServer message, Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(()->{
            PacketHandler.CHANNEL.send(PacketDistributor.ALL.noArg(), new MessageQuestUserProgressServerToClient());//todo fix this mess
        });
        ctx.get().setPacketHandled(true);
    }
}
