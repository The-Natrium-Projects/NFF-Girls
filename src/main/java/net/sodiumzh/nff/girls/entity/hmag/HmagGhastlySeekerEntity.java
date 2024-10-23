package net.sodiumzh.nff.girls.entity.hmag;

import java.util.Arrays;

import com.github.mechalopa.hmag.registry.ModItems;
import com.github.mechalopa.hmag.world.entity.GhastlySeekerEntity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.sodiumzh.nautils.statics.NaUtilsReflectionStatics;
import net.sodiumzh.nff.girls.entity.INFFGirlTamed;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsFlyingFollowOwnerGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsHmagFlyingGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.NFFGirlsNearestHostileToOwnerTargetGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.NFFGirlsNearestHostileToSelfTargetGoal;
import net.sodiumzh.nff.girls.entity.ai.movecontrol.NFFGirlsHmagFlyingMoveControl;
import net.sodiumzh.nff.girls.entity.projectile.NFFGhastFireballEntity;
import net.sodiumzh.nff.girls.eventlisteners.NFFGirlsEntityEventListeners;
import net.sodiumzh.nff.girls.inventory.NFFGirlsGhastlySeekerInventoryMenu;
import net.sodiumzh.nff.girls.registry.NFFGirlsHealingItems;
import net.sodiumzh.nff.girls.registry.NFFGirlsItems;
import net.sodiumzh.nff.girls.sound.NFFGirlsSoundPresets;
import net.sodiumzh.nff.girls.util.NFFGirlsEntityStatics;
import net.sodiumzh.nff.services.entity.ai.goal.NFFGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.INFFFollowOwner;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFFlyingLandGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFFlyingRandomMoveGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.target.NFFHurtByTargetGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.target.NFFOwnerHurtByTargetGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.target.NFFOwnerHurtTargetGoal;
import net.sodiumzh.nautils.entity.ItemApplyingToMobTable;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nff.services.entity.taming.NFFTamedStatics;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventory;

/**
 * NOT IMPLEMENTED YET
 */

public class HmagGhastlySeekerEntity extends GhastlySeekerEntity implements INFFGirlTamed
{
	
	/** Handled in {@link NFFGirlsEntityEventListeners#onLivingSetAttackTarget} */
	public LivingEntity lastTarget = null;
	public int shootCooldown = 70;
	public float fireballBaseExplosionPower = 1f;
	public float fireballBaseHitDamage = 6f;

	/* Initialization */

	public HmagGhastlySeekerEntity(EntityType<? extends HmagGhastlySeekerEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		this.xpReward = 0;
		Arrays.fill(this.armorDropChances, 0);
		Arrays.fill(this.handDropChances, 0);
		this.moveControl = new NFFGirlsHmagFlyingMoveControl(this);
	}

	@Deprecated
	public static Builder createAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 60.0D)
				.add(Attributes.ARMOR, 2.0D)
				.add(Attributes.ATTACK_DAMAGE, 0)
				.add(Attributes.FOLLOW_RANGE, 64.0D);
	}

	public void setExplosionPower(int value)
	{
		NaUtilsReflectionStatics.forceSet(this, GhastlySeekerEntity.class, "explosionPower", value);
	}
	
	/* AI */

	@Override
	protected void registerGoals() {				
		this.goalSelector.addGoal(5, new NFFFlyingLandGoal(this));
		this.goalSelector.addGoal(6, new HmagGhastlySeekerEntity.FollowOwnerGoal(this));
		this.goalSelector.addGoal(7, new FireballAttackGoal(this));
		this.goalSelector.addGoal(8, new NFFFlyingRandomMoveGoal(this, 0.25d, 20, 8, 2));
		this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
		this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
		this.goalSelector.addGoal(11, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new NFFOwnerHurtByTargetGoal(this));
		targetSelector.addGoal(2, new NFFHurtByTargetGoal(this));
		targetSelector.addGoal(3, new NFFOwnerHurtTargetGoal(this));
		targetSelector.addGoal(5, new NFFGirlsNearestHostileToSelfTargetGoal(this));
		targetSelector.addGoal(6, new NFFGirlsNearestHostileToOwnerTargetGoal(this));
	}
		
	/*@Override
	public void aiStep()
	{
		if (!level.isClientSide)
			super.aiStep();
		else super.aiStep();
	}*/
	
	/* Interaction */

	// Map items that can heal the mob and healing values here.
	// Leave it empty if you don't need healing features.
	@Override
	public ItemApplyingToMobTable getHealingItems()
	{
		return NFFGirlsHealingItems.GHAST.get();
	}

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand)
	{
		if (player.getUUID().equals(getOwnerUUID())) {
			// For normal interaction
			if (!player.isShiftKeyDown())
			{
				if (!player.level.isClientSide()) 
				{
					/* Put checks before healing item check */
					/* if (....)
					 {
					 	....
					 }
					else */if (this.tryApplyHealingItems(player.getItemInHand(hand)) != InteractionResult.PASS)
						return InteractionResult.sidedSuccess(player.level.isClientSide);
					// The function above returns PASS when the items are not correct. So when not PASS it should stop here
					else if (hand == InteractionHand.MAIN_HAND
							&& NFFGirlsEntityStatics.isOnEitherHand(player, NFFGirlsItems.COMMANDING_WAND.get()))
					{
						switchAIState();
					}
					else return InteractionResult.PASS;
				}
				// Interacted
				return InteractionResult.sidedSuccess(player.level.isClientSide);
			}
			// For interaction with shift key down
			else
			{	
				if (hand == InteractionHand.MAIN_HAND && NFFGirlsEntityStatics.isOnEitherHand(player, NFFGirlsItems.COMMANDING_WAND.get()))
				{
					NFFTamedStatics.openBefriendedInventory(player, this);
					return InteractionResult.sidedSuccess(player.level.isClientSide);
				}
			}
		} 
		// Always pass when not owning this mob
		return InteractionResult.PASS;
	}
	
	/* Inventory */

	@Override
	public NFFTamedMobInventory createAdditionalInventory() {
		return new NFFTamedMobInventory(5, this);
	}

	@Override
	public NFFTamedInventoryMenu makeMenu(int containerId, Inventory playerInventory, Container container) {
		return new NFFGirlsGhastlySeekerInventoryMenu(containerId, playerInventory, container, this); 
	}

	/* Save and Load */
	
	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		NFFTamedStatics.readBefriendedCommonSaveData(this, nbt);
		// Add other data reading here
		setInit();
	}

	// Sounds
	
	@Override
	protected SoundEvent getAmbientSound()
	{
		return NFFGirlsSoundPresets.ghastAmbient(super.getAmbientSound());
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSource)
	{
		return NFFGirlsSoundPresets.ghastHurt(super.getHurtSound(damageSource));
	}

	@Override
	protected SoundEvent getDeathSound()
	{
		return NFFGirlsSoundPresets.ghastDeath(super.getDeathSound());
	}
	
	// Misc
	
	// Indicates which mod this mob belongs to

	@Deprecated
	public float calculateExplosionPower()
	{
		return calculateFireballDamageScale();
	}
	
	public float calculateFireballDamageScale()
	{
		if (getAdditionalInventory().getItem(4).is(Items.FIRE_CHARGE))			
			return (float) (this.getAttributeValue(Attributes.ATTACK_DAMAGE) / 10f) + 1f;
		else if (getAdditionalInventory().getItem(4).is(ModItems.BLASTING_BOTTLE.get()))
			return (float) (this.getAttributeValue(Attributes.ATTACK_DAMAGE) * 1.5f / 10f) + 1.5f;
		else return 0f;
	}
	
	// ==================================================================== //
	// ========================= General Settings ========================= //
	// Generally these can be copy-pasted to other INFFTamed classes //
/*
	@Override
	public boolean isPersistenceRequired() {
		return true;
	}

	/*@Override
	public boolean isPreventingPlayerRest(Player pPlayer) {
		return false;
	}

	@Override
	protected boolean shouldDespawnInPeaceful() {
		return false;
	}
*/
	// ========================= General Settings end ========================= //
	// ======================================================================== //

	/*** AI Goals ***/
	
	public static class FireballAttackGoal extends NFFGoal implements NFFGirlsHmagFlyingGoal
	{
		private final HmagGhastlySeekerEntity parent;
		public int attackTimer;
		public FireballAttackGoal(HmagGhastlySeekerEntity mob)
		{
			super(mob);
			this.parent = mob;
			this.allowAllStatesExceptWait();
		}

		@Override
		public boolean checkCanUse()
		{
			// It consumes fire charges
			return this.parent.getTarget() != null 
					&& (mob.getAdditionalInventory().getItem(4).is(Items.FIRE_CHARGE) 
							|| mob.getAdditionalInventory().getItem(4).is(ModItems.BLASTING_BOTTLE.get()));
		}

		@Override
		public boolean checkCanContinueToUse()
		{
			return checkCanUse();
		}
		
		@Override
		public void onStart()
		{
			this.attackTimer = 0;
			this.parent.getTarget();
		}

		@Override
		public void onStop()
		{
			NaUtilsReflectionStatics.forceInvoke(parent, GhastlySeekerEntity.class, "setAttackingTime", 
					int.class, -1);
		}

		@Override
		public void onTick()
		{
			LivingEntity target = this.parent.getTarget();
			double d0 = 24.0D;

			if ((target.distanceToSqr(this.parent) < d0 * d0 || this.attackTimer > 10) && this.parent.hasLineOfSight(target))
			{
				
				if (mob.getAdditionalInventory().getItem(4).isEmpty())
					return;
				
				Level world = this.parent.level;
				++this.attackTimer;

				if (this.attackTimer == 10 && !this.parent.isSilent())
				{
					mob.asMob().getLookControl().setLookAt(mob.asMob().getTarget());
					world.levelEvent((Player)null, 1015, this.parent.blockPosition(), 0);
				}

				if (this.attackTimer == 20)
				{
					double speed = 4.0D;
					Vec3 vec3 = this.parent.getViewVector(1.0F);
					//double d2 = target.getX() - (this.parent.getX() + vec3.x * d1);
					//double d3 = target.getY() + target.getEyeHeight() * 0.5D - this.parent.getY(0.5D) + 0.25D;
					//double d4 = target.getZ() - (this.parent.getZ() + vec3.z * d1);
					if (!this.parent.isSilent())
					{
						world.levelEvent((Player)null, 1016, this.parent.blockPosition(), 0);
					}
					
					Vec3 pos = new Vec3(this.parent.getX() + vec3.x * 0.5D, this.parent.getY(0.5D) + 0.25D, this.parent.getZ() + vec3.z * 0.5D);
					Vec3 velocity = target.getBoundingBox().getCenter().subtract(pos).normalize().scale(speed);
					
					
					NFFGhastFireballEntity fireball = new NFFGhastFireballEntity(world, this.parent, velocity.x, velocity.y, velocity.z, this.parent.calculateFireballDamageScale() * this.parent.fireballBaseExplosionPower);					
					fireball.setPos(pos);
					fireball.hitDamage = this.parent.fireballBaseHitDamage * this.parent.calculateFireballDamageScale();
					if (mob.getAdditionalInventory().getItem(4).is(Items.FIRE_CHARGE))
						fireball.breakBlocks = false;
						
					// Check again to prevent firing without ammo
					if (mob.getAdditionalInventory().getItem(4).is(Items.FIRE_CHARGE)
							|| mob.getAdditionalInventory().getItem(4).is(ModItems.BLASTING_BOTTLE.get()))
					{
						mob.asMob().getLookControl().setLookAt(mob.asMob().getTarget());
						world.addFreshEntity(fireball);
						mob.getAdditionalInventory().getItem(4).shrink(1);
					}
					this.attackTimer = -50;
				}
			}
			else if (this.attackTimer > 0)
			{
				--this.attackTimer;
			}
			NaUtilsReflectionStatics.forceInvoke(parent, GhastlySeekerEntity.class, "setAttackingTime", int.class, this.attackTimer < 0 ? -1 : this.attackTimer);
		}
	}

	public static class FollowOwnerGoal extends NFFGirlsFlyingFollowOwnerGoal implements INFFFollowOwner
	{

		public FollowOwnerGoal(INFFTamed mob)
		{
			super(mob);
			speed = 0.25d;
		}
		
		@Override
		public Vec3 teleportOffset()
		{
			return teleportOffsetDefault().add(0, 1, 0);
		}
		
		
		@Override
		public void onTick() {
			if (!mob.isOwnerInDimension())
				return;
			this.teleportToOwner();
			this.moveToOwner(getActualSpeed(), new Vec3(0, 3, 0));		
		}		
	}

}
