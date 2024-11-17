package net.sodiumzh.nff.girls.item;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.sodiumzh.nautils.item.NaUtilsItem;
import net.sodiumzh.nautils.statics.NaUtilsInfoStatics;
import net.sodiumzh.nautils.statics.NaUtilsMiscStatics;
import net.sodiumzh.nautils.statics.NaUtilsNBTStatics;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.registry.NFFGirlsCapabilities;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;

public class TradeIntroductionLetterItem extends NaUtilsItem
{

	public TradeIntroductionLetterItem(Properties pProperties)
	{
		super(pProperties);
		this.foilCondition(i -> this.isValid(i));
	}
	
	public void write(ItemStack stack, INFFTamed writer)
	{
		stack.getOrCreateTag();
		stack.getTag().putUUID("writer_id", writer.getIdentifier());
		stack.getTag().putString("writer_name", writer.asMob().getName().getString());
		stack.getTag().putUUID("player_uuid", writer.getOwnerUUID());
		stack.getTag().putString("player_name", writer.getData().getOwnerName());
	}
	
	public boolean isValid(ItemStack stack)
	{
		return stack.hasTag() 
				&& stack.getTag().hasUUID("writer_id") 
				&& stack.getTag().contains("writer_name", NaUtilsNBTStatics.TAG_STRING_ID) 
				&& stack.getTag().hasUUID("player_uuid")
				&& stack.getTag().contains("player_name", NaUtilsNBTStatics.TAG_STRING_ID);
	}
	
	public UUID getWriterId(ItemStack stack)
	{
		return stack.getOrCreateTag().getUUID("writer_id");
	}
	
	public String getWriterName(ItemStack stack)
	{
		return stack.getOrCreateTag().getString("writer_name");
	}
	
	public UUID getPlayerUUID(ItemStack stack)
	{
		return stack.getOrCreateTag().getUUID("player_uuid");
	}
	
	public String getPlayerName(ItemStack stack)
	{
		return stack.getOrCreateTag().getString("player_name");
	}
	
	@Override
	public InteractionResult interactLivingEntity(Player player, LivingEntity living, InteractionHand usedHand)
	{
		ItemStack stack = player.getItemInHand(usedHand);
		if (INFFGirlsTamed.isBM(living) 
				&& living.getCapability(NFFGirlsCapabilities.CAP_TRADE_HANDLER).isPresent()
				&& INFFGirlsTamed.getBM(living).getOwnerUUID().equals(player.getUUID()))
		{
			if (!player.level().isClientSide)
			{
				UUID targetId = INFFGirlsTamed.getBM(living).getIdentifier();
				if (this.isValid(stack) && this.getPlayerUUID(stack).equals(player.getUUID()))
				{
					if (this.getWriterId(stack).equals(targetId))
					{
						NaUtilsMiscStatics.printToScreen(NaUtilsInfoStatics.createTranslatable("info.nffgirls.introduction_using_to_self"), player);
					}
					else
					{
						living.getCapability(NFFGirlsCapabilities.CAP_TRADE_HANDLER).ifPresent(c -> c.regenerateTrades(true));
						NaUtilsMiscStatics.printToScreen(NaUtilsInfoStatics.createTranslatable("info.nffgirls.introduction_used", living.getName().getString()), player);
						if (!player.isCreative())
							stack.shrink(1);
					}
				}
				else if (!this.isValid(stack))
				{
					this.write(stack, INFFGirlsTamed.getBM(living));
					NaUtilsMiscStatics.printToScreen(NaUtilsInfoStatics.createTranslatable("info.nffgirls.introduction_written", living.getName().getString()), player);
				}
				else return InteractionResult.PASS;
				return InteractionResult.sidedSuccess(player.level().isClientSide);
			}
		}
		return InteractionResult.PASS;
	}
	
	@Override
	public void beforeAddingHoveringDescriptions(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) 
	{
		list.add(NaUtilsInfoStatics.createTranslatable("info.nffgirls.introduction_letter_desc").withStyle(ChatFormatting.DARK_GRAY));
		if (this.isValid(stack))
		{
			list.add(NaUtilsInfoStatics.createTranslatable("info.nffgirls.introduction_letter_writer", this.getWriterName(stack)).withStyle(ChatFormatting.DARK_GRAY));
			list.add(NaUtilsInfoStatics.createTranslatable("info.nffgirls.introduction_letter_player", this.getPlayerName(stack)).withStyle(ChatFormatting.DARK_GRAY));
		}
		else
		{
			list.add(NaUtilsInfoStatics.createTranslatable("info.nffgirls.introduction_letter_invalid").withStyle(ChatFormatting.DARK_GRAY));
		}
	}
}
