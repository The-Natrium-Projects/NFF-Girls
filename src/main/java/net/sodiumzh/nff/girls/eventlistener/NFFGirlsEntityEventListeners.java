package net.sodiumzh.nff.girls.eventlistener;

import com.github.mechalopa.hmag.HMaG;
import com.github.mechalopa.hmag.registry.ModEntityTypes;
import com.github.mechalopa.hmag.registry.ModItems;
import com.github.mechalopa.hmag.world.entity.EnderExecutorEntity;
import com.github.mechalopa.hmag.world.entity.GhastlySeekerEntity;
import com.github.mechalopa.hmag.world.entity.NightwalkerEntity;
import com.github.mechalopa.hmag.world.entity.projectile.MagicBulletEntity;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.*;
import net.minecraftforge.event.entity.*;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.effect.NecromancerWitherEffect;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsTamableGhastlySeekerRandomFlyGoal;
import net.sodiumzh.nff.girls.entity.hmag.*;
import net.sodiumzh.nff.girls.entity.projectile.MobileParticleSourceEntity;
import net.sodiumzh.nff.girls.entity.projectile.NecromancerMagicBulletEntity;
import net.sodiumzh.nff.girls.entity.tamingprocess.hmag.HmagCreeperGirlTamingProcess;
import net.sodiumzh.nff.girls.entity.tamingprocess.hmag.HmagJiangshiTamingProcess;
import net.sodiumzh.nff.girls.item.NecromancerArmorItem;
import net.sodiumzh.nff.girls.registry.*;
import net.sodiumzh.nff.girls.util.NFFGirlsEntityStatics;
import net.sodiumzh.nff.services.entity.ai.NFFTamedMobAIState;
import net.sodiumzh.nff.services.entity.capability.CNFFTamable;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nff.services.entity.taming.NFFTamedStatics;
import net.sodiumzh.nff.services.entity.taming.NFFTamingMapping;
import net.sodiumzh.nff.services.event.entity.NFFMobTamedEvent;
import net.sodiumzh.nff.services.event.entity.NFFTamedDeathEvent;
import net.sodiumzh.nff.services.event.entity.ai.NFFTamedChangeAiStateEvent;
import net.sodiumzh.nff.services.eventlistener.ServerEntityTickEvent;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventory;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventoryWithEquipment;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventoryWithHandItems;
import net.sodiumzh.nff.services.item.NFFMobOwnershipTransfererItem;
import net.sodiumzh.nff.services.item.NFFMobRespawnerItem;
import net.sodiumzh.nff.services.registry.NFFCapRegistry;
import net.sodiumzh.nfu.block.ColoredBlocks;
import net.sodiumzh.nfu.entity.RepeatableAttributeModifier;
import net.sodiumzh.nfu.entity.taming.ITamingProcessWithProgress;
import net.sodiumzh.nfu.mixin.event.entity.*;
import net.sodiumzh.nfu.util.*;
import org.apache.commons.lang3.mutable.MutableObject;

import java.util.ArrayList;
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

	private static final RepeatableAttributeModifier ENCHANTMENT_LOOTING_LEVEL
		= new RepeatableAttributeModifier(1d, AttributeModifier.Operation.ADDITION);

	@SuppressWarnings("unused")
	@SubscribeEvent
	public static void onLivingSetAttackTarget(LivingChangeTargetEvent event)
	{
		LivingEntity target = event.getNewTarget();		
		LivingEntity lastHurtBy = event.getEntity().getLastHurtByMob();
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
        		mob.getCapability(NFFGirlsCapabilities.CAP_UNDEAD_AFFINITY_HANDLER).ifPresent((l) ->
        		{
        			if (target.hasEffect(NFFGirlsEffects.UNDEAD_AFFINITY.get()) && lastHurtBy != target && !l.getHatred().contains(target.getUUID()))
        			{
        				event.setCanceled(true);
        				return;
        			}
        			// Hatred will be added in priority-lowest event
        		});
        		// Handle CUndeadAffinityHandler end //
		    } 
	        // Handle undead mobs end //

	        // Handle Ghastly Seeker
	        if (mob instanceof HmagGhastlySeekerEntity gs)
	        {
	        	// If last target is still attackable, prevent removing target
	        	if (gs.lastTarget != null
	        		&& gs.lastTarget.isAlive() 
	        		&& gs.lastTarget.distanceToSqr(gs) <= gs.getAttributeValue(Attributes.FOLLOW_RANGE) * gs.getAttributeValue(Attributes.FOLLOW_RANGE)
	        		&& gs.hasLineOfSight(gs.lastTarget))
	        	{
	        		event.setNewTarget(gs.lastTarget);
	        	}
	        	if (gs.getLastHurtByMob() != gs.getTarget() && gs.getAIState() == NFFTamedMobAIState.WAIT)
	        	{
	        		event.setCanceled(true);
	        	}
	        	gs.lastTarget = gs.getTarget();
	        }    
		}
		// Handle befriended mobs //

		// Handle mobs end //
	}
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onLivingSetAttackTarget_PriorityLowest(LivingChangeTargetEvent event)
	{
		if (event.getEntity() instanceof Mob mob)
		{
			// Undead mob add neutral here to prevent compat issues with other mods that can make undead mobs non-hostile
			event.getEntity().getCapability(NFFGirlsCapabilities.CAP_UNDEAD_AFFINITY_HANDLER).ifPresent((l) ->
			{
				if (event.isCanceled())
					return;
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
		List<Entity> tamed = p.level().getEntities(attacker, attacker.getBoundingBox().inflate(16d))
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
					if (process.handleFinalExplosionKillingOtherTamedMob(srcMob, event.getMob().asMob()))
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
					&& !event.getDamageSource().is(DamageTypes.FELL_OUT_OF_WORLD)
					&& !event.getDamageSource().is(DamageTypes.GENERIC_KILL))
					
				bm.getFavorabilityHandler().setFavorability(0);
			else if (bm.asMob().distanceToSqr(bm.getOwner()) < 64d 
					&& bm.asMob().hasLineOfSight(bm.getOwner())
					&& !event.getDamageSource().is(DamageTypes.FELL_OUT_OF_WORLD)
					&& !event.getDamageSource().is(DamageTypes.GENERIC_KILL))
				bm.getFavorabilityHandler().addFavorability(-20);
			// EXP loses by a half on death
			// As respawner construction (in befriendmobs) is after posting NFFTamedDeathEvent, it can be set here
			bm.getLevelHandler().setExp(bm.getLevelHandler().getExp() / 2);
		}
		/* Favorability & Level end */
	}
	
	
	@SuppressWarnings("resource")
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onPlayerAttack_PriorityHighest(AttackEntityEvent event)
	{
		if (!event.getEntity().level().isClientSide)
		{
			/*event.getEntity().getCapability(NFFCapRegistry.CAP_BM_PLAYER).ifPresent(c -> {
				c.getNbt().putUUID("directly_attacking", event.getTarget().getUUID());
			});*/
		}
	}
	
	@SuppressWarnings("resource")
	@SubscribeEvent
	public static void onLivingHurt(LivingHurtEvent event) {

		if (event.isCanceled())
			return;
			
		LivingEntity living = event.getEntity();
		if (!living.level().isClientSide)
		{
			// Cancel necromancer magic bullet normal attack
			if (event.getSource().getDirectEntity() != null && event.getSource().getDirectEntity() instanceof NecromancerMagicBulletEntity)
			{
				event.setCanceled(true);
				return;
			}
			
			// Cancel indirect player attack from owner e.g. sweeping
			if (event.getEntity() instanceof INFFGirlsTamed bm
					&& event.getSource().getMsgId().equals("player")
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
				if (event.getSource().is(DamageTypes.FELL_OUT_OF_WORLD))
				{
					// Ignore damage by /kill
					if (living.getY() < -64.0d)
					{
						// Lift up
						living.setPosRaw(living.getX(), 64.0d, living.getZ());
						// and find a standable block
						NFUEntityStatics.chorusLikeTeleport(living);
						living.level().addParticle(ParticleTypes.PORTAL, living.getRandomX(0.5D),
								living.getRandomY() - 0.25D, living.getRandomZ(0.5D),
								(living.getRandom().nextDouble() - 0.5D) * 2.0D, -living.getRandom().nextDouble(),
								(living.getRandom().nextDouble() - 0.5D) * 2.0D);
						living.removeEffect(NFFGirlsEffects.ENDER_PROTECTION.get());

						// whether player is standing on a solid block
						BlockPos standingOn = new BlockPos(living.blockPosition().getX(),
								living.blockPosition().getY() - 1, living.blockPosition().getZ());
						if (living.level().getBlockState(standingOn).is(Blocks.AIR))
						{
							// failed, add slow falling
							if (living instanceof Player p)
							{
								NFUMiscStatics.printToScreen(
										NFUInfoStatics.createTranslatable("info.nffgirls.ender_protection_lift_teleport_failed")
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
								NFUMiscStatics.printToScreen(/*""*/
										NFUInfoStatics.createTranslatable("info.nffgirls.ender_protection_lift"), p);
							}
						}
					}
				}
				else if (!event.getSource().is(DamageTypes.IN_FIRE)
						&& !event.getSource().is(DamageTypes.STARVE))
				{
					NFUParticleStatics.sendParticlesToEntity(living, ParticleTypes.PORTAL, 0, living.getBbHeight()/2, 0, 0.5, living.getBbHeight()/2, 0.5, 2, 1);
					/*living.level.addParticle(ParticleTypes.PORTAL, 
							living.getRandomX(0.5D), 
							living.getRandomY() - 0.25D,
							living.getRandomZ(0.5D), 
							(living.getRandom().nextDouble() - 0.5D) * 2.0D,
							-living.getRandom().nextDouble(), 
							(living.getRandom().nextDouble() - 0.5D) * 2.0D);*/
					NFUEntityStatics.chorusLikeTeleport(living);
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
					if (event.getSource().is(DamageTypeTags.DAMAGES_HELMET))
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
			if (event.getSource().getEntity() != null && !NFFGirlsConfigs.ValueCache.Combat.ENABLE_FRIENDLY_DAMAGE)
			{
				if (INFFGirlsTamed.isBMAnd(event.getSource().getEntity(), tamed ->
					Optional.ofNullable(tamed.getOwnerInDimension()).map(owner -> owner == event.getEntity()).orElse(false)))
				{
					event.setCanceled(true);
					return;
				}
				if (INFFGirlsTamed.isBMAnd(event.getEntity(), tamed ->
					Optional.ofNullable(tamed.getOwnerInDimension()).map(owner -> owner == event.getSource().getEntity()).orElse(false)))
				{
					event.setCanceled(true);
					return;
				}
			}
			
			/** Cancel Ghastly Seeker friendly damage */
			
			if (event.getSource().getEntity() != null && event.getSource().getEntity() instanceof HmagGhastlySeekerEntity gs)
			{
				if (NFFGirlsEntityStatics.isAlly(gs, event.getEntity()))
				{
					event.setCanceled(true);
					return;
				}
			}
			
			/** Cancel projectile friendly damage */
			if (event.getSource().getEntity() != null && event.getSource().getEntity() instanceof INFFGirlsTamed bm && event.getSource().getDirectEntity() instanceof Projectile)
			{
				if (!NFFGirlsConfigs.ValueCache.Combat.ENABLE_PROJECTILE_FRIENDLY_DAMAGE && NFFGirlsEntityStatics.isAlly(bm, event.getEntity()))
				{
					event.setCanceled(true);
					return;
				}
			}

			// Handle peach sword
			if (event.getEntity() instanceof Mob mob
					&& mob.getMobType() == MobType.UNDEAD
					&& event.getSource().getEntity() instanceof Player player
					&& player.getItemInHand(InteractionHand.MAIN_HAND).is(NFFGirlsItems.PEACH_WOOD_SWORD.get()))
			{
				// For Jiangshi, processed in befriending handler
				if (mob.getType() == ModEntityTypes.JIANGSHI.get()
						&& NFFTamingMapping.getProcess(mob) instanceof HmagJiangshiTamingProcess proc
						&& proc.onPeachSwordHit(mob, player)) {}
				else {
					NFUEntityStatics.addEffectSafe(mob, MobEffects.HEAL, 1, 1);
					NFUEntityStatics.addEffectSafe(mob, MobEffects.WEAKNESS, 5 * 20, 2);
					NFUEntityStatics.addEffectSafe(mob, MobEffects.MOVEMENT_SLOWDOWN, 5 * 20, 2);
				}
			}
		}
	}
		
	protected static void hurtArmor(Mob mob, DamageSource damageSource, float damage, EquipmentSlot[] slots)
	{
		// Ignore effect of /kill
		if ((damageSource.is(DamageTypes.FELL_OUT_OF_WORLD) || damageSource.is(DamageTypes.GENERIC_KILL)) && damage > 1000)
			return;
		if (damageSource.is(DamageTypeTags.BYPASSES_ARMOR))
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
				if ((!damageSource.is(DamageTypeTags.IS_FIRE) || !itemstack.getItem().isFireResistant())
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
	
	@SuppressWarnings("resource")
	@SubscribeEvent
	public static void onLivingTick(LivingTickEvent event)
	{
		if (!event.getEntity().level().isClientSide)
		{
			NecromancerArmorItem.necromancerArmorUpdate(event.getEntity());
			
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

			         List<Entity> list = bm.asMob().level().getEntities(bm.asMob(), aabb);

			         for(int i = 0; i < list.size(); ++i) {
			            Entity entity = list.get(i);
			            if (!entity.isRemoved()) {
			               bm.touchEntity(entity);
			            }
			         }
			     }
			}
			// Handle necromancer wither effect
			if (event.getEntity().hasEffect(NFFGirlsEffects.NECROMANCER_WITHER.get()))
			{
				// Wither skeletons are immune to this effect
				if (event.getEntity() instanceof WitherSkeleton)
					event.getEntity().removeEffect(NFFGirlsEffects.NECROMANCER_WITHER.get());
				else
				{
					int ampl = event.getEntity().getEffect(NFFGirlsEffects.NECROMANCER_WITHER.get()).getAmplifier();
					if (event.getEntity().tickCount % NecromancerWitherEffect.deltaTickPerDamage(ampl) == 0)
					{
						if (!(event.getEntity() instanceof Player player && (player.isCreative() || player.isSpectator()))
							|| event.getEntity() instanceof WitherSkeleton
							|| !event.getEntity().canBeAffected(new MobEffectInstance(MobEffects.WITHER)))
						{
							event.getEntity().getCombatTracker().recordDamage(event.getEntity().level().damageSources().wither(), 1f);
							float amount = 1f;
							if (event.getEntity().getAbsorptionAmount() > 1f)
							{
								event.getEntity().setAbsorptionAmount(event.getEntity().getAbsorptionAmount() - 1f);
								amount = 0f;
							}
							else if (event.getEntity().getAbsorptionAmount() > 0f)
							{
								amount -= event.getEntity().getAbsorptionAmount();
								event.getEntity().setAbsorptionAmount(0f);
							}
							if (amount > 0f)
							{
								event.getEntity().setHealth(event.getEntity().getHealth() - 1f);
								if (event.getEntity().getHealth() <= 0f)
									event.getEntity().die(event.getEntity().level().damageSources().wither());
							}
						}
					}
				}
			}
			// Update looting level
			INFFGirlsTamed.get(event.getEntity()).ifPresent(tamed -> {
				if (tamed.asMob().getAttributes().hasAttribute(NFFGirlsEntityAttributes.LOOTING_LEVEL.get())) {
					int lootingLevel = 0;
					if (tamed.asMob().getType().is(NFFGirlsTags.USES_FORTUNE_AS_LOOTING)) {
						lootingLevel = Math.max(lootingLevel, tamed.asMob().getItemBySlot(EquipmentSlot.MAINHAND).getEnchantmentLevel(Enchantments.BLOCK_FORTUNE));
					}
					ENCHANTMENT_LOOTING_LEVEL.apply(tamed.asMob(), NFFGirlsEntityAttributes.LOOTING_LEVEL.get(), lootingLevel);
				}
				else {
					NFUDebugStatics.errorOnce(tamed.getClass(), "NFF Girls mob missing looting level attribute.");
				}
			});

			// In Combat Commanding Wand it will manually set target, which may cause the mob to keep attacking
			// after the target dies. Fix it here
			INFFGirlsTamed.get(event.getEntity()).ifPresent(tamed -> {
				if (tamed.asMob().getTarget() != null && !tamed.asMob().getTarget().isAlive())
					tamed.asMob().setTarget(null);
			});

			// Handle persistent healing
			INFFGirlsTamed.get(event.getEntity()).ifPresent(tamed -> {
				if (tamed.asMob().getAttributeValue(NFFGirlsEntityAttributes.PERSISTENT_HEALING.get()) > 1e-12) {
					tamed.asMob().heal((float) (tamed.asMob().getAttributeValue(NFFGirlsEntityAttributes.PERSISTENT_HEALING.get()) / 20f));
				}
				if (tamed.asMob().getAttributeValue(NFFGirlsEntityAttributes.PERSISTENT_RANGED_HEALING.get()) > 1e-12) {
					List<LivingEntity> toSendParticles = new ArrayList<>();
					tamed.asMob().level().getEntities(EntityTypeTest.forClass(LivingEntity.class),
						tamed.asMob().getBoundingBox().inflate(8, 4, 8),
							e -> NFFTamedStatics.isLivingAlliedToBM(tamed, e)
							// Apply to a round area
							&& (e.getX() - tamed.asMob().getX()) * (e.getX() - tamed.asMob().getX()) + (e.getY() - tamed.asMob().getY()) * (e.getY() - tamed.asMob().getY()) < 64d)
						.stream().filter(e -> !e.equals(tamed.asMob()))
						.forEach(e -> {
								float oldHP = e.getHealth();
								e.heal((float) (tamed.asMob().getAttributeValue(NFFGirlsEntityAttributes.PERSISTENT_RANGED_HEALING.get()) / 20d));
								if (e.getHealth() > oldHP)
									toSendParticles.add(e);
						});
					if (tamed.asMob().tickCount % 200 == 0) {
						for (LivingEntity e: toSendParticles) {
							MobileParticleSourceEntity particleSource =
								new MobileParticleSourceEntity(tamed.asMob().level(), e::getEyePosition)
									.setParticleType(ParticleTypes.HAPPY_VILLAGER)
									.particlesPerTick(2)
									.setSpeed(10d);
							particleSource.setPos(tamed.asMob().getEyePosition());
							e.level().addFreshEntity(particleSource);
						}
					}
				}
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
		    	  NFUReflectionStatics.forceInvoke(slime, Slime.class, "m_33637_", 	// Slime#dealDamage()
		    			  LivingEntity.class, event.thisMob.asMob());
		      }
		}
	}
	
	@SubscribeEvent
	public static void onBefriendedSwitchAiState(NFFTamedChangeAiStateEvent event)
	{
		if (INFFGirlsTamed.isBM(event.getMob()) && !event.getMob().asMob().level().isClientSide)
		{
			NFUMiscStatics.printToScreen(NFUInfoStatics.createText("")
					.append(event.getMob().asMob().getName())
					.append(NFUInfoStatics.createText(" "))
					.append(event.getStateAfter().getDisplayInfo()), event.getMob().getOwner());
		}
	}

	@SubscribeEvent
	public static void onNonBefriendedDie(LivingDeathEvent event)
	{
		if (!event.getEntity().level().isClientSide)
		{
			// This function only handle non-befriended
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
	public static void onGetLootingLevel(LootingLevelEvent event)
	{
		if (event.getDamageSource() != null)
			INFFGirlsTamed.get(event.getDamageSource().getEntity()).ifPresent(tamed -> {
				if (tamed.asMob().getAttributes().hasAttribute(NFFGirlsEntityAttributes.LOOTING_LEVEL.get()))
					event.setLootingLevel(event.getLootingLevel() + (int)Math.round(tamed.asMob().getAttribute(NFFGirlsEntityAttributes.LOOTING_LEVEL.get()).getValue()));
			});
	}
	
	
	@SubscribeEvent
	public static void onDropExp(LivingExperienceDropEvent event)
	{
		// When a mob is killed by a befriended mob, it doesn't drop exp orbs, but directly add exp to the mob.
		if (event.getEntity().getLastHurtByMob() != null 
				&& event.getEntity().getLastHurtByMob() instanceof INFFGirlsTamed bm)
		{
			long exp = event.getOriginalExperience();
			exp = Math.round((double)exp * bm.asMob().getAttributeValue(NFFGirlsEntityAttributes.XP_GAIN_RATE.get()));
			exp = handleMending(exp, bm.asMob());
			bm.getLevelHandler().addExp(exp);
			event.setCanceled(true);
		}
	}
	
	
	/** Handle equipment fixing from Mending enchantment for mobs, and return the exp remains
	 * 
	 * @param noUpdateInventory if true, the mob additional inventory will not be updated from equipment, and it should be manually synced.
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
					&& items[i].getEnchantmentLevel(Enchantments.MENDING) > 0)
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
		if (!event.getEntity().level().isClientSide
			&& !event.isCanceled()
			&& event.getSource().getEntity() instanceof LivingEntity source)
		{
			// Handle attributes
			//INFFGirlsTamed.get(source).ifPresent(tamed ->);

			// Handle favorability
			if (event.getEntity() instanceof Mob mob)
			{
				// On player attack a mob attacking the BM
				if (source instanceof Player player
						&& INFFGirlsTamed.get(mob.getTarget()).filter(tamed ->
							tamed.asMob().isAlive() && tamed.getOwner() == player).isPresent())
				{
					INFFGirlsTamed.get(mob.getTarget()).ifPresent(bm ->
						bm.getFavorabilityHandler().addFavorability(event.getAmount() / 50f));
				}
				// On BM attack a mob attacking the player
				if (INFFGirlsTamed.get(source).isPresent()
						&& mob.getTarget() != null
						&& mob.getTarget() instanceof Player player
						&& source.isAlive()
						&& INFFGirlsTamed.get(source).filter(tamed -> tamed.getOwner() == player).isPresent())
				{
					INFFGirlsTamed.get(source).ifPresent(tamed ->
						tamed.getFavorabilityHandler().addFavorability(event.getAmount() / 100f));
				}
				// If owner attacked friendly mob, lose favorability depending on damage; no lost if < 0.5
				if (event.getSource().getEntity() != null
						&& event.getSource().getEntity() instanceof Player player
						&& INFFGirlsTamed.isBMAnd(event.getEntity(), bm -> bm.getOwnerUUID().equals(player.getUUID()))
						&& !event.getSource().is(DamageTypes.FELL_OUT_OF_WORLD)
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
								NFUParticleStatics.sendSmokeParticlesToEntityDefault(event.getEntity());
							else
								NFUParticleStatics.sendAngryParticlesToEntityDefault(event.getEntity());
						});
					}
				}
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void handleAttributesOnFinalizingDamage(LivingDamageEvent event) {
		if (event.isCanceled()) return;
		Optional.ofNullable(event.getSource().getEntity()).flatMap(INFFGirlsTamed::get).ifPresent(tm -> {
			double boostingRate = 1.0d;
			// Handle debuff attachment
			double poisonAspect = tm.asMob().getAttributeValue(NFFGirlsEntityAttributes.POISON_ASPECT.get());
			if (poisonAspect >= 0.5d && !NFFTamedStatics.isLivingAlliedToBM(tm, event.getEntity())) {
				NFUEntityStatics.addEffectSafe(event.getEntity(), MobEffects.POISON, (int)Math.round(100d + poisonAspect * 40d), Math.max(0, (int) (Math.round(poisonAspect - 1) / 3)));
				NFUEntityStatics.addEffectSafe(event.getEntity(), MobEffects.MOVEMENT_SLOWDOWN, (int)Math.round(100d + poisonAspect * 40d), (((int)poisonAspect) + 2) / 2);
			}
			double witherAspect = tm.asMob().getAttributeValue(NFFGirlsEntityAttributes.WITHER_ASPECT.get());
			if (witherAspect >= 0.5d && !NFFTamedStatics.isLivingAlliedToBM(tm, event.getEntity())) {
				NFUEntityStatics.addEffectSafe(event.getEntity(), MobEffects.WITHER, (int) Math.round(100d + witherAspect * 20d), Math.max(0, (int) (Math.round(witherAspect - 1) / 3)));
			}
			// Handle Anti-Type damage boosts
			if (event.getEntity().getMobType().equals(MobType.UNDEAD))
				boostingRate += tm.asMob().getAttributeValue(NFFGirlsEntityAttributes.ANTI_UNDEAD.get());
			if (event.getEntity().getMobType().equals(MobType.ARTHROPOD))
				boostingRate += tm.asMob().getAttributeValue(NFFGirlsEntityAttributes.ANTI_ARTHROPOD.get());
			if (event.getEntity() instanceof Mob mob && mob.isSensitiveToWater())
				boostingRate += tm.asMob().getAttributeValue(NFFGirlsEntityAttributes.WATER_ASPECT.get());
			// Handle critical
			if (tm.asMob().getRandom().nextDouble() < tm.asMob().getAttributeValue(NFFGirlsEntityAttributes.CRITICAL_RATE.get())) {
				NFUParticleStatics.sendParticlesToEntity(event.getEntity(),
					ParticleTypes.CRIT, (double)event.getEntity().getBbHeight() - 0.2D, 0.3D, 10, 1.0);
				event.getEntity().level().playSound(null, event.getEntity().blockPosition(),
					SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.NEUTRAL);
				boostingRate *= 1.5d;
			}
			event.setAmount((float) (event.getAmount() * boostingRate));
		});
	}

	@SuppressWarnings("resource")
	@SubscribeEvent
	public static void onEntityJoinLevel(EntityJoinLevelEvent event)
	{
		if (!event.getLevel().isClientSide)
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
					NFUAIStatics.setHostileTo(mob, HmagZombieGirlEntity.class);
					NFUAIStatics.setHostileTo(mob, HmagHuskGirlEntity.class);
					NFUAIStatics.setHostileTo(mob, HmagDrownedGirlEntity.class);
					NFUAIStatics.setHostileTo(mob, HmagCreeperGirlEntity.class);
				}
				// Zombies (including Zombified Piglins and Zoglins) hostile to skeletons & creepers
				if ((mob instanceof Zombie || mob instanceof Zoglin)
						&& !(EntityType.getKey(mob.getType()).getNamespace().equals(HMaG.MODID)))	// Exclude HMAG mob girls
				{
					//NaUtilsDebugStatics.debugPrintToScreen("Zombie add hostility", player);
					NFUAIStatics.setHostileTo(mob, HmagSkeletonGirlEntity.class);
					NFUAIStatics.setHostileTo(mob, HmagStrayGirlEntity.class);
					NFUAIStatics.setHostileTo(mob, HmagWitherSkeletonGirlEntity.class);
					NFUAIStatics.setHostileTo(mob, HmagCreeperGirlEntity.class);
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
					NFUAIStatics.setHostileTo(mob, HmagSkeletonGirlEntity.class);
					NFUAIStatics.setHostileTo(mob, HmagStrayGirlEntity.class);
					NFUAIStatics.setHostileTo(mob, HmagHornetEntity.class);
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
									NFUAIStatics.addAndTargetingCondition(tg, (le) ->
										!(le instanceof Player player &&
												process.getProgressValue(mob, player.getUUID()) .orElse(0d) > 0.7d));
								}
							}
							// Now it's added in NFFGirlsItemDroppingTamingProcess#tamableInit
							/*mob.goalSelector.addGoal(2, new NFFGirlsTamableWatchHandItemGoal(mob));
							mob.goalSelector.addGoal(4, new NFFGirlsTamablePickItemGoal(mob));*/
						}
					}
					// Jiangshi
					// Handled in HmagJiangshiTamingProcess now
					/*if (mob instanceof JiangshiEntity js)
					{
						// Frozen by talisman
						js.goalSelector.addGoal(1, new FreezeGoal(js, HmagJiangshiTamingProcess::isFrozen));
						// Adjust leap goal
						WrappedGoal oldLeapGoal = null;
						for (WrappedGoal wg : js.goalSelector.getAvailableGoals())
						{
							if (wg.getPriority() == 2)
							{
								oldLeapGoal = wg;
								break;
							}
						}
						if (oldLeapGoal != null)
						{
							js.goalSelector.getAvailableGoals().remove(oldLeapGoal);//.getAvailableGoals().remove(oldMoveGoal);
							js.goalSelector.addGoal(2, new NFFGirlsTamableJiangshiMutableLeapGoal(js));
						}
					}*/
					// Now it's added in NFFGirlsItemDroppingTamingProcess#tamableInit
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
	 * Set a monster hostile to all nffgirls tamed mobs
	 */
	public static void setHostileToAllBefriendedMobs(Mob mob, Predicate<LivingEntity> condition)
	{
		NFUAIStatics.setHostileTo(mob, Mob.class, condition.and(m -> m instanceof INFFGirlsTamed));
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
		if (event.getTarget() instanceof INFFGirlsTamed bm && !event.getEntity().level().isClientSide)
		{
			if (bm.getOwnerUUID() == null)
			{
				if (event.getItemStack().is(NFFGirlsItems.TRANSFERRING_TAG.get()) && !NFFGirlsItems.TRANSFERRING_TAG.get().isWritten(event.getItemStack()))
				{
					LogUtils.getLogger().error("Mob \"" + bm.asMob().getName().getString() + 
						"\" missing owner. This is probably a bug. Please contact the author for help: https://github.com/SodiumZH/Days-with-Monster-Girls/issues");
					bm.setOwner(event.getEntity());
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
				&& event.getHand() == InteractionHand.MAIN_HAND && !event.getEntity().getItemInHand(InteractionHand.MAIN_HAND).isEmpty()
				&& !(event.getEntity().getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof NFFMobOwnershipTransfererItem))
		{
			// Send msg if trying to interact other people's mob
			if (!event.getEntity().getUUID().equals(bm.getOwnerUUID())) 
			{
				if (bm.getData().getOwnerName() != null) {
					NFUInfoStatics.printMessageTranslatable(event.getEntity(), "info.nffgirls.interact_not_owning", bm.getData().getOwnerName());
				} else {
					NFUInfoStatics.printMessageTranslatable(event.getEntity(), "info.nffgirls.interact_not_owning_unpresent");
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
		if (event.getEntity().getType() == NFFGirlsEntityTypes.HMAG_JIANGSHI.get())
		{
			((HmagJiangshiEntity)(event.getEntity())).onThunderHit();
			event.setCanceled(true);
			return;
		}
	}
	
	@SubscribeEvent
	public static void onBefriended(NFFMobTamedEvent event)
	{
		event.mobBefriended.setCustomName(null);
		INFFTamed.get(event.mobBefriended).ifPresent(tamed -> tamed.setAIState(NFFTamedMobAIState.FOLLOW, false));
		if (NFFGirlsConfigs.ValueCache.Misc.REMOVE_HAND_ITEM_ON_TAMING) {
			INFFGirlsTamed.get(event.mobBefriended).ifPresent(tamed -> {
				NFFTamedMobInventory inv = tamed.getAdditionalInventory();
				if (inv instanceof NFFTamedMobInventoryWithHandItems) {
					inv.setItem(0, ItemStack.EMPTY);
					inv.setItem(1, ItemStack.EMPTY);
				} else if (inv instanceof NFFTamedMobInventoryWithEquipment) {
					inv.setItem(4, ItemStack.EMPTY);
					inv.setItem(5, ItemStack.EMPTY);
				}
			});
			event.mobBefriended.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
			event.mobBefriended.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
		}
		if (NFFGirlsConfigs.ValueCache.Misc.REMOVE_ARMOR_ON_TAMING) {
			INFFGirlsTamed.get(event.mobBefriended).ifPresent(tamed -> {
				NFFTamedMobInventory inv = tamed.getAdditionalInventory();
				if (inv instanceof NFFTamedMobInventoryWithEquipment) {
					inv.setItem(0, ItemStack.EMPTY);
					inv.setItem(1, ItemStack.EMPTY);
					inv.setItem(2, ItemStack.EMPTY);
					inv.setItem(3, ItemStack.EMPTY);
				}
			});
			event.mobBefriended.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
			event.mobBefriended.setItemSlot(EquipmentSlot.CHEST, ItemStack.EMPTY);
			event.mobBefriended.setItemSlot(EquipmentSlot.LEGS, ItemStack.EMPTY);
			event.mobBefriended.setItemSlot(EquipmentSlot.FEET, ItemStack.EMPTY);
		}
	}
	
	@SubscribeEvent
	public static void onProjectileImpact(ProjectileImpactEvent event)
	{
		if (!event.getProjectile().level().isClientSide)
		{
			if (event.getProjectile() instanceof MagicBulletEntity mb 
					&& mb.getOwner() != null 
					&& mb.getOwner() instanceof NightwalkerEntity ne
					&& mb.getOwner().getClass() == NightwalkerEntity.class)
			{
				boolean didConvert = false;
				if (event.getRayTraceResult().getType() == HitResult.Type.BLOCK
					&& event.getRayTraceResult() instanceof BlockHitResult bhr) {
					didConvert = !NFUMathStatics.withinManhattanDistance(bhr.getBlockPos(), 2)
						.map(pos -> Boolean.valueOf(nightwalkerTerracottaUpgrade(event.getProjectile().level(), pos)))
						.filter(Boolean::booleanValue).toList().isEmpty();
				}
				else if (event.getRayTraceResult().getType() == HitResult.Type.ENTITY
					&& event.getRayTraceResult() instanceof EntityHitResult ehr)
				{
					didConvert = !NFUMathStatics.withinManhattanDistance(ehr.getEntity().getOnPos(), 2)
						.map(pos -> nightwalkerTerracottaUpgrade(event.getProjectile().level(), pos))
						.filter(Boolean::booleanValue).toList().isEmpty();
				}
				if (didConvert)
				{
					NFUEntityStatics.sendParticlesToEntity(mb, ParticleTypes.EXPLOSION, 0, 0, 1, 0);
					mb.level().playSound(null, mb, SoundEvents.GENERIC_EXPLODE, mb.getSoundSource(), 2.0f, 0.7f);
				}
			}
		}
	}
	
	private static boolean nightwalkerTerracottaUpgrade(Level level, BlockPos pos)
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
	}
	
	// NFU MIXIN EVENTS BELOW //
	
	@SubscribeEvent
	public static void onItemEntityHurt(ItemEntityHurtEvent event)
	{
		if (event.getEntity().getItem().getItem() instanceof NFFMobRespawnerItem item)
			event.setCanceled(true);
		/*if (INFFGirlsTamed.get(event.damageSource.getEntity()).isPresent() || INFFGirlsTamed.get(event.damageSource.getDirectEntity()).isPresent())
			event.setCanceled(true);*/
	}

	@SubscribeEvent
	public static void onItemEntityOutOfWorld(ItemEntityOutOfWorldEvent event) {
		if (event.getEntity().getItem().getItem() instanceof NFFMobRespawnerItem item) {
			Vec3 v = event.getEntity().position();
			event.getEntity().setPos(v.x, event.getEntity().level().getSeaLevel(), v.z);
			event.getEntity().setNoGravity(true);
			event.setCanceled(true);
		}
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
		if (event.getEntity() instanceof HmagJackFrostEntity jf && jf.immuneToHotBiomes.test(jf))
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
		if (event.getInteractionResult().consumesAction()) return;
		INFFGirlsTamed.get(event.getEntity()).ifPresent(tamed -> {
			if (!event.player.getUUID().equals(tamed.getOwnerUUID()))
				return;    // This listener only handles owner interaction
			Player player = event.player;
			InteractionHand hand = event.hand;
			LogicalSide side = tamed.asMob().level().isClientSide() ? LogicalSide.CLIENT : LogicalSide.SERVER;
			InteractionResult res = InteractionResult.PASS;

			if (!tamed.isReservedInteraction(player, hand, side))
				res = tamed.ownerInteraction(player, hand, tamed.asMob().level().isClientSide() ? LogicalSide.CLIENT : LogicalSide.SERVER);
			if (!res.consumesAction() && !tamed.shouldBypassCommonInteractions()) {
				res = tamed.commonInteractions(player, hand, side);
			}
			if (res.consumesAction())
				event.setInteractionResult(InteractionResult.sidedSuccess(event.getEntity().level().isClientSide()));

		});
	}

	// Account NFF Girls mob kill as owner kill
	@SubscribeEvent
	public static void onCheckPlayerKill(LootCheckPlayerKillEvent event) {
		if (event.damageSource != null)
			INFFGirlsTamed.get(event.damageSource.getEntity()).ifPresent(tamed -> {
				event.setResult(Event.Result.ALLOW);
			});
	}

	// Handle health absorption
	@SubscribeEvent
	public static void onDamageTaken(LivingEntityDamageTakenEvent event) {
		INFFGirlsTamed.get(event.getDamageSource().getEntity()).ifPresent(t -> {
			float absorbRate = (float) t.asMob().getAttributeValue(NFFGirlsEntityAttributes.HEALTH_ABSORPTION.get());
			if (absorbRate >= 1e-9f && !NFFTamedStatics.isLivingAlliedToBM(t, event.getEntity()))
				t.asMob().heal(absorbRate * event.getAmount());
		});
	}

}
