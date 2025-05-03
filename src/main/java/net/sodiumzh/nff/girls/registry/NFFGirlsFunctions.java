package net.sodiumzh.nff.girls.registry;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.FlowerBlock;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nfu.registry.NFURegistries;
import net.sodiumzh.nfu.registry.NFURegistry;
import net.sodiumzh.nfu.registry.NFURegistryEntryCollection;

import java.util.function.Function;

public class NFFGirlsFunctions {

    public static final NFURegistryEntryCollection<Function<?, ?>> FUNCTIONS = NFURegistryEntryCollection.create(
            NFURegistries.FUNCTIONS, NFFGirls.MOD_ID);

    /**
     * A {@code Function<Mob, Double>} to get the mob's max health as double.
     */
    public static final NFURegistry.Accessor<Function<Mob, Double>> MOB_MAX_HEALTH =
            FUNCTIONS.<Function<Mob, Double>>register("mob_max_health",
            () -> ((Mob mob) -> (double)mob.getMaxHealth()));
    /**
     * A {@code Function<ItemStack, Boolean>} (i.e. {@code Predicate<ItemStack>} but not directly castable) to
     * check if an {@link ItemStack} is a {@link BlockItem} of {@link FlowerBlock}.
     */
    public static final NFURegistry.Accessor<Function<ItemStack, Boolean>> IS_FLOWER_ITEM_STACK =
            FUNCTIONS.<Function<ItemStack, Boolean>>register("is_flower_item_stack",
            () -> ((ItemStack stack) -> (
                            (stack.getItem() != null) &&
                            (stack.getItem() instanceof BlockItem blockitem) &&
                            (blockitem.getBlock() instanceof FlowerBlock))));
}
