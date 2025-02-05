package net.sodiumzh.nff.girls.entity;

import net.minecraft.world.entity.Mob;
import net.sodiumzh.nautils.statics.NaUtilsParticleStatics;
import net.sodiumzh.nff.services.entity.taming.INFFDefaultProgressedTamingProcess;

public class NFFGirlsTamingRules {

	public static final double CONTINUOUS_PROGRESS_LOSS_RATE = 0.001d;  // 0.02/s
	public static final int COOLDOWN_SHORT = 2 * 20;
	public static final int COOLDOWN_MIDDLE = 3 * 20;
	public static final int COOLDOWN_LONG = 5 * 20;

	public static void tickContinuousProgressLoss(INFFDefaultProgressedTamingProcess<? super Mob> process, Mob mob) {
		process.addProgressValue(mob, -CONTINUOUS_PROGRESS_LOSS_RATE);
		NaUtilsParticleStatics.sendSmokeParticlesToEntityDefault(mob);
		if (process.getProgressValue(mob).orElse(1d) <= 0d)
			process.asProcess().interruptAll(mob, true);
	}

}
