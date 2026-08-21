package net.sodiumzh.nff.girls.item.bauble;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.sodiumzh.nff.girls.registry.NFFGirlsBaubles;
import net.sodiumzh.nfu.item.bauble.IBaubleRegistryEntry;
import net.sodiumzh.nfu.item.bauble.NFUBaubleAPI;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * Get the "category key" of the bauble. E.g. For Soul Amulets of all tiers, the key
     * is "nffgirls:soul_amulet".
     */
    public ResourceLocation getCategoryKey();

    /**
     * Get the tier under this category key, deciding the actual effects.
     */
    public int getTier();

    /**
     * Get additional string tags to identify common behaviors that cannot be defined with
     * attribute modifiers.
     */
    public List<String> getBaubleTags();

    /**
     * Build an {@link UnsupportedOperationException} for the current, unsupported {@link #getTier()}.
     * Intended to be thrown by implementations when they encounter a tier they don't know how to handle.
     */
    public default UnsupportedOperationException unsupportedTier()
    {
        return new UnsupportedOperationException("Unsupported bauble tier ( " + this.getTier() + ").");
    }

    /**
     * Check if this bauble currently carries the given {@link #getBaubleTags() bauble tag}.
     */
    public default boolean hasBaubleTag(String tag) {
        return this.getBaubleTags().contains(tag);
    }

    /**
     * A type-safe utility for checking if an Item or ItemStack has a Bauble Tag.
     */
    public static boolean hasBaubleTag(Object test, String tag) {
        if (test instanceof INFFGirlsBauble bauble) {
            return bauble.hasBaubleTag(tag);
        }
        else if (test instanceof ItemStack is && is.getItem() instanceof INFFGirlsBauble bauble) {
            return bauble.hasBaubleTag(tag);
        }
        else return false;
    }

    /**
     * Get the {@link NFFGirlsBaubleProperties} that describe this bauble's attribute modifiers,
     * tick behavior and tooltips.
     */
    public NFFGirlsBaubleProperties getProperties();

    /**
     * Check if the given mob is currently equipped with a bauble carrying the
     * {@link #TAG_ENVIRONMENT_IMMUNITY} tag.
     */
    public static boolean isEnvironmentImmunized(Mob test) {
        return NFUBaubleAPI.getAllSlotItems(test).values().stream()
            .anyMatch(i -> hasBaubleTag(i, TAG_ENVIRONMENT_IMMUNITY));
    }

    /**
     * Resolve the {@link INFFGirlsBauble} entry (or entries) associated with the given object, which
     * may be an {@link INFFGirlsBauble} itself, an {@link Item}, or an {@link ItemStack}. For items/stacks
     * that are not themselves a bauble, this also searches {@code NFFGirlsBaubles.BAUBLE_REGISTRY} for
     * matching {@link NFFGirlsBaubleBehavior} entries (single-item or multi-item condition based).
     */
    public static List<INFFGirlsBauble> asBauble(Object obj) {
        List<INFFGirlsBauble> list = new ArrayList<>();
        if (obj instanceof INFFGirlsBauble i) {
            list.add(i);
        }
        ItemStack itemStack = (obj instanceof Item item) ? item.getDefaultInstance() :
            ((obj instanceof ItemStack stack) ? stack : null);
        if (itemStack == null) return list;
        list.addAll(NFFGirlsBaubles.BAUBLE_REGISTRY.values().stream().filter(i -> i instanceof NFFGirlsBaubleBehavior)
            .map(i -> (NFFGirlsBaubleBehavior)i).filter(i -> {
                if (i.isMulti()) return i.getMultiItemCondition().test(itemStack.getItem(), itemStack);
                else return i.getItem().equals(itemStack.getItem());
            }).toList());
        return list;
    }

}
