package net.sodiumzh.nff.girls.entity.ai.goal;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.sodiumzh.nff.services.entity.ai.goal.NFFMoveGoal;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;

public class NFFGirlsAmphibiousLandInShallowWaterGoal extends NFFMoveGoal {
    public NFFGirlsAmphibiousLandInShallowWaterGoal(INFFTamed mob) {
        super(mob);
        this.isAmphibious = true;
    }

    public NFFGirlsAmphibiousLandInShallowWaterGoal(INFFTamed mob, double speedModifier) {
        super(mob, speedModifier);
        this.isAmphibious = true;
    }

    @Override
    public boolean checkCanUse() {
        Mob mob = this.getMob().asMob();
        if (!mob.isInWater()) return false;
        BlockState block = mob.level.getBlockState(mob.blockPosition());
        BlockState below = mob.level.getBlockState(mob.blockPosition().below());
        return (block.is(Blocks.WATER) || block.getValue(BlockStateProperties.WATERLOGGED))
            && below.entityCanStandOn(mob.level, mob.blockPosition().below(), mob)
            && !mob.isOnGround();
    }

}
