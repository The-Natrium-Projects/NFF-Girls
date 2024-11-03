package net.sodiumzh.nff.girls.entity.tamingprocesses.hmag;

import java.util.HashSet;

import com.github.mechalopa.hmag.registry.ModItems;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.FlowerBlock;
import net.sodiumzh.nautils.statics.NaUtilsMathStatics;
import net.sodiumzh.nautils.statics.NaUtilsContainerStatics;
import net.sodiumzh.nff.services.entity.taming.TamableHatredReason;
import net.sodiumzh.nff.services.entity.taming.TamingProcessItemGivingProgress;

public class HmagAlrauneTamingProcess extends TamingProcessItemGivingProgress
{

	/*Override
	protected double getProcValueToAdd(ItemStack item, Player player, Mob mob, double oldProc) {
		if (item.is(Items.SPORE_BLOSSOM))
			return NaUtilsMathStatics.rndRangedDouble(0.05, 0.1);
		else if (item.is(ModItems.MYSTERIOUS_PETAL.get()))
			return NaUtilsMathStatics.rndRangedDouble(0.1, 0.2);
		else if (item.is(ModItems.CUREBERRY.get()) || item.is(ModItems.EXP_BERRY.get()) || item.is(ModItems.RANDOMBERRY.get()))
			return NaUtilsMathStatics.rndRangedDouble(0.2, 0.3);
		else if (item.getItem() != null && item.getItem() instanceof BlockItem blockitem && blockitem.getBlock() instanceof FlowerBlock)
<<<<<<< HEAD
			return NaUtilsMathStatics.rndRangedDouble(0.03, 0.06);
		else return 0;
=======
			return NaUtilsMathStatics.rndRangedDouble(0.03, 0.06);
		else return 0；
>>>>>>> b2ab5b02 (Taming)
	}

	@Override
	public boolean isItemAcceptable(ItemStack itemstack) {
		if (itemstack.is(Items.SPORE_BLOSSOM))
			return true;
		else if (itemstack.is(ModItems.MYSTERIOUS_PETAL.get()))
			return true;
		else if (itemstack.is(ModItems.CUREBERRY.get()) || itemstack.is(ModItems.EXP_BERRY.get()) || itemstack.is(ModItems.RANDOMBERRY.get()))
			return true;
		else if (itemstack.getItem() != null && itemstack.getItem() instanceof BlockItem blockitem && blockitem.getBlock() instanceof FlowerBlock)
			return true;
		else return false;
	}*/

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
