package net.sodiumzh.nff.girls.entity.tamingprocess.hmag;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.sodiumzh.nff.girls.entity.NFFGirlsTamingRules;
import net.sodiumzh.nff.girls.registry.NFFGirlsAngerRules;
import net.sodiumzh.nff.girls.registry.NFFGirlsEffects;
import net.sodiumzh.nff.services.entity.taming.CNFFTamable;
import net.sodiumzh.nff.services.entity.taming.TamingProcessItemGivingProgress;
import net.sodiumzh.nfu.entity.anger.MobAngerRules;

public class HmagVanillaUndeadTamingProcess extends TamingProcessItemGivingProgress
{

	@Override
	public boolean additionalConditions(Player player, Mob mob)
	{
		return player.hasEffect(NFFGirlsEffects.UNDEAD_AFFINITY.get());
	}

	@Override
	public int getItemGivingCooldownTicks() {
		return NFFGirlsTamingRules.COOLDOWN_MIDDLE;
	}


	@Override
	public void tamableInit(CNFFTamable cnffTamable) {

	}

	@Override
	public void onAttackedByProcessingPlayer(Mob mob, Player player, double damageGiven)
	{
		if (damageGiven > CNFFTamable.get(mob).getDamageThreshold())
			interrupt(player, mob, false);		
	}

}
