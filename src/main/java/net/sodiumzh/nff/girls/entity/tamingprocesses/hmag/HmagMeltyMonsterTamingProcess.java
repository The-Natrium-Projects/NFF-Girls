package net.sodiumzh.nff.girls.entity.tamingprocesses.hmag;

import com.github.mechalopa.hmag.registry.ModEffects;
import com.github.mechalopa.hmag.registry.ModItems;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.sodiumzh.nautils.entity.anger.MobAngerRules;
import net.sodiumzh.nautils.math.RandomSelection;
import net.sodiumzh.nautils.math.RndUtil;
import net.sodiumzh.nautils.statics.NaUtilsEntityStatics;
import net.sodiumzh.nautils.entity.anger.MobAngerReason;
import net.sodiumzh.nff.girls.entity.NFFGirlsTamingRules;
import net.sodiumzh.nff.girls.registry.NFFGirlsAngerRules;
import net.sodiumzh.nff.services.entity.capability.CNFFTamable;
import net.sodiumzh.nff.services.entity.taming.TamingProcessItemGivingProgress;

public class HmagMeltyMonsterTamingProcess extends TamingProcessItemGivingProgress
{

	@Override
	public boolean additionalConditions(Player player, Mob mob) {
		return (player.isInLava() || player.isOnFire() || player.hasEffect(ModEffects.COMBUSTION.get()))
				&& !player.hasEffect(MobEffects.FIRE_RESISTANCE);
	}

	@Override
	public int getItemGivingCooldownTicks() {
		return NFFGirlsTamingRules.COOLDOWN_LONG;
	}

	@Override
	public void onItemGiven(Player player, Mob mob, ItemStack itemGivenCopy, double procBefore, double procAfter)
	{
		player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 5 * 20));
		NaUtilsEntityStatics.addEffectSafe(player, ModEffects.COMBUSTION.get(), 30 * 20);
	}
	
	@Override
	public MobAngerRules getAngerRules() {
		return NFFGirlsAngerRules.DEFAULT.get();
	}

	@Override
	public void tamableInit(CNFFTamable cnffTamable) {

	}
}
