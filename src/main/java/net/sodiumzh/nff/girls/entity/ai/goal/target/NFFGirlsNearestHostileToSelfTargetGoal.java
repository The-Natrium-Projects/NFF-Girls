package net.sodiumzh.nff.girls.entity.ai.goal.target;

import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;

public class NFFGirlsNearestHostileToSelfTargetGoal extends NFFNearestUnfriendlyMobTargetGoal
{

	public NFFGirlsNearestHostileToSelfTargetGoal(INFFGirlsTamed pMob)
	{		
		super(pMob, true, true);
		stateConditions(bm -> INFFGirlsTamed.get(bm.asMob()).filter(INFFGirlsTamed::shouldAttackMobsHostileToSelf).isPresent());
		targetOfTargetConditions(living -> living != null && living.equals(pMob.asMob()));
		allowAllStatesExceptWait();
	}

}
