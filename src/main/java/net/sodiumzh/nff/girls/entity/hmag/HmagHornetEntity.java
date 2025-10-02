package net.sodiumzh.nff.girls.entity.hmag;

import com.github.mechalopa.hmag.world.entity.HornetEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Container;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.entity.IHoneyCollecting;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsFlyingFollowOwnerGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsHmagFlyingGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.*;
import net.sodiumzh.nff.girls.entity.ai.movecontrol.NFFGirlsHmagFlyingMoveControl;
import net.sodiumzh.nff.girls.inventory.NFFGirlsHandItemsTwoBaublesInventoryMenu;
import net.sodiumzh.nff.girls.registry.NFFGirlsHealingItems;
import net.sodiumzh.nff.girls.sound.NFFGirlsSoundPresets;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFFlyingLandGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFFlyingMoveGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFFlyingRandomMoveGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.target.NFFHurtByTargetGoal;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nff.services.entity.taming.NFFTamedStatics;
import net.sodiumzh.nff.services.entity.taming.NFFTamingMapping;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventory;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventoryWithHandItems;
import net.sodiumzh.nfu.entity.MobApplicableItemTable;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class HmagHornetEntity extends HornetEntity implements INFFGirlsTamed//, IHoneyCollecting
{

	private final int HONEY_COLLECTING_COOLDOWN = 5 * 60 * 20;
	private int currentHoneyCollectingCooldown = HONEY_COLLECTING_COOLDOWN;
	/*private EntityDataAccessor<Integer> DATA_HONEY_LEVEL =
		SynchedEntityData.defineId(HmagHornetEntity.class, EntityDataSerializers.INT);*/

	public HmagHornetEntity(EntityType<? extends HornetEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		this.xpReward = 0;
		Arrays.fill(this.armorDropChances, 0);
		Arrays.fill(this.handDropChances, 0);
		this.moveControl = new NFFGirlsHmagFlyingMoveControl(this);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		//entityData.define(DATA_HONEY_LEVEL, 0);
	}

	/* AI */

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(4, new NFFGirlsHmagFlyingGoal.ChargeAttackGoal(this, 0.5D, 1.5F, 6));
		//this.goalSelector.addGoal(4, new NFFMeleeAttackGoal(this, 1d, false));
		this.goalSelector.addGoal(5, new NFFFlyingLandGoal(this));
		this.goalSelector.addGoal(6, new NFFGirlsFlyingFollowOwnerGoal(this));
		this.goalSelector.addGoal(8, new NFFFlyingRandomMoveGoal(this).heightLimit(7));
		this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
		this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
		this.goalSelector.addGoal(11, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new NFFGirlsOwnerHurtByTargetGoal(this));
		targetSelector.addGoal(2, new NFFHurtByTargetGoal(this));
		targetSelector.addGoal(3, new NFFGirlsOwnerHurtTargetGoal(this));
		targetSelector.addGoal(5, new NFFGirlsNearestHostileToSelfTargetGoal(this));
		targetSelector.addGoal(6, new NFFGirlsNearestHostileToOwnerTargetGoal(this));
		targetSelector.addGoal(7, new NFFGirlsNearestPotentiallyHostileToSelfTargetGoal(this));
		targetSelector.addGoal(8, new NFFGirlsNearestPotentiallyHostileToOwnerTargetGoal(this));
		targetSelector.addGoal(9, new NFFGirlsAttackingStrategyTargetGoal(this));
	}
	
	/* Combat */
	public static final int ADD_POISON_LEVEL_DEFAULT = 1;
	public static final int ADD_POISON_TICKS_DEFAULT = 200;
	public int addPoisonLevel = 1;
	public int addPoisonTicks = 10 * 20;	// 10s, equal to hornet poisoning time in hard mode
	
	@Override
	public boolean doHurtTarget(Entity entity)
	{
		if (!super.doHurtTarget(entity))
			return false;
		// Remove old poison effect and add new one
		if (entity instanceof LivingEntity living)
		{
			MobEffectInstance instance = living.getActiveEffectsMap().get(MobEffects.POISON);
			// The expected duration of poison added in super class
			int superExpectedDuration =	 
					level().getDifficulty() == Difficulty.NORMAL ? 100 : (
					level().getDifficulty() == Difficulty.HARD ? 200 : 0);
			// If the poison is no stronger than the super class given effect, remove it
			if (instance != null && instance.getAmplifier() <= 1 && instance.getDuration() <= superExpectedDuration)
			{
				living.getActiveEffectsMap().remove(MobEffects.POISON);
				instance = null;
			}
			// Add when don't have poison effect, or have lower level than this mob's adding level, or have the same level but with a shorter duration time 
			if (instance == null 
					|| instance.getAmplifier() == addPoisonLevel && instance.getDuration() < addPoisonTicks
					|| instance.getAmplifier() < addPoisonLevel)
			{
				// Add poison based on this mob's properties
				living.removeEffect(MobEffects.POISON);
				living.addEffect(new MobEffectInstance(MobEffects.POISON, addPoisonTicks, addPoisonLevel));
			}
		}
		return true;
	}


/*	@Override
	public int getCurrentHoneyCollectingCooldown() {
		return currentHoneyCollectingCooldown;
	}

	@Override
	public int getOverallHoneyCollectingCooldown() {
		return HONEY_COLLECTING_COOLDOWN;
	}

	public int getHoneyLevel() {
		return this.entityData.get(DATA_HONEY_LEVEL);
	}

	@Override
	public void setHoneyLevel(int val) {
		this.entityData.set(DATA_HONEY_LEVEL, val);
	}
*/
	/* Interaction */

	/*@Override
	public MobApplicableItemTable getHealingItems()
	{
		return NFFGirlsHealingItems.BEE.get();
	}
*/
	/* Inventory */

	@Override
	public NFFTamedMobInventory createAdditionalInventory() {
		return new NFFTamedMobInventoryWithHandItems(4, this);
	}

	@Override
	public NFFTamedInventoryMenu makeMenu(int containerId, Inventory playerInventory, Container container) {
		return new NFFGirlsHandItemsTwoBaublesInventoryMenu(containerId, playerInventory, container, this);
	}

	/* Save and Load */

	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		//NFFTamedStatics.readBefriendedCommonSaveData(this, nbt);
		// Add other data reading here

		setInit();
	}

	@Override
	protected SoundEvent getAmbientSound()
	{
		return NFFGirlsSoundPresets.generalAmbient(super.getAmbientSound());
	}
	
	// ------------------ Misc ------------------ //
	@Override
	@Nonnull
	public Component getTypeName() {
		EntityType<?> typeBefore = NFFTamingMapping.getTypeBefore(this);
		return typeBefore != null ? typeBefore.getDescription() : super.getTypeName();
	}

	@Override
	public boolean shouldSitOnWaiting() {
		return false;
	}

}
