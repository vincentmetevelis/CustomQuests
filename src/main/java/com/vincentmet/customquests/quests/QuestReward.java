package com.vincentmet.customquests.quests;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.Blocks;
import net.minecraft.command.CommandSource;
import net.minecraft.command.ICommandSource;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.dimension.DimensionType;

public class QuestReward {
    private QuestRewardType type;
    private IQuestReward reward;

    public QuestReward(QuestRewardType type, IQuestReward reward){
        this.type = type;
        this.reward = reward;
    }

    public QuestRewardType getType() {
        return type;
    }

    public IQuestReward getReward() {
        return reward;
    }

    public static class ReceiveItems implements IQuestReward{
        private ItemStack items;
        public ReceiveItems(ItemStack items){
            this.items = items;
        }

        @Override
        public void executeReward(PlayerEntity player) {
            player.inventory.addItemStackToInventory(items);
        }

        @Override
        public String toString() {
            return items.toString();
        }

        @Override
        public ItemStack getItemStack() {
            return items;
        }


    }

    public static class Command implements IQuestReward{
        private String command;
        public Command(String command){
            this.command = command;
        }

        @Override
        public void executeReward(PlayerEntity player) {
            final CommandDispatcher<CommandSource> dispatcher = new CommandDispatcher<>();
            try {
                dispatcher.execute(command, new CommandSource(ICommandSource.field_213139_a_, player.getPositionVec(), player.getPitchYaw(), player.world.getServer().getWorld(player.dimension), 3, player.getDisplayName().getString(), player.getDisplayName(), null, player));
            } catch (CommandSyntaxException e) {
                e.printStackTrace();
            }
        }

        @Override
        public String toString() {
            return command;
        }

        @Override
        public ItemStack getItemStack() {
            return new ItemStack(Blocks.COMMAND_BLOCK);
        }
    }

    public static class SpawnEntity implements IQuestReward{
        private EntityType entity;
        public SpawnEntity(EntityType entity){
            this.entity = entity;
        }

        @Override
        public void executeReward(PlayerEntity player) {
            player.getEntityWorld().getServer().getWorld(player.dimension).summonEntity(entity.create(player.getEntityWorld()));
        }

        @Override
        public String toString() {
            return entity.toString();
        }

        @Override
        public ItemStack getItemStack() {
            return new ItemStack(Items.DIAMOND_SWORD);
        }
    }
}