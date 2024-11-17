package net.sodiumzh.nff.girls.entity.tamingprocesses.hmag;

import java.util.Objects;
import java.util.UUID;

import javax.annotation.Nullable;

import com.github.mechalopa.hmag.world.entity.JiangshiEntity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.sodiumzh.nautils.entity.AttributeModifierSwitch;
import net.sodiumzh.nautils.statics.NaUtilsEntityStatics;
import net.sodiumzh.nautils.statics.NaUtilsNBTStatics;
import net.sodiumzh.nff.girls.befriendmobs.entity.ai.goal.FreezeGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsTamableJiangshiMutableLeapGoal;
import net.sodiumzh.nff.girls.item.TaoistTalismanItem;
import net.sodiumzh.nff.girls.registry.NFFGirlsItems;
import net.sodiumzh.nff.girls.util.NFFGirlsEntityStatics;
import net.sodiumzh.nff.services.entity.taming.CNFFTamable;
import net.sodiumzh.nff.services.entity.taming.NFFTamingMapping;
import net.sodiumzh.nff.services.entity.taming.NFFTamingProcess;
import net.sodiumzh.nff.services.entity.taming.TamableHatredReason;
import net.sodiumzh.nff.services.entity.taming.TamableInteractArguments;
import net.sodiumzh.nff.services.entity.taming.TamableInteractionResult;

public class HmagJiangshiTamingProcess extends NFFTamingProcess
{
	
	protected static final AttributeModifierSwitch MODIFIERS = new AttributeModifierSwitch()
			.putGenerated(1, Attributes.ATTACK_DAMAGE, 4.0, Operation.ADDITION)
			.putGenerated(1, Attributes.MOVEMENT_SPEED, 0.1, Operation.MULTIPLY_BASE)
			.putGenerated(2, Attributes.ATTACK_DAMAGE, 8.0, Operation.ADDITION)
			.putGenerated(3, Attributes.MOVEMENT_SPEED, 0.2, Operation.MULTIPLY_BASE)
			.putGenerated(3, Attributes.ATTACK_DAMAGE, 16.0, Operation.ADDITION)
			.putGenerated(3, Attributes.ATTACK_KNOCKBACK, 0.5, Operation.ADDITION)
			.putGenerated(3, Attributes.MOVEMENT_SPEED, 0.3, Operation.MULTIPLY_BASE)
			.putGenerated(4, Attributes.ATTACK_DAMAGE, 28.0, Operation.ADDITION)
			.putGenerated(4, Attributes.ATTACK_KNOCKBACK, 1.5, Operation.ADDITION)
			.putGenerated(4, Attributes.MOVEMENT_SPEED, 0.6, Operation.MULTIPLY_BASE)
			.putGenerated(5, Attributes.ATTACK_DAMAGE, 40.0, Operation.ADDITION)
			.putGenerated(5, Attributes.ATTACK_KNOCKBACK, 2.5, Operation.ADDITION)
			.putGenerated(5, Attributes.MOVEMENT_SPEED, 1.0, Operation.MULTIPLY_BASE);
	
	protected static final AttributeModifier FROZEN_ARMOR = new AttributeModifier(UUID.fromString("3b41599b-b30f-4106-8b69-93cb4b3df66a"), 
			"frozen_armor", 40d, AttributeModifier.Operation.ADDITION);
	@Override
	public void initCap(CNFFTamable cap)
	{
		cap.getNbt().put("progress", new CompoundTag());
		//this.updateModifiers(cap.getOwner());
	}	
	
	@Override
	public TamableInteractionResult handleInteract(TamableInteractArguments args) 
	{
		return new TamableInteractionResult();
	}

	@Override
	public void interrupt(Player player, Mob mob, boolean isQuiet) 
	{
		CNFFTamable.getCapNbt(mob).getCompound("progress").remove(player.getStringUUID());
		MODIFIERS.apply(mob, getAngerPhase(mob));
	}
	
	@Override
	public boolean interruptAll(Mob mob, boolean isQuiet)
	{
		if (!CNFFTamable.getCapNbt(mob).getCompound("progress").isEmpty())
		{
			CNFFTamable.getCapNbt(mob).put("progress", new CompoundTag());
			MODIFIERS.apply(mob, 0);
			return true;
		}
		else return false;
	}
	
	@Override
	public boolean isInProcess(Player player, Mob mob) {
		return getProgressLevel(mob, player) > 0;
	}

	@Override
	public TamableHatredReason[] getAddHatredReasons() {
		return new TamableHatredReason[] {};
	}

	@Override
	public void serverTick(Mob mob)
	{
		super.serverTick(mob);
		CNFFTamable cap = CNFFTamable.getCap(mob);
		if (cap == null)
			return;
		//this.forAllPlayersInProcess(mob, player -> this.interrupt(player, mob, false));
		
		if (getFrozenTime(mob) > 0)
		{
			if (!mob.getAttribute(Attributes.ARMOR).hasModifier(FROZEN_ARMOR))
				mob.getAttribute(Attributes.ARMOR).addTransientModifier(FROZEN_ARMOR);
		}
		else
		{
			mob.getAttribute(Attributes.ARMOR).removeModifier(FROZEN_ARMOR);
		}
		
		if (getAngerPhase(mob) >= 3)
		{
			NaUtilsEntityStatics.addEffectSafe(mob,new MobEffectInstance(MobEffects.JUMP, 19, getAngerPhase(mob) == 3 ? 1 : 2));
			NFFGirlsTamableJiangshiMutableLeapGoal.setLeapHeightBonus(mob, getAngerPhase(mob) == 3 ? 1 : 2);
		}
		else
		{
			NFFGirlsTamableJiangshiMutableLeapGoal.setLeapHeightBonus(mob, 0);
		}
		
		if (getFrozenTime(mob) > 0)
		{
			NFFGirlsEntityStatics.sendCriticalParticlesToLivingDefault(mob, 0, 5);
		}
		if (getAngerPhase(mob) > 0)
		{
			NaUtilsEntityStatics.sendSmokeParticlesToLivingDefault(mob, 0, getAngerPhase(mob) * 2);
		}
		if (cap.hasTimer("clear_fire") && mob.tickCount % 10 == 0)
		{
			mob.clearFire();
		}

	}
	
	/**
	 *  Peach-Wood sword hit
	 *  Invoked in {@link DwmgEntityEvent#onLivingDamage}
	 */
	
	public void onPeachSwordHit(Mob mob, Player player)
	{
		int oldPhase = getAngerPhase(mob);
		if (getFrozenTime(mob) > 0)
		{
			if (getProgressLevel(mob, player) == 5)
			{
				MODIFIERS.apply(mob, 0);
				thunderEffect(mob);
				NaUtilsEntityStatics.sendHeartParticlesToLivingDefault(mob);
				Mob bm = (Mob) doTaming(player, mob);
				bm.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 10 * 20));
				return;
			}
			else
			{
				progressIncrease(mob, player);
				NaUtilsEntityStatics.sendParticlesToEntity(mob, ParticleTypes.EXPLOSION, 0.2, 0, 1, 0);
				CNFFTamable.getCap(mob).stopTimer("frozen", true);
				CNFFTamable.getCap(mob).setPlayerTimer(player, "peach_sword", 60 * 20);
			}
			if (oldPhase == 2 || getProgressLevel(mob, player) == 3)
			{
				thunderEffect(mob);
			}
			updateModifiers(mob);
			mob.level.playSound(null, mob, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 2f, 0.7f);
		}
		else
		{
			mob.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 20 * 20, 1));
		}
	}
	
	
	// Freezing related
	/** Freezing is handled with {@link FreezeGoal and @link NFFGirlsEntityEventListeners#onEntityJoinLevel} */
	
	public int getFrozenTime(Mob mob)
	{
		CNFFTamable cap = CNFFTamable.getCap(mob);
		if (cap == null)
			return 0;
		else if (cap.hasTimer("frozen"))
			return cap.getTimer("frozen");
		else return 0;
	}
	
	public static boolean isFrozen(Mob mob)
	{
		NFFTamingProcess handler = NFFTamingMapping.getHandler(mob);
		if (handler instanceof HmagJiangshiTamingProcess hjs)
		{
			return hjs.getFrozenTime(mob) > 0;
		}
		else return false;
			
	}
	
	/**
	 * Invoked in {@link TaoistTalismanItem#interactLivingEntity}
	 */
	public boolean applyTalisman(Mob mob)
	{
		CNFFTamable cap = CNFFTamable.getCap(mob);
		if (getFrozenTime(mob) > 0)
			return false;
		else if (cap.hasTimer("freeze_cooldown"))
			return false;
		else
		{
			cap.setTimer("frozen", 200);
			return true;
		}
	}
	
	// Progress related
	
	public int getProgressLevel(Mob mob, Player player)
	{
		if (CNFFTamable.getCapNbt(mob).getCompound("progress").contains(player.getStringUUID(), NaUtilsNBTStatics.TAG_INT_ID))
			return CNFFTamable.getCapNbt(mob).getCompound("progress").getInt(player.getStringUUID());
		else return 0;
	}
	
	public void setProgressLevel(Mob mob, Player player, int value)
	{
		if (value < 0 || value > 5)
			CNFFTamable.getCapNbt(mob).getCompound("progress").remove(player.getStringUUID());
		else
			CNFFTamable.getCapNbt(mob).getCompound("progress").putInt(player.getStringUUID(), value);
		updateModifiers(mob);		
	}
	
	public void progressIncrease(Mob mob, Player player)
	{
		setProgressLevel(mob, player, getProgressLevel(mob, player) + 1);
	}
	
	public void progressDecrease(Mob mob, Player player)
	{
		setProgressLevel(mob, player, getProgressLevel(mob, player) - 1);
	}
	
	public int getAngerPhase(Mob mob)
	{
		int res = 0;
		for (Player player: mob.level.players())
		{
			if (isInProcess(player, mob) && getProgressLevel(mob, player) > res)
				res = getProgressLevel(mob, player);
		}
		return res;
	}

	public void updateModifiers(Mob mob)
	{
		if (mob == null) return;
		MODIFIERS.apply(mob, getAngerPhase(mob));
	}
	
	@Override
	public void onBefriendableMobTimerUp(Mob mob, String timerKey, @Nullable Player player) 
	{
		if (CNFFTamable.getCap(mob) == null)
			return;
		if (mob instanceof JiangshiEntity && Objects.equals(timerKey, "frozen") && player == null)
		{
			if (mob.getRandom().nextDouble() < 0.667)
				mob.spawnAtLocation(NFFGirlsItems.TAOIST_TALISMAN.get().getDefaultInstance());
			CNFFTamable.getCap(mob).setTimer("freeze_cooldown", 15 * 20);
		}
		else if (mob instanceof JiangshiEntity && Objects.equals(timerKey, "peach_sword") && player != null)
		{
			this.progressDecrease(mob, player);
		}
	}
	
	public void thunderEffect(Mob mob)
	{
		LightningBolt lb = EntityType.LIGHTNING_BOLT.create(mob.level);
		lb.moveTo(Vec3.atBottomCenterOf(mob.blockPosition()));
		lb.setDamage(0);
		mob.level.addFreshEntity(lb);
		mob.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 30 * 20));
		CNFFTamable.getCap(mob).setTimer("clear_fire", 10 * 20);
	}
}
