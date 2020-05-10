package com.vincentmet.customquests.quests.quest;

import com.google.gson.JsonObject;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.quests.IJsonProvider;
import com.vincentmet.customquests.quests.IQuestReward;
import net.minecraft.block.Blocks;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistries;

public class QuestReward implements IJsonProvider {
    private QuestRewardType type;
    private IQuestReward reward;

    public QuestReward(QuestRewardType type, IQuestReward reward){
        this.type = type;
        this.reward = reward;
    }

    public JsonObject getJson(){
        JsonObject json = new JsonObject();
        switch(type){
            case ITEMS:
                json.addProperty("type", "ITEMS");
                break;
            case SPAWN_ENTITY:
                json.addProperty("type", "SPAWN_ENTITY");
                break;
            case COMMAND:
                json.addProperty("type", "COMMAND");
                break;
        }
        json.add("content", reward.getJson());
        return json;
    }

    public QuestRewardType getType() {
        return type;
    }

    public IQuestReward getReward() {
        return reward;
    }

    public void setType(QuestRewardType type) {
        this.type = type;
        Ref.shouldSaveNextTick = true;
    }

    public void setReward(IQuestReward reward) {
        this.reward = reward;
        Ref.shouldSaveNextTick = true;
    }

    public static class ReceiveItems implements IQuestReward{
        private ItemStack itemStack;
        public ReceiveItems(ItemStack item){
            this.itemStack = item;
        }

        @Override
        public JsonObject getJson(){
            JsonObject json = new JsonObject();
            json.addProperty("item", ForgeRegistries.ITEMS.getKey(itemStack.getItem()).toString());
            json.addProperty("amount", itemStack.getCount());
            json.addProperty("nbt", itemStack.getTag()==null?"{}":itemStack.getTag().toString());
            return json;
        }

        @Override
        public void executeReward(PlayerEntity player) {
            player.inventory.addItemStackToInventory(itemStack.copy());
        }

        @Override
        public String toString() {
            return itemStack.getCount() + "x " + itemStack.getItem().getName().getString();
        }

        @Override
        public ItemStack getItemStack() {
            return itemStack;
        }

        public void setItem(ItemStack item) {
            this.itemStack = item;
            Ref.shouldSaveNextTick = true;
        }
    }

    public static class Command implements IQuestReward{
        private String command;
        public Command(String command){
            this.command = command;
        }

        @Override
        public JsonObject getJson() {
            JsonObject json = new JsonObject();
            json.addProperty("command", command);
            return json;
        }

        @Override
        public void executeReward(PlayerEntity player) {
            final CommandDispatcher<CommandSource> dispatcher = player.getServer().getCommandManager().getDispatcher();
            try {
                dispatcher.execute("execute at " + player.getDisplayName().getString() + " run " + command, player.getServer().getCommandSource().withFeedbackDisabled());
            } catch (CommandSyntaxException e) {
                e.printStackTrace();
            }
        }

        @Override
        public String toString() {
            return "/" + command;
        }

        @Override
        public ItemStack getItemStack() {
            return new ItemStack(Blocks.COMMAND_BLOCK);
        }

        public void setCommand(String command) {
            this.command = command;
            Ref.shouldSaveNextTick = true;
        }
    }

    public static class SpawnEntity implements IQuestReward{
        private EntityType entity;
        private int amount;
        public SpawnEntity(EntityType entity, int amount){
            this.entity = entity;
            this.amount = amount;
        }

        @Override
        public JsonObject getJson() {
            JsonObject json = new JsonObject();
            json.addProperty("entity", ForgeRegistries.ENTITIES.getKey(entity).toString());
            json.addProperty("amount", amount);
            return json;
        }

        @Override
        public void executeReward(PlayerEntity player) {
            for(int i=0; i<amount;i++)player.getEntityWorld().getWorld().getServer().getWorld(player.dimension).summonEntity(entity.create(player.world, new CompoundNBT(), new TranslationTextComponent("Your Reward <3"), player, player.getPosition(), SpawnReason.COMMAND, true, false));
        }

        @Override
        public String toString() {
            return "Summon " + amount + "x " + entity.getName().getString();
        }

        @Override
        public ItemStack getItemStack() {
            return new ItemStack(Items.DIAMOND_SWORD);
        }

        public void setEntity(EntityType entity) {
            this.entity = entity;
            Ref.shouldSaveNextTick = true;
        }

        public void setAmount(int amount) {
            this.amount = amount;
            Ref.shouldSaveNextTick = true;
        }
    }

    public static class GiveXP implements IQuestReward{
        private int amount;
        public GiveXP(int amount){
            this.amount = amount;
        }

        @Override
        public void executeReward(PlayerEntity player) {
            player.giveExperiencePoints(amount);
        }

        @Override
        public ItemStack getItemStack() {
            return new ItemStack(Items.EXPERIENCE_BOTTLE);
        }

        @Override
        public JsonObject getJson() {
            JsonObject json = new JsonObject();
            json.addProperty("amount", amount);
            return json;
        }

        @Override
        public String toString() {
            return "Receive " + amount + " Experience points";
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }
    }
}