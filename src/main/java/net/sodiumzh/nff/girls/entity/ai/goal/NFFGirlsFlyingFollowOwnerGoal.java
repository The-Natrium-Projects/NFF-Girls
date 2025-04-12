package net.sodiumzh.nff.girls.entity.ai.goal;

import net.sodiumzh.nff.girls.entity.capability.CNFFGirlsFavorabilityHandler;
import net.sodiumzh.nff.services.entity.ai.goal.presets.NFFFlyingFollowOwnerGoal;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;

public class NFFGirlsFlyingFollowOwnerGoal extends NFFFlyingFollowOwnerGoal
{

	public NFFGirlsFlyingFollowOwnerGoal(INFFTamed mob)
	{
		super(mob);
	}

	@Override
	public boolean checkCanUse()
	{
		return super.checkCanUse() && !CNFFGirlsFavorabilityHandler.isLowFavorability(mob.asMob());
	}
	
}
