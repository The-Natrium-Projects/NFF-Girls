package net.sodiumzh.nff.girls.entity.tamingprocess.hmag;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.sodiumzh.nff.girls.entity.NFFGirlsTamingRules;
import net.sodiumzh.nff.girls.registry.NFFGirlsAngerRules;
import net.sodiumzh.nff.services.entity.taming.CNFFTamable;
import net.sodiumzh.nff.services.entity.taming.TamingProcessItemGivingProgress;
import net.sodiumzh.nfu.entity.anger.MobAngerRules;

import java.util.stream.Stream;

public class HmagHornetTamingProcess extends TamingProcessItemGivingProgress
{

	@Override
	public boolean isItemAcceptable(ItemStack item) {
		return item.is(Items.HONEY_BOTTLE)
				|| item.is(Items.HONEY_BLOCK);
	}

	/*
	// No longer needed as remaining item is implemented in the parent class: 0.x.27
	@Override
	public TamingInteractionResult handleInteract(TamableInteractArguments args) 
	{
		// Get if using honey bottle
		ItemStack stack = args.getPlayer().getItemInHand(args.getHand());
		boolean flag = stack.is(Items.HONEY_BOTTLE);
		int count = stack.getCount();
		
		TamingInteractionResult res = super.handleInteract(args);
		
		// If consumed honey bottle, drop a glass bottle
		if (!args.isClient() && args.getPlayer().getItemInHand(args.getHand()).getCount() != count && flag)
		{
			NaUtilsItemStatics.giveOrDropDefault(args.getPlayer(), Items.GLASS_BOTTLE);
		}
		return res;
	}*/
	
	@Override
	public boolean additionalConditions(Player player, Mob mob) {
		return !player.hasEffect(MobEffects.POISON) && has8HoneyBlocksAround(mob);
	}

	public boolean has8HoneyBlocksAround(Mob mob)
	{
		BlockPos pos = mob.blockPosition();
		// Search 9x9x9 area centered by mob
		AABB searchArea = new AABB(pos.getX() - 4, pos.getY() - 4, pos.getZ() - 4, pos.getX() + 4, pos.getY() + 4, pos.getZ() + 4);
		Stream<BlockState> blocks = mob.level.getBlockStates(searchArea);
		long count = blocks.filter(b -> b.is(Blocks.HONEY_BLOCK)).count();
		return count >= 8;
	}
	
	@Override
	public int getItemGivingCooldownTicks() {
		return NFFGirlsTamingRules.COOLDOWN_MIDDLE;
	}
	
	@Override
	public void serverTick(Mob mob)
	{
		Player ongoing = this.getOngoingPlayerInLevel(mob).orElse(null);
		if (ongoing != null) {
			if (ongoing.distanceToSqr(mob) > 32d * 32d)
				this.interrupt(ongoing, mob, true);
		}
		if (!has8HoneyBlocksAround(mob) && this.isInAnyProcess(mob)) {
			NFFGirlsTamingRules.tickContinuousProgressLoss(this, mob);
		}
		CNFFTamable.get(mob).setAlwaysHostileTo(ongoing);
	}

	@Override
	public void tamableInit(CNFFTamable cnffTamable) {

	}
}
