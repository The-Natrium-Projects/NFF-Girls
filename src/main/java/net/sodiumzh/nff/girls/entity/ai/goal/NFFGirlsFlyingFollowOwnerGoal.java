package net.sodiumzh.nff.girls.entity.ai.goal;

import net.sodiumzh.nff.girls.entity.NFFGirlsDataAccessor;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFFlyingFollowOwnerGoal;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;

public class NFFGirlsFlyingFollowOwnerGoal extends NFFFlyingFollowOwnerGoal
{
	public NFFGirlsFlyingFollowOwnerGoal(INFFTamed mob, double moveSpeed) {
		super(mob, moveSpeed);
	}

	public NFFGirlsFlyingFollowOwnerGoal(INFFTamed mob)
	{
		super(mob);
	}

	@Override
	public boolean checkCanUse()
	{
		return super.checkCanUse() && !NFFGirlsDataAccessor.isLowFavorability(mob.asMob());
	}
	
}
