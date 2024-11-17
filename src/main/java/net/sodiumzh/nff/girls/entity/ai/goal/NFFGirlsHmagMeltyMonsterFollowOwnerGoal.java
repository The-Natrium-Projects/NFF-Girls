package net.sodiumzh.nff.girls.entity.ai.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;

public class NFFGirlsHmagMeltyMonsterFollowOwnerGoal extends NFFGirlsFollowOwnerGoal
{

	protected float oldLavaCost;
	
	public NFFGirlsHmagMeltyMonsterFollowOwnerGoal(INFFTamed inMob, double pSpeedModifier, float pStartDistance,
			float pStopDistance, boolean pCanFly)
	{
		super(inMob, pSpeedModifier, pStartDistance, pStopDistance, pCanFly);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected boolean canTeleportTo(BlockPos pos)
	{
		if (super.canTeleportTo(pos))
			return true;
		if (mob.asMob().level().getBlockState(pos).is(Blocks.LAVA))
		{
			BlockPos blockpos = pos.subtract(getPathfinder().blockPosition());
			return this.level.noCollision(getPathfinder(), getPathfinder().getBoundingBox().move(blockpos));
		} 
		else return false;
	}
	
	@Override
	public void onStart()
	{
		super.onStart();
		this.oldLavaCost = getPathfinder().getPathfindingMalus(BlockPathTypes.LAVA);
		getPathfinder().setPathfindingMalus(BlockPathTypes.LAVA, 0.0F);
	}
	
	@Override
	public void onStop()
	{
		super.onStop();
		getPathfinder().setPathfindingMalus(BlockPathTypes.LAVA, oldLavaCost);
	}
	
	
}



