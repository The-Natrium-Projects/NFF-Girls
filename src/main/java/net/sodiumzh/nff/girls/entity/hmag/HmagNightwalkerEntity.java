package net.sodiumzh.nff.girls.entity.hmag;

import com.github.mechalopa.hmag.registry.ModEntityTypes;
import com.github.mechalopa.hmag.registry.ModItems;
import com.github.mechalopa.hmag.world.entity.NightwalkerEntity;
import com.github.mechalopa.hmag.world.entity.projectile.MagicBulletEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.network.PlayMessages;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsFollowOwnerGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsRangedAttackGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.*;
import net.sodiumzh.nff.girls.inventory.NFFGirlsHmagNightwalkerInventoryMenu;
import net.sodiumzh.nff.girls.registry.NFFGirlsBlocks;
import net.sodiumzh.nff.girls.registry.NFFGirlsConfigs;
import net.sodiumzh.nff.girls.registry.NFFGirlsHealingItems;
import net.sodiumzh.nff.girls.sound.NFFGirlsSoundPresets;
import net.sodiumzh.nff.girls.util.NFFGirlsEntityStatics;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFWaterAvoidingRandomStrollGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.target.NFFHurtByTargetGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.target.NFFOwnerHurtByTargetGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.target.NFFOwnerHurtTargetGoal;
import net.sodiumzh.nff.services.entity.taming.NFFTamedStatics;
import net.sodiumzh.nff.services.entity.taming.NFFTamingMapping;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventory;
import net.sodiumzh.nfu.block.ColoredBlocks;
import net.sodiumzh.nfu.entity.MobApplicableItemTable;
import net.sodiumzh.nfu.entity.RepeatableAttributeModifier;
import net.sodiumzh.nfu.util.NFUEntityStatics;
import net.sodiumzh.nfu.util.NFUMathStatics;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class HmagNightwalkerEntity extends NightwalkerEntity implements INFFGirlsTamed {


	private static final RepeatableAttributeModifier ARMOR_MODIFIER = new RepeatableAttributeModifier(0.1d, AttributeModifier.Operation.ADDITION, 300);

	/* Initialization */

	public HmagNightwalkerEntity(EntityType<? extends HmagNightwalkerEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		this.xpReward = 0;
		Arrays.fill(this.armorDropChances, 0);
		Arrays.fill(this.handDropChances, 0);
	}
	
	/* Behavior */

	
	protected int getAttackInterval()
	{
		int expected = 60 - (this.getLevelHandler().getExpectedLevel() / 2);
		return Math.max(10, expected);
	}
	
	@Override
	protected void registerGoals() {
		goalSelector.addGoal(4, new NFFGirlsRangedAttackGoal(this, 1.0D, 3 * 20, 15.0F)
				.setAttackIntervalGetter(() -> this.getAttackInterval()).setSkipChance(0.5d));
		goalSelector.addGoal(5, new NFFGirlsFollowOwnerGoal(this, 1.0d, 5.0f, 2.0f, false));
		goalSelector.addGoal(6, new NFFWaterAvoidingRandomStrollGoal(this, 1.0d));
		goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
		goalSelector.addGoal(8, new RandomLookAroundGoal(this));
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
	public void performRangedAttack(LivingEntity target, float distance)
	{
		double d1 = target.getX() - this.getX();
		double d2 = target.getY() + target.getEyeHeight() * 0.5D - this.getY(0.4D);
		double d3 = target.getZ() - this.getZ();
		double d4 = Math.sqrt(d1 * d1 + d3 * d3) * 0.04D;
		BefriendedNightwalkerMagicBallEntity bullet = 
				new BefriendedNightwalkerMagicBallEntity(this.level(), this, d1 + this.getRandom().nextGaussian() * d4, d2, d3 + this.getRandom().nextGaussian() * d4);
		bullet.setPos(bullet.getX(), this.getY(0.4D) + 0.25D, bullet.getZ());
		bullet.setDamage(4.0F + (float)(this.getAttributeValue(Attributes.ATTACK_DAMAGE)));
		bullet.setEffectLevel((byte)1);
		bullet.setVariant(MagicBulletEntity.Variant.byId(3));
		if (this.getAdditionalInventory().getItem(4).is(ModItems.ANCIENT_STONE.get()))
		{
			bullet.setExpandsTransformingRange();
			this.getAdditionalInventory().getItem(4).shrink(1);
			bullet.setDamage(bullet.getDamage() * 1.5f);
		}
		else if (this.getAdditionalInventory().getItem(4).is(Items.CLAY_BALL))
		{
			this.getAdditionalInventory().getItem(4).shrink(1);
			bullet.setDamage(bullet.getDamage() * 1.2f);
		}
		this.level().addFreshEntity(bullet);
		this.playSound(SoundEvents.SHULKER_SHOOT, 2.0F, (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.2F + 1.0F);	
	}
	
	@Override
	public void customServerAiStep()
	{
		super.customServerAiStep();
		// It may be applied > 100 times, so update per 0.5s to reduce cost
		if (this.tickCount % 10 == 2)
			ARMOR_MODIFIER.apply(this, Attributes.ARMOR, Math.min(200, this.getLevelHandler().getExpectedLevel()));
	}
	
	/* Interaction */

	// Map items that can heal the mob and healing values here.
	// Leave it empty if you don't need healing features.
	/*@Override
	public MobApplicableItemTable getHealingItems()
	{
		return NFFGirlsHealingItems.CLAY_DOLL.get();
	}
*/
	/*@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand)
	{
		if (player.getUUID().equals(getOwnerUUID())) {
			// For normal interaction
			if (!player.isShiftKeyDown())
			{
				if (!player.level().isClientSide()) 
				{
					if (this.tryApplyHealingItems(player.getItemInHand(hand)) != InteractionResult.PASS)
						return InteractionResult.sidedSuccess(player.level().isClientSide);
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
				return InteractionResult.sidedSuccess(player.level().isClientSide);
			}
			// For interaction with shift key down
			else
			{
				// Open inventory and GUI
				if (hand == InteractionHand.MAIN_HAND && NFFGirlsEntityStatics.isOnEitherHand(player, NFFGirlsItems.COMMANDING_WAND.get()))
				{
					NFFTamedStatics.openBefriendedInventory(player, this);
					return InteractionResult.sidedSuccess(player.level().isClientSide);
				}
			}
		} 
		// Always pass when not owning this mob
		return InteractionResult.PASS;
	}*/
	
	/* Inventory */

	@Override
	public NFFTamedMobInventory createAdditionalInventory() {
		return new NFFTamedMobInventory(5, this);
	}
	
	@Override
	public NFFTamedInventoryMenu makeMenu(int containerId, Inventory playerInventory, Container container) {
		return new NFFGirlsHmagNightwalkerInventoryMenu(containerId, playerInventory, container, this);
	}

	/* Save and Load */

	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		//NFFTamedStatics.readBefriendedCommonSaveData(this, nbt);
		// Add other data reading here
		setInit();
	}

	// Sounds
	
	@Override
	protected SoundEvent getAmbientSound()
	{
		return NFFGirlsSoundPresets.generalAmbient(super.getAmbientSound());
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource damageSource)
	{
		return super.getHurtSound(damageSource);
	}

	@Override
	protected SoundEvent getDeathSound()
	{
		return super.getDeathSound();
	}

	// Misc
	
	// Indicates which mod this mob belongs to
	@Override
	public String getModId() {
		return NFFGirls.MOD_ID;
	}

	@Override
	@Nonnull
	public Component getTypeName() {
		EntityType<?> typeBefore = NFFTamingMapping.getTypeBefore(this);
		return typeBefore != null ? typeBefore.getDescription() : super.getTypeName();
	}

	protected static class BefriendedNightwalkerMagicBallEntity extends MagicBulletEntity
	{
		
		protected boolean shouldExpandTransformingRange = false;
		
		public BefriendedNightwalkerMagicBallEntity(EntityType<? extends MagicBulletEntity> type, Level level)
		{
			super(type, level);
		}

		public BefriendedNightwalkerMagicBallEntity(Level level, LivingEntity shooter, double accelX, double accelY, double accelZ)
		{
			super(level, shooter, accelX, accelY, accelZ);
		}

		public BefriendedNightwalkerMagicBallEntity(Level level, double x, double y, double z, double accelX, double accelY, double accelZ)
		{
			super(level, x, y, z, accelX, accelY, accelZ);
		}

		public BefriendedNightwalkerMagicBallEntity(PlayMessages.SpawnEntity spawnEntity, Level level)
		{
			this(ModEntityTypes.MAGIC_BULLET.get(), level);
		}
		
		@Override
		public HmagNightwalkerEntity getOwner()
		{
			return (HmagNightwalkerEntity)(super.getOwner());
		}
		
		public void setExpandsTransformingRange()
		{
			shouldExpandTransformingRange = true;
		}
		
		@Override
		public void addAdditionalSaveData(CompoundTag nbt) {
			super.addAdditionalSaveData(nbt);
			nbt.putBoolean("transforms_blocks", shouldExpandTransformingRange);
		}

		@Override
		public void readAdditionalSaveData(CompoundTag nbt) {
			super.readAdditionalSaveData(nbt);
			this.shouldExpandTransformingRange = nbt.getBoolean("transforms_blocks");
		}
		
		@Override
		public void onHitBlock(BlockHitResult result)
		{
			super.onHitBlock(result);
			if (!this.level().isClientSide)
			{
				if (!NFUMathStatics.withinManhattanDistance(result.getBlockPos(), this.shouldExpandTransformingRange ? 3 : 2)
					.map(pos -> transformBlocks(this.level(), pos)).filter(Boolean::booleanValue).toList().isEmpty())
				{
					NFUEntityStatics.sendParticlesToEntity(this, ParticleTypes.EXPLOSION, 0, 0, 1, 0);
					this.level().playSound(null, this, SoundEvents.GENERIC_EXPLODE, this.getSoundSource(), 2.0f, 0.7f);
				}
			}
		}
		
		@Override
		public void onHitEntity(EntityHitResult result)
		{
			boolean shouldDealDamage = true;
			if (!this.level().isClientSide
					&& result.getEntity() instanceof LivingEntity living 
					&& NFFGirlsEntityStatics.isAlly(getOwner(), living) 
					&& !NFFGirlsConfigs.ValueCache.Combat.ENABLE_PROJECTILE_FRIENDLY_DAMAGE)
			{
				shouldDealDamage = false;
			}
			if (shouldDealDamage)
				super.onHitEntity(result);
			if (!NFUMathStatics.withinManhattanDistance(result.getEntity().getOnPos(), this.shouldExpandTransformingRange ? 3 : 2)
				.map(pos -> transformBlocks(this.level(), pos)).filter(Boolean::booleanValue).toList().isEmpty())
			{
				NFUEntityStatics.sendParticlesToEntity(this, ParticleTypes.EXPLOSION, 0, 0, 1, 0);
				this.level().playSound(null, this, SoundEvents.GENERIC_EXPLODE, this.getSoundSource(), 2.0f, 0.7f);
			}

		}
		
		
		protected static boolean transformBlocks(Level level, BlockPos pos)
		{
			BlockState blockstate = level.getBlockState(pos);
			if (blockstate.getBlock() == null) return false;
			if (blockstate.is(NFFGirlsBlocks.LUMINOUS_TERRACOTTA.get())) {
				level.setBlock(pos, NFFGirlsBlocks.ENHANCED_LUMINOUS_TERRACOTTA.get().defaultBlockState(), 1 | 2);
				return true;
			}
			else if (ColoredBlocks.GLAZED_TERRACOTTA_BLOCKS.contains(blockstate.getBlock())) {
				level.setBlock(pos, NFFGirlsBlocks.LUMINOUS_TERRACOTTA.get().defaultBlockState(), 1 | 2);
				return true;
			}
			else return false;
		}



	}

}
