package net.sodiumzh.nff.girls.entity.tamingprocesses.hmag;

import com.github.mechalopa.hmag.world.entity.NecroticReaperEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.block.SoulCarpetBlock;
import net.sodiumzh.nff.girls.entity.projectile.NecromancerMagicBulletEntity;
import net.sodiumzh.nff.girls.registry.NFFGirlsItems;
import net.sodiumzh.nff.services.entity.capability.CNFFTamable;
import net.sodiumzh.nff.services.entity.taming.NFFTamingProcess;
import net.sodiumzh.nff.services.registry.NFFCapRegistry;
import net.sodiumzh.nfu.entity.anger.MobAngerRules;
import net.sodiumzh.nfu.entity.taming.TamingInteractionResult;
import net.sodiumzh.nfu.util.NFUEntityStatics;
import net.sodiumzh.nfu.util.NFUMathStatics;
import net.sodiumzh.nfu.util.NFUParticleStatics;

import javax.annotation.Nullable;

/**
 * Process mechanism:
 * Player must have Necromancer's Hat and Necromancer's Wand.
 * When the mob is standing on soul carpet, cast with wand to hit it.
 * 
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = NFFGirls.MOD_ID)
public class HmagNecroticReaperTamingProcess extends NFFTamingProcess
{
	
	protected static final String NBT_KEY_HIT_COUNT = "already_hits";
	protected static final String NBT_KEY_ONGOING_PLAYER_UUID = "ongoing_player_uuid";
	protected static final String TIMER_NO_ATTACK_EXPIRING_TIME = "no_attack_expire_time";

	/**
	 * Init on adding capability to mob
	 */
	@Override
	public void tamableInit(CNFFTamable cap)
	{
		if (!cap.getGeneralNBT().contains(NBT_KEY_HIT_COUNT, 3))
		{
			cap.getGeneralNBT().putInt(NBT_KEY_HIT_COUNT, 0);
		}
	}	
	
	/**
	 * Update modifier from current hit count, invoked every tick
	 */
	protected void updateStrengthEffect(CNFFTamable cap)
	{
		int hits = cap.getGeneralNBT().getInt(NBT_KEY_HIT_COUNT);
		if (hits < 0 || hits > 5)
			throw new RuntimeException("Wrong hit counts. Accepts: 0 ~ 5");
		if (hits == 0) return;
		NFUEntityStatics.addEffectSafe(cap.getEntity(), new MobEffectInstance(MobEffects.DAMAGE_BOOST, 5, 2 * hits - 2));
		NFUEntityStatics.addEffectSafe(cap.getEntity(), new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 5, hits / 2));
	}

	@Override
	public void interrupt(@Nullable Player player, Mob mob, boolean isQuiet) {
		CNFFTamable cap = CNFFTamable.get(mob);
		cap.getGeneralNBT().remove(NBT_KEY_ONGOING_PLAYER_UUID);
		cap.getGeneralNBT().putInt(NBT_KEY_HIT_COUNT, 0);
		mob.setLastHurtByPlayer(null);
		mob.setTarget(null);
		if (!isQuiet)
			NFUParticleStatics.sendAngryParticlesToEntityDefault(mob);
	}

	@Override
	public boolean interruptAll(Mob mob, boolean b) {
		if (CNFFTamable.get(mob).getGeneralNBT().contains(NBT_KEY_ONGOING_PLAYER_UUID)) {
			interrupt(null, mob, false);    // interrupt here doesn't involve player, see above
			return true;
		}
		return false;
	}

	@Override
	public boolean isInProcess(Player player, Mob mob) {
		return CNFFTamable.get(mob).getGeneralNBT().hasUUID(NBT_KEY_ONGOING_PLAYER_UUID)
				&& CNFFTamable.get(mob).getGeneralNBT().getUUID(NBT_KEY_ONGOING_PLAYER_UUID).equals(player.getUUID());
	}

	/**
	 *  Invoked on server when player hit the mob with Necromancer's Wand. (Only in server)
	 *  Return if the hit effects taken here. If true, it will cancel adding Wither effect.
	 *  <p>Handled by {@link NecromancerMagicBulletEntity} in {@code applyEffect} and {@code applyDirectEffect}.
	 */
	public boolean onHit(Player player, Mob mob)
	{
		CNFFTamable cap = CNFFTamable.get(mob);
		// If player not wearing necromancer's hat, it cannot be befriended, add wither
		if (!player.getItemBySlot(EquipmentSlot.HEAD).is(NFFGirlsItems.NECROMANCER_HAT.get())) return false;

		// If the mob is under befriending by other person who is present in the level, just add wither. If not,
		// you can continue taming the mob and inherit his progress
		else if (cap.getGeneralNBT().hasUUID(NBT_KEY_ONGOING_PLAYER_UUID)
				&& !cap.getGeneralNBT().getUUID(NBT_KEY_ONGOING_PLAYER_UUID).equals(player.getUUID())) {
			if (player.level.getPlayerByUUID(cap.getGeneralNBT().getUUID(NBT_KEY_ONGOING_PLAYER_UUID)) != null)
				return true;
		}
		// If mob is not on soul carpet, nothing happens but don't add wither
		else if (!SoulCarpetBlock.isEntityInside(mob)) return true;
		else if (cap.isAngryAt(player))
		{
			NFUParticleStatics.sendAngryParticlesToEntityDefault(mob);
			return true;
		}
		// Block if it's passenger
		else if (mob.isPassenger())
		{
			return true;
		}

		int hits = cap.getGeneralNBT().getInt(NBT_KEY_HIT_COUNT);

		/* Main process */

		// At this time this mob may be on process of others who is absent in the level. Just take the ownership and inherit the progress
		cap.getGeneralNBT().putUUID(NBT_KEY_ONGOING_PLAYER_UUID, player.getUUID());

		// The first hit, add player info
		if (hits == 0 || !cap.hasTimer(TIMER_NO_ATTACK_EXPIRING_TIME)/* 0.2.30.2 fixed a missing-timer issue. Port the old mob */)
		{
			// After 10s without attacking player, interrupt
			cap.setTimer(TIMER_NO_ATTACK_EXPIRING_TIME, 30 * 20);
		}

		// The last hit, befriend
		if (hits == 5)
		{
			NFUParticleStatics.sendHeartParticlesToEntityDefault(mob);
			doTaming(player, mob);
			return true;
		}
		// Otherwise just increase the number
		else
		{
			cap.getGeneralNBT().putInt(NBT_KEY_HIT_COUNT, hits + 1);
			NFUParticleStatics.sendGlintParticlesToEntityDefault(mob);
			return true;
		}
	}

	@Override
	public TamingInteractionResult handleInteract(Player player, Mob mob, InteractionHand interactionHand) {
		return TamingInteractionResult.unhandled(player.level);
	}

	@Override
	public void serverTick(Mob mob)
	{
		CNFFTamable cap = CNFFTamable.get(mob);
		updateStrengthEffect(cap);
		boolean isAlwaysHostile = false;
		if (	// Is in player process
			cap.getGeneralNBT().hasUUID(NBT_KEY_ONGOING_PLAYER_UUID)
			&& mob.level.getPlayerByUUID(cap.getGeneralNBT().getUUID(NBT_KEY_ONGOING_PLAYER_UUID)) != null)
		{
			Player player = mob.level.getPlayerByUUID(cap.getGeneralNBT().getUUID(NBT_KEY_ONGOING_PLAYER_UUID));
			/*mob.getCapability(NFFGirlsCapabilities.CAP_UNDEAD_AFFINITY_HANDLER).ifPresent((capUM) ->
			{
				capUM.addHatred(player, 300 * 20);	// This blocks the effect of undead affinity
			});*/
			assert player != null;
			if (!player.isCreative())
			{
				isAlwaysHostile = true;
				cap.setAlwaysHostileTo(player);
			}
			// Amount of particles emitting each frame
			int amountPerTick = 0;
			
			// Interrupt if player is > 32 blocks away from the mob
			if (mob.distanceToSqr(player) > 1024d)
			{
				interrupt(player, mob, false);
				return;
			}
			
			// If not attacked player for 10s, drop level by 1 (except creative)
			int atkCtd = cap.getGeneralNBT().getInt(TIMER_NO_ATTACK_EXPIRING_TIME);
			if (atkCtd <= 0)
			{
				if (!player.isCreative() && cap.getGeneralNBT().getInt(NBT_KEY_HIT_COUNT) > 0)
				{
					int hits = cap.getGeneralNBT().getInt(NBT_KEY_HIT_COUNT);
						cap.getGeneralNBT().putInt(NBT_KEY_HIT_COUNT, hits - 1);
					if (cap.getGeneralNBT().getInt(NBT_KEY_HIT_COUNT) <= 0)
						interrupt(player, mob, true);
					NFUParticleStatics.sendParticlesToEntity(mob, ParticleTypes.ANGRY_VILLAGER, mob.getBbHeight() - 0.2, 0.3d, 1, 1d);
					cap.getGeneralNBT().putInt(TIMER_NO_ATTACK_EXPIRING_TIME, 200);// Reset timer
				}

			}
			else 
			{
				cap.getGeneralNBT().putInt(TIMER_NO_ATTACK_EXPIRING_TIME, atkCtd - 1);
			}			

			// Send smoke particles during befriending process
			int hitCount = cap.getGeneralNBT().getInt(NBT_KEY_HIT_COUNT);
			amountPerTick = hitCount == 0 ? 0 : NFUMathStatics.getFibonacci(Mth.clamp(cap.getGeneralNBT().getInt(NBT_KEY_HIT_COUNT), 1, 5)).orElseThrow();
			if (amountPerTick > 0)
				NFUParticleStatics.sendParticlesToEntity(
					mob, ParticleTypes.SMOKE, mob.getBbHeight() - 0.2d, 0.5d, amountPerTick, 0d);			
		}
		if (!isAlwaysHostile)
		{
			cap.setAlwaysHostileTo(null);
		}
		
	}
	
	@Override
	public void onAttackProcessingPlayer(Mob mob, Player player, double damageGiven)
	{
		if (mob instanceof NecroticReaperEntity ee)
		{
			ee.getCapability(NFFCapRegistry.CAP_BEFRIENDABLE_MOB).ifPresent((cap) -> {
				if (cap.getGeneralNBT().contains(TIMER_NO_ATTACK_EXPIRING_TIME))
				{
					cap.getGeneralNBT().putInt(TIMER_NO_ATTACK_EXPIRING_TIME, 30 * 20);
				}						
			});
		}
	}

	@Override
	public void onGeneralTimerExpire(Mob mob, String key) {
		CNFFTamable tamable = CNFFTamable.get(mob);
		if (tamable.getGeneralNBT().hasUUID(NBT_KEY_ONGOING_PLAYER_UUID)) {
			Player player = tamable.getEntity().level.getPlayerByUUID(tamable.getGeneralNBT().getUUID(NBT_KEY_ONGOING_PLAYER_UUID));
			if (player == null || !player.isCreative() && tamable.getGeneralNBT().getInt(NBT_KEY_HIT_COUNT) > 0) {
				int hits = tamable.getGeneralNBT().getInt(NBT_KEY_HIT_COUNT);
				tamable.getGeneralNBT().putInt(NBT_KEY_HIT_COUNT, hits - 1);
				if (tamable.getGeneralNBT().getInt(NBT_KEY_HIT_COUNT) <= 0) {
					this.interrupt(player, tamable.getEntity(), true);
					return;
				}
				NFUParticleStatics.sendParticlesToEntity(tamable.getEntity(), ParticleTypes.ANGRY_VILLAGER, tamable.getEntity().getBbHeight() - 0.2, 0.3d, 2, 1d);
			}
			tamable.getGeneralNBT().putInt(TIMER_NO_ATTACK_EXPIRING_TIME, 30 * 20);// Reset timer
		} else {
			this.interruptAll(mob, false);
		}
	}
}
