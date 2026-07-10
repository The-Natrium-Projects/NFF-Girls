package net.sodiumzh.nff.girls.entity.tamingprocesses.hmag;

import com.github.mechalopa.hmag.registry.ModEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.sodiumzh.nff.girls.entity.NFFGirlsTamingRules;
import net.sodiumzh.nff.services.entity.taming.NFFTamableComponent;
import net.sodiumzh.nff.services.entity.taming.TamingProcessItemGivingProgress;
import net.sodiumzh.nfu.util.NFUEntityStatics;

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
		NFUEntityStatics.addEffectSafe(player, ModEffects.COMBUSTION.get(), 30 * 20);
	}

	@Override
	public void tamableInit(NFFTamableComponent cnffTamable) {

	}
}
