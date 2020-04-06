package com.vincentmet.customquests.commands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.lib.handlers.JsonHandler;
import com.vincentmet.customquests.lib.handlers.PacketHandler;
import com.vincentmet.customquests.lib.handlers.StructureHandler;
import com.vincentmet.customquests.network.packets.MessageUpdateQuestPartiesServerToClient;
import com.vincentmet.customquests.network.packets.MessageUpdateQuestbookServerToClient;
import com.vincentmet.customquests.network.packets.MessageUpdateQuestsServerToClient;
import com.vincentmet.customquests.network.packets.PacketHelper;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.network.PacketDistributor;

public class ReloadCommand {
    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("reload")
                .requires(cs -> {
                    try {
                        return cs.hasPermissionLevel(2) || cs.asPlayer().getDisplayName().getString().equals("vincentmet");
                    } catch (CommandSyntaxException e) {
                        e.printStackTrace();
                    }
                    return cs.hasPermissionLevel(2);
                })
                .executes(context -> {
                    JsonHandler.loadJson(
                            Ref.questsLocation = FMLPaths.CONFIGDIR.get().resolve("Quests.json"),
                            Ref.questBookLocation = FMLPaths.CONFIGDIR.get().resolve("QuestsBook.json"),
                            Ref.questingProgressLocation = Ref.currWorldDir.resolve("QuestingProgress.json"),
                            Ref.questingPartiesLocation = Ref.currWorldDir.resolve("QuestingParties.json")
                    );
                    StructureHandler.initQuests(JsonHandler.getQuestsJson());
                    StructureHandler.initQuestbook(JsonHandler.getQuestbookJson());
                    StructureHandler.initQuestingProgress(JsonHandler.getQuestingProgressJson());
                    StructureHandler.initQuestingParties(JsonHandler.getQuestingPartiesJson());

                    ServerPlayerEntity player;
                    try {
                        player = context.getSource().asPlayer();
                        PacketHelper.sendAllProgressUpdatePackets(context.getSource().asPlayer());
                        PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new MessageUpdateQuestsServerToClient());
                        PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new MessageUpdateQuestbookServerToClient());
                        PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new MessageUpdateQuestPartiesServerToClient());
                    }catch (CommandSyntaxException e){
                        e.printStackTrace();
                    }
                    return 0;
                });
    }
}
