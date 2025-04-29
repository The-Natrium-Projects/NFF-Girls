package net.sodiumzh.nff.girls.entity.ai.goal;

import java.util.EnumSet;

import javax.annotation.Nonnull;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.LevelReader;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFFollowOwnerGoal;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;

/* Adjusted from vanilla FollowOwnerGoal for TameableAnimal */
public class NFFGirlsInWaterFollowOwnerGoal extends NFFFollowOwnerGoal {

	public static final int TELEPORT_WHEN_DISTANCE_IS = 12;
	protected static final int MIN_HORIZONTAL_DISTANCE_FROM_PLAYER_WHEN_TELEPORTING = 2;
	protected static final int MAX_HORIZONTAL_DISTANCE_FROM_PLAYER_WHEN_TELEPORTING = 3;
	protected static final int MAX_VERTICAL_DISTANCE_FROM_PLAYER_WHEN_TELEPORTING = 1;
	protected final LevelReader level;
	protected final double speedModifier;
	protected final PathNavigation navigation;
	protected int timeToRecalcPath;
	protected final float stopDistance;
	protected final float startDistance;
	protected float oldWaterCost;
	protected final boolean canFly;

	public NFFGirlsInWaterFollowOwnerGoal(@Nonnull INFFTamed inMob, double pSpeedModifier, float pStartDistance,
			float pStopDistance) {
		super(inMob, pSpeedModifier, pStartDistance, pStartDistance, false);
		mob = inMob;
		this.level = mob.asMob().level;
		this.speedModifier = pSpeedModifier;
		this.navigation = getPathfinder().getNavigation();
		this.startDistance = pStartDistance;
		this.stopDistance = pStopDistance;
		this.canFly = false;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));

		allowState(FOLLOW);
	}

	/**
	 * Returns whether execution should begin. You can also read and cache any state
	 * necessary for execution in this handler as well.
	 */
	@Override
	public boolean checkCanUse() {
		if (isDisabled())
			return false;
		if (!mob.asMob().isInWater() && !mob.getOwner().isInWater())
			return false;
		return super.checkCanUse();
		}
	

}
