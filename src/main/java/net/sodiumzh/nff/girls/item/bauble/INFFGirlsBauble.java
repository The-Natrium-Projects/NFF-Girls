package net.sodiumzh.nff.girls.item.bauble;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.sodiumzh.nff.girls.registry.NFFGirlsBaubles;
import net.sodiumzh.nff.girls.registry.NFFGirlsItemTooltips;
import net.sodiumzh.nfu.item.bauble.NFUBaubleAPI;
import net.sodiumzh.nfu.item.bauble.IBaubleRegistryEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface INFFGirlsBauble extends IBaubleRegistryEntry {

    public static final String TAG_ENVIRONMENT_IMMUNITY = "environment_immunity";
    public static final String TAG_ACTIVE_ATTACK_1 = "active_attack_1";
    public static final String TAG_ACTIVE_ATTACK_2 = "active_attack_2";

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
        return NFUBaubleAPI.getAllSlotItems(test).values().stream()
            .anyMatch(i -> hasBaubleTag(i, TAG_ENVIRONMENT_IMMUNITY));
    }

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
