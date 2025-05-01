package net.sodiumzh.nff.girls.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.sodiumzh.nff.services.item.NFFMobOwnershipTransfererItem;
import net.sodiumzh.nfu.util.NFUInfoStatics;

import javax.annotation.Nullable;
import java.util.List;

public class TransferringTagItem extends NFFMobOwnershipTransfererItem
{

	public TransferringTagItem(Properties pProperties)
	{
		super(pProperties);
		this.foilCondition(s -> this.isWritten(s));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag)
	{
		super.appendHoverText(stack, level, list, tooltipFlag);
		if (this.isWritten(stack))
		{
			list.add(NFUInfoStatics.createTranslatable("info.nffgirls.item.transferring_tag_mob", this.getMobName(stack)).withStyle(ChatFormatting.GRAY));
			list.add(NFUInfoStatics.createTranslatable("info.nffgirls.item.transferring_tag_owner", this.getOldOwnerName(stack)).withStyle(ChatFormatting.GRAY));
			if (this.isLocked(stack))
			{
				list.add(NFUInfoStatics.createTranslatable("info.nffgirls.item.transferring_tag_locked").withStyle(ChatFormatting.GRAY));
			}
		}
	}
	
}
