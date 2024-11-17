package net.sodiumzh.nff.girls.entity.ai.goal;

import java.util.EnumSet;

import com.github.mechalopa.hmag.world.entity.GhastlySeekerEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.sodiumzh.nautils.statics.NaUtilsMathStatics;
import net.sodiumzh.nff.girls.eventlisteners.NFFGirlsEntityEventListeners;

/**
 * Adjusted from {@code GhastlySeekerEntity$RandomFlyGoal}, mainly for behaviors outside Nether
 * <p>Adjustments:
 * <p>a) No change in Nether;
 * <p>b) Out of Nether, it will not fly over 32 blocks high or leave the 64x64 square area centered by players (ignoring Y).
 * <p> Handled in {@link NFFGirlsEntityEventListeners#onEntityJoinLevel}. 
 */
public class NFFGirlsTamableGhastlySeekerRandomFlyGoal extends Goal
{
	private final GhastlySeekerEntity gs;

	public NFFGirlsTamableGhastlySeekerRandomFlyGoal(GhastlySeekerEntity mob)
	{
		this.gs = mob;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE));
	}

	@Override
	public boolean canUse()
	{
		MoveControl movecontrol = this.gs.getMoveControl();

		if (!movecontrol.hasWanted())
		{
			return true;
		}
		else
		{
			double d0 = movecontrol.getWantedX() - this.gs.getX();
			double d1 = movecontrol.getWantedY() - this.gs.getY();
			double d2 = movecontrol.getWantedZ() - this.gs.getZ();
			double d3 = d0 * d0 + d1 * d1 + d2 * d2;
			return d3 < 1.0D || d3 > 3600.0D;
		}
	}

	@Override
	public boolean canContinueToUse()
	{
		return false;
	}

	@Override
	public void start()
	{
		// Don't move if no player nearby
		if (!gs.level().dimension().equals(Level.NETHER) && !hasPlayerNearby())
			return;
		RandomSource random = this.gs.getRandom();
		boolean flag = false;
		Vec3 newWanted = null;
		if (this.gs.getTarget() != null)
		{
			LivingEntity target = this.gs.getTarget();
			double distanceSqr = target.distanceToSqr(this.gs);

			if (distanceSqr < 64.d * 64.d && distanceSqr > 16.d * 16.d)
			{
				Vec3 targetPos = target.getEyePosition(1.0F);
				// Search at most 16 times for an allowed target position
				for (int i = 0; i < 16; ++i)
				{
					newWanted = new Vec3(targetPos.x + (random.nextFloat() * 2.0F - 1.0F) * 2.0F, targetPos.y - 1.0D, targetPos.z + (random.nextFloat() * 2.0F - 1.0F) * 2.0F);
					if (isAllowedTargetPosPosition(newWanted))
						break;
					else newWanted = null;
				}	
				flag = true;
			}
		}

		if (!flag)
		{
			double d0 = this.gs.getX() + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
			double d1 = this.gs.getY() + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
			double d2 = this.gs.getZ() + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
			for (int i = 0; i < 16; ++i)
			{
				newWanted = new Vec3(d0, d1, d2);
				if (isAllowedTargetPosPosition(newWanted))
					break;
				else newWanted = null;
			}
		}
		// If failed, maybe it's too high, try fly down
		if (newWanted == null)
		{
			double d0 = this.gs.getX() + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
			double d1 = this.gs.getY() + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
			double d2 = this.gs.getZ() + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
			BlockPos pos = NaUtilsMathStatics.getBlockPos(d0, d1, d2);
			while (gs.level().getBlockState(pos.below()).isAir() && pos.getY() > gs.level().getMinBuildHeight())
				pos = pos.below();
			pos = pos.above(31);
			Vec3 v = new Vec3(pos.getX(), pos.getY(), pos.getZ());
			if (isAllowedTargetPosPosition(v))
				newWanted = v;	
		}
		if (newWanted != null)
			this.gs.getMoveControl().setWantedPosition(newWanted.x, newWanted.y, newWanted.z, 0.75D);
	}

	protected boolean hasPlayerNearby()
	{
		// Search 64 x 64 area centered by the mob's xz
		Vec3 pos = gs.position();
		AABB bound = new AABB(pos.subtract(32, 32, 32), pos.add(32, 32, 32));
		bound = new AABB(bound.minX, gs.level().getMinBuildHeight(), bound.minZ, bound.maxX, gs.level().getMaxBuildHeight(), bound.maxZ);	
		// The mob will not move to position that isn't any player in 64x64 area centered by it
		return gs.level().getEntitiesOfClass(Player.class, bound).size() > 0;
	}
	
	protected boolean isAllowedTargetPosPosition(Vec3 pos)
	{
		Level level = gs.level();
		// No adjustment if in nether
		if (level.dimension().equals(Level.NETHER))
			return true;
		
		if (!hasPlayerNearby())
			return false;
		
		/* Check if it's too high */
		BlockPos blockpos = NaUtilsMathStatics.getBlockPos(pos.x, pos.y, pos.z);
		int height = 0;
		while (level.getBlockState(blockpos.below()).isAir())
		{
			blockpos = blockpos.below();
			height++;
			// case when it's above the void
			if (level.getMinBuildHeight() > blockpos.getY())
			{
				height = 9999;
				break;
			}
		}
		return height <= 32;
	}
	
}
