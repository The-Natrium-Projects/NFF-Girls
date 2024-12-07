package net.sodiumzh.nff.girls.entity.tamingprocess.hmag;

import java.util.HashSet;

import com.github.mechalopa.hmag.registry.ModEffects;
import com.github.mechalopa.hmag.registry.ModItems;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.sodiumzh.nautils.statics.NaUtilsEntityStatics;
import net.sodiumzh.nautils.statics.NaUtilsContainerStatics;
import net.sodiumzh.nautils.statics.NaUtilsMathStatics;
import net.sodiumzh.nautils.math.RandomSelection;
import net.sodiumzh.nff.services.entity.taming.TamableHatredReason;
import net.sodiumzh.nff.services.entity.taming.TamingProcessItemGivingProgress;

public class HmagMeltyMonsterTamingProcess extends TamingProcessItemGivingProgress
{

	@Override
	protected double getProcValueToAdd(ItemStack item, Player player, Mob mob, double oldProc) {
		if (item.is(Items.BLAZE_POWDER))
			return NaUtilsMathStatics.rndRangedDouble(0.02, 0.04);
		else if (item.is(Items.BLAZE_ROD))
			return NaUtilsMathStatics.rndRangedDouble(0.03, 0.06);
		else if (item.is(ModItems.BURNING_CORE.get()))
			return NaUtilsMathStatics.rndRangedDouble(0.03, 0.06);
		else if (item.is(Items.NETHER_STAR))
			return RandomSelection.createDouble(0.5d).add(1.01d, 0.3d).getDouble();
		else return 0;
	}

	@Override
	public boolean isItemAcceptable(ItemStack itemstack) {
		return getProcValueToAdd(itemstack, null, null, 0) > 0;
	}

	@Override
	public boolean additionalConditions(Player player, Mob mob) {
		return (player.isInLava() || player.isOnFire() || player.hasEffect(ModEffects.COMBUSTION.get()))
				&& !player.hasEffect(MobEffects.FIRE_RESISTANCE);
	}

	@Override
	public int getItemGivingCooldownTicks() {
		return 15 * 20;
	}

	@Override
	public void onItemGiven(Player player, Mob mob, ItemStack itemGivenCopy, double procBefore, double procAfter)
	{
		player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 5 * 20));
		NaUtilsEntityStatics.addEffectSafe(player, ModEffects.COMBUSTION.get(), 30 * 20);
	}
	
	@Override
	public HashSet<TamableHatredReason> getAddHatredReasons() {
		return NaUtilsContainerStatics.setOf(TamableHatredReason.ATTACKED);
	}

}
