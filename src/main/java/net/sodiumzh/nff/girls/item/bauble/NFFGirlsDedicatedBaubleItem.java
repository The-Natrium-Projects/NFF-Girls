package net.sodiumzh.nff.girls.item.bauble;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.sodiumzh.nfu.item.bauble.DedicatedBaubleItem;
import net.sodiumzh.nfu.util.NFUMathStatics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public abstract class NFFGirlsDedicatedBaubleItem extends DedicatedBaubleItem implements INFFGirlsBauble
{

	private ResourceLocation categoryKey;
	private int tier;
	private Map<String, Predicate<Integer>> tags = new HashMap<>();
	
	/** additionalKey uses ResourceLocation format. */
	public NFFGirlsDedicatedBaubleItem(ResourceLocation categoryKey, int tier, Item.Properties pProperties)
	{
		super(pProperties);
		if (tier <= 0)
			throw new IllegalArgumentException("NFFGirlsDedicatedBaubleItem tier must be positive (not supporting 0).");
		this.categoryKey = categoryKey;
		this.tier = tier;
	}

	public String getTierSuffix()
	{
		if (tier == 1)
			return "";
		return "_" + NFUMathStatics.intToRoman(tier).toLowerCase();
	}
	
	@Override
	public final ResourceLocation getBaubleRegistryKey()
	{
		ResourceLocation unsuffixed = getCategoryKey();
		return new ResourceLocation(unsuffixed.getNamespace(), unsuffixed.getPath() + getTierSuffix());
	}
	
	public final ResourceLocation getCategoryKey() {
		return this.categoryKey;
	}

	@Override
	public final int getTier() {
		return this.tier;
	}

	public NFFGirlsDedicatedBaubleItem setCategoryKey(ResourceLocation categoryKey) {
		this.categoryKey = categoryKey;
		return this;
	}

	@Override
	public final List<String> getBaubleTags() {
		return tags.entrySet().stream().filter(entry -> entry.getValue().test(this.getTier()))
			.map(Map.Entry::getKey).toList();
	}

	public NFFGirlsDedicatedBaubleItem addBaubleTag(String tag, Predicate<Integer> tierCondition) {
		this.tags.put(tag, tierCondition);
		return this;
	}

	public NFFGirlsDedicatedBaubleItem addBaubleTag(String tag) {
		return this.addBaubleTag(tag, i -> true);
	}

	public NFFGirlsDedicatedBaubleItem addBaubleTags(String... tags) {
		for (String tag: tags) {
			this.tags.put(tag, i -> true);
		}
		return this;
	}
}
