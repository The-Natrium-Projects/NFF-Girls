package net.sodiumzh.nff.girls.entity.tamingprocesses.hmag;

import java.util.HashSet;
import java.util.UUID;

import com.github.mechalopa.hmag.world.entity.CrimsonSlaughtererEntity;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.sodiumzh.nautils.math.RandomSelection;
import net.sodiumzh.nautils.statics.NaUtilsMathStatics;
import net.sodiumzh.nautils.statics.NaUtilsEntityStatics;
import net.sodiumzh.nautils.statics.NaUtilsTagStatics;
import net.sodiumzh.nff.girls.registry.NFFGirlsTags;
import net.sodiumzh.nff.services.entity.taming.TamableHatredReason;
import net.sodiumzh.nff.services.entity.taming.TamingProcessItemGivingProgress;

public class HmagCrimsonSlaughtererTamingProcess extends TamingProcessItemGivingProgress
{
	protected static final UUID WARPED_BLOCK_KNOCKBACK_UUID = UUID.fromString("e934d764-7e28-4dc7-a652-a156ac4ce44d");
	protected static final AttributeModifier WARPED_BLOCK_KNOCKBACK = new AttributeModifier(WARPED_BLOCK_KNOCKBACK_UUID, "warped_block_knockback",
			2.0d, AttributeModifier.Operation.ADDITION);
	
	@Override
	protected double getProcValueToAdd(ItemStack item, Player player, Mob mob, double oldProc) {
		if (item.is(Items.CRIMSON_ROOTS))
			return NaUtilsMathStatics.rndRangedDouble(0.02d, 0.04d);
		else if (item.is(Items.WEEPING_VINES))
			return NaUtilsMathStatics.rndRangedDouble(0.02d, 0.04d);
		else if (item.is(Items.NETHER_WART))
			return NaUtilsMathStatics.rndRangedDouble(0.03d, 0.06d);
		else if (item.is(Items.CRIMSON_FUNGUS))
			return NaUtilsMathStatics.rndRangedDouble(0.03d, 0.06d);
		else if (item.is(Items.SHROOMLIGHT))
			return NaUtilsMathStatics.rndRangedDouble(0.04d, 0.08d);
		else if (item.is(Items.GILDED_BLACKSTONE))
			return NaUtilsMathStatics.rndRangedDouble(0.04d, 0.08d);
		else if (item.is(Items.GOLDEN_APPLE))
			return NaUtilsMathStatics.rndRangedDouble(0.08d, 0.16d);
		else if (NaUtilsTagStatics.hasTag(item, "forge:ingots/netherite") || item.is(Items.NETHERITE_INGOT))
			return NaUtilsMathStatics.rndRangedDouble(0.16d, 0.32d);
		else if (item.is(Items.NETHER_STAR))
			return RandomSelection.createDouble(0.50d).add(1.01d, 0.20d).getDouble();
		else return 0;
			
	}

	@Override
	public boolean isItemAcceptable(ItemStack item) {
		if (item.is(Items.CRIMSON_ROOTS)
			|| item.is(Items.WEEPING_VINES)
			|| item.is(Items.NETHER_WART)
			|| item.is(Items.SHROOMLIGHT)
			|| item.is(Items.CRIMSON_FUNGUS)
			|| item.is(Items.GILDED_BLACKSTONE)
			|| item.is(Items.GOLDEN_APPLE)
			|| NaUtilsTagStatics.hasTag(item, "forge:ingots/netherite") || item.is(Items.NETHERITE_INGOT)
			|| item.is(Items.NETHER_STAR))
			return true;
		else return false;
	}

	@Override
	public boolean additionalConditions(Player player, Mob mob) {
		return isOnWarpedBlock(mob) && satisfiesShroomlightCondition(mob);
	}

	@Override
	public int getItemGivingCooldownTicks() {
		return 10 * 20;
	}

	@Override
	public int getHatredDurationTicks(TamableHatredReason reason)
	{
		switch (reason)
		{
		case ATTACKED:
			return 300 * 20;
		default:
			return 0;				
		}
	}
	
	@Override
	public HashSet<TamableHatredReason> getAddHatredReasons() {
		HashSet<TamableHatredReason> set = new HashSet<TamableHatredReason>();
		set.add(TamableHatredReason.ATTACKED);
		return set;
	}

	@Override
	public void serverTick(Mob mob)
	{
		super.serverTick(mob);
		if (mob instanceof CrimsonSlaughtererEntity cs)
		{
			if (isOnWarpedBlock(mob))
			{
				NaUtilsEntityStatics.addEffectSafe(mob, new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10));
				if (!mob.getAttribute(Attributes.ATTACK_KNOCKBACK).hasModifier(WARPED_BLOCK_KNOCKBACK))
					mob.getAttribute(Attributes.ATTACK_KNOCKBACK).addTransientModifier(WARPED_BLOCK_KNOCKBACK);
			}
			else
			{
				mob.getAttribute(Attributes.ATTACK_KNOCKBACK).removeModifier(WARPED_BLOCK_KNOCKBACK);
			}
		}
		
		if (!satisfiesShroomlightCondition(mob) && this.isInProcess(mob))
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
	
	public boolean isOnWarpedBlock(Mob mob)
	{
		return mob.level().getBlockState(mob.blockPosition().below()).is(NFFGirlsTags.AFFECTS_CRIMSON_SLAUGHTERER);
	}
	
	public boolean satisfiesShroomlightCondition(Mob mob)
	{
		return mob.level().getBlockStates(NaUtilsEntityStatics.getNeighboringArea(mob, 6, 3)).filter(bs -> bs.is(Blocks.SHROOMLIGHT))
				.toList().size() >= 16;
	}

	
}
