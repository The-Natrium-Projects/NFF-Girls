package net.sodiumzh.nff.girls.item.bauble;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.sodiumzh.nfu.item.bauble.NFUBaubleAPI;
import net.sodiumzh.nfu.util.NFUContainerStatics;

import javax.annotation.Nullable;
import java.util.AbstractMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Utility methods for querying how many baubles (from {@link NFFGirlsBaubleAdditionalRegistry}) a mob
 * currently has equipped, filtered by category key, tier, or a custom condition.
 */
public class NFFGirlsBaubleStatics
{

	/**
	 * Count how many bauble of given key the mob has within the tier range.
	 */
	public static int countBaublesWithTierRange(Mob mob, ResourceLocation key, int minTier, int maxTierExcluding)
	{
		var equipped = NFUBaubleAPI.getAllSlotItems(mob).values();
		int count = 0;
		for (ItemStack stack: equipped)
		{
			if (stack.getItem() == null) continue;
			var prop = NFFGirlsBaubleAdditionalRegistry.getRegistry().get(stack.getItem());
			if (prop != null && prop.getA().equals(key) && prop.getB() >= minTier && prop.getB() < maxTierExcluding)
				count++;
		}
		return count;
	}
	
	/**
	 * Count how many bauble of given key the mob has with the minimum tier.
	 */
	public static int countBaublesWithMinTier(Mob mob, ResourceLocation key, int minTier)
	{
		return countBaublesWithTierRange(mob, key, minTier, Integer.MAX_VALUE);
	}
	
	/**
	 * Count how many bauble of given key the mob has with the exact tier.
	 */
	public static int countBaublesWithTier(Mob mob, ResourceLocation key, int tier)
	{
		return countBaublesWithTierRange(mob, key, tier, tier + 1);
	}

	/**
	 * Count how many bauble of given key the mob has with the exact tier.
	 */
	public static int countBaublesWithCondition(Mob mob, Predicate<ItemStack> condition)
	{
		return NFUBaubleAPI.getAllSlotItems(mob).values().stream().filter(condition).toList().size();
	}

	/**
	 * Count how many bauble of given key the mob has, despite the tier.
	 */
	public static int countBaubles(Mob mob, ResourceLocation key)
	{
		return countBaublesWithMinTier(mob, key, 0); 
	}
	
}
