package net.sodiumzh.nff.girls.registry;

import net.minecraft.world.entity.Mob;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nfu.function.RegistrableFunction;
import net.sodiumzh.nfu.registry.NFURegistries;
import net.sodiumzh.nfu.registry.NFURegistry;
import net.sodiumzh.nfu.registry.NFURegistryEntryCollection;

public class NFFGirlsFunctions {

    public static final NFURegistryEntryCollection<RegistrableFunction<?, ?>> FUNCTIONS = NFURegistryEntryCollection.create(
            NFURegistries.FUNCTIONS, NFFGirls.MOD_ID);

    /**
     * A {@code Function<Mob, Double>} to get the mob's max health as double.
     */
    public static final NFURegistry.Accessor<RegistrableFunction<Mob, Double>> MOB_MAX_HEALTH
        = FUNCTIONS.register("mob_max_health", () ->
            new RegistrableFunction<>(Mob.class, Double.class, "mob_max_health",
                mob -> (double)mob.getMaxHealth()));


}
