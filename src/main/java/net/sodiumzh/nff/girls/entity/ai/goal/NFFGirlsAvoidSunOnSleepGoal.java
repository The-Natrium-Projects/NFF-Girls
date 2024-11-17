package net.sodiumzh.nff.girls.entity.ai.goal;

import net.minecraft.world.entity.EquipmentSlot;
import net.sodiumzh.nff.services.entity.ai.goal.presets.NFFFleeSunGoal;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;


// TODO Why did I make this? It appears to make the mob flee sun when owner is sleeping? Maybe should merge into` NFFFleeSunGoal?
public class NFFGirlsAvoidSunOnSleepGoal extends NFFFleeSunGoal
{

	public NFFGirlsAvoidSunOnSleepGoal(INFFTamed pMob, double pSpeedModifier)
	{
		super(pMob, pSpeedModifier);
	}

	@Override
	public boolean checkCanUse()
	{
		if (isDisabled())
			return false;
		if (!mob.getOwner().isSleeping())
			return false;
		// Not taking effect when player is > 16 blocks away
		else if (mob.asMob().distanceToSqr(mob.getOwner()) > 256f)
			return false;
		else if (!this.level.canSeeSky(getPathfinder().blockPosition())) 
			return false;
		else if (!ignoreHelmet && !getPathfinder().getItemBySlot(EquipmentSlot.HEAD).isEmpty())
			return false;
		else if	(!this.setWantedPos())
			return false;
		else if (mob.asMob().isInWater())
			return false;
		else return true;
	}
}
