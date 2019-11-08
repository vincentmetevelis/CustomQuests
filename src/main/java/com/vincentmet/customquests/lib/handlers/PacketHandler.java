package com.vincentmet.customquests.lib.handlers;

import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.network.packets.MessageNotifyServer;
import com.vincentmet.customquests.network.packets.MessageQuestBookServerToClient;
import com.vincentmet.customquests.network.packets.MessageQuestUserProgressServerToClient;
import com.vincentmet.customquests.network.packets.MessageQuestsServerToClient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {
    private static int messageID = 0;
    private static final String PROTOCOL_VERSION = Integer.toString(1);

    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(Ref.MODID, "network"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();

    private static int nextID() {
        return messageID++;
    }

    public static void init() {
        CHANNEL.registerMessage(nextID(), MessageQuestsServerToClient.class, MessageQuestsServerToClient::encode, MessageQuestsServerToClient::decode, MessageQuestsServerToClient::handle);
        CHANNEL.registerMessage(nextID(), MessageQuestBookServerToClient.class, MessageQuestBookServerToClient::encode, MessageQuestBookServerToClient::decode, MessageQuestBookServerToClient::handle);
        CHANNEL.registerMessage(nextID(), MessageQuestUserProgressServerToClient.class, MessageQuestUserProgressServerToClient::encode, MessageQuestUserProgressServerToClient::decode, MessageQuestUserProgressServerToClient::handle);
        CHANNEL.registerMessage(nextID(), MessageNotifyServer.class, MessageNotifyServer::encode, MessageNotifyServer::decode, MessageNotifyServer::handle);
    }
}
