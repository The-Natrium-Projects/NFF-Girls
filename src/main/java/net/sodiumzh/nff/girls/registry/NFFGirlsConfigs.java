package net.sodiumzh.nff.girls.registry;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.config.ModConfigEvent;

public class NFFGirlsConfigs
{
	protected static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static ForgeConfigSpec CONFIG;

	// Sound
	public static ForgeConfigSpec.DoubleValue AMBIENT_SOUND_CHANCE;
	public static ForgeConfigSpec.BooleanValue ZOMBIES_USE_GIRL_SOUND;
	public static ForgeConfigSpec.BooleanValue SKELETONS_USE_GIRL_SOUND;
	public static ForgeConfigSpec.BooleanValue GHASTS_USE_GIRL_SOUND;
	public static ForgeConfigSpec.BooleanValue ZOMBIES_NO_AMBIENT_SOUND;
	public static ForgeConfigSpec.BooleanValue SKELETONS_NO_AMBIENT_SOUND;
	public static ForgeConfigSpec.BooleanValue GHASTS_NO_AMBIENT_SOUND;

	// Combat
	public static ForgeConfigSpec.BooleanValue ENABLE_FRIENDLY_DAMAGE;
	public static ForgeConfigSpec.BooleanValue ENABLE_PROJECTILE_FRIENDLY_DAMAGE;
	public static ForgeConfigSpec.DoubleValue MAX_HEALTH_BOOST_BY_LEVEL;
	public static ForgeConfigSpec.DoubleValue MAX_ATK_BOOST_BY_LEVEL;
	public static ForgeConfigSpec.DoubleValue HEALTH_BOOST_PER_LEVEL;
	public static ForgeConfigSpec.DoubleValue ATK_BOOST_PER_LEVEL;
	
	// Interaction
	public static ForgeConfigSpec.BooleanValue ALLOW_VANILLA_CONVERSION;
	public static ForgeConfigSpec.BooleanValue ALLOW_REVERSE_CONVERSION;
	public static ForgeConfigSpec.BooleanValue ALL_ZOMBIE_GIRLS_CAN_CONVERT_TO_HUSKS;
	public static ForgeConfigSpec.BooleanValue ALL_DROWNED_GIRLS_CAN_CONVERT_TO_ZOMBIES;
	public static ForgeConfigSpec.BooleanValue ALL_STRAY_GIRLS_CAN_CONVERT_TO_SKELETONS;
	
	// Baubles
	public static ForgeConfigSpec.DoubleValue BAUBLE_HEALTH_RECOVERY_SCALE;
	public static ForgeConfigSpec.DoubleValue BAUBLE_MAX_HP_BOOSTING_SCALE;
	public static ForgeConfigSpec.DoubleValue BAUBLE_ATK_BOOSTING_SCALE;
	public static ForgeConfigSpec.DoubleValue BAUBLE_ARMOR_BOOSTING_SCALE;
	
	static
	{
		BUILDER.push("sound");
		AMBIENT_SOUND_CHANCE = BUILDER.comment("Friendly mobs in NFF-Girls mods ambient sound frequency is multiplied by this number.")
				.defineInRange("ambientSoundChance", 1.0d, 0.0d, 1.0d);
		ZOMBIES_USE_GIRL_SOUND = BUILDER.comment("If true, friendly Zombie Girls and variants will use HMaG default girl sound.")
				.define("zombiesUseGirlSound", false);
		SKELETONS_USE_GIRL_SOUND = BUILDER.comment("If true, friendly Skeleton Girls and variants will use HMaG default girl sound.")
				.define("skeletonsUseGirlSound", false);
		GHASTS_USE_GIRL_SOUND = BUILDER.comment("If true, friendly Ghastly Seekers will use HMaG default girl sound.")
				.define("ghastsUseGirlSound", false);
		ZOMBIES_NO_AMBIENT_SOUND = BUILDER.comment("If true, friendly Zombie Girls and variants will not make ambient sound.")
				.define("zombiesNoAmbientSound", false);
		SKELETONS_NO_AMBIENT_SOUND = BUILDER.comment("If true, friendly Skeleton Girls and variants will not make ambient sound.")
				.define("skeletonsNoAmbientSound", false);
		GHASTS_NO_AMBIENT_SOUND = BUILDER.comment("If true, friendly Ghastly Seekers will not make ambient sound.")
				.define("ghastsNoAmbientSound", false);
		BUILDER.pop();
		
		BUILDER.push("combat");
		ENABLE_FRIENDLY_DAMAGE = BUILDER.comment("If true, players and owning NFF-Girls mobs can damage each other.")
			.define("enableFriendlyDamage", true);
		ENABLE_PROJECTILE_FRIENDLY_DAMAGE = BUILDER.comment("If true, projectiles from friendly mobs in NFF-Girls mods will hurt allies.")
				.define("enableProjectileFriendlyDamage", false);
		MAX_HEALTH_BOOST_BY_LEVEL = BUILDER.comment("Max (max HP) value increase by level upgrade. Zero = no limit.")
				.defineInRange("maxHealthIncreaseByLevel", 0, 0, 1e+6);
		MAX_ATK_BOOST_BY_LEVEL = BUILDER.comment("Max ATK value increase by level upgrade. Zero = no limit.")
				.defineInRange("maxAtkIncreaseByLevel", 0, 0, 1e+6);
		HEALTH_BOOST_PER_LEVEL = BUILDER.comment("Max health value increase on each level upgrade.")
				.defineInRange("maxHealthIncreasePerLevel", 1.0d, 0, 1e+6);
		ATK_BOOST_PER_LEVEL = BUILDER.comment("ATK health value increase on each level upgrade.")
				.defineInRange("atkIncreasePerLevel", 0.1d, 0, 1e+6);
		BUILDER.pop();
		
		BUILDER.push("interaction");
		ALLOW_VANILLA_CONVERSION = BUILDER.comment("Allow vanilla conversion if true, i.e. Husk -> Zombie -> Drowned; Skeleton -> Stray.")
				.define("allowVanillaConversion", true);
		ALLOW_REVERSE_CONVERSION = BUILDER.comment("Allow reverting vanilla conversion if true, i.e. Drowned -> Zombie -> Husk; Stray -> Skeleton.")
				.define("allowReverseConversion", true);
		ALL_ZOMBIE_GIRLS_CAN_CONVERT_TO_HUSKS = BUILDER.comment("If true, all friendly Zombie Girls can be converted to Husk Girls with Sponge, no matter if they're converted from Husk Girls.")
				.define("allZombieGirlsCanConvertToHusks", false);
		ALL_DROWNED_GIRLS_CAN_CONVERT_TO_ZOMBIES = BUILDER.comment("If true, all friendly Drowned Girls can be converted to Zombie Girls with Sponge, no matter if they're converted from Zombie Girls.")
				.define("allDrownedGirlsCanConvertToZombies", false);
		ALL_STRAY_GIRLS_CAN_CONVERT_TO_SKELETONS = BUILDER.comment("If true, all friendly Stray Girls can be converted to Skeleton Girls with Flint and Steel, no matter if they're converted from Skeleton Girls.")
				.define("allStrayGirlsCanConvertToSkeletons", false);
		BUILDER.pop();
		
		BUILDER.push("baubles");
		BAUBLE_HEALTH_RECOVERY_SCALE = BUILDER.comment("Bauble health recovery effect amount will be scaled with this value.")
				.defineInRange("baubleHealthRecoveryScale", 1.0d, 0.0d, 1e+6);
		BAUBLE_MAX_HP_BOOSTING_SCALE = BUILDER.comment("Bauble max HP increase will be scaled with this value.")
				.defineInRange("baubleMaxHPScale", 1.0d, 0.0d, 1e+6);
		BAUBLE_ATK_BOOSTING_SCALE = BUILDER.comment("Bauble ATK increase will be scaled with this value.")
				.defineInRange("baubleATKScale", 1.0d, 0.0d, 1e+6);
		BAUBLE_ARMOR_BOOSTING_SCALE = BUILDER.comment("Bauble armor increase will be scaled with this value.")
				.defineInRange("baubleMaxHPScale", 1.0d, 0.0d, 1e+6);
		
		CONFIG = BUILDER.build();
	}
	
	public static class ValueCache
	{
		public static class Sound
		{
			public static double AMBIENT_SOUND_CHANCE = 1.0d;
			public static boolean ZOMBIES_USE_GIRL_SOUND = false;
			public static boolean SKELETONS_USE_GIRL_SOUND = false;
			public static boolean GHASTS_USE_GIRL_SOUND = false;
			public static boolean ZOMBIES_NO_AMBIENT_SOUND = false;
			public static boolean SKELETONS_NO_AMBIENT_SOUND = false;
			public static boolean GHASTS_NO_AMBIENT_SOUND = false;
		}
		
		public static class Combat
		{
			public static boolean ENABLE_FRIENDLY_DAMAGE;
			public static boolean ENABLE_PROJECTILE_FRIENDLY_DAMAGE;
			public static double MAX_HEALTH_BOOST_BY_LEVEL;
			public static double MAX_ATK_BOOST_BY_LEVEL;
			public static float HEALTH_BOOST_PER_LEVEL;
			public static double ATK_BOOST_PER_LEVEL;
		}
		
		public static class Interaction
		{
			public static boolean ALLOW_VANILLA_CONVERSION;
			public static boolean ALLOW_REVERSE_CONVERSION;
			public static boolean ALL_ZOMBIE_GIRLS_CAN_CONVERT_TO_HUSKS;
			public static boolean ALL_DROWNED_GIRLS_CAN_CONVERT_TO_ZOMBIES;
			public static boolean ALL_STRAY_GIRLS_CAN_CONVERT_TO_SKELETONS;
		}
		
		public static class Baubles
		{
			public static float BAUBLE_HEALTH_RECOVERY_SCALE;
			public static double BAUBLE_MAX_HP_BOOSTING_SCALE;
			public static double BAUBLE_ATK_BOOSTING_SCALE;
			public static double BAUBLE_ARMOR_BOOSTING_SCALE;
			
		}
		
		public static void refreshCommon()
		{
			Sound.AMBIENT_SOUND_CHANCE = NFFGirlsConfigs.AMBIENT_SOUND_CHANCE.get();
			Sound.ZOMBIES_USE_GIRL_SOUND = NFFGirlsConfigs.ZOMBIES_USE_GIRL_SOUND.get();
			Sound.SKELETONS_USE_GIRL_SOUND = NFFGirlsConfigs.SKELETONS_USE_GIRL_SOUND.get();
			Sound.GHASTS_USE_GIRL_SOUND = NFFGirlsConfigs.GHASTS_USE_GIRL_SOUND.get();
			Sound.ZOMBIES_NO_AMBIENT_SOUND = NFFGirlsConfigs.ZOMBIES_NO_AMBIENT_SOUND.get();
			Sound.SKELETONS_NO_AMBIENT_SOUND = NFFGirlsConfigs.SKELETONS_NO_AMBIENT_SOUND.get();
			Sound.GHASTS_NO_AMBIENT_SOUND = NFFGirlsConfigs.GHASTS_NO_AMBIENT_SOUND.get();
			Combat.ENABLE_FRIENDLY_DAMAGE = NFFGirlsConfigs.ENABLE_FRIENDLY_DAMAGE.get();
			Combat.ENABLE_PROJECTILE_FRIENDLY_DAMAGE = NFFGirlsConfigs.ENABLE_PROJECTILE_FRIENDLY_DAMAGE.get();
			Combat.MAX_HEALTH_BOOST_BY_LEVEL = NFFGirlsConfigs.MAX_HEALTH_BOOST_BY_LEVEL.get();
			Combat.MAX_ATK_BOOST_BY_LEVEL = NFFGirlsConfigs.MAX_ATK_BOOST_BY_LEVEL.get();
			Combat.HEALTH_BOOST_PER_LEVEL = NFFGirlsConfigs.HEALTH_BOOST_PER_LEVEL.get().floatValue();
			Combat.ATK_BOOST_PER_LEVEL = NFFGirlsConfigs.ATK_BOOST_PER_LEVEL.get();
			Interaction.ALLOW_VANILLA_CONVERSION = NFFGirlsConfigs.ALLOW_VANILLA_CONVERSION.get();
			Interaction.ALLOW_REVERSE_CONVERSION = NFFGirlsConfigs.ALLOW_REVERSE_CONVERSION.get();
			Interaction.ALL_ZOMBIE_GIRLS_CAN_CONVERT_TO_HUSKS = NFFGirlsConfigs.ALL_ZOMBIE_GIRLS_CAN_CONVERT_TO_HUSKS.get();
			Interaction.ALL_DROWNED_GIRLS_CAN_CONVERT_TO_ZOMBIES = NFFGirlsConfigs.ALL_DROWNED_GIRLS_CAN_CONVERT_TO_ZOMBIES.get();
			Interaction.ALL_STRAY_GIRLS_CAN_CONVERT_TO_SKELETONS = NFFGirlsConfigs.ALL_STRAY_GIRLS_CAN_CONVERT_TO_SKELETONS.get();
			Baubles.BAUBLE_HEALTH_RECOVERY_SCALE = NFFGirlsConfigs.BAUBLE_HEALTH_RECOVERY_SCALE.get().floatValue();
			Baubles.BAUBLE_MAX_HP_BOOSTING_SCALE = NFFGirlsConfigs.BAUBLE_MAX_HP_BOOSTING_SCALE.get();
			Baubles.BAUBLE_ATK_BOOSTING_SCALE = NFFGirlsConfigs.BAUBLE_ATK_BOOSTING_SCALE.get();
			Baubles.BAUBLE_ARMOR_BOOSTING_SCALE = NFFGirlsConfigs.BAUBLE_ARMOR_BOOSTING_SCALE.get();
		}
		
		/*public static void refreshServer()
		{
			Combat.ENABLE_PROJECTILE_FRIENDLY_DAMAGE = NFFGirlsConfigs.ENABLE_PROJECTILE_FRIENDLY_DAMAGE.get();
			Combat.MAX_HEALTH_BOOST_BY_LEVEL = NFFGirlsConfigs.MAX_HEALTH_BOOST_BY_LEVEL.get();
			Combat.MAX_ATK_BOOST_BY_LEVEL = NFFGirlsConfigs.MAX_ATK_BOOST_BY_LEVEL.get();
		}*/
	}
	
	@SubscribeEvent
	public static void loadConfig(final ModConfigEvent event)
	{
		if (event.getConfig().getSpec() == CONFIG)
		{
			ValueCache.refreshCommon();
		}
		/*else if (event.getConfig().getSpec() == CONFIG_SERVER)
		{
			ValueCache.refreshServer();
		}*/
		
	}
}
