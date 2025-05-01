package net.sodiumzh.nff.girls.entity.ai.goal;

import net.sodiumzh.nff.services.entity.ai.NFFTamedMobAIState;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFRangedAttackGoal;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;

public class NFFGirlsRangedAttackGoal extends NFFRangedAttackGoal
{

	public NFFGirlsRangedAttackGoal(INFFTamed mob, double pSpeedModifier, int pAttackIntervalMin,
			int pAttackIntervalMax, float pAttackRadius)
	{
		super(mob, pSpeedModifier, pAttackIntervalMin, pAttackIntervalMax, pAttackRadius);
	}

	public NFFGirlsRangedAttackGoal(INFFTamed mob, double pSpeedModifier, int pAttackInterval,
			float pAttackRadius)
	{
		super(mob, pSpeedModifier, pAttackInterval, pAttackRadius);
	}

	@Override
	public boolean checkCanContinueToUse()
	{
		if (this.mob.isOwnerPresent() && this.mob.getAIState() == NFFTamedMobAIState.FOLLOW
				&& this.mob.asMob().distanceToSqr(this.mob.getOwner()) > 256d)
			return false;
		return super.checkCanContinueToUse();
	}
	
}
