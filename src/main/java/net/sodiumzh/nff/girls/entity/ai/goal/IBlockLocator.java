package net.sodiumzh.nff.girls.entity.ai.goal;

import net.minecraft.world.level.block.Block;

import java.util.Collection;

/**
 * Interface for mobs that can locate a certain type of block nearby
 */
public interface IBlockLocator
{
	public Collection<Block> getLocatingBlocks();
	public int getFrequency();
	public void onStartLocating();
}
