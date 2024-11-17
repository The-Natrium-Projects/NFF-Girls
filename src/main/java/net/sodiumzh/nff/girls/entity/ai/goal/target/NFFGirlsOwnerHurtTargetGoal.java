package net.sodiumzh.nff.girls.entity.ai.goal.target;

import net.sodiumzh.nff.services.entity.ai.goal.presets.target.NFFOwnerHurtTargetGoal;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;

public class NFFGirlsOwnerHurtTargetGoal extends NFFOwnerHurtTargetGoal
{

	public NFFGirlsOwnerHurtTargetGoal(INFFTamed inMob)
	{
		super(inMob);
	}
	
	@Override
	public boolean checkCanUse()
	{
		return super.checkCanUse(); //&& !CNFFGirlsFavorabilityHandler.isLowFavorability(mob.asMob());
	}
	
}
