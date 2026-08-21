/**
 * 
 */
package net.sodiumzh.nff.girls.entity.ai.goal;

import net.sodiumzh.nff.girls.entity.NFFGirlsDataAccessor;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFFollowOwnerGoal;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;

/**
 * @author SodiumZH
 *
 */
public class NFFGirlsFollowOwnerGoal extends NFFFollowOwnerGoal
{

	public NFFGirlsFollowOwnerGoal(INFFTamed inMob, double pSpeedModifier, float pStartDistance,
			float pStopDistance, boolean pCanFly)
	{
		super(inMob, pSpeedModifier, pStartDistance, pStopDistance, pCanFly);
	}
	
	@Override
	public boolean checkCanUse()
	{
		return super.checkCanUse() && !NFFGirlsDataAccessor.isLowFavorability(mob.asMob());
	}

}
