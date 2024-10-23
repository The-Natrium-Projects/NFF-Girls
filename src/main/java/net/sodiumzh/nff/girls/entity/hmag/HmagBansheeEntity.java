package net.sodiumzh.nff.girls.entity.hmag;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.annotation.Nullable;

import com.github.mechalopa.hmag.world.entity.BansheeEntity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Container;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.phys.AABB;
import net.sodiumzh.nautils.statics.NaUtilsEntityStatics;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.entity.INFFGirlTamedSunSensitiveMob;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsFlyingFollowOwnerGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsHmagFlyingGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.NFFGirlsNearestHostileToOwnerTargetGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.NFFGirlsNearestHostileToSelfTargetGoal;
import net.sodiumzh.nff.girls.inventory.NFFGirlsHmagBansheeInventoryMenu;
import net.sodiumzh.nff.girls.registry.NFFGirlsHealingItems;
import net.sodiumzh.nff.girls.registry.NFFGirlsItems;
import net.sodiumzh.nff.girls.sound.NFFGirlsSoundPresets;
import net.sodiumzh.nff.girls.util.NFFGirlsEntityStatics;
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
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventoryWithHandItems;

public class HmagBansheeEntity extends BansheeEntity implements INFFGirlTamedSunSensitiveMob
{
	
	/* Initialization */

	public HmagBansheeEntity(EntityType<? extends HmagBansheeEntity> pEntityType, Level pLevel)
	{
		super(pEntityType, pLevel);
		this.xpReward = 0;
		Arrays.fill(this.armorDropChances, 0);
		Arrays.fill(this.handDropChances, 0);
	}

	@Deprecated
	public static Builder createAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 40.0D).add(Attributes.MOVEMENT_SPEED, 0.24D)
				.add(Attributes.ATTACK_DAMAGE, 6.0D).add(Attributes.KNOCKBACK_RESISTANCE, 0.25D)
				.add(Attributes.FOLLOW_RANGE, 24.0D);
	}

	@Override
	public void onInit(UUID playerUUID, Mob from)
	{
		if (from instanceof BansheeEntity b)
		{
			this.setVariant(b.getVariant());
		}
	}
	
	/* AI */

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(5, new NFFFlyingLandGoal(this));
		this.goalSelector.addGoal(4, new NFFGirlsHmagFlyingGoal.ChargeAttackGoal(this, 0.5D, 1.5F, 6));
		// this.goalSelector.addGoal(4, new NFFMeleeAttackGoal(this, 1d, false));
		this.goalSelector.addGoal(6, new NFFGirlsFlyingFollowOwnerGoal(this));
		this.goalSelector.addGoal(8, new NFFFlyingRandomMoveGoal(this).heightLimit(10));
		this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
		this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
		this.goalSelector.addGoal(11, new RandomLookAroundGoal(this));
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
	public ItemApplyingToMobTable getHealingItems() {
		return NFFGirlsHealingItems.UNDEAD.get();
	}

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		if (player.getUUID().equals(getOwnerUUID()))
		{
			// For normal interaction
			if (!player.isShiftKeyDown())
			{
				if (!player.level.isClientSide())
				{
					/* Put checks before healing item check */
					/*
					 * if (....) { .... } else
					 */if (this.tryApplyHealingItems(player.getItemInHand(hand)) != InteractionResult.PASS)
						return InteractionResult.sidedSuccess(player.level.isClientSide);
					// The function above returns PASS when the items are not correct. So when not
					// PASS it should stop here
					else if (hand == InteractionHand.MAIN_HAND
							&& NFFGirlsEntityStatics.isOnEitherHand(player, NFFGirlsItems.COMMANDING_WAND.get()))
					{
						switchAIState();
					}
					// Here it's main hand but no interaction. Return pass to enable off hand
					// interaction.
					else
						return InteractionResult.PASS;
				}
				// Interacted
				return InteractionResult.sidedSuccess(player.level.isClientSide);
			}
			// For interaction with shift key down
			else
			{
				// Open inventory and GUI
				if (hand == InteractionHand.MAIN_HAND
						&& NFFGirlsEntityStatics.isOnEitherHand(player, NFFGirlsItems.COMMANDING_WAND.get()))
				{
					NFFTamedStatics.openBefriendedInventory(player, this);
					return InteractionResult.sidedSuccess(player.level.isClientSide);
				}
			}
		}

		// Always pass when not owning this mob
		return InteractionResult.PASS;
	}

	/** Flower Effects **/

	protected int allyEffectCooldown = 300;
	protected int addEffectTimePoint = new Random().nextInt(allyEffectCooldown);
	protected FlowerBlock lastFlower = null;

	@Nullable
	protected FlowerBlock getFlowerOnOffhand() {
		if (this.getItemBySlot(EquipmentSlot.OFFHAND).isEmpty())
			return null;
		Item item = this.getItemBySlot(EquipmentSlot.OFFHAND).getItem();
		if (item instanceof BlockItem blockitem && blockitem.getBlock() instanceof FlowerBlock flower)
			return flower;
		else
			return null;
	}

	protected void applyEnemyEffect(LivingEntity target) {
		if (!this.level.isClientSide)
		{
			FlowerBlock flower = getFlowerOnOffhand();
			if (flower == null)
				return;
			MobEffect effect = flower.getSuspiciousStewEffect();
			int duration = flower.getEffectDuration();

			// Reverse for undead mob to apply the expected effect
			if (target instanceof Mob mob && mob.getMobType() == MobType.UNDEAD)
			{
				if (effect == MobEffects.HEAL)
					effect = MobEffects.HARM;
				else if (effect == MobEffects.HARM)
					effect = MobEffects.HEAL;
			}

			if (effect.getCategory() != MobEffectCategory.BENEFICIAL)
				NaUtilsEntityStatics.addEffectSafe(target, new MobEffectInstance(effect, duration, 0));
		}
	}

	protected void applyAllyEffect() {
		if (!this.level.isClientSide)
		{
			// Add effect each 15s
			if (this.tickCount % 300 != addEffectTimePoint)
				return;
			FlowerBlock flower = getFlowerOnOffhand();
			if (flower == null)
				return;

			// Block harmful effect first
			if (flower.getSuspiciousStewEffect().getCategory() == MobEffectCategory.HARMFUL
					&& flower.getSuspiciousStewEffect() != MobEffects.HARM)
				return;

			// Applie on owner and owner's other befriended mobs/tamed animals
			List<Entity> entities = this.level.getEntities(this,
					new AABB(this.position().add(-8, -8, -8), this.position().add(8, 8, 8)));
			entities = entities.stream().filter(e ->
			{
				if (e instanceof Player player && player == this.getOwner())
					return true;
				else if (e instanceof INFFTamed bm && bm.getOwner() == this.getOwner())
					return true;
				else if (e instanceof TamableAnimal ta && ta.getOwner() == this.getOwner())
					return true;
				else
					return false;
			}).toList();

			for (Entity entity : entities)
			{
				MobEffect effect = flower.getSuspiciousStewEffect();
				int duration = flower.getEffectDuration();
				// Reverse for undead mob to apply the expected effect
				if (entity instanceof Mob mob && mob.getMobType() == MobType.UNDEAD)
				{
					if (effect == MobEffects.HEAL)
						effect = MobEffects.HARM;
					else if (effect == MobEffects.HARM)
						effect = MobEffects.HEAL;
				}
				if (effect.getCategory() != MobEffectCategory.HARMFUL)
					NaUtilsEntityStatics.addEffectSafe((LivingEntity) entity, new MobEffectInstance(effect, duration, 0));
			}
		}
	}

	public void removeDefaultEffects(LivingEntity target) {
		int time = this.level.getDifficulty() == Difficulty.NORMAL ? 7 * 20
				: (this.level.getDifficulty() == Difficulty.HARD ? 15 * 20 : 0);
		if (target.getEffect(MobEffects.HUNGER) == null || target.getEffect(MobEffects.HUNGER).getDuration() <= time
				&& target.getEffect(MobEffects.HUNGER).getAmplifier() == 0)
			target.removeEffect(MobEffects.HUNGER);
		if (target.getEffect(MobEffects.WEAKNESS) == null || target.getEffect(MobEffects.WEAKNESS).getDuration() <= time
				&& target.getEffect(MobEffects.WEAKNESS).getAmplifier() == 0)
			target.removeEffect(MobEffects.WEAKNESS);
	}

	@Override
	public boolean doHurtTarget(Entity target) {
		if (super.doHurtTarget(target))
		{
			if (target instanceof LivingEntity living)
			{
				removeDefaultEffects(living);
				applyEnemyEffect(living);
			}
			return true;
		}
		return false;
	}

	@Override
	public void aiStep() {
		super.aiStep();
		if (!this.level.isClientSide)
		{
			applyAllyEffect();

			FlowerBlock flower = this.getFlowerOnOffhand();
			if (flower != null && flower != lastFlower)
			{
				addEffectTimePoint = this.random.nextInt(allyEffectCooldown);
				lastFlower = flower;
			}
		}
	}

	/** Inventory **/

	// This enables mob armor and hand items by default.
	// If not needed, use NFFTamedMobInventory class instead.

	@Override
	public NFFTamedMobInventory createAdditionalInventory() {
		return new NFFTamedMobInventoryWithHandItems(5, this);
	}

	@Override
	public NFFTamedInventoryMenu makeMenu(int containerId, Inventory playerInventory, Container container) {
		return new NFFGirlsHmagBansheeInventoryMenu(containerId, playerInventory, container, this);
	}

	/* IBaubleEquipable interface */
/*
	@Override
	public HashMap<String, ItemStack> getBaubleSlots() {
		HashMap<String, ItemStack> map = new HashMap<String, ItemStack>();
		map.put("0", this.getAdditionalInventory().getItem(2));
		map.put("1", this.getAdditionalInventory().getItem(3));
		map.put("2", this.getAdditionalInventory().getItem(4));
		return map;
	}

	@Override
	public BaubleHandler getBaubleHandler() {
		return DwmgBaubleHandlers.UNDEAD;
	}
*/
	/*@Override
	public void setupSunImmunityRules() {
		this.getSunImmunity().putOptional("soul_amulet", mob -> ((INFFGirlTamed)mob).hasDwmgBauble("soul_amulet"));
		this.getSunImmunity().putOptional("resis_amulet", mob -> ((INFFGirlTamed)mob).hasDwmgBauble("resistance_amulet"));
	}*/

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
	protected SoundEvent getAmbientSound() {
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

}
