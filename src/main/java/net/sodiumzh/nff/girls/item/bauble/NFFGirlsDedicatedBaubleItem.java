package net.sodiumzh.nff.girls.item.bauble;

import com.google.common.base.Suppliers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
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

	private ResourceLocation categoryKey;
	private final int tier;
	private final Map<String, Predicate<Integer>> tags = new HashMap<>();
    // For lazy-loading properties. Items are registered in Forge registry phase,
    // and properties are registered in common setup phase (NFU Registry).
    private final Supplier<NFFGirlsBaubleProperties> propertiesProvider;
    @Nullable
    private NFFGirlsBaubleProperties propertiesInstance = null;

	/** additionalKey uses ResourceLocation format. */
	public NFFGirlsDedicatedBaubleItem(ResourceLocation categoryKey, int tier, Item.Properties pProperties, Supplier<NFFGirlsBaubleProperties> propertiesProvider)
	{
		super(pProperties);
		if (tier <= 0)
			throw new IllegalArgumentException("NFFGirlsDedicatedBaubleItem tier must be positive (not supporting 0).");
		this.categoryKey = categoryKey;
		this.tier = tier;
        this.propertiesProvider = propertiesProvider;
	}

	/**
	 * Get the tier suffix (e.g. {@code "_ii"}) appended to the category key to build the registry key
	 * for this instance's {@link #getTier()}. Empty for tier 1.
	 */
	public String getTierSuffix()
	{
		if (tier == 1)
			return "";
		return "_" + NFUMathStatics.intToRoman(tier).toLowerCase(Locale.ENGLISH);
	}

	/**
	 * Get the tier suffix (e.g. {@code "_ii"}) appended to the category key to build the registry key
	 * for the given tier. Empty for tier 1.
	 */
	public static String getTierSuffix(int tier)
	{
		if (tier == 1)
			return "";
		return "_" + NFUMathStatics.intToRoman(tier).toLowerCase(Locale.ENGLISH);
	}

	/**
	 * @return The NFU bauble registry key, built from {@link #getCategoryKey()} and {@link #getTierSuffix()}.
	 */
	@Override
	public final ResourceLocation getBaubleRegistryKey()
	{
		ResourceLocation unsuffixed = getCategoryKey();
		return new ResourceLocation(unsuffixed.getNamespace(), unsuffixed.getPath() + getTierSuffix());
	}

	/**
	 * Build the NFU bauble registry key from a category key and tier by appending the
	 * {@link #getTierSuffix(int) tier suffix} to the category key's path.
	 */
	public static ResourceLocation getBaubleRegistryKey(ResourceLocation categoryKey, int tier)
	{
		return new ResourceLocation(categoryKey.getNamespace(), categoryKey.getPath() + getTierSuffix(tier));
	}

	/**
	 * @return The category key of this bauble.
	 */
	public final ResourceLocation getCategoryKey() {
		return this.categoryKey;
	}

	/**
	 * @return The tier of this bauble under its category key.
	 */
	@Override
	public final int getTier() {
		return this.tier;
	}

	/**
	 * Change the category key of this bauble.
	 * @return this.
	 */
	public NFFGirlsDedicatedBaubleItem setCategoryKey(ResourceLocation categoryKey) {
		this.categoryKey = categoryKey;
		return this;
	}

	/**
	 * @return The bauble tags currently active for this instance's tier.
	 */
	@Override
	public final List<String> getBaubleTags() {
		return tags.entrySet().stream().filter(entry -> entry.getValue().test(this.getTier()))
			.map(Map.Entry::getKey).toList();
	}

    /**
     * Lazily resolve (and cache) the {@link NFFGirlsBaubleProperties} from the properties provider given
     * at construction, running {@link #onPropertiesLoad(NFFGirlsBaubleProperties)} and validating them on
     * first access.
     */
    @Override
    public NFFGirlsBaubleProperties getProperties() {
        if (this.propertiesInstance == null) {
            this.propertiesInstance = propertiesProvider.get();
            if (this.propertiesInstance == null)
                throw new NullPointerException("NFF Girls Bauble properties supplier returned null.");
            this.onPropertiesLoad(this.propertiesInstance);
            this.propertiesInstance.validate();
        }
        return this.propertiesInstance;
    }

    /**
     * Update self (e.g. bauble tags) from properties on load.
     */
    protected void onPropertiesLoad(@Nonnull NFFGirlsBaubleProperties properties) {
        if (properties.environmentImmune) {
            this.addBaubleTag(INFFGirlsBauble.TAG_ENVIRONMENT_IMMUNITY);
        }
        properties.getBaubleTags().forEach(this::addBaubleTag);
        this.setNameStyle(properties.getNameStyleModifier());
    }

    /**
     * Register a bauble tag under a condition on the tier: it is considered present only when
     * {@code tierCondition} matches {@link #getTier()}.
     * @return this.
     */
    public NFFGirlsDedicatedBaubleItem addBaubleTag(String tag, Predicate<Integer> tierCondition) {
		this.tags.put(tag, tierCondition);
		return this;
	}

	/**
	 * Register a bauble tag unconditionally, present at every tier.
	 * @return this.
	 */
	public NFFGirlsDedicatedBaubleItem addBaubleTag(String tag) {
		return this.addBaubleTag(tag, i -> true);
	}

	/**
	 * Register several bauble tags unconditionally, present at every tier.
	 * @return this.
	 */
	public NFFGirlsDedicatedBaubleItem addBaubleTags(String... tags) {
		for (String tag: tags) {
			this.tags.put(tag, i -> true);
		}
		return this;
	}


    // IBaubleRegistryEntry interface //
    /**
     * Delegates to {@link NFFGirlsBaubleProperties#getTickAction()}.
     */
    @Override
    public void slotTick(BaubleProcessingArgs baubleProcessingArgs) {
        this.getProperties().getTickAction().accept(baubleProcessingArgs);
    }

    /**
     * Delegates to {@link NFFGirlsBaubleProperties#getRepeatableModifierSuppliers()}.
     */
    @Nullable
    @Override
    public BaubleAttributeModifier[] getRepeatableModifiers(BaubleProcessingArgs baubleProcessingArgs) {
        return this.getProperties().getRepeatableModifierSuppliers().keySet().stream()
            .map(Supplier::get).toArray(BaubleAttributeModifier[]::new);
    }

    /**
     * Delegates to {@link NFFGirlsBaubleProperties#getUnrepeatableModifierSuppliers()}.
     * @throws IllegalStateException If the properties builder hasn't been validated yet.
     */
    @Nullable
    @Override
    public BaubleAttributeModifier[] getUnrepeatableModifiers(Mob mob) {
        if (!this.getProperties().isValidated()) throw new IllegalStateException("NFFGirlsBaubleBuilder: Item calling before builder validation.");
        return this.getProperties().getUnrepeatableModifierSuppliers().keySet().stream()
            .map(Supplier::get).toArray(BaubleAttributeModifier[]::new);
    }

    /**
     * Delegates to {@link NFFGirlsBaubleProperties}'s configured equipping condition, defaulting to
     * {@code BaubleEquippingConditions.CONDITION_ALWAYS} if none was set.
     */
    @Nonnull
    @Override
    public BaubleEquippingCondition getEquippingCondition() {
        return Optional.ofNullable(this.getProperties().equippingCondition).orElse(BaubleEquippingConditions.CONDITION_ALWAYS.get());
    }

}
