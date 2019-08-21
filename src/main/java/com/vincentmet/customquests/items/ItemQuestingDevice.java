package com.vincentmet.customquests.items;

import com.vincentmet.customquests.screens.ScreenQuestingDevice;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemQuestingDevice extends Item{
    public ItemQuestingDevice(Item.Properties properties){
        super(properties);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("This is your questing device, take good care of it"));
        tooltip.add(new TranslationTextComponent("Rightclick to open your Questing GUI"));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand){
        if (!world.isRemote){
            Minecraft.getInstance().displayGuiScreen(new ScreenQuestingDevice(new TranslationTextComponent("title")));
        }
        return super.onItemRightClick(world, player, hand);
    }
}