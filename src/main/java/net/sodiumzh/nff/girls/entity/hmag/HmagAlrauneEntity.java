package net.sodiumzh.nff.girls.entity.hmag;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;

import com.github.mechalopa.hmag.world.entity.AlrauneEntity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.entity.INFFGirlTamed;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsFollowOwnerGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.NFFGirlsNearestHostileToOwnerTargetGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.NFFGirlsNearestHostileToSelfTargetGoal;
import net.sodiumzh.nff.girls.entity.projectile.NFFHmagAlrauneSeedEntity;
import net.sodiumzh.nff.girls.inventory.NFFGirlsHmagThreeBaublesInventoryMenu;
import net.sodiumzh.nff.girls.registry.NFFGirlsHealingItems;
import net.sodiumzh.nff.girls.registry.NFFGirlsItems;
import net.sodiumzh.nff.girls.sound.NFFGirlsSoundPresets;
import net.sodiumzh.nff.girls.util.NFFGirlsEntityStatics;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFMeleeAttackGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFRangedAttackGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFShootProjectileGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFWaterAvoidingRandomStrollGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.target.NFFHurtByTargetGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.target.NFFOwnerHurtByTargetGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.target.NFFOwnerHurtTargetGoal;
import net.sodiumzh.nautils.entity.ItemApplyingToMobTable;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nff.services.entity.taming.NFFTamedStatics;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventory;

public class HmagAlrauneEntity extends AlrauneEntity implements INFFGirlTamed {

	/* Initialization */

	public HmagAlrauneEntity(EntityType<? extends HmagAlrauneEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		this.xpReward = 0;
		Arrays.fill(this.armorDropChances, 0);
		Arrays.fill(this.handDropChances, 0);
	}
	
	/* AI */

	protected NFFMeleeAttackGoal meleeAttackGoal;
	
	@Override
	protected void registerGoals() {
		goalSelector.addGoal(1, new FloatGoal(this));
		goalSelector.addGoal(3, meleeAttackGoal = new NFFMeleeAttackGoal(this, 1.2d, true));
		goalSelector.addGoal(4, new NFFRangedAttackGoal(this, 1.0D, 3 * 20, 15.0F).setSkipChance(0.5));
		goalSelector.addGoal(4, new HmagAlrauneEntity.ShootHealingGoal(this, 1.0D, 10 * 20, 15.0F).setSkipChance(0.8));
		goalSelector.addGoal(5, new NFFGirlsFollowOwnerGoal(this, 1.0d, 5.0f, 2.0f, false));
		goalSelector.addGoal(6, new NFFWaterAvoidingRandomStrollGoal(this, 1.0d));
		goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
		goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new NFFOwnerHurtByTargetGoal(this));
		targetSelector.addGoal(2, new NFFHurtByTargetGoal(this));
		targetSelector.addGoal(3, new NFFOwnerHurtTargetGoal(this));
		targetSelector.addGoal(5, new NFFGirlsNearestHostileToSelfTargetGoal(this));
		targetSelector.addGoal(6, new NFFGirlsNearestHostileToOwnerTargetGoal(this));
	}
	
	@Override
	public void aiStep()
	{
		super.aiStep();
		if (!this.level.isClientSide && meleeAttackGoal != null)
		{
			if (this.getTarget() != null && this.hasLineOfSight(this.getTarget()) && this.distanceToSqr(this.getTarget()) >= 9d)
				meleeAttackGoal.block();
			else meleeAttackGoal.unblock();
		}
	}
	
	/* Combat */
	
	@Override
	public void performRangedAttack(LivingEntity target, float distanceFactor)
	{
		this.shootSeed(target, () -> new NFFHmagAlrauneSeedEntity.PoisonSeed(level, this), distanceFactor);
	}
	
	public void shootSeed(LivingEntity target, Supplier<? extends NFFHmagAlrauneSeedEntity> shotSupplier, float distanceFactor)
	{
		int c = this.getRandom().nextInt(3) + 1;

		for (int i = 0; i < c; ++i)
		{
			NFFHmagAlrauneSeedEntity shot = shotSupplier.get();
			double d0 = target.getEyeY() - 1.1F;
			double d1 = target.getX() - this.getX();
			double d2 = d0 - shot.getY();
			double d3 = target.getZ() - this.getZ();
			double d4 = Math.sqrt(d1 * d1 + d3 * d3) * 0.15D;
			shot.shoot(d1, d2 + d4, d3, 1.5F, 10.0F);
			shot.setDamage(4.0F);
			this.level.addFreshEntity(shot);
		}

		this.playSound(SoundEvents.LLAMA_SPIT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
		this.swing(InteractionHand.MAIN_HAND);
	}
	
	/* Interaction */

	// Map items that can heal the mob and healing values here.
	// Leave it empty if you don't need healing features.
	@Override
	public ItemApplyingToMobTable getHealingItems()
	{
		return NFFGirlsHealingItems.PLANT.get();
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
					// Here it's main hand but no interaction. Return pass to enable off hand interaction.
					else return InteractionResult.PASS;
				}
				// Interacted
				return InteractionResult.sidedSuccess(player.level.isClientSide);
			}
			// For interaction with shift key down
			else
			{
				// Open inventory and GUI
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
		return new NFFTamedMobInventory(3, this);
	}

	@Override
	public void updateFromInventory() {
		if (!this.level.isClientSide) {
			// Sync inventory with mob equipments. If it's not NFFTamedMobInventoryWithEquipment, remove it
			//additionalInventory.setMobEquipment(this);
		}
	}

	@Override
	public void setInventoryFromMob()
	{
		if (!this.level.isClientSide) {
			// Sync inventory with mob equipments. If it's not NFFTamedMobInventoryWithEquipment, remove it
			//additionalInventory.getFromMob(this);
		}
		return;
	}

	@Override
	public NFFTamedInventoryMenu makeMenu(int containerId, Inventory playerInventory, Container container) {
		return new NFFGirlsHmagThreeBaublesInventoryMenu(containerId, playerInventory, container, this);
		// You can keep it null, but in this case never call openBefriendedInventory() or it will crash.
	}

	/* Save and Load */
	
	@Override
	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		// Add other data to save here
	}

	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		NFFTamedStatics.readBefriendedCommonSaveData(this, nbt);
		// Add other data reading here
		setInit();
	}
/*
	@Override
	public HashMap<String, ItemStack> getBaubleSlots() {
		return NaUtilsContainerStatics.mapOf(
				MapPair.of("0", this.getAdditionalInventory().getItem(0)),
				MapPair.of("1", this.getAdditionalInventory().getItem(1)),
				MapPair.of("2", this.getAdditionalInventory().getItem(2)));
	}

	@Override
	public BaubleHandler getBaubleHandler() {
		return DwmgBaubleHandlers.GENERAL;
	}
*/
	// Sounds
	
	@Override
	protected SoundEvent getAmbientSound()
	{
		return NFFGirlsSoundPresets.generalAmbient(super.getAmbientSound());
	}
	
	// Misc
	
	// Indicates which mod this mob belongs to
	@Override
	public String getModId() {
		return NFFGirls.MOD_ID;
	}
	
	// ==================================================================== //
	// ========================= General Settings ========================= //
	// Generally these can be copy-pasted to other INFFTamed classes //
/*
	@Override
	public boolean isPersistenceRequired() {
		return true;
	}

	@Override
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

	protected static class ShootHealingGoal extends NFFShootProjectileGoal
	{
		
		public ShootHealingGoal(INFFTamed mob, double pSpeedModifier, int pAttackInterval, float pAttackRadius)
		{
			super(mob, pSpeedModifier, pAttackInterval, pAttackRadius);
		}

		public ShootHealingGoal(INFFTamed mob, double pSpeedModifier, int pAttackIntervalMin,
				int pAttackIntervalMax, float pAttackRadius)
		{
			super(mob, pSpeedModifier, pAttackIntervalMin, pAttackIntervalMax, pAttackRadius);
		}

		@Override
		public boolean checkCanUse()
		{
			if (!super.checkCanUse())
				return false;
			return this.target.getHealth() < this.target.getMaxHealth() / 2;
		}
		
		@Override
		protected void performShooting(LivingEntity target, float velocity) 
		{
			((HmagAlrauneEntity)mob).shootSeed(target, () -> new NFFHmagAlrauneSeedEntity.HealingSeed(mob.asMob().level, mob), velocity);
		}

		@Override
		protected LivingEntity updateTarget() {
			List<LivingEntity> visible = 
					mob.asMob().level.getEntitiesOfClass(LivingEntity.class, mob.asMob().getBoundingBox().inflate(8d))
					.stream().filter((LivingEntity living) -> mob.asMob().hasLineOfSight(living)).toList();
					
			List<LivingEntity> owner = visible.stream().filter((LivingEntity living) -> living.getUUID().equals(mob.getOwnerUUID())).toList();			
			if (!owner.isEmpty())
				return owner.get(0);			
			List<LivingEntity> allies = visible.stream().filter((LivingEntity living) -> NFFHmagAlrauneSeedEntity.HealingSeed.canAffect(mob, living))
					.sorted(Comparator.comparingDouble((LivingEntity living) -> mob.asMob().distanceToSqr(living))).toList();
			if (!allies.isEmpty())
				return allies.get(0);
			else return null;
		}
		
	}
	
	
}
