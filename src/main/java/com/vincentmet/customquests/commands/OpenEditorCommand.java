package com.vincentmet.customquests.commands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.vincentmet.customquests.Objects;
import com.vincentmet.customquests.screens.ScreenQuestingEditor;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

public class OpenEditorCommand {
    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("editor")
                .requires(cs -> cs.hasPermissionLevel(2))
                .executes(context -> {
                    //Minecraft.getInstance().displayGuiScreen(new ScreenQuestingEditor());//TODO packetify this
                    return 0;
                })
        ;
    }
}