package net.sodiumzh.nff.girls.registry;

import java.util.function.Function;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.entity.hmag.HmagAlrauneEntity;
import net.sodiumzh.nff.girls.entity.hmag.HmagBansheeEntity;
import net.sodiumzh.nff.girls.entity.hmag.HmagCreeperGirlEntity;
import net.sodiumzh.nff.girls.entity.hmag.HmagCrimsonSlaughtererEntity;
import net.sodiumzh.nff.girls.entity.hmag.HmagCursedDollEntity;
import net.sodiumzh.nff.girls.entity.hmag.HmagDodomekiEntity;
import net.sodiumzh.nff.girls.entity.hmag.HmagDrownedGirlEntity;
import net.sodiumzh.nff.girls.entity.hmag.HmagDullahanEntity;
import net.sodiumzh.nff.girls.entity.hmag.HmagEnderExecutorEntity;
import net.sodiumzh.nff.girls.entity.hmag.HmagGhastlySeekerEntity;
import net.sodiumzh.nff.girls.entity.hmag.HmagGlaryadEntity;
import net.sodiumzh.nff.girls.entity.hmag.HmagHarpyEntity;
import net.sodiumzh.nff.girls.entity.hmag.HmagHornetEntity;
import net.sodiumzh.nff.girls.entity.hmag.HmagHuskGirlEntity;
import net.sodiumzh.nff.girls.entity.hmag.HmagImpEntity;
import net.sodiumzh.nff.girls.entity.hmag.HmagJackFrostEntity;
import net.sodiumzh.nff.girls.entity.hmag.HmagJiangshiEntity;
import net.sodiumzh.nff.girls.entity.hmag.HmagKoboldEntity;
import net.sodiumzh.nff.girls.entity.hmag.HmagMeltyMonsterEntity;
import net.sodiumzh.nff.girls.entity.hmag.HmagNecroticReaperEntity;
import net.sodiumzh.nff.girls.entity.hmag.HmagNightwalkerEntity;
import net.sodiumzh.nff.girls.entity.hmag.HmagRedcapEntity;
import net.sodiumzh.nff.girls.entity.hmag.HmagSkeletonGirlEntity;
import net.sodiumzh.nff.girls.entity.hmag.HmagSlimeGirlEntity;
import net.sodiumzh.nff.girls.entity.hmag.HmagSnowCanineEntity;
import net.sodiumzh.nff.girls.entity.hmag.HmagStrayGirlEntity;
import net.sodiumzh.nff.girls.entity.hmag.HmagWitherSkeletonGirlEntity;
import net.sodiumzh.nff.girls.entity.hmag.HmagZombieGirlEntity;
import net.sodiumzh.nff.girls.entity.projectile.MagicalGelBallEntity;
import net.sodiumzh.nff.girls.entity.projectile.NFFGhastFireballEntity;
import net.sodiumzh.nff.girls.entity.projectile.NFFHmagAlrauneSeedEntity;
import net.sodiumzh.nff.girls.entity.projectile.NecromancerMagicBulletEntity;
import net.sodiumzh.nff.girls.entity.projectile.ReinforcedFishingHookEntity;

@Mod.EventBusSubscriber(modid = NFFGirls.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NFFGirlsEntityTypes {

	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, NFFGirls.MOD_ID);
	
	public static final RegistryObject<EntityType<HmagZombieGirlEntity>> HMAG_ZOMBIE_GIRL = 
			ENTITY_TYPES.register("hmag_zombie_girl", () -> EntityType.Builder.of(
			HmagZombieGirlEntity::new, MobCategory.CREATURE)
			.sized(0.6F, 1.95F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.noSummon()
			.build(new ResourceLocation(NFFGirls.MOD_ID, "hmag_zombie_girl").toString()));
	
	public static final RegistryObject<EntityType<HmagSkeletonGirlEntity>> HMAG_SKELETON_GIRL = 
			ENTITY_TYPES.register("hmag_skeleton_girl", () -> EntityType.Builder.of(
			HmagSkeletonGirlEntity::new, MobCategory.CREATURE)
			.sized(0.6F, 1.99F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.noSummon()
			.build(new ResourceLocation(NFFGirls.MOD_ID, "hmag_skeleton_girl").toString()));

	public static final RegistryObject<EntityType<HmagHuskGirlEntity>> HMAG_HUSK_GIRL = 
			ENTITY_TYPES.register("hmag_husk_girl", () -> EntityType.Builder.of(
			HmagHuskGirlEntity::new, MobCategory.CREATURE)
			.sized(0.6F, 1.95F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.noSummon()
			.build(new ResourceLocation(NFFGirls.MOD_ID, "hmag_husk_girl").toString()));
	
	public static final RegistryObject<EntityType<HmagDrownedGirlEntity>> HMAG_DROWNED_GIRL = 
			ENTITY_TYPES.register("hmag_drowned_girl", () -> EntityType.Builder.of(
			HmagDrownedGirlEntity::new, MobCategory.CREATURE)
			.sized(0.6F, 1.95F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.noSummon()
			.build(new ResourceLocation(NFFGirls.MOD_ID, "hmag_drowned_girl").toString()));
	
	public static final RegistryObject<EntityType<HmagCreeperGirlEntity>> HMAG_CREEPER_GIRL = 
			ENTITY_TYPES.register("hmag_creeper_girl", () -> EntityType.Builder.of(
			HmagCreeperGirlEntity::new, MobCategory.CREATURE)
			.sized(0.6F, 1.95F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.noSummon()
			.build(new ResourceLocation(NFFGirls.MOD_ID, "hmag_creeper_girl").toString()));
	
	public static final RegistryObject<EntityType<HmagEnderExecutorEntity>> HMAG_ENDER_EXECUTOR =
			ENTITY_TYPES.register("hmag_ender_executor", () -> EntityType.Builder.of(
			HmagEnderExecutorEntity::new, MobCategory.CREATURE)
			.sized(0.6F, 2.9F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.noSummon()
			.build(new ResourceLocation(NFFGirls.MOD_ID, "hmag_ender_executor").toString()));
	
	public static final RegistryObject<EntityType<HmagStrayGirlEntity>> HMAG_STRAY_GIRL = 
			ENTITY_TYPES.register("hmag_stray_girl", () -> EntityType.Builder
			.of(HmagStrayGirlEntity::new, MobCategory.CREATURE)
			.immuneTo(Blocks.POWDER_SNOW)
			.sized(0.6F, 1.99F)			
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.noSummon()
			.build(new ResourceLocation(NFFGirls.MOD_ID, "hmag_stray_girl").toString()));
	
	public static final RegistryObject<EntityType<HmagWitherSkeletonGirlEntity>> HMAG_WITHER_SKELETON_GIRL = 
			ENTITY_TYPES.register("hmag_wither_skeleton_girl", () -> EntityType.Builder
			.of(HmagWitherSkeletonGirlEntity::new, MobCategory.CREATURE)
			.fireImmune()
			.immuneTo(Blocks.WITHER_ROSE)
			.sized(0.7F, 2.4F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.noSummon()
			.build(new ResourceLocation(NFFGirls.MOD_ID, "hmag_wither_skeleton_girl").toString()));

	public static final RegistryObject<EntityType<HmagHornetEntity>> HMAG_HORNET = 
			ENTITY_TYPES.register("hmag_hornet", () -> EntityType.Builder
			.of(HmagHornetEntity::new, MobCategory.CREATURE)
			.sized(0.6F, 1.7F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.noSummon()
			.build(new ResourceLocation(NFFGirls.MOD_ID, "hmag_hornet").toString()));
	
	public static final RegistryObject<EntityType<HmagNecroticReaperEntity>> HMAG_NECROTIC_REAPER =
			ENTITY_TYPES.register("hmag_necrotic_reaper", () -> EntityType.Builder
			.of(HmagNecroticReaperEntity::new, MobCategory.CREATURE)
			.sized(0.6F, 1.95F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.noSummon()
			.build(new ResourceLocation(NFFGirls.MOD_ID, "hmag_necrotic_reaper").toString()));
	
	public static final RegistryObject<EntityType<HmagGhastlySeekerEntity>> HMAG_GHASTLY_SEEKER = 
			ENTITY_TYPES.register("hmag_ghastly_seeker", () -> EntityType.Builder
			.of(HmagGhastlySeekerEntity::new, MobCategory.CREATURE)
			.fireImmune()
			.sized(0.9F, 2.9F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.noSummon()
			.build(new ResourceLocation(NFFGirls.MOD_ID, "hmag_ghastly_seeker").toString()));
	
	public static final RegistryObject<EntityType<HmagBansheeEntity>> HMAG_BANSHEE = 
			ENTITY_TYPES.register("hmag_banshee", () -> EntityType.Builder
			.of(HmagBansheeEntity::new, MobCategory.CREATURE)
			.sized(0.6F, 1.95F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.noSummon()
			.build(new ResourceLocation(NFFGirls.MOD_ID, "hmag_banshee").toString()));
	
	public static final RegistryObject<EntityType<HmagKoboldEntity>> HMAG_KOBOLD =
			ENTITY_TYPES.register("hmag_kobold", () -> EntityType.Builder
			.of(HmagKoboldEntity::new, MobCategory.CREATURE)
			.sized(0.6F, 1.7F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.noSummon()
			.build(new ResourceLocation(NFFGirls.MOD_ID, "hmag_kobold").toString()));
	
	public static final RegistryObject<EntityType<HmagImpEntity>> HMAG_IMP =
			ENTITY_TYPES.register("hmag_imp", () -> EntityType.Builder
			.of(HmagImpEntity::new, MobCategory.CREATURE)
			.fireImmune()
			.sized(0.6F, 1.7F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.noSummon()
			.build(new ResourceLocation(NFFGirls.MOD_ID, "hmag_imp").toString()));
	
	public static final RegistryObject<EntityType<HmagHarpyEntity>> HMAG_HARPY = 
			ENTITY_TYPES.register("hmag_harpy", () -> EntityType.Builder
			.of(HmagHarpyEntity::new, MobCategory.CREATURE)
			.sized(0.6F, 1.95F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.noSummon()
			.build(new ResourceLocation(NFFGirls.MOD_ID, "hmag_harpy").toString()));
	
	public static final RegistryObject<EntityType<HmagSnowCanineEntity>> HMAG_SNOW_CANINE = 
			ENTITY_TYPES.register("hmag_snow_canine", () -> EntityType.Builder
			.of(HmagSnowCanineEntity::new, MobCategory.CREATURE)
			.sized(0.6F, 1.95F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.noSummon()
			.build(new ResourceLocation(NFFGirls.MOD_ID, "hmag_snow_canine").toString()));
	
	public static final RegistryObject<EntityType<HmagSlimeGirlEntity>> HMAG_SLIME_GIRL = 
			ENTITY_TYPES.register("hmag_slime_girl", () -> EntityType.Builder
			.of(HmagSlimeGirlEntity::new, MobCategory.CREATURE)
			.sized(0.6F, 1.95F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.noSummon()
			.build(new ResourceLocation(NFFGirls.MOD_ID, "hmag_slime_girl").toString()));
	
	public static final RegistryObject<EntityType<HmagJiangshiEntity>> HMAG_JIANGSHI = 
			ENTITY_TYPES.register("hmag_jiangshi", () -> EntityType.Builder
			.of(HmagJiangshiEntity::new, MobCategory.CREATURE)
			.sized(0.6F, 1.95F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.noSummon()
			.build(new ResourceLocation(NFFGirls.MOD_ID, "hmag_jiangshi").toString()));
	
	public static final RegistryObject<EntityType<HmagDullahanEntity>> HMAG_DULLAHAN = 
			ENTITY_TYPES.register("hmag_dullahan", () -> EntityType.Builder
			.of(HmagDullahanEntity::new, MobCategory.CREATURE)
			.sized(0.6F, 1.75F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.noSummon()
			.build(new ResourceLocation(NFFGirls.MOD_ID, "hmag_dullahan").toString()));
	
	public static final RegistryObject<EntityType<HmagDodomekiEntity>> HMAG_DODOMEKI = 
			ENTITY_TYPES.register("hmag_dodomeki", () -> EntityType.Builder
			.of(HmagDodomekiEntity::new, MobCategory.CREATURE)
			.sized(0.6F, 1.95F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.noSummon()
			.build(new ResourceLocation(NFFGirls.MOD_ID, "hmag_dodomeki").toString()));
	
	public static final RegistryObject<EntityType<HmagAlrauneEntity>> HMAG_ALRAUNE = 
			ENTITY_TYPES.register("hmag_alraune", () -> EntityType.Builder
			.of(HmagAlrauneEntity::new, MobCategory.CREATURE)
			.sized(0.6F, 1.95F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.noSummon()
			.build(new ResourceLocation(NFFGirls.MOD_ID, "hmag_alraune").toString()));

	public static final RegistryObject<EntityType<HmagGlaryadEntity>> HMAG_GLARYAD = 
			ENTITY_TYPES.register("hmag_glaryad", () -> EntityType.Builder
			.of(HmagGlaryadEntity::new, MobCategory.CREATURE)
			.sized(0.6F, 1.95F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.noSummon()
			.build(new ResourceLocation(NFFGirls.MOD_ID, "hmag_glaryad").toString()));

	public static final RegistryObject<EntityType<HmagCrimsonSlaughtererEntity>> HMAG_CRIMSON_SLAUGHTERER = 
			ENTITY_TYPES.register("hmag_crimson_slaughterer", () -> EntityType.Builder
			.of(HmagCrimsonSlaughtererEntity::new, MobCategory.CREATURE)
			.fireImmune()
			.sized(0.75F, 2.45F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.noSummon()
			.build(new ResourceLocation(NFFGirls.MOD_ID, "hmag_crimson_slaughterer").toString()));

	public static final RegistryObject<EntityType<HmagCursedDollEntity>> HMAG_CURSED_DOLL = 
			/*ENTITY_TYPES.register("hmag_cursed_doll", () -> EntityType.Builder
			.of(HmagCursedDollEntity::new, MobCategory.CREATURE)
			.sized(0.6F, 1.7F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.noSummon()
			.build(new ResourceLocation(NFFGirls.MOD_ID, "hmag_cursed_doll").toString()));*/
			registerBM("hmag_cursed_doll", HmagCursedDollEntity::new, (builder) -> builder
			.sized(0.6F, 1.7F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false));
	
	public static final RegistryObject<EntityType<HmagRedcapEntity>> HMAG_REDCAP = 
			registerBM("hmag_redcap", HmagRedcapEntity::new, (builder) -> builder
			.sized(0.6F, 1.7F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false));
	
	public static final RegistryObject<EntityType<HmagJackFrostEntity>> HMAG_JACK_FROST = 
			registerBM("hmag_jack_frost", HmagJackFrostEntity::new, (builder) -> builder
			.sized(0.6F, 1.95F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false));
	
	public static final RegistryObject<EntityType<HmagMeltyMonsterEntity>> HMAG_MELTY_MONSTER = 
			registerBM("hmag_melty_monster", HmagMeltyMonsterEntity::new, (builder) -> builder
			.fireImmune()
			.sized(0.6F, 1.95F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false));

	public static final RegistryObject<EntityType<HmagNightwalkerEntity>> HMAG_NIGHTWALKER = 
			registerBM("hmag_nightwalker", HmagNightwalkerEntity::new, (builder) -> builder
			.sized(1.2F, 2.52F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false));
	
	// ================================================================================================= //
	@SubscribeEvent
	public static void onAttributeCreate(EntityAttributeCreationEvent event) {	
		
		NFFGirlsEntityAttributes.REGISTRY.forEach((type, supplier) -> 
		{
			event.put(type, supplier.get().build());
		});
	
	}
	// ================================================================================================= //
	
	// Projectiles
	
	public static final RegistryObject<EntityType<NecromancerMagicBulletEntity>> NECROMANCER_MAGIC_BULLET = 
			ENTITY_TYPES.register("necromancer_magic_bullet", () -> EntityType.Builder
			.<NecromancerMagicBulletEntity>of(NecromancerMagicBulletEntity::new, MobCategory.MISC)
			.sized(0.3215F, 0.3215F)
			.setTrackingRange(4)
			.setUpdateInterval(1)
			.setShouldReceiveVelocityUpdates(true)
			.setCustomClientFactory(NecromancerMagicBulletEntity::new)
			.build(new ResourceLocation(NFFGirls.MOD_ID, "necromancer_magic_bullet").toString()));
	
	public static final RegistryObject<EntityType<NFFGhastFireballEntity>> BEFRIENDED_GHAST_FIREBALL = 
			ENTITY_TYPES.register("befriended_ghast_fireball", () -> EntityType.Builder
			.<NFFGhastFireballEntity>of(NFFGhastFireballEntity::new, MobCategory.MISC)
			.sized(1.0F, 1.0F)
			.clientTrackingRange(4)
			.updateInterval(10)
			.build(new ResourceLocation(NFFGirls.MOD_ID, "befriended_ghast_fireball").toString()));

	public static final RegistryObject<EntityType<MagicalGelBallEntity>> MAGICAL_GEL_BALL = 
			ENTITY_TYPES.register("magical_gel_ball", () -> EntityType.Builder
			.<MagicalGelBallEntity>of(MagicalGelBallEntity::new, MobCategory.MISC)
			.sized(0.25F, 0.25F)		
			.clientTrackingRange(4)
			.updateInterval(10)
			.build(new ResourceLocation(NFFGirls.MOD_ID, "magical_gel_ball").toString()));
	
	public static final RegistryObject<EntityType<NFFHmagAlrauneSeedEntity.PoisonSeed>> ALRAUNE_POISON_SEED = 
			ENTITY_TYPES.register("hmag_alraune_poison_seed", () -> EntityType.Builder
			.<NFFHmagAlrauneSeedEntity.PoisonSeed>of(NFFHmagAlrauneSeedEntity.PoisonSeed::new, MobCategory.MISC)
			.sized(0.25F, 0.25F)
			.setTrackingRange(4)
			.setUpdateInterval(10)
			.setShouldReceiveVelocityUpdates(true)
			.setCustomClientFactory(NFFHmagAlrauneSeedEntity.PoisonSeed::new)
			.build(new ResourceLocation(NFFGirls.MOD_ID, "hmag_alraune_poison_seed").toString()));
	
	public static final RegistryObject<EntityType<NFFHmagAlrauneSeedEntity.HealingSeed>> ALRAUNE_HEALING_SEED = 
			ENTITY_TYPES.register("hmag_alraune_healing_seed", () -> EntityType.Builder
			.<NFFHmagAlrauneSeedEntity.HealingSeed>of(NFFHmagAlrauneSeedEntity.HealingSeed::new, MobCategory.MISC)
			.sized(0.25F, 0.25F)
			.setTrackingRange(4)
			.setUpdateInterval(10)
			.setShouldReceiveVelocityUpdates(true)
			.setCustomClientFactory(NFFHmagAlrauneSeedEntity.HealingSeed::new)
			.build(new ResourceLocation(NFFGirls.MOD_ID, "hmag_alraune_healing_seed").toString()));
	
	public static final RegistryObject<EntityType<ReinforcedFishingHookEntity>> REINFORCED_FISHING_HOOK = 
			ENTITY_TYPES.register("reinforced_fishing_hook", () -> EntityType.Builder
			.<ReinforcedFishingHookEntity>of(ReinforcedFishingHookEntity::new, MobCategory.MISC)
			.noSave()
			.noSummon()
			.sized(0.25F, 0.25F)
			.clientTrackingRange(4)
			.updateInterval(5)
			.build(new ResourceLocation(NFFGirls.MOD_ID, "reinforced_fishing_hook").toString()));

	
	
	
	// ========== Utilities ============ //
	
	protected static <T extends LivingEntity> RegistryObject<EntityType<T>> registerBM(DeferredRegister<EntityType<?>> registry, String modId, String regName, 
			EntityType.EntityFactory<T> creator, MobCategory category, Function<EntityType.Builder<T>, EntityType.Builder<T>> builderModifier)
	{
		return registry.register(regName, () -> builderModifier.apply(EntityType.Builder
		.of(creator, category)).noSummon().build(new ResourceLocation(modId, regName).toString()));
	}
	
	private static <T extends LivingEntity> RegistryObject<EntityType<T>> registerBM(String regName, 
			EntityType.EntityFactory<T> creator, Function<EntityType.Builder<T>, EntityType.Builder<T>> builderModifier)
	{
		return registerBM(ENTITY_TYPES, NFFGirls.MOD_ID, regName, creator, MobCategory.CREATURE, builderModifier);
	}
}