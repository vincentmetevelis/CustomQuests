package com.vincentmet.customquests.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.lib.Utils;
import com.vincentmet.customquests.quests.party.Party;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;

public class PartyCommand {
    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("party")
                .requires(cs -> cs.hasPermissionLevel(0))
                .then(Commands.literal("create")
                        .then(Commands.argument("name", StringArgumentType.word())
                                .executes(context -> {
                                    Party.createNewParty(Utils.simplifyUUID(context.getSource().asPlayer().getUniqueID()), StringArgumentType.getString(context, "name"));
                                    Ref.shouldSaveNextTick = true;
                                    return 0;
                                })
                        )
                        .executes(context -> {
                            context.getSource().sendFeedback(new TranslationTextComponent(TextFormatting.BLUE + "[Custom Quests]" + TextFormatting.RESET + " Please specify a party name"), false);
                            Ref.shouldSaveNextTick = true;
                            return 0;
                        })
                ).then(Commands.literal("deleteall")
                        .executes(context -> {
                            context.getSource().sendFeedback(new TranslationTextComponent(TextFormatting.BLUE + "[Custom Quests]" + TextFormatting.RESET + " Successfully deleted all parties"), false);
                            Ref.ALL_QUESTING_PARTIES = new ArrayList<>();
                            Ref.shouldSaveNextTick = true;
                            return 0;
                        })
                )
                .executes(context -> {
                    Ref.ALL_QUESTING_PARTIES.forEach(party -> context.getSource().sendFeedback(new TranslationTextComponent(TextFormatting.BLUE + "[Custom Quests DEBUG] " + TextFormatting.RESET + "PartyID: " + party.getId() + " - PartyName: " + party.getPartyName() + " - CreatorUuid: " + party.getCreator()), false));
                    return 0;
                })
        ;
    }
}
