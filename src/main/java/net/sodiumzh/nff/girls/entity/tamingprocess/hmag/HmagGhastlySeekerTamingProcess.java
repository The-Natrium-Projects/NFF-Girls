package net.sodiumzh.nff.girls.entity.tamingprocess.hmag;

import java.util.HashSet;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.sodiumzh.nff.services.entity.taming.TamableHatredReason;

public class HmagGhastlySeekerTamingProcess extends HmagVanillaUndeadTamingProcess
{

	@Override
	public boolean additionalConditions(Player player, Mob mob)
	{
		return mob.level.dimension().equals(Level.OVERWORLD) && mob.level.canSeeSky(mob.blockPosition());
	}
	
	@Override
	public HashSet<TamableHatredReason> getAddHatredReasons() {
		HashSet<TamableHatredReason> set = new HashSet<TamableHatredReason>();
		set.add(TamableHatredReason.ATTACKED);
		return set;
	}
	
	/*@Override
	protected double getProcValueToAdd(ItemStack item, Player player, Mob mob, double lastProc) {
		double rnd = this.rnd.nextDouble();
		if (item.is(NFFGirlsItems.SOUL_CAKE_SLICE.get()))
			return rnd < 0.01 ? 1.d : (rnd < 0.05d ? 0.75d : (rnd < 0.2d ? 0.50d : 0.25d));
		else if (item.is(ModItems.SOUL_POWDER.get()))
			return NaUtilsMathStatics.rndRangedDouble(0.015, 0.03);
		else if (item.is(ModItems.SOUL_APPLE.get()))
			return NaUtilsMathStatics.rndRangedDouble(0.03, 0.06);
		else return 0;
	}*/
	
	@Override
	public void onAttackProcessingPlayer(Mob mob, Player player, boolean damageGiven)
	{
	}
}
