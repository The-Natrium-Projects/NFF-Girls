package net.sodiumzh.nff.girls.entity.tamingprocess.hmag;

import com.github.mechalopa.hmag.registry.ModItems;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.sodiumzh.nautils.statics.NaUtilsMathStatics;
import net.sodiumzh.nff.girls.registry.NFFGirlsItems;
import net.sodiumzh.nautils.math.RandomSelection;

public class HmagWitherSkeletonGirlTamingProcess extends NFFGirlsZombieGirlTamingProcess
{	
	@Override
	public boolean isItemAcceptable(ItemStack item)
	{
		return super.isItemAcceptable(item) || item.is(Items.NETHER_STAR);
	}
	
	@Override
	protected double getProcValueToAdd(ItemStack item, Player player, Mob mob, double lastProc) {
		if (item.is(NFFGirlsItems.SOUL_CAKE_SLICE.get()))
			return RandomSelection.createDouble(0.15d)
					.add(0.30d, 0.15d)
					.add(0.45d, 0.04d)
					.add(0.60d, 0.01d)
					.getDouble();
		else if (item.is(Items.NETHER_STAR))
			return RandomSelection.createDouble(0.50d)
					.add(1.00d, 0.20d)
					.getDouble();
		else if (item.is(ModItems.SOUL_POWDER.get()))
			return NaUtilsMathStatics.rndRangedDouble(0.005d, 0.01d);
		else if (item.is(ModItems.SOUL_APPLE.get()))
			return NaUtilsMathStatics.rndRangedDouble(0.01d, 0.02d);
		else return 0;
	}
}
