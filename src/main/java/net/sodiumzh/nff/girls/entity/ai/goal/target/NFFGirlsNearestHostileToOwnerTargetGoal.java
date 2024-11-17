package net.sodiumzh.nff.girls.entity.ai.goal.target;

import java.util.function.Predicate;

import net.minecraft.world.entity.Mob;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.services.entity.ai.goal.presets.target.NFFNearestUnfriendlyMobTargetGoal;

public class NFFGirlsNearestHostileToOwnerTargetGoal extends NFFNearestUnfriendlyMobTargetGoal
{
	public NFFGirlsNearestHostileToOwnerTargetGoal(INFFGirlsTamed mob, Predicate<Mob> condition)
	{		
		super(mob, true, true);
		stateConditions(bm -> bm instanceof INFFGirlsTamed dbm && dbm.shouldAttackMobsHostileToOwner());
		targetOfTargetConditions(living -> mob.isOwnerPresent() && living == mob.getOwner());
		allowAllStatesExceptWait();
	}
	
	public NFFGirlsNearestHostileToOwnerTargetGoal(INFFGirlsTamed pMob)
	{
		this(pMob, l -> true);
	}
}
