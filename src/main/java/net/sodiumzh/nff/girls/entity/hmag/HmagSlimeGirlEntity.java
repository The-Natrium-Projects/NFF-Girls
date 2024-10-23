package net.sodiumzh.nff.girls.entity.hmag;

import java.util.Arrays;
import java.util.UUID;

import com.github.mechalopa.hmag.world.entity.SlimeGirlEntity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.sodiumzh.nautils.annotation.DontCallManually;
import net.sodiumzh.nautils.math.LinearColor;
import net.sodiumzh.nautils.registries.NaUtilsEntityDataSerializers;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.entity.INFFGirlTamed;
import net.sodiumzh.nff.girls.entity.ai.goal.target.NFFGirlsNearestHostileToOwnerTargetGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.NFFGirlsNearestHostileToSelfTargetGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.NFFGirlsOwnerHurtByTargetGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.NFFGirlsOwnerHurtTargetGoal;
import net.sodiumzh.nff.girls.inventory.NFFGirlsHmagSlimeGirlInventoryMenu;
import net.sodiumzh.nff.girls.item.MagicalGelColorUtils;
import net.sodiumzh.nff.girls.registry.NFFGirlsHealingItems;
import net.sodiumzh.nff.girls.registry.NFFGirlsItems;
import net.sodiumzh.nff.girls.sound.NFFGirlsSoundPresets;
import net.sodiumzh.nff.girls.util.NFFGirlsEntityStatics;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFFollowOwnerGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFLeapAtOwnerGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFLeapAtTargetGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFMeleeAttackGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFWaterAvoidingRandomStrollGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.target.NFFHurtByTargetGoal;
import net.sodiumzh.nautils.entity.ItemApplyingToMobTable;
import net.sodiumzh.nff.services.entity.taming.NFFTamedStatics;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventory;

public class HmagSlimeGirlEntity extends SlimeGirlEntity implements INFFGirlTamed {

	/* Data sync */

	protected static final EntityDataAccessor<LinearColor> DATA_COLOR = SynchedEntityData
			.defineId(HmagSlimeGirlEntity.class, NaUtilsEntityDataSerializers.LINEAR_COLOR.get());
	
	
	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(DATA_COLOR, LinearColor.fromNormalized(0.5d, 0.5d, 0.5d));
	}

	/* Initialization */

	public HmagSlimeGirlEntity(EntityType<? extends HmagSlimeGirlEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		this.xpReward = 0;
		Arrays.fill(this.armorDropChances, 0);
		Arrays.fill(this.handDropChances, 0);
	}

	@Override
	public void onInit(UUID playerUUID, Mob from)
	{
		if (from instanceof SlimeGirlEntity sg)
		{
			this.setColorLinear(MagicalGelColorUtils.getSlimeColor(sg));
		}
	}
	
	/* AI */

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(1, new FloatGoal(this));
		goalSelector.addGoal(2, new SGLeapAtTargetGoal(this));
		goalSelector.addGoal(3, new NFFMeleeAttackGoal(this, 1.0d, true));
		goalSelector.addGoal(4, new SGLeapAtOwnerGoal(this));
		goalSelector.addGoal(5, new NFFFollowOwnerGoal(this, 1.0d, 5.0f, 2.0f, false));
		goalSelector.addGoal(6, new NFFWaterAvoidingRandomStrollGoal(this, 1.0d));
		goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
		goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new NFFGirlsOwnerHurtByTargetGoal(this));
		targetSelector.addGoal(2, new NFFHurtByTargetGoal(this));
		targetSelector.addGoal(3, new NFFGirlsOwnerHurtTargetGoal(this));
		targetSelector.addGoal(5, new NFFGirlsNearestHostileToSelfTargetGoal(this));
		targetSelector.addGoal(6, new NFFGirlsNearestHostileToOwnerTargetGoal(this));
	}
	
	/* Interaction */

	// Map items that can heal the mob and healing values here.
	// Leave it empty if you don't need healing features.
	@Override
	public ItemApplyingToMobTable getHealingItems()
	{
		return NFFGirlsHealingItems.SLIME.get();
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
		return new NFFTamedMobInventory(4, this);
	}

	@Override
	public NFFTamedInventoryMenu makeMenu(int containerId, Inventory playerInventory, Container container) {
		return new NFFGirlsHmagSlimeGirlInventoryMenu(containerId, playerInventory, container, this);
	}

	/* Save and Load */
	
	@Override
	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		nbt.putDouble("sg_color_r", this.getColorLinear().r);
		nbt.putDouble("sg_color_g", this.getColorLinear().g);
		nbt.putDouble("sg_color_b", this.getColorLinear().b);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		NFFTamedStatics.readBefriendedCommonSaveData(this, nbt);
		this.setColorLinear(LinearColor.fromNormalized(nbt.getDouble("sg_color_r"), nbt.getDouble("sg_color_g"), nbt.getDouble("sg_color_b")));
		setInit();
	}

	// Sound
	
	protected SoundEvent getJumpSound()
	{
		return SoundEvents.SLIME_JUMP;
	}
	
	// Color related
		
	public LinearColor getColorLinear()
	{
		return this.getEntityData().get(DATA_COLOR);
	}
	
	public void setColorLinear(LinearColor color)
	{
		this.getEntityData().set(DATA_COLOR, color);
	}
	
	/**
	 * Invoked on staining with magical gel bottle
	 */
	public void stain(LinearColor gelColor)
	{
		this.setColorLinear(LinearColor.lerp(getColorLinear(), gelColor, 0.2d));
	}
	
	/**
	 * This is for renderer ONLY! On server use {@code getColorLinear} instead.
	 */
	@Override
	@OnlyIn(Dist.CLIENT)
	@DontCallManually
	public float[] getColors()
	{
		LinearColor color = getColorLinear();
		float[] res = {(float) color.r, (float) color.g, (float) color.b};
		return res;
	}
	
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
	
	@Override
	public void remove(RemovalReason reason)
	{
	      this.setRemoved(reason);
	      this.invalidateCaps();
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

	protected static class SGLeapAtTargetGoal extends NFFLeapAtTargetGoal
	{
		protected final HmagSlimeGirlEntity sg;
		
		public SGLeapAtTargetGoal(HmagSlimeGirlEntity mob)
		{
			super(mob, 0.25F, 0.3F, 6.0F, 6);
			this.sg = mob;
		}
		
		@Override
		public void onStart()
		{
			super.onStart();
			this.sg.playSound(sg.getJumpSound(), 1.0F, ((this.sg.getRandom().nextFloat() - this.sg.getRandom().nextFloat()) * 0.2F + 1.0F) * 0.8F);
		}
	}
	
	protected static class SGLeapAtOwnerGoal extends NFFLeapAtOwnerGoal
	{
		protected final HmagSlimeGirlEntity sg;
		
		public SGLeapAtOwnerGoal(HmagSlimeGirlEntity mob)
		{
			super(mob, 0.25F, 0.3F, 6.0F, 6);
			this.sg = mob;
		}
		
		@Override
		public void onStart()
		{
			super.onStart();
			this.sg.playSound(sg.getJumpSound(), 1.0F, ((this.sg.getRandom().nextFloat() - this.sg.getRandom().nextFloat()) * 0.2F + 1.0F) * 0.8F);
		}
	}

}
