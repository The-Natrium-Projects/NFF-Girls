package net.sodiumzh.nff.girls.entity.tamingprocess;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class HmagGhastlySeekerTamingProcess extends HmagVanillaUndeadTamingProcess {

	@Override
	public boolean additionalConditions(Player player, Mob mob) {
		return mob.level.dimension().equals(Level.OVERWORLD) && mob.level.canSeeSky(mob.blockPosition());
	}

}
