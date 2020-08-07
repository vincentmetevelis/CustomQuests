package com.vincentmet.customquests.items;

import com.vincentmet.customquests.lib.Utils;
import com.vincentmet.customquests.screens.ScreenQuestingDevice;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.*;

public class ItemQuestingDevice extends Item{
    public ItemQuestingDevice(Item.Properties properties){
        super(properties);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new StringTextComponent(TextFormatting.BLUE + "").append(Utils.createTextComponent(".item_questing_device.tooltip0")));
        tooltip.add(new StringTextComponent(TextFormatting.BLUE + "").append(Utils.createTextComponent(".item_questing_device.tooltip1")));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand){
        if(world.isRemote) Minecraft.getInstance().displayGuiScreen(new ScreenQuestingDevice());
        return super.onItemRightClick(world, player, hand);
    }
}