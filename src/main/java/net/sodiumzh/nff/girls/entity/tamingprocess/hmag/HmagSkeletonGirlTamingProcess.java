package net.sodiumzh.nff.girls.entity.tamingprocess.hmag;

import com.github.mechalopa.hmag.registry.ModItems;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.sodiumzh.nautils.statics.NaUtilsMathStatics;
import net.sodiumzh.nff.girls.registry.NFFGirlsItems;

public class HmagSkeletonGirlTamingProcess extends NFFGirlsZombieGirlTamingProcess
{
	@Override
	protected double getProcValueToAdd(ItemStack item, Player player, Mob mob, double lastProc) {
		double rnd = this.rnd.nextDouble();
		if (item.is(NFFGirlsItems.SOUL_CAKE_SLICE.get()))
			return rnd < 0.01 ? 1.d : (rnd < 0.05d ? 0.75d : (rnd < 0.2d ? 0.50d : 0.25d));
		else if (item.is(ModItems.SOUL_POWDER.get()))
			return NaUtilsMathStatics.rndRangedDouble(0.015, 0.03);
		else if (item.is(ModItems.SOUL_APPLE.get()))
			return NaUtilsMathStatics.rndRangedDouble(0.03, 0.06);
		else return 0;
	}
}
