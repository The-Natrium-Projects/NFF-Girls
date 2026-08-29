package net.sodiumzh.nff.girls.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.sodiumzh.nff.girls.registry.NFFGirlsItems;
import net.sodiumzh.nff.girls.registry.NFFGirlsTags;
import net.sodiumzh.nff.services.registry.NFFItemRegistry;
import org.jetbrains.annotations.Nullable;

public class EnderberryBushBlock extends SweetBerryBushBlock {
    private static final float HURT_SPEED_THRESHOLD = 0.003F;
    public static final int MAX_AGE = 3;
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
    private static final VoxelShape SAPLING_SHAPE = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 8.0D, 13.0D);
    private static final VoxelShape MID_GROWTH_SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);
    // If true, it can receive Ender Executor beam and grow.
    public static final BooleanProperty CAN_GROW_ENDERBERRY = BooleanProperty.create("can_grow_enderberry");

    public EnderberryBushBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(CAN_GROW_ENDERBERRY, false));
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter pLevel, BlockPos pPos, BlockState pState) {
        return new ItemStack(NFFGirlsItems.ENDERBERRY.get());
    }

    /**
     * Performs a random tick on a block.
     */
    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        int i = pState.getValue(AGE);
        if (i < 3 && /*pLevel.getRawBrightness(pPos.above(), 0) >= 9 && */net.minecraftforge.common.ForgeHooks.onCropsGrowPre(pLevel, pPos, pState, pRandom.nextInt(20) == 0)) {
            BlockState blockstate = pState.setValue(CAN_GROW_ENDERBERRY, true);
            pLevel.setBlock(pPos, blockstate, 2);
            pLevel.gameEvent(GameEvent.BLOCK_CHANGE, pPos, GameEvent.Context.of(blockstate));
        }
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        int age = pState.getValue(AGE);
        boolean isMaxAge = age == 3;
        if (isMaxAge) {
            int j = 1 + pLevel.random.nextInt(2);
            popResource(pLevel, pPos, new ItemStack(NFFGirlsItems.ENDERBERRY.get(), pLevel.random.nextInt(4) == 0 ? 2 : 1));
            pLevel.playSound((Player)null, pPos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + pLevel.random.nextFloat() * 0.4F);
            pLevel.playSound((Player)null, pPos, SoundEvents.ENDERMAN_TELEPORT, SoundSource.BLOCKS, 1.0F, 0.8F + pLevel.random.nextFloat() * 0.4F);
            BlockState blockstate = pState.setValue(AGE, Integer.valueOf(0)).setValue(CAN_GROW_ENDERBERRY, false);
            pLevel.setBlock(pPos, blockstate, 2);
            pLevel.gameEvent(GameEvent.BLOCK_CHANGE, pPos, GameEvent.Context.of(pPlayer, blockstate));
            return InteractionResult.sidedSuccess(pLevel.isClientSide);
        } else {
            // For debug
            if (pPlayer.getItemInHand(pHand).is(NFFItemRegistry.DEBUG_BEFRIENDER.get()))
            {
                if (pState.getValue(CAN_GROW_ENDERBERRY)) {
                    BlockState blockstate = pState.setValue(AGE, Integer.valueOf(pState.getValue(AGE) + 1)).setValue(CAN_GROW_ENDERBERRY, false);
                    pLevel.setBlock(pPos, blockstate, 2);
                }
                else {
                    BlockState blockstate = pState.setValue(CAN_GROW_ENDERBERRY, true);
                    pLevel.setBlock(pPos, blockstate, 2);
                }
                return InteractionResult.sidedSuccess(pLevel.isClientSide);
            }
            else return InteractionResult.PASS;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(CAN_GROW_ENDERBERRY);
    }

    @Override
    protected boolean mayPlaceOn(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return pState.is(NFFGirlsTags.CAN_GROW_ENDERBERRY_ON);
    }

    @Override
    public BlockPathTypes getBlockPathType(BlockState state, BlockGetter level, BlockPos pos, @Nullable Mob mob)
    {
        return BlockPathTypes.DAMAGE_OTHER;
    }

    // Cannot be bonemeal-ed

    @Override
    public boolean isValidBonemealTarget(BlockGetter pLevel, BlockPos pPos, BlockState pState, boolean pIsClient){
        return false;
    }

    @Override
    public boolean isBonemealSuccess(Level pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState){
        return false;
    }

    @Override
    public void performBonemeal(ServerLevel pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {

    }


}
