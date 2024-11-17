package net.sodiumzh.nff.girls.entity.ai.goal.target;

import java.util.function.Predicate;

import net.minecraft.world.entity.Mob;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.services.entity.ai.goal.presets.target.NFFNearestUnfriendlyMobTargetGoal;

public class NFFGirlsNearestHostileToSelfTargetGoal extends NFFNearestUnfriendlyMobTargetGoal
{

	public NFFGirlsNearestHostileToSelfTargetGoal(INFFGirlsTamed pMob, Predicate<Mob> condition)
	{		
		super(pMob, true, true);
		stateConditions(bm -> bm instanceof INFFGirlsTamed dbm && dbm.shouldAttackMobsHostileToSelf());
		//targetOfTargetCondition(living -> living instanceof Mob && ((Mob)living).getTarget() == pMob.asMob());
		allowAllStatesExceptWait();
	}
	
	public NFFGirlsNearestHostileToSelfTargetGoal(INFFGirlsTamed pMob)
	{
		this(pMob, l -> true);
	}

}
