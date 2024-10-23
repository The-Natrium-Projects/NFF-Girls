package net.sodiumzh.nff.girls.entity.hmag;

import java.util.Arrays;
import java.util.List;

import com.github.mechalopa.hmag.world.entity.NecroticReaperEntity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.sodiumzh.nautils.statics.NaUtilsInfoStatics;
import net.sodiumzh.nautils.statics.NaUtilsMiscStatics;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.entity.INFFGirlTamedSunSensitiveMob;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsFollowOwnerGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.NFFGirlsNearestHostileToOwnerTargetGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.NFFGirlsNearestHostileToSelfTargetGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.NFFGirlsOwnerHurtByTargetGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.NFFGirlsOwnerHurtTargetGoal;
import net.sodiumzh.nff.girls.inventory.NFFGirlsNecroticReaperInventoryMenu;
import net.sodiumzh.nff.girls.registry.NFFGirlsHealingItems;
import net.sodiumzh.nff.girls.registry.NFFGirlsItems;
import net.sodiumzh.nff.girls.sound.NFFGirlsSoundPresets;
import net.sodiumzh.nff.girls.util.NFFGirlsEntityStatics;
import net.sodiumzh.nff.services.entity.ai.NFFTamedMobAIState;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFFleeSunGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFRestrictSunGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFWaterAvoidingRandomStrollGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFZombieAttackGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.target.NFFHurtByTargetGoal;
import net.sodiumzh.nautils.entity.ItemApplyingToMobTable;
import net.sodiumzh.nff.services.entity.taming.NFFTamedStatics;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventory;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventoryWithHandItems;

public class HmagNecroticReaperEntity extends NecroticReaperEntity implements INFFGirlTamedSunSensitiveMob
{

	/* Initialization */

	public HmagNecroticReaperEntity(EntityType<? extends HmagNecroticReaperEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		this.xpReward = 0;
		Arrays.fill(this.armorDropChances, 0);
		Arrays.fill(this.handDropChances, 0);
	}

	/* AI */

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(1, new FloatGoal(this));
		goalSelector.addGoal(1, new NFFRestrictSunGoal(this));
		goalSelector.addGoal(2, new NFFFleeSunGoal(this, 1));
		goalSelector.addGoal(3, new NFFZombieAttackGoal(this, 1.0d, true));
		// To control a NecroticReaper player must have a Necromancer's Hat or a Necromancer's Wand
		goalSelector.addGoal(4, new NFFGirlsFollowOwnerGoal(this, 1.0d, 5.0f, 2.0f, false)
		{
			@Override
			public boolean checkCanUse()
			{
				return super.checkCanUse() && ((HmagNecroticReaperEntity)mob).controllable();
			}
		}
				.avoidSunCondition(NFFGirlsEntityStatics::isSunSensitive));
		goalSelector.addGoal(5, new NFFWaterAvoidingRandomStrollGoal(this, 1.0d)
		{
			@Override
			public boolean checkCanUse()
			{
				return super.checkCanUse() 
						&& !(((HmagNecroticReaperEntity)mob).controllable() && mob.getAIState() == NFFTamedMobAIState.FOLLOW);
			}
		}
		.allowState(NFFTamedMobAIState.FOLLOW));		
		goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
		goalSelector.addGoal(7, new RandomLookAroundGoal(this));

		// Without a Necromancer's Hat, NecroticReaper will not 
		targetSelector.addGoal(1, new NFFGirlsOwnerHurtByTargetGoal(this)
		{
			@Override
			public boolean checkCanUse()
			{
				return super.checkCanUse() && ((HmagNecroticReaperEntity)mob).controllable();
			}
		});
		targetSelector.addGoal(2, new NFFHurtByTargetGoal(this));
		targetSelector.addGoal(3, new NFFGirlsOwnerHurtTargetGoal(this)
		{
			@Override
			public boolean checkCanUse()
			{
				return super.checkCanUse() && ((HmagNecroticReaperEntity)mob).controllable();
			}
		});
		targetSelector.addGoal(5, new NFFGirlsNearestHostileToSelfTargetGoal(this));
		targetSelector.addGoal(6, new NFFGirlsNearestHostileToOwnerTargetGoal(this));
	}

	/* Interaction */

	// Map items that can heal the mob and healing values here.
	// Leave it empty if you don't need healing features.
	@Override
	public ItemApplyingToMobTable getHealingItems()
	{
		return NFFGirlsHealingItems.UNDEAD.get();
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
					if (getOwner() == null)
					{
						throw new RuntimeException("Mob \"" + this.getName().getString() + "\" missing owner.");
					}
					if (this.tryApplyHealingItems(player.getItemInHand(hand)) != InteractionResult.PASS)
						return InteractionResult.sidedSuccess(player.level.isClientSide);
					// The function above returns PASS when the items are not correct. So when not PASS it should stop here
					// Print a notice info when trying to use commanding wand
					else if (hand == InteractionHand.MAIN_HAND
							&& NFFGirlsEntityStatics.isOnEitherHand(player, NFFGirlsItems.COMMANDING_WAND.get()))
					{
						NaUtilsMiscStatics.printToScreen(NaUtilsInfoStatics.createTranslatable("info.nffgirls.necrotic_reaper_using_commanding_wand"), player);
					}
					else if (hand == InteractionHand.MAIN_HAND
							&& NFFGirlsEntityStatics.isOnEitherHand(player, NFFGirlsItems.NECROMANCER_WAND.get()))
					{
						if (this.controllable())
						{
							switchAIState();
						}
						else
						{
							NaUtilsMiscStatics.printToScreen(NaUtilsInfoStatics.createTranslatable("info.nffgirls.necrotic_reaper_not_controllable"), getOwner());
						}
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
				if (player.getUUID().equals(getOwnerUUID())) {		
					if (hand == InteractionHand.MAIN_HAND && NFFGirlsEntityStatics.isOnEitherHand(player, NFFGirlsItems.NECROMANCER_WAND.get()))
					{
						NFFTamedStatics.openBefriendedInventory(player, this);
						return InteractionResult.sidedSuccess(player.level.isClientSide);
					}
				}
			}
		} 
		// Always pass when not owning this mob
		return InteractionResult.PASS;
	}
	
	/* Inventory */

	@Override
	public NFFTamedMobInventory createAdditionalInventory() {
		// TODO Auto-generated method stub
		return new NFFTamedMobInventoryWithHandItems(6, this);
	}
	
	@Override
	public NFFTamedInventoryMenu makeMenu(int containerId, Inventory playerInventory, Container container) {
		return new NFFGirlsNecroticReaperInventoryMenu(containerId, playerInventory, container, this);
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
	// IBaubleEquipable interface
	@Override
	public HashMap<String, ItemStack> getBaubleSlots() {
		HashMap<String, ItemStack> map = new HashMap<String, ItemStack>();
		map.put("0", this.getAdditionalInventory().getItem(2));
		map.put("1", this.getAdditionalInventory().getItem(3));
		map.put("2", this.getAdditionalInventory().getItem(4));
		map.put("3", this.getAdditionalInventory().getItem(5));
		map.put("main_hand", this.getAdditionalInventory().getItem(0));
		return map;
	}

	@Override
	public BaubleHandler getBaubleHandler() {
		return DwmgBaubleHandlers.NECROTIC_REAPER;
	}
	*/
	// INFFTamedSunSensitiveMob interface
	
	/*@Override
	public void setupSunImmunityRules() {
		this.getSunImmunity().putOptional("soul_amulet", mob -> ((INFFGirlTamed)mob).hasDwmgBauble("soul_amulet"));
		this.getSunImmunity().putOptional("resis_amulet", mob -> ((INFFGirlTamed)mob).hasDwmgBauble("resistance_amulet"));
	}*/
	// Misc
	
	// Indicates which mod this mob belongs to
	@Override
	public String getModId() {
		return NFFGirls.MOD_ID;
	}
	
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
	
	// Util
	// If player can control this mob
	public boolean controllable()
	{
		return this.getOwner().getItemBySlot(EquipmentSlot.HEAD).is(NFFGirlsItems.NECROMANCER_HAT.get())
				|| this.getOwner().getItemBySlot(EquipmentSlot.MAINHAND).is(NFFGirlsItems.NECROMANCER_WAND.get())
				|| this.getOwner().getItemBySlot(EquipmentSlot.OFFHAND).is(NFFGirlsItems.NECROMANCER_WAND.get())
				|| this.getFavorabilityHandler().getFavorability() >= 90f;
	}
	
	// Get how many Necrotic Reapers is <8 blocks away from owner 
	public static int countNearby(Player player)
	{
		Vec3 center = player.position();
		AABB area = new AABB(center.x - 8d, center.y - 8d, center.z - 8d, center.x + 8d, center.y + 8d, center.z + 8d);
		List<Entity> entityList = player.level.getEntities(player, area);
		int count = 0;
		for (Entity e: entityList)
		{
			if (e instanceof HmagNecroticReaperEntity nr
					&& nr.getOwnerUUID() != null // On Necrotic Reaper befriended, a nullptr exception occured here
					&& nr.getOwnerUUID().equals(player.getUUID())
					&& nr.distanceToSqr(player) <= 64d)
			{
				count++;
			}
		}
		return count;
	}

}
