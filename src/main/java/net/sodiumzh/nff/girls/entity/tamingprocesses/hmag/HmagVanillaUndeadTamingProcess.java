package net.sodiumzh.nff.girls.entity.tamingprocesses.hmag;

import java.util.HashSet;

import com.github.mechalopa.hmag.registry.ModItems;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.sodiumzh.nautils.entity.anger.MobAngerRules;
import net.sodiumzh.nautils.statics.NaUtilsMathStatics;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.sodiumzh.nff.girls.entity.NFFGirlsTamingRules;
import net.sodiumzh.nff.girls.registry.NFFGirlsAngerRules;
import net.sodiumzh.nff.girls.registry.NFFGirlsEffects;
import net.sodiumzh.nautils.entity.anger.MobAngerReason;
import net.sodiumzh.nff.services.entity.capability.CNFFTamable;
import net.sodiumzh.nff.services.entity.taming.TamingProcessItemGivingProgress;

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

	@Override
	public MobAngerRules getAngerRules() {
		return NFFGirlsAngerRules.ATTACKER_AND_MINOR_ATTACKING.get();
	}

}
