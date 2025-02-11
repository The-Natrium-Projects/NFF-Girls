package net.sodiumzh.nff.girls.eventlisteners;

import com.github.mechalopa.hmag.HMaG;
import com.github.mechalopa.hmag.registry.ModItems;
import com.github.mechalopa.hmag.world.entity.EnderExecutorEntity;
import com.github.mechalopa.hmag.world.entity.GhastlySeekerEntity;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityMobGriefingEvent;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.nautils.entity.taming.ITamingProcessWithProgress;
import net.sodiumzh.nautils.statics.*;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.effect.NecromancerWitherEffect;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsTamableGhastlySeekerRandomFlyGoal;
import net.sodiumzh.nff.girls.entity.hmag.*;
import net.sodiumzh.nff.girls.entity.projectile.NecromancerMagicBulletEntity;
import net.sodiumzh.nff.girls.entity.tamingprocess.hmag.HmagCreeperGirlTamingProcess;
import net.sodiumzh.nff.girls.item.CombatCommandingWandItem;
import net.sodiumzh.nff.girls.item.NecromancerArmorItem;
import net.sodiumzh.nff.girls.registry.*;
import net.sodiumzh.nff.girls.util.NFFGirlsEntityStatics;
import net.sodiumzh.nff.services.entity.ai.NFFTamedMobAIState;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nff.services.entity.taming.NFFTamedStatics;
import net.sodiumzh.nff.services.entity.taming.NFFTamingMapping;
import net.sodiumzh.nff.services.event.entity.NFFMobTamedEvent;
import net.sodiumzh.nff.services.event.entity.ServerEntityTickEvent;
import net.sodiumzh.nff.services.event.entity.ai.NFFTamedChangeAiStateEvent;
import net.sodiumzh.nff.services.eventlisteners.NFFTamedDeathEvent;
import net.sodiumzh.nff.services.item.NFFMobOwnershipTransfererItem;
import net.sodiumzh.nff.services.item.NFFMobRespawnerItem;
import net.sodiumzh.nff.services.registry.NFFCapRegistry;
import org.apache.commons.lang3.mutable.MutableObject;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@SuppressWarnings("removal")
@Mod.EventBusSubscriber(modid = NFFGirls.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class NFFGirlsEntityEventListeners
{
	private static final EquipmentSlot[] ARMOR_SLOTS =
		{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
	private static final EquipmentSlot[] ARMOR_SLOT_HELMET =
		{EquipmentSlot.HEAD};
	private static final EquipmentSlot[] ARMOR_AND_HANDS =
		{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND, EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

	@SuppressWarnings("unused")
	@SubscribeEvent
	public static void onLivingSetAttackTarget(LivingSetAttackTargetEvent event)
	{
		@SuppressWarnings("deprecation")
		LivingEntity target = event.getTarget();		
		LivingEntity lastHurtBy = event.getEntityLiving().getLastHurtByMob();
		MutableObject<Boolean> isCancelledByEffect = new MutableObject<Boolean>(Boolean.FALSE);
		
		// Handle mobs //
		if (target != null && event.getEntity() instanceof Mob mob)
		{
			// Handle undead mobs start //
	        if (mob.getMobType() == MobType.UNDEAD 
	        		&& !(event.getEntity() instanceof INFFTamed) 
	        		&& !event.getEntity().getType().is(NFFGirlsTags.IGNORES_UNDEAD_AFFINITY)) 
	        {
	        	// Handle CUndeadAffinityHandler //
        		mob.getCapability(NFFGirlsCapabilities.CAP_UNDEAD_MOB).ifPresent((l) ->
        		{
        			if (target.hasEffect(NFFGirlsEffects.UNDEAD_AFFINITY.get()) && lastHurtBy != target && !l.getHatred().contains(target.getUUID()))
        			{
        				mob.setTarget(null);
        				isCancelledByEffect.setValue(true);
        			}
        			// Hatred will be added in priority-lowest event
        		});
        		// Handle CUndeadAffinityHandler end //
		    } 
	        // Handle undead mobs end //

	        // Befriendable mobs don't attack their befriended variation
	        if (NFFTamingMapping.contains(mob) 
	        		&& NFFTamingMapping.getConvertTo(mob) == target.getType()
	        		&& INFFGirlsTamed.isBM(target))
	        {
				mob.setTarget(null);
	        }
	        // Befriended mobs don't attack their wild variation
	        if (mob instanceof INFFTamed bef 
	        		&& bef.getModId().equals(NFFGirls.MOD_ID)
	        		&& NFFTamingMapping.getTypeBefore(mob) == target.getType())
	        {
				mob.setTarget(null);
	        }
	        // Handle Ghastly Seeker
	        if (mob instanceof HmagGhastlySeekerEntity gs)
	        {
	        	// If last target is still attackable, prevent removing target
	        	if (gs.lastTarget != null
	        		&& gs.lastTarget.isAlive() 
	        		&& gs.lastTarget.distanceToSqr(gs) <= gs.getAttributeValue(Attributes.FOLLOW_RANGE) * gs.getAttributeValue(Attributes.FOLLOW_RANGE)
	        		&& gs.hasLineOfSight(gs.lastTarget))
	        	{
	        		gs.setTarget(gs.lastTarget);
	        	}
	        	if (gs.getLastHurtByMob() != gs.getTarget() && gs.getAIState() == NFFTamedMobAIState.WAIT)
	        	{
	        		gs.setTarget(null);
	        	}
	        	gs.lastTarget = gs.getTarget();
	        }    
		}
		// Handle befriended mobs //

		// Handle mobs end //
	}
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onLivingSetAttackTarget_PriorityLowest(LivingSetAttackTargetEvent event)
	{
		if (event.getEntity() instanceof Mob mob)
		{
			// Undead mob add neutral here to prevent compat issues with other mods that can make undead mobs non-hostile
			event.getEntity().getCapability(NFFGirlsCapabilities.CAP_UNDEAD_MOB).ifPresent((l) ->
			{
				LivingEntity target = mob.getTarget();
				if (target != null && mob.getLastHurtByMob() == target)
				{
					l.addHatred(target, 295 * 20);
				}		
			});	        
		}
	}

	@SubscribeEvent
	public static void onLivingChangeTarget(LivingChangeTargetEvent event)
	{
		LivingEntity target = event.getNewTarget();
		if (target != null && event.getEntity() instanceof Mob mob) {
			// Tamable mobs don't attack their tamed variation
			if (NFFTamingMapping.contains(mob)
				&& NFFTamingMapping.getConvertTo(mob) == target.getType()
				&& INFFGirlsTamed.isBM(target)) {
				event.setCanceled(true);
				return;
			}
			// Tamed mobs don't attack their wild variation
			if (INFFGirlsTamed.isBM(mob)
				&& NFFTamingMapping.getTypeBefore(mob) == target.getType()) {
				event.setCanceled(true);
				return;
			}
			// When tamed mob is present, wild variation will not be hostile to player
			if (hasTamedVariationAround(mob, target))
			{
				event.setCanceled(true);
				return;
			}
		}
	}

	private static boolean hasTamedVariationAround(Mob attacker, LivingEntity target)
	{
		if (!(target instanceof Player p)) return false;
		EntityType<? extends Mob> tamedType = NFFTamingMapping.getConvertTo(attacker);
		if (tamedType == null) return false;
		List<Entity> tamed = p.getLevel().getEntities(attacker, attacker.getBoundingBox().inflate(16d))
			.stream().filter(e ->
				e instanceof Mob mob
				&& mob.getType().equals(tamedType)
				&& INFFGirlsTamed.isBMAnd(mob, m -> p.equals(m.getOwner())))
			.filter(attacker::hasLineOfSight)
			.toList();
		return !tamed.isEmpty();
	}

	@SubscribeEvent
	public static void onTamedDeath(NFFTamedDeathEvent event)
	{
		if (event.getDamageSource().getEntity() instanceof Mob srcMob)
		{
			srcMob.getCapability(NFFCapRegistry.CAP_BEFRIENDABLE_MOB).ifPresent((tamable) ->
			{
				if (tamable.getTamingProcess() instanceof HmagCreeperGirlTamingProcess process) {
					process.handleFinalExplosionKillingOtherTamedMob(srcMob, event.getMob().asMob());
					event.setCanceled(true);
				}
			});
			if (event.isCanceled())
				return;
		}
		
		if (event.getMob() instanceof HmagCreeperGirlEntity cg)
		{
			if (cg.isPowered())
				cg.spawnAtLocation(new ItemStack(ModItems.LIGHTNING_PARTICLE.get(), 1));
		}

		/* Favorability & Level */
		if (event.getMob() instanceof INFFGirlsTamed bm && bm.isOwnerInDimension())
		{
			// Favorability loss on death
			if (event.getDamageSource().getEntity() != null
					&& event.getDamageSource().getEntity() == bm.getOwner()
					&& event.getDamageSource() != DamageSource.OUT_OF_WORLD)
				bm.getFavorabilityHandler().setFavorability(0);
			else if (bm.asMob().distanceToSqr(bm.getOwner()) < 64d 
					&& bm.asMob().hasLineOfSight(bm.getOwner())
					&& !event.getDamageSource().equals(DamageSource.OUT_OF_WORLD))
				bm.getFavorabilityHandler().addFavorability(-20);
			// EXP loses by a half on death
			// As respawner construction (in nffservices) is after posting NFFTamedDeathEvent, it can be set here
			bm.getLevelHandler().setExp(bm.getLevelHandler().getExp() / 2);
		}
		/* Favorability & Level end */
	}
	
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onPlayerAttack_PriorityHighest(AttackEntityEvent event)
	{
		if (!event.getEntity().level.isClientSide)
		{
			/*event.getEntity().getCapability(NFFCapRegistry.CAP_BM_PLAYER).ifPresent(c -> {
				c.getNbt().putUUID("directly_attacking", event.getTarget().getUUID());
			});*/
		}
	}
	
	@SubscribeEvent
	public static void onLivingHurt(LivingHurtEvent event) {
		/** Compat */

		//CompatEventHandlers.onLivingHurt(event);
		if (event.isCanceled())
			return;

		/** Compat end */
					
		LivingEntity living = event.getEntityLiving();
		if (!living.level.isClientSide)
		{
			// Cancel necromancer magic bullet normal attack
			if (event.getSource().getDirectEntity() != null && event.getSource().getDirectEntity() instanceof NecromancerMagicBulletEntity)
			{
				event.setCanceled(true);
				return;
			}
			
			// Cancel indirect player attack from owner e.g. sweeping
			if (event.getEntity() instanceof INFFGirlsTamed bm
					&& event.getSource().msgId.equals("player")
					&& event.getSource().getEntity() != null
					&& event.getSource().getEntity() instanceof Player player
					&& bm.isOwnerPresent()
					&& bm.getOwner() == player)
			{
				player.getCapability(NFFCapRegistry.CAP_BM_PLAYER).ifPresent(cap -> 
				{
					/*if (cap.getNbt().hasUUID("directly_attacking") 
							&& !cap.getNbt().getUUID("directly_attacking").equals(bm.asMob().getUUID()))
					{
						event.setCanceled(true);
						return;
					}*/
				});
			}
			
			
			/** Ender Protection Effect */
			if (living.hasEffect(NFFGirlsEffects.ENDER_PROTECTION.get()))
			{
				// If the player drops into the void, try pull up
				if (event.getSource().equals(DamageSource.OUT_OF_WORLD))
				{
					// Ignore damage by /kill
					if (living.getY() < -64.0d)
					{
						// Lift up
						living.setPosRaw(living.getX(), 64.0d, living.getZ());
						// and find a standable block
						NaUtilsEntityStatics.chorusLikeTeleport(living);
						living.level.addParticle(ParticleTypes.PORTAL, living.getRandomX(0.5D),
								living.getRandomY() - 0.25D, living.getRandomZ(0.5D),
								(living.getRandom().nextDouble() - 0.5D) * 2.0D, -living.getRandom().nextDouble(),
								(living.getRandom().nextDouble() - 0.5D) * 2.0D);
						living.removeEffect(NFFGirlsEffects.ENDER_PROTECTION.get());

						// whether player is standing on a solid block
						BlockPos standingOn = new BlockPos(living.blockPosition().getX(),
								living.blockPosition().getY() - 1, living.blockPosition().getZ());
						if (living.level.getBlockState(standingOn).is(Blocks.AIR))
						{
							// failed, add slow falling
							if (living instanceof Player p)
							{
								NaUtilsMiscStatics.printToScreen(
										NaUtilsInfoStatics.createTranslatable("info.nffgirls.ender_protection_lift_teleport_failed")
										/*"You're lifted from the void because of the Ender Protection, but..."*/, p);
							}
							living.setDeltaMovement(new Vec3(0, 0, 0)); // Velocity
							living.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 200));
						} 
						else
						{
							// succeeded
							if (living instanceof Player p)
							{
								NaUtilsMiscStatics.printToScreen(/*""*/
										NaUtilsInfoStatics.createTranslatable("info.nffgirls.ender_protection_lift"), p);
							}
						}
					}
				}
				else if (!event.getSource().equals(DamageSource.IN_FIRE)
						&& !event.getSource().equals(DamageSource.STARVE))
				{
					NaUtilsParticleStatics.sendParticlesToEntity(living, ParticleTypes.PORTAL, 0, living.getBbHeight()/2, 0, 0.5, living.getBbHeight()/2, 0.5, 2, 1);
					/*living.level.addParticle(ParticleTypes.PORTAL, 
							living.getRandomX(0.5D), 
							living.getRandomY() - 0.25D,
							living.getRandomZ(0.5D), 
							(living.getRandom().nextDouble() - 0.5D) * 2.0D,
							-living.getRandom().nextDouble(), 
							(living.getRandom().nextDouble() - 0.5D) * 2.0D);*/
					NaUtilsEntityStatics.chorusLikeTeleport(living);
				}
			} 
			/** Ender Protection Effect end */
			
			/** Durability */
			// Weapon durability
			if (event.getSource().getEntity() != null)
			{
				INFFGirlsTamed.ifBM(event.getSource().getEntity(), bm -> {
					if (!bm.asMob().getMainHandItem().isEmpty() && bm.asMob().getMainHandItem().getItem() instanceof DiggerItem dg)
					{
						bm.asMob().getMainHandItem().hurtAndBreak(2, bm.asMob(), (mob) ->
						{
							mob.broadcastBreakEvent(EquipmentSlot.MAINHAND);
						});			
					}
					if (!bm.asMob().getMainHandItem().isEmpty() && bm.asMob().getMainHandItem().getItem() instanceof SwordItem sw)
					{
						bm.asMob().getMainHandItem().hurtAndBreak(1, bm.asMob(), (mob) ->
						{
							mob.broadcastBreakEvent(EquipmentSlot.MAINHAND);
						});
					}
				});
			}
			// Armor durability
			if (event.getEntity() instanceof INFFGirlsTamed bm
					&& bm.getModId().equals(NFFGirls.MOD_ID))
			{
				if (!bm.asMob().getItemBySlot(EquipmentSlot.HEAD).isEmpty())
				{
					if (event.getSource().isDamageHelmet())
					{
						hurtHelmet(bm.asMob(), event.getSource(), event.getAmount());
					}
					hurtArmor(bm.asMob(), event.getSource(), event.getAmount());
				}				
			}
			
			/** Durability end */
			
			// Label player on bef mob attacking target, just like for TamableAnimal, so that it can drop player's loot table
			if (event.getEntity() instanceof Mob mob
					&& event.getSource().getEntity() != null
					&& event.getSource().getEntity() instanceof INFFGirlsTamed bm)
			{
				mob.setLastHurtByPlayer(bm.getOwner());
			}

			/** Cancel friendly damage */
			if (event.getSource() instanceof EntityDamageSource eds && !NFFGirlsConfigs.ValueCache.Combat.ENABLE_FRIENDLY_DAMAGE)
			{
				if (INFFGirlsTamed.isBMAnd(eds.getEntity(), tamed ->
					Optional.ofNullable(tamed.getOwnerInDimension()).map(owner -> owner == event.getEntity()).orElse(false)))
				{
					event.setCanceled(true);
					return;
				}
				if (INFFGirlsTamed.isBMAnd(event.getEntity(), tamed ->
					Optional.ofNullable(tamed.getOwnerInDimension()).map(owner -> owner == eds.getEntity()).orElse(false)))
				{
					event.setCanceled(true);
					return;
				}
			}

			/** Cancel Ghastly Seeker friendly damage */

			if (event.getSource() instanceof EntityDamageSource eds && eds.getEntity() instanceof HmagGhastlySeekerEntity gs)
			{
				if (NFFGirlsEntityStatics.isAlly(gs, event.getEntityLiving()))
				{
					event.setCanceled(true);
					return;
				}
			}
			
			/** Cancel projectile friendly damage */
			if (event.getSource() instanceof EntityDamageSource eds && eds.getEntity() instanceof INFFGirlsTamed bm && eds.getDirectEntity() instanceof Projectile)
			{
				if (!NFFGirlsConfigs.ValueCache.Combat.ENABLE_PROJECTILE_FRIENDLY_DAMAGE && NFFGirlsEntityStatics.isAlly(bm, event.getEntityLiving()))
				{
					event.setCanceled(true);
					return;
				}
			}
		}
	}
		
	protected static void hurtArmor(Mob mob, DamageSource damageSource, float damage, EquipmentSlot[] slots)
	{
		// Ignore effect of /kill
		if (damageSource.getMsgId().equals(DamageSource.OUT_OF_WORLD.getMsgId()) && damage > 1000)
			return;
		if (damageSource.isBypassArmor())
			return;
		if (!(damage <= 0.0F))
		{
			damage /= 4.0F;
			if (damage < 1.0F)
			{
				damage = 1.0F;
			}
			for (EquipmentSlot slot : slots)
			{
				ItemStack itemstack = mob.getItemBySlot(slot);
				if ((!damageSource.isFire() || !itemstack.getItem().isFireResistant())
						&& itemstack.getItem() instanceof ArmorItem)
				{
					itemstack.hurtAndBreak((int) damage, mob, (m) ->
					{
						m.broadcastBreakEvent(EquipmentSlot.byTypeAndIndex(EquipmentSlot.Type.ARMOR, slot.getIndex()));
					});
				}
			}
		}
	}
	
	protected static void hurtArmor(Mob mob, DamageSource damageSource, float damage)
	{
		hurtArmor(mob, damageSource, damage, ARMOR_SLOTS);
	}
	
	protected static void hurtHelmet(Mob mob, DamageSource damageSource, float damage)
	{
		hurtArmor(mob, damageSource, damage, ARMOR_SLOT_HELMET);
	}
	
	
	@SubscribeEvent
	public static void onEnderTeleport(EntityTeleportEvent.EnderEntity event)
	{
		if (event.getEntityLiving() instanceof EnderExecutorEntity ee)
		{
			ee.getCapability(NFFCapRegistry.CAP_BEFRIENDABLE_MOB).ifPresent((tamable) ->
			{
				if (tamable.getGeneralNBT().getBoolean("cannot_teleport"))
				{
					// Still teleport in water
					if (!ee.isInWater())
					{
						event.setCanceled(true);
					}
				}
			});
		}
	}
	
	/*@SubscribeEvent
	public static void onBefriendedAttributeChange(CAttributeMonitor.ChangeEvent event)
	{
		if (event.entity instanceof INFFTamed b 
				&& b.getModId().equals(NFFGirls.MOD_ID)
				&& event.attribute.equals(Attributes.MAX_HEALTH)
				)
		{
			event.entity.setHealth((float) (event.entity.getHealth() * event.newValue / event.oldValue));
		}
	}*/
	
	@SubscribeEvent
	public static void onLivingTick(LivingUpdateEvent event)
	{
		// Necromancer armor
		if (!event.getEntity().level.isClientSide)
		{
			NecromancerArmorItem.necromancerArmorUpdate(event.getEntityLiving());
			
			if (event.getEntity() instanceof Mob mob)
			{
				// Undead mob forgiving player
				// TODO Is this out-of-date?
				mob.getCapability(NFFGirlsCapabilities.CAP_UNDEAD_AFFINITY_HANDLER).ifPresent(cap ->
				{
					cap.updateForgivingTimers();
					if (mob.getTarget() != null && mob.getTarget().hasEffect(NFFGirlsEffects.UNDEAD_AFFINITY.get()) && !cap.getHatred().contains(mob.getTarget().getUUID()))
						mob.setTarget(null);
				});
				// Sync mobs
				if (mob instanceof INFFGirlsTamed bm)
					bm.doSync();
			}
			// Send overlap event
			if (event.getEntity() instanceof INFFGirlsTamed bm)
			{
				if (bm.asMob().getHealth() > 0.0F) {
			         AABB aabb;
			         if (bm.asMob().isPassenger() && !bm.asMob().getVehicle().isRemoved()) {
			            aabb = bm.asMob().getBoundingBox().minmax(bm.asMob().getVehicle().getBoundingBox()).inflate(1.0D, 0.0D, 1.0D);
			         } else {
			            aabb = bm.asMob().getBoundingBox().inflate(1.0D, 0.5D, 1.0D);
			         }

			         List<Entity> list = bm.asMob().level.getEntities(bm.asMob(), aabb);

			         for(int i = 0; i < list.size(); ++i) {
			            Entity entity = list.get(i);
			            if (!entity.isRemoved()) {
			               bm.touchEntity(entity);
			            }
			         }
			     }
			}
			/* Handle necromancer wither effect */
			if (event.getEntityLiving().hasEffect(NFFGirlsEffects.NECROMANCER_WITHER.get()))
			{
				// Wither skeletons are immune to this effect
				if (event.getEntityLiving() instanceof WitherSkeleton)
					event.getEntityLiving().removeEffect(NFFGirlsEffects.NECROMANCER_WITHER.get());
				else
				{
					int ampl = event.getEntityLiving().getEffect(NFFGirlsEffects.NECROMANCER_WITHER.get()).getAmplifier();
					if (event.getEntity().tickCount % NecromancerWitherEffect.deltaTickPerDamage(ampl) == 0)
					{
						if (!(event.getEntity() instanceof Player player && (player.isCreative() || player.isSpectator()))
							|| event.getEntity() instanceof WitherSkeleton
							|| !event.getEntityLiving().canBeAffected(new MobEffectInstance(MobEffects.WITHER)))
						{
							event.getEntityLiving().getCombatTracker().recordDamage(NFFGirlsDamageSources.NECROMANCER_WITHER, event.getEntityLiving().getHealth(), 1f);
							float amount = 1f;
							if (event.getEntityLiving().getAbsorptionAmount() > 1f)
							{
								event.getEntityLiving().setAbsorptionAmount(event.getEntityLiving().getAbsorptionAmount() - 1f);
								amount = 0f;
							}
							else if (event.getEntityLiving().getAbsorptionAmount() > 0f)
							{
								amount -= event.getEntityLiving().getAbsorptionAmount();
								event.getEntityLiving().setAbsorptionAmount(0f);
							}
							if (amount > 0f)
							{
								event.getEntityLiving().setHealth(event.getEntityLiving().getHealth() - 1f);
								if (event.getEntityLiving().getHealth() <= 0f)
									event.getEntityLiving().die(NFFGirlsDamageSources.NECROMANCER_WITHER);
							}
						}
					}
				}
			}
			// In Combat Commanding Wand it will manually set target, which may cause the mob to keep attacking
			// after the target dies. Fix it here
			INFFGirlsTamed.ifBM(event.getEntity(), tamed -> {
				if (tamed.asMob().getTarget() != null && !tamed.asMob().getTarget().isAlive())
					tamed.asMob().setTarget(null);
			});
		}
	}
	
	@SubscribeEvent
	public static void onBMOverlap(INFFGirlsTamed.OverlapEntityEvent event)
	{
		if (event.touchedEntity instanceof Slime slime)
		{
		      if (!slime.isTiny() && slime.isEffectiveAi() && slime.getTarget() == event.thisMob.asMob()) 
		      {
		    	  NaUtilsReflectionStatics.forceInvoke(slime, Slime.class, "m_33637_", 	// Slime#dealDamage()
		    			  LivingEntity.class, event.thisMob.asMob());
		      }
		}
	}
	
	@SubscribeEvent
	public static void onBefriendedSwitchAiState(NFFTamedChangeAiStateEvent event)
	{
		if (INFFGirlsTamed.isBM(event.getMob()) && !event.getMob().asMob().level.isClientSide)
		{
			NaUtilsMiscStatics.printToScreen(NaUtilsInfoStatics.createText("")
					.append(event.getMob().asMob().getName())
					.append(NaUtilsInfoStatics.createText(" "))
					.append(event.getStateAfter().getDisplayInfo()), event.getMob().getOwner());
		}
	}

	@SubscribeEvent
	public static void onNonBefriendedDie(LivingDeathEvent event)
	{
		if (!event.getEntity().level.isClientSide)
		{
			// This function only handler non-befriended
			if (event.getEntity() instanceof INFFTamed)
				return;
			// When BM killed a mob targeting the player, favorability + 0.5 
			if (event.getSource().getEntity() != null 
					&& event.getSource().getEntity() instanceof INFFGirlsTamed bm
					&& event.getEntity() instanceof Mob mob
					&& mob.getTarget() != null
					&& mob.getTarget() == bm.getOwner())
			{
				bm.getFavorabilityHandler().addFavorability(0.5f);
			}
			// When player killed a mob targeting BM, fav + 1
			if (event.getSource().getEntity() != null
					&& event.getSource().getEntity() instanceof Player player
					&& event.getEntity() instanceof Mob mob
					&& mob.getTarget() instanceof INFFGirlsTamed bm
					&& bm.getOwner() == player)
			{
				bm.getFavorabilityHandler().addFavorability(1f);
			}
			
		}
	}
	
	/**
	 * Actions before checking if mob is killed by player
	 */
	@SubscribeEvent
	public static void onGetLootLevel(LootingLevelEvent event)
	{
		if (event.getDamageSource() != null
			&& event.getDamageSource().getEntity() != null
			&& event.getDamageSource().getEntity() instanceof INFFGirlsTamed bm)
		{
			/** After this, vanilla will use LivingEntity#lastHurtByPlayerTime to check if it's killed by player
			 * so force set this to make it drop player-kill loot */
			NaUtilsReflectionStatics.forceSet(event.getEntityLiving(), LivingEntity.class, "f_20889_",  1);	// LivingEntity.lastHurtByPlayerTime
			/** For mobs with tag "use_fortune_as_looting", Fortune enchantment is applied in place of Looting */
			if (bm.asMob().getType().is(NFFGirlsTags.USES_FORTUNE_AS_LOOTING) && !bm.asMob().getItemBySlot(EquipmentSlot.MAINHAND).isEmpty())
			{
				event.setLootingLevel(Math.max(EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, bm.asMob().getItemBySlot(EquipmentSlot.MAINHAND)),
						EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MOB_LOOTING, bm.asMob().getItemBySlot(EquipmentSlot.MAINHAND))));
			}
		}
	}
	
	
	@SubscribeEvent
	public static void onDropExp(LivingExperienceDropEvent event)
	{
		// When a mob is killed by a befriended mob, it don't drop exp orbs, but directly add exp to the mob.
		if (event.getEntityLiving().getLastHurtByMob() != null 
				&& event.getEntityLiving().getLastHurtByMob() instanceof INFFGirlsTamed bm)
		{
			int exp = event.getOriginalExperience();
			exp = (int)handleMending(exp, bm.asMob());
			bm.getLevelHandler().addExp(exp);
			event.setCanceled(true);
		}
	}
	
	
	/** Handle equipment fixing from Mending enchantment for mobs, and return the exp remains
	 * 
	 * @param noUpdateInventory if true, the mob additional inventory will not be updated from equipment, and it should be manually synced. 
	 * It should be true when sometimes updating inventory may cause AI goal change which lead to ConcurrentModificationException 
	 * @return Exp remained after mending.
	 */
	protected static long handleMending(long expBefore, Mob mob, boolean noUpdateInventory)
	{
		if (expBefore >= (long) Integer.MAX_VALUE)
		{
			throw new UnsupportedOperationException("Adding too many exp (more than INT_MAX).");
		}
		
		int remained = (int)expBefore;
		ItemStack[] items = new ItemStack[7];
		for (int i = 0; i < 6; ++i)
		{
			items[i > 1 ? i + 1 : i] = mob.getItemBySlot(ARMOR_AND_HANDS[i]);
		}
		// Sometimes head item is moved to the temp objects for handling sun immunity, so insert it in front of head items
		// This isn't used anymore
		/*if (mob instanceof INFFTamed bm && bm.getData().getNbt().contains("head_item"))
		{
			items[2] = NaUtilsNBTStatics.readItemStack(bm.getData().getNbt(), "head_item");
		}
		else items[2] = ItemStack.EMPTY;*/
		items[2] = ItemStack.EMPTY;	// TODO: fully refactor this and remove it
		
		for (int i = 0; i < 7; ++i)
		{
			if (!items[i].isEmpty()
					&& items[i].isDamageableItem() 
					&& items[i].getDamageValue() > 0
					&& EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MENDING,  items[i]) > 0)
			{
				// If cannot fix up
				if (items[i].getDamageValue() > remained * 2)
				{
					items[i].setDamageValue(
							(int) (items[i].getDamageValue() - 2 * remained));
					remained = 0;
				}
				else
				{
					int needed = (items[i].getDamageValue() + 1) / 2;
					items[i].setDamageValue(0);
					remained -= needed;
				}
				if (remained < 0)
				{
					throw new RuntimeException("Math error: handleMending");
				}
				else if (remained == 0)
				{
					if (mob instanceof INFFTamed bm && !noUpdateInventory)
						bm.setInventoryFromMob();
					return 0;
				}				
			}
		}
		if (mob instanceof INFFTamed bm && !noUpdateInventory)
			bm.setInventoryFromMob();
		return (long)remained;
	}

	protected static long handleMending(long expBefore, Mob mob)
	{
		return handleMending(expBefore, mob, false);
	}
	
	@SubscribeEvent
	public static void onLivingDamage(LivingDamageEvent event)
	{
		if (!event.getEntity().level.isClientSide && event.getEntity() instanceof Mob mob && !event.isCanceled())
		{
			if (event.getSource().getEntity() != null && event.getSource().getEntity() instanceof LivingEntity source)
			{
				// Favorbility change
				// On player attack a mob attacking the BM
				if (source instanceof Player player 
						&& mob.getTarget() != null
						&& mob.getTarget() instanceof INFFGirlsTamed bm
						&& bm.asMob().isAlive()
						&& bm.getOwner() == player)
				{
					bm.getFavorabilityHandler().addFavorability(event.getAmount() / 50f);
				}
				// On BM attack a mob attacking the player
				if (source instanceof INFFGirlsTamed bm
						&& mob.getTarget() != null
						&& mob.getTarget() instanceof Player player
						&& bm.asMob().isAlive()
						&& bm.getOwner() == player)
				{
					bm.getFavorabilityHandler().addFavorability(event.getAmount() / 100f);
				}
				// If owner attacked friendly mob, lose favorability depending on damage; no lost if < 0.5
				if (event.getSource().getEntity() != null
						&& event.getSource().getEntity() instanceof Player player
						&& INFFGirlsTamed.isBMAnd(event.getEntity(), bm -> bm.getOwnerUUID().equals(player.getUUID()))
						&& !event.getSource().equals(DamageSource.OUT_OF_WORLD)
						&& !event.getSource().isCreativePlayer())
				{
					if (event.getAmount() >= 0.5f)
					{
						event.getEntity().getCapability(NFFGirlsCapabilities.CAP_FAVORABILITY_HANDLER).ifPresent((cap) -> 
						{
							float loseValue = event.getAmount() / 2f;
							if (loseValue > 10f)
								loseValue = 10f;
							cap.addFavorability(-loseValue);
							if (loseValue < 1.0f)
								NaUtilsParticleStatics.sendSmokeParticlesToEntityDefault(event.getEntityLiving());
							else
								NaUtilsParticleStatics.sendAngryParticlesToEntityDefault(event.getEntityLiving());
						});
					}
				}
			}
		}
	}

	@SuppressWarnings("resource")
	@SubscribeEvent
	public static void onEntityJoinLevel(EntityJoinWorldEvent event)
	{
		if (!event.getWorld().isClientSide)
		{
			if (event.getEntity() instanceof Mob mob && !(event.getEntity() instanceof INFFTamed))
			{
				/** Handle mob hostility */
				//Predicate<LivingEntity> none = (l) -> true;
				Predicate<LivingEntity> isNotWaiting = NFFGirlsEntityStatics::isNotWaiting;
				Predicate<LivingEntity> isNotWearingGold = NFFGirlsEntityStatics::isNotWearingGold;
				Predicate<LivingEntity> isUndead = (living -> living.getMobType() == MobType.UNDEAD);
				
				// Illagers and witches attack all mobs
				if (mob.getMobType() == MobType.ILLAGER)
				{
					setHostileToAllBefriendedMobs(mob, isNotWaiting);
				}
				// Skeletons hostile to zombies & creepers
				else if (mob instanceof AbstractSkeleton 
					&& !(EntityType.getKey(mob.getType()).getNamespace().equals(HMaG.MODID))	// Exclude HMAG mob girls
					/*&& NaUtilsAIStatics.isMobHostileToPlayer(mob)*/)	// For hostile mobs only // Something is wrong with NaUtilsAIStatics#isMobHostileToPlayer
				{
					NaUtilsAIStatics.setHostileTo(mob, HmagZombieGirlEntity.class);
					NaUtilsAIStatics.setHostileTo(mob, HmagHuskGirlEntity.class);
					NaUtilsAIStatics.setHostileTo(mob, HmagDrownedGirlEntity.class);
					NaUtilsAIStatics.setHostileTo(mob, HmagCreeperGirlEntity.class);
				}
				// Zombies (including Zombified Piglins and Zoglins) hostile to skeletons & creepers
				if ((mob instanceof Zombie || mob instanceof Zoglin)
						&& !(EntityType.getKey(mob.getType()).getNamespace().equals(HMaG.MODID)))	// Exclude HMAG mob girls
				{
					//NaUtilsDebugStatics.debugPrintToScreen("Zombie add hostility", player);
					NaUtilsAIStatics.setHostileTo(mob, HmagSkeletonGirlEntity.class);
					NaUtilsAIStatics.setHostileTo(mob, HmagStrayGirlEntity.class);
					NaUtilsAIStatics.setHostileTo(mob, HmagWitherSkeletonGirlEntity.class);
					NaUtilsAIStatics.setHostileTo(mob, HmagCreeperGirlEntity.class);
					/*NaUtilsDebugStatics.debugPrintToScreen("Zombie add hostility end", player);
					for (WrappedGoal wg: mob.goalSelector.getAvailableGoals())
					{
						NaUtilsDebugStatics.debugPrintToScreen(wg.getGoal().getClass().getTypeName(), player);
					}*/
				}
				// Piglins hostile to all mobs not wearing gold
				if (mob instanceof Piglin)
				{
					setHostileToAllBefriendedMobs(mob, isNotWearingGold);
				}
				// Piglin brutes, Hoglins hostile to all mobs
				if (mob instanceof PiglinBrute || mob instanceof Hoglin)
				{
					setHostileToAllBefriendedMobs(mob);
				}
				// Ghasts attack non-undead mobs
				if (mob instanceof Ghast)
				{
					setHostileToAllBefriendedMobs(mob, isUndead.negate());
				}
				// Slimes (including magical) and magma cubes attack all mobs
				if (mob instanceof Slime)
				{
					setHostileToAllBefriendedMobs(mob);
				}
				// Blaze attacks all flying mobs and skeletons (excluding wither)
				if (mob instanceof Blaze)
				{
					NaUtilsAIStatics.setHostileTo(mob, HmagSkeletonGirlEntity.class);
					NaUtilsAIStatics.setHostileTo(mob, HmagStrayGirlEntity.class);
					NaUtilsAIStatics.setHostileTo(mob, HmagHornetEntity.class);
				}
				if (mob instanceof Spider)
				{
					setHostileToAllBefriendedMobs(mob, (living) -> (living.getMobType() != MobType.ARTHROPOD));
				}
				/* Mob hostility end */
				
				/* Existing befriendable mob adjustment */
				if (NFFTamingMapping.contains(mob))
				{
					// Ghastly Seeker in overworld
					if (mob instanceof GhastlySeekerEntity gs)
					{
						WrappedGoal oldMoveGoal = null;
						for (WrappedGoal wg : gs.goalSelector.getAvailableGoals())
						{
							if (wg.getPriority() == 1 /* Priority 1 is only random fly goal */)
							{
								oldMoveGoal = wg;
								break;
							}
						}
						if (oldMoveGoal != null)
						{
							gs.goalSelector.getAvailableGoals().remove(oldMoveGoal);//.getAvailableGoals().remove(oldMoveGoal);
							gs.goalSelector.addGoal(1, new NFFGirlsTamableGhastlySeekerRandomFlyGoal(gs));
						}
					}
					// Kobolds and Imps picking up and being neutral
					if (NFFTamingMapping.getProcess(mob) instanceof ITamingProcessWithProgress<?> processRaw)
					{
						if (mob.getType().is(NFFGirlsTags.NEUTRAL_ON_HIGH_PROGRESS))
						{
							for (WrappedGoal wg: mob.targetSelector.getAvailableGoals())
							{
								// Neutral to players with progress > 0.7
								if (wg.getGoal() instanceof NearestAttackableTargetGoal<?> tg)
								{
									@SuppressWarnings("unchecked")
									ITamingProcessWithProgress<Mob> process = (ITamingProcessWithProgress<Mob>)processRaw;
									NaUtilsAIStatics.addAndTargetingCondition(tg, (le) ->
										!(le instanceof Player player &&
												process.getProgressValue(mob, player.getUUID()) .orElse(0d) > 0.7d));
								}
							}
							// Now it's added in NFFGirlsItemDroppingTamingProcess#tamableInit
							/*mob.goalSelector.addGoal(2, new NFFGirlsTamableWatchHandItemGoal(mob));
							mob.goalSelector.addGoal(4, new NFFGirlsTamablePickItemGoal(mob));*/
						}
					}
					// Harpy and Snow Canine
					/*if (mob instanceof HarpyEntity || mob instanceof SnowCanineEntity)
					{
						mob.goalSelector.addGoal(2, new NFFGirlsTamableWatchHandItemGoal(mob));
						mob.goalSelector.addGoal(3, new NFFGirlsTamablePickItemGoal(mob));
					}
					if (mob instanceof RedcapEntity || mob instanceof JackFrostEntity)
					{
						mob.goalSelector.addGoal(3, new NFFGirlsTamableWatchHandItemGoal(mob));
						mob.goalSelector.addGoal(4, new NFFGirlsTamablePickItemGoal(mob));
					}*/
				}
			}
			/** Add ConditionalAttributeModifier */
			/*if (event.getEntity() instanceof HmagMeltyMonsterEntity mm)
			{
				HmagMeltyMonsterEntity.MODIFIER_SELF_SPEED_UP_IN_LAVA.apply(mm);
				HmagMeltyMonsterEntity.MODIFIER_SELF_SPEED_UP_ON_GROUND.apply(mm);
			}
			if (event.getEntity() instanceof Player player)
			{
				HmagMeltyMonsterEntity.MODIFIER_OWNER_SPEED_UP_IN_LAVA.apply(player);
			}*/
		}
	}
	
	/**
	 * Set a monster hostile to all nffgirls befriended mobs
	 */
	public static void setHostileToAllBefriendedMobs(Mob mob, Predicate<LivingEntity> condition)
	{
		NaUtilsAIStatics.setHostileTo(mob, Mob.class, condition.and(m -> m instanceof INFFGirlsTamed));
	}

	public static void setHostileToAllBefriendedMobs(Mob mob)
	{
		setHostileToAllBefriendedMobs(mob, (l) -> true);
	}
	
	protected static boolean shouldPiglinAttack(LivingEntity living)
	{
		return NFFGirlsEntityStatics.isNotWearingGold(living);
	}
	
	
	@SubscribeEvent
	public static void onMobGriefing(EntityMobGriefingEvent event)
	{
		if (event.getEntity() instanceof HmagGhastlySeekerEntity gs)
		{
		}
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onEntityInteract_PriorityHighest(EntityInteract event)
	{
		// Detect missing-owner cases
		if (event.getTarget() instanceof INFFGirlsTamed bm && !event.getEntity().level.isClientSide)
		{
			if (bm.getOwnerUUID() == null)
			{
				if (event.getItemStack().is(NFFGirlsItems.TRANSFERRING_TAG.get()) && !NFFGirlsItems.TRANSFERRING_TAG.get().isWritten(event.getItemStack()))
				{
					LogUtils.getLogger().error("Mob \"" + bm.asMob().getName().getString() + 
						"\" missing owner. This is probably a bug. Please contact the author for help: https://github.com/SodiumZH/Days-with-Monster-Girls/issues");
					bm.setOwner(event.getPlayer());
				}
				else throw new IllegalStateException("Mob \"" + bm.asMob().getName().getString() + 
						"\" missing owner. This is probably a bug. Please contact the author for help: https://github.com/SodiumZH/Days-with-Monster-Girls/issues");
			}
		}
	}
	
	@SubscribeEvent
	public static void onEntityInteract(EntityInteract event)
	{
		if (event.getTarget() instanceof INFFGirlsTamed bm && event.getSide() == LogicalSide.SERVER 
				&& event.getHand() == InteractionHand.MAIN_HAND && !event.getEntityLiving().getItemInHand(InteractionHand.MAIN_HAND).isEmpty()
				&& !(event.getEntityLiving().getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof NFFMobOwnershipTransfererItem))
		{
			// Send msg if trying to interact other people's mob
			if (!event.getEntity().getUUID().equals(bm.getOwnerUUID())) 
			{
				if (bm.getData().getOwnerName() != null) 
				{
					NaUtilsMiscStatics.printToScreen(
							NaUtilsInfoStatics.createTranslatable("info.nffgirls.interact_not_owning", bm.getData().getOwnerName()), event.getPlayer());
				} 
				else 
				{
					NaUtilsMiscStatics.printToScreen(NaUtilsInfoStatics.createTranslatable("info.nffgirls.interact_not_owning_unpresent"), event.getPlayer());
				}
			}			
		}
		if (event.getEntity().getItemInHand(event.getHand()).is(NFFGirlsItems.COMBAT_COMMANDING_WAND.get()))
			event.setCanceled(true);
	}
	
	@SubscribeEvent
	public static void onServerEntityFinalizeTick(ServerEntityTickEvent.PostWorldTick event)
	{
		if (event.getEntity() instanceof Player player)
		{
			player.getCapability(NFFCapRegistry.CAP_BM_PLAYER).ifPresent(c -> {
				//c.getNbt().remove("directly_attacking");
				c.getNbt().remove("magical_gel_ball_no_use");
			});
		}
	}
	
	@SubscribeEvent
	public static void onThunderHit(EntityStruckByLightningEvent event)
	{
	/*	if (event.getEntity().getType() == NFFGirlsEntityTypes.HMAG_JIANGSHI.get())
		{
			((HmagJiangshiEntity)(event.getEntity())).onThunderHit();
			event.setCanceled(true);
			return;
		}*/
	}
	
	@SubscribeEvent
	public static void onBefriended(NFFMobTamedEvent event)
	{
		event.mobBefriended.setCustomName(null);
		if (NFFGirlsConfigs.ValueCache.Misc.REMOVE_HAND_ITEM_ON_TAMING) {
			event.mobBefriended.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
			event.mobBefriended.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
		}
		if (NFFGirlsConfigs.ValueCache.Misc.REMOVE_ARMOR_ON_TAMING) {
			event.mobBefriended.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
			event.mobBefriended.setItemSlot(EquipmentSlot.CHEST, ItemStack.EMPTY);
			event.mobBefriended.setItemSlot(EquipmentSlot.LEGS, ItemStack.EMPTY);
			event.mobBefriended.setItemSlot(EquipmentSlot.FEET, ItemStack.EMPTY);
		}
	}
	
	/*@SubscribeEvent
	public static void onProjectileImpact(ProjectileImpactEvent event)
	{
		if (!event.getProjectile().level.isClientSide)
		{
			if (event.getProjectile() instanceof MagicBulletEntity mb 
					&& mb.getOwner() != null 
					&& mb.getOwner() instanceof NightwalkerEntity ne
					&& mb.getOwner().getClass() == NightwalkerEntity.class)
			{
				boolean didConvert = false;
				if (event.getRayTraceResult().getType() == HitResult.Type.BLOCK
					&& event.getRayTraceResult() instanceof BlockHitResult bhr) {
					didConvert = NaUtilsMathStatics.withinManhattanDistance(bhr.getBlockPos(), 2)
						.map(pos -> Boolean.valueOf(nightwalkerTerracottaUpgrade(event.getProjectile().level, pos)))
						.filter(Boolean::booleanValue).toList().isEmpty();
				}
				else if (event.getRayTraceResult().getType() == HitResult.Type.ENTITY
					&& event.getRayTraceResult() instanceof EntityHitResult ehr)
				{
					didConvert = NaUtilsMathStatics.withinManhattanDistance(ehr.getEntity().getOnPos(), 2)
						.map(pos -> nightwalkerTerracottaUpgrade(event.getProjectile().level, pos))
						.filter(Boolean::booleanValue).toList().isEmpty();
				}
				if (didConvert)
				{
					NaUtilsEntityStatics.sendParticlesToEntity(mb, ParticleTypes.EXPLOSION, 0, 0, 1, 0);
					mb.level.playSound(null, mb, SoundEvents.GENERIC_EXPLODE, mb.getSoundSource(), 2.0f, 0.7f);
				}
			}
		}
	}*/
	
	/*private static boolean nightwalkerTerracottaUpgrade(Level level, BlockPos pos)
	{
		BlockState blockstate = level.getBlockState(pos);
		if (blockstate.getBlock() == null) return false;
		if (blockstate.is(NFFGirlsBlocks.LUMINOUS_TERRACOTTA.get()))
		{
			level.setBlock(pos, NFFGirlsBlocks.ENHANCED_LUMINOUS_TERRACOTTA.get().defaultBlockState(), 1 | 2);
			return true;
		}
		else if (ColoredBlocks.GLAZED_TERRACOTTA_BLOCKS.contains(blockstate.getBlock()))
		{
			level.setBlock(pos, NFFGirlsBlocks.LUMINOUS_TERRACOTTA.get().defaultBlockState(), 1 | 2);
			return true;
		}
		else return false;
	}*/
	
	// SODIUM'S UTILITIES MIXIN EVENTS BELOW //
	
	@SubscribeEvent
	public static void onItemEntityHurt(ItemEntityHurtEvent event)
	{
		if (event.getEntityItem().getItem().getItem() instanceof NFFMobRespawnerItem item)
			event.setCanceled(true);
		if (event.damageSource.getEntity() != null && event.damageSource.getEntity() instanceof INFFGirlsTamed mob)
			event.setCanceled(true);
	}
	
	@SubscribeEvent
	public static void onSweepHurt(LivingEntitySweepHurtEvent event)
	{
		if (event.getEntity() instanceof INFFGirlsTamed) {
			event.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public static void onMeltyMonsterSetFire(NFFGirlsHooks.MeltyMonsterSetFireEvent event)
	{
		if (event.getEntity() instanceof HmagMeltyMonsterEntity mm && !mm.shouldSetFire())
			event.setCanceled(true);
	}
	
	@SubscribeEvent
	public static void onJackFrostCheckMeltingBiome(NFFGirlsHooks.JackFrostCheckMeltingBiomeEvent event)
	{
		if (event.getEntity() instanceof HmagJackFrostEntity jf && jf.isImmuneToHotBiomes())
			event.setCanceled(true);
	}
	
	@SubscribeEvent
	public static void onThrownTridentSetBaseDamage(ThrownTridentSetBaseDamageEvent event)
	{
		if (event.getEntity().getOwner() != null && event.getEntity().getOwner() instanceof INFFGirlsTamed dbm)
		{
			event.setDamage((float) (event.getOriginalDamage() - dbm.asMob().getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue() + dbm.asMob().getAttribute(Attributes.ATTACK_DAMAGE).getValue()));
		}
	}
	
	@SubscribeEvent
	public static void onMobPickUpItem(MobPickUpItemEvent event)
	{
		if (INFFGirlsTamed.isBM(event.getEntity()))
			event.setCanceled(true);
	}

	@SubscribeEvent
	public static void onMobInteract(MobInteractEvent event) {
		INFFGirlsTamed.ifBM(event.getEntity(), tamed -> {
			if (tamed == null) return;
			if (tamed.shouldBypassCommonInteractions()) return;
			Player player = event.player;
			InteractionHand hand = event.hand;
			if (!player.isShiftKeyDown()) {
				if (player.getUUID().equals(tamed.getOwnerUUID())) {
					if (!player.level.isClientSide() && hand == InteractionHand.MAIN_HAND) {
						InteractionResult res = tamed.serversideMainHandInteraction(player, hand);
						if (res.consumesAction()) {
							event.setInteractionResult(InteractionResult.sidedSuccess(player.level.isClientSide()));
						} else if (tamed.tryApplyHealingItems(player.getItemInHand(hand), player) != InteractionResult.PASS) {
						} else if (tamed.isCommandingItem(player.getItemInHand(hand))) {
							tamed.switchAIState();
						} else return;
					}
					event.setInteractionResult(InteractionResult.sidedSuccess(player.level.isClientSide()));
					return;
				}
				return;
			} else {
				if (player.getUUID().equals(tamed.getOwnerUUID())) {
					if (hand == InteractionHand.MAIN_HAND) {
						if (NFFGirlsEntityStatics.isOnEitherHand(player, NFFGirlsItems.COMMANDING_WAND.get())) {
							NFFTamedStatics.openBefriendedInventory(player, tamed);
							event.setInteractionResult(InteractionResult.sidedSuccess(player.level.isClientSide));
							return;
						} else {
							tamed.clientsideMainHandInteraction(player, hand);
						}
					}
					return;
				}
			}
		});
		return;
	}

	@SubscribeEvent
	public static void onEntitySpecificInteraction(EntitySpecificInteractionEvent event) {
		if (event.getPlayer().getItemInHand(event.getHand()).getItem() instanceof CombatCommandingWandItem)
			event.setCanceled(true);
	}

}
