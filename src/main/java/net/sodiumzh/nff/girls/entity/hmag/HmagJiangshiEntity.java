package net.sodiumzh.nff.girls.entity.hmag;

import java.util.Arrays;
import java.util.UUID;

import com.github.mechalopa.hmag.registry.ModSoundEvents;
import com.github.mechalopa.hmag.world.entity.JiangshiEntity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.sodiumzh.nautils.statics.NaUtilsEntityStatics;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.entity.INFFGirlTamedSunSensitiveMob;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsFollowOwnerGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.NFFGirlsNearestHostileToOwnerTargetGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.NFFGirlsNearestHostileToSelfTargetGoal;
import net.sodiumzh.nff.girls.inventory.NFFGirlsHmagJiangshiInventoryMenu;
import net.sodiumzh.nff.girls.registry.NFFGirlsHealingItems;
import net.sodiumzh.nff.girls.registry.NFFGirlsItems;
import net.sodiumzh.nff.girls.sound.NFFGirlsSoundPresets;
import net.sodiumzh.nff.girls.util.NFFGirlsEntityStatics;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFLeapAtOwnerGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFLeapAtTargetGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFMeleeAttackGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFWaterAvoidingRandomStrollGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.target.NFFHurtByTargetGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.target.NFFOwnerHurtByTargetGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.target.NFFOwnerHurtTargetGoal;
import net.sodiumzh.nautils.entity.ItemApplyingToMobTable;
import net.sodiumzh.nff.services.entity.taming.NFFTamedStatics;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventory;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventoryWithHandItems;

public class HmagJiangshiEntity extends JiangshiEntity implements INFFGirlTamedSunSensitiveMob
{

	/* Initialization */

	public HmagJiangshiEntity(EntityType<? extends HmagJiangshiEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		this.xpReward = 0;
		Arrays.fill(this.armorDropChances, 0);
		Arrays.fill(this.handDropChances, 0);
	}

	@Override
	public void onInit(UUID playerUUID, Mob from)
	{
		if (from instanceof JiangshiEntity js)
		{
			this.setVariant(js.getVariant());
		}
	}
	
	/* AI */

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(2, new HmagJiangshiEntity.LeapAtTargetGoal(this));
		goalSelector.addGoal(3, new NFFMeleeAttackGoal(this, 1.0d, true));
		goalSelector.addGoal(4, new HmagJiangshiEntity.LeapAtOwnerGoal(this));
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
					// Interacted
					return InteractionResult.sidedSuccess(player.level.isClientSide);
				}
				else return InteractionResult.PASS;
				
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
		return new NFFTamedMobInventoryWithHandItems(7, this);
	}
	
	@Override
	public NFFTamedInventoryMenu makeMenu(int containerId, Inventory playerInventory, Container container) {
		return new NFFGirlsHmagJiangshiInventoryMenu(containerId, playerInventory, container, this);
	}

	/* Save and Load */

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
				MapPair.of("0", this.getAdditionalInventory().getItem(3)),
				MapPair.of("1", this.getAdditionalInventory().getItem(4)),
				MapPair.of("2", this.getAdditionalInventory().getItem(5)),
				MapPair.of("3", this.getAdditionalInventory().getItem(6)));
	}

	@Override
	public BaubleHandler getBaubleHandler() {
		return DwmgBaubleHandlers.UNDEAD;
	}*/
	
	// Misc
	
	// TODO: Removing the talisman on its head will make it mad and attack everything
	public boolean isMad()
	{
		return false;
	}
	
	public void onThunderHit()
	{
		NaUtilsEntityStatics.addEffectSafe(this, new MobEffectInstance(MobEffects.DAMAGE_BOOST, 60 * 20, 2));
		NaUtilsEntityStatics.addEffectSafe(this, new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 60 * 20, 2));
		NaUtilsEntityStatics.addEffectSafe(this, new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 60 * 20, 1));
		NaUtilsEntityStatics.addEffectSafe(this, new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 60 * 20));
		this.getLevelHandler().addExp(20);
	}
	
	/*@Override
	public void setupSunImmunityRules() {
		this.getSunImmunity().putOptional("soul_amulet", mob -> ((INFFGirlTamed)mob).hasDwmgBauble("soul_amulet"));
		this.getSunImmunity().putOptional("resis_amulet", mob -> ((INFFGirlTamed)mob).hasDwmgBauble("resistance_amulet"));
	}*/
	
	// Indicates which mod this mob belongs to
	@Override
	public String getModId() {
		return NFFGirls.MOD_ID;
	}
	
	// Sounds
	
	@Override
	protected SoundEvent getAmbientSound()
	{
		return NFFGirlsSoundPresets.generalAmbient(super.getAmbientSound());
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
	}*/

	// ========================= General Settings end ========================= //
	// ======================================================================== //

	
	
	// =============== AI ================== //
	
	protected static class LeapAtTargetGoal extends NFFLeapAtTargetGoal
	{

		protected HmagJiangshiEntity jiangshi;
		
		public LeapAtTargetGoal(HmagJiangshiEntity mob)
		{
			super(mob, 0.4F, 0.2F, 8.0F, 12);
			this.mob = mob;
			jiangshi = (HmagJiangshiEntity)mob;
		}

		@Override
		public boolean checkCanUse()
		{
			return super.canUse() && this.mob.asMob().hasLineOfSight(this.mob.asMob().getTarget());
		}

		@Override
		public void onStart()
		{
			super.onStart();
			this.mob.asMob().playSound(ModSoundEvents.JIANGSHI_JUMP.get(), 0.8F, 1.0F);
		}

		@Override
		public float getMaxAttackDistance()
		{
			return super.getMaxAttackDistance() - 3.0F * ((float)jiangshi.getSpeedBonus() / (float)JiangshiEntity.SPEED_BONUS_MAX);
		}

		@Override
		public double getXZSpeed()
		{
			return super.getXZSpeed() + 0.3D * ((double)jiangshi.getSpeedBonus() / (double)JiangshiEntity.SPEED_BONUS_MAX);
		}
	}
	
	protected static class LeapAtOwnerGoal extends NFFLeapAtOwnerGoal
	{

		protected HmagJiangshiEntity jiangshi;
		
		public LeapAtOwnerGoal(HmagJiangshiEntity mob)
		{
			super(mob, 0.4F, 0.2F, 8.0F, 12);
			this.mob = mob;
			jiangshi = (HmagJiangshiEntity)mob;
		}

		@Override
		public boolean checkCanUse()
		{
			return super.checkCanUse() && this.mob.asMob().getTarget() != null && this.mob.asMob().hasLineOfSight(this.mob.asMob().getTarget());
		}

		@Override
		public void onStart()
		{
			super.onStart();
			this.mob.asMob().playSound(ModSoundEvents.JIANGSHI_JUMP.get(), 0.8F, 1.0F);
		}

		@Override
		public float getMaxAttackDistance()
		{
			return super.getMaxAttackDistance() - 3.0F * ((float)jiangshi.getSpeedBonus() / (float)JiangshiEntity.SPEED_BONUS_MAX);
		}

		@Override
		public double getXZSpeed()
		{
			return super.getXZSpeed() + 0.3D * ((double)jiangshi.getSpeedBonus() / (double)JiangshiEntity.SPEED_BONUS_MAX);
		}
	}
	
}

