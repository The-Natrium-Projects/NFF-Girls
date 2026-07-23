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

	public String getTierSuffix()
	{
		if (tier == 1)
			return "";
		return "_" + NFUMathStatics.intToRoman(tier).toLowerCase(Locale.ENGLISH);
	}

	public static String getTierSuffix(int tier)
	{
		if (tier == 1)
			return "";
		return "_" + NFUMathStatics.intToRoman(tier).toLowerCase(Locale.ENGLISH);
	}

	@Override
	public final ResourceLocation getBaubleRegistryKey()
	{
		ResourceLocation unsuffixed = getCategoryKey();
		return new ResourceLocation(unsuffixed.getNamespace(), unsuffixed.getPath() + getTierSuffix());
	}

	public static ResourceLocation getBaubleRegistryKey(ResourceLocation categoryKey, int tier)
	{
		return new ResourceLocation(categoryKey.getNamespace(), categoryKey.getPath() + getTierSuffix(tier));
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


    // IBaubleRegistryEntry interface //
    @Override
    public void slotTick(BaubleProcessingArgs baubleProcessingArgs) {
        this.getProperties().getTickAction().accept(baubleProcessingArgs);
    }

    @Nullable
    @Override
    public BaubleAttributeModifier[] getRepeatableModifiers(BaubleProcessingArgs baubleProcessingArgs) {
        return this.getProperties().getRepeatableModifierSuppliers().keySet().stream()
            .map(Supplier::get).toArray(BaubleAttributeModifier[]::new);
    }

    @Nullable
    @Override
    public BaubleAttributeModifier[] getUnrepeatableModifiers(Mob mob) {
        if (!this.getProperties().isValidated()) throw new IllegalStateException("NFFGirlsBaubleBuilder: Item calling before builder validation.");
        return this.getProperties().getUnrepeatableModifierSuppliers().keySet().stream()
            .map(Supplier::get).toArray(BaubleAttributeModifier[]::new);
    }

    @Nonnull
    @Override
    public BaubleEquippingCondition getEquippingCondition() {
        return Optional.ofNullable(this.getProperties().equippingCondition).orElse(BaubleEquippingConditions.CONDITION_ALWAYS.get());
    }

}
