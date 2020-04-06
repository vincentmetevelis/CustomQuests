package com.vincentmet.customquests.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.lib.Utils;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.TranslationTextComponent;

public class CustomQuestsCommand {
    public CustomQuestsCommand(CommandDispatcher<CommandSource> dispatcher){
        dispatcher.register(
                LiteralArgumentBuilder.<CommandSource>literal(Ref.MODID)
                        .then(PartyCommand.register())
                        .then(GiveDeviceCommand.register())
                        .then(OpenEditorCommand.register())
                        .then(ProgressCommand.register())
                        .then(ReloadCommand.register())
                        .executes(context -> {
                            context.getSource().sendFeedback(Utils.createTextComponent(".command_warning"), false);
                            return 0;
                        })
        );
    }
}
