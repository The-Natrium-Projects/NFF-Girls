package net.sodiumzh.nff.girls.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFFlyingMoveGoal;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nfu.container.Tuple2;

import javax.annotation.Nullable;
import java.util.Comparator;

/**
 * Common interface of flying mobs that can actively collect honey from flower blocks and then fly back.
 * WIP
 */
public interface IHoneyCollecting {

    public int getCurrentHoneyCollectingCooldown();

    public int getOverallHoneyCollectingCooldown();

    public int getHoneyLevel();

    public void setHoneyLevel(int val);

    public int getMaxHoneyLevel();

    public boolean shouldFlyBack();

    public boolean isFlyingBack();

    public static class BeeCollectHoneyGoal extends NFFFlyingMoveGoal {

        private final IHoneyCollecting collecting;
        @Nullable
        private Tuple2<BlockPos, BlockState> targetBlockState = null;
        private int resetTimer = 0; // Prevent getting stuck somewhere

        public BeeCollectHoneyGoal(INFFTamed mob, double speed) {
            super(mob, speed);
            this.collecting = (IHoneyCollecting) mob;
        }

        public BeeCollectHoneyGoal(INFFTamed mob) {
            super(mob);
            this.collecting = (IHoneyCollecting) mob;
        }

        @Override
        public boolean checkCanUse() {
            if (mob.asMob().tickCount % 100 != 1) return false; // As this goal will search blocks, don't call it too frequently
            if (collecting.isFlyingBack()) return false;
            if (collecting.shouldFlyBack()) return false;
            if (collecting.getMaxHoneyLevel() > 0 && collecting.getHoneyLevel() >= collecting.getMaxHoneyLevel()) return false;
            if (collecting.getCurrentHoneyCollectingCooldown() > 0) return false;
            Level level = mob.asMob().level;
            targetBlockState = BlockPos.betweenClosedStream(mob.asMob().getBoundingBox().inflate(15d, 15d, 7d))
                .filter(pos -> level.getBlockState(pos).getBlock() instanceof FlowerBlock)
                .map(pos -> Tuple2.of(new BlockPos(pos.getX(), pos.getY(), pos.getZ()), mob.asMob().level.getBlockState(pos)))
                .filter(tp -> mob.asMob().level.clip(new ClipContext(
                    mob.asMob().position(), new Vec3(tp.getA().getX(), tp.getA().getY(), tp.getA().getZ()),
                    ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, mob.asMob())).getType() == HitResult.Type.MISS)
                .min(Comparator.comparingDouble(tp -> tp.getA().distSqr(mob.asMob().blockPosition())))
                .orElse(null);
            return targetBlockState != null;
        }

        @Override
        public boolean checkCanContinueToUse() {
            return super.checkCanContinueToUse();
        }

        @Override
        public void onTick() {
            super.onTick();
        }

        @Override
        public void onStop() {
            super.onStop();
        }
    }



}
