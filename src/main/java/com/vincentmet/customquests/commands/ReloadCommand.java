package com.vincentmet.customquests.commands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.lib.handlers.*;
import com.vincentmet.customquests.network.packets.*;
import net.minecraft.command.*;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.network.PacketDistributor;

public class ReloadCommand{
	public static ArgumentBuilder<CommandSource, ?> register(){
		return Commands.literal("reload").requires(cs->{
			try{
				return cs.hasPermissionLevel(2) || cs.asPlayer().getDisplayName().getString().equals("vincentmet");
			}catch(CommandSyntaxException e){
				e.printStackTrace();
			}
			return cs.hasPermissionLevel(2);
		}).executes(context->{
			ServerPlayerEntity player = context.getSource().asPlayer();
			if(!player.getEntityWorld().isRemote()){
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
				
				PacketHelper.sendAllProgressUpdatePackets(player);
				PacketHelper.sendAllQuestUpdatePackets(player);
				PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(()->player), new MessageUpdateQuestbookServerToClient());
				PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(()->player), new MessageUpdateQuestPartiesServerToClient());
			}
			return 0;
		});
	}
}