package net.sodiumzh.nff.girls.entity.tamingprocess.hmag;

import java.util.HashSet;

import com.github.mechalopa.hmag.registry.ModItems;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.FlowerBlock;
import net.sodiumzh.nautils.statics.NaUtilsContainerStatics;
import net.sodiumzh.nautils.statics.NaUtilsMathStatics;
import net.sodiumzh.nff.services.entity.taming.TamableHatredReason;
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
		return 5 * 20;
	}

	@Override
	public HashSet<TamableHatredReason> getAddHatredReasons() {
		return NaUtilsContainerStatics.setOf(TamableHatredReason.ATTACKED);
	}

}
