package net.sodiumzh.nff.girls.item.bauble;

import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.sodiumzh.nfu.item.bauble.BaubleProcessingArgs;
import net.sodiumzh.nfu.item.bauble.IBaubleRegistryEntry;
import net.sodiumzh.nfu.item.bauble.NFUBaubleAPI;
import net.sodiumzh.nfu.util.NFUMathStatics;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Common interface of all NFF-Girls bauble entries built on top of NFU-Library's bauble system
 * ({@link IBaubleRegistryEntry}). It adds the "category key" + "tier" concept used to group related
 * baubles (e.g. every tier of the Soul Amulet shares the same category key) as well as a set of
 * string-based "bauble tags" for identifying common behaviors that can't be expressed with plain
 * attribute modifiers, and exposes the {@link NFFGirlsBaubleProperties} that actually drives the
 * bauble's effects. It is implemented by both {@link NFFGirlsBaubleBehavior} (baubles attached to an
 * existing item) and {@link NFFGirlsDedicatedBaubleItem} (baubles that are their own dedicated item).
 */
public interface INFFGirlsBauble extends IBaubleRegistryEntry {

    /** Bauble tag marking a bauble that grants environment (e.g. weather/biome) damage immunity. */
    public static final String TAG_ENVIRONMENT_IMMUNITY = "environment_immunity";
    /** Bauble tag marking a bauble that enables an active attack behavior. */
    public static final String TAG_ACTIVE_ATTACK = "active_attack";

    public static final String META_KEY_CATEGORY_KEY = "categoryKey";
    public static final String META_KEY_TIER = "tier";


    /**
     * Get the "category key" of the bauble. E.g. For Soul Amulets of all tiers, the key
     * is "nffgirls:soul_amulet".
     */
    @ApiStatus.NonExtendable
    public default ResourceLocation getCategoryKey() {
        return this.getMetaDataMap().getOptionalAs(META_KEY_CATEGORY_KEY, ResourceLocation.class)
            .orElseThrow(() -> new IllegalStateException("NFFGirlsBauble missing category key"));
    }

    /**
     * Get the tier under this category key, deciding the actual effects.
     */
    @ApiStatus.NonExtendable
    public default int getTier() {
        return this.getMetaDataMap().getOptionalAs(META_KEY_TIER, Integer.class)
            .orElseThrow(() -> new IllegalStateException("NFFGirlsBauble missing tier"));
    }

    /**
     * Get additional string tags to identify common behaviors that cannot be defined with
     * attribute modifiers.
     */
    @ApiStatus.NonExtendable
    public default List<String> getBaubleTags(BaubleProcessingArgs args) {
        return this.getProperties().getTagProperties(args).stream().map(NFFGirlsBaubleProperties.TagProperty::name).toList();
    }

    /**
     * Check if this bauble currently carries the given {@link #getBaubleTags bauble tag}.
     */
    @ApiStatus.NonExtendable
    public default boolean hasBaubleTag(String tag, BaubleProcessingArgs args) {
        return this.getBaubleTags(args).contains(tag);
    }

    /**
     * Get the {@link NFFGirlsBaubleProperties} that describe this bauble's attribute modifiers,
     * tick behavior and tooltips.
     */
    public NFFGirlsBaubleProperties getProperties();

    /**
     * Resolve the {@link INFFGirlsBauble} entry (or entries) associated with the given an {@link ItemStack}.
     */
    public static Map<ResourceLocation, INFFGirlsBauble> asBauble(ItemStack itemStack) {
       if (itemStack.getItem() == null)
           return Map.of();
        return NFUBaubleAPI.getItemKeyedSingleItemBaubles().get(itemStack.getItem()).stream()
            .filter(entry -> entry.bauble() instanceof INFFGirlsBauble)
            .collect(Collectors.toMap(entry -> entry.key(),
                entry -> (INFFGirlsBauble)(entry.bauble())));
    }

    public static ChatFormatting getRarityStyle(int rarity) {
        return switch (rarity) {
            case 0, 1, 2 -> ChatFormatting.WHITE;
            case 3 -> ChatFormatting.YELLOW;
            case 4 -> ChatFormatting.GREEN;
            case 5 -> ChatFormatting.LIGHT_PURPLE;
            case 6 -> ChatFormatting.BLUE;
            case 7 -> ChatFormatting.GOLD;
            case 8 -> ChatFormatting.RED;
            case 9 -> ChatFormatting.AQUA;
            case 10 -> ChatFormatting.DARK_PURPLE;
            default -> ChatFormatting.WHITE;
        };
    }

    public static boolean isEnvironmentImmune(Mob mob) {
        return NFUBaubleAPI.getAllSlotItems(mob).values().stream()
            .flatMap(is -> NFUBaubleAPI.getAllRelatedEntries(is).stream())
            .anyMatch(entry -> entry instanceof INFFGirlsBauble b && b.getProperties().hasEnvironmentResistance());
    }

    @Override
    public default ResourceLocation getBaubleRegistryKey() {
        return new ResourceLocation(this.getCategoryKey().getNamespace(), this.getCategoryKey().getPath() + getTierSuffix(this.getTier()));
    }

    public static String getTierSuffix(int tier)
    {
        if (tier == 1)
            return "";
        return "_" + NFUMathStatics.intToRoman(tier).toLowerCase(Locale.ENGLISH);
    }

}
