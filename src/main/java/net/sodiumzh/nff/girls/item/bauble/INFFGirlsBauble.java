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
 * Common contract for every NFF-Girls bauble, layered on top of the NFU-Library
 * {@link IBaubleRegistryEntry} system.
 * <p>
 * On top of the base NFU bauble system (mob-equippable accessories that grant attribute
 * modifiers and behaviors), this interface adds the NFF-Girls-specific concepts of a
 * <em>category key</em> plus a <em>tier</em> (so multiple tiers of the same conceptual
 * bauble can share a category), and a set of free-form <em>bauble tags</em> used to flag
 * common behaviors that cannot be expressed with attribute modifiers alone.
 * <p>
 * It is implemented by both {@link NFFGirlsDedicatedBaubleItem} (the item itself is a
 * bauble) and {@link NFFGirlsBaubleBehavior} (bauble behavior attached to an existing item).
 */
public interface INFFGirlsBauble extends IBaubleRegistryEntry {

    /** Bauble tag marking a bauble that grants immunity to environmental damage/effects. */
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
     * @return a ready-to-throw exception describing that this bauble's {@link #getTier() tier}
     *         is not supported by the calling logic.
     */
    public default UnsupportedOperationException unsupportedTier()
    {
        return new UnsupportedOperationException("Unsupported bauble tier ( " + this.getTier() + ").");
    }

    /**
     * @param tag the bauble tag to test for
     * @return {@code true} if this bauble carries the given bauble tag.
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
     * @param test the mob to inspect
     * @return {@code true} if any bauble currently equipped by the mob carries the
     *         {@link #TAG_ENVIRONMENT_IMMUNITY} tag.
     */
    public static boolean isEnvironmentImmunized(Mob test) {
        return NFUBaubleAPI.getAllSlotItems(test).values().stream()
            .anyMatch(i -> hasBaubleTag(i, TAG_ENVIRONMENT_IMMUNITY));
    }

    /**
     * Resolve an arbitrary object to the list of {@link INFFGirlsBauble} entries associated with it.
     * <p>
     * If {@code obj} is itself an {@link INFFGirlsBauble} it is included. In addition, any
     * {@link NFFGirlsBaubleBehavior} registered in {@link NFFGirlsBaubles#BAUBLE_REGISTRY} whose
     * target item (or multi-item condition) matches the item/stack is included.
     *
     * @param obj an {@link INFFGirlsBauble}, {@link Item}, {@link ItemStack} or any other object
     * @return the matching baubles; empty if none match.
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
