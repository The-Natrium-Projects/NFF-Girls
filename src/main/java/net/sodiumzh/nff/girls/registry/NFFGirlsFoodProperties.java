package net.sodiumzh.nff.girls.registry;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class NFFGirlsFoodProperties {

	public static final FoodProperties SOUL_CAKE_SLICE = (new FoodProperties.Builder()).nutrition(9)
			.saturationMod(0.1F)
			.effect(() -> new MobEffectInstance(NFFGirlsEffects.UNDEAD_AFFINITY.get(), 8*60*20), 1.0f)
			.effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 8*60*20, 1), 1.0f)
			.effect(() -> new MobEffectInstance(MobEffects.SATURATION, 8), 1.0f)
			.alwaysEat().build();
	public static final FoodProperties ENDERBERRY = (new FoodProperties.Builder()).nutrition(6)
			.saturationMod(0.1F).build();
	public static final FoodProperties ENDER_PIE = (new FoodProperties.Builder()).nutrition(16)
			.saturationMod(0.6F)
			.effect(() -> new MobEffectInstance(NFFGirlsEffects.ENDER_PROTECTION.get(), 9*60*20), 1.0f)
			.effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 9*60*20, 1), 1.0f)
			.effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 3 * 60 * 20, 3), 1.0f)
			.alwaysEat().build();

}
