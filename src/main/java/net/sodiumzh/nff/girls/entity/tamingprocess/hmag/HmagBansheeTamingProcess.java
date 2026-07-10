package net.sodiumzh.nff.girls.entity.tamingprocess.hmag;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.sodiumzh.nff.girls.entity.NFFGirlsTamingRules;
import net.sodiumzh.nff.services.entity.taming.NFFTamableComponent;

public class HmagBansheeTamingProcess extends HmagVanillaUndeadTamingProcess
{
	@Override
	public boolean additionalConditions(Player player, Mob mob)
	{
		return witherRoseCondition(mob) && player.hasEffect(MobEffects.WITHER);
	}

	@Override
	public void serverTick(Mob mob)
	{
		Player ongoing = this.getOngoingPlayerInLevel(mob).orElse(null);
		if (ongoing != null) {
			if (ongoing.distanceToSqr(mob) > 32d * 32d)
				this.interrupt(ongoing, mob, true);
		}
		if (!witherRoseCondition(mob) && this.isInAnyProcess(mob))
		{
			NFFGirlsTamingRules.tickContinuousProgressLoss(this, mob);
		}
		if (ongoing != null && mob.hasLineOfSight(ongoing)) {
			this.getTamable(mob).setAlwaysHostileTo(ongoing);
		}
		else this.getTamable(mob).setAlwaysHostileTo(null);

	}

	// 8 wither roses in 15*15*15 range centered by mob
	public boolean witherRoseCondition(Mob mob)
	{
		AABB area = mob.getBoundingBox().inflate(7d, 7d, 7d);
		return mob.level.getBlockStates(area).filter((b) -> b.is(Blocks.WITHER_ROSE)).count() >= 8;
	}

	@SubscribeEvent
	public static void preventWitherInProcess(MobEffectEvent.Applicable event) {
		if (event.getEffectInstance().getEffect().equals(MobEffects.WITHER)
				&& event.getEntity() instanceof Mob e
				&& NFFTamableComponent.getOptional(e).map(tamable -> tamable.getTamingProcess() instanceof HmagBansheeTamingProcess
			&& tamable.getTamingProcess().isInAnyProcess(e)).orElse(false))
		{
			event.setResult(Event.Result.DENY);
		}
	}
}

