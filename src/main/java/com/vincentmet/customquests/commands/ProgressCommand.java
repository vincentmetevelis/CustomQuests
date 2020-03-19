package com.vincentmet.customquests.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.vincentmet.customquests.lib.Utils;
import com.vincentmet.customquests.quests.Quest;
import com.vincentmet.customquests.quests.QuestUserProgress;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;

public class ProgressCommand {
    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("progress")
                .requires(cs -> {
                    try {
                        return cs.hasPermissionLevel(2) || cs.asPlayer().getDisplayName().getString().equals("vincentmet");
                    } catch (CommandSyntaxException e) {
                        e.printStackTrace();
                    }
                    return cs.hasPermissionLevel(2);
                })
                .then(Commands.argument("player", EntityArgument.player())
                        .then(Commands.literal("complete")
                                .then(Commands.argument("questid", IntegerArgumentType.integer())
                                        .executes(context -> {
                                            int inputQuestId = IntegerArgumentType.getInteger(context, "questid");
                                            String uuid = Utils.simplifyUUID(context.getSource().asPlayer().getUniqueID());
                                            QuestUserProgress qup = QuestUserProgress.getUserProgressForUuid(uuid);
                                            qup.getQuestStatuses().entrySet().stream().filter(questStatus-> questStatus.getKey() == inputQuestId).forEach(questStatus -> questStatus.getValue().completeAllRequirements());
                                            return 0;
                                        })
                                )
                        )
                        .executes(context -> {
                            //PlayerEntity player = EntityArgument.getPlayer(context, "player");
                            context.getSource().sendFeedback(new StringTextComponent("Please use a subcommand"), false);
                            return 0;
                        })
                )
                .executes(context -> {
                    context.getSource().sendFeedback(new StringTextComponent("Please use a subcommand"), false);
                    return 0;
                });
    }
}
