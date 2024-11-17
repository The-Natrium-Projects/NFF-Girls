package net.sodiumzh.nff.girls.entity.ai.goal.target;

import java.util.function.Predicate;

import net.minecraft.world.entity.Mob;
import net.sodiumzh.nff.girls.befriendmobs.entity.ai.target.NFFNearestUnfriendlyMobTargetGoal;
import net.sodiumzh.nff.girls.entity.INFFGirlTamed;

public class NFFGirlsNearestHostileToSelfTargetGoal extends NFFNearestUnfriendlyMobTargetGoal
{

	public NFFGirlsNearestHostileToSelfTargetGoal(INFFGirlTamed pMob, Predicate<Mob> condition)
	{		
		super(pMob, true, true);
		stateConditions(bm -> bm instanceof INFFGirlTamed dbm && dbm.shouldAttackMobsHostileToSelf());
		//targetOfTargetCondition(living -> living instanceof Mob && ((Mob)living).getTarget() == pMob.asMob());
		allowAllStatesExceptWait();
	}
	
	public NFFGirlsNearestHostileToSelfTargetGoal(INFFGirlTamed pMob)
	{
		this(pMob, l -> true);
	}

}
