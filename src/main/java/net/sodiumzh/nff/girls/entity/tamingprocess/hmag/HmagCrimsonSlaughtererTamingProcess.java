package net.sodiumzh.nff.girls.entity.tamingprocess.hmag;

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
import net.sodiumzh.nautils.statics.NaUtilsEntityStatics;
import net.sodiumzh.nautils.statics.NaUtilsTagStatics;
import net.sodiumzh.nautils.statics.NaUtilsMathStatics;
import net.sodiumzh.nautils.math.RandomSelection;
import net.sodiumzh.nff.girls.registry.NFFGirlsTags;
import net.sodiumzh.nff.services.entity.taming.TamableHatredReason;
import net.sodiumzh.nff.services.entity.taming.TamingProcessItemGivingProgress;

public class HmagCrimsonSlaughtererTamingProcess extends TamingProcessItemGivingProgress
{
	protected static final UUID WARPED_BLOCK_KNOCKBACK_UUID = UUID.fromString("e934d764-7e28-4dc7-a652-a156ac4ce44d");
	protected static final AttributeModifier WARPED_BLOCK_KNOCKBACK = new AttributeModifier(WARPED_BLOCK_KNOCKBACK_UUID, "warped_block_knockback",
			2.0d, AttributeModifier.Operation.ADDITION);

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
		return mob.level.getBlockState(mob.blockPosition().below()).is(NFFGirlsTags.AFFECTS_CRIMSON_SLAUGHTERER);
	}
	
	public boolean satisfiesShroomlightCondition(Mob mob)
	{
		return mob.level.getBlockStates(NaUtilsEntityStatics.getNeighboringArea(mob, 6, 3)).filter(bs -> bs.is(Blocks.SHROOMLIGHT))
				.toList().size() >= 16;
	}

	
}
