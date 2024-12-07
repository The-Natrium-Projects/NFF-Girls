package net.sodiumzh.nff.girls.entity.tamingprocess.hmag;

import com.github.mechalopa.hmag.registry.ModItems;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.sodiumzh.nautils.statics.NaUtilsMathStatics;
import net.sodiumzh.nff.girls.registry.NFFGirlsItems;

public class HmagDrownedGirlTamingProcess extends NFFGirlsZombieGirlTamingProcess
{
	@Override
	protected double getProcValueToAdd(ItemStack item, Player player, Mob mob, double lastProc) {
		double rnd = this.rnd.nextDouble();
		if (item.is(NFFGirlsItems.SOUL_CAKE_SLICE.get()))
			return rnd < 0.05 ? 1.00 : (rnd < 0.2 ? 0.666667 : 0.333334);
		else if (item.is(ModItems.SOUL_POWDER.get()))
			return NaUtilsMathStatics.rndRangedDouble(0.02, 0.04);
		else if (item.is(ModItems.SOUL_APPLE.get()))
			return NaUtilsMathStatics.rndRangedDouble(0.04, 0.08);
		else return 0;
	}
}
