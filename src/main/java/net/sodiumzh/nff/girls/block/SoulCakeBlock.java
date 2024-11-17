package net.sodiumzh.nff.girls.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.sodiumzh.nautils.statics.NaUtilsEntityStatics;
import net.sodiumzh.nff.girls.registry.NFFGirlsEffects;

public class SoulCakeBlock extends CakeBlock
{

	public SoulCakeBlock(Properties pProperties)
	{
		super(pProperties);
	}
	
	@Override
	public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand,
			BlockHitResult pHit) {
		ItemStack itemstack = pPlayer.getItemInHand(pHand);
		//Item item = itemstack.getItem();
		/*
		if (itemstack.is(ItemTags.CANDLES) && pState.getValue(BITES) == 0)
		{
			Block block = Block.byItem(item);
			if (block instanceof CandleBlock)
			{
				if (!pPlayer.isCreative())
				{
					itemstack.shrink(1);
				}

				pLevel.playSound((Player) null, pPos, SoundEvents.CAKE_ADD_CANDLE, SoundSource.BLOCKS, 1.0F, 1.0F);
				pLevel.setBlockAndUpdate(pPos, CandleCakeBlock.byCandle(block));	// Should be changed
				pLevel.gameEvent(pPlayer, GameEvent.BLOCK_CHANGE, pPos);
				pPlayer.awardStat(Stats.ITEM_USED.get(item));
				return InteractionResult.SUCCESS;
			}
		}*/
		
		if (pLevel.isClientSide)
		{
			if (eat(pLevel, pPos, pState, pPlayer).consumesAction())
			{
				return InteractionResult.SUCCESS;
			}

			if (itemstack.isEmpty())
			{
				return InteractionResult.CONSUME;
			}
		}

		return SoulCakeBlock.eat(pLevel, pPos, pState, pPlayer);
	}

	  protected static InteractionResult eat(LevelAccessor pLevel, BlockPos pPos, BlockState pState, Player player) {
	  {
	         player.getFoodData().eat(3, 0.1F);
	         // 30s undead affinity
	         NaUtilsEntityStatics.addEffectSafe(player, NFFGirlsEffects.UNDEAD_AFFINITY.get(), 90*20);
	         int i = pState.getValue(BITES);
	         pLevel.gameEvent(player, GameEvent.EAT, pPos);
	         if (i < 6) {
	            pLevel.setBlock(pPos, pState.setValue(BITES, Integer.valueOf(i + 1)), 3);
	         } else {
	            pLevel.removeBlock(pPos, false);
	            pLevel.gameEvent(player, GameEvent.BLOCK_DESTROY, pPos);
	         }

	         return InteractionResult.SUCCESS;
	      }
	   }
	
}
