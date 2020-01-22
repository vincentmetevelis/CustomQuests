package com.vincentmet.customquests.network.packets;

import com.vincentmet.customquests.lib.Utils;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import java.util.function.Supplier;

public class MessageOpenEditorGuiServerToClient {

    public MessageOpenEditorGuiServerToClient(){}

    public static void encode(MessageOpenEditorGuiServerToClient packet, PacketBuffer buffer){}

    public static MessageOpenEditorGuiServerToClient decode(PacketBuffer buffer) {
        return new MessageOpenEditorGuiServerToClient();
    }

    public static void handle(final MessageOpenEditorGuiServerToClient message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(Utils::displayEditorGui);
        ctx.get().setPacketHandled(true);
    }
}