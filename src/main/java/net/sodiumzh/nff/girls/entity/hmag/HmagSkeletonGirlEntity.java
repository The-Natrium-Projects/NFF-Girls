package net.sodiumzh.nff.girls.entity.hmag;

import java.util.Arrays;

import com.github.mechalopa.hmag.world.entity.SkeletonGirlEntity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.entity.INFFGirlTamedSunSensitiveMob;
import net.sodiumzh.nff.girls.entity.INFFGirlsBowShootingMob;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsFollowOwnerGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsSkeletonMeleeAttackGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsSkeletonRangedBowAttackGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.NFFGirlsNearestHostileToOwnerTargetGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.NFFGirlsNearestHostileToSelfTargetGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.NFFGirlsOwnerHurtByTargetGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.NFFGirlsOwnerHurtTargetGoal;
import net.sodiumzh.nff.girls.inventory.NFFGirlsSkeletonInventoryMenu;
import net.sodiumzh.nff.girls.registry.NFFGirlsConfigs;
import net.sodiumzh.nff.girls.registry.NFFGirlsEntityTypes;
import net.sodiumzh.nff.girls.registry.NFFGirlsHealingItems;
import net.sodiumzh.nff.girls.registry.NFFGirlsItems;
import net.sodiumzh.nff.girls.sound.NFFGirlsSoundPresets;
import net.sodiumzh.nff.girls.util.NFFGirlsEntityStatics;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFFleeSunGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFRestrictSunGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFWaterAvoidingRandomStrollGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.target.NFFHurtByTargetGoal;
import net.sodiumzh.nautils.entity.ItemApplyingToMobTable;
import net.sodiumzh.nff.services.entity.taming.NFFTamedStatics;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventory;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventoryWithEquipment;

public class HmagSkeletonGirlEntity extends SkeletonGirlEntity implements INFFGirlTamedSunSensitiveMob, INFFGirlsBowShootingMob
{

	
	public HmagSkeletonGirlEntity(EntityType<? extends HmagSkeletonGirlEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		this.xpReward = 0;
		Arrays.fill(this.armorDropChances, 0);
		Arrays.fill(this.handDropChances, 0);
	}

	/* AI */

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(1, new NFFRestrictSunGoal(this));
		goalSelector.addGoal(2, new NFFFleeSunGoal(this, 1));
		goalSelector.addGoal(3, new NFFGirlsSkeletonRangedBowAttackGoal(this, 1.0D, 20, 15.0F));
		goalSelector.addGoal(4, new NFFGirlsSkeletonMeleeAttackGoal(this, 1.2d, true));
		goalSelector.addGoal(5, new NFFGirlsFollowOwnerGoal(this, 1.0d, 5.0f, 2.0f, false)
				.avoidSunCondition(NFFGirlsEntityStatics::isSunSensitive));
		goalSelector.addGoal(6, new NFFWaterAvoidingRandomStrollGoal(this, 1.0d));
		goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
		goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new NFFGirlsOwnerHurtByTargetGoal(this));
		targetSelector.addGoal(2, new NFFHurtByTargetGoal(this));
		targetSelector.addGoal(3, new NFFGirlsOwnerHurtTargetGoal(this));
		targetSelector.addGoal(5, new NFFGirlsNearestHostileToSelfTargetGoal(this));
		targetSelector.addGoal(6, new NFFGirlsNearestHostileToOwnerTargetGoal(this));
		
	}
	
	/* Bow shooting related */
	
	private boolean justShot = false;

	@Override
	public void performRangedAttack(LivingEntity pTarget, float pVelocity) {
		this.shoot(pTarget, pVelocity);
		justShot = true;
	}

	@Override
	public void aiStep() {
		super.aiStep();
		/* Handle combat AI */
		if (!this.level.isClientSide)
		{
			if (justShot)
			{
				this.postShoot();
				justShot = false;
			}
			
			if (this.getTarget() != null) {
				this.checkSwitchingWeapons();
			}
		}
	}
	
	// It's not needed here
	@Override
	public void reassessWeaponGoal() 
	{}
	
	/* Bow shooting end */


	/* Interaction */

	@Override
	public ItemApplyingToMobTable getHealingItems()
	{
		return NFFGirlsHealingItems.UNDEAD.get();
	}
	
	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand)
	{
		if (!player.isShiftKeyDown())
		{
			if (player.getUUID().equals(getOwnerUUID())) {
				if (!player.level.isClientSide() && hand == InteractionHand.MAIN_HAND) 
				{
					if (this.tryApplyHealingItems(player.getItemInHand(hand)) != InteractionResult.PASS)
					{}
					else if (hand == InteractionHand.MAIN_HAND
							&& NFFGirlsEntityStatics.isOnEitherHand(player, NFFGirlsItems.COMMANDING_WAND.get()))
					{
						switchAIState();
					}		
					else return InteractionResult.PASS;
				}
				return InteractionResult.sidedSuccess(player.level.isClientSide);
			}
			return InteractionResult.PASS;
		}
		else
		{
			if (player.getUUID().equals(getOwnerUUID())) {		
				if (hand == InteractionHand.MAIN_HAND && NFFGirlsEntityStatics.isOnEitherHand(player, NFFGirlsItems.COMMANDING_WAND.get()))
				{
					NFFTamedStatics.openBefriendedInventory(player, this);
					return InteractionResult.sidedSuccess(player.level.isClientSide);
				}
			}
			return InteractionResult.PASS;
		}
	}

	/* Inventory */

	@Override
	public NFFTamedMobInventory createAdditionalInventory() {
		return new NFFTamedMobInventoryWithEquipment(9, this);
	}

	@Override
	public NFFTamedInventoryMenu makeMenu(int containerId, Inventory playerInventory, Container container) {
		return new NFFGirlsSkeletonInventoryMenu(containerId, playerInventory, container, this);
	}
	
	/* Save and Load */

	@Override
	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		NFFTamedStatics.addBefriendedCommonSaveData(this, nbt);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		NFFTamedStatics.readBefriendedCommonSaveData(this, nbt);
		setInit();
	}
	
	/* Conversion */
	
	@Override
	public boolean isFreezeConverting()
	{
		return super.isFreezeConverting() && NFFGirlsConfigs.ValueCache.Interaction.ALLOW_VANILLA_CONVERSION;
	}
	
	@Override
	protected void doFreezeConversion() {
		this.convertToStray();
		if (!this.isSilent())
		{
			this.level.levelEvent((Player) null, 1041, this.blockPosition(), 0);
		}
	}	
	
	public void forceFreezeConversion()
	{
		this.doFreezeConversion();
	}
	
	// Called when convert to stray
	protected HmagStrayGirlEntity convertToStray()
	{
		HmagStrayGirlEntity newMob = (HmagStrayGirlEntity)NFFTamedStatics.convertToOtherBefriendedType(this, NFFGirlsEntityTypes.HMAG_STRAY_GIRL.get());
		newMob.isFromSkeleton = true;
		newMob.setInit();
		return newMob;
	}

	/* INFFTamedSunSensitiveMob interface */

	// Implementation is in aiStep()
	/*@Override
	public void setupSunImmunityRules() {
		this.getSunImmunity().putOptional("sunhat", mob -> mob.getMob().getItemBySlot(EquipmentSlot.HEAD).is(NFFGirlsItems.SUNHAT.get()));
		this.getSunImmunity().putOptional("soul_amulet", mob -> ((INFFGirlTamed)mob).hasDwmgBauble("soul_amulet"));
		this.getSunImmunity().putOptional("resis_amulet", mob -> ((INFFGirlTamed)mob).hasDwmgBauble("resistance_amulet"));
	}*/
	
	
	/* IBaubleEquipable interface */
/*
	@Override
	public HashMap<String, ItemStack> getBaubleSlots() {
		HashMap<String, ItemStack> map = new HashMap<String, ItemStack>();
		map.put("0", this.getAdditionalInventory().getItem(6));
		return map;
	}
	@Override
	public BaubleHandler getBaubleHandler() {
		return DwmgBaubleHandlers.UNDEAD;
	}
*/
	// Sounds
	
	@Override
	protected SoundEvent getAmbientSound()
	{
		return NFFGirlsSoundPresets.skeletonAmbient(super.getAmbientSound());
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSource)
	{
		return NFFGirlsSoundPresets.skeletonHurt(super.getHurtSound(damageSource));
	}

	@Override
	protected SoundEvent getDeathSound()
	{
		return NFFGirlsSoundPresets.skeletonDeath(super.getDeathSound());
	}	
	
	// ------------------ Misc ------------------ //
	
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
	
	
	
}
