package net.sodiumzh.nff.girls.subsystem.baublesystem;

import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.sodiumzh.nautils.containers.MapPair;
import net.sodiumzh.nautils.statics.NaUtilsContainerStatics;
import net.sodiumzh.nff.services.subsystem.baublesystem.BaubleSystem;

public class NFFGirlsBaubleStatics
{
	
	private static final Map<Integer, String> ROMAN_NUMERALS = NaUtilsContainerStatics.mapOf(
			MapPair.of(0, "0"),
			MapPair.of(1, "i"),
			MapPair.of(2, "ii"),
			MapPair.of(3, "iii"),
			MapPair.of(4, "iv"),
			MapPair.of(5, "v"),
			MapPair.of(6, "vi"),
			MapPair.of(7, "vii"),
			MapPair.of(8, "viii"),
			MapPair.of(9, "ix"),
			MapPair.of(10, "x")
			);
	
	/**
	 * Count how many bauble of given key the mob has within the tier range.
	 */
	public static int countBaublesWithTierRange(Mob mob, ResourceLocation key, int minTier, int maxTierExcluding)
	{
		var equipped = BaubleSystem.getAllSlotItems(mob).values();
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
	 * Count how many bauble of given key the mob has, despite the tier.
	 */
	public static int countBaubles(Mob mob, ResourceLocation key)
	{
		return countBaublesWithMinTier(mob, key, 0); 
	}
	
	/**
	 * Get a value as Roman numerals.
	 * Supports 0-10 now.
	 */
	@Nullable
	public static String getRomanNumeral(int value, boolean isUpperCase)
	{
		if (value > 10 || value < 0)
			throw new UnsupportedOperationException("getRomanNumeral support only 0-10.");
		String out = ROMAN_NUMERALS.get(value);
		if (isUpperCase) out = out.toUpperCase();
		return out;
	}
	
}
