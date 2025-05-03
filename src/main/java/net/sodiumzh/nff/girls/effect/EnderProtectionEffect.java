package net.sodiumzh.nff.girls.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class EnderProtectionEffect extends MobEffect
{

	public EnderProtectionEffect() {
		super(MobEffectCategory.BENEFICIAL, 0XC900FA /* Color of ender man eye */ );
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
