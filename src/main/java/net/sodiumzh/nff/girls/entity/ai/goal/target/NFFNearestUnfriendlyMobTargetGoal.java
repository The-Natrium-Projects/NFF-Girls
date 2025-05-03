package net.sodiumzh.nff.girls.entity.ai.goal.target;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.sodiumzh.nff.services.entity.ai.goal.NFFTargetGoal;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

/**
 * Makes the mob to attack the nearest mob targeting a given LivingEntity.
 */
public class NFFNearestUnfriendlyMobTargetGoal extends NFFTargetGoal
{
	// private static final int DEFAULT_RANDOM_INTERVAL = 10;
	protected final int randomInterval;
	@Nullable
	protected LivingEntity target;
	// The condition that a mob's target should fulfill for this mob to be selected by this goal.
	// By default it's the owner mob itself
	protected Predicate<LivingEntity> targetOfTargetCondition ;
	/**
	 * This filter is applied to the Entity search. Only matching entities will be
	 * targeted.
	 */
	protected TargetingConditions targetConditions;
	/** Attacker state filter. It will try targeting only when the attack fulfills this check. */
	protected Predicate<INFFTamed> stateConditions = m -> true;

	public NFFNearestUnfriendlyMobTargetGoal(INFFTamed mob, boolean mustSee)
	{
		this(mob, 10, mustSee, false, (Predicate<LivingEntity>) null);
	}

	public NFFNearestUnfriendlyMobTargetGoal(INFFTamed mob, boolean mustSee,
			Predicate<LivingEntity> targetPredicate)
	{
		this(mob, 10, mustSee, false, targetPredicate);
	}

	public NFFNearestUnfriendlyMobTargetGoal(INFFTamed mob, boolean mustSee, boolean mustReach)
	{
		this(mob, 10, mustSee, mustReach, (Predicate<LivingEntity>) null);
	}

	public NFFNearestUnfriendlyMobTargetGoal(INFFTamed mob, int pRandomInterval, boolean mustSee,
			boolean mustReach, @Nullable Predicate<LivingEntity> targetPredicate)
	{
		super(mob, mustSee, mustReach);
		this.randomInterval = reducedTickDelay(pRandomInterval);
		this.setFlags(EnumSet.of(Goal.Flag.TARGET));
		this.targetConditions = TargetingConditions.forCombat().range(this.getFollowDistance())
				.selector(targetPredicate);
		this.allowAllStatesExceptWait();
		this.targetOfTargetCondition = m -> (m == mob.asMob());
	}

	/**
	 * Set state conditions. The {@code INFFTamed} using this goal needs to pass this check to start.
	 */
	public NFFNearestUnfriendlyMobTargetGoal stateConditions(Predicate<INFFTamed> condition)
	{
		stateConditions = condition;
		return this;
	}
	
	/**
	 * Set the condition for the targets of the mob's target candidates. The target mob must have a target satisfying this to be set as this mob's target.
	 */
	public NFFNearestUnfriendlyMobTargetGoal targetOfTargetConditions(Predicate<LivingEntity> cond)
	{
		targetOfTargetCondition = cond;
		return this;
	}
	
	@Override
	public boolean checkCanUse() {
		if (!stateConditions.test(mob))
			return false;
		if (this.randomInterval > 0 && this.mob.asMob().getRandom().nextInt(this.randomInterval) != 0)
		{
			return false;
		} else
		{
			this.findTarget();
			return this.target != null;
		}
	}

	protected AABB getTargetSearchArea(double targetDistance) {
		return this.mob.asMob().getBoundingBox().inflate(targetDistance, 4.0D, targetDistance);
	}

	protected void findTarget() {
	      double followDist = mob.asMob().getAttributeValue(Attributes.FOLLOW_RANGE);
	      AABB searchArea = new AABB(mob.asMob().position().subtract(new Vec3(followDist, followDist, followDist)), mob.asMob().position().add(new Vec3(followDist, followDist, followDist)));
	      List<Entity> candidates = mob.asMob().level.getEntities(mob.asMob(), searchArea, (Entity e) -> 
	      {
	    	  if (e instanceof Mob m)
	    	  {
	    		 return mob.asMob().hasLineOfSight(m)
	    			&& mob.asMob().distanceToSqr(m) <= followDist * followDist
	    			&& targetConditions.test(mob.asMob(), m)
	    			&& m.getTarget() != null
	    			&& targetOfTargetCondition.test(m.getTarget());
	    	  }
	    	  return false;
	      });
	      if (candidates.size() <= 0)
	      {
	    	  this.target = null;
	    	  return;
	      }
	      Mob setTarget = null;
	      for (Entity e: candidates)
	      {
	    	  if (e instanceof Mob m)
	    	  {
	    		  if (setTarget == null || mob.asMob().distanceToSqr(m) > mob.asMob().distanceToSqr(setTarget))
	    		  {
	    			  setTarget = m;
	    		  }
	    	  }
	      }
	      this.target = setTarget;
	      return;
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void onStart() {
		this.mob.asMob().setTarget(this.target);
		super.onStart();
	}

	public void setTarget(@Nullable LivingEntity target) {
		this.target = target;
	}

}
