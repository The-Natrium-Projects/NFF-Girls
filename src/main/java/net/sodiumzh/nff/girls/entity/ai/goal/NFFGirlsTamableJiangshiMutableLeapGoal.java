package net.sodiumzh.nff.girls.entity.ai.goal;

import com.github.mechalopa.hmag.registry.ModSoundEvents;
import com.github.mechalopa.hmag.world.entity.JiangshiEntity;
import com.github.mechalopa.hmag.world.entity.ai.goal.LeapAtTargetGoal2;

import net.minecraft.world.entity.Mob;

public class NFFGirlsTamableJiangshiMutableLeapGoal extends LeapAtTargetGoal2
{
	protected final JiangshiEntity mob;
	protected float leapHeightBonus = 0f;
	
	
	public NFFGirlsTamableJiangshiMutableLeapGoal(JiangshiEntity mob)
	{
		super(mob, 0.4F, 0.2F, 8.0F, 12);
		this.mob = mob;
	}

	@Override
	public boolean canUse()
	{
		return super.canUse() && this.mob.hasLineOfSight(this.mob.getTarget());
	}

	@Override
	public void start()
	{
		super.start();
		this.mob.playSound(ModSoundEvents.JIANGSHI_JUMP.get(), 0.8F, 1.0F);
	}

	@Override
	public float getMaxAttackDistance()
	{
		return super.getMaxAttackDistance() - 3.0F * ((float)mob.getSpeedBonus() / (float)JiangshiEntity.SPEED_BONUS_MAX);
	}

	@Override
	public double getXZD()
	{
		return super.getXZD() + 0.3D * ((double)mob.getSpeedBonus() / (double)JiangshiEntity.SPEED_BONUS_MAX) + leapHeightBonus;
	}
	
	public static void setLeapHeightBonus(Mob mob, float value)
	{
		if (!(mob instanceof JiangshiEntity))
			return;
		for (var wg: mob.goalSelector.getAvailableGoals())
		{
			if (wg.getGoal() instanceof NFFGirlsTamableJiangshiMutableLeapGoal goal)
			{
				goal.leapHeightBonus = value;
			}
		}
	}
	
}
