package net.sodiumzh.nff.girls.entity.tamingprocesses.hmag;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.sodiumzh.nautils.entity.anger.MobAngerRules;
import net.sodiumzh.nff.girls.registry.NFFGirlsAngerRules;

public class HmagGhastlySeekerTamingProcess extends HmagVanillaUndeadTamingProcess {

	@Override
	public boolean additionalConditions(Player player, Mob mob) {
		return mob.level.dimension().equals(Level.OVERWORLD) && mob.level.canSeeSky(mob.blockPosition());
	}

	@Override
	public MobAngerRules getAngerRules() {
		return NFFGirlsAngerRules.DEFAULT.get();
	}
}
