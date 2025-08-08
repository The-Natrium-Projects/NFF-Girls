package net.sodiumzh.nff.girls.entity.tamingprocess.hmag;

import com.github.mechalopa.hmag.world.entity.JiangshiEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.Tag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsTamableJiangshiMutableLeapGoal;
import net.sodiumzh.nff.girls.eventlistener.NFFGirlsEntityEventListeners;
import net.sodiumzh.nff.girls.item.TaoistTalismanItem;
import net.sodiumzh.nff.girls.registry.NFFGirlsItems;
import net.sodiumzh.nff.girls.util.NFFGirlsEntityStatics;
import net.sodiumzh.nff.services.entity.ai.goal.preset.FreezeGoal;
import net.sodiumzh.nff.services.entity.capability.CNFFTamable;
import net.sodiumzh.nff.services.entity.taming.NFFTamingMapping;
import net.sodiumzh.nff.services.entity.taming.NFFTamingProcess;
import net.sodiumzh.nfu.entity.AttributeModifierSwitch;
import net.sodiumzh.nfu.entity.anger.MobAngerRules;
import net.sodiumzh.nfu.entity.taming.TamingInteractionResult;
import net.sodiumzh.nfu.util.NFUEntityStatics;
import net.sodiumzh.nfu.util.NFUParticleStatics;

import javax.annotation.Nonnull;
import java.util.UUID;

public class HmagJiangshiTamingProcess extends NFFTamingProcess
{
	// IntTag; Indicates the progress phase (0-5).
	protected static final String NBT_KEY_PROGRESS = "progress";
	// UUID; Indicates the ongoing player.
	protected static final String NBT_KEY_ONGOING_PLAYER = "ongoing_player";
	// Started when it's frozen. On expire it will unfreeze.
	protected static final String TIMER_KEY_FROZEN = "frozen";
	protected static final String TIMER_KEY_CLEAR_FIRE = "clear_fire";
	// Started when it's successfully hit by the peach sword. When the timer expires, the progress will drop.
	protected static final String TIMER_KEY_PEACH_SWORD = "peach_sword";
	protected static final String TIMER_KEY_FREEZE_COOLDOWN = "freeze_cooldown";

	
	protected static final AttributeModifierSwitch MODIFIERS = new AttributeModifierSwitch()
			.putGenerated(1, Attributes.ATTACK_DAMAGE, 4.0, Operation.ADDITION)
			.putGenerated(1, Attributes.MOVEMENT_SPEED, 0.1, Operation.MULTIPLY_BASE)
			.putGenerated(2, Attributes.ATTACK_DAMAGE, 8.0, Operation.ADDITION)
			.putGenerated(2, Attributes.MOVEMENT_SPEED, 0.2, Operation.MULTIPLY_BASE)
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
	public void tamableInit(CNFFTamable cap)
	{
		if (cap.getEntity() instanceof JiangshiEntity js)
		{
			// Frozen by talisman
			js.goalSelector.addGoal(1, new FreezeGoal(js, HmagJiangshiTamingProcess::isFrozen));
			// Adjust leap goal
			WrappedGoal oldLeapGoal = null;
			for (WrappedGoal wg : js.goalSelector.getAvailableGoals())
			{
				if (wg.getPriority() == 2) // Priority 2 is only for leap goal
				{
					oldLeapGoal = wg;
					break;
				}
			}
			if (oldLeapGoal != null)
			{
				js.goalSelector.getAvailableGoals().remove(oldLeapGoal);
				js.goalSelector.addGoal(2, new NFFGirlsTamableJiangshiMutableLeapGoal(js));
			}
		}
	}	

	@Override
	public void interrupt(Player player, Mob mob, boolean isQuiet) 
	{
		CNFFTamable.get(mob).getGeneralNBT().remove(NBT_KEY_PROGRESS);
		MODIFIERS.apply(mob, 0);
	}
	
	@Override
	public boolean interruptAll(Mob mob, boolean isQuiet)
	{
		if (!CNFFTamable.get(mob).getGeneralNBT().contains(NBT_KEY_PROGRESS, Tag.TAG_INT))
		{
			CNFFTamable.get(mob).getGeneralNBT().putInt(NBT_KEY_PROGRESS, 0);
			MODIFIERS.apply(mob, 0);
			return true;
		}
		else return false;
	}
	
	@Override
	public boolean isInProcess(Player player, Mob mob) {
		return getProgressLevel(mob) > 0;
	}

	@Override
	public MobAngerRules getAngerRules() {
		return MobAngerRules.NO_ANGER.get();
	}

	@Override
	@Nonnull
	public MobAngerRules getInterruptingAngerRules() {
		return MobAngerRules.NO_ANGER.get();
	}

	@Override
	public TamingInteractionResult handleInteract(Player player, Mob mob, InteractionHand interactionHand) {
		return TamingInteractionResult.unhandled(player.level());
	}

	@Override
	public void serverTick(Mob mob)
	{
		CNFFTamable cap = CNFFTamable.get(mob);
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
			NFUEntityStatics.addEffectSafe(mob,new MobEffectInstance(MobEffects.JUMP, 19, getAngerPhase(mob) == 3 ? 1 : 2));
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
			NFUParticleStatics.sendSmokeParticlesToEntityDefault(mob, 0, getAngerPhase(mob) * 2);
		}
		if (cap.hasTimer(TIMER_KEY_CLEAR_FIRE) && mob.tickCount % 10 == 0)
		{
			mob.clearFire();
		}
		if (cap.getGeneralNBT().hasUUID(NBT_KEY_ONGOING_PLAYER) && cap.getGeneralNBT().getInt(NBT_KEY_PROGRESS) > 0) {
			cap.setAlwaysHostileTo(mob.level().getPlayerByUUID(cap.getGeneralNBT().getUUID(NBT_KEY_ONGOING_PLAYER)));
		}
		else {
			cap.setAlwaysHostileTo(null);
		}
	}
	
	/**
	 *  Invoked on peach-wood sword hits a tamable Jiangshi.
	 *  Invoked in {@link NFFGirlsEntityEventListeners#onLivingHurt}
	 *  @return If true, it will prevent from applying the general effects of peach sword.
	 */
	public boolean onPeachSwordHit(Mob mob, Player player)
	{
		int oldPhase = getAngerPhase(mob);
		if (getFrozenTime(mob) > 0)
		{
			// If it's already 5, remove attr modifiers and tame
			if (getProgressLevel(mob) == 5)
			{
				MODIFIERS.apply(mob, 0);
				thunderEffect(mob);
				NFUParticleStatics.sendHeartParticlesToEntityDefault(mob);
				Mob bm = (Mob) doTaming(player, mob);
				bm.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 10 * 20));
				return true;
			}
			// Otherwise increase the progress and update timers
			else
			{
				progressIncrease(mob);
				NFUParticleStatics.sendParticlesToEntity(mob, ParticleTypes.EXPLOSION, 0.2, 0, 1, 0);
				CNFFTamable.get(mob).removeTimer(TIMER_KEY_FROZEN, true);
				CNFFTamable.get(mob).setTimer(TIMER_KEY_PEACH_SWORD, 60 * 20);
			}
			// phase 2->3 strike a thunder bolt
			if (oldPhase == 2 || getProgressLevel(mob) == 3)
			{
				thunderEffect(mob);
			}
			updateModifiers(mob);
			mob.level().playSound(null, mob, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 2f, 0.7f);
			CNFFTamable.get(mob).getGeneralNBT().putUUID(NBT_KEY_ONGOING_PLAYER, player.getUUID());
			return true;
		}
		// Prevent damage and effects during the friending process
		else return this.isInAnyProcess(mob);
	}
	
	
	// Freezing related
	/** Freezing is handled with {@link FreezeGoal} and {@link NFFGirlsEntityEventListeners#onEntityJoinLevel} */
	public int getFrozenTime(Mob mob)
	{
		CNFFTamable cap = CNFFTamable.get(mob);
		if (cap.hasTimer(TIMER_KEY_FROZEN))
			return cap.getTimerRemainingTime(TIMER_KEY_FROZEN);
		else return 0;
	}
	
	public static boolean isFrozen(Mob mob)
	{
		NFFTamingProcess handler = NFFTamingMapping.getProcess(mob);
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
		CNFFTamable cap = CNFFTamable.get(mob);
		if (getFrozenTime(mob) > 0)
			return false;
		else if (cap.hasTimer(TIMER_KEY_FREEZE_COOLDOWN))
			return false;
		else
		{
			cap.setTimer(TIMER_KEY_FROZEN, 200);
			return true;
		}
	}
	
	// Progress related
	
	public int getProgressLevel(Mob mob)
	{
		return CNFFTamable.get(mob).getGeneralNBT().getInt(NBT_KEY_PROGRESS);
	}
	
	public void setProgressLevel(Mob mob, int value)
	{
		if (value < 0 || value > 5)
			throw new IllegalArgumentException("Progress should be 0-5. Input: " + value);
		else
			CNFFTamable.get(mob).getGeneralNBT().putInt(NBT_KEY_PROGRESS, value);
		updateModifiers(mob);		
	}
	
	public void progressIncrease(Mob mob)
	{
		setProgressLevel(mob, getProgressLevel(mob) + 1);
	}
	
	public void progressDecrease(Mob mob)
	{
		setProgressLevel(mob, getProgressLevel(mob) - 1);
	}
	
	public int getAngerPhase(Mob mob)
	{
		return CNFFTamable.get(mob).getGeneralNBT().getInt(NBT_KEY_PROGRESS);
	}

	public void updateModifiers(Mob mob)
	{
		if (mob == null) return;
		MODIFIERS.apply(mob, getAngerPhase(mob));
	}

	@Override
	public void onGeneralTimerExpire(Mob mob, String key) {
		if (key.equals(TIMER_KEY_FROZEN)) {
			if (mob.getRandom().nextDouble() < 0.667)
				mob.spawnAtLocation(NFFGirlsItems.TAOIST_TALISMAN.get().getDefaultInstance());
			CNFFTamable.get(mob).setTimer(TIMER_KEY_FREEZE_COOLDOWN, 15 * 20);
		}
		else if (key.equals(TIMER_KEY_PEACH_SWORD)) {
			if (this.isInAnyProcess(mob) && getProgressLevel(mob) > 1) {
				this.progressDecrease(mob);
				CNFFTamable.get(mob).setTimer(TIMER_KEY_PEACH_SWORD, 60 * 20);
			} else {
				this.interruptAll(mob, true);
			}
		}
	}

	public void thunderEffect(Mob mob)
	{
		LightningBolt lb = EntityType.LIGHTNING_BOLT.create(mob.level());
		lb.moveTo(Vec3.atBottomCenterOf(mob.blockPosition()));
		lb.setDamage(0);
		mob.level().addFreshEntity(lb);
		mob.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 30 * 20));
		CNFFTamable.get(mob).setTimer(TIMER_KEY_CLEAR_FIRE, 10 * 20);
	}
}
