package com.vincentmet.customquests.lib.handlers;

import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.network.packets.*;
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
        CHANNEL.registerMessage(nextID(), MessageUpdateSingleQuestProgressServerToClient.class, MessageUpdateSingleQuestProgressServerToClient::encode, MessageUpdateSingleQuestProgressServerToClient::decode, MessageUpdateSingleQuestProgressServerToClient::handle);
        CHANNEL.registerMessage(nextID(), MessageUpdateQuestUserProgressServerToClient.class, MessageUpdateQuestUserProgressServerToClient::encode, MessageUpdateQuestUserProgressServerToClient::decode, MessageUpdateQuestUserProgressServerToClient::handle);
        CHANNEL.registerMessage(nextID(), MessageUpdateQuestbookServerToClient.class, MessageUpdateQuestbookServerToClient::encode, MessageUpdateQuestbookServerToClient::decode, MessageUpdateQuestbookServerToClient::handle);
        CHANNEL.registerMessage(nextID(), MessageUpdateQuestsServerToClient.class, MessageUpdateQuestsServerToClient::encode, MessageUpdateQuestsServerToClient::decode, MessageUpdateQuestsServerToClient::handle);
        CHANNEL.registerMessage(nextID(), MessageRewardButtonPressClientToServer.class, MessageRewardButtonPressClientToServer::encode, MessageRewardButtonPressClientToServer::decode, MessageRewardButtonPressClientToServer::handle);
        CHANNEL.registerMessage(nextID(), MessageHandInButtonPressClientToServer.class, MessageHandInButtonPressClientToServer::encode, MessageHandInButtonPressClientToServer::decode, MessageHandInButtonPressClientToServer::handle);
        CHANNEL.registerMessage(nextID(), MessageUpdateQuestPartiesServerToClient.class, MessageUpdateQuestPartiesServerToClient::encode, MessageUpdateQuestPartiesServerToClient::decode, MessageUpdateQuestPartiesServerToClient::handle);
        CHANNEL.registerMessage(nextID(), MessageOpenEditorGuiServerToClient.class, MessageOpenEditorGuiServerToClient::encode, MessageOpenEditorGuiServerToClient::decode, MessageOpenEditorGuiServerToClient::handle);
    }
}
