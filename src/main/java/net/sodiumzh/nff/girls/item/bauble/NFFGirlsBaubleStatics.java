package net.sodiumzh.nff.girls.item.bauble;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.sodiumzh.nfu.item.bauble.NFUBaubleAPI;

import java.util.Collection;
import java.util.function.Predicate;

/**
 * Utility methods for querying how many baubles (from {@link NFFGirlsBaubleAdditionalRegistry}) a mob
 * currently has equipped, filtered by category name, tier, or a custom condition.
 */
public class NFFGirlsBaubleStatics
{

	/**
	 * Count how many bauble of given name the mob has within the tier range.
	 */
	public static int countBaublesWithTierRange(Mob mob, ResourceLocation key, int minTier, int maxTierExcluding)
	{
		Collection<ItemStack> equipped = NFUBaubleAPI.getAllSlotItems(mob).values();
		int count = 0;
		for (ItemStack stack: equipped)
		{
			boolean is = NFUBaubleAPI.getAllRelatedEntries(stack).stream().anyMatch(entry ->
				entry instanceof INFFGirlsBauble bauble
					&& bauble.getCategoryKey().equals(key)
					&& bauble.getTier() >= minTier && bauble.getTier() < maxTierExcluding);
			if (is) count++;
		}
		return count;
	}
	
	/**
	 * Count how many bauble of given name the mob has with the minimum tier.
	 */
	public static int countBaublesWithMinTier(Mob mob, ResourceLocation key, int minTier)
	{
		return countBaublesWithTierRange(mob, key, minTier, Integer.MAX_VALUE);
	}
	
	/**
	 * Count how many bauble of given name the mob has with the exact tier.
	 */
	public static int countBaublesWithTier(Mob mob, ResourceLocation key, int tier)
	{
		return countBaublesWithTierRange(mob, key, tier, tier + 1);
	}

	/**
	 * Count how many bauble of given name the mob has with the exact tier.
	 */
	public static int countBaublesWithCondition(Mob mob, Predicate<ItemStack> condition)
	{
		return NFUBaubleAPI.getAllSlotItems(mob).values().stream().filter(condition).toList().size();
	}

	/**
	 * Count how many bauble of given name the mob has, despite the tier.
	 */
	public static int countBaubles(Mob mob, ResourceLocation key)
	{
		return countBaublesWithMinTier(mob, key, 0); 
	}

}
