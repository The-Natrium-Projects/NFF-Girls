package net.sodiumzh.nff.girls.item.bauble;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.sodiumzh.nfu.item.bauble.BaubleAttributeModifier;
import net.sodiumzh.nfu.item.bauble.BaubleBehavior;
import net.sodiumzh.nfu.item.bauble.BaubleEquippingCondition;
import net.sodiumzh.nfu.item.bauble.BaubleProcessingArgs;
import net.sodiumzh.nfu.util.NFUInfoStatics;
import net.sodiumzh.nfu.util.NFUMathStatics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Base class for NFF-Girls baubles that are attached to an existing {@link Item} (or a set of items
 * matched by a predicate), as opposed to being a dedicated item class. It implements
 * {@link INFFGirlsBauble} on top of NFU-Library's {@link BaubleBehavior}, delegating attribute
 * modifiers, tick behavior and tooltips to an attached {@link NFFGirlsBaubleProperties} instance.
 */
public abstract class NFFGirlsBaubleBehavior extends BaubleBehavior implements INFFGirlsBauble {

    private ResourceLocation categoryKey;
    private int tier = 1;
    private Map<String, Predicate<Integer>> tags = new HashMap<>();
    private List<Supplier<? extends Component>> tooltips = new ArrayList<>();
    @Nonnull    // No need to lazy load here because it doesn't involve item registration
    private final NFFGirlsBaubleProperties properties;

    protected NFFGirlsBaubleBehavior(@Nullable Item item, @Nullable BiPredicate<Item, ItemStack> multiItemCondition,
                                     BaubleEquippingCondition equippingCondition, ResourceLocation categoryKey, int tier,
                                     @NotNull NFFGirlsBaubleProperties properties) { 
        super(getBaubleRegistryKey(categoryKey, tier), item, multiItemCondition, equippingCondition);
        this.categoryKey = categoryKey;
        this.tier = tier;
        this.properties = properties;
        this.finalizeConstruction();
    }

    /**
     * Attach this behavior to a single existing item.
     * @param item Item that should take this behavior.
     * @param equippingCondition Condition deciding whether a slot can equip this bauble.
     * @param categoryKey Category key of this bauble.
     * @param tier Tier of this bauble under the category key.
     * @param properties Properties describing this bauble's effects.
     */
    public NFFGirlsBaubleBehavior(@NotNull Item item, BaubleEquippingCondition equippingCondition, ResourceLocation categoryKey, int tier, @NotNull NFFGirlsBaubleProperties properties) {
        super(item, getBaubleRegistryKey(categoryKey, tier), equippingCondition);
        this.categoryKey = categoryKey;
        this.tier = tier;
        this.properties = properties;
        this.finalizeConstruction();
    }

    /**
     * Attach this behavior to every item matched by the given condition.
     * @param condition Condition matching (item, item stack) pairs that should take this behavior.
     * @param equippingCondition Condition deciding whether a slot can equip this bauble.
     * @param categoryKey Category key of this bauble.
     * @param tier Tier of this bauble under the category key.
     * @param properties Properties describing this bauble's effects.
     */
    public NFFGirlsBaubleBehavior(@NotNull BiPredicate<Item, ItemStack> condition, BaubleEquippingCondition equippingCondition, ResourceLocation categoryKey, int tier, @NotNull NFFGirlsBaubleProperties properties) {
        super(condition, getBaubleRegistryKey(categoryKey, tier), equippingCondition);
        this.categoryKey = categoryKey;
        this.tier = tier;
        this.properties = properties;
        this.finalizeConstruction();
    }
    
    /**
     * Get the tier suffix (e.g. {@code "_ii"}) appended to the category key to build the registry key
     * for this instance's {@link #getTier()}. Empty for tier 1.
     */
    public String getTierSuffix()
    {
        if (tier == 1)
            return "";
        return "_" + NFUMathStatics.intToRoman(tier).toLowerCase();
    }

    /**
     * Get the tier suffix (e.g. {@code "_ii"}) appended to the category key to build the registry key
     * for the given tier. Empty for tier 1.
     */
    public static String getTierSuffix(int tier)
    {
        if (tier == 1)
            return "";
        return "_" + NFUMathStatics.intToRoman(tier).toLowerCase();
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
     * @return The bauble tags currently active for this instance's tier.
     */
    @Override
    public final List<String> getBaubleTags() {
        return tags.entrySet().stream().filter(entry -> entry.getValue().test(this.getTier()))
            .map(Map.Entry::getKey).toList();
    }

    /**
     * Register a bauble tag under a condition on the tier: it is considered present only when
     * {@code tierCondition} matches {@link #getTier()}.
     * @return this.
     */
    public NFFGirlsBaubleBehavior addBaubleTag(String tag, Predicate<Integer> tierCondition) {
        this.tags.put(tag, tierCondition);
        return this;
    }

    /**
     * Register a bauble tag unconditionally, present at every tier.
     * @return this.
     */
    public NFFGirlsBaubleBehavior addBaubleTag(String tag) {
        return this.addBaubleTag(tag, i -> true);
    }

    /**
     * Register several bauble tags unconditionally, present at every tier.
     * @return this.
     */
    public NFFGirlsBaubleBehavior addBaubleTags(String... tags) {
        for (String tag: tags) {
            this.tags.put(tag, i -> true);
        }
        return this;
    }

    /**
     * Change the category key of this bauble.
     * @return this.
     */
    public NFFGirlsBaubleBehavior setCategoryKey(ResourceLocation categoryKey) {
        this.categoryKey = categoryKey;
        return this;
    }

    /**
     * Register an additional tooltip line supplier for this bauble.
     * @return this.
     */
    public NFFGirlsBaubleBehavior addTooltip(Supplier<? extends Component> tooltip) {
        this.tooltips.add(tooltip);
        return this;
    }

    /**
     * Get all registered tooltip line suppliers, including the default ones added by
     * {@link #finalizeConstruction()}.
     */
    public List<Supplier<? extends Component>> getTooltips() {
        return this.tooltips;
    }

    // Properties related //
    
    /**
     * @return The {@link NFFGirlsBaubleProperties} attached to this behavior, driving its effects.
     */
    @Override
    public NFFGirlsBaubleProperties getProperties() {
        return this.properties;
    }

    /**
     * No-op by default; the equip-time effect is handled via {@link #properties}'s attribute modifiers.
     */
    @Override
    public void onEquipped(BaubleProcessingArgs baubleProcessingArgs) {
    }

    /**
     * No-op by default.
     */
    @Override
    public void preSlotTick(BaubleProcessingArgs baubleProcessingArgs) {
    }

    /**
     * No-op by default.
     */
    @Override
    public void postSlotTick(BaubleProcessingArgs baubleProcessingArgs) {
    }

    /**
     * Delegates to {@link NFFGirlsBaubleProperties#getTickAction()}.
     */
    @Override
    public void slotTick(BaubleProcessingArgs baubleProcessingArgs) {
        this.properties.getTickAction().accept(baubleProcessingArgs);
    }

    /**
     * Delegates to {@link NFFGirlsBaubleProperties#getRepeatableModifierSuppliers()}.
     */
    @Nullable
    @Override
    public BaubleAttributeModifier[] getRepeatableModifiers(BaubleProcessingArgs baubleProcessingArgs) {
        return this.properties.getRepeatableModifierSuppliers().keySet().stream()
            .map(Supplier::get).toArray(BaubleAttributeModifier[]::new);
    }

    /**
     * Delegates to {@link NFFGirlsBaubleProperties#getUnrepeatableModifierSuppliers()}.
     */
    @Nullable
    @Override
    public BaubleAttributeModifier[] getUnrepeatableModifiers(Mob mob) {
        return this.properties.getUnrepeatableModifierSuppliers().keySet().stream()
            .map(Supplier::get).toArray(BaubleAttributeModifier[]::new);
    }

    protected void finalizeConstruction() {
        this.addTooltip(() -> NFUInfoStatics.createTranslatable("tooltip.nffgirls.bauble.existing_item"));
        if (this.properties.shouldShowRarityTier())
            this.properties.getRarityTierDesc().ifPresent(m -> this.addTooltip(() -> m));
        this.properties.getTooltips().forEach(this::addTooltip);
        if (this.properties.environmentImmune && !this.properties.isEnvironmentImmunityTooltipManuallyAdded)
            this.addTooltip(() -> NFUInfoStatics.createTranslatable("tooltip.nffgirls.bauble.environment_immunity").withStyle(ChatFormatting.WHITE));
    }

}
