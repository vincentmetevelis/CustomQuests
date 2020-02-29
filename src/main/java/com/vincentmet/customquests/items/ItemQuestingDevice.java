package com.vincentmet.customquests.items;

import com.vincentmet.customquests.lib.Utils;
import com.vincentmet.customquests.screens.ScreenQuestingDevice;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class ItemQuestingDevice extends Item{
    public ItemQuestingDevice(Item.Properties properties){
        super(properties);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        Style blue = new Style().setColor(TextFormatting.BLUE);
        tooltip.add(Utils.createTextComponent(".item_questing_device.tooltip0").setStyle(blue));
        tooltip.add(Utils.createTextComponent(".item_questing_device.tooltip1").setStyle(blue));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand){
        Minecraft.getInstance().displayGuiScreen(new ScreenQuestingDevice());
        return super.onItemRightClick(world, player, hand);
    }
}