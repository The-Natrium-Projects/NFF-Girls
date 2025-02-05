package net.sodiumzh.nff.girls.registry;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sodiumzh.nff.girls.NFFGirls;

public class NFFGirlsPotions
{
	public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, NFFGirls.MOD_ID);

	public static final RegistryObject<Potion> UNDEAD_AFFINITY = POTIONS.register("nffgirls.undead_affinity",
			() -> new Potion(new MobEffectInstance[]{new MobEffectInstance(NFFGirlsEffects.UNDEAD_AFFINITY.get(),
					3 * 60 * 20)}));

	public static final RegistryObject<Potion> UNDEAD_AFFINITY_LONG = POTIONS.register("nffgirls.undead_affinity_long",
			() -> new Potion(new MobEffectInstance[]{new MobEffectInstance(NFFGirlsEffects.UNDEAD_AFFINITY.get(),
					8 * 60 * 20)}));

	public static final RegistryObject<Potion> UNDEAD_AFFINITY_LONG_2 = POTIONS.register("nffgirls.undead_affinity_long_2",
			() -> new Potion(new MobEffectInstance[]{new MobEffectInstance(NFFGirlsEffects.UNDEAD_AFFINITY.get(),
					18 * 60 * 20)}));

	public static final RegistryObject<Potion> ENDER_PROTECTION = POTIONS.register("nffgirls.ender_protection",
			() -> new Potion(new MobEffectInstance[]{new MobEffectInstance(NFFGirlsEffects.ENDER_PROTECTION.get(),
					90 * 20)}));

	public static final RegistryObject<Potion> ENDER_PROTECTION_LONG = POTIONS.register("nffgirls.ender_protection_long",
			() -> new Potion(new MobEffectInstance[]{new MobEffectInstance(NFFGirlsEffects.ENDER_PROTECTION.get(),
					4 * 60 * 20)}));

	public static final RegistryObject<Potion> ENDER_PROTECTION_LONG_2 = POTIONS.register("nffgirls.ender_protection_long_2",
			() -> new Potion(new MobEffectInstance[]{new MobEffectInstance(NFFGirlsEffects.ENDER_PROTECTION.get(),
					9 * 60 * 20)}));
}
