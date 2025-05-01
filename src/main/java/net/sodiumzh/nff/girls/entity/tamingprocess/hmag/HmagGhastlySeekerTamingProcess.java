package net.sodiumzh.nff.girls.entity.tamingprocess.hmag;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.sodiumzh.nff.girls.registry.NFFGirlsAngerRules;
import net.sodiumzh.nfu.entity.anger.MobAngerRules;

public class HmagGhastlySeekerTamingProcess extends HmagVanillaUndeadTamingProcess {

	@Override
	public boolean additionalConditions(Player player, Mob mob) {
		return mob.level().dimension().equals(Level.OVERWORLD) && mob.level().canSeeSky(mob.blockPosition());
	}

	@Override
	public MobAngerRules getAngerRules() {
		return NFFGirlsAngerRules.DEFAULT.get();
	}
}
