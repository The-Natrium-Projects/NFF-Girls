package net.sodiumzh.nff.girls.registry;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Monster;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nfu.entity.EntityAttributeProvider;
import net.sodiumzh.nfu.registry.NFURegistries;
import net.sodiumzh.nfu.registry.NFURegistry;
import net.sodiumzh.nfu.registry.NFURegistryEntryCollection;

import java.util.HashMap;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = NFFGirls.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NFFGirlsEntityAttributes
{

	public static final NFURegistryEntryCollection<EntityAttributeProvider> ATTRIBUTE_PROVIDERS =
		NFURegistryEntryCollection.create(NFURegistries.ENTITY_ATTRIBUTE_PROVIDERS, NFFGirls.MOD_ID);

	public static final NFURegistry.Accessor<EntityAttributeProvider> HMAG_ZOMBIE_GIRL = 
		ATTRIBUTE_PROVIDERS.register("zombie_girl", () -> EntityAttributeProvider.monster()
			.add(Attributes.MAX_HEALTH, 30.0D)
			.add(Attributes.MOVEMENT_SPEED, 0.28D)
			.add(Attributes.ATTACK_DAMAGE, 4.0D)
			.add(Attributes.ARMOR, 5.0D)
			.add(Attributes.FOLLOW_RANGE, 35.0D)
			.add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 0D));

	public static final NFURegistry.Accessor<EntityAttributeProvider> HMAG_DROWNED_GIRL =
		ATTRIBUTE_PROVIDERS.register("drowned_girl", () -> EntityAttributeProvider.monster()
			.add(Attributes.MAX_HEALTH, 30.0D)
			.add(Attributes.MOVEMENT_SPEED, 0.245D)
			.add(Attributes.ATTACK_DAMAGE, 4.0D)
			.add(Attributes.ARMOR, 3.0D)
			.add(Attributes.FOLLOW_RANGE, 35.0D)
			.add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 0D));

	public static final NFURegistry.Accessor<EntityAttributeProvider> HMAG_SKELETON_GIRL =
		ATTRIBUTE_PROVIDERS.register("skeleton_girl", () -> EntityAttributeProvider.monster()
			.add(Attributes.MAX_HEALTH, 30.0D)
			.add(Attributes.MOVEMENT_SPEED, 0.25D)
			.add(Attributes.ATTACK_DAMAGE, 3.25D)
			.add(Attributes.ARMOR, 1.0D)
			.add(Attributes.FOLLOW_RANGE, 64.0D));

	public static final NFURegistry.Accessor<EntityAttributeProvider> HMAG_WITHER_SKELETON_GIRL =
		ATTRIBUTE_PROVIDERS.register("wither_skeleton_girl", () -> EntityAttributeProvider.monster()
			.add(Attributes.MAX_HEALTH, 36.0D)
			.add(Attributes.MOVEMENT_SPEED, 0.26D)
			.add(Attributes.ATTACK_DAMAGE, 4.5D)
			.add(Attributes.ARMOR, 4.0D)
			.add(Attributes.KNOCKBACK_RESISTANCE, 0.25D));

	public static final NFURegistry.Accessor<EntityAttributeProvider> HMAG_CREEPER_GIRL = 
		ATTRIBUTE_PROVIDERS.register("creeper_girl", () -> EntityAttributeProvider.monster()
		.add(Attributes.MAX_HEALTH, 30.0D)
		.add(Attributes.MOVEMENT_SPEED, 0.3D)
		.add(Attributes.KNOCKBACK_RESISTANCE, 0.25D)
		.add(Attributes.ATTACK_DAMAGE, 0D));

	public static final NFURegistry.Accessor<EntityAttributeProvider> HMAG_ENDER_EXECUTOR =
		ATTRIBUTE_PROVIDERS.register("ender_executor", () -> EntityAttributeProvider.from(EnderMan::createAttributes)
		.add(Attributes.MAX_HEALTH, 120.0D)
		.add(Attributes.MOVEMENT_SPEED, 0.3D)
		.add(Attributes.ATTACK_DAMAGE, 8.0D)
		.add(Attributes.ARMOR, 4.0D));

	public static final NFURegistry.Accessor<EntityAttributeProvider> HMAG_HORNET = 
		ATTRIBUTE_PROVIDERS.register("hornet", () -> EntityAttributeProvider.monster()
		.add(Attributes.MAX_HEALTH, 60.0D)
		.add(Attributes.MOVEMENT_SPEED, 0.28D)
		.add(Attributes.ATTACK_DAMAGE, 5.0D)
		.add(Attributes.FOLLOW_RANGE, 24.0D));

	public static final NFURegistry.Accessor<EntityAttributeProvider> HMAG_NECROTIC_REAPER = 
		ATTRIBUTE_PROVIDERS.register("necrotic_reaper", () -> EntityAttributeProvider.monster()
		.add(Attributes.MAX_HEALTH, 60.0D)
		.add(Attributes.MOVEMENT_SPEED, 0.31D)
		.add(Attributes.ATTACK_DAMAGE, 9.0D)
		.add(Attributes.ARMOR, 5.0D)
		.add(Attributes.KNOCKBACK_RESISTANCE, 0.25D)
		.add(Attributes.FOLLOW_RANGE, 24.0D));

	public static final NFURegistry.Accessor<EntityAttributeProvider> HMAG_GHASTLY_SEEKER = 
		ATTRIBUTE_PROVIDERS.register("ghastly_seeker", () -> EntityAttributeProvider.monster()
		.add(Attributes.MAX_HEALTH, 60.0D)
		.add(Attributes.ARMOR, 2.0D)
		.add(Attributes.ATTACK_DAMAGE, 0D)
		.add(Attributes.FOLLOW_RANGE, 64.0D));

	public static final NFURegistry.Accessor<EntityAttributeProvider> HMAG_BANSHEE = 
		ATTRIBUTE_PROVIDERS.register("banshee", () -> EntityAttributeProvider.monster()
		.add(Attributes.MAX_HEALTH, 40.0D)
		.add(Attributes.MOVEMENT_SPEED, 0.24D)
		.add(Attributes.ATTACK_DAMAGE, 6.0D)
		.add(Attributes.KNOCKBACK_RESISTANCE, 0.25D)
		.add(Attributes.FOLLOW_RANGE, 24.0D));

	public static final NFURegistry.Accessor<EntityAttributeProvider> HMAG_KOBOLD = 
		ATTRIBUTE_PROVIDERS.register("kobold", () -> EntityAttributeProvider.monster()
		.add(Attributes.MAX_HEALTH, 40.0D)
		.add(Attributes.MOVEMENT_SPEED, 0.3D)
		.add(Attributes.ATTACK_DAMAGE, 6.0D)
		.add(Attributes.ARMOR, 2.0D)
		.add(Attributes.KNOCKBACK_RESISTANCE, 0.25D)
		.add(Attributes.FOLLOW_RANGE, 20.0D));

	public static final NFURegistry.Accessor<EntityAttributeProvider> HMAG_IMP = 
		ATTRIBUTE_PROVIDERS.register("imp", () -> EntityAttributeProvider.monster()
		.add(Attributes.MAX_HEALTH, 40.0D)
		.add(Attributes.MOVEMENT_SPEED, 0.265D)
		.add(Attributes.ATTACK_DAMAGE, 7.0D)
		.add(Attributes.ARMOR, 2.0D)
		.add(Attributes.KNOCKBACK_RESISTANCE, 0.5D));

	public static final NFURegistry.Accessor<EntityAttributeProvider> HMAG_HARPY = 
		ATTRIBUTE_PROVIDERS.register("harpy", () -> EntityAttributeProvider.monster()
		.add(Attributes.MAX_HEALTH, 40.0D)
		.add(Attributes.MOVEMENT_SPEED, 0.295D)
		.add(Attributes.ATTACK_DAMAGE, 7.0D)
		.add(Attributes.ATTACK_KNOCKBACK, 0.5D)
		.add(Attributes.FOLLOW_RANGE, 20.0D)
		.add(ForgeMod.STEP_HEIGHT_ADDITION.get(), 1.5D));

	public static final NFURegistry.Accessor<EntityAttributeProvider> HMAG_SNOW_CANINE = 
		ATTRIBUTE_PROVIDERS.register("snow_canine", () -> EntityAttributeProvider.monster()
		.add(Attributes.MAX_HEALTH, 40.0D)
		.add(Attributes.MOVEMENT_SPEED, 0.325D)
		.add(Attributes.ATTACK_DAMAGE, 7.0D)
		.add(Attributes.ATTACK_KNOCKBACK, 0.5D)
		.add(Attributes.ARMOR, 2.0D)
		.add(Attributes.KNOCKBACK_RESISTANCE, 0.25D));

	public static final NFURegistry.Accessor<EntityAttributeProvider> HMAG_SLIME_GIRL = 
		ATTRIBUTE_PROVIDERS.register("slime_girl", () -> EntityAttributeProvider.monster()
		.add(Attributes.MAX_HEALTH, 60.0D)
		.add(Attributes.MOVEMENT_SPEED, 0.19D)
		.add(Attributes.ATTACK_DAMAGE, 7.0D)
		.add(Attributes.ARMOR, 8.0D)
		.add(Attributes.KNOCKBACK_RESISTANCE, 0.5D));

	public static final NFURegistry.Accessor<EntityAttributeProvider> HMAG_DULLAHAN = 
		ATTRIBUTE_PROVIDERS.register("dullahan", () -> EntityAttributeProvider.monster()
		.add(Attributes.MAX_HEALTH, 60.0D)
		.add(Attributes.MOVEMENT_SPEED, 0.31D)
		.add(Attributes.ATTACK_DAMAGE, 6.0D)
		.add(Attributes.ARMOR, 5.0D)
		.add(Attributes.KNOCKBACK_RESISTANCE, 0.5D)
		.add(Attributes.FOLLOW_RANGE, 20.0D));

	public static final NFURegistry.Accessor<EntityAttributeProvider> HMAG_JIANGSHI = 
		ATTRIBUTE_PROVIDERS.register("jiangshi", () -> EntityAttributeProvider.monster()
		.add(Attributes.MAX_HEALTH, 40.0D)
		.add(Attributes.MOVEMENT_SPEED, 0.19D)
		.add(Attributes.ATTACK_DAMAGE, 6.0D)
		.add(Attributes.ATTACK_KNOCKBACK, 0.5D)
		.add(Attributes.ARMOR, 4.0D)
		.add(Attributes.KNOCKBACK_RESISTANCE, 0.5D)
		.add(Attributes.FOLLOW_RANGE, 24.0D));

	public static final NFURegistry.Accessor<EntityAttributeProvider> HMAG_DODOMEKI = 
		ATTRIBUTE_PROVIDERS.register("dodomeki", () -> EntityAttributeProvider.monster()
		.add(Attributes.MAX_HEALTH, 40.0D)
		.add(Attributes.MOVEMENT_SPEED, 0.24D)
		.add(Attributes.ATTACK_DAMAGE, 7.0D)
		.add(Attributes.ARMOR, 5.0D)
		.add(Attributes.KNOCKBACK_RESISTANCE, 0.5D)
		.add(Attributes.FOLLOW_RANGE, 20.0D));

	public static final NFURegistry.Accessor<EntityAttributeProvider> HMAG_ALRAUNE = 
		ATTRIBUTE_PROVIDERS.register("alraune", () -> EntityAttributeProvider.monster()
		.add(Attributes.MAX_HEALTH, 60.0D)
		.add(Attributes.MOVEMENT_SPEED, 0.12D)
		.add(Attributes.ATTACK_DAMAGE, 6.0D)
		.add(Attributes.ARMOR, 5.0D)
		.add(Attributes.KNOCKBACK_RESISTANCE, 0.98D));

	public static final NFURegistry.Accessor<EntityAttributeProvider> HMAG_GLARYAD = 
		ATTRIBUTE_PROVIDERS.register("glaryad", () -> EntityAttributeProvider.monster()
		.add(Attributes.MAX_HEALTH, 40.0D)
		.add(Attributes.MOVEMENT_SPEED, 0.23D)
		.add(Attributes.ATTACK_DAMAGE, 7.0D)
		.add(Attributes.ARMOR, 2.0D)
		.add(Attributes.KNOCKBACK_RESISTANCE, 0.5D));

	public static final NFURegistry.Accessor<EntityAttributeProvider> HMAG_CRIMSON_SLAUGHTERER = 
		ATTRIBUTE_PROVIDERS.register("crimson_slaughterer", () -> EntityAttributeProvider.monster()
		.add(Attributes.MAX_HEALTH, 80.0D)
		.add(Attributes.MOVEMENT_SPEED, 0.33D)
		.add(Attributes.ATTACK_DAMAGE, 12.0D)
		.add(Attributes.ATTACK_KNOCKBACK, 0.5D)
		.add(Attributes.ARMOR, 5.0D)
		.add(Attributes.KNOCKBACK_RESISTANCE, 0.75D)
		.add(ForgeMod.STEP_HEIGHT_ADDITION.get(), 2.0D));

	public static final NFURegistry.Accessor<EntityAttributeProvider> HMAG_CURSED_DOLL = 
		ATTRIBUTE_PROVIDERS.register("cursed_doll", () -> EntityAttributeProvider.monster()
		.add(Attributes.MAX_HEALTH, 40.0D)
		.add(Attributes.MOVEMENT_SPEED, 0.29D)
		.add(Attributes.ATTACK_DAMAGE, 4.0D)
		.add(Attributes.ATTACK_KNOCKBACK, 1.0D)
		.add(Attributes.KNOCKBACK_RESISTANCE, 0.25D)
		.add(ForgeMod.STEP_HEIGHT_ADDITION.get(), 1.5D));

	public static final NFURegistry.Accessor<EntityAttributeProvider> HMAG_REDCAP = 
		ATTRIBUTE_PROVIDERS.register("redcap", () -> EntityAttributeProvider.monster()
		.add(Attributes.MAX_HEALTH, 40.0D)
		.add(Attributes.MOVEMENT_SPEED, 0.31D)
		.add(Attributes.ATTACK_DAMAGE, 4.0D)
		.add(Attributes.FOLLOW_RANGE, 24.0D));

	public static final NFURegistry.Accessor<EntityAttributeProvider> HMAG_JACK_FROST = 
		ATTRIBUTE_PROVIDERS.register("jack_frost", () -> EntityAttributeProvider.monster()
		.add(Attributes.MAX_HEALTH, 60.0D)
		.add(Attributes.MOVEMENT_SPEED, 0.24D)
		.add(Attributes.ATTACK_DAMAGE, 0d)
		.add(Attributes.ARMOR, 2.0D));

	public static final NFURegistry.Accessor<EntityAttributeProvider> HMAG_MELTY_MONSTER = 
		ATTRIBUTE_PROVIDERS.register("melty_monster", () -> EntityAttributeProvider.monster()
		.add(Attributes.MAX_HEALTH, 25.0D)
		.add(Attributes.MOVEMENT_SPEED, 0.18D)
		.add(Attributes.ATTACK_DAMAGE, 0D));

	public static final NFURegistry.Accessor<EntityAttributeProvider> HMAG_NIGHTWALKER = 
		ATTRIBUTE_PROVIDERS.register("nightwalker", () -> EntityAttributeProvider.monster()
		.add(Attributes.MAX_HEALTH, 40.0D)
		.add(Attributes.MOVEMENT_SPEED, 0.22D)
		.add(Attributes.ARMOR, 2.0D)
		.add(Attributes.KNOCKBACK_RESISTANCE, 0.5D)
		.add(ForgeMod.STEP_HEIGHT_ADDITION.get(), 1.0D));


	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(NFFGirlsEntityTypes.HMAG_ZOMBIE_GIRL.get(), NFFGirlsEntityAttributes.HMAG_ZOMBIE_GIRL.get().get().build());
		event.put(NFFGirlsEntityTypes.HMAG_HUSK_GIRL.get(), NFFGirlsEntityAttributes.HMAG_ZOMBIE_GIRL.get().get().build());
		event.put(NFFGirlsEntityTypes.HMAG_DROWNED_GIRL.get(), NFFGirlsEntityAttributes.HMAG_DROWNED_GIRL.get().get().build());
		event.put(NFFGirlsEntityTypes.HMAG_SKELETON_GIRL.get(), NFFGirlsEntityAttributes.HMAG_SKELETON_GIRL.get().get().build());
		event.put(NFFGirlsEntityTypes.HMAG_STRAY_GIRL.get(), NFFGirlsEntityAttributes.HMAG_SKELETON_GIRL.get().get().build());
		event.put(NFFGirlsEntityTypes.HMAG_WITHER_SKELETON_GIRL.get(), NFFGirlsEntityAttributes.HMAG_WITHER_SKELETON_GIRL.get().get().build());
		event.put(NFFGirlsEntityTypes.HMAG_CREEPER_GIRL.get(), NFFGirlsEntityAttributes.HMAG_CREEPER_GIRL.get().get().build());
		event.put(NFFGirlsEntityTypes.HMAG_ENDER_EXECUTOR.get(), NFFGirlsEntityAttributes.HMAG_ENDER_EXECUTOR.get().get().build());
		event.put(NFFGirlsEntityTypes.HMAG_HORNET.get(), NFFGirlsEntityAttributes.HMAG_HORNET.get().get().build());
		event.put(NFFGirlsEntityTypes.HMAG_NECROTIC_REAPER.get(), NFFGirlsEntityAttributes.HMAG_NECROTIC_REAPER.get().get().build());
		event.put(NFFGirlsEntityTypes.HMAG_GHASTLY_SEEKER.get(), NFFGirlsEntityAttributes.HMAG_GHASTLY_SEEKER.get().get().build());
		event.put(NFFGirlsEntityTypes.HMAG_BANSHEE.get(), NFFGirlsEntityAttributes.HMAG_BANSHEE.get().get().build());
		event.put(NFFGirlsEntityTypes.HMAG_KOBOLD.get(), NFFGirlsEntityAttributes.HMAG_KOBOLD.get().get().build());
		event.put(NFFGirlsEntityTypes.HMAG_IMP.get(), NFFGirlsEntityAttributes.HMAG_IMP.get().get().build());
		event.put(NFFGirlsEntityTypes.HMAG_HARPY.get(), NFFGirlsEntityAttributes.HMAG_HARPY.get().get().build());
		event.put(NFFGirlsEntityTypes.HMAG_SNOW_CANINE.get(), NFFGirlsEntityAttributes.HMAG_SNOW_CANINE.get().get().build());
		event.put(NFFGirlsEntityTypes.HMAG_SLIME_GIRL.get(), NFFGirlsEntityAttributes.HMAG_SLIME_GIRL.get().get().build());
		event.put(NFFGirlsEntityTypes.HMAG_DULLAHAN.get(), NFFGirlsEntityAttributes.HMAG_DULLAHAN.get().get().build());
		event.put(NFFGirlsEntityTypes.HMAG_DODOMEKI.get(), NFFGirlsEntityAttributes.HMAG_DODOMEKI.get().get().build());
		event.put(NFFGirlsEntityTypes.HMAG_ALRAUNE.get(), NFFGirlsEntityAttributes.HMAG_ALRAUNE.get().get().build());
		event.put(NFFGirlsEntityTypes.HMAG_GLARYAD.get(), NFFGirlsEntityAttributes.HMAG_GLARYAD.get().get().build());
		event.put(NFFGirlsEntityTypes.HMAG_CRIMSON_SLAUGHTERER.get(), NFFGirlsEntityAttributes.HMAG_CRIMSON_SLAUGHTERER.get().get().build());
		event.put(NFFGirlsEntityTypes.HMAG_CURSED_DOLL.get(), NFFGirlsEntityAttributes.HMAG_CURSED_DOLL.get().get().build());
		event.put(NFFGirlsEntityTypes.HMAG_REDCAP.get(), NFFGirlsEntityAttributes.HMAG_REDCAP.get().get().build());
		event.put(NFFGirlsEntityTypes.HMAG_JACK_FROST.get(), NFFGirlsEntityAttributes.HMAG_JACK_FROST.get().get().build());
		event.put(NFFGirlsEntityTypes.HMAG_MELTY_MONSTER.get(), NFFGirlsEntityAttributes.HMAG_MELTY_MONSTER.get().get().build());
		event.put(NFFGirlsEntityTypes.HMAG_JIANGSHI.get(), NFFGirlsEntityAttributes.HMAG_JIANGSHI.get().get().build());
		event.put(NFFGirlsEntityTypes.HMAG_NIGHTWALKER.get(), NFFGirlsEntityAttributes.HMAG_NIGHTWALKER.get().get().build());
	}

		
}
