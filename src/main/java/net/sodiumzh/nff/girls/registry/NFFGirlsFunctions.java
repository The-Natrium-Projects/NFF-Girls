package net.sodiumzh.nff.girls.registry;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nfu.function.NFUOperator;
import net.sodiumzh.nfu.registry.NFURegistries;
import net.sodiumzh.nfu.registry.NFURegistry;
import net.sodiumzh.nfu.registry.NFURegistryEntryCollection;

public class NFFGirlsFunctions {

    public static final NFURegistryEntryCollection<NFUOperator<?, ?>> OPERATORS = NFURegistryEntryCollection.create(
            NFURegistries.OPERATORS, NFFGirls.MOD_ID);

    /**
     * A {@code Function<Mob, Double>} to get the mob's max health as double.
     */
    public static final NFURegistry.Accessor<NFUOperator<LivingEntity, Double>> MOB_MAX_HEALTH
        = OPERATORS.register("mob_max_health", () ->
            NFUOperator.simpleFunction(LivingEntity.class, Double.class, (LivingEntity living) -> (double)living.getMaxHealth()));


}
