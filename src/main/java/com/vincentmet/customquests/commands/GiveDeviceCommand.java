package com.vincentmet.customquests.commands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.vincentmet.customquests.Objects;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.item.ItemStack;

public class GiveDeviceCommand {
    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("give")
                .requires(cs -> cs.hasPermissionLevel(0))
                .then(Commands.argument("player", EntityArgument.player())
                        .requires(cs -> cs.hasPermissionLevel(2))
                        .executes(context -> {
                            EntityArgument.getEntity(context, "player").getCommandSource().asPlayer().inventory.addItemStackToInventory(new ItemStack(Objects.Items.itemQuestingDevice, 1));
                            return 0;
                        })
                )
                .executes(context -> {
                    context.getSource().asPlayer().inventory.addItemStackToInventory(new ItemStack(Objects.Items.itemQuestingDevice, 1));
                    return 0;
                })
        ;
    }
}