package net.sodiumzh.nff.girls.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;


/**
 * Death Affinity makes undead mobs neutral to the effected mob
 */

public class UndeadAffinityEffect extends MobEffect {

	public UndeadAffinityEffect() {
		super(MobEffectCategory.BENEFICIAL, 0X483D8B /*DarkSlateBlue*/ );
	}
	
	// No explicit effect here. Configured in ServerEvents.
	@Override
	public void applyEffectTick(LivingEntity livingEntityIn, int amplifier) {
	}
	
	// For an effect with a duration
	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return duration > 0;
	}
	
}
