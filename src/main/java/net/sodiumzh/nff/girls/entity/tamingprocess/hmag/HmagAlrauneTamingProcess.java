package net.sodiumzh.nff.girls.entity.tamingprocess.hmag;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.sodiumzh.nff.girls.entity.NFFGirlsTamingRules;
import net.sodiumzh.nff.services.entity.taming.NFFTamableComponent;
import net.sodiumzh.nff.services.entity.taming.TamingProcessItemGivingProgress;

public class HmagAlrauneTamingProcess extends TamingProcessItemGivingProgress
{

	@Override
	public boolean additionalConditions(Player player, Mob mob) {
		return mob.hasEffect(MobEffects.REGENERATION) 
				&& mob.getEffect(MobEffects.REGENERATION).getAmplifier() >= 2
				&& mob.getEffect(MobEffects.REGENERATION).getDuration() > 10 * 20;
	}

	@Override
	public int getItemGivingCooldownTicks() {
		return NFFGirlsTamingRules.COOLDOWN_SHORT;
	}

	@Override
	public void tamableInit(NFFTamableComponent c) {
	}

}
