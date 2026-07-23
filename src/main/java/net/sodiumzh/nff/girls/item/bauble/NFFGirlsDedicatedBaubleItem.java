package net.sodiumzh.nff.girls.item.bauble;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.sodiumzh.nfu.item.bauble.DedicatedBaubleItem;
import net.sodiumzh.nfu.util.NFUMathStatics;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Base class for items whose own {@link Item} class <em>is</em> the bauble
 * (the NFU "dedicated bauble item" model), extended with the NFF-Girls
 * category-key/tier and bauble-tag concepts from {@link INFFGirlsBauble}.
 * <p>
 * A dedicated bauble's registry key is derived from its category key plus a
 * tier suffix (e.g. category {@code nffgirls:soul_amulet} at tier 2 becomes
 * {@code nffgirls:soul_amulet_ii}). Subclasses supply the concrete attribute
 * modifiers and per-tick behavior inherited from {@link DedicatedBaubleItem}.
 */
public abstract class NFFGirlsDedicatedBaubleItem extends DedicatedBaubleItem implements INFFGirlsBauble
{

	private ResourceLocation categoryKey;
	private int tier;
	private Map<String, Predicate<Integer>> tags = new HashMap<>();

	/**
	 * @param categoryKey the shared category key (ResourceLocation format) for all tiers of this bauble
	 * @param tier        the tier of this bauble; must be positive (0 is not supported)
	 * @param pProperties the item properties
	 * @throws IllegalArgumentException if {@code tier <= 0}
	 */
	public NFFGirlsDedicatedBaubleItem(ResourceLocation categoryKey, int tier, Item.Properties pProperties)
	{
		super(pProperties);
		if (tier <= 0)
			throw new IllegalArgumentException("NFFGirlsDedicatedBaubleItem tier must be positive (not supporting 0).");
		this.categoryKey = categoryKey;
		this.tier = tier;
	}

	/**
	 * @return the tier suffix for this bauble's tier (empty for tier 1, otherwise
	 *         {@code "_" + lowercase Roman numeral}, e.g. {@code "_ii"}).
	 */
	public String getTierSuffix()
	{
		if (tier == 1)
			return "";
		return "_" + NFUMathStatics.intToRoman(tier).toLowerCase(Locale.ENGLISH);
	}

	/**
	 * @param tier the tier to compute a suffix for
	 * @return the tier suffix (empty for tier 1, otherwise {@code "_" + lowercase Roman numeral}).
	 */
	public static String getTierSuffix(int tier)
	{
		if (tier == 1)
			return "";
		return "_" + NFUMathStatics.intToRoman(tier).toLowerCase(Locale.ENGLISH);
	}

	/**
	 * @return the bauble registry key, i.e. the {@link #getCategoryKey() category key}
	 *         with the tier suffix appended.
	 */
	@Override
	public final ResourceLocation getBaubleRegistryKey()
	{
		ResourceLocation unsuffixed = getCategoryKey();
		return new ResourceLocation(unsuffixed.getNamespace(), unsuffixed.getPath() + getTierSuffix());
	}

	/**
	 * @param categoryKey the category key
	 * @param tier        the tier
	 * @return the bauble registry key formed from the category key and tier suffix.
	 */
	public static ResourceLocation getBaubleRegistryKey(ResourceLocation categoryKey, int tier)
	{
		return new ResourceLocation(categoryKey.getNamespace(), categoryKey.getPath() + getTierSuffix(tier));
	}

	/** @return the shared category key of this bauble. */
	public final ResourceLocation getCategoryKey() {
		return this.categoryKey;
	}

	/** @return the tier of this bauble. */
	@Override
	public final int getTier() {
		return this.tier;
	}

	/**
	 * Override the category key of this bauble.
	 * @param categoryKey the new category key
	 * @return {@code this}, for chaining.
	 */
	public NFFGirlsDedicatedBaubleItem setCategoryKey(ResourceLocation categoryKey) {
		this.categoryKey = categoryKey;
		return this;
	}

	/** @return the bauble tags active for this bauble's current tier. */
	@Override
	public final List<String> getBaubleTags() {
		return tags.entrySet().stream().filter(entry -> entry.getValue().test(this.getTier()))
			.map(Map.Entry::getKey).toList();
	}

	/**
	 * Add a bauble tag that is only active when the tier satisfies the given condition.
	 * @param tag           the tag to add
	 * @param tierCondition predicate on the tier deciding whether the tag is active
	 * @return {@code this}, for chaining.
	 */
	public NFFGirlsDedicatedBaubleItem addBaubleTag(String tag, Predicate<Integer> tierCondition) {
		this.tags.put(tag, tierCondition);
		return this;
	}

	/**
	 * Add a bauble tag that is always active regardless of tier.
	 * @param tag the tag to add
	 * @return {@code this}, for chaining.
	 */
	public NFFGirlsDedicatedBaubleItem addBaubleTag(String tag) {
		return this.addBaubleTag(tag, i -> true);
	}

	/**
	 * Add multiple bauble tags that are always active regardless of tier.
	 * @param tags the tags to add
	 * @return {@code this}, for chaining.
	 */
	public NFFGirlsDedicatedBaubleItem addBaubleTags(String... tags) {
		for (String tag: tags) {
			this.tags.put(tag, i -> true);
		}
		return this;
	}

}
