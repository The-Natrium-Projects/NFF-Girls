package net.sodiumzh.nff.girls.entity.ai.goal;

import java.util.EnumSet;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import net.sodiumzh.nff.girls.entity.tamingprocess.hmag.NFFGirlsItemDroppingTamingProcess;
import net.sodiumzh.nff.services.entity.capability.CNFFTamable;
import net.sodiumzh.nff.services.entity.taming.NFFTamingMapping;
import net.sodiumzh.nff.services.registry.NFFCapRegistry;

public class NFFGirlsTamableWatchHandItemGoal extends Goal
{
	protected final Mob mob;
	protected final CNFFTamable cap;
	
	public NFFGirlsTamableWatchHandItemGoal(Mob mobBefriendable)
	{
		this.mob = mobBefriendable;
		if (mob.getCapability(NFFCapRegistry.CAP_BEFRIENDABLE_MOB).isPresent())
		{
			cap = CNFFTamable.getCap(mob);
		}
		else throw new UnsupportedOperationException("This goal supports only mobs with CNFFTamable capability.");
		if (!(NFFTamingMapping.getHandler(mob) instanceof NFFGirlsItemDroppingTamingProcess))
			throw new UnsupportedOperationException("This goal supports befriendable mobs only with NFFGirlsItemDroppingTamingProcess as befriending handler.");
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
	}
	
	@Override
	public boolean canUse() {
		return cap.hasTimer("hold_item_time");
	}
	
	@Override
	public void tick()
	{
		mob.getNavigation().stop();
		Vec3 v = mob.position();
		mob.getMoveControl().setWantedPosition(v.x, v.y, v.z, 1);
	}
}
