package net.sodiumzh.nff.girls.entity.hmag;

import com.github.mechalopa.hmag.world.entity.NecroticReaperEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Container;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamedSunSensitiveMob;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsFollowOwnerGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.*;
import net.sodiumzh.nff.girls.inventory.NFFGirlsHmagNecroticReaperInventoryMenu;
import net.sodiumzh.nff.girls.sound.NFFGirlsSoundPresets;
import net.sodiumzh.nff.girls.util.NFFGirlsEntityStatics;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFFleeSunGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFRestrictSunGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFWaterAvoidingRandomStrollGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFZombieAttackGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.target.NFFHurtByTargetGoal;
import net.sodiumzh.nff.services.entity.taming.NFFTamingMapping;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventory;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventoryWithHandItems;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class HmagNecroticReaperEntity extends NecroticReaperEntity implements INFFGirlsTamedSunSensitiveMob
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

	/* Inventory */

	@Override
	public NFFTamedMobInventory createAdditionalInventory() {
		// TODO Auto-generated method stub
		return new NFFTamedMobInventoryWithHandItems(6, this);
	}
	
	@Override
	public NFFTamedInventoryMenu makeMenu(int containerId, Inventory playerInventory, Container container) {
		return new NFFGirlsHmagNecroticReaperInventoryMenu(containerId, playerInventory, container, this);
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
		//NFFTamedStatics.readBefriendedCommonSaveData(this, nbt);
		// Add other data reading here

	}

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

	@Override
	@Nonnull
	public Component getTypeName() {
		EntityType<?> typeBefore = NFFTamingMapping.getTypeBefore(this);
		return typeBefore != null ? typeBefore.getDescription() : super.getTypeName();
	}
	
	// Util
	// If player can control this mob
	@Deprecated
	public boolean controllable()
	{
		return true;
	}
	
	// Get how many Necrotic Reapers is <8 blocks away from owner 
	public static int countNearby(Player player)
	{
		Vec3 center = player.position();
		AABB area = new AABB(center.x - 8d, center.y - 8d, center.z - 8d, center.x + 8d, center.y + 8d, center.z + 8d);
		List<Entity> entityList = player.level().getEntities(player, area);
		int count = 0;
		for (Entity e: entityList)
		{
			if (e instanceof HmagNecroticReaperEntity nr
					&& nr.getOwnerUUID() != null // On Necrotic Reaper befriended, a nullptr exception occurred here
					&& nr.getOwnerUUID().equals(player.getUUID())
					&& nr.distanceToSqr(player) <= 64d)
			{
				count++;
			}
		}
		return count;
	}

	@Override
	public float sitPositionOffset() {
		return -0.5f;
	}

}
