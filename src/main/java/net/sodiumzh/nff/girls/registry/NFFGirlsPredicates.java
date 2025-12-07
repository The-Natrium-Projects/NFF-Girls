package net.sodiumzh.nff.girls.registry;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.FlowerBlock;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nfu.function.RegistrablePredicate;
import net.sodiumzh.nfu.registry.NFURegistries;
import net.sodiumzh.nfu.registry.NFURegistry;
import net.sodiumzh.nfu.registry.NFURegistryEntryCollection;

public class NFFGirlsPredicates {

    public static final NFURegistryEntryCollection<RegistrablePredicate<?>> PREDICATES =
        NFURegistryEntryCollection.create(NFURegistries.PREDICATES, NFFGirls.MOD_ID);


    /**
     * A {@code Function<ItemStack, Boolean>} (i.e. {@code Predicate<ItemStack>} but not directly castable) to
     * check if an {@link ItemStack} is a {@link BlockItem} of {@link FlowerBlock}.
     */
    public static final NFURegistry.Accessor<RegistrablePredicate<ItemStack>> IS_FLOWER_ITEM_STACK =
        PREDICATES.register("is_flower_item_stack",
            () -> new RegistrablePredicate<>(ItemStack.class, "is_flower_item_stack",
                (ItemStack stack) -> (stack.getItem() instanceof BlockItem blockitem &&
                    blockitem.getBlock() instanceof FlowerBlock)));
}
