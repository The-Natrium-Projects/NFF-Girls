package net.sodiumzh.nff.girls.item.bauble;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.sodiumzh.nfu.item.bauble.*;
import net.sodiumzh.nfu.util.NFUMathStatics;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Base class for NFF-Girls baubles that are their own dedicated {@link Item} class, implementing
 * {@link INFFGirlsBauble} on top of NFU-Library's {@link DedicatedBaubleItem}. The bauble's
 * {@link NFFGirlsBaubleProperties} are lazily loaded (since items are registered in the Forge registry
 * phase, while properties are only available in the NFU common-setup registry phase).
 */
public class NFFGirlsDedicatedBaubleItem extends DedicatedBaubleItem implements INFFGirlsBauble
{
	private final int rarityTier;
    // For lazy-loading properties. Items are registered on Forge registry phase,
    // and properties are registered on server startup phase
    private final Supplier<NFFGirlsBaubleProperties> propertiesProvider;
    // Should be loaded/reloaded on server startup phase
	@Nullable
    private NFFGirlsBaubleProperties propertiesInstance = null;

	/** additionalKey uses ResourceLocation format. */
	public NFFGirlsDedicatedBaubleItem(
		ResourceLocation categoryKey,
		int tier,
		int rarityTier,
		Item.Properties pProperties,
		Supplier<NFFGirlsBaubleProperties> propertiesProvider)
	{
		super(pProperties);
		if (tier <= 0)
			throw new IllegalArgumentException("NFFGirlsDedicatedBaubleItem tier must be positive (not supporting 0).");
		this.metaData.put("categoryKey", categoryKey);
		this.metaData.put("tier", tier);
		this.rarityTier = rarityTier;
        this.propertiesProvider = propertiesProvider;
	}

	/**
	 * Get the tier suffix (e.g. {@code "_ii"}) appended to the category name to build the registry name
	 * for this instance's {@link #getTier()}. Empty for tier 1.
	 */
	public String getTierSuffix()
	{
		if (this.getTier() == 1)
			return "";
		return "_" + NFUMathStatics.intToRoman(this.getTier()).toLowerCase(Locale.ENGLISH);
	}

	/**
	 * Get the tier suffix (e.g. {@code "_ii"}) appended to the category name to build the registry name
	 * for the given tier. Empty for tier 1.
	 */
	public static String getTierSuffix(int tier)
	{
		if (tier == 1)
			return "";
		return "_" + NFUMathStatics.intToRoman(tier).toLowerCase(Locale.ENGLISH);
	}

	public int getRarityTier() {
		return this.rarityTier;
	}

    @Override
    public NFFGirlsBaubleProperties getProperties() {
		if (this.propertiesInstance == null)
			throw new IllegalStateException("NFFGirlsDedicatedBaubleItem not loaded.");
        return this.propertiesInstance;
    }

	/**
	 * Only invoked on server startup, refresh bauble properties.
	 */
    public void loadProperties() {
		this.propertiesInstance = propertiesProvider.get();
    }

    // IBaubleRegistryEntry interface //
    @Override
    public void slotTick(BaubleProcessingArgs args) {
        this.getProperties().getTickActionProperties(args)
			.forEach(prop -> prop.action().accept(args));
    }

    @Override
    public BaubleAttributeModifier[] getRepeatableModifiers(BaubleProcessingArgs baubleProcessingArgs) {
        return this.getProperties().getModifierProperties(baubleProcessingArgs).stream()
			.filter(NFFGirlsBaubleProperties.ModifierProperty::repeatable)
            .map(NFFGirlsBaubleProperties.ModifierProperty::createModifier)
			.toArray(BaubleAttributeModifier[]::new);
    }

    @Override
    public BaubleAttributeModifier[] getUnrepeatableModifiers(Mob mob) {
		BaubleProcessingArgs args = new BaubleProcessingArgs(null, mob, null);
		return this.getProperties().getModifierProperties(args).stream()
			.filter(NFFGirlsBaubleProperties.ModifierProperty::repeatable)
			.map(NFFGirlsBaubleProperties.ModifierProperty::createModifier)
			.toArray(BaubleAttributeModifier[]::new);
    }

	@Override
	public ResourceLocation getBaubleRegistryKey() {
		return null;
	}

	/**
     * Delegates to {@link NFFGirlsBaubleProperties}'s configured equipping condition, defaulting to
     * {@code BaubleEquippingConditions.CONDITION_ALWAYS} if none was set.
     */
    @Nonnull
    @Override
    public BaubleEquippingCondition getEquippingCondition() {
        return Optional.ofNullable(this.getProperties().getEquippingCondition())
			.orElse(BaubleEquippingConditions.CONDITION_ALWAYS.get());
    }

	public static ResourceLocation getBaubleRegistryKey(ResourceLocation categoryKey, int tier)
	{
		return new ResourceLocation(categoryKey.getNamespace(), categoryKey.getPath() + getTierSuffix(tier));
	}

    // NFUItem interface //


}
