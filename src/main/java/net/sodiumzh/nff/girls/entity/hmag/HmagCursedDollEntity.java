package net.sodiumzh.nff.girls.entity.hmag;

import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

import com.github.mechalopa.hmag.world.entity.CursedDollEntity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.sodiumzh.nautils.statics.NaUtilsContainerStatics;
import net.sodiumzh.nautils.statics.NaUtilsEntityStatics;
import net.sodiumzh.nautils.statics.NaUtilsParticleStatics;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.entity.INFFGirlTamedSunSensitiveMob;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsFollowOwnerGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.NFFGirlsNearestHostileToOwnerTargetGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.NFFGirlsNearestHostileToSelfTargetGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.NFFGirlsOwnerHurtByTargetGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.NFFGirlsOwnerHurtTargetGoal;
import net.sodiumzh.nff.girls.inventory.NFFGirlsHandItemsSixBaublesInventoryMenu;
import net.sodiumzh.nff.girls.registry.NFFGirlsHealingItems;
import net.sodiumzh.nff.girls.registry.NFFGirlsItems;
import net.sodiumzh.nff.girls.sound.NFFGirlsSoundPresets;
import net.sodiumzh.nff.girls.util.NFFGirlsEntityStatics;
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

public class HmagCursedDollEntity extends CursedDollEntity implements INFFGirlTamedSunSensitiveMob {

	/* Initialization */

	public HmagCursedDollEntity(EntityType<? extends HmagCursedDollEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		this.xpReward = 0;
		Arrays.fill(this.armorDropChances, 0);
		
		Arrays.fill(this.handDropChances, 0);
	}
	@Override
	public void onInit(UUID playerUUID, Mob from)
	{
		if (from instanceof CursedDollEntity c)
		{
			this.setVariant(c.getVariant());
		}
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
	}
	
	@Override
	public void aiStep()
	{
		super.aiStep();
		if (enhancingCooldown > 0)
			enhancingCooldown--;
	}
	
	/* Interaction */

	// Map items that can heal the mob and healing values here.
	// Leave it empty if you don't need healing features.
	@Override
	public ItemApplyingToMobTable getHealingItems()
	{
		return NFFGirlsHealingItems.CLOTH_DOLL.get();

	}

	protected int enhancingCooldown = 0;
	protected static final HashSet<Item> ENHANCING_ITEMS = NaUtilsContainerStatics.setOf(
			Items.ORANGE_WOOL, Items.YELLOW_WOOL, Items.RED_WOOL, Items.PURPLE_WOOL,
			Items.MAGENTA_WOOL, Items.LIGHT_BLUE_WOOL, Items.BLUE_WOOL, Items.PINK_WOOL,
			Items.CYAN_WOOL, Items.LIME_WOOL, Items.GREEN_WOOL);
	
	/**
	 * Some colors of wool can give the mob specific effects.
	 * orange - Fire resistance 30s
	 * yellow - Glowing 30s
	 * red - Strength 30s
	 * purple - Strength II 10s
	 * lime - Regeneration I 30s
	 * green - Regeneration II 10s
	 * light blue - Speed I 30s
	 * blue - Speed II 15s
	 * cyan - Speed III 15s, Weakness I 15s
	 * magenta - Strength III 15s, Slowness I 15s
	 * pink - remove negative effects
	 * @return Whether handled.
	 */
	protected boolean giveEnhancingItems()
	{
		if (level.isClientSide)
			return false;
		ItemStack handItem = getOwner().getMainHandItem();	
		if (ENHANCING_ITEMS.contains(handItem.getItem()))
		{
			if (enhancingCooldown > 0)
			{
				NaUtilsParticleStatics.sendSmokeParticlesToEntityDefault(this);
				return true;
			}
			else
			{
				if (handItem.is(Items.ORANGE_WOOL))
					this.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 30 * 20));
				else if (handItem.is(Items.YELLOW_WOOL))
					NaUtilsEntityStatics.addEffectSafe(this, MobEffects.GLOWING, 30 * 20);
				else if (handItem.is(Items.RED_WOOL))
					NaUtilsEntityStatics.addEffectSafe(this, MobEffects.DAMAGE_BOOST, 30 * 20);
				else if (handItem.is(Items.PURPLE_WOOL))
					NaUtilsEntityStatics.addEffectSafe(this, MobEffects.DAMAGE_BOOST, 10 * 20, 1);
				else if (handItem.is(Items.LIME_WOOL))
					NaUtilsEntityStatics.addEffectSafe(this, MobEffects.REGENERATION, 30 * 20);
				else if (handItem.is(Items.GREEN_WOOL))
					NaUtilsEntityStatics.addEffectSafe(this, MobEffects.REGENERATION, 10 * 20, 1);
				else if (handItem.is(Items.LIGHT_BLUE_WOOL))
					NaUtilsEntityStatics.addEffectSafe(this, MobEffects.MOVEMENT_SPEED, 30 * 20);
				else if (handItem.is(Items.BLUE_WOOL))
					NaUtilsEntityStatics.addEffectSafe(this, MobEffects.MOVEMENT_SPEED, 10 * 20, 1);
				else if (handItem.is(Items.CYAN_WOOL))
				{
					NaUtilsEntityStatics.addEffectSafe(this, MobEffects.MOVEMENT_SPEED, 15 * 20, 2);
					NaUtilsEntityStatics.addEffectSafe(this, MobEffects.WEAKNESS, 15 * 20);
				}
				else if (handItem.is(Items.MAGENTA_WOOL))
				{
					NaUtilsEntityStatics.addEffectSafe(this, MobEffects.DAMAGE_BOOST, 15 * 20, 2);
					NaUtilsEntityStatics.addEffectSafe(this, MobEffects.WEAKNESS, 15 * 20);
				}
				else if (handItem.is(Items.PINK_WOOL))
				{
					HashSet<MobEffect> removal = new HashSet<>();
					for (MobEffect effect: this.getActiveEffectsMap().keySet())
					{
						if (effect.getCategory() == MobEffectCategory.HARMFUL)
							removal.add(effect);
					}
					for (MobEffect effect: removal)
					{
						this.removeEffect(effect);
					}
				}
				else throw new IllegalArgumentException("Missing enhancing wool type.");
				getOwner().getMainHandItem().shrink(1);
				enhancingCooldown = 15 * 20;
				return true;
			}
		}
		else return false;
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
					if (giveEnhancingItems())
						return InteractionResult.sidedSuccess(player.level.isClientSide);
					else if (this.tryApplyHealingItems(player.getItemInHand(hand)) != InteractionResult.PASS)
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
	public NFFTamedMobInventory createAdditionalInventory()
	{
		return new NFFTamedMobInventoryWithHandItems(8, this);
	}
	
	@Override
	public NFFTamedInventoryMenu makeMenu(int containerId, Inventory playerInventory, Container container) {
		return new NFFGirlsHandItemsSixBaublesInventoryMenu(containerId, playerInventory, container, this);
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
		return this.continuousBaubleSlots(2, 8);
	}

	@Override
	public BaubleHandler getBaubleHandler() {
		return DwmgBaubleHandlers.UNDEAD;
	}
	*/
	// INFFTamedSunSensitiveMob interface

	// Misc
	
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
	}
*/
	// ========================= General Settings end ========================= //
	// ======================================================================== //

}
