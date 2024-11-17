package net.sodiumzh.nff.girls.entity.tamingprocesses.hmag;
<<<<<<< HEAD

import java.util.HashSet;
=======
>>>>>>> b2ab5b02 (Taming)

import com.github.mechalopa.hmag.registry.ModItems;
import com.github.mechalopa.hmag.world.entity.EnderExecutorEntity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.sodiumzh.nautils.statics.NaUtilsMathStatics;
import net.sodiumzh.nautils.statics.NaUtilsEntityStatics;
import net.sodiumzh.nautils.statics.NaUtilsNBTStatics;
import net.sodiumzh.nff.girls.registry.NFFGirlsItems;
import net.sodiumzh.nff.services.entity.capability.CNFFTamable;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nff.services.entity.taming.TamableHatredReason;
import net.sodiumzh.nff.services.entity.taming.TamableInteractArguments;
import net.sodiumzh.nff.services.entity.taming.TamableInteractionResult;
import net.sodiumzh.nff.services.entity.taming.TamingProcessItemGivingProgress;
import net.sodiumzh.nff.services.registry.NFFCapRegistry;

public class HmagEnderExecutorTamingProcess extends TamingProcessItemGivingProgress
{

	@Override
	public void initCap(CNFFTamable cap)
	{
		cap.getNbt().putBoolean("cannot_teleport", false);
	}

	@Override
	public INFFTamed doTaming(Player player, Mob target)
	{
		((EnderExecutorEntity)target).setCarriedBlock(null);
		return super.doTaming(player, target);
	}
	
	@Override
	public TamableInteractionResult handleInteract(TamableInteractArguments args)
	{
		TamableInteractionResult result = super.handleInteract(args);
		if (result.handled && args.getPlayer().isCreative())
			CNFFTamable.getCap(args.getTarget()).getNbt().putUUID("player_uuid_on_befriending", args.getPlayer().getUUID());
		return result;
	}
	
	/*@Override
	protected double getProcValueToAdd(ItemStack item, Player player, Mob mob, double lastProc) {
		if (item.is(Items.ENDER_EYE))
			return NaUtilsMathStatics.rndRangedDouble(0.005d, 0.01d);
		else if (item.is(ModItems.ENDER_PLASM.get()))
			return NaUtilsMathStatics.rndRangedDouble(0.01d, 0.015d);
		else if (item.is(NFFGirlsItems.ENDER_PIE.get()))
		{
			double rnd = this.rnd.nextDouble();
			if (rnd < 0.05d)
				return 0.750d;
			else if (rnd < 0.15d)
				return 0.500d;
			else return 0.250d;
		}
		else return 0.0d;
	}

	@Override
	public boolean isItemAcceptable(ItemStack item) {
		return item.is(Items.ENDER_EYE)
			|| item.is(ModItems.ENDER_PLASM.get())
			|| item.is(NFFGirlsItems.ENDER_PIE.get());
	}*/

	@Override
	public boolean additionalConditions(Player player, Mob mob) {
		// Only works on player in the mob's tag "player_uuid_on_befriending"
		// This tag is given when player is looked at the enderman
		return CNFFTamable.getCapNbt(mob).contains("player_uuid_on_befriending", 11)
				&& CNFFTamable.getCapNbt(mob).getUUID("player_uuid_on_befriending").equals(player.getUUID())
				|| player.isCreative();	// Creative-mode player can always befriend
	}

	@Override
	public int getItemGivingCooldownTicks() {
		return 300;
	}

	@Override
	public void serverTick(Mob mob)
	{
		if (mob instanceof EnderExecutorEntity ee)
		{
			ee.getCapability(NFFCapRegistry.CAP_BEFRIENDABLE_MOB).ifPresent((l) -> {
				/** If not in befriending process, find if it's looked at, and enter the process
				* The tag is also an indicator for if this mob is on befriending process
				* Because Ender Executors have an extra state in which the "proc_value" is still 0 but the process has started
				* i.e. the mob is looked at but nothing has been given
				* On interruption it's removed
				* */
				if (!l.getNbt().contains("player_uuid_on_befriending", 11))
				{
					for (Player player: mob.level().players()) 
					{
						if (mob.distanceToSqr(player) <= 256.0d)
						{
							if (NaUtilsEntityStatics.isEnderManLookedAt(ee, player) && ee.getTarget() != null && ee.getTarget().equals(player))
							{
								l.getNbt().putUUID("player_uuid_on_befriending", player.getUUID());
								// If this time reaches 0, it will be interrupted
								l.getNbt().putInt("no_attack_expire_time", 30 * 20);
								l.setAlwaysHostileTo(player);
								break;
							}
						}
					}
				}
				/**
				 * Now the mob is in process
				 */
				if (l.getNbt().contains("player_uuid_on_befriending", 11))
				{
					Player player = ee.level().getPlayerByUUID(l.getNbt().getUUID("player_uuid_on_befriending"));
					/**  When player is present in the same level */
					if (player != null)
					{
						/** If player is further than 12 blocks, interrupt */
						if (ee.distanceToSqr(player) > 144d)
						{
							this.interrupt(player, mob, false);
						}
						/** Now the player is in range */
						else 
						{
							/** Once entered 7 blocks away, disable teleporting
							* So that the mob will not get over 12 blocks away by itself
							* This tag blocks teleporting in NFFGirlsEntityEventListeners class
							*/
							if (ee.distanceToSqr(player) <= 49.0d && (!l.getNbt().getBoolean("cannot_teleport")))
							{
								l.getNbt().putBoolean("cannot_teleport", true);
							}
							/** Update no attack timer
							* This timer will be reset in EntityEvent LivingHurtEvent listener
							*/
							if (l.getNbt().contains("no_attack_expire_time") && l.getNbt().getInt("no_attack_expire_time") > 0 && !player.isCreative())
							{
								l.getNbt().putInt("no_attack_expire_time", l.getNbt().getInt("no_attack_expire_time") - 1);
								if (l.getNbt().getInt("no_attack_expire_time") == 0)
								{
									// When 10s no attack expired, process loses 0.2
									DoubleTag currentValueTag = (DoubleTag) NaUtilsNBTStatics.getPlayerData(l.getPlayerDataNbt(), player, "proc_value");
									double procValue = currentValueTag == null ? 0 : currentValueTag.getAsDouble();
									procValue -= 0.2d;
									// If it drops 0, interrupt
									if (procValue <= 0)
									{
										interrupt(player, mob, false);
									}
									// Otherwise put the value back, send some particles and reset timer
									else 
									{
										NaUtilsNBTStatics.putPlayerData(DoubleTag.valueOf(procValue), l.getPlayerDataNbt(), player, "proc_value");
										NaUtilsEntityStatics.sendParticlesToEntity(mob, ParticleTypes.ANGRY_VILLAGER, mob.getBbHeight() - 0.2, 0.3d, 1, 1d);
										l.getNbt().putInt("no_attack_expire_time", 30 * 20);
									}
								}
							}
							/** Keep hostile to the player */
							CNFFTamable.getCap(mob).setAlwaysHostileTo(player);
						}
					}
				}
			});
		}
	}

	@Override
	public void interrupt(Player player, Mob mob, boolean isQuiet)
	{
		if (this.getProgressValue(mob, player) < 0.001d)
			super.interrupt(player, mob, true);
		else super.interrupt(player, mob, isQuiet);
		CompoundTag nbt = CNFFTamable.getCapNbt(mob);
		nbt.remove("player_uuid_on_befriending");
		nbt.remove("no_attack_expire_time");
		nbt.putBoolean("cannot_teleport", false);	
		CNFFTamable.getCap(mob).setAlwaysHostileTo(null);
	}
	
	@Override
	public boolean isInProcess(Player player, Mob mob)
	{
		CNFFTamable l = CNFFTamable.getCap(mob);
		return l.getNbt().contains("player_uuid_on_befriending", 11) && l.getNbt().getUUID("player_uuid_on_befriending").equals(player.getUUID());
	}

	@Override
	public double getProgressValue(Mob mob, Player player)
	{
		if (!isInProcess(player, mob))
			return -1;
		else if (NaUtilsNBTStatics.getPlayerData(CNFFTamable.getCap(mob).getPlayerDataNbt(), player, "proc_value") == null)
		{
			return 0;
		}
		else return ((DoubleTag) (NaUtilsNBTStatics.getPlayerData(CNFFTamable.getCap(mob).getPlayerDataNbt(), player, "proc_value"))).getAsDouble();
	}
	
	@Override
	public HashSet<TamableHatredReason> getAddHatredReasons() {
		HashSet<TamableHatredReason> set = new HashSet<TamableHatredReason>();
		set.add(TamableHatredReason.ATTACKED);
		return set;
	}

	@Override
	public void onAttackProcessingPlayer(Mob mob, Player player, boolean damageGiven)
	{
		if (mob instanceof EnderExecutorEntity ee)
		{
			ee.getCapability(NFFCapRegistry.CAP_BEFRIENDABLE_MOB).ifPresent((cap) -> {
				if (cap.getNbt().contains("no_attack_expire_time"))
				{
					cap.getNbt().putInt("no_attack_expire_time", 30 * 20);
				}
						
			});
		}
	}
}
