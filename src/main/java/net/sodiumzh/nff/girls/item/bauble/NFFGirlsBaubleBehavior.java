package net.sodiumzh.nff.girls.item.bauble;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.sodiumzh.nfu.item.bauble.*;
import net.sodiumzh.nfu.util.NFUInfoStatics;
import net.sodiumzh.nfu.util.NFUMathStatics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Base class for NFF-Girls baubles that are attached to an existing {@link Item} (or a set of items
 * matched by a predicate), as opposed to being a dedicated item class. It implements
 * {@link INFFGirlsBauble} on top of NFU-Library's {@link BaubleBehavior}, delegating attribute
 * modifiers, tick behavior and tooltips to an attached {@link NFFGirlsBaubleProperties} instance.
 */
public class NFFGirlsBaubleBehavior extends BaubleBehavior implements INFFGirlsBauble {

    @Nonnull
    private final NFFGirlsBaubleProperties properties;

    protected NFFGirlsBaubleBehavior(
        @Nullable Item item,
        @Nullable BiPredicate<Item, ItemStack> multiItemCondition,
        ResourceLocation categoryKey,
        int tier,
        @Nonnull NFFGirlsBaubleProperties properties)
    {
        super(getBaubleRegistryKey(categoryKey, tier), item, multiItemCondition,
            Optional.ofNullable(properties.getEquippingCondition()).orElse(BaubleEquippingConditions.CONDITION_ALWAYS.get()));
        this.metaData.put("categoryKey", categoryKey);
        this.metaData.put("tier", tier);
        this.properties = properties;
    }

    /**
     * Attach this behavior to a single existing item.
     * @param item Item that should take this behavior.
     * @param categoryKey Category name of this bauble.
     * @param tier Tier of this bauble under the category name.
     * @param properties Properties describing this bauble's effects.
     */
    public NFFGirlsBaubleBehavior(@NotNull Item item, ResourceLocation categoryKey, int tier, @NotNull NFFGirlsBaubleProperties properties) {
        super(item, getBaubleRegistryKey(categoryKey, tier), properties.getEquippingCondition());
        this.metaData.put("categoryKey", categoryKey);
        this.metaData.put("tier", tier);
        this.properties = properties;
    }

    /**
     * Attach this behavior to every item matched by the given condition.
     * @param condition Condition matching (item, item stack) pairs that should take this behavior.
     * @param categoryKey Category name of this bauble.
     * @param tier Tier of this bauble under the category name.
     * @param properties Properties describing this bauble's effects.
     */
    public NFFGirlsBaubleBehavior(@NotNull BiPredicate<Item, ItemStack> condition, ResourceLocation categoryKey, int tier, @NotNull NFFGirlsBaubleProperties properties) {
        super(condition, getBaubleRegistryKey(categoryKey, tier), properties.getEquippingCondition());
        this.metaData.put("categoryKey", categoryKey);
        this.metaData.put("tier", tier);
        this.properties = properties;
    }
    
    /**
     * Get the tier suffix (e.g. {@code "_ii"}) appended to the category name to build the registry name
     * for this instance's {@link #getTier()}. Empty for tier 1.
     */
    public String getTierSuffix()
    {
        if (getTier() == 1)
            return "";
        return "_" + NFUMathStatics.intToRoman(getTier()).toLowerCase();
    }

    /**
     * Get the tier suffix (e.g. {@code "_ii"}) appended to the category name to build the registry name
     * for the given tier. Empty for tier 1.
     */
    public static String getTierSuffix(int tier)
    {
        if (tier == 1)
            return "";
        return "_" + NFUMathStatics.intToRoman(tier).toLowerCase();
    }

    /**
     * Build the NFU bauble registry name from a category name and tier by appending the
     * {@link #getTierSuffix(int) tier suffix} to the category name's path.
     */
    public static ResourceLocation getBaubleRegistryKey(ResourceLocation categoryKey, int tier)
    {
        return new ResourceLocation(categoryKey.getNamespace(), categoryKey.getPath() + getTierSuffix(tier));
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

    @Override
    public void postSlotTick(BaubleProcessingArgs baubleProcessingArgs) {
    }

    @Override
    public void slotTick(BaubleProcessingArgs baubleProcessingArgs) {
        this.properties.getTickActionProperties(baubleProcessingArgs)
            .forEach(action -> action.action().accept(baubleProcessingArgs));
    }

    @Nullable
    @Override
    public BaubleAttributeModifier[] getRepeatableModifiers(BaubleProcessingArgs baubleProcessingArgs) {
        return this.properties.getModifierProperties(baubleProcessingArgs).stream()
            .filter(NFFGirlsBaubleProperties.ModifierProperty::repeatable)
            .map(NFFGirlsBaubleProperties.ModifierProperty::createModifier)
            .toArray(BaubleAttributeModifier[]::new);
    }

    @Nullable
    @Override
    public BaubleAttributeModifier[] getUnrepeatableModifiers(Mob mob) {
        BaubleProcessingArgs args = new BaubleProcessingArgs(null, mob, null);
        return this.getProperties().getModifierProperties(args).stream()
            .filter(NFFGirlsBaubleProperties.ModifierProperty::repeatable)
            .map(NFFGirlsBaubleProperties.ModifierProperty::createModifier)
            .toArray(BaubleAttributeModifier[]::new);
    }

    @Override
    @Nonnull
    public NFFGirlsBaubleProperties getProperties() {
        return this.properties;
    }
}
