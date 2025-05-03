package net.sodiumzh.nff.girls.entity.ai.goal.target;

import net.sodiumzh.nff.girls.entity.capability.CNFFGirlsFavorabilityHandler;
import net.sodiumzh.nff.services.entity.ai.goal.preset.target.NFFOwnerHurtByTargetGoal;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;

public class NFFGirlsOwnerHurtByTargetGoal extends NFFOwnerHurtByTargetGoal
{

	public NFFGirlsOwnerHurtByTargetGoal(INFFTamed inMob)
	{
		super(inMob);
	}
	
	@Override
	public boolean checkCanUse()
	{
		return super.checkCanUse() && !CNFFGirlsFavorabilityHandler.isLowFavorability(mob.asMob());
	}
	
}
