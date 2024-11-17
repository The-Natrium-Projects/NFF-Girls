package net.sodiumzh.nff.girls.entity.ai.goal;

import net.minecraft.world.item.BowItem;
import net.sodiumzh.nff.services.entity.ai.goal.presets.NFFRangedBowAttackGoal;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;

// Only for skeleton, stray and wither skeleton
// They consume arrows which locate at inventory 8 position
public class NFFGirlsHmagSkeletonRangedBowAttackGoal extends NFFRangedBowAttackGoal {

	public NFFGirlsHmagSkeletonRangedBowAttackGoal(INFFTamed pMob, double pSpeedModifier, int pAttackIntervalMin,
			float pAttackRadius) {
		super(pMob, pSpeedModifier, pAttackIntervalMin, pAttackRadius);
		allowAllStatesExceptWait();
	}
	
	@Override
	public boolean checkCanUse() {
		if (mob.getAdditionalInventory().getItem(4).isEmpty())
			return false;
		else if (mob.getAdditionalInventory().getItem(8).isEmpty())
			return false;
		else if (!(mob.getAdditionalInventory().getItem(4).getItem() instanceof BowItem))
			return false;
		else
			return super.checkCanUse();
	}
	
}
