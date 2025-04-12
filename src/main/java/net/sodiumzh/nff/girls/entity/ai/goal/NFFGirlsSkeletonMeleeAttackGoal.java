package net.sodiumzh.nff.girls.entity.ai.goal;

import net.sodiumzh.nff.services.entity.ai.goal.presets.NFFMeleeAttackGoal;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;


public class NFFGirlsSkeletonMeleeAttackGoal extends NFFMeleeAttackGoal
{

	public NFFGirlsSkeletonMeleeAttackGoal(INFFTamed pMob, double pSpeedModifier,
			boolean pFollowingTargetEvenIfNotSeen)
	{
		super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
	}

}
