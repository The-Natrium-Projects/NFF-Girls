package net.sodiumzh.nff.girls.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.sodiumzh.nfu.annotation.DontOverride;
import net.sodiumzh.nfu.util.NFUInfoStatics;
import net.sodiumzh.nfu.util.NFUNBTStatics;

/**
 * Interface of items with a duration but not using the vanilla item damage system, thus cannot be repaired with Mending or regarded as damageable items.
 * The item will not vanish when duration runs out.
 */
public interface IWithDuration {
	
	
	public int getMaxDuration();

	@DontOverride
	public default int getDuration(ItemStack stack)
	{
		if (stack.getItem() == null || !(stack.getItem() instanceof IWithDuration))
		{
			throw new IllegalArgumentException("Wrong item type");
		}
		if (!stack.getOrCreateTag().contains("IWithDuration_duration", NFUNBTStatics.TAG_INT_ID))
		{
			stack.getOrCreateTag().putInt("IWithDuration_duration", getMaxDuration());
			return getMaxDuration();
		}
		else return stack.getOrCreateTag().getInt("IWithDuration_duration");
	}
	
	@DontOverride
	public default boolean consumeDuration(ItemStack stack, int amount)
	{
		int duration = getDuration(stack);
		if (duration < amount)
			return false;
		else
		{
			stack.getOrCreateTag().putInt("IWithDuration_duration", duration - amount);
			return true;
		}
	}
	
	@DontOverride
	public default boolean isMaxDuration(ItemStack stack)
	{
		return getDuration(stack) == getMaxDuration();
	}
	
	public default boolean canRepair(ItemStack stack)
	{
		return !isMaxDuration(stack);
	}
	
	@DontOverride
	public default boolean repair(ItemStack stack, int amount)
	{
		if (canRepair(stack))
		{
			stack.getOrCreateTag().putInt("IWithDuration_duration", Math.min(getDuration(stack) + amount, getMaxDuration()));
			return true;
		}
		else return false;
	}
	
	public default MutableComponent getDurationDescription(ItemStack stack)
	{
		return NFUInfoStatics.createTranslatable("info.nffgirls.item.duration", Integer.toString(getDuration(stack)), Integer.toString(getMaxDuration())).withStyle(ChatFormatting.GRAY);
	}
}
