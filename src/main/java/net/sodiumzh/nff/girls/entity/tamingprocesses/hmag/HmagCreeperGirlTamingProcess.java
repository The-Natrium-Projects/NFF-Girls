package net.sodiumzh.nff.girls.entity.tamingprocesses.hmag;

import java.util.Collection;
import java.util.HashSet;

import com.github.mechalopa.hmag.registry.ModItems;
import com.github.mechalopa.hmag.world.entity.CreeperGirlEntity;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.mutable.MutableObject;
import net.sodiumzh.nautils.statics.NaUtilsMathStatics;
import net.sodiumzh.nautils.statics.NaUtilsEntityStatics;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nff.services.entity.taming.TamableHatredReason;
import net.sodiumzh.nff.services.entity.taming.TamingProcessItemGivingProgress;
import net.sodiumzh.nff.services.eventlisteners.TamableTimerUpEvent;
import net.sodiumzh.nff.services.registry.NFFCapRegistry;

@Mod.EventBusSubscriber(modid = NFFGirls.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HmagCreeperGirlTamingProcess extends TamingProcessItemGivingProgress
{
	
	@Override
	public INFFTamed doTaming(Player player, Mob target)
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
			res.setValue(!cap.hasTimer("final_explosion_fail_cooldown"));
		});
		return res.getValue();
	}
	
	@Override
	public void serverTick(Mob mob)
	{
		CreeperGirlEntity cg = (CreeperGirlEntity)mob;
		mob.getCapability(NFFCapRegistry.CAP_BEFRIENDABLE_MOB).ifPresent((l) ->
		{
			if (l.getNbt().contains("final_explosion_player", 11)) 
			{
				
				if (cg.getSwelling(1.0f) * 28.0f <= 26.0f)
				{
					cg.setSwellDir(1);
				}
				else if (cg.getSwelling(1.0f) * 28.0f >= 28.0f)
				{
					cg.setSwellDir(-1);
				}
				
				Player player = mob.level().getPlayerByUUID(l.getNbt().getUUID("final_explosion_player"));
				// Fix reloading crash after quit after player die 
				if (player == null)
					return;
				int tb = l.getNbt().getInt("final_explosion_ticks_before");
				int ta = l.getNbt().getInt("final_explosion_ticks_after");
				if (mob.distanceToSqr(player) >= 64.0f)
				{
					this.interrupt(player, cg, false);
				}
				else if (tb > 0)
				{
					if (tb % 3 == 1 || tb <= 13)
						NaUtilsEntityStatics.sendGlintParticlesToLivingDefault(cg);
					NaUtilsEntityStatics.sendSmokeParticlesToLivingDefault(cg);
					l.getNbt().putInt("final_explosion_ticks_before", tb - 1);
				}
				else if (ta > 0)
				{
					if (ta == 5)
					{
						doFinalExplosion((CreeperGirlEntity)mob, player);
					}
					l.getNbt().putInt("final_explosion_ticks_after", ta - 1);
				}
				else
				{
					this.doTaming(player, mob);
					NaUtilsEntityStatics.sendHeartParticlesToLivingDefault(mob);
				}
			}
		});
	}
	
	protected void finalExplosionStart(CreeperGirlEntity mob, Player player)
	{
		mob.getCapability(NFFCapRegistry.CAP_BEFRIENDABLE_MOB).ifPresent((l) ->
		{
			l.getNbt().putUUID("final_explosion_player", player.getUUID());
			l.getNbt().putInt("final_explosion_ticks_before", 80);
			l.getNbt().putInt("final_explosion_ticks_after", 5);
			mob.setNoAi(true);
			if (mob.getSwelling(1.0f) * 28.0f < 24.0f)	// getSwelling(1) is ((float)swell/28.0f)
				mob.setSwellDir(1);
			else mob.setSwellDir(-1);
		});
	}
	
	protected void finalExplosionFailed(CreeperGirlEntity mob, Player player, boolean isQuiet)
	{
		mob.getCapability(NFFCapRegistry.CAP_BEFRIENDABLE_MOB).ifPresent((l) ->
		{
			if (l.getNbt().contains("final_explosion_player", 11) && l.getNbt().getUUID("final_explosion_player").equals(player.getUUID()))
			{
					l.getNbt().remove("final_explosion_player");
					l.getNbt().remove("final_explosion_ticks_before");
					l.getNbt().remove("final_explosion_ticks_after");
					//l.setTimer("final_explosion_fail_cooldown", 60);	/* NOT WORKING NOW */
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
					if (l.getNbt().contains("final_explosion_player", 11)
							&& cg.level.getPlayerByUUID(l.getNbt().getUUID("final_explosion_player")) == player)
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
					if (l.getNbt().contains("final_explosion_player", 11) && bef.getOwner() != null
							&& cg.level.getPlayerByUUID(l.getNbt().getUUID("final_explosion_player")) == bef.getOwner())
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
		return 100;
	}

	/*@Override
	public boolean isItemAcceptable(ItemStack item) {
		return item.is(Items.GUNPOWDER)
				|| item.is(Items.TNT)
				|| item.is(ModItems.LIGHTNING_PARTICLE.get());
	}*/

	@Override
	public INFFTamed finalActions(Player player, Mob mob)
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

	// Duration of neutral added
	// -1 means permanent
	@Override
	public int getHatredDurationTicks(TamableHatredReason reason)
	{
		return 200;
	}
	
	@Override
	public HashSet<TamableHatredReason> getAddHatredReasons() {
		HashSet<TamableHatredReason> set = new HashSet<TamableHatredReason>();
		set.add(TamableHatredReason.ATTACKED);
		return set;
	}
	
	@SubscribeEvent
	public static void onTimerUp(TamableTimerUpEvent event)
	{
		if (event.getMob() != null && event.getMob().isAlive() && event.getMob() instanceof CreeperGirlEntity)
		{
			if (event.getKey().equals("final_explosion_fail_cooldown"))
				event.getMob().setNoAi(false);			
		}
	}
}
