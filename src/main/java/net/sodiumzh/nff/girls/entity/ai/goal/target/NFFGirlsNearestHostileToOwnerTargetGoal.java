package net.sodiumzh.nff.girls.entity.ai.goal.target;

import java.util.function.Predicate;

import net.minecraft.world.entity.Mob;
import net.sodiumzh.nff.girls.befriendmobs.entity.ai.target.NFFNearestUnfriendlyMobTargetGoal;
import net.sodiumzh.nff.girls.entity.INFFGirlTamed;

public class NFFGirlsNearestHostileToOwnerTargetGoal extends NFFNearestUnfriendlyMobTargetGoal
{
	public NFFGirlsNearestHostileToOwnerTargetGoal(INFFGirlTamed mob, Predicate<Mob> condition)
	{		
		super(mob, true, true);
		stateConditions(bm -> bm instanceof INFFGirlTamed dbm && dbm.shouldAttackMobsHostileToOwner());
		targetOfTargetConditions(living -> mob.isOwnerPresent() && living == mob.getOwner());
		allowAllStatesExceptWait();
	}
	
	public NFFGirlsNearestHostileToOwnerTargetGoal(INFFGirlTamed pMob)
	{
		this(pMob, l -> true);
	}
}
