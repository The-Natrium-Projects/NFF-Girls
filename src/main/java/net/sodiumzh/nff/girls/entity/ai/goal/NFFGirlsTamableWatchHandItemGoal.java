package net.sodiumzh.nff.girls.entity.ai.goal;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import net.sodiumzh.nff.girls.entity.tamingprocess.NFFGirlsItemDroppingTamingProcess;
import net.sodiumzh.nff.services.entity.taming.NFFTamableComponent;
import net.sodiumzh.nff.services.entity.taming.NFFTamingMapping;

import java.util.EnumSet;

public class NFFGirlsTamableWatchHandItemGoal extends Goal
{
	protected final Mob mob;
	protected final NFFTamableComponent tamable;
	
	public NFFGirlsTamableWatchHandItemGoal(Mob mobBefriendable)
	{
		this.mob = mobBefriendable;
		this.tamable = NFFTamableComponent.getOptional(mob)
			.orElseThrow(() -> new UnsupportedOperationException("This goal supports only mobs with CNFFTamable capability."));
		if (!(NFFTamingMapping.getProcess(mob) instanceof NFFGirlsItemDroppingTamingProcess))
			throw new UnsupportedOperationException("This goal supports befriendable mobs only with NFFGirlsItemDroppingTamingProcess as befriending handler.");
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
	}
	
	@Override
	public boolean canUse() {
		return tamable.getTimerComponent().hasGeneralTimer(NFFGirlsItemDroppingTamingProcess.TIMER_KEY_PICKING_COOLDOWN);
	}
	
	@Override
	public void tick()
	{
		mob.getNavigation().stop();
		Vec3 v = mob.position();
		mob.getMoveControl().setWantedPosition(v.x, v.y, v.z, 1);
	}
}
