package net.sodiumzh.nff.girls.entity.hmag;

import com.github.mechalopa.hmag.world.entity.ZombieGirlEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Container;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamedSunSensitiveMob;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsFollowOwnerGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.*;
import net.sodiumzh.nff.girls.inventory.NFFGirlsEquipmentTwoBaublesInventoryMenu;
import net.sodiumzh.nff.girls.registry.NFFGirlsConfigs;
import net.sodiumzh.nff.girls.registry.NFFGirlsEntityTypes;
import net.sodiumzh.nff.girls.sound.NFFGirlsSoundPresets;
import net.sodiumzh.nff.girls.util.NFFGirlsEntityStatics;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFFleeSunGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFRestrictSunGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFWaterAvoidingRandomStrollGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFZombieAttackGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.target.NFFHurtByTargetGoal;
import net.sodiumzh.nff.services.entity.taming.NFFTamedStatics;
import net.sodiumzh.nff.services.entity.taming.NFFTamingMapping;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventory;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventoryWithEquipment;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class HmagZombieGirlEntity extends ZombieGirlEntity implements INFFGirlsTamedSunSensitiveMob {

	/* Initialization */

	public HmagZombieGirlEntity(EntityType<? extends HmagZombieGirlEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		this.xpReward = 0;
		Arrays.fill(this.armorDropChances, 0);
		Arrays.fill(this.handDropChances, 0);

	}

	@Deprecated
	public static Builder createAttributes() {
		return ZombieGirlEntity.createAttributes().add(Attributes.MAX_HEALTH, 30.0D).add(Attributes.MOVEMENT_SPEED, 0.28D).add(Attributes.ATTACK_DAMAGE, 4.0D).add(Attributes.ARMOR, 4.0D);
	}

	/* AI */

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(1, new NFFRestrictSunGoal(this));
		goalSelector.addGoal(2, new NFFFleeSunGoal(this, 1));
		goalSelector.addGoal(3, new NFFZombieAttackGoal(this, 1.0d, true));
		goalSelector.addGoal(4, new NFFGirlsFollowOwnerGoal(this, 1.0d, 5.0f, 2.0f, false)
				.avoidSunCondition(NFFGirlsEntityStatics::isSunSensitive));
		goalSelector.addGoal(5, new NFFWaterAvoidingRandomStrollGoal(this, 1.0d));
		goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
		goalSelector.addGoal(7, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new NFFGirlsOwnerHurtByTargetGoal(this));
		targetSelector.addGoal(2, new NFFHurtByTargetGoal(this));
		targetSelector.addGoal(3, new NFFGirlsOwnerHurtTargetGoal(this));
		targetSelector.addGoal(5, new NFFGirlsNearestHostileToSelfTargetGoal(this));
		targetSelector.addGoal(6, new NFFGirlsNearestHostileToOwnerTargetGoal(this));
		targetSelector.addGoal(7, new NFFGirlsNearestPotentiallyHostileToSelfTargetGoal(this));
		targetSelector.addGoal(8, new NFFGirlsNearestPotentiallyHostileToOwnerTargetGoal(this));
		targetSelector.addGoal(9, new NFFGirlsAttackingStrategyTargetGoal(this));

	}


	@Override
	public boolean enableSunSensitivity() {
		return true;
	}

	/* Combat */
	
	@Override
	public boolean doHurtTarget(Entity target)
	{
		// Occupy the main hand to block the ignition action in super.doHurtTarget
		// See Zombie class
		/*if (this.getMainHandItem().isEmpty())
			this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(NFFItemRegistry.DUMMY_ITEM.get(), 1));*/
		boolean res = super.doHurtTarget(target);
		// Remove dummy item
		/*if (!this.getMainHandItem().isEmpty() && this.getMainHandItem().is(NFFItemRegistry.DUMMY_ITEM.get()))
			this.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);	*/	
		// Overwrite ignition here
		if (this.getMainHandItem().isEmpty() && this.isOnFire() && this.random.nextFloat() < 0.8f)
			target.setSecondsOnFire(10);

		return res;
	}

	/* Interaction */

	@Override
	public InteractionResult serversideMainHandInteraction(Player player, InteractionHand hand) {
		if (NFFGirlsConfigs.ValueCache.Interaction.ALLOW_REVERSE_CONVERSION
				&& player.getItemInHand(hand).is(Items.SPONGE)
				&& (isFromHusk || NFFGirlsConfigs.ValueCache.Interaction.ALL_ZOMBIE_GIRLS_CAN_CONVERT_TO_HUSKS)) {
			player.getItemInHand(hand).shrink(1);
			this.spawnAtLocation(new ItemStack(Items.WET_SPONGE, 1));
			this.convertToHusk();
			return InteractionResult.sidedSuccess(player.level.isClientSide);
		}
		return InteractionResult.PASS;
	}

	/* Inventory */

	@Override
	public NFFTamedMobInventory createAdditionalInventory() {
		return new NFFTamedMobInventoryWithEquipment(8, this);
	}

	@Override
	public NFFTamedInventoryMenu makeMenu(int containerId, Inventory playerInventory, Container container) {
		return new NFFGirlsEquipmentTwoBaublesInventoryMenu(containerId, playerInventory, container, this);
	}

	// Fix an unknown bug that mob spawned from 
	@Override
	protected void populateDefaultEquipmentSlots(DifficultyInstance pDifficulty) {}
	/* Save and Load */
	
	@Override
	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		nbt.putBoolean("isFromHusk", this.isFromHusk);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		//NFFTamedStatics.readBefriendedCommonSaveData(this, nbt);
		this.isFromHusk = nbt.getBoolean("isFromHusk") || nbt.getBoolean("is_from_husk");	// TODO: the latter is legacy, remove after 0.x.30
		
	}

	/* Convertions */
		
	@Override
	protected boolean convertsInWater()
	{
		return NFFGirlsConfigs.ValueCache.Interaction.ALLOW_VANILLA_CONVERSION;
	}
	
	public boolean isFromHusk = false;	
	
	@Override
	protected void doUnderWaterConversion() {
		this.convertToDrowned();
		if (!this.isSilent())
		{
			this.level.levelEvent((Player) null, 1041, this.blockPosition(), 0);
		}
	}	
	
	public void forceUnderWaterConversion()
	{
		this.doUnderWaterConversion();
	}
	
	public HmagHuskGirlEntity convertToHusk()
	{
		HmagHuskGirlEntity newMob = (HmagHuskGirlEntity)NFFTamedStatics.convertToOtherBefriendedType(this, NFFGirlsEntityTypes.HMAG_HUSK_GIRL.get());
		return newMob;
	}
	
	public HmagDrownedGirlEntity convertToDrowned()
	{
		HmagDrownedGirlEntity newMob = (HmagDrownedGirlEntity)NFFTamedStatics.convertToOtherBefriendedType(this, NFFGirlsEntityTypes.HMAG_DROWNED_GIRL.get());
		newMob.isFromHusk = this.isFromHusk;
		newMob.isFromZombie = true;
		;
		return newMob;
	}

	// Sounds
	@Override
	protected SoundEvent getAmbientSound()
	{
		return NFFGirlsSoundPresets.zombieAmbient(super.getAmbientSound());
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSource)
	{
		return NFFGirlsSoundPresets.zombieHurt(super.getHurtSound(damageSource));
	}

	@Override
	protected SoundEvent getDeathSound()
	{
		return NFFGirlsSoundPresets.zombieDeath(super.getDeathSound());
	}

	@Override
	@Nonnull
	public Component getTypeName() {
		EntityType<?> typeBefore = NFFTamingMapping.getTypeBefore(this);
		return typeBefore != null ? typeBefore.getDescription() : super.getTypeName();
	}

}
