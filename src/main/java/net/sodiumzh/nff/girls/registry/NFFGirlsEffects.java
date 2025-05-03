package net.sodiumzh.nff.girls.registry;

import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.effect.EnderProtectionEffect;
import net.sodiumzh.nff.girls.effect.NecromancerWitherEffect;
import net.sodiumzh.nff.girls.effect.UndeadAffinityEffect;

@Mod.EventBusSubscriber(modid = NFFGirls.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NFFGirlsEffects {

	public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, NFFGirls.MOD_ID);
	
	// Registry body
	
	public static final RegistryObject<MobEffect> UNDEAD_AFFINITY = EFFECTS.register("undead_affinity", () -> new UndeadAffinityEffect());
	public static final RegistryObject<MobEffect> ENDER_PROTECTION = EFFECTS.register("ender_protection", () -> new EnderProtectionEffect());
	public static final RegistryObject<MobEffect> NECROMANCER_WITHER = EFFECTS.register("necromancer_wither", () -> new NecromancerWitherEffect());
}
