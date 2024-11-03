package net.sodiumzh.nff.girls.entity.tamingprocesses.hmag;

import java.util.stream.Stream;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.sodiumzh.nautils.math.RndUtil;
import net.sodiumzh.nautils.statics.NaUtilsEntityStatics;
import net.sodiumzh.nautils.statics.NaUtilsItemStatics;
import net.sodiumzh.nff.services.entity.taming.TamableHatredReason;
import net.sodiumzh.nff.services.entity.taming.TamableInteractArguments;
import net.sodiumzh.nff.services.entity.taming.TamableInteractionResult;
import net.sodiumzh.nff.services.entity.taming.TamingProcessItemGivingProgress;

public class HmagHornetTamingProcess extends TamingProcessItemGivingProgress
{


	@Override
	protected double getProcValueToAdd(ItemStack item, Player player, Mob mob, double lastProc) {
		if (item.is(Items.HONEY_BOTTLE))
			return RndUtil.rndRangedDouble(0.04d, 0.08d);
		else if (item.is(Items.HONEY_BLOCK))
			return RndUtil.rndRangedDouble(0.08d, 0.16d);
		else return 0;
	}

	@Override
	public boolean isItemAcceptable(ItemStack item) {
		return item.is(Items.HONEY_BOTTLE)
				|| item.is(Items.HONEY_BLOCK);
	}

	/*
	// No longer needed as remaining item is implemented in the parent class: 0.x.27
	@Override
	public TamableInteractionResult handleInteract(TamableInteractArguments args) 
	{
		// Get if using honey bottle
		ItemStack stack = args.getPlayer().getItemInHand(args.getHand());
		boolean flag = stack.is(Items.HONEY_BOTTLE);
		int count = stack.getCount();
		
		TamableInteractionResult res = super.handleInteract(args);
		
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
		return 200;
	}
	
	@Override
	public void serverTick(Mob mob)
	{
		this.forAllPlayersInProcess(mob, (p) -> {
			if (p != null && mob != null && p.distanceToSqr(mob) > 32 * 32)
				this.interrupt(p, mob, false);
		});
		
		if (!has8HoneyBlocksAround(mob) && this.isInProcess(mob))
		{
			this.forAllPlayersInProcess(mob, player -> {
				this.addProgressValue(mob, player, -0.005);	// 0.1 per second
				if (this.getProgressValue(mob, player) <= 0)
				{
					interrupt(player, mob, false);
				}
			});
			NaUtilsEntityStatics.sendSmokeParticlesToLivingDefault(mob);
		}		
	}

	@Override
	public TamableHatredReason[] getAddHatredReasons() {
		return new TamableHatredReason[] {TamableHatredReason.ATTACKED, TamableHatredReason.ATTACKING, TamableHatredReason.HIT};
	}
	
	@Override
	public int getHatredDurationTicks(TamableHatredReason reason)
	{
		switch (reason)
		{
		case ATTACKED:
			return 300 * 20;
		case ATTACKING:
			return 30 * 20; 
		case HIT:
			return 30 * 20;
		default:
			return 0;				
		}
	}
	
	@Override
	public void onAddingHatred(Mob mob, Player player, TamableHatredReason reason)
	{
		if (reason != TamableHatredReason.ATTACKING)
			super.onAddingHatred(mob, player, reason);
	}
	
	
}
