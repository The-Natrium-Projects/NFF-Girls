package net.sodiumzh.nff.girls.item.bauble;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.sodiumzh.nfu.item.bauble.BaubleSystem;
import net.sodiumzh.nfu.item.bauble.IBaubleRegistryEntry;

import java.util.List;

public interface INFFGirlsBauble extends IBaubleRegistryEntry {

    public static final String TAG_ENVIRONMENT_IMMUNITY = "environment_immunity";

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

    public default UnsupportedOperationException unsupportedTier()
    {
        return new UnsupportedOperationException("Unsupported bauble tier ( " + this.getTier() + ").");
    }

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

    public static boolean isEnvironmentImmunized(Mob test) {
        return BaubleSystem.getAllSlotItems(test).values().stream()
            .anyMatch(i -> hasBaubleTag(i, TAG_ENVIRONMENT_IMMUNITY));
    }

}
