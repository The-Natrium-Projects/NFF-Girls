package net.sodiumzh.nff.girls.entity.tamingprocesses.hmag;

import com.github.mechalopa.hmag.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.sodiumzh.nautils.block.ColoredBlocks;
import net.sodiumzh.nautils.entity.anger.MobAngerRules;
import net.sodiumzh.nautils.math.RndUtil;
import net.sodiumzh.nff.girls.entity.NFFGirlsTamingRules;
import net.sodiumzh.nff.girls.registry.NFFGirlsAngerRules;
import net.sodiumzh.nff.girls.registry.NFFGirlsBlocks;
import net.sodiumzh.nff.girls.registry.NFFGirlsTags;
import net.sodiumzh.nff.services.entity.capability.CNFFTamable;
import net.sodiumzh.nff.services.entity.taming.TamingProcessItemGivingProgress;

public class HmagNightwalkerTamingProcess extends TamingProcessItemGivingProgress
{

	@Override
	public boolean additionalConditions(Player player, Mob mob) {
		return mob.level.getBlockState(mob.blockPosition().below()).is(NFFGirlsTags.CAN_BEFRIEND_NIGHTWALKERS_ON);
	}

	@Override
	public int getItemGivingCooldownTicks() {
		return NFFGirlsTamingRules.COOLDOWN_MIDDLE;
	}

	@Override
	public MobAngerRules getAngerRules() {
		return NFFGirlsAngerRules.DEFAULT.get();
	}
	
	@Override
	public void afterItemGiven(Player player, Mob mob, ItemStack item) 
	{
		convertBlockOnGiven(mob.level, mob.blockPosition().below());
		convertBlockOnGiven(mob.level, mob.blockPosition().below().east());
		convertBlockOnGiven(mob.level, mob.blockPosition().below().west());
		convertBlockOnGiven(mob.level, mob.blockPosition().below().south());
		convertBlockOnGiven(mob.level, mob.blockPosition().below().north());
		convertBlockOnGiven(mob.level, mob.blockPosition().below(2));
	}
	
	@Override
	public Mob finalActions(Player player, Mob mob)
	{
		afterItemGiven(player, mob, null);
		return super.finalActions(player, mob);
	}
	
	protected void convertBlockOnGiven(Level level, BlockPos pos)
	{
		BlockState blockstate = level.getBlockState(pos);
		if (blockstate.getBlock() == null)
			return;
		Block convertTo = null;
		
		if (blockstate.is(NFFGirlsBlocks.ENHANCED_LUMINOUS_TERRACOTTA.get()))
			convertTo = NFFGirlsBlocks.LUMINOUS_TERRACOTTA.get();
		else if (blockstate.is(NFFGirlsBlocks.LUMINOUS_TERRACOTTA.get()))
			convertTo = ColoredBlocks.GLAZED_TERRACOTTA_BLOCKS.ofRandomColor();
		else if (ColoredBlocks.GLAZED_TERRACOTTA_BLOCKS.contains(blockstate.getBlock()))
			convertTo = ColoredBlocks.TERRACOTTA_BLOCKS.ofColor(ColoredBlocks.GLAZED_TERRACOTTA_BLOCKS.getColor(blockstate.getBlock()));
		else if (ColoredBlocks.TERRACOTTA_BLOCKS.contains(blockstate.getBlock()))
			convertTo = Blocks.CLAY;
		if (convertTo != null)
			level.setBlock(pos, convertTo.defaultBlockState(), 1 | 2);
	}

	@Override
	public void tamableInit(CNFFTamable cnffTamable) {

	}
}
