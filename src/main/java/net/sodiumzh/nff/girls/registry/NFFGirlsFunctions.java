package net.sodiumzh.nff.girls.registry;

import net.minecraft.world.entity.Mob;
import net.sodiumzh.nautils.registries.NaUtilsRegistries;
import net.sodiumzh.nautils.registries.NaUtilsRegistry;
import net.sodiumzh.nautils.registries.RegistryEntryCollection;
import net.sodiumzh.nff.girls.NFFGirls;

import java.util.function.Function;

public class NFFGirlsFunctions {

    public static final RegistryEntryCollection<Function<?, ?>> FUNCTIONS = RegistryEntryCollection.create(
            NaUtilsRegistries.FUNCTIONS, NFFGirls.MOD_ID);

    public static final NaUtilsRegistry.Accessor<Function<?, ?>> MOB_MAX_HEALTH = FUNCTIONS.register("mob_max_health",
            () -> ((Mob mob) -> (double)mob.getMaxHealth()));
}
