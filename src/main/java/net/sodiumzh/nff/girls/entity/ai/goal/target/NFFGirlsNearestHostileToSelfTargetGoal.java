package net.sodiumzh.nff.girls.entity.ai.goal.target;

import net.minecraft.world.entity.Mob;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;

import java.util.function.Predicate;

public class NFFGirlsNearestHostileToSelfTargetGoal extends NFFNearestUnfriendlyMobTargetGoal
{

	public NFFGirlsNearestHostileToSelfTargetGoal(INFFGirlsTamed pMob)
	{		
		super(pMob, true, true);
		stateConditions(bm -> INFFGirlsTamed.get(bm).filter(INFFGirlsTamed::shouldAttackMobsHostileToSelf).isPresent());
		targetOfTargetConditions(living -> living != null && living.equals(pMob.asMob()));
		allowAllStatesExceptWait();
	}

}
