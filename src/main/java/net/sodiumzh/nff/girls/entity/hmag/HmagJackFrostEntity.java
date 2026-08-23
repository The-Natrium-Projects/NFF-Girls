package net.sodiumzh.nff.girls.entity.hmag;

import com.github.mechalopa.hmag.world.entity.JackFrostEntity;
import com.github.mechalopa.hmag.world.entity.projectile.HardSnowballEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsFollowOwnerGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsRangedAttackGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.*;
import net.sodiumzh.nff.girls.inventory.NFFGirlsFourBaublesInventoryMenu;
import net.sodiumzh.nff.girls.item.bauble.INFFGirlsBauble;
import net.sodiumzh.nff.girls.sound.NFFGirlsSoundPresets;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFWaterAvoidingRandomStrollGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.target.NFFHurtByTargetGoal;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nff.services.entity.taming.NFFTamingMapping;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventory;
import net.sodiumzh.nfu.entity.component.EntityComponentAPI;
import net.sodiumzh.nfu.entity.component.preset.EntityTimerComponent;
import net.sodiumzh.nfu.entity.component.preset.IEntityTimerComponentAccess;
import net.sodiumzh.nfu.function.MutablePredicate;
import net.sodiumzh.nfu.math.RndUtil;
import net.sodiumzh.nfu.util.NFUMathStatics;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.UUID;
import java.util.function.Consumer;

public class HmagJackFrostEntity extends JackFrostEntity implements INFFGirlsTamed {

	private EntityTimerComponent<Entity> getTimerComponent() {
		return EntityComponentAPI.getDefaultTimer(this);
	}

	/* Initialization */

	public HmagJackFrostEntity(EntityType<? extends HmagJackFrostEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		this.xpReward = 0;
		Arrays.fill(this.armorDropChances, 0);
		Arrays.fill(this.handDropChances, 0);
		this.immuneToHotBiomes.putOptional("bauble", INFFGirlsBauble::isEnvironmentImmunized);
		this.immuneToHotBiomes.putOptional("powder_snow", mob -> mob.isInPowderSnow);
	}
	
	/* Behavior */

	private final MutablePredicate<HmagJackFrostEntity> immuneToHotBiomes = new MutablePredicate<>();
	
	@Override
	protected void registerGoals() {
		goalSelector.addGoal(1, new FloatGoal(this));
		goalSelector.addGoal(4, new NFFGirlsRangedAttackGoal(this, 1.0D, 3 * 20, 15.0F).setSkipChance(0.5d));
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
	
	protected HardSnowballEntity getNewSnowball()
	{
		return new BefriendedJackFrostSnowball(this.level, this);
	}
	
	protected int getThrowLevel()
	{
		return this.getDataAccessor().getExpectedXPLevel() < 15 ? 0 : (
				this.getDataAccessor().getExpectedXPLevel() < 30 ? 1 : (
				this.getDataAccessor().getExpectedXPLevel() < 60 ? 2 : 3));
	}
	
	@Override
	public void performRangedAttack(LivingEntity target, float distance)
	{
		Consumer<Vec3> action = (offset) -> 
		{
			this.throwSnowballTo(target.getEyePosition().add(offset));
			this.playThrowingSound();
		};
		Runnable action1 = () -> action.accept(Vec3.ZERO);
		int i = getThrowLevel();
		switch (i)
		{
		case 0: 
		{
			action1.run();
			break;
		}
		case 1:
		{
			this.getTimerComponent().addDelayedAction(action1, 4, 2, false, true);
			//action1.run();
			//this.addMultipleDelayedActions(action1, 4, 8);
			break;
		}
		case 2:
		{
			Runnable action2 = () -> {
				action1.run();
				for (int j = 0; j < 3; ++j)
					action.accept(NFUMathStatics.randomUnitVector().scale(RndUtil.rndRangedDouble(0, 2)));
			};
			this.getTimerComponent().addDelayedAction(action2, 3, 4, false, true);
			//action2.run();
			//this.addMultipleDelayedActions(action2, 3, 6, 9, 12);
			break;
		}
		case 3:
		{
			Runnable action3 = () -> {
				action1.run();
				for (int j = 0; j < 6; ++j)
					action.accept(NFUMathStatics.randomUnitVector().scale(RndUtil.rndRangedDouble(0, 2)));
			};
			this.getTimerComponent().addDelayedAction(action3, 3, 6, false, true);
			//action3.run();
			//this.addMultipleDelayedActions(action3, 3, 6, 9, 12, 15, 18);
			break;
		}
		default: 
		{
			throw new RuntimeException();
		}
		}
	}
	
	protected float getShootInaccuracy()
	{
		return 5f;
	}
	
	protected float getShootDamage()
	{
		return 3f + (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
	}
	
	public void throwSnowballTo(Vec3 targetPos)
	{
		HardSnowballEntity snowball = getNewSnowball();
		double deltaX = targetPos.x() - snowball.getX();
		double deltaY = targetPos.y() - snowball.getY();
		double deltaZ = targetPos.z() - snowball.getZ();
		double yOffset = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ) * 0.05D;
		snowball.shoot(deltaX, deltaY + yOffset, deltaZ, getShootSpeed(), getShootInaccuracy());
		snowball.setDamage(getShootDamage());
		this.level.addFreshEntity(snowball);
	}
	
	public void throwSnowball(Vec3 direction)
	{
		Vec3 n = direction.normalize();
		//double yOffset = Math.sqrt(n.x * n.x + n.z * n.z) * 0.1D;
		HardSnowballEntity snowball = getNewSnowball();
		snowball.shoot(n.x, n.y, n.z, getShootSpeed(), getShootInaccuracy());
		snowball.setDamage(getShootDamage());
		this.level.addFreshEntity(snowball);
	}
	
	public void playThrowingSound()
	{
		this.playSound(SoundEvents.SNOW_GOLEM_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
	}
	
	protected float getShootSpeed()
	{
		return 1.5f;
	}
	
	public boolean isImmuneToHotBiomes()
	{
		return this.immuneToHotBiomes.test(this);
	}

	/* Inventory */
	
	@Override
	public NFFTamedMobInventory createAdditionalInventory() {
		return  new NFFTamedMobInventory(4, this);
	}

	@Override
	public NFFTamedInventoryMenu makeMenu(int containerId, Inventory playerInventory, Container container) {
		return new NFFGirlsFourBaublesInventoryMenu(containerId, playerInventory, container, this);
		// You can keep it null, but in this case never call openBefriendedInventory() or it will crash.
	}

	/* Save and Load */

	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		//NFFTamedStatics.readBefriendedCommonSaveData(this, nbt);
		// Add other data reading here

	}
/*
	@Override
	public HashMap<String, ItemStack> getBaubleSlots() {
		return this.continuousBaubleSlots(0, 4);
	}

	@Override
	public BaubleHandler getBaubleHandler() {
		return DwmgBaubleHandlers.GENERAL;
	}*/

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
	@Nonnull
	public Component getTypeName() {
		EntityType<?> typeBefore = NFFTamingMapping.getTypeBefore(this);
		return typeBefore != null ? typeBefore.getDescription() : super.getTypeName();
	}


	public static class BefriendedJackFrostSnowball extends HardSnowballEntity
	{

		public BefriendedJackFrostSnowball(Level level, LivingEntity thrower)
		{
			super(level, thrower);
		}

		@Override
		public void onHitEntity(EntityHitResult result)
		{
			if (this.getOwner() instanceof HmagJackFrostEntity jf)
			{
				if (jf == result.getEntity())
					return;
				if (jf.isOwnerPresent() && jf.getOwnerUUID().equals(result.getEntity().getUUID()))
					return;
				if (result.getEntity() instanceof INFFTamed bm && bm.getOwnerUUID().equals(jf.getOwnerUUID()))
					return;
				if (result.getEntity() instanceof TamableAnimal ta && ta.isTame() && ta.getOwnerUUID().equals(jf.getOwnerUUID()))
					return;
			}
			super.onHitEntity(result);
		}
	}

}


