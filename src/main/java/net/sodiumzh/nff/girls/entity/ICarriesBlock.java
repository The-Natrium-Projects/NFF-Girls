package net.sodiumzh.nff.girls.entity;

import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

/**
 * Interface for mobs carrying a block like Enderman.
 */
public interface ICarriesBlock
{
	/**
	 * Invoked on inventory change and/or on tick (depending on subclasses), set the mob's actual holding block.
	 * @param newBlock Block accessed from inventory. The mob block slot will be set to this block.
	 */
	public void setCarryingBlock(@Nullable BlockState newBlock);
	
	/**
	 * Get the block this mob is <i>actually</i> carrying, i.e. the block in the <i>mob's block slot</i> (not the expected one from the inventory).
	 */
	@Nullable
	public BlockState getCarryingBlock();
}
