package net.sodiumzh.nff.girls.entity.tamingprocess.hmag;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.entity.NFFGirlsTamingRules;
import net.sodiumzh.nff.girls.registry.NFFGirlsConfigs;
import net.sodiumzh.nff.services.entity.taming.NFFTamableComponent;
import net.sodiumzh.nff.services.entity.taming.NFFTamingProcess;
import net.sodiumzh.nff.services.entity.taming.TamingProcessItemGivingProgress;
import net.sodiumzh.nff.services.entity.taming.preset.NFFTamedEnderManPreset;

import java.util.Optional;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = NFFGirls.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HmagEnderExecutorTamingProcess extends TamingProcessItemGivingProgress
{
	// The latest player that gave an item. This controls always-hostile target.
	private static final String TIMER_KEY_NO_ATTACK_EXPIRING = "noAttackExpiring";
	private static final int NO_ATTACK_EXPIRE_TIME = 30 * 20;
	public static final String DATA_KEY_NO_TELEPORT = "nffgirlsEnderManNoTeleport";

	@Override
	public void tamableInit(NFFTamableComponent cap)
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
		NFFTamableComponent tamable = this.getTamable(mob);
		Optional<UUID> playerUUID = this.getOngoingPlayerUUID(mob);
		if (playerUUID.isEmpty()) return;
		if (tamable.getTimerComponent().getGeneralTimer(TIMER_KEY_NO_ATTACK_EXPIRING).filter(timer -> timer.getTicksRemaining() != 0).isEmpty()    // Too long time not attacked
				|| mob.getTarget() == null                                  // Missing target
				|| !mob.getTarget().getUUID().equals(playerUUID.get())) {         // Attacking another target
			NFFGirlsTamingRules.tickContinuousProgressLoss(this, mob);
		}
		this.getTamable(mob).setAlwaysHostileTo(this.getOngoingPlayer(mob).orElse(null));
		this.getTamable(mob).getDataComponent().putTransientVariable(DATA_KEY_NO_TELEPORT, true);
	}

	@Override
	public void interrupt(Player player, Mob mob, boolean isQuiet)
	{
		super.interrupt(player, mob, isQuiet);
		this.getTamable(mob).getTimerComponent().removeGeneralTimer(TIMER_KEY_NO_ATTACK_EXPIRING, false);
	}

	@Override
	public void onAttackProcessingPlayer(Mob mob, Player player, double damageGiven)
	{
		this.getTamable(mob).getTimerComponent().addTimer(TIMER_KEY_NO_ATTACK_EXPIRING, NO_ATTACK_EXPIRE_TIME, 1, true);
	}

	public static boolean allowTeleport(Mob mob) {
		NFFTamingProcess processRaw = NFFTamableComponent.getOptional(mob).map(NFFTamableComponent::getTamingProcess).orElse(null);
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
