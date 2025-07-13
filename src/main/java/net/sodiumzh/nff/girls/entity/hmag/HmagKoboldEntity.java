package net.sodiumzh.nff.girls.entity.hmag;

import com.github.mechalopa.hmag.world.entity.KoboldEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITag;
import net.minecraftforge.registries.tags.ITagManager;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.entity.ai.goal.IBlockLocator;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsFollowOwnerGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsLocateBlockGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.NFFGirlsNearestHostileToOwnerTargetGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.NFFGirlsNearestHostileToSelfTargetGoal;
import net.sodiumzh.nff.girls.entity.projectile.MobileParticleSourceEntity;
import net.sodiumzh.nff.girls.inventory.NFFGirlsKoboldInventoryMenu;
import net.sodiumzh.nff.girls.registry.NFFGirlsHealingItems;
import net.sodiumzh.nff.girls.sound.NFFGirlsSoundPresets;
import net.sodiumzh.nff.services.entity.ai.NFFTamedMobAIState;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFMeleeAttackGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFWaterAvoidingRandomStrollGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.target.NFFHurtByTargetGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.target.NFFOwnerHurtByTargetGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.target.NFFOwnerHurtTargetGoal;
import net.sodiumzh.nff.services.entity.taming.NFFTamedStatics;
import net.sodiumzh.nff.services.entity.taming.NFFTamingMapping;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventory;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventoryWithHandItems;
import net.sodiumzh.nfu.container.Tuple2;
import net.sodiumzh.nfu.entity.ManualTimer;
import net.sodiumzh.nfu.entity.MobApplicableItemTable;
import net.sodiumzh.nfu.util.NFUContainerStatics;
import net.sodiumzh.nfu.util.NFULevelStatics;
import net.sodiumzh.nfu.util.NFUTagStatics;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class HmagKoboldEntity extends KoboldEntity implements INFFGirlsTamed
{

	/* Initialization */

	public HmagKoboldEntity(EntityType<? extends HmagKoboldEntity> pEntityType, Level pLevel) {
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
	public MobApplicableItemTable getHealingItems()
	{
		return NFFGirlsHealingItems.GENERAL_HUMANOID_0.get();
	}

	/* Inventory */

	@Override
	public NFFTamedMobInventory createAdditionalInventory() {
		return new NFFTamedMobInventoryWithHandItems(4, this);
	}

	@Override
	public NFFTamedInventoryMenu makeMenu(int containerId, Inventory playerInventory, Container container) {
		return new NFFGirlsHmagKoboldInventoryMenu(containerId, playerInventory, container, this);
	}

	// Block locating related

	private ManualTimer<BlockPos> locatedBlocks = new ManualTimer<BlockPos>().setSerializable((BlockPos pos) -> {
		return pos.getX() + "," + pos.getY() + "," + pos.getZ();
	}, str -> {
		String[] split = str.split(",");
		return new BlockPos(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
	});
	private int locatingBlockRemainingCooldown = 0;
	private static final int LOCATING_BLOCK_COOLDOWN = 30 * 20;

	private static final List<String> LOCATABLE_ORES = List.of("coal", "copper", "iron", "gold",
		"redstone", "lapis", "diamond", "emerald");
	private static final List<String> USING_RAW_MATERIAL = List.of("coal", "lapis", "redstone");

	private static Optional<ResourceLocation> getLocatingBlockTags(ResourceLocation itemTag) {
		if (!itemTag.getNamespace().equals("forge")) return Optional.empty();
		String[] pathSplit = itemTag.getPath().split("/");
		if (pathSplit.length != 2) return Optional.empty();
		if (!LOCATABLE_ORES.contains(pathSplit[1])) return Optional.empty();
		List<String> keywords = USING_RAW_MATERIAL.contains(pathSplit[1]) ?
			List.of("gems", "ingots", "dusts", "fuels") : List.of("nuggets");
		if (!keywords.contains(pathSplit[0])) return Optional.empty();
		return Optional.of(new ResourceLocation("forge", "ores/" + pathSplit[1]));
	}

	private static List<Block> getLocatableBlocks(ItemStack offhandItem) {
		List<ResourceLocation> tagLocations = NFUTagStatics.getAllTags(offhandItem.getItem(), ForgeRegistries.ITEMS).stream()
			.map(t -> getLocatingBlockTags(t.location())).filter(Optional::isPresent)
			.map(Optional::get).toList();
		if (tagLocations.isEmpty()) return List.of();
		List<TagKey<Block>> tagNames = Optional.ofNullable(ForgeRegistries.BLOCKS.tags()).map(ITagManager::getTagNames)
			.map(Stream::toList).orElse(List.of());
		if (tagNames.isEmpty()) return List.of();
		return tagNames.stream().filter(k -> tagLocations.contains(k.location()))
			.map(k -> ForgeRegistries.BLOCKS.tags().getTag(k)).filter(t -> !t.isEmpty())
			.flatMap(ITag::stream).toList();
	}

	private void updateLocatingBlocks() {
		if (this.locatingBlockRemainingCooldown > 0)
			this.locatingBlockRemainingCooldown--;
		else this.locatingBlockRemainingCooldown = 0;
		this.locatedBlocks.update();
		if (this.locatingBlockRemainingCooldown > 0) return;
		if (!this.getAIState().equals(NFFTamedMobAIState.FOLLOW)) return;	// Only locate on following
		if (this.getAdditionalInventory().getItem(0).isEmpty()) return;	// Only when holding a pickaxe
		List<Block> locatableBlocks = getLocatableBlocks(this.getAdditionalInventory().getItem(1));
		if (locatableBlocks.isEmpty()) return;

		List<BlockPos> ores = NFULevelStatics.getSphericalBlockStates(this.level(), this.blockPosition(), 8,
				(pos, bs) -> locatableBlocks.contains(bs.getBlock()) && !this.locatedBlocks.hasTimer(pos))
			.map(Tuple::getA).toList();
		if (!ores.isEmpty()) {
			BlockPos targetPos = ores.get(this.random.nextInt(ores.size()));
			MobileParticleSourceEntity particleSource = new MobileParticleSourceEntity(this.level(), targetPos::getCenter)
				.setParticleType(ParticleTypes.HAPPY_VILLAGER).particlesPerTick(3).setSpeed(0.5d)
				.setMaxLifetime(40 * 20).setStartingPos(this.getEyePosition());
			this.level().addFreshEntity(particleSource);
			this.locatedBlocks.addTimer(targetPos, 300 * 20);	// Add 120s cooldown to prevent repeatedly locating the same block
			this.locatingBlockRemainingCooldown = LOCATING_BLOCK_COOLDOWN;
			this.getAdditionalInventory().getItem(1).shrink(1);
			this.getAdditionalInventory().syncToMob(this);
			this.level().playSound(this, this.blockPosition(), this.getAmbientSound(),
				SoundSource.PLAYERS, this.getSoundVolume() * 1.5f, this.getVoicePitch() * 1.5f);
		}
	}

	@Override
	protected void customServerAiStep() {
		super.customServerAiStep();
		if (!this.level().isClientSide())
			this.updateLocatingBlocks();
	}

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
	public Collection<Block> getLocatingBlocks() {
		Item offhand = this.getOffhandItem().getItem();
		if (NFUTagStatics.hasTag(offhand, "forge:nuggets/diamond"))
			return NFUTagStatics.getAllBlocksUnderTag("forge:ores/diamond");
		if (NFUTagStatics.hasTag(offhand, "forge:nuggets/emerald"))
			return NFUTagStatics.getAllBlocksUnderTag("forge:ores/emerald");
		if (NFUTagStatics.hasTag(offhand, "forge:nuggets/gold"))
			return NFUTagStatics.getAllBlocksUnderTag("forge:ores/gold");
		if (offhand == Items.LAPIS_LAZULI)
			return NFUTagStatics.getAllBlocksUnderTag("forge:ores/lapis");
		if (NFUTagStatics.hasTag(offhand, "forge:nuggets/iron"))
			return NFUTagStatics.getAllBlocksUnderTag("forge:ores/iron");
		if (NFUTagStatics.hasTag(offhand, "forge:nuggets/copper"))
			return NFUTagStatics.getAllBlocksUnderTag("forge:ores/copper");
		if (offhand == Items.REDSTONE)
			return NFUTagStatics.getAllBlocksUnderTag("forge:ores/redstone");
		if (offhand == Items.COAL)
			return NFUTagStatics.getAllBlocksUnderTag("forge:ores/coal");
		return NFUContainerStatics.listOf();
	}

	@Override
	public int getFrequency()
	{
		return 5 * 20;
	}
	
	@Override
	public void onStartLocating()
	{
		this.getAdditionalInventory().getItem(1).shrink(1);
		this.updateFromInventory();
	}
	*/
	// Sounds
	
	@Override
	protected SoundEvent getAmbientSound()
	{
		return NFFGirlsSoundPresets.generalAmbient(super.getAmbientSound());
	}
	
	// Misc

	@Override
	@Nonnull
	public Component getTypeName() {
		EntityType<?> typeBefore = NFFTamingMapping.getTypeBefore(this);
		return typeBefore != null ? typeBefore.getDescription() : super.getTypeName();
	}


}
