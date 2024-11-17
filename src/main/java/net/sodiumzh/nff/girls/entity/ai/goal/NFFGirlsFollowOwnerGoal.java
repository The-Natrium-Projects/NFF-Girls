/**
 * 
 */
package net.sodiumzh.nff.girls.entity.ai.goal;

import net.sodiumzh.nff.girls.entity.capability.CNFFGirlsFavorabilityHandler;
import net.sodiumzh.nff.services.entity.ai.goal.presets.NFFFollowOwnerGoal;
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
		return super.checkCanUse() && !CNFFGirlsFavorabilityHandler.isLowFavorability(mob.asMob());
	}

}
