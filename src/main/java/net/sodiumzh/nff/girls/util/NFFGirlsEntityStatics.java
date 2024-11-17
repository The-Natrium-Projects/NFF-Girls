package net.sodiumzh.nff.girls.util;

import com.mojang.logging.LogUtils;

import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.sodiumzh.nautils.statics.NaUtilsEntityStatics;
import net.sodiumzh.nautils.statics.NaUtilsReflectionStatics;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.services.entity.ai.NFFTamedMobAIState;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nff.services.entity.taming.INFFTamedSunSensitiveMob;
import net.sodiumzh.nff.services.entity.taming.NFFTamedStatics;

public class NFFGirlsEntityStatics
{
	
	/**
	 * Check if a befriended mob is not waiting.
	 * If it's not a befriended mob, return always true.
	 */
	public static boolean isNotWaiting(LivingEntity living)
	{
		return living instanceof INFFTamed bm && bm.getAIState() != NFFTamedMobAIState.WAIT;
	}
	
	/**
	 * Check if a mob isn't wearing/holding any golden items
	 * For piglin hostility
	 */
	public static boolean isNotWearingGold(LivingEntity living)
	{
		for (EquipmentSlot slot: EquipmentSlot.values())
		{
			if (living.getItemBySlot(slot).is(ItemTags.PIGLIN_LOVED))
			{
				return false;
			}
		}
		return true;
	}
	
	public static boolean isWearingGold(LivingEntity living)
	{
		return !isNotWearingGold(living);
	}
	
	public static boolean isOnEitherHand(LivingEntity living, Item item)
	{
		return living.getMainHandItem().is(item) || living.getOffhandItem().is(item);
	}
	
	/**
	 * Force set equipment item without any other operation.
	 */
	@SuppressWarnings("unchecked")
	public static void setMobEquipmentWithoutSideEffect(Mob mob, EquipmentSlot slot, ItemStack item)
	{
		NonNullList<ItemStack> slotList = null;
		switch (slot.getType())
		{
		case HAND:
			slotList = NaUtilsReflectionStatics.forceGet(mob, Mob.class, "f_21350_").cast();	// Mob.handItems
			break;
		case ARMOR:
			slotList = NaUtilsReflectionStatics.forceGet(mob, Mob.class, "f_21351_").cast();	// Mob.armorItems
		}
		if (slotList != null)
			slotList.set(slot.getIndex(), item);
	}
	
	/**
	 * Check if a befriended undead mob will burn under sun.
	 * @param mobUndead Mob to test.
	 * @return Whether this mob is sun-sensitive, or always false if it doesn't implement {@link INFFTamedSunSensitiveMob}/
	 */
	public static boolean isSunSensitive(INFFTamed mobUndead)
	{
		if (mobUndead instanceof INFFTamedSunSensitiveMob u)
		{
			return !u.isSunImmune() && !mobUndead.asMob().hasEffect(MobEffects.FIRE_RESISTANCE);
		}
		else
		{
			LogUtils.getLogger().error("NFFGirlsEntityStatics#isSunSafe: mob to test must implement INFFTamedSunSensitiveMob.");
			return false;
		}
	}

	public static void sendCriticalParticlesToLivingDefault(LivingEntity entity, float heightOffset, int amount)
	{
		NaUtilsEntityStatics.sendParticlesToEntity(entity, ParticleTypes.CRIT, entity.getBbHeight() - 0.2 + heightOffset, 0.5d, amount, 0.1d);
	}
	
	/**
	 * @deprecated Use {@link NFFTamedStatics#isLivingAlliedToBM} instead
	 * Check if a LivingEntity is ally to the given befriended mob.
	 * <p> Rule: Owner, owner's other befriended mobs, owner's tamed animals; other players and their befriended mobs & tamed animals if no PVP
	 * <p> Only on server. On client always false.
	 */
	@Deprecated
	public static boolean isAlly(INFFGirlsTamed allyTo, LivingEntity test)
	{
		return NFFTamedStatics.isLivingAlliedToBM(allyTo, test);
	}

}
