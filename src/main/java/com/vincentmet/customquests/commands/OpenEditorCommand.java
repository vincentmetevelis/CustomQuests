package com.vincentmet.customquests.commands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.vincentmet.customquests.lib.handlers.PacketHandler;
import com.vincentmet.customquests.network.packets.MessageOpenEditorGuiServerToClient;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.PacketDistributor;

public class OpenEditorCommand {
    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("editor")
                .requires(cs -> cs.hasPermissionLevel(2))
                .executes(context -> {
                    ServerPlayerEntity spe = context.getSource().asPlayer();
                    PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> spe), new MessageOpenEditorGuiServerToClient());
                    return 0;
                })
        ;
    }
}