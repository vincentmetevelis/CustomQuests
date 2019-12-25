package com.vincentmet.customquests.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.vincentmet.customquests.lib.Ref;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.TranslationTextComponent;

public class CustomQuestsCommand {
    public CustomQuestsCommand(CommandDispatcher<CommandSource> dispatcher){
        dispatcher.register(
                LiteralArgumentBuilder.<CommandSource>literal(Ref.MODID)
                        .then(PartyCommand.register())
                        .executes(context -> {
                            context.getSource().sendFeedback(new TranslationTextComponent("Please use a subcommand!"), false);
                            return 0;
                        })
        );
    }
}
