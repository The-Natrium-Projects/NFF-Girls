package net.sodiumzh.nff.girls.entity.tamingprocess.hmag;

import com.github.mechalopa.hmag.world.entity.CreeperGirlEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.entity.NFFGirlsTamingRules;
import net.sodiumzh.nff.services.entity.capability.CNFFTamable;
import net.sodiumzh.nff.services.entity.taming.TamingProcessItemGivingProgress;
import net.sodiumzh.nff.services.registry.NFFCapRegistry;
import net.sodiumzh.nfu.annotation.DontCallManually;
import net.sodiumzh.nfu.entity.anger.MobAngerRules;
import net.sodiumzh.nfu.util.NFUParticleStatics;
import org.apache.commons.lang3.mutable.MutableObject;

import java.util.Collection;

@Mod.EventBusSubscriber(modid = NFFGirls.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HmagCreeperGirlTamingProcess extends TamingProcessItemGivingProgress
{

	protected static final String NBT_KEY_FINAL_EXPLOSION_PLAYER = "final_explosion_player";
	protected static final String NBT_KEY_FINAL_EXPLOSION_TICKS_BEFORE = "final_explosion_ticks_before";
	protected static final String NBT_KEY_FINAL_EXPLOSION_TICKS_AFTER = "final_explosion_ticks_after";
	protected static final String TIMER_KEY_FINAL_EXPLOSION_FAIL_COOLDOWN = "final_explosion_fail_cooldown";
	@Override
	public void tamableInit(CNFFTamable cnffTamable) {

	}

	@Override
	public Mob doTaming(Player player, Mob target)
	{
		target.setNoAi(false);
		return super.doTaming(player, target);
	}

	@Override
	public boolean additionalConditions(Player player, Mob mob)
	{
		MutableObject<Boolean> res = new MutableObject<Boolean>(false);
		mob.getCapability(NFFCapRegistry.CAP_BEFRIENDABLE_MOB).ifPresent((cap) -> 
		{
			res.setValue(!cap.hasTimer(TIMER_KEY_FINAL_EXPLOSION_FAIL_COOLDOWN));
		});
		return res.getValue();
	}
	
	@Override
	public void serverTick(Mob mob)
	{
		CreeperGirlEntity cg = (CreeperGirlEntity)mob;
		mob.getCapability(NFFCapRegistry.CAP_BEFRIENDABLE_MOB).ifPresent((tamable) ->
		{
			if (tamable.getGeneralNBT().hasUUID(NBT_KEY_FINAL_EXPLOSION_PLAYER))
			{
				
				if (cg.getSwelling(1.0f) * 28.0f <= 26.0f)
				{
					cg.setSwellDir(1);
				}
				else if (cg.getSwelling(1.0f) * 28.0f >= 28.0f)
				{
					cg.setSwellDir(-1);
				}
				
				Player player = mob.level().getPlayerByUUID(tamable.getGeneralNBT().getUUID(NBT_KEY_FINAL_EXPLOSION_PLAYER));
				// Fix reloading crash after quit after player die 
				if (player == null)
					return;
				int tb = tamable.getGeneralNBT().getInt(NBT_KEY_FINAL_EXPLOSION_TICKS_BEFORE);
				int ta = tamable.getGeneralNBT().getInt(NBT_KEY_FINAL_EXPLOSION_TICKS_AFTER);
				if (mob.distanceToSqr(player) >= 64.0f)
				{
					this.interrupt(player, cg, false);
				}
				else if (tb > 0)
				{
					if (tb % 3 == 1 || tb <= 13)
						NFUParticleStatics.sendGlintParticlesToEntityDefault(cg);
					NFUParticleStatics.sendSmokeParticlesToEntityDefault(cg);
					tamable.getGeneralNBT().putInt(NBT_KEY_FINAL_EXPLOSION_TICKS_BEFORE, tb - 1);
				}
				else if (ta > 0)
				{
					if (ta == 5)
					{
						doFinalExplosion((CreeperGirlEntity)mob, player);
					}
					tamable.getGeneralNBT().putInt(NBT_KEY_FINAL_EXPLOSION_TICKS_AFTER, ta - 1);
				}
				else
				{
					this.doTaming(player, mob);
					NFUParticleStatics.sendHeartParticlesToEntityDefault(mob);
				}
			}
		});
	}
	
	protected void finalExplosionStart(CreeperGirlEntity mob, Player player)
	{
		mob.getCapability(NFFCapRegistry.CAP_BEFRIENDABLE_MOB).ifPresent((tamable) ->
		{
			tamable.getGeneralNBT().putUUID(NBT_KEY_FINAL_EXPLOSION_PLAYER, player.getUUID());
			tamable.getGeneralNBT().putInt(NBT_KEY_FINAL_EXPLOSION_TICKS_BEFORE, 80);
			tamable.getGeneralNBT().putInt(NBT_KEY_FINAL_EXPLOSION_TICKS_AFTER, 5);
			mob.setNoAi(true);
			if (mob.getSwelling(1.0f) * 28.0f < 24.0f)	// getSwelling(1) is ((float)swell/28.0f)
				mob.setSwellDir(1);
			else mob.setSwellDir(-1);
		});
	}
	
	protected void finalExplosionFailed(CreeperGirlEntity mob, Player player, boolean isQuiet)
	{
		mob.getCapability(NFFCapRegistry.CAP_BEFRIENDABLE_MOB).ifPresent((tamable) ->
		{
			if (tamable.getGeneralNBT().contains(NBT_KEY_FINAL_EXPLOSION_PLAYER, 11) && tamable.getGeneralNBT().getUUID(NBT_KEY_FINAL_EXPLOSION_PLAYER).equals(player.getUUID()))
			{
					tamable.getGeneralNBT().remove(NBT_KEY_FINAL_EXPLOSION_PLAYER);
					tamable.getGeneralNBT().remove(NBT_KEY_FINAL_EXPLOSION_TICKS_BEFORE);
					tamable.getGeneralNBT().remove(NBT_KEY_FINAL_EXPLOSION_TICKS_AFTER);
					//l.setTimer(TIMER_KEY_FINAL_EXPLOSION_FAIL_COOLDOWN, 60);	/* NOT WORKING NOW */
					mob.setNoAi(false);
					mob.setSwellDir(-1);
					/*if (!isQuiet)
						for (int i = 0; i < 5; ++i)
							NaUtilsEntityStatics.sendAngryParticlesToLivingDefault(mob);*/
					//NaUtilsDebugStatics.debugPrintToScreen("Creeper Girl befriending failed.", player);
			}	
		});
	}
	
	protected void doFinalExplosion(CreeperGirlEntity mob, Player player)
	{
		mob.invulnerableTime += 2;
		Level.ExplosionInteraction interaction = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(mob.level(), mob)
				? Level.ExplosionInteraction.MOB : Level.ExplosionInteraction.NONE;
		mob.level().explode(mob, mob.getX(), mob.getY(), mob.getZ(), 12.0f, interaction);
		spawnLingeringCloud(mob);
	}
	/*
	@SubscribeEvent
	public static void onPlayerDie(LivingDeathEvent event) {

		if (event.getSource().getEntity() instanceof CreeperGirlEntity cg)
		{
			if (event.getEntity() instanceof Player player)
			{
				cg.getCapability(NFFCapRegistry.CAP_BEFRIENDABLE_MOB).ifPresent((l) ->
				{
					if (l.getNbt().contains(NBT_KEY_FINAL_EXPLOSION_PLAYER, 11)
							&& cg.level.getPlayerByUUID(l.getNbt().getUUID(NBT_KEY_FINAL_EXPLOSION_PLAYER)) == player)
					{
						((HmagCreeperGirlTamingProcess) (NFFTamingMapping.getHandler(ModEntityTypes.CREEPER_GIRL.get())))
								.finalExplosionFailed(cg, player, true);
					}
				});
			}
			else if (event.getEntity() instanceof INFFTamed bef)
			{
				cg.getCapability(NFFCapRegistry.CAP_BEFRIENDABLE_MOB).ifPresent((l) ->
				{
					if (l.getNbt().contains(NBT_KEY_FINAL_EXPLOSION_PLAYER, 11) && bef.getOwner() != null
							&& cg.level.getPlayerByUUID(l.getNbt().getUUID(NBT_KEY_FINAL_EXPLOSION_PLAYER)) == bef.getOwner())
					{
						
					}
				});
			}
		}
	}*/
	
	protected void spawnLingeringCloud(CreeperGirlEntity mob) {
		Collection<MobEffectInstance> collection = mob.getActiveEffects();
		if (!collection.isEmpty())
		{
			AreaEffectCloud areaeffectcloud = new AreaEffectCloud(mob.level(), mob.getX(), mob.getY(), mob.getZ());
			areaeffectcloud.setRadius(10F);	// 4x creeper explosion here
			areaeffectcloud.setRadiusOnUse(-0.5F);
			areaeffectcloud.setWaitTime(10);
			areaeffectcloud.setDuration(areaeffectcloud.getDuration() / 2);
			areaeffectcloud.setRadiusPerTick(-areaeffectcloud.getRadius() / (float) areaeffectcloud.getDuration());

			for (MobEffectInstance mobeffectinstance : collection)
			{
				areaeffectcloud.addEffect(new MobEffectInstance(mobeffectinstance));
			}

			mob.level().addFreshEntity(areaeffectcloud);
		}

	}


/*	@Override
	protected double getProcValueToAdd(ItemStack item, Player player, Mob mob, double lastProc) {
		double rnd = this.rnd.nextDouble();
		if (item.is(ModItems.LIGHTNING_PARTICLE.get()))
			return rnd < 0.1 ? 0.50 : (rnd < 0.4 ? 0.25 : 0.125);
		if (item.is(Items.GUNPOWDER))
			return NaUtilsMathStatics.rndRangedDouble(0.015, 0.03);
		else if (item.is(Items.TNT))
			return NaUtilsMathStatics.rndRangedDouble(0.03, 0.06);
		else return 0;
	}*/

	@Override
	public int getItemGivingCooldownTicks() {
		return NFFGirlsTamingRules.COOLDOWN_SHORT;
	}

	/*@Override
	public boolean isItemAcceptable(ItemStack item) {
		return item.is(Items.GUNPOWDER)
				|| item.is(Items.TNT)
				|| item.is(ModItems.LIGHTNING_PARTICLE.get());
	}*/

	@Override
	public Mob finalActions(Player player, Mob mob)
	{
		finalExplosionStart((CreeperGirlEntity)mob, player);
		return null;
	}
	
	@Override
	public void interrupt(Player player, Mob mob, boolean isQuiet)
	{
		super.interrupt(player, mob, isQuiet);
		this.finalExplosionFailed((CreeperGirlEntity) mob, player, isQuiet);
	}

	@Override
	public MobAngerRules getAngerRules() {
		return MobAngerRules.ATTACKER_DAMAGED_AND_ATTACKING.get();
	}

	@Override
	public void onGeneralTimerExpire(Mob mob, String key) {
		if (key.equals(TIMER_KEY_FINAL_EXPLOSION_FAIL_COOLDOWN))
			mob.setNoAi(false);
	}

	@DontCallManually
	public void handleFinalExplosionKillingOtherTamedMob(Mob thisMob, Mob impactedMob) {
		// NFF tamed mobs won't be killed by CreeperGirl's "final explosion".
		// They leave 1 health and get invulnerable for 3s,
		// preventing them to be killed by falling down after blew up by the explosion.
		CNFFTamable.getOptional(thisMob).ifPresent(tamable -> {
			if (tamable.getGeneralNBT().contains("final_explosion_player", 11))
			{
				impactedMob.setHealth(1.0f);
				impactedMob.invulnerableTime += 60;
				NFUParticleStatics.sendGlintParticlesToEntityDefault(impactedMob);
			}
		});
	}
}
