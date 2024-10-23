package net.sodiumzh.nff.girls.entity.hmag;

import java.util.Arrays;

import com.github.mechalopa.hmag.world.entity.DrownedGirlEntity;

import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.sodiumzh.nff.girls.entity.INFFGirlTamedSunSensitiveMob;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsFollowOwnerGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsHmagDrownedTridentAttackGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.NFFGirlsNearestHostileToOwnerTargetGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.NFFGirlsNearestHostileToSelfTargetGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.NFFGirlsOwnerHurtByTargetGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.NFFGirlsOwnerHurtTargetGoal;
import net.sodiumzh.nff.girls.inventory.NFFGirlsEquipmentTwoBaublesInventoryMenu;
import net.sodiumzh.nff.girls.registry.NFFGirlsConfigs;
import net.sodiumzh.nff.girls.registry.NFFGirlsEntityTypes;
import net.sodiumzh.nff.girls.registry.NFFGirlsHealingItems;
import net.sodiumzh.nff.girls.registry.NFFGirlsItems;
import net.sodiumzh.nff.girls.sound.NFFGirlsSoundPresets;
import net.sodiumzh.nff.girls.util.NFFGirlsEntityStatics;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFAmphibiousGoals;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFFleeSunGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFRandomStrollGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFRandomSwimGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFRestrictSunGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFZombieAttackGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.target.NFFHurtByTargetGoal;
import net.sodiumzh.nautils.entity.ItemApplyingToMobTable;
import net.sodiumzh.nff.services.entity.taming.INFFTamedAmphibious;
import net.sodiumzh.nff.services.entity.taming.NFFTamedStatics;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventory;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventoryWithEquipment;

public class HmagDrownedGirlEntity extends DrownedGirlEntity implements INFFGirlTamedSunSensitiveMob, INFFTamedAmphibious
{

	/* Initialization */

	public HmagDrownedGirlEntity(EntityType<? extends HmagDrownedGirlEntity> pEntityType, Level pLevel)
	{
		super(pEntityType, pLevel);
		this.xpReward = 0;
		Arrays.fill(this.armorDropChances, 0);
		Arrays.fill(this.handDropChances, 0);

	}
	
	/* AI */

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(1, new NFFAmphibiousGoals.GoToWaterGoal(this, 1.0D));
		goalSelector.addGoal(1, new NFFRestrictSunGoal(this));
		goalSelector.addGoal(2, new NFFFleeSunGoal(this, 1));
		goalSelector.addGoal(3, new NFFGirlsHmagDrownedTridentAttackGoal(this, 1.0D, 40, 10.0F));
		goalSelector.addGoal(3, new NFFZombieAttackGoal(this, 1.0D, false));
		goalSelector.addGoal(4, new NFFGirlsFollowOwnerGoal(this, 1.0d, 5.0f, 2.0f, false).amphibious()
				.avoidSunCondition(NFFGirlsEntityStatics::isSunSensitive));
		goalSelector.addGoal(5, new NFFAmphibiousGoals.GoToBeachGoal(this, 1.0D));
		goalSelector.addGoal(6, new NFFAmphibiousGoals.SwimUpGoal(this, 1.0D, this.level.getSeaLevel()));
		goalSelector.addGoal(7, new NFFRandomStrollGoal(this, 1.0d));
		goalSelector.addGoal(7, new NFFRandomSwimGoal(this, 1.0d, 120));
		goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
		goalSelector.addGoal(9, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new NFFGirlsOwnerHurtByTargetGoal(this));
		targetSelector.addGoal(2, new NFFHurtByTargetGoal(this));
		targetSelector.addGoal(3, new NFFGirlsOwnerHurtTargetGoal(this));
		targetSelector.addGoal(5, new NFFGirlsNearestHostileToSelfTargetGoal(this));
		targetSelector.addGoal(6, new NFFGirlsNearestHostileToOwnerTargetGoal(this));

	}
	
	@Override
	public void tick()
	{
		// This affects Drowned::wantsToSwim(),
		// if searching-for-land is false and it doesn't have a target
		// the drowned cannot swim
		setSearchingForLand(true);
		super.tick();
	}
	
	/* Combat */

	@Override
	public void performRangedAttack(LivingEntity pTarget, float pDistanceFactor) {
		ThrownTrident throwntrident = new ThrownTrident(this.level, this, new ItemStack(Items.TRIDENT));
		double d0 = pTarget.getX() - this.getX();
		double d1 = pTarget.getY(0.3333333333333333D) - throwntrident.getY();
		double d2 = pTarget.getZ() - this.getZ();
		double d3 = Math.sqrt(d0 * d0 + d2 * d2);
		throwntrident.shoot(d0, d1 + d3 * 0.2F, d2, 1.6F, 2.0F);	// Inaccuracy is fixed at hard mode (i.e. 2.0)
		this.playSound(SoundEvents.DROWNED_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
		this.swing(InteractionHand.MAIN_HAND);
		this.level.addFreshEntity(throwntrident);
	}
	
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
					// If this zombie is converted from a husk,
					// it can be converted back by using a sponge to it
					if (NFFGirlsConfigs.ValueCache.Interaction.ALLOW_REVERSE_CONVERSION
							&& player.getItemInHand(hand).is(Items.SPONGE) 
							&& (isFromZombie || NFFGirlsConfigs.ValueCache.Interaction.ALL_DROWNED_GIRLS_CAN_CONVERT_TO_ZOMBIES)) {
						player.getItemInHand(hand).shrink(1);
						this.spawnAtLocation(new ItemStack(Items.WET_SPONGE, 1));
						HmagZombieGirlEntity z = this.convertToZombie();
						z.isFromHusk = this.isFromHusk;
					} 
					else if (this.tryApplyHealingItems(player.getItemInHand(hand)) != InteractionResult.PASS)
					{
						return InteractionResult.sidedSuccess(player.level.isClientSide);
					}
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
		return new NFFTamedMobInventoryWithEquipment(8, this);
	}
	
	@Override
	public NFFTamedInventoryMenu makeMenu(int containerId, Inventory playerInventory, Container container) {
		return new NFFGirlsEquipmentTwoBaublesInventoryMenu(containerId, playerInventory, container, this);
	}
	
	/* Save and Load */

	@Override
	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		nbt.put("isFromHusk", ByteTag.valueOf(isFromHusk));
		nbt.put("isFromZombie", ByteTag.valueOf(isFromZombie));
	}

	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		NFFTamedStatics.readBefriendedCommonSaveData(this, nbt);
		isFromHusk = nbt.getBoolean("isFromHusk") || nbt.getBoolean("is_from_husk");
		isFromZombie = nbt.getBoolean("isFromZombie") || nbt.getBoolean("is_from_zombie");
		setInit();
	}

	/* Convertions */

	public boolean isFromHusk = false;
	public boolean isFromZombie = false;

	public HmagZombieGirlEntity convertToZombie() {
		HmagZombieGirlEntity newMob = (HmagZombieGirlEntity) NFFTamedStatics
				.convertToOtherBefriendedType(this, NFFGirlsEntityTypes.HMAG_ZOMBIE_GIRL.get());
		newMob.isFromHusk = isFromHusk;
		newMob.setInit();
		return newMob;
	}
	
	/* INFFTamedSunSensitiveMob interface */

/*	@Override
	public void setupSunImmunityRules() {
		this.getSunImmunity().putOptional("sunhat", mob -> mob.getMob().getItemBySlot(EquipmentSlot.HEAD).is(NFFGirlsItems.SUNHAT.get()));
		this.getSunImmunity().putOptional("soul_amulet", mob -> ((INFFGirlTamed)mob).hasDwmgBauble("soul_amulet"));
		this.getSunImmunity().putOptional("resis_amulet", mob -> ((INFFGirlTamed)mob).hasDwmgBauble("resistance_amulet"));
	}*/

	@Override
	protected boolean isSunSensitive()
	{
		return !this.isSunImmune();
	}
	
	/* IBaubleEquipable interface */
/*
	@Override
	public HashMap<String, ItemStack> getBaubleSlots() {
		HashMap<String, ItemStack> map = new HashMap<String, ItemStack>();
		map.put("0", this.getAdditionalInventory().getItem(6));
		map.put("1", this.getAdditionalInventory().getItem(7));
		return map;
	}
	@Override
	public BaubleHandler getBaubleHandler() {
		return DwmgBaubleHandlers.DROWNED;
	}	
*/
	/* INFFTamedAmphibious interface */

	@Override
	public WaterBoundPathNavigation getWaterNav() {
		return this.waterNavigation;
	}

	@Override
	public GroundPathNavigation getGroundNav() {
		return this.groundNavigation;
	}

	@Override
	public PathNavigation getAppliedNav()
	{
		return this.navigation;
	}
	
	@Override
	public void switchNav(boolean isWaterNav) {
		this.navigation = isWaterNav ? this.waterNavigation : this.groundNavigation;
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
	
	// ==================================================================== //
	// ========================= General Settings ========================= //
	// Generally these can be copy-pasted to other INFFTamed classes //

	// ------------------ INFFTamed interface end ------------------ //

	// ------------------ Misc ------------------ //
	/*
	@Override
	public String getModId() {
		return NFFGirls.MOD_ID;
	}
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
