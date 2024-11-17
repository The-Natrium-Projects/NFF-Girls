package net.sodiumzh.nff.girls.registry;

import java.util.HashMap;
import java.util.function.Supplier;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Monster;
import net.minecraftforge.common.ForgeMod;

public class NFFGirlsEntityAttributes
{
	
	public static final HashMap<EntityType<? extends LivingEntity>, Supplier<AttributeSupplier.Builder>> REGISTRY = new HashMap<>();

	// ================================================================================================= //
	
	// Presets
	
	protected static final Supplier<AttributeSupplier.Builder> VANILLA_MONSTER_COMMON_ATTRIBUTES = Monster::createMonsterAttributes;
	
	protected static final Supplier<AttributeSupplier.Builder> VANILLA_ZOMBIE_ATTRIBUTES = () ->
		VANILLA_MONSTER_COMMON_ATTRIBUTES.get()
		.add(Attributes.FOLLOW_RANGE, 35.0D)
		.add(Attributes.MOVEMENT_SPEED, 0.23D)
		.add(Attributes.ATTACK_DAMAGE, 3.0D)
		.add(Attributes.ARMOR, 2.0D);

	protected static final Supplier<AttributeSupplier.Builder> VANILLA_ENDER_MAN_ATTRIBUTES = () ->
		VANILLA_MONSTER_COMMON_ATTRIBUTES.get()
		.add(Attributes.MAX_HEALTH, 40.0D)
		.add(Attributes.MOVEMENT_SPEED, 0.3D)
		.add(Attributes.ATTACK_DAMAGE, 7.0D)
		.add(Attributes.FOLLOW_RANGE, 64.0D);
	
	// ================================================================================================= //
		
	// Registries
		
	public static final Supplier<AttributeSupplier.Builder> HMAG_ZOMBIE_GIRL_ATTRIBUTES = register(() ->
		VANILLA_MONSTER_COMMON_ATTRIBUTES.get()
		.add(Attributes.MAX_HEALTH, 30.0D)
		.add(Attributes.MOVEMENT_SPEED, 0.28D)
		.add(Attributes.ATTACK_DAMAGE, 4.0D)
		.add(Attributes.ARMOR, 4.0D)
		.add(Attributes.FOLLOW_RANGE, 35.0D)
		.add(Attributes.SPAWN_REINFORCEMENTS_CHANCE),
		NFFGirlsEntityTypes.HMAG_ZOMBIE_GIRL.get()); 
		
	public static final Supplier<AttributeSupplier.Builder> HMAG_HUSK_GIRL_ATTRIBUTES = register(() ->
		VANILLA_MONSTER_COMMON_ATTRIBUTES.get()
		.add(Attributes.MAX_HEALTH, 30.0D)
		.add(Attributes.MOVEMENT_SPEED, 0.28D)
		.add(Attributes.ATTACK_DAMAGE, 4.0D)
		.add(Attributes.ARMOR, 5.0D)
		.add(Attributes.FOLLOW_RANGE, 35.0D)
		.add(Attributes.SPAWN_REINFORCEMENTS_CHANCE),
		NFFGirlsEntityTypes.HMAG_HUSK_GIRL.get());

	public static final Supplier<AttributeSupplier.Builder> HMAG_DROWNED_GIRL_ATTRIBUTES = register(() ->
		VANILLA_MONSTER_COMMON_ATTRIBUTES.get()
		.add(Attributes.MAX_HEALTH, 30.0D)
		.add(Attributes.MOVEMENT_SPEED, 0.245D)
		.add(Attributes.ATTACK_DAMAGE, 4.0D)
		.add(Attributes.ARMOR, 3.0D)
		.add(Attributes.FOLLOW_RANGE, 35.0D)
		.add(Attributes.SPAWN_REINFORCEMENTS_CHANCE),
		NFFGirlsEntityTypes.HMAG_DROWNED_GIRL.get());

	public static final Supplier<AttributeSupplier.Builder> HMAG_SKELETON_GIRL_ATTRIBUTES = register(() ->	
		VANILLA_MONSTER_COMMON_ATTRIBUTES.get()
		.add(Attributes.MAX_HEALTH, 30.0D)
		.add(Attributes.MOVEMENT_SPEED, 0.25D)
		.add(Attributes.ATTACK_DAMAGE, 3.25D)
		.add(Attributes.ARMOR, 1.0D)
		.add(Attributes.FOLLOW_RANGE, 64.0D),
		NFFGirlsEntityTypes.HMAG_SKELETON_GIRL.get());

	public static final Supplier<AttributeSupplier.Builder> HMAG_CREEPER_GIRL_ATTRIBUTES = register(() ->
		VANILLA_MONSTER_COMMON_ATTRIBUTES.get()
		.add(Attributes.MAX_HEALTH, 30.0D)
		.add(Attributes.MOVEMENT_SPEED, 0.3D)
		.add(Attributes.KNOCKBACK_RESISTANCE, 0.25D)
		.add(Attributes.ATTACK_DAMAGE, 0),
		NFFGirlsEntityTypes.HMAG_CREEPER_GIRL.get());

	public static final Supplier<AttributeSupplier.Builder> HMAG_ENDER_EXECUTOR_ATTRIBUTES = register(() ->
		EnderMan.createAttributes()
		.add(Attributes.MAX_HEALTH, 120.0D)
		.add(Attributes.MOVEMENT_SPEED, 0.3D)
		.add(Attributes.ATTACK_DAMAGE, 8.0D)
		.add(Attributes.ARMOR, 4.0D),
		NFFGirlsEntityTypes.HMAG_ENDER_EXECUTOR.get());
		
	
	public static final Supplier<AttributeSupplier.Builder> HMAG_STRAY_GIRL_ATTRIBUTES = register(() ->
		VANILLA_MONSTER_COMMON_ATTRIBUTES.get()
		.add(Attributes.MAX_HEALTH, 30.0D)
		.add(Attributes.MOVEMENT_SPEED, 0.25D)
		.add(Attributes.ATTACK_DAMAGE, 3.25D)
		.add(Attributes.ARMOR, 1.0D),
		NFFGirlsEntityTypes.HMAG_STRAY_GIRL.get());

	public static final Supplier<AttributeSupplier.Builder> HMAG_WITHER_SKELETON_GIRL_ATTRIBUTES = register(() ->
		VANILLA_MONSTER_COMMON_ATTRIBUTES.get()
		.add(Attributes.MAX_HEALTH, 36.0D)
		.add(Attributes.MOVEMENT_SPEED, 0.26D)
		.add(Attributes.ATTACK_DAMAGE, 4.5D)
		.add(Attributes.ARMOR, 4.0D)
		.add(Attributes.KNOCKBACK_RESISTANCE, 0.25D),
		NFFGirlsEntityTypes.HMAG_WITHER_SKELETON_GIRL.get());

	public static final Supplier<AttributeSupplier.Builder> HMAG_HORNET_ATTRIBUTES = register(() ->
		VANILLA_MONSTER_COMMON_ATTRIBUTES.get()
		.add(Attributes.MAX_HEALTH, 60.0D)
		.add(Attributes.MOVEMENT_SPEED, 0.28D)
		.add(Attributes.ATTACK_DAMAGE, 5.0D)
		.add(Attributes.FOLLOW_RANGE, 24.0D),
		NFFGirlsEntityTypes.HMAG_HORNET.get());
	
	public static final Supplier<AttributeSupplier.Builder> HMAG_NECROTIC_REAPER_ATTRIBUTES = register(() ->
		VANILLA_MONSTER_COMMON_ATTRIBUTES.get()
		.add(Attributes.MAX_HEALTH, 60.0D)
		.add(Attributes.MOVEMENT_SPEED, 0.31D)
		.add(Attributes.ATTACK_DAMAGE, 9.0D)
		.add(Attributes.ARMOR, 5.0D)
		.add(Attributes.KNOCKBACK_RESISTANCE, 0.25D)
		.add(Attributes.FOLLOW_RANGE, 24.0D),
		NFFGirlsEntityTypes.HMAG_NECROTIC_REAPER.get());
	
	public static final Supplier<AttributeSupplier.Builder> HMAG_GHASTLY_SEEKER_ATTRIBUTES = register(() ->
		VANILLA_MONSTER_COMMON_ATTRIBUTES.get()
		.add(Attributes.MAX_HEALTH, 60.0D)
		.add(Attributes.ARMOR, 2.0D)
		.add(Attributes.ATTACK_DAMAGE, 0)
		.add(Attributes.FOLLOW_RANGE, 64.0D),
		NFFGirlsEntityTypes.HMAG_GHASTLY_SEEKER.get());
	
	public static final Supplier<AttributeSupplier.Builder> HMAG_BANSHEE_ATTRIBUTES = register(() ->
		VANILLA_MONSTER_COMMON_ATTRIBUTES.get()
		.add(Attributes.MAX_HEALTH, 40.0D)
		.add(Attributes.MOVEMENT_SPEED, 0.24D)
		.add(Attributes.ATTACK_DAMAGE, 6.0D)
		.add(Attributes.KNOCKBACK_RESISTANCE, 0.25D)
		.add(Attributes.FOLLOW_RANGE, 24.0D),
		NFFGirlsEntityTypes.HMAG_BANSHEE.get());
	
	public static final Supplier<AttributeSupplier.Builder> HMAG_KOBOLD_ATTRIBUTES = register(() ->
		VANILLA_MONSTER_COMMON_ATTRIBUTES.get()
		.add(Attributes.MAX_HEALTH, 40.0D)
		.add(Attributes.MOVEMENT_SPEED, 0.3D)
		.add(Attributes.ATTACK_DAMAGE, 6.0D)
		.add(Attributes.ARMOR, 2.0D)
		.add(Attributes.KNOCKBACK_RESISTANCE, 0.25D)
		.add(Attributes.FOLLOW_RANGE, 20.0D),
		NFFGirlsEntityTypes.HMAG_KOBOLD.get());

	public static final Supplier<AttributeSupplier.Builder> HMAG_IMP_ATTRIBUTES = register(() ->
		VANILLA_MONSTER_COMMON_ATTRIBUTES.get()
		.add(Attributes.MAX_HEALTH, 40.0D)
		.add(Attributes.MOVEMENT_SPEED, 0.265D)
		.add(Attributes.ATTACK_DAMAGE, 7.0D)
		.add(Attributes.ARMOR, 2.0D)
		.add(Attributes.KNOCKBACK_RESISTANCE, 0.5D),
		NFFGirlsEntityTypes.HMAG_IMP.get());
	
	public static final Supplier<AttributeSupplier.Builder> HMAG_HARPY_ATTRIBUTES = register(() ->
		VANILLA_MONSTER_COMMON_ATTRIBUTES.get()
		.add(Attributes.MAX_HEALTH, 40.0D)
		.add(Attributes.MOVEMENT_SPEED, 0.295D)
		.add(Attributes.ATTACK_DAMAGE, 7.0D)
		.add(Attributes.ATTACK_KNOCKBACK, 0.5D)
		.add(Attributes.FOLLOW_RANGE, 20.0D)
		.add(ForgeMod.STEP_HEIGHT_ADDITION.get(), 1.5D),
		NFFGirlsEntityTypes.HMAG_HARPY.get());
	
	public static final Supplier<AttributeSupplier.Builder> HMAG_SNOW_CANINE_ATTRIBUTES = register(() ->
		VANILLA_MONSTER_COMMON_ATTRIBUTES.get()
		.add(Attributes.MAX_HEALTH, 40.0D)
		.add(Attributes.MOVEMENT_SPEED, 0.325D)
		.add(Attributes.ATTACK_DAMAGE, 7.0D)
		.add(Attributes.ATTACK_KNOCKBACK, 0.5D)
		.add(Attributes.ARMOR, 2.0D)
		.add(Attributes.KNOCKBACK_RESISTANCE, 0.25D),
		NFFGirlsEntityTypes.HMAG_SNOW_CANINE.get());
	
	public static final Supplier<AttributeSupplier.Builder> HMAG_SLIME_GIRL_ATTRIBUTES = register(() ->
		VANILLA_MONSTER_COMMON_ATTRIBUTES.get()
		.add(Attributes.MAX_HEALTH, 60.0D)
		.add(Attributes.MOVEMENT_SPEED, 0.19D)
		.add(Attributes.ATTACK_DAMAGE, 7.0D)
		.add(Attributes.ARMOR, 8.0D)
		.add(Attributes.KNOCKBACK_RESISTANCE, 0.5D),
		NFFGirlsEntityTypes.HMAG_SLIME_GIRL.get());

	public static final Supplier<AttributeSupplier.Builder> HMAG_DULLAHAN_ATTRIBUTES = register(() ->
		VANILLA_MONSTER_COMMON_ATTRIBUTES.get()
		.add(Attributes.MAX_HEALTH, 60.0D)
		.add(Attributes.MOVEMENT_SPEED, 0.31D)
		.add(Attributes.ATTACK_DAMAGE, 6.0D)
		.add(Attributes.ARMOR, 5.0D)
		.add(Attributes.KNOCKBACK_RESISTANCE, 0.5D)
		.add(Attributes.FOLLOW_RANGE, 20.0D),
		NFFGirlsEntityTypes.HMAG_DULLAHAN.get());
	
	public static final Supplier<AttributeSupplier.Builder> HMAG_JIANGSHI_ATTRIBUTES = register(() ->
		VANILLA_MONSTER_COMMON_ATTRIBUTES.get()
		.add(Attributes.MAX_HEALTH, 40.0D)
		.add(Attributes.MOVEMENT_SPEED, 0.19D)
		.add(Attributes.ATTACK_DAMAGE, 6.0D)
		.add(Attributes.ATTACK_KNOCKBACK, 0.5D)
		.add(Attributes.ARMOR, 4.0D)
		.add(Attributes.KNOCKBACK_RESISTANCE, 0.5D)
		.add(Attributes.FOLLOW_RANGE, 24.0D),
		NFFGirlsEntityTypes.HMAG_JIANGSHI.get());

	public static final Supplier<AttributeSupplier.Builder> HMAG_DODOMEKI_ATTRIBUTES = register(() ->
		VANILLA_MONSTER_COMMON_ATTRIBUTES.get()
		.add(Attributes.MAX_HEALTH, 40.0D)
		.add(Attributes.MOVEMENT_SPEED, 0.24D)
		.add(Attributes.ATTACK_DAMAGE, 7.0D)
		.add(Attributes.ARMOR, 5.0D)
		.add(Attributes.KNOCKBACK_RESISTANCE, 0.5D)
		.add(Attributes.FOLLOW_RANGE, 20.0D),
		NFFGirlsEntityTypes.HMAG_DODOMEKI.get());
		
	public static final Supplier<AttributeSupplier.Builder> HMAG_ALRAUNE_ATTRIBUTES = register(() ->
		VANILLA_MONSTER_COMMON_ATTRIBUTES.get()
		.add(Attributes.MAX_HEALTH, 60.0D)
		.add(Attributes.MOVEMENT_SPEED, 0.12D)
		.add(Attributes.ATTACK_DAMAGE, 6.0D)
		.add(Attributes.ARMOR, 5.0D)
		.add(Attributes.KNOCKBACK_RESISTANCE, 0.98D),
		NFFGirlsEntityTypes.HMAG_ALRAUNE.get());
	
	public static final Supplier<AttributeSupplier.Builder> HMAG_GLARYAD_ATTRIBUTES = register(() ->
		VANILLA_MONSTER_COMMON_ATTRIBUTES.get()
		.add(Attributes.MAX_HEALTH, 40.0D)
		.add(Attributes.MOVEMENT_SPEED, 0.23D)
		.add(Attributes.ATTACK_DAMAGE, 7.0D)
		.add(Attributes.ARMOR, 2.0D)
		.add(Attributes.KNOCKBACK_RESISTANCE, 0.5D),
		NFFGirlsEntityTypes.HMAG_GLARYAD.get());

	public static final Supplier<AttributeSupplier.Builder> HMAG_CRIMSON_SLAUGHTERER_ATTRIBUTES = register(() ->
		VANILLA_MONSTER_COMMON_ATTRIBUTES.get()
		.add(Attributes.MAX_HEALTH, 80.0D)
		.add(Attributes.MOVEMENT_SPEED, 0.33D)
		.add(Attributes.ATTACK_DAMAGE, 12.0D)
		.add(Attributes.ATTACK_KNOCKBACK, 0.5D)
		.add(Attributes.ARMOR, 5.0D)
		.add(Attributes.KNOCKBACK_RESISTANCE, 0.75D)
		.add(ForgeMod.STEP_HEIGHT_ADDITION.get(), 2.0D),
		NFFGirlsEntityTypes.HMAG_CRIMSON_SLAUGHTERER.get());

	public static final Supplier<AttributeSupplier.Builder> HMAG_CURSED_DOLL_ATTRIBUTES = register(() ->
		VANILLA_MONSTER_COMMON_ATTRIBUTES.get()
		.add(Attributes.MAX_HEALTH, 40.0D)
		.add(Attributes.MOVEMENT_SPEED, 0.29D)
		.add(Attributes.ATTACK_DAMAGE, 4.0D)
		.add(Attributes.ATTACK_KNOCKBACK, 1.0D)
		.add(Attributes.KNOCKBACK_RESISTANCE, 0.25D)
		.add(ForgeMod.STEP_HEIGHT_ADDITION.get(), 1.5D),
		NFFGirlsEntityTypes.HMAG_CURSED_DOLL.get());
	
	public static final Supplier<AttributeSupplier.Builder> HMAG_REDCAP_ATTRIBUTES = register(() ->
		VANILLA_MONSTER_COMMON_ATTRIBUTES.get()
		.add(Attributes.MAX_HEALTH, 40.0D)
		.add(Attributes.MOVEMENT_SPEED, 0.31D)
		.add(Attributes.ATTACK_DAMAGE, 4.0D)
		.add(Attributes.FOLLOW_RANGE, 24.0D),
		NFFGirlsEntityTypes.HMAG_REDCAP.get());
	
	public static final Supplier<AttributeSupplier.Builder> HMAG_JACK_FROST_ATTRIBUTES = register(() ->
		VANILLA_MONSTER_COMMON_ATTRIBUTES.get()
		.add(Attributes.MAX_HEALTH, 60.0D)
		.add(Attributes.MOVEMENT_SPEED, 0.24D)
		.add(Attributes.ATTACK_DAMAGE, 0d)
		.add(Attributes.ARMOR, 2.0D),
		NFFGirlsEntityTypes.HMAG_JACK_FROST.get());
	
	public static final Supplier<AttributeSupplier.Builder> HMAG_MELTY_MONSTER_ATTRIBUTES = register(() ->
		VANILLA_MONSTER_COMMON_ATTRIBUTES.get()
		.add(Attributes.MAX_HEALTH, 25.0D)
		.add(Attributes.MOVEMENT_SPEED, 0.18D)
		.add(Attributes.ATTACK_DAMAGE, 0d),
		NFFGirlsEntityTypes.HMAG_MELTY_MONSTER.get());

	public static final Supplier<AttributeSupplier.Builder> HMAG_NIGHTWALKER_ATTRIBUTES = register(() ->
		VANILLA_MONSTER_COMMON_ATTRIBUTES.get()
		.add(Attributes.MAX_HEALTH, 40.0D)
		.add(Attributes.MOVEMENT_SPEED, 0.22D)
		.add(Attributes.ARMOR, 2.0D)
		.add(Attributes.KNOCKBACK_RESISTANCE, 0.5D)
		.add(ForgeMod.STEP_HEIGHT_ADDITION.get(), 1.0D),
		NFFGirlsEntityTypes.HMAG_NIGHTWALKER.get());
	
	/*	
	public static final Supplier<AttributeSupplier.Builder> HMAG__ATTRIBUTES = register(() ->
	*/	

	// ================================================================================================= //
		
	protected static Supplier<AttributeSupplier.Builder> register(Supplier<AttributeSupplier.Builder> builderSupplier, EntityType<? extends LivingEntity> type)
	{
		REGISTRY.put(type, builderSupplier);
		return builderSupplier;
	}

		
}
