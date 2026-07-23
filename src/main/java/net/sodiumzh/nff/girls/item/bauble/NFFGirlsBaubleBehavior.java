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

    public NFFGirlsBaubleBehavior(@NotNull Item item, BaubleEquippingCondition equippingCondition, ResourceLocation categoryKey, int tier) {
        super(item, getBaubleRegistryKey(categoryKey, tier), equippingCondition);
        this.categoryKey = categoryKey;
        this.tier = tier;
    }

    public NFFGirlsBaubleBehavior(@NotNull BiPredicate<Item, ItemStack> condition, BaubleEquippingCondition equippingCondition, ResourceLocation categoryKey, int tier) {
        super(condition, getBaubleRegistryKey(categoryKey, tier), equippingCondition);
        this.categoryKey = categoryKey;
        this.tier = tier;
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
}
