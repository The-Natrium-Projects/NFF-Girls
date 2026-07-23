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

    public NFFGirlsBaubleBehavior(@NotNull Item item, BaubleEquippingCondition equippingCondition, ResourceLocation categoryKey, int tier, @NotNull NFFGirlsBaubleProperties properties) {
        super(item, getBaubleRegistryKey(categoryKey, tier), equippingCondition);
        this.categoryKey = categoryKey;
        this.tier = tier;
        this.properties = properties;
        this.finalizeConstruction();
    }

    public NFFGirlsBaubleBehavior(@NotNull BiPredicate<Item, ItemStack> condition, BaubleEquippingCondition equippingCondition, ResourceLocation categoryKey, int tier, @NotNull NFFGirlsBaubleProperties properties) {
        super(condition, getBaubleRegistryKey(categoryKey, tier), equippingCondition);
        this.categoryKey = categoryKey;
        this.tier = tier;
        this.properties = properties;
        this.finalizeConstruction();
    }
    
    public String getTierSuffix()
    {
        if (tier == 1)
            return "";
        return "_" + NFUMathStatics.intToRoman(tier).toLowerCase();
    }

    public static String getTierSuffix(int tier)
    {
        if (tier == 1)
            return "";
        return "_" + NFUMathStatics.intToRoman(tier).toLowerCase();
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

    @Override
    public final List<String> getBaubleTags() {
        return tags.entrySet().stream().filter(entry -> entry.getValue().test(this.getTier()))
            .map(Map.Entry::getKey).toList();
    }

    public NFFGirlsBaubleBehavior addBaubleTag(String tag, Predicate<Integer> tierCondition) {
        this.tags.put(tag, tierCondition);
        return this;
    }

    public NFFGirlsBaubleBehavior addBaubleTag(String tag) {
        return this.addBaubleTag(tag, i -> true);
    }

    public NFFGirlsBaubleBehavior addBaubleTags(String... tags) {
        for (String tag: tags) {
            this.tags.put(tag, i -> true);
        }
        return this;
    }

    public NFFGirlsBaubleBehavior setCategoryKey(ResourceLocation categoryKey) {
        this.categoryKey = categoryKey;
        return this;
    }

    public NFFGirlsBaubleBehavior addTooltip(Supplier<? extends Component> tooltip) {
        this.tooltips.add(tooltip);
        return this;
    }

    public List<Supplier<? extends Component>> getTooltips() {
        return this.tooltips;
    }

    // Properties related //
    
    @Override
    public NFFGirlsBaubleProperties getProperties() {
        return this.properties;
    }

    @Override
    public void onEquipped(BaubleProcessingArgs baubleProcessingArgs) {
    }

    @Override
    public void preSlotTick(BaubleProcessingArgs baubleProcessingArgs) {
    }

    @Override
    public void postSlotTick(BaubleProcessingArgs baubleProcessingArgs) {
    }

    @Override
    public void slotTick(BaubleProcessingArgs baubleProcessingArgs) {
        this.properties.getTickAction().accept(baubleProcessingArgs);
    }

    @Nullable
    @Override
    public BaubleAttributeModifier[] getRepeatableModifiers(BaubleProcessingArgs baubleProcessingArgs) {
        return this.properties.getRepeatableModifierSuppliers().keySet().stream()
            .map(Supplier::get).toArray(BaubleAttributeModifier[]::new);
    }

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
