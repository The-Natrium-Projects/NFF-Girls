package net.sodiumzh.nff.girls.item.bauble;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.sodiumzh.nfu.item.bauble.BaubleBehavior;
import net.sodiumzh.nfu.item.bauble.BaubleEquippingCondition;
import net.sodiumzh.nfu.util.NFUMathStatics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Base class for bauble behavior that is attached to an <em>existing</em> item (the NFU
 * {@link BaubleBehavior} model), extended with the NFF-Girls category-key/tier and
 * bauble-tag concepts from {@link INFFGirlsBauble}.
 * <p>
 * Unlike {@link NFFGirlsDedicatedBaubleItem}, the behavior does not own an item class:
 * it targets a single {@link Item} or a set of items matched by a {@link BiPredicate},
 * which lets baubles be layered onto vanilla or third-party items. In addition to the
 * base features it also carries a list of tooltip suppliers. Its registry key is derived
 * from the category key plus a tier suffix.
 */
public abstract class NFFGirlsBaubleBehavior extends BaubleBehavior implements INFFGirlsBauble {

    private ResourceLocation categoryKey;
    private int tier = 1;
    private Map<String, Predicate<Integer>> tags = new HashMap<>();
    private List<Supplier<? extends Component>> tooltips = new ArrayList<>();

    protected NFFGirlsBaubleBehavior(@Nullable Item item, @Nullable BiPredicate<Item, ItemStack> multiItemCondition,
                                     BaubleEquippingCondition equippingCondition, ResourceLocation categoryKey, int tier) {
        super(getBaubleRegistryKey(categoryKey, tier), item, multiItemCondition, equippingCondition);
        this.categoryKey = categoryKey;
        this.tier = tier;

    }

    /**
     * Create a bauble behavior targeting a single item.
     * @param item               the item this behavior applies to
     * @param equippingCondition condition gating whether the bauble can be equipped/active
     * @param categoryKey        the shared category key for all tiers
     * @param tier               the tier of this bauble
     */
    public NFFGirlsBaubleBehavior(@NotNull Item item, BaubleEquippingCondition equippingCondition, ResourceLocation categoryKey, int tier) {
        super(item, getBaubleRegistryKey(categoryKey, tier), equippingCondition);
        this.categoryKey = categoryKey;
        this.tier = tier;
    }

    /**
     * Create a bauble behavior targeting every item matched by a condition.
     * @param condition          predicate matching the items this behavior applies to
     * @param equippingCondition condition gating whether the bauble can be equipped/active
     * @param categoryKey        the shared category key for all tiers
     * @param tier               the tier of this bauble
     */
    public NFFGirlsBaubleBehavior(@NotNull BiPredicate<Item, ItemStack> condition, BaubleEquippingCondition equippingCondition, ResourceLocation categoryKey, int tier) {
        super(condition, getBaubleRegistryKey(categoryKey, tier), equippingCondition);
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
        return "_" + NFUMathStatics.intToRoman(tier).toLowerCase();
    }

    /**
     * @param tier the tier to compute a suffix for
     * @return the tier suffix (empty for tier 1, otherwise {@code "_" + lowercase Roman numeral}).
     */
    public static String getTierSuffix(int tier)
    {
        if (tier == 1)
            return "";
        return "_" + NFUMathStatics.intToRoman(tier).toLowerCase();
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
    public NFFGirlsBaubleBehavior addBaubleTag(String tag, Predicate<Integer> tierCondition) {
        this.tags.put(tag, tierCondition);
        return this;
    }

    /**
     * Add a bauble tag that is always active regardless of tier.
     * @param tag the tag to add
     * @return {@code this}, for chaining.
     */
    public NFFGirlsBaubleBehavior addBaubleTag(String tag) {
        return this.addBaubleTag(tag, i -> true);
    }

    /**
     * Add multiple bauble tags that are always active regardless of tier.
     * @param tags the tags to add
     * @return {@code this}, for chaining.
     */
    public NFFGirlsBaubleBehavior addBaubleTags(String... tags) {
        for (String tag: tags) {
            this.tags.put(tag, i -> true);
        }
        return this;
    }

    /**
     * Override the category key of this bauble.
     * @param categoryKey the new category key
     * @return {@code this}, for chaining.
     */
    public NFFGirlsBaubleBehavior setCategoryKey(ResourceLocation categoryKey) {
        this.categoryKey = categoryKey;
        return this;
    }

    /**
     * Register a tooltip line supplier shown for this bauble.
     * @param tooltip supplier producing the tooltip component
     * @return {@code this}, for chaining.
     */
    public NFFGirlsBaubleBehavior addTooltip(Supplier<? extends Component> tooltip) {
        this.tooltips.add(tooltip);
        return this;
    }

    /** @return the registered tooltip line suppliers for this bauble. */
    public List<Supplier<? extends Component>> getTooltips() {
        return this.tooltips;
    }
}
