package net.sodiumzh.nff.girls.entity.ai.goal;

import net.minecraft.world.entity.LivingEntity;
import net.sodiumzh.nff.girls.entity.hmag.HmagCreeperGirlEntity;
import net.sodiumzh.nff.services.entity.ai.goal.presets.NFFMeleeAttackGoal;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;

public class NFFGirlsHmagCreeperGirlExplosionAttackGoal extends NFFMeleeAttackGoal
{

	HmagCreeperGirlEntity creeper;

	@Override
	public boolean checkCanUse()
	{
		if (!creeper.canAutoBlowEnemy)
			return false;
		if (creeper.getSwellDir() > 0)
			return false;
		if (creeper.blowEnemyCooldown > 0)
			return false;
		if (!creeper.hasEnoughAmmoToExplode())
			return false;
		if (creeper.getTarget() == null)
			return false;
		if (creeper.getOwner() != null && creeper.explodeSafeDistance * creeper.explodeSafeDistance > creeper.getTarget().distanceToSqr(creeper.getOwner()))
			return false;
		return super.checkCanUse();
	}
	
	public NFFGirlsHmagCreeperGirlExplosionAttackGoal(INFFTamed mob, double speedModifier, boolean followingTargetEvenIfNotSeen)
	{
		super(mob, speedModifier, followingTargetEvenIfNotSeen);
		this.creeper = (HmagCreeperGirlEntity)mob;
	}

	@Override
	protected void checkAndPerformAttack(LivingEntity enemy) {
		if (mob.asMob().distanceToSqr(enemy) < 9.0d && creeper.blowEnemyCooldown == 0)
		{
			this.resetAttackCooldown();
			creeper.setSwellDir(1);
		}
	}
	
}
