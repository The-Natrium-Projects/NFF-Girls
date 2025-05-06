package net.sodiumzh.nff.girls.entity.ai.goal;

import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nff.services.entity.taming.preset.NFFTamedCreeperPreset;

import javax.annotation.Nonnull;

/* Adjusted from vanilla FollowOwnerGoal for TameableAnimal */
public class NFFGirlsCreeperFollowOwnerGoal extends NFFGirlsFollowOwnerGoal {

	protected NFFTamedCreeperPreset creeper;
	public NFFGirlsCreeperFollowOwnerGoal(@Nonnull INFFTamed inMob, double pSpeedModifier, float pStartDistance,
			float pStopDistance, boolean pCanFly) {
		super(inMob, pSpeedModifier, pStartDistance, pStopDistance, pCanFly);
		creeper = (NFFTamedCreeperPreset) inMob;
	}

	@Override
	public boolean checkCanUse()
	{
		if (creeper.getSwellDir() > 0)
			return false;
		return super.checkCanUse();
	}
	
	
}
