package net.sodiumzh.nff.girls.entity.ai.goal.target;

import net.minecraft.world.entity.Mob;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;

import java.util.function.Predicate;

public class NFFGirlsNearestHostileToOwnerTargetGoal extends NFFNearestUnfriendlyMobTargetGoal
{
	public NFFGirlsNearestHostileToOwnerTargetGoal(INFFGirlsTamed mob)
	{		
		super(mob, true, true);
		stateConditions(bm -> INFFGirlsTamed.get(bm).filter(INFFGirlsTamed::shouldAttackMobsHostileToSelf).isPresent());
		targetOfTargetConditions(living -> living != null && living.equals(mob.getOwnerInDimension()));
		allowAllStatesExceptWait();
	}
}
