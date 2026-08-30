package net.sodiumzh.nff.girls.block;

import com.github.mechalopa.hmag.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.sodiumzh.nff.girls.entity.projectile.MobileParticleSourceEntity;
import net.sodiumzh.nff.girls.registry.NFFGirlsEntityTypes;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nfu.util.NFUEntityStatics;

import java.util.Random;

public class SoulWheatBlock extends CropBlock {

    protected static final Random RND = new Random();

    public SoulWheatBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected boolean mayPlaceOn(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return pState.is(ModBlocks.SOUL_POWDER_BLOCK.get());
    }

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, Random pRandom) {
        if (RND.nextFloat() < 0.2d // 5x slower growth than vanilla wheat
            // Only grows under night sky or in Nether/End
            && (pLevel.isNight() && pLevel.canSeeSky(pPos)
                || pLevel.dimension().equals(ServerLevel.NETHER)
                || pLevel.dimension().equals(ServerLevel.END)))
        {
            // At least a friended undead mob around
            Mob friendedUndead = pLevel.getEntitiesOfClass(Mob.class, new AABB(pPos.getX() - 8d, pPos.getY() - 8d, pPos.getZ() - 8d, pPos.getX() + 8d, pPos.getY() + 8d, pPos.getZ() + 8d))
                .stream().filter(m ->
                    (m instanceof OwnableEntity oe && oe.getOwner() instanceof Player || INFFTamed.get(m).isPresent())
                        && m.getMobType().equals(MobType.UNDEAD) && NFUEntityStatics.hasLineOfSight(m, pPos))
                    .findAny().orElse(null);
            if (friendedUndead != null) {
                MobileParticleSourceEntity.addDefault(pLevel, ParticleTypes.HAPPY_VILLAGER,
                    friendedUndead.getEyePosition(), new Vec3(pPos.getX(), pPos.getY(), pPos.getZ()), 5d, 3);
                super.randomTick(pState, pLevel, pPos, pRandom);
            }
        }
    }

    // Cannot be bonemeal-ed

    @Override
    public boolean isValidBonemealTarget(BlockGetter pLevel, BlockPos pPos, BlockState pState, boolean pIsClient){
        return false;
    }

    @Override
    public boolean isBonemealSuccess(Level pLevel, Random pRandom, BlockPos pPos, BlockState pState){
        return false;
    }

    @Override
    public void performBonemeal(ServerLevel pLevel, Random pRandom, BlockPos pPos, BlockState pState) {

    }

}
