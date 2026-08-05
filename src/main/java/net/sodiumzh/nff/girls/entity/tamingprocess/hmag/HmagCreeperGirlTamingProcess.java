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
import net.sodiumzh.nff.services.entity.taming.NFFTamableComponent;
import net.sodiumzh.nff.services.entity.taming.TamingProcessItemGivingProgress;
import net.sodiumzh.nff.services.registry.NFFCapRegistry;
import net.sodiumzh.nfu.annotation.DontCallManually;
import net.sodiumzh.nfu.entity.anger.MobAngerRules;
import net.sodiumzh.nfu.util.NFUParticleStatics;
import org.apache.commons.lang3.mutable.MutableObject;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;

@Mod.EventBusSubscriber(modid = NFFGirls.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HmagCreeperGirlTamingProcess extends TamingProcessItemGivingProgress
{

	protected static final String NBT_KEY_FINAL_EXPLOSION_PLAYER = "final_explosion_player";
	protected static final String NBT_KEY_FINAL_EXPLOSION_TICKS_BEFORE = "final_explosion_ticks_before";
	protected static final String NBT_KEY_FINAL_EXPLOSION_TICKS_AFTER = "final_explosion_ticks_after";
	protected static final String TIMER_KEY_FINAL_EXPLOSION_FAIL_COOLDOWN = "final_explosion_fail_cooldown";

	@Override
	public void tamableInit(NFFTamableComponent c) {

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
		return !this.getTamable(mob).getTimerComponent().hasGeneralTimer(TIMER_KEY_FINAL_EXPLOSION_FAIL_COOLDOWN);
	}
	
	@Override
	public void serverTick(Mob mob)
	{
		CreeperGirlEntity cg = (CreeperGirlEntity)mob;
		NFFTamableComponent tamable = this.getTamable(mob);

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
	}
	
	protected void finalExplosionStart(CreeperGirlEntity mob, Player player)
	{
		NFFTamableComponent tamable = this.getTamable(mob);
		tamable.getGeneralNBT().putUUID(NBT_KEY_FINAL_EXPLOSION_PLAYER, player.getUUID());
		tamable.getGeneralNBT().putInt(NBT_KEY_FINAL_EXPLOSION_TICKS_BEFORE, 80);
		tamable.getGeneralNBT().putInt(NBT_KEY_FINAL_EXPLOSION_TICKS_AFTER, 5);
		mob.setNoAi(true);
		if (mob.getSwelling(1.0f) * 28.0f < 24.0f)	// getSwelling(1) is ((float)swell/28.0f)
			mob.setSwellDir(1);
		else mob.setSwellDir(-1);
	}
	
	protected void finalExplosionFailed(CreeperGirlEntity mob, Player player, boolean isQuiet)
	{
		NFFTamableComponent tamable = this.getTamable(mob);
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
	}
	
	protected void doFinalExplosion(CreeperGirlEntity mob, Player player)
	{
		mob.invulnerableTime += 2;
		Level.ExplosionInteraction interaction = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(mob.level(), mob)
				? Level.ExplosionInteraction.MOB : Level.ExplosionInteraction.NONE;
		mob.level().explode(mob, mob.getX(), mob.getY(), mob.getZ(), 6.0f, interaction);
		spawnLingeringCloud(mob);
	}

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

	@Override
	public int getItemGivingCooldownTicks() {
		return NFFGirlsTamingRules.COOLDOWN_SHORT;
	}

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
	public void onGeneralTimerExpire(Mob mob, String key) {
		if (key.equals(TIMER_KEY_FINAL_EXPLOSION_FAIL_COOLDOWN))
			mob.setNoAi(false);
	}

	@DontCallManually
	public boolean handleFinalExplosionKillingOtherTamedMob(Mob thisMob, Mob impactedMob) {
		// NFF tamed mobs won't be killed by CreeperGirl's "final explosion".
		// They leave 1 health and get invulnerable for 3s,
		// preventing them to be killed by falling down after blew up by the explosion.
		// Return if the death is prevented.
		NFFTamableComponent tamable = this.getTamable(thisMob);
		if (tamable.getGeneralNBT().contains("final_explosion_player", 11))
		{
			impactedMob.setHealth(1.0f);
			impactedMob.invulnerableTime += 60;
			NFUParticleStatics.sendGlintParticlesToEntityDefault(impactedMob);
			return true;
		}
		return false;
	}
}
