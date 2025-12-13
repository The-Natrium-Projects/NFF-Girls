package net.sodiumzh.nff.girls.entity.hmag;

import com.github.mechalopa.hmag.world.entity.ImpEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Tuple;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsFollowOwnerGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.*;
import net.sodiumzh.nff.girls.entity.projectile.MobileParticleSourceEntity;
import net.sodiumzh.nff.girls.inventory.HmagImpInventoryMenu;
import net.sodiumzh.nff.girls.registry.NFFGirlsItems;
import net.sodiumzh.nff.girls.sound.NFFGirlsSoundPresets;
import net.sodiumzh.nff.services.entity.ai.NFFTamedMobAIState;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFMeleeAttackGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFWaterAvoidingRandomStrollGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.target.NFFHurtByTargetGoal;
import net.sodiumzh.nff.services.entity.taming.NFFTamingMapping;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventory;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventoryWithHandItems;
import net.sodiumzh.nfu.entity.ManualTimer;
import net.sodiumzh.nfu.util.NFULevelStatics;
import net.sodiumzh.nfu.util.NFUTagStatics;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class HmagImpEntity extends ImpEntity implements INFFGirlsTamed//, IBlockLocator
{

	private ManualTimer<BlockPos> locatedBlocks = new ManualTimer<BlockPos>().setSerializable((BlockPos pos) -> {
		return pos.getX() + "," + pos.getY() + "," + pos.getZ();
		}, str -> {
			String[] split = str.split(",");
			return new BlockPos(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
		});
	private int locatingBlockRemainingCooldown = 0;
	private static final int LOCATING_BLOCK_COOLDOWN = 30 * 20;


	/* Initialization */

	public HmagImpEntity(EntityType<? extends HmagImpEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		this.xpReward = 0;
		Arrays.fill(this.armorDropChances, 0);
		Arrays.fill(this.handDropChances, 0);
	}

	/* AI */

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(1, new FloatGoal(this));
		goalSelector.addGoal(3, new NFFMeleeAttackGoal(this, 1.0d, true));
		//goalSelector.addGoal(3, new NFFGirlsLocateBlockGoal(this, 6d));
		goalSelector.addGoal(4, new NFFGirlsFollowOwnerGoal(this, 1.0d, 5.0f, 2.0f, false));
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
	
	/* Interaction */

	// Map items that can heal the mob and healing values here.
	// Leave it empty if you don't need healing features.
	/*@SuppressWarnings("unchecked")
	@Override
	public MobApplicableItemTable getHealingItems()
	{
		return NFFGirlsHealingItems.GENERAL_HUMANOID_0.get();
	}
*/

	/* Inventory */

	@Override
	public NFFTamedMobInventory createAdditionalInventory() {
		return new NFFTamedMobInventoryWithHandItems(4, this);
	}

	@Override
	public NFFTamedInventoryMenu makeMenu(int containerId, Inventory playerInventory, Container container) {
		return new HmagImpInventoryMenu(containerId, playerInventory, container, this);
	}

	// block locating related

	private static final TagKey<Item> NETHERITE_SCRAP_NUGGETS =
		NFUTagStatics.createItemTag("forge", "nuggets/netherite_scrap");
	private static final TagKey<Block> NETHERITE_SCRAP_ORES =
		NFUTagStatics.createBlockTag("forge", "ores/netherite_scrap");

	private void updateLocatingBlocks() {
		if (this.locatingBlockRemainingCooldown > 0)
			this.locatingBlockRemainingCooldown--;
		else this.locatingBlockRemainingCooldown = 0;
		this.locatedBlocks.update();
		if (this.locatingBlockRemainingCooldown > 0) return;
		if (!this.getAIState().equals(NFFTamedMobAIState.FOLLOW)) return;	// Only locate on following
		if (!this.getAdditionalInventory().getItem(0).is(NFFGirlsItems.NETHERITE_FORK.get()))
			return;

		if (this.getAdditionalInventory().getItem(1).is(NETHERITE_SCRAP_NUGGETS)) {
			List<BlockPos> ores = NFULevelStatics.getSphericalBlockStates(this.level, this.blockPosition(), 8,
				(pos, bs) -> bs.is(NETHERITE_SCRAP_ORES) && !this.locatedBlocks.hasTimer(pos))
				.map(Tuple::getA).toList();
			if (!ores.isEmpty()) {
				BlockPos targetPos = ores.get(this.random.nextInt(ores.size()));
				MobileParticleSourceEntity particleSource = new MobileParticleSourceEntity(this.level, () -> Vec3.atCenterOf(targetPos))
					.setParticleType(ParticleTypes.HAPPY_VILLAGER).particlesPerTick(3).setSpeed(0.5d)
					.setMaxLifetime(40 * 20).setStartingPos(this.getEyePosition());
				this.level.addFreshEntity(particleSource);
				this.locatedBlocks.addTimer(targetPos, 300 * 20);	// Add 300s cooldown to prevent repeatedly locating the same block
				this.locatingBlockRemainingCooldown = LOCATING_BLOCK_COOLDOWN;
				this.getAdditionalInventory().getItem(1).shrink(1);
				this.getAdditionalInventory().syncToMob(this);
				this.level.playSound(this.getOwner(), this.blockPosition(), this.getAmbientSound(),
					SoundSource.PLAYERS, this.getSoundVolume() * 1.5f, this.getVoicePitch() * 1.5f);
			}
		}
	}

	@Override
	protected void customServerAiStep() {
		super.customServerAiStep();
		if (!this.level.isClientSide())
			this.updateLocatingBlocks();
	}

	/*@Override
	public Collection<Block> getLocatingBlocks() {
		if (!this.getAdditionalInventory().getItem(0).is(NFFGirlsItems.NETHERITE_FORK.get()))
			return NFUContainerStatics.listOf();
		Item offhand = this.getOffhandItem().getItem();
		if (NFUTagStatics.hasTag(offhand, "forge:nuggets/netherite_scrap"))
			return NFUTagStatics.getAllBlocksUnderTag("forge:ores/netherite_scrap");
		return NFUContainerStatics.listOf();
	}

	@Override
	public int getFrequency()
	{
		return 10 * 20;
	}
	
	@Override
	public void onStartLocating()
	{
		this.getAdditionalInventory().getItem(1).shrink(1);
		this.updateFromInventory();
	}
*/
	/* Save and Load */

	@Override
	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		nbt.put("locatedBlocks", this.locatedBlocks.serialize());
		nbt.putInt("locatingCooldown", this.locatingBlockRemainingCooldown);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		if (nbt.contains("locatedBlocks"))
			this.locatedBlocks.deserialize(nbt.getCompound("locatedBlocks"));
		if (nbt.contains("locatingCooldown"))
			this.locatingBlockRemainingCooldown = nbt.getInt("locatingCooldown");
		setInit();
	}
/*
	@Override
	public HashMap<String, ItemStack> getBaubleSlots() {
		HashMap<String, ItemStack> map = new HashMap<String, ItemStack>();
		map.put("0", this.getAdditionalInventory().getItem(2));
		map.put("1", this.getAdditionalInventory().getItem(3));
		return map;
	}

	@Override
	public BaubleHandler getBaubleHandler() {
		return DwmgBaubleHandlers.GENERAL;
	}
	*/
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

	@Override
	@Nonnull
	public Component getTypeName() {
		EntityType<?> typeBefore = NFFTamingMapping.getTypeBefore(this);
		return typeBefore != null ? typeBefore.getDescription() : super.getTypeName();
	}


}

