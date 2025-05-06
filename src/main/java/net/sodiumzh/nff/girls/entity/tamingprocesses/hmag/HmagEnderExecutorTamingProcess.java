package net.sodiumzh.nff.girls.entity.tamingprocesses.hmag;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.entity.NFFGirlsTamingRules;
import net.sodiumzh.nff.girls.registry.NFFGirlsAngerRules;
import net.sodiumzh.nff.girls.registry.NFFGirlsConfigs;
import net.sodiumzh.nff.services.entity.capability.CNFFTamable;
import net.sodiumzh.nff.services.entity.taming.NFFTamingProcess;
import net.sodiumzh.nff.services.entity.taming.TamingProcessItemGivingProgress;
import net.sodiumzh.nff.services.entity.taming.preset.NFFTamedEnderManPreset;
import net.sodiumzh.nfu.capability.EntityTimerAccessor;
import net.sodiumzh.nfu.entity.anger.MobAngerRules;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = NFFGirls.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HmagEnderExecutorTamingProcess extends TamingProcessItemGivingProgress
{
	// The latest player that gave an item. This controls always-hostile target.
	private static final EntityTimerAccessor TIMER_KEY_NO_ATTACK_EXPIRING = CNFFTamable.getTimerAccessor("noAttackExpiring");
	private static final int NO_ATTACK_EXPIRE_TIME = 30 * 20;

	@Override
	public void tamableInit(CNFFTamable cap)
	{
	}

	@Override
	public Mob doTaming(Player player, Mob target)
	{
		BlockState holdingBlock = null;
		if (target instanceof EnderMan e)
			holdingBlock = e.getCarriedBlock();
		Mob mob = super.doTaming(player, target);
		if (mob instanceof NFFTamedEnderManPreset tamed)
			tamed.setCarriedBlock(NFFGirlsConfigs.ValueCache.Misc.REMOVE_HAND_ITEM_ON_TAMING ? null :holdingBlock);
		return mob;
	}

	@Override
	public boolean additionalConditions(Player player, Mob mob) {
		return mob.getTarget() == player || player.isCreative();	// Creative-mode player can always befriend
	}

	@Override
	public int getItemGivingCooldownTicks() {
		return NFFGirlsTamingRules.COOLDOWN_LONG;
	}

	@Override
	public void serverTick(Mob mob)
	{
		super.serverTick(mob);
		Optional<UUID> playerUUID = this.getOngoingPlayerUUID(mob);
		if (playerUUID.isEmpty()) return;
		if (TIMER_KEY_NO_ATTACK_EXPIRING.getRemainingTime(mob) == 0    // Too long time not attacked
				|| mob.getTarget() == null                                  // Missing target
				|| !mob.getTarget().getUUID().equals(playerUUID.get())) {         // Attacking another target
			NFFGirlsTamingRules.tickContinuousProgressLoss(this, mob);
		}
		this.getTamable(mob).setAlwaysHostileTo(this.getOngoingPlayer(mob).orElse(null));
	}

	@Override
	public void interrupt(Player player, Mob mob, boolean isQuiet)
	{
		super.interrupt(player, mob, isQuiet);
		TIMER_KEY_NO_ATTACK_EXPIRING.removeTimer(mob, false);
	}

	@Override
	public MobAngerRules getAngerRules() {
		return NFFGirlsAngerRules.DEFAULT.get();
	}

	@NotNull
	@Override
	public MobAngerRules getInterruptingAngerRules() {
		return NFFGirlsAngerRules.DEFAULT.get();
	}

	@Override
	public void onAttackProcessingPlayer(Mob mob, Player player, double damageGiven)
	{
		TIMER_KEY_NO_ATTACK_EXPIRING.setTimer(mob, NO_ATTACK_EXPIRE_TIME);
	}

	public static boolean allowTeleport(Mob mob) {
		NFFTamingProcess processRaw = CNFFTamable.getOptional(mob).map(CNFFTamable::getTamingProcess).orElse(null);
		if (!(processRaw instanceof HmagEnderExecutorTamingProcess process)) return true;
		for (Player player: mob.level.players()) {
			if (player.distanceToSqr(mob) <= 16d * 16d && process.isInProcess(player, mob)) return false;
		}
		return true;
	}



	@SubscribeEvent
	public static void onEnderExecutorTeleport(EntityTeleportEvent.EnderEntity event) {
		if (event.getEntity() instanceof Mob e && !allowTeleport(e))
			event.setCanceled(true);
	}
}
