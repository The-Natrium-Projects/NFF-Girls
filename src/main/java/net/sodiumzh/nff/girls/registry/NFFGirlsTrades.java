package net.sodiumzh.nff.girls.registry;

import com.github.mechalopa.hmag.registry.ModItems;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sodiumzh.nautils.NaUtils;
import net.sodiumzh.nautils.entity.vanillatrade.VanillaTradeListing;
import net.sodiumzh.nautils.entity.vanillatrade.VanillaTradeListingCollection;
import net.sodiumzh.nautils.entity.vanillatrade.VanillaTradeListingCollectionHelper;
import net.sodiumzh.nautils.entity.vanillatrade.VanillaTradeRegistry;
import net.sodiumzh.nautils.item.ColoredItems;
import net.sodiumzh.nautils.registries.NaUtilsRegistries;
import net.sodiumzh.nautils.registries.NaUtilsRegistry;
import net.sodiumzh.nautils.registries.RegistryEntryCollection;
import net.sodiumzh.nautils.statics.NaUtilsDataStatics;
import net.sodiumzh.nff.girls.NFFGirls;

import java.util.*;

public class NFFGirlsTrades
{

	// REGISTRIES
	
	public static RegistryEntryCollection<VanillaTradeRegistry> TRADE_REGISTRIES =
		RegistryEntryCollection.create(NaUtilsRegistries.VANILLA_TRADE_REGISTRIES, NFFGirls.MOD_ID);
	
	public static final NaUtilsRegistry.Accessor<VanillaTradeRegistry> TRADE_REGISTRY = 
		TRADE_REGISTRIES.register("trade_registry", () -> 
		new VanillaTradeRegistry().readData(new ResourceLocation(NFFGirls.MOD_ID, "trades/trade_registry.json")));

	// COLLECTIONS
	
	
	public static RegistryEntryCollection<VanillaTradeListingCollection<?>> TRADE_COLLECTIONS =
		RegistryEntryCollection.create(NaUtilsRegistries.VANILLA_TRADE_LISTING_COLLECTIONS, NFFGirls.MOD_ID);
	
	// For all undead mobs
	//public static final ResourceLocation COMMON_UNDEAD = new ResourceLocation(NFFGirls.MOD_ID, "common_undead");
	// For all explosive-related mobs e.g. creeper, ghast etc.
	//public static final ResourceLocation COMMON_EXPLOSIVE = new ResourceLocation(NFFGirls.MOD_ID, "common_explosive");
	// For ice-related mobs
	//public static final ResourceLocation COMMON_ICE = new ResourceLocation(NFFGirls.MOD_ID, "common_ice");
	// For nether-related mobs
	//public static final ResourceLocation COMMON_NETHER = new ResourceLocation(NFFGirls.MOD_ID, "common_nether");
	// For ender-related mobs
	//public static final ResourceLocation COMMON_ENDER = new ResourceLocation(NFFGirls.MOD_ID, "common_ender");
	// For archer mobs
	//public static final ResourceLocation COMMON_ARCHER = new ResourceLocation(NFFGirls.MOD_ID, "common_archer");

	/* static final RegistryEntryCollection<VanillaTradeListing> LISTINGS = RegistryEntryCollection.create(NaUtilsRegistries.VANILLA_TRADE_LISTINGS,
		NFFGirls.MOD_ID);

	public static NaUtilsRegistry.Accessor<VanillaTradeListing> BUYS_COAL = LISTINGS.register("buys_coal",
		() -> VanillaTradeListing.exchanges(Items.COAL, 48, 64, NFFGirlsItems.EVIL_GEM.get(), 1, 1).setMaxUses(4));
	public static NaUtilsRegistry.Accessor<VanillaTradeListing> SELLS_OBSIDIAN = LISTINGS.register("sells_obsidian",
		() -> VanillaTradeListing.exchanges(NFFGirlsItems.EVIL_GEM.get(), 1, 1, Items.OBSIDIAN, 6, 10).setMaxUses(4));*/

	private static ItemStack byKey(String key)
	{
		//ResourceLocation loc = new ResourceLocation(key);
		return ForgeRegistries.ITEMS.containsKey(new ResourceLocation(key)) ? 
				ForgeRegistries.ITEMS.getValue(new ResourceLocation(key)).getDefaultInstance() : ItemStack.EMPTY;
	}
	
	private static ItemStack gaiaItem(String key)
	{
		return byKey("grimoireofgaia:" + key);
	}
	
	private static ResourceLocation jsonPath(String rawPath) {
		return new ResourceLocation(NFFGirls.MOD_ID, "trades/" + rawPath + ".json");
	}
	
	private static ResourceLocation jsonPath(ResourceLocation raw) {
		return new ResourceLocation(raw.getNamespace(), "trades/" + raw.getPath() + ".json");
	}
	
	private static ResourceLocation jsonPath(RegistryObject<?> object) {
		return jsonPath(object.getId());
	}
	
	public static NaUtilsRegistry.Accessor<VanillaTradeListingCollection<?>> COMMON_UNDEAD = TRADE_COLLECTIONS.register(
		"common_undead", () -> VanillaTradeListingCollectionHelper.newCollection()
			.setCurrency(NFFGirlsItems.EVIL_GEM.get())
			.setRequiredLevel(1)
			.addBuys(ModItems.SOUL_POWDER.get(), 24, 32, 1, 1, 4)
			.addBuys(Items.NETHER_WART, 56, 64, 1, 1, 4)
			.addSells(1, 1, Items.RED_MUSHROOM, 16, 22, 4)
			.addSells(1, 1, Items.BROWN_MUSHROOM, 16, 22, 4)
			.setRequiredLevel(2)
			.addBuys(Items.ROTTEN_FLESH, 56, 64, 1, 1, 2)
			.addBuys(Items.BONE, 56, 64, 1, 1, 2)
			.addBuys(Items.FERMENTED_SPIDER_EYE, 24, 36, 1, 1, 2)
			.addSells(1, 1, Items.QUARTZ, 18, 24, 4)
			.addSells(1, 1, NFFGirlsItems.DEATH_CRYSTAL_POWDER.get(), 2, 2, 4)
			.setRequiredLevel(3)
			.addBuys(ModItems.SOUL_APPLE.get(), 3, 4, 1, 1, 4)
			.addBuys(Items.SOUL_SAND, 56, 64, 1, 1, 2)
			.addBuys(Items.SOUL_SOIL, 56, 64, 1, 1, 2)
			.addSells(1, 1, NFFGirlsBlocks.ITEM_SOUL_CARPET.get(), 4, 6, 4)
			.addBuys(gaiaItem("stone_coal"), 16, 20, 1, 1, 4)
			.addBuys(gaiaItem("rotten_heart"), 1, 1, 1, 1, 2)
			.setRequiredLevel(4)
			.addBuys(Items.GOLD_INGOT, 32, 40, 1, 1, 4)
			.addBuys(Items.ZOMBIE_HEAD, 1, 1, 1, 1, 2)
			.addBuys(Items.SKELETON_SKULL, 1, 1, 1, 1, 2)
			.setRequiredLevel(5)
			.addBuys(Items.CRYING_OBSIDIAN, 8, 12, 1, 1, 4)
			.addBuys(Items.TOTEM_OF_UNDYING, 1, 1, 5, 7, 4)
			.readData(jsonPath("common_undead")).get());

	public static NaUtilsRegistry.Accessor<VanillaTradeListingCollection<?>> COMMON_ICE = TRADE_COLLECTIONS.register(
		"common_ice", () -> VanillaTradeListingCollectionHelper.newCollection()
			.setCurrency(NFFGirlsItems.EVIL_GEM.get())
			.setRequiredLevel(1)
			.addBuys(Items.SNOW_BLOCK, 56, 64, 1, 1, 4)
			.setRequiredLevel(2)
			.addBuys(Items.PACKED_ICE, 56, 64, 1, 1, 4)
			.addBuys(Items.LAPIS_LAZULI, 40, 46, 1, 1, 4)
			.setRequiredLevel(3)
			.addSells(1, 1, Items.BLUE_ICE, 6, 10, 4)
			.setRequiredLevel(4)
			.readData(jsonPath("common_ice")).get());

	public static NaUtilsRegistry.Accessor<VanillaTradeListingCollection<?>> COMMON_ARCHER = TRADE_COLLECTIONS.register(
		"common_archer", () -> VanillaTradeListingCollectionHelper.newCollection()
			.setCurrency(NFFGirlsItems.EVIL_GEM.get())
			.setRequiredLevel(1)
			.addBuys(Items.ARROW, 64, 64, 1, 1, 4)
			.setRequiredLevel(4)
			.addBuys(gaiaItem("bag_arrows"), 1, 1, 2, 3, 2)
			.readData(jsonPath("common_archer")).get());

	public static NaUtilsRegistry.Accessor<VanillaTradeListingCollection<?>> COMMON_EXPLOSIVE = TRADE_COLLECTIONS.register(
		"common_explosive", () -> VanillaTradeListingCollectionHelper.newCollection()
			.setCurrency(NFFGirlsItems.EVIL_GEM.get())
			.setRequiredLevel(1)
			.addBuys(Items.GUNPOWDER, 28, 36, 1, 1, 4)
			.addBuys(Items.CHARCOAL, 32, 48, 1, 1, 4)
			.addBuys(Items.FIRE_CHARGE, 24, 32, 1, 1, 4)
			.setRequiredLevel(2)
			.addBuys(Items.TNT, 8, 12, 1, 1, 4)
			.addSells(1, 1, ModItems.BLASTING_BOTTLE.get(), 3, 5, 4)
			.readData(jsonPath("common_explosive")).get());
	
	// Mob specific part
	public static NaUtilsRegistry.Accessor<VanillaTradeListingCollection<?>> HMAG_ZOMBIE_GIRL = TRADE_COLLECTIONS.register(
		NFFGirlsEntityTypes.HMAG_ZOMBIE_GIRL.getId().getPath(), () -> VanillaTradeListingCollectionHelper.newCollection()
			.setCurrency(NFFGirlsItems.EVIL_GEM.get())
			.setRequiredLevel(5)
			.addSells(64, 64, Items.ZOMBIE_SPAWN_EGG, 1, 1, 1).weight(0.1d)
			.readData(jsonPath(NFFGirlsEntityTypes.HMAG_ZOMBIE_GIRL)).get());

	public static NaUtilsRegistry.Accessor<VanillaTradeListingCollection<?>> HMAG_HUSK_GIRL = TRADE_COLLECTIONS.register(
		NFFGirlsEntityTypes.HMAG_HUSK_GIRL.getId().getPath(), () -> VanillaTradeListingCollectionHelper.newCollection()
			.setCurrency(NFFGirlsItems.EVIL_GEM.get())
			.setRequiredLevel(1)
			.addBuys(Items.RABBIT, 56, 64, 1, 1, 4)
			.addBuys(Items.FLINT, 56, 64, 1, 1, 4)
			.setRequiredLevel(3)
			.addSells(1, 1, Items.RABBIT_FOOT, 1, 1, 4)
			.setRequiredLevel(4)
			.addBuys(gaiaItem("weapon_book_hunger"), 1, 1, 4, 6, 2)
			.setRequiredLevel(5)
			.addSells(64, 64, Items.HUSK_SPAWN_EGG, 1, 1, 1).weight(0.1d)
			.readData(jsonPath(NFFGirlsEntityTypes.HMAG_HUSK_GIRL)).get());

	public static NaUtilsRegistry.Accessor<VanillaTradeListingCollection<?>> HMAG_DROWNED_GIRL = TRADE_COLLECTIONS.register(
		NFFGirlsEntityTypes.HMAG_DROWNED_GIRL.getId().getPath(), () -> VanillaTradeListingCollectionHelper.newCollection()
			.setCurrency(NFFGirlsItems.EVIL_GEM.get())
			.setRequiredLevel(1)
			.addBuys(Items.SEA_PICKLE, 24, 36, 1, 1, 4)
			.addBuys(Items.INK_SAC, 56, 64, 1, 1, 4)
			.setRequiredLevel(2)
			.addBuys(Items.COD, 56, 64, 1, 1, 4)
			.addBuys(Items.SALMON, 56, 64, 1, 1, 4)
			.addSells(3, 4, Items.NAUTILUS_SHELL, 1, 1, 4)
			.setRequiredLevel(3)
			.addBuys(ModItems.SAVAGEFANG_MEAT.get(), 24, 32, 1, 1, 4)
			.addBuys(ModItems.SWAMPER_TENTACLE.get(), 8, 12, 1, 1, 4)
			.addBuys(Items.SCUTE, 4, 6, 1, 1, 4)
			.addSells(12, 20, Items.TRIDENT, 1, 1, 4)
			.setRequiredLevel(4)
			.addBuys(Items.PRISMARINE_CRYSTALS, 24, 32, 1, 1, 4)
			.addBuys(Items.PRISMARINE_SHARD, 24, 32, 1, 1, 4)
			.addBuys(Items.GLOW_INK_SAC, 12, 16, 1, 1, 4)
			.addSells(1, 1, gaiaItem("shiny_pearl"), 12, 16, 4)
			.setRequiredLevel(5)
			.addSells(32, 40, Items.HEART_OF_THE_SEA, 1, 1, 1)
			.addSells(64, 64, Items.DROWNED_SPAWN_EGG, 1, 1, 1).weight(0.1d)
			.readData(jsonPath(NFFGirlsEntityTypes.HMAG_DROWNED_GIRL)).get());

	public static NaUtilsRegistry.Accessor<VanillaTradeListingCollection<?>> HMAG_SKELETON_GIRL = TRADE_COLLECTIONS.register(
		NFFGirlsEntityTypes.HMAG_SKELETON_GIRL.getId().getPath(), () -> VanillaTradeListingCollectionHelper.newCollection()
			.setCurrency(NFFGirlsItems.EVIL_GEM.get())
			.setRequiredLevel(1)
			.addBuys(byKey("alexsmobs:fish_bones"), 24, 36, 1, 1, 4)
			.setRequiredLevel(2)
			.addBuys(Items.AMETHYST_SHARD, 24, 32, 1, 1, 4)
			.setRequiredLevel(3)
			.addBuys(byKey("alexsmobs:centipede_leg"), 4, 7, 1, 1, 4)
			.setRequiredLevel(4)
			.addSells(2, 4, ModItems.OGRE_HORN.get(), 2, 3, 2)
			.addBuys(byKey("alexsmobs:rocky_shell"), 3, 5, 1, 1, 4)
			.addBuys(byKey("iceandfire:troll_tusk"), 4, 6, 1, 1, 4)
			.setRequiredLevel(5)
			.addEnchantsBook(6, 8,  Enchantments.PROJECTILE_PROTECTION,4, 2)
			.addSells(40, 48, byKey("twilightforest:triple_bow"), 1, 1, 1).weight(0.5)
			.addSells(64, 64, Items.SKELETON_SPAWN_EGG, 1, 1, 1).weight(0.1)
			.readData(jsonPath(NFFGirlsEntityTypes.HMAG_SKELETON_GIRL)).get());

	public static NaUtilsRegistry.Accessor<VanillaTradeListingCollection<?>> HMAG_STRAY_GIRL = TRADE_COLLECTIONS.register(
		NFFGirlsEntityTypes.HMAG_STRAY_GIRL.getId().getPath(), () -> VanillaTradeListingCollectionHelper.newCollection()
			.setCurrency(NFFGirlsItems.EVIL_GEM.get())
				.setRequiredLevel(4)
				.addBuys(gaiaItem("weapon_book_freezing"), 1, 1, 4, 6, 2)
				.readData(jsonPath(NFFGirlsEntityTypes.HMAG_STRAY_GIRL)).get());
	
	public static NaUtilsRegistry.Accessor<VanillaTradeListingCollection<?>> HMAG_WITHER_SKELETON_GIRL = TRADE_COLLECTIONS.register(
		NFFGirlsEntityTypes.HMAG_WITHER_SKELETON_GIRL.getId().getPath(), () -> VanillaTradeListingCollectionHelper.newCollection()
			.setCurrency(NFFGirlsItems.EVIL_GEM.get())
				.setRequiredLevel(1)
				.addBuys(Items.COAL, 48, 64, 1, 1, 4)
				.setRequiredLevel(2)
				.addSells(1, 1, Items.WITHER_ROSE, 2, 3, 2)
				.setRequiredLevel(3)
				.addSells(1, 1, Items.OBSIDIAN, 6, 10, 4)
				.addBuys(ModItems.ANCIENT_STONE.get(), 8, 12, 1, 1, 4)
				.addSells(1, 1, gaiaItem("nether_wart_jam"), 2, 3, 4)
				.addBuys(gaiaItem("withered_brain"), 1, 1, 1, 1, 4)
				.setRequiredLevel(4)
				.addBuys(Items.WITHER_SKELETON_SKULL, 1, 1, 1, 1, 2)
				.addBuys(gaiaItem("weapon_book_wither"), 1, 1, 4, 6, 2)
				.setRequiredLevel(5)
				.addEnchantsBook(6, 8,  Enchantments.POWER_ARROWS,5, 2)
				.addSells(64, 64, Items.WITHER_SKELETON_SPAWN_EGG, 1, 1, 1).weight(0.05d)
				.addSells(12, 16, ModItems.NETHER_STAR_FRAGMENT.get(), 1, 1, 2)
				.readData(jsonPath(NFFGirlsEntityTypes.HMAG_WITHER_SKELETON_GIRL)).get());

	public static NaUtilsRegistry.Accessor<VanillaTradeListingCollection<?>> HMAG_CREEPER_GIRL = TRADE_COLLECTIONS.register(
		NFFGirlsEntityTypes.HMAG_CREEPER_GIRL.getId().getPath(), () -> VanillaTradeListingCollectionHelper.newCollection()
			.setCurrency(NFFGirlsItems.EVIL_GEM.get())
				.setRequiredLevel(3)
				.addBuys(Items.REDSTONE, 40, 48, 1, 1, 4)
				.addSells(1, 1, Items.REPEATER, 3, 4, 4)
				.addSells(1, 1, Items.COMPARATOR, 2, 3, 4)
				.setRequiredLevel(4)
				.addSells(1, 1, ModItems.LIGHTNING_PARTICLE.get(), 1, 1, 4)
				.addSells(1, 1, ModItems.EXP_BERRY.get(), 2, 2, 4)
				.addSells(1, 1, ModItems.RANDOMBERRY.get(), 3, 5, 4)
				.addSells(1, 1, ModItems.CUREBERRY.get(), 3, 5, 4)
				.addBuys(Items.CREEPER_HEAD, 1, 1, 1, 1, 4)
				.addSells(1, 1, ModItems.LIGHTNING_SOUP.get(), 2, 3, 4)
				.setRequiredLevel(5)
				.addEnchantsBook(6, 8, Enchantments.BLAST_PROTECTION, 4, 2)
				.addBuys(gaiaItem("doll_creeper_girl"), 1, 1, 8, 12, 2)

				.readData(jsonPath(NFFGirlsEntityTypes.HMAG_CREEPER_GIRL)).get());
	
	public static NaUtilsRegistry.Accessor<VanillaTradeListingCollection<?>> HMAG_ENDER_EXECUTOR = TRADE_COLLECTIONS.register(
		NFFGirlsEntityTypes.HMAG_ENDER_EXECUTOR.getId().getPath(), () -> VanillaTradeListingCollectionHelper.newCollection()
			.setCurrency(NFFGirlsItems.EVIL_GEM.get())
				.setRequiredLevel(1)
				.addBuys(Items.ENDER_PEARL, 16, 24, 1, 1, 4)
				.addBuys(Items.TWISTING_VINES, 36, 56, 1, 1, 4)
				.addBuys(Items.WARPED_FUNGUS, 24, 36, 1, 1, 4)
				.setRequiredLevel(2)
				.addBuys(Items.CHORUS_FRUIT, 48, 64, 1, 1, 4)
				.addBuys(Items.ENDER_EYE, 16, 24, 1, 1, 4)
				.addSells(1, 1, Items.END_ROD, 8, 12, 4)
				.setRequiredLevel(3)
				.addSells(3, 5, NFFGirlsItems.ENDERBERRY.get(), 1, 1, 4)
				.addSells(2, 3, Items.SHULKER_SHELL, 1, 1, 4)
				.addSells(1, 1, Items.CHORUS_FLOWER, 2, 3, 4)
				.addSells(1, 1, ModItems.ENDER_PLASM.get(), 3, 4, 4)
				.addSells(4, 6, Items.END_CRYSTAL, 1, 2, 4)
				.setRequiredLevel(4)
				.addBuys(Items.DRAGON_BREATH, 8, 12, 1, 1, 4)
				.addSells(1, 1, ModItems.DYSSOMNIA_SKIN.get(), 2, 2, 2)
				.addBuys(gaiaItem("weapon_book_ender"), 1, 1, 4, 6, 2)
				.setRequiredLevel(5)
				.addBuys(Items.DRAGON_HEAD, 1, 1, 4, 5, 4)
				.addBuys(gaiaItem("doll_ender_girl"), 1, 1, 8, 12, 2)
				.readData(jsonPath(NFFGirlsEntityTypes.HMAG_ENDER_EXECUTOR)).get());

	public static NaUtilsRegistry.Accessor<VanillaTradeListingCollection<?>> HMAG_KOBOLD=  TRADE_COLLECTIONS.register(
		NFFGirlsEntityTypes.HMAG_KOBOLD.getId().getPath(), () -> VanillaTradeListingCollectionHelper.newCollection()
			.setCurrency(NFFGirlsItems.EVIL_GEM.get())
				.setRequiredLevel(1)
				.addBuys(Items.COAL, 48, 64, 1, 1, 4)
				.addBuys(Items.RAW_COPPER, 48, 64, 1, 1, 4)
				.addBuys(Items.RAW_IRON, 40, 48, 1, 1, 4)
				.addBuys(Items.RAW_GOLD, 32, 40, 1, 1, 4)
				.setRequiredLevel(2)
				.addBuys(Items.GLOW_BERRIES, 24, 32, 1, 1, 4)
				.addBuys(Items.IRON_INGOT, 40, 48, 1, 1, 4)
				.addBuys(Items.GOLD_INGOT, 32, 40, 1, 1, 4)
				.addBuys(ModItems.BAT_WING.get(), 8, 12, 1, 1, 4)
				.addBuys(gaiaItem("stone_coal"), 16, 20, 1, 1, 4)
				.setRequiredLevel(3)
				.addBuys(Items.LAPIS_LAZULI, 40, 46, 1, 1, 4)
				.addBuys(Items.REDSTONE, 40, 48, 1, 1, 4)
				.addBuys(Items.QUARTZ, 24, 32, 1, 1, 4)
				.addBuys(Items.EMERALD, 18, 24, 1, 1, 4)
				.addSells(1, 1, ModItems.OGRE_HORN.get(), 1, 1, 2).weight(0.2)
				.addSells(1, 1, ModItems.NECROFIBER.get(), 1, 1, 2).weight(0.2)
				.addSells(1, 1, ModItems.LICH_CLOTH.get(), 1, 1, 2).weight(0.2)
				.addSells(1, 1, ModItems.KOBOLD_LEATHER.get(), 2, 2, 2).weight(0.5)
				.setRequiredLevel(4)
				.addBuys(Items.DIAMOND, 5, 9, 1, 1, 4)
				.addBuys(Items.NETHER_GOLD_ORE, 32, 40, 1, 1, 4)
				//.addBuys(Items.SCULK, 48, 64, 1, 1, 4)
				.addSells(3, 5, Items.NETHERITE_SCRAP, 1, 1, 4)
				.setRequiredLevel(5)
				.addBuys(Items.SCULK_SENSOR, 20, 28, 1, 1, 4)
				//.addSells(3, 4, Items.SCULK_CATALYST, 1, 1, 2)
				.addEnchantsBook(8, 10, Enchantments.BLOCK_EFFICIENCY, 5, 2)
				.readData(jsonPath(NFFGirlsEntityTypes.HMAG_KOBOLD)).get());

	public static NaUtilsRegistry.Accessor<VanillaTradeListingCollection<?>> HMAG_NECROTIC_REAPER=  TRADE_COLLECTIONS.register(
		NFFGirlsEntityTypes.HMAG_NECROTIC_REAPER.getId().getPath(), () -> VanillaTradeListingCollectionHelper.newCollection()
			.setCurrency(NFFGirlsItems.EVIL_GEM.get())
				//.linkListings(COMMON_UNDEAD)
				.setRequiredLevel(1)
				.addBuys(Items.FLINT, 56, 64, 1, 1, 4)
				.addBuys(Items.STRING, 56, 64, 1, 1, 4)
				.setRequiredLevel(2)
				.addBuys(Items.COAL, 48, 64, 1, 1, 4)
				.addBuys(Items.CHARCOAL, 32, 48, 1, 1, 4)
				.addSells(1, 1, ModItems.SOUL_POWDER.get(), 12, 16, 4)
				.setRequiredLevel(3)
				.addSells(1, 1, ModItems.NECROFIBER.get(), 2, 2, 2)
				.addSells(1, 1, ModItems.LICH_CLOTH.get(), 2, 2, 2)
				.setRequiredLevel(4)
				.addSells(6, 8, ModItems.DYSSOMNIA_SKIN.get(), 1, 1, 2)
				.addBuys(gaiaItem("cursed_metal_sword"), 1, 1, 3, 5, 2)
				.setRequiredLevel(5)
				//.addSells(10, 12, Items.ECHO_SHARD, 1, 1, 2).weight(0.5)
				//.addSellsEnchantmentBook(24, 32, Enchantments.SWIFT_SNEAK, 1, 1).weight(0.1)
				//.addSellsEnchantmentBook(64, 64, Enchantments.SWIFT_SNEAK, 3, 1).weight(0.05)
				.addSellsEnchantmentBook(6, 8, Enchantments.SHARPNESS, 5, 2)

				.readData(jsonPath(NFFGirlsEntityTypes.HMAG_NECROTIC_REAPER)).get());

	public static NaUtilsRegistry.Accessor<VanillaTradeListingCollection<?>> HMAG_MELTY_MONSTER = TRADE_COLLECTIONS.register(
		NFFGirlsEntityTypes.HMAG_MELTY_MONSTER.getId().getPath(), () -> VanillaTradeListingCollectionHelper.newCollection()
			.setCurrency(NFFGirlsItems.EVIL_GEM.get())
				/*.setRequiredLevel(1)
				.addBuys(Items.COAL, 48, 56, 1, 1, 4)
				.addBuys(Items.BLAZE_ROD, 24, 32, 1, 1, 4)
				.addBuys(Items.MAGMA_BLOCK, 48, 64, 1, 1, 2)
				.setRequiredLevel(2)
				.addBuys(Items.MAGMA_CREAM, 36, 44, 1, 1, 4)
				.addBuys(Items.FIRE_CHARGE, 36, 44, 1, 1, 4)
				.addBuys(gaiaItem("fireshard"), 8, 12, 1, 1, 4)
				.setRequiredLevel(3)
				.addBuys(ModItems.FIRE_BOTTLE.get(), 6, 12, 1, 1, 4)
				.addSells(1, 1, ModItems.BURNING_CORE.get(), 2, 2, 2)
				.setRequiredLevel(4)
				.addBuys(gaiaItem("fan_fire"), 1, 1, 3, 5, 4)
				.addSells(2, 3, byKey("iceandfire:fire_lily"), 1, 1, 4)
				.addBuys(byKey("iceandfire:fire_dragon_blood"), 6, 10, 1, 3, 4)
				//.addSellsEnchantmentBook(6, 10, Enchantments.FLAME, 1, 4)
				.setRequiredLevel(5)
				.addSellsEnchantmentBook(16, 20, Enchantments.FIRE_ASPECT, 5, 2)
				.addSellsEnchantmentBook(16, 20, Enchantments.FIRE_PROTECTION, 8, 2)
				.addSells(2, 3, byKey("twilightforest:fiery_ingot"), 1, 1, 2)
				.addSells(24, 48, byKey("iceandfire:dragonforge_fire_input"), 1, 1, 2)*/
				.readData(jsonPath(NFFGirlsEntityTypes.HMAG_MELTY_MONSTER)).get());

	public static NaUtilsRegistry.Accessor<VanillaTradeListingCollection<?>> HMAG_CURSED_DOLL = TRADE_COLLECTIONS.register(
		NFFGirlsEntityTypes.HMAG_CURSED_DOLL.getId().getPath(), () -> VanillaTradeListingCollectionHelper.newCollection()
			.setCurrency(NFFGirlsItems.EVIL_GEM.get())
				/*.setRequiredLevel(1)
				.addBuys(Items.FEATHER, 32, 48, 1, 1, 16)
				.addBuys(Items.STRING, 56, 64, 1, 1, 16)
				.addBuys(Items.PAPER, 32, 40, 1, 1, 16)
				.setRequiredLevel(2)
				.addBuys(Items.WHITE_WOOL, 18, 28, 1, 1, 16)
				.addBuys(Items.AMETHYST_SHARD, 16, 24, 1, 1, 16)
				.addBuys(byKey("alexsmobs:fedora"), 1, 1, 2, 4, 4)
				.addSells(2, 2, Items.PHANTOM_MEMBRANE, 3, 5, 16)
				.addSells(2, 2, ModItems.SOUL_POWDER.get(), 3, 5, 16)
				.setRequiredLevel(3)
				.addBuys(Items.CAKE, 1, 1, 2, 3, 4)
				.addBuys(ModItems.HONEYED_APPLE.get(), 4, 8, 1, 2, 8)
				.addBuys(ModItems.HONEYED_LEMON.get(), 4, 8, 1, 2, 8)
				.addBuys(gaiaItem("honeydew"), 4, 6, 1, 3, 8)
				.addSells(3, 6, Items.NAME_TAG, 1, 1, 8)
				.setRequiredLevel(4)
				.addBuys(Items.WRITTEN_BOOK, 1, 1, 3, 4, 2)
				.addBuys(byKey("twilightforest:magic_map_focus"), 1, 1, 1, 2, 4)
				.addBuys(byKey("iceandfire:pixie_dust"), 8, 12, 2, 3, 8)
				.addSells(12, 18, gaiaItem("book_of_memory"), 1, 1, 4)
				.addSells(2, 4, ModItems.FORTUNE_CRYSTAL.get(), 2, 3, 8)
				.addSells(2, 4, ModItems.REPULSION_GADGET.get(), 2, 3, 8)
				.setRequiredLevel(5)
				.addBuys(byKey("twilightforest:peacock_feather_fan"), 1, 1, 4, 6, 4)
				.addSells(8, 16, ModItems.FORTUNE_CRYSTAL_PLUS.get(), 2, 3, 4)
				.addSells(8, 12, ModItems.PURIFICATION_CLOTH.get(), 2, 3, 4)
				.addSells(16, 32, byKey("iceandfire:pixie_wings"), 1, 1, 2)*/
			.readData(jsonPath(NFFGirlsEntityTypes.HMAG_CURSED_DOLL)).get());

	public static NaUtilsRegistry.Accessor<VanillaTradeListingCollection<?>> HMAG_JACK_FROST = TRADE_COLLECTIONS.register(
		NFFGirlsEntityTypes.HMAG_JACK_FROST.getId().getPath(), () -> VanillaTradeListingCollectionHelper.newCollection()
			.setCurrency(NFFGirlsItems.EVIL_GEM.get())
				/*.setRequiredLevel(1)
				.addBuys(Items.NETHER_WART, 24, 32, 1, 1, 16)
				.addBuys(Items.LAPIS_LAZULI, 28, 36, 1, 1, 16)
				.addBuys(Items.SNOW_BLOCK, 48, 56, 1, 1, 16)
				.setRequiredLevel(2)
				.addBuys(Items.AMETHYST_SHARD, 24, 32, 1, 1, 16)
				.addBuys(Items.PUMPKIN_PIE, 28, 36, 1, 2, 16)
				.addBuys(gaiaItem("fur"), 8, 12, 1, 2, 8)
				.addSells(2, 2, Items.DIAMOND, 8, 16, 8)
				.addSells(2, 2, Items.BLUE_ICE, 6, 12, 16)
				.setRequiredLevel(3)
				.addBuys(ModItems.LEMON_PIE.get(), 4, 6, 2, 3, 8)
				.addBuys(byKey("iceandfire:frosty_lily"), 10, 16, 3, 4, 4)
				.addBuys(byKey("twilightforest:aurora_block"), 18, 28, 1, 3, 16)
				.addSellsEnchantmentBook(12, 16, Enchantments.FROST_WALKER, 2, 4)
				.setRequiredLevel(4)
				.addBuys(byKey("alexsmobs:froststalker_horn"), 3, 4, 2, 4, 8)
				.addBuys(byKey("twilightforest:alpha_yeti_fur"), 4, 8, 3, 5, 4)
				.addSells(6, 8, ModItems.FORTUNE_CRYSTAL.get(), 1, 1, 8)
				.addSells(4, 6, ModItems.PURIFICATION_CLOTH.get(), 1, 1, 4)
				.addSells(8, 12, byKey("twilightforest:ice_sword"), 1, 1, 4)
				.setRequiredLevel(5)
				.addBuys(gaiaItem("fan_ice"), 1, 1, 3, 5, 4)
				.addSells(4, 8, Items.ENCHANTED_GOLDEN_APPLE, 1, 1, 16)
				.addSells(12, 32, ModItems.FORTUNE_CRYSTAL_PLUS.get(), 1, 1, 4)
				.addSells(16, 32, byKey("iceandfire:dragonforge_ice_input"), 1, 1, 2)
				.addSells(64, 128, byKey("iceandfire:dread_queen_sword"), 1, 1, 1)
*/
				.readData(jsonPath(NFFGirlsEntityTypes.HMAG_JACK_FROST)).get());

	public static NaUtilsRegistry.Accessor<VanillaTradeListingCollection<?>> HMAG_ALRAUNE = TRADE_COLLECTIONS.register(
		NFFGirlsEntityTypes.HMAG_ALRAUNE.getId().getPath(), () -> VanillaTradeListingCollectionHelper.newCollection()
			.setCurrency(NFFGirlsItems.EVIL_GEM.get())
				/*.setRequiredLevel(1)
				.addBuys(Items.WHEAT_SEEDS, 48, 64, 1, 1, 16)
				.addBuys(Items.PUMPKIN_SEEDS, 36, 56, 1, 1, 16)
				.addBuys(Items.MELON_SEEDS, 36, 56, 1, 1, 16)
				.addBuys(Items.BEETROOT_SEEDS, 36, 56, 1, 1, 16)
				.setRequiredLevel(2)
				.addBuys(Items.SWEET_BERRIES, 32, 48, 1, 1, 16)
				.addBuys(byKey("twilightforest:torchberries"), 24, 36, 1, 1, 16)
				.addSells(2, 2, Items.COCOA_BEANS, 8, 16, 8)
				.addSells(3, 6, ModItems.MYSTERIOUS_PETAL.get(), 1, 2, 4)
				.setRequiredLevel(3)
				.addBuys(byKey("iceandfire:pixie_jar_0"), 1, 1, 2, 4, 4)
				.addBuys(byKey("iceandfire:pixie_jar_1"), 1, 1, 2, 4, 4)
				.addBuys(byKey("iceandfire:pixie_jar_2"), 1, 1, 2, 4, 4)
				.addBuys(byKey("iceandfire:pixie_jar_3"), 1, 1, 2, 4, 4)
				.addBuys(byKey("iceandfire:pixie_jar_4"), 1, 1, 2, 4, 4)
				.addSells(2, 5, ModItems.CUREBERRY.get(), 1, 2, 8)
				.addSells(2, 5, ModItems.RANDOMBERRY.get(), 1, 2, 8)
				.addSells(2, 5, ModItems.EXP_BERRY.get(), 1, 2, 8)
				.setRequiredLevel(4)
				.addBuys(gaiaItem("mandrake"), 1, 1, 4, 6, 4)
				.addSells(5, 14, NFFGirlsItems.ENDERBERRY.get(), 1, 1, 4)
				.addSells(6, 8, ModItems.GREEDY_CRYSTAL.get(), 1, 1, 4)
				.addSells(4, 6, ModItems.EVIL_THORN.get(), 1, 1, 8)
				.addSells(6, 10, byKey("alexsmobs:mungal_spores"), 1, 1, 4)
				.setRequiredLevel(5)
				.addBuys(gaiaItem("doll_dryad"), 1, 1, 8, 12, 2)
				.addSells(10, 20, NFFGirlsItems.HEALING_JADE.get(), 1, 1, 8)
				.addSells(12, 32, ModItems.GREEDY_CRYSTAL_PLUS.get(), 1, 1, 4)
				.addSells(10, 24, byKey("twilightforest:magic_beans"), 1, 1, 2)
				.addSells(24, 42, byKey("twilightforest:charm_of_life_2"), 1, 1, 2)
*/
				.readData(jsonPath(NFFGirlsEntityTypes.HMAG_ALRAUNE)).get());
	
	public static NaUtilsRegistry.Accessor<VanillaTradeListingCollection<?>> HMAG_BANSHEE = TRADE_COLLECTIONS.register(
		NFFGirlsEntityTypes.HMAG_BANSHEE.getId().getPath(), () -> VanillaTradeListingCollectionHelper.newCollection()
			.setCurrency(NFFGirlsItems.EVIL_GEM.get())
				/*.setRequiredLevel(1)
				.addBuys(Items.ALLIUM, 32, 36, 1, 1, 16)
				.addBuys(Items.RED_TULIP, 32, 36, 1, 1, 16)
				.addBuys(Items.ORANGE_TULIP, 32, 36, 1, 1, 16)
				.addBuys(Items.WHITE_TULIP, 32, 36, 1, 1, 16)
				.addBuys(Items.PINK_TULIP, 32, 36, 1, 1, 16)
				.addBuys(Items.POPPY, 32, 36, 1, 1, 16)
				.addBuys(Items.DANDELION, 32, 36, 1, 1, 16)
				.addBuys(Items.LILY_OF_THE_VALLEY, 32, 36, 1, 1, 16)
				.addBuys(Items.AZURE_BLUET, 32, 36, 1, 1, 16)
				.addBuys(Items.BLUE_ORCHID, 32, 36, 1, 1, 16)
				.setRequiredLevel(2)
				.addBuys(Items.WITHER_ROSE, 12, 24, 1, 1, 16)
				.addBuys(byKey("alexsmobs:acacia_blossom"), 12, 24, 1, 1, 16)
				//.addSells(2, 4, ModItems.SOUL_POWDER, 4, 8, 8)
				.addSells(2, 2, Items.AMETHYST_SHARD, 3, 6, 32)
				.setRequiredLevel(3)
				.addBuys(ModItems.SOUL_APPLE.get(), 3, 5, 1, 1, 8)
				.addBuys(byKey("iceandfire:ectoplasm"), 12, 16, 1, 2, 8)
				.addBuys(byKey("iceandfire:pixie_dust"), 12, 16, 1, 2, 8)
				.addSells(2, 4, Items.GHAST_TEAR, 1, 3, 8)
				.addSells(2, 2, Items.GLOW_INK_SAC, 2, 3, 8)
				.addSells(2, 4, Items.PHANTOM_MEMBRANE, 1, 3, 8)
				.setRequiredLevel(4)
				.addBuys(gaiaItem("soulfire"), 4, 8, 3, 6, 4)
				.addBuys(gaiaItem("weapon_book_nightmare"), 1, 1, 4, 6, 2)
				.addBuys(byKey("alexsmobs:soul_heart"), 1, 1, 4, 6, 8)
				.addSells(6, 8, ModItems.FORTUNE_CRYSTAL.get(), 1, 1, 8)
				.setRequiredLevel(5)
				.addSells(12, 32, ModItems.FORTUNE_CRYSTAL_PLUS.get(), 1, 1, 4)
				.addSells(12, 32, ModItems.PURIFICATION_CLOTH.get(), 1, 1, 4)
				.addSells(48, 96, byKey("twilightforest:glass_sword"), 1, 1, 2)
				.addSells(48, 96, byKey("iceandfire:ghost_sword"), 1, 1, 2)
*/
				.readData(jsonPath(NFFGirlsEntityTypes.HMAG_BANSHEE)).get());

	public static NaUtilsRegistry.Accessor<VanillaTradeListingCollection<?>> HMAG_HORNET = TRADE_COLLECTIONS.register(
		NFFGirlsEntityTypes.HMAG_HORNET.getId().getPath(), () -> VanillaTradeListingCollectionHelper.newCollection()
			.setCurrency(NFFGirlsItems.EVIL_GEM.get())
				/*.setRequiredLevel(1)
				.addBuys(Items.SUNFLOWER, 24, 32, 1, 1, 16)
				.addBuys(Items.ROSE_BUSH, 24, 32, 1, 1, 16)
				.addBuys(Items.PEONY, 24, 32, 1, 1, 16)
				.addBuys(Items.LILAC, 24, 32, 1, 1, 16)
				.setRequiredLevel(2)
				.addBuys(Items.SPIDER_EYE, 48, 56, 1, 1, 16)
				.addBuys(Items.BONE_MEAL, 56, 64, 1, 1, 16)
				.addBuys(Items.SUGAR, 36, 48, 1, 1, 16)
				.addBuys(Items.BEETROOT, 36, 56, 1, 1, 16)
				.setRequiredLevel(3)
				.addBuys(ModItems.IRON_SPEAR.get(), 1, 1, 2, 3, 4)
				.addBuys(byKey("twilightforest:steeleaf_ingot"), 6, 10, 2, 4, 8)
				.addSells(4, 6, Items.HONEYCOMB, 1, 3, 16)
				.addSells(6, 10, Items.HONEY_BOTTLE, 1, 3, 16)
				.setRequiredLevel(4)
				.addBuys(gaiaItem("weapon_book_nature"), 1, 1, 4, 6, 2)
				//.addSells(6, 12, Items.BEE_NEST.get(), 1, 1, 4)
				.addSells(6, 8, ModItems.MYSTERIOUS_PETAL.get(), 1, 1, 8)
				.addSells(6, 8, ModItems.EVIL_THORN.get(), 1, 1, 8)
				.setRequiredLevel(5)
				.addBuys(byKey("alexsmobs:poison_bottle"), 16, 32, 3, 4, 8)
				.addBuys(byKey("iceandfire:hydra_fang"), 2, 4, 3, 4, 8)
				.addSells(72, 112, ModItems.INSOMNIA_FRUIT.get(), 1, 1, 2)
				.addSellsEnchantmentBook(16, 20, Enchantments.SILK_TOUCH, 1, 4)

				.readData(jsonPath(NFFGirlsEntityTypes.)).get());.push("nffgirls:hmag_dullahan")
				.setRequiredLevel(1)
				.addBuys(Items.IRON_INGOT, 36, 48, 1, 1, 16)
				.addBuys(Items.BRICK, 56, 64, 1, 1, 16)
				.addBuys(Items.NETHER_BRICK, 48, 56, 1, 1, 16)
				.setRequiredLevel(2)
				.addBuys(Items.SHIELD, 1, 1, 1, 1, 4)
				.addBuys(Items.OBSIDIAN, 12, 18, 1, 1, 16)
				.addBuys(Items.END_STONE, 36, 48, 1, 1, 16)
				.addSells(2, 4, Items.CRYING_OBSIDIAN, 4, 6, 8)
				.setRequiredLevel(3)
				.addBuys(byKey("alexsmobs:spiked_scute"), 2, 4, 1, 2, 8)
				.addBuys(byKey("alexsmobs:crocodile_scute"), 8, 16, 1, 2, 8)
				.addBuys(byKey("alexsmobs:straddlite"), 6, 12, 1, 2, 8)
				.addBuys(byKey("twilightforest:knightmetal_ingot"), 6, 8, 2, 4, 8)
				//.addSells(10, 16, ModItems.NETHERITE_NUGGET, 3, 6, 16)
				//.addSells(6, 8, ModItems.ORGE_HORN, 1, 1, 8)
				.setRequiredLevel(4)
				.addBuys(ModItems.FORTRESS_SHIELD.get(), 1, 1, 3, 5, 4)
				.addBuys(ModItems.ANCIENT_SHIELD.get(), 1, 1, 3, 5, 4)
				.addBuys(byKey("twilightforest:knightmetal_shield"), 1, 1, 4, 6, 4)
				.addBuys(gaiaItem("giga_gear"), 1, 1, 4, 6, 2)
				.addBuys(gaiaItem("weapon_book_metal"), 1, 1, 4, 6, 2)
				.addSells(6, 8, ModItems.REINFORCING_CHAIN.get(), 1, 1, 8)
				.addSells(6, 8, ModItems.ANCIENT_STONE.get(), 2, 3, 8)
				.setRequiredLevel(5)
				.addBuys(gaiaItem("doll_dullahan"), 1, 1, 8, 12, 2)
				.addSells(36, 56, byKey("iceandfire:hippogryph_sword"), 1, 1, 2)
				//.addSells(64, 128, gaiaItem("weapon_book_buff"), 1, 1, 2)
				.addSells(8, 16, ModItems.MULTIPLEX_REINFORCING_CHAIN.get(), 1, 1, 4)
				//.addSellsEnchantmentBook(48, 72, Enchantments.PROTECTION, 8, 2)
*/
				.readData(jsonPath(NFFGirlsEntityTypes.HMAG_HORNET)).get());
	
	public static NaUtilsRegistry.Accessor<VanillaTradeListingCollection<?>> HMAG_GHASTLY_SEEKER = TRADE_COLLECTIONS.register(
		NFFGirlsEntityTypes.HMAG_GHASTLY_SEEKER.getId().getPath(), () -> VanillaTradeListingCollectionHelper.newCollection()
			.setCurrency(NFFGirlsItems.EVIL_GEM.get())
				/*.setRequiredLevel(1)
				.addBuys(Items.SOUL_SOIL, 48, 64, 1, 1, 16)
				.addBuys(Items.SOUL_SAND, 48, 64, 1, 1, 16)
				.addBuys(Items.SOUL_LANTERN, 16, 24, 1, 1, 16)
				.setRequiredLevel(2)
				.addBuys(Items.FIRE_CHARGE, 16, 24, 1, 1, 16)
				.addBuys(byKey("twilightforest:experiment_115"), 6, 10, 1, 2, 16)
				.addSells(2, 4, ModItems.SOUL_POWDER.get(), 4, 8, 8)
				.addSells(4, 6, Items.GHAST_TEAR, 6, 8, 16)
				.setRequiredLevel(3)
				.addBuys(ModItems.SOUL_APPLE.get(), 3, 5, 1, 1, 8)
				.addBuys(ModItems.FIRE_BOTTLE.get(), 3, 5, 1, 1, 16)
				.addBuys(byKey("iceandfire:ectoplasm"), 12, 16, 1, 2, 8)
				.addBuys(byKey("twilightforest:carminite"), 12, 16, 1, 2, 8)
				.addSells(6, 8, NFFGirlsItems.SOUL_CLOTH.get(), 2, 3, 8)
				.setRequiredLevel(4)
				.addBuys(ModItems.BLASTING_BOTTLE.get(), 2, 4, 1, 3, 8)
				.addBuys(gaiaItem("soulfire"), 4, 8, 3, 6, 4)
				.addBuys(byKey("alexsmobs:soul_heart"), 1, 1, 4, 6, 8)
				.addBuys(byKey("twilightforest:fiery_tears"), 4, 8, 2, 3, 4)
				.addSells(6, 8, ModItems.EVIL_FLAME.get(), 1, 1, 8)
				.addSells(6, 8, ModItems.REPULSION_GADGET.get(), 1, 1, 8)
				.setRequiredLevel(5)
				.addBuys(byKey("twilightforest:ur_ghast_trophy"), 1, 1, 8, 12, 2)
				.addSells(48, 84, ModItems.NEMESIS_BLADE.get(), 1, 1, 2)
				.addSells(12, 24, Items.NETHER_STAR, 1, 1, 2)
*/
				.readData(jsonPath(NFFGirlsEntityTypes.HMAG_GHASTLY_SEEKER)).get());
	
	public static NaUtilsRegistry.Accessor<VanillaTradeListingCollection<?>> HMAG_REDCAP = TRADE_COLLECTIONS.register(
		NFFGirlsEntityTypes.HMAG_REDCAP.getId().getPath(), () -> VanillaTradeListingCollectionHelper.newCollection()
			.setCurrency(NFFGirlsItems.EVIL_GEM.get())
			.readData(jsonPath(NFFGirlsEntityTypes.HMAG_REDCAP)).get());
				/*.setRequiredLevel(1)
				.addBuys(Items.SPIDER_EYE, 42, 56, 1, 1, 16)
				.addBuys(Items.RED_DYE, 48, 64, 1, 1, 16)
				.addBuys(Items.REDSTONE, 48, 64, 1, 1, 16)
				.addBuys(Items.RED_MUSHROOM, 48, 64, 1, 1, 16)
				.setRequiredLevel(2)
				.addBuys(Items.BEETROOT, 48, 64, 1, 1, 16)
				.addBuys(Items.SWEET_BERRIES, 36, 48, 1, 1, 16)
				.addBuys(Items.APPLE, 36, 48, 1, 1, 16)
				.addBuys(Items.MELON_SLICE, 48, 64, 1, 1, 16)
				.setRequiredLevel(3)
				.addBuys(Items.CRIMSON_FUNGUS, 36, 48, 1, 2, 16)
				.addBuys(Items.NETHER_WART, 48, 64, 1, 2, 16)
				.addBuys(Items.WEEPING_VINES, 36, 48, 1, 2, 16)
				.addBuys(Items.FIRE_CORAL, 24, 32, 1, 3, 16)
				.setRequiredLevel(4)
				.addBuys(ModItems.EVIL_FLAME.get(), 6, 10, 3, 6, 8)
				.addBuys(ModItems.CUREBERRY.get(), 16, 20, 3, 6, 8)
				.addBuys(ModItems.CRIMSON_CUTICULA.get(), 4, 6, 3, 5, 8)
				.addBuys(byKey("alexsmobs:blood_sac"), 12, 18, 3, 5, 8)
				.addBuys(byKey("iceandfire:dragonscales_red"), 8, 12, 4, 6, 8)
				.addBuys(byKey("iceandfire:sea_serpent_scales_red"), 8, 12, 4, 6, 8)
				.addBuys(gaiaItem("deco_garden_gnome"), 1, 1, 4, 5, 2)
				.setRequiredLevel(5)
				.addSellsEnchantmentBook(72, 112, Enchantments.MENDING, 1, 4)
				.addSells(96, 128, byKey("iceandfire:dragonegg_red"), 1, 1, 2)
				.addSells(48, 64, gaiaItem("premium_monster_feed"), 1, 1, 4)
				.addSells(72, 96, byKey("twilightforest:moonworm_queen"), 1, 1, 2)
				.addSells(96, 128, byKey("alexsmobs:dimensional_carver"), 1, 1, 2)

				.readData(jsonPath(NFFGirlsEntityTypes.)).get());*/

	public static NaUtilsRegistry.Accessor<VanillaTradeListingCollection<?>> HMAG_SLIME_GIRL = TRADE_COLLECTIONS.register(
		NFFGirlsEntityTypes.HMAG_SLIME_GIRL.getId().getPath(), () -> VanillaTradeListingCollectionHelper.newCollection()
			.setCurrency(NFFGirlsItems.EVIL_GEM.get())
			.readData(jsonPath(NFFGirlsEntityTypes.HMAG_SLIME_GIRL)).get());
				/*.setRequiredLevel(1)
				.addBuys(ColoredItems.DYES.objectArray(), 48, 64, 1, 1, 4)
				.addBuys(ColoredItems.DYES.objectArray(), 48, 64, 1, 1, 4)
				.addBuys(ColoredItems.DYES.objectArray(), 48, 64, 1, 1, 4)
				.addBuys(Items.SLIME_BALL, 36, 48, 1, 1, 4)
				.setRequiredLevel(2)
				.addBuys(Items.HONEY_BLOCK, 8, 12, 1, 1, 16)
				.addBuys(ModItems.SWAMPY_STEW.get(), 1, 1, 1, 2, 4)
				.setRequiredLevel(3)
				.addBuys(Items.MAGMA_CREAM, 24, 36, 1, 1, 16)
				.addBuys(byKey("iceandfire:myrmex_desert_resin"), 16, 24, 1, 2, 8)
				.addBuys(byKey("iceandfire:myrmex_jungle_resin"), 16, 24, 1, 2, 8)
				.addSells(4, 8, Items.SLIME_BLOCK, 1, 2, 8)
				.addSells(4, 8, ModItems.CUREBERRY.get(), 1, 2, 8)
				.setRequiredLevel(4)
				.addBuys(NFFGirlsItems.MAGICAL_GEL_BALL.get(), 4, 8, 1, 2, 8)
				.addBuys(byKey("alexsmobs:komodo_spit"), 4, 8, 2, 3, 4)
				.addBuys(byKey("alexsmobs:banana_slug_slime"), 4, 8, 2, 3, 4)
				.addSells(8, 10, ModItems.CUBIC_NUCLEUS.get(), 1, 1, 8)
				.addSells(6, 8, ModItems.FORTUNE_CRYSTAL.get(), 1, 1, 8)
				.setRequiredLevel(5)
				.addBuys(gaiaItem("doll_slime_girl"), 1, 1, 8, 12, 2)
				.addSells(12, 32, ModItems.FORTUNE_CRYSTAL_PLUS.get(), 1, 1, 4)
				.addSellsEnchantmentBook(64, 96, Enchantments.PROJECTILE_PROTECTION, 8, 4)
				.addSells(24, 36, byKey("alexsmobs:rainbow_jelly"), 1, 1, 4)
				.addSells(16, 36, byKey("alexsmobs:mimicream"), 1, 1, 4)

				.readData(jsonPath(NFFGirlsEntityTypes.)).get());*/

	public static NaUtilsRegistry.Accessor<VanillaTradeListingCollection<?>> HMAG_CRIMSON_SLAUGHTERER= TRADE_COLLECTIONS.register(
		NFFGirlsEntityTypes.HMAG_CRIMSON_SLAUGHTERER.getId().getPath(), () -> VanillaTradeListingCollectionHelper.newCollection()
			.setCurrency(NFFGirlsItems.EVIL_GEM.get())
			.readData(jsonPath(NFFGirlsEntityTypes.HMAG_CRIMSON_SLAUGHTERER)).get());
				/*.setRequiredLevel(1)
				.addBuys(Items.GOLD_INGOT, 32, 48, 1, 1, 16)
				.addBuys(Items.NETHERRACK, 56, 64, 1, 1, 16)
				.addBuys(Items.QUARTZ, 48, 64, 1, 1, 16)
				.addBuys(Items.GLOWSTONE_DUST, 32, 48, 1, 1, 16)
				.setRequiredLevel(2)
				.addBuys(Items.CRIMSON_NYLIUM, 56, 64, 1, 1, 16)
				//.addBuys(Items.CRIMSON_STEW, 56, 64, 1, 1, 16)
				.addBuys(Items.CRIMSON_ROOTS, 32, 48, 1, 1, 16)
				.addBuys(Items.CRIMSON_FUNGUS, 32, 48, 1, 1, 16)
				.addBuys(Items.WEEPING_VINES, 32, 48, 1, 1, 16)
				.setRequiredLevel(3)
				.addBuys(byKey("alexsmobs:mosquito_larva"), 16, 24, 1, 2, 8)
				.addBuys(byKey("byg:crimson_berry_pie"), 16, 24, 1, 2, 8)
				.addSells(6, 8, ModItems.GREEDY_CRYSTAL.get(), 1, 1, 8)
				.addSells(6, 8, ModItems.EVIL_THORN.get(), 1, 1, 8)
				.setRequiredLevel(4)
				.addBuys(byKey("iceandfire:fire_dragon_blood"), 2, 4, 3, 4, 4)
				.addBuys(byKey("iceandfire:ice_dragon_blood"), 2, 4, 3, 4, 4)
				.addBuys(byKey("iceandfire:lightning_dragon_blood"), 2, 4, 3, 4, 4)
				.addBuys(byKey("twilightforest:fiery_blood"), 3, 6, 3, 4, 4)
				.addSells(16, 20, ModItems.CRIMSON_BOW.get(), 1, 1, 4)
				.addSells(4, 6, ModItems.EVIL_ARROW.get(), 6, 8, 32)
				.addSells(16, 20, byKey("alexsmobs:blood_sprayer"), 1, 1, 4)
				.setRequiredLevel(5)
				.addSells(12, 32, ModItems.GREEDY_CRYSTAL_PLUS.get(), 1, 1, 4)
				.addSellsEnchantmentBook(56, 84, Enchantments.THORNS, 5, 4)
				.addSells(64, 96, byKey("alexsmobs:hemolymph_blaster"), 1, 1, 2)
				.addSells(64, 96, byKey("twilightforest:lifedrain_scepter"), 1, 1, 2)
	

				.readData(jsonPath(NFFGirlsEntityTypes.)).get());*/

	public static NaUtilsRegistry.Accessor<VanillaTradeListingCollection<?>> HMAG_SNOW_CANINE= TRADE_COLLECTIONS.register(
		NFFGirlsEntityTypes.HMAG_SNOW_CANINE.getId().getPath(), () -> VanillaTradeListingCollectionHelper.newCollection()
			.setCurrency(NFFGirlsItems.EVIL_GEM.get())
			.readData(jsonPath(NFFGirlsEntityTypes.HMAG_SNOW_CANINE)).get());
/*				.setRequiredLevel(1)
				.addBuys(Items.PORKCHOP, 32, 48, 1, 1, 16)
				.addBuys(Items.MUTTON, 32, 48, 1, 1, 16)
				.addBuys(Items.BEEF, 32, 48, 1, 1, 16)
				.addBuys(byKey("alexsmobs:moose_ribs"), 18, 32, 1, 1, 16)
				.addBuys(byKey("alexsmobs:kangaroo_meat"), 18, 32, 1, 1, 16)
				.addBuys(byKey("twilightforest:raw_venison"), 18, 32, 1, 1, 16)
				.setRequiredLevel(2)
				.addBuys(Items.LEATHER, 32, 48, 1, 1, 16)
				.addBuys(Items.BONE, 56, 64, 1, 1, 16)
				.addBuys(byKey("iceandfire:dragonbone"), 12, 18, 1, 2, 16)
				.addBuys(byKey("iceandfire:troll_tusk"), 8, 16, 1, 1, 8)
				.addBuys(gaiaItem("fur"), 12, 16, 1, 2, 8)
				.addSells(4, 6, ModItems.SHARP_FANG.get(), 1, 2, 24)
				.setRequiredLevel(3)
				.addBuys(ModItems.RAVAGER_MEAT.get(), 18, 24, 1, 1, 16)
				.addBuys(byKey("alexsmobs:shark_tooth"), 6, 10, 1, 1, 8)
				.addBuys(byKey("alexsmobs:serrated_shark_tooth"), 6, 10, 1, 1, 8)
				.addBuys(byKey("alexsmobs:sea_serpent_fang"), 6, 10, 1, 1, 8)
				.addBuys(byKey("alexsmobs:cachalot_whale_tooth"), 6, 10, 1, 1, 8)
				.addBuys(byKey("twilightforest:raw_meef"), 12, 18, 1, 1, 16)
				//.addSells(4, 6, ModItems.ORGE_HORN.get(), 1, 1, 16)
				.setRequiredLevel(4)
				.addBuys(byKey("iceandfire:fire_dragon_flesh"), 6, 10, 1, 2, 4)
				.addBuys(byKey("iceandfire:ice_dragon_flesh"), 6, 10, 1, 2, 4)
				.addBuys(byKey("iceandfire:lightning_dragon_flesh"), 6, 10, 1, 2, 4)
				.addBuys(gaiaItem("weapon_book_battle"), 1, 1, 4, 6, 2)
				.addSells(6, 8, ModItems.GREEDY_CRYSTAL.get(), 1, 1, 16)
				.addSells(6, 8, ModItems.REINFORCING_CHAIN.get(), 1, 1, 16)
				.addSells(6, 8, ModItems.REPULSION_GADGET.get(), 1, 1, 16)
				.setRequiredLevel(5)
				.addSells(12, 32, ModItems.MULTIPLEX_REINFORCING_CHAIN.get(), 1, 1, 4)
				.addSells(12, 32, ModItems.GREEDY_CRYSTAL_PLUS.get(), 1, 1, 4)
				//.addSellsEnchantmentBook(72, 112, Enchantments.LOOTING, 5, 4)
				.addSells(56, 72, byKey("twilightforest:diamond_minotaur_axe"), 1, 1, 2)

				.readData(jsonPath(NFFGirlsEntityTypes.)).get());*/

	public static NaUtilsRegistry.Accessor<VanillaTradeListingCollection<?>> HMAG_HARPY= TRADE_COLLECTIONS.register(
		NFFGirlsEntityTypes.HMAG_HARPY.getId().getPath(), () -> VanillaTradeListingCollectionHelper.newCollection()
			.setCurrency(NFFGirlsItems.EVIL_GEM.get())
			.readData(jsonPath(NFFGirlsEntityTypes.HMAG_HARPY)).get());
				/*.setRequiredLevel(1)
				.addBuys(Items.EGG, 12, 16, 1, 1, 8)
				.addBuys(Items.SPIDER_EYE, 28, 36, 1, 1, 16)
				.addBuys(Items.CHICKEN, 24, 36, 1, 1, 16)
				.addBuys(Items.RABBIT, 24, 36, 1, 1, 16)
				.setRequiredLevel(2)
				.addBuys(Items.FEATHER, 32, 48, 1, 1, 16)
				.addBuys(Items.RABBIT_HIDE, 24, 32, 1, 1, 16)
				.addBuys(Items.RABBIT_STEW, 1, 1, 2, 3, 4)
				.addBuys(byKey("twilightforest:raven_feather"), 12, 18, 1, 1, 16)
				.setRequiredLevel(3)
				.addBuys(ModItems.SWAMPER_TENTACLE.get(), 18, 24, 1, 1, 16)
				.addBuys(Items.PHANTOM_MEMBRANE, 12, 16, 1, 2, 16)
				.addBuys(Items.RABBIT_FOOT, 6, 8, 1, 2, 8)
				.addBuys(byKey("alexsmobs:emu_feather"), 8, 12, 1, 1, 8)
				.addBuys(byKey("alexsmobs:roadrunner_feather"), 8, 12, 1, 1, 8)
				.setRequiredLevel(4)
				.addBuys(byKey("iceandfire:stymphalian_bird_feather"), 6, 10, 1, 1, 8)
				.addBuys(byKey("iceandfire:amphithere_feather"), 6, 10, 1, 1, 8)
				.addSells(6, 8, ModItems.FORTUNE_CRYSTAL.get(), 1, 1, 16)
				.addSells(8, 12, Items.TURTLE_EGG, 1, 2, 16)
				.addSells(6, 8, ModItems.REPULSION_GADGET.get(), 1, 1, 16)
				.setRequiredLevel(5)
				.addBuys(gaiaItem("deco_nest_harpy"), 1, 1, 6, 8, 2)
				.addBuys(byKey("twilightforest:peacock_feather_fan"), 1, 1, 6, 8, 4)
				.addSells(36, 64, ModItems.INSOMNIA_FRUIT.get(), 1, 1, 4)
				.addSells(12, 32, ModItems.FORTUNE_CRYSTAL_PLUS.get(), 1, 1, 4)
				.addSellsEnchantmentBook(64, 84, Enchantments.KNOCKBACK, 10, 4)

				.readData(jsonPath(NFFGirlsEntityTypes.)).get());*/

	public static NaUtilsRegistry.Accessor<VanillaTradeListingCollection<?>> HMAG_DODOMEKI= TRADE_COLLECTIONS.register(
		NFFGirlsEntityTypes.HMAG_DODOMEKI.getId().getPath(), () -> VanillaTradeListingCollectionHelper.newCollection()
			.setCurrency(NFFGirlsItems.EVIL_GEM.get())
			.readData(jsonPath(NFFGirlsEntityTypes.HMAG_DODOMEKI)).get());
				/*.setRequiredLevel(1)
				.addBuys(Items.GOLD_INGOT, 24, 32, 1, 2, 16)
				.addBuys(Items.EMERALD, 36, 48, 1, 2, 16)
				.addBuys(Items.DIAMOND, 18, 24, 1, 2, 16)
				.addBuys(byKey("iceandfire:silver_ingot"), 24, 32, 1, 2, 16)
				.addBuys(byKey("iceandfire:sapphire_gem"), 24, 32, 1, 2, 16)
				.setRequiredLevel(2)
				.addBuys(gaiaItem("shiny_pearl"), 18, 24, 1, 2, 16)
				.addSells(4, 8, Items.EXPERIENCE_BOTTLE, 8, 12, 16)
				.addSells(8, 10, Items.ENCHANTED_GOLDEN_APPLE, 1, 1, 8)
				.setRequiredLevel(3)
				.addBuys(ModItems.SOUL_APPLE.get(), 3, 5, 1, 1, 8)
				.addSells(6, 8, ModItems.FORTUNE_CRYSTAL.get(), 2, 3, 4)
				.addSells(6, 8, ModItems.GREEDY_CRYSTAL.get(), 2, 3, 4)
				.addSells(8, 10, ModItems.LICH_CLOTH.get(), 2, 3, 4)
				.setRequiredLevel(4)
				//.addBuys(NFFGirlsItems.SOUL_CAKE.get(), 1, 1, 4, 6, 8)
				.addSells(8, 12, Items.NETHERITE_INGOT, 1, 2, 8)
				.addSells(8, 12, ModItems.FORTUNE_CRYSTAL_PLUS.get(), 1, 2, 2)
				.addSells(8, 12, ModItems.GREEDY_CRYSTAL_PLUS.get(), 1, 2, 2)
				.setRequiredLevel(5)
				.addSells(6, 8, Items.TOTEM_OF_UNDYING, 1, 1, 4)
				.addSells(10, 14, Items.NETHER_STAR, 1, 2, 4)
				//.addSellsEnchantmentBook(64, 96, Enchantments.LOOTING, 6, 2)
				//.addSellsEnchantmentBook(64, 96, Enchantments.FORTUNE, 6, 2)

				.readData(jsonPath(NFFGirlsEntityTypes.)).get());*/

	public static NaUtilsRegistry.Accessor<VanillaTradeListingCollection<?>> HMAG_IMP= TRADE_COLLECTIONS.register(
		NFFGirlsEntityTypes.HMAG_IMP.getId().getPath(), () -> VanillaTradeListingCollectionHelper.newCollection()
			.setCurrency(NFFGirlsItems.EVIL_GEM.get())
			.readData(jsonPath(NFFGirlsEntityTypes.HMAG_IMP)).get());
				/*.setRequiredLevel(1)
				.addBuys(Items.GOLD_INGOT, 28, 36, 1, 1, 16)
				.addBuys(Items.OBSIDIAN, 18, 28, 1, 1, 16)
				.addBuys(Items.GLOWSTONE_DUST, 36, 48, 1, 1, 16)
				.addBuys(Items.QUARTZ, 36, 48, 1, 1, 16)
				.setRequiredLevel(2)
				.addBuys(gaiaItem("nether_wart_jam"), 8, 16, 1, 1, 8)
				.addBuys(Items.MAGMA_CREAM, 12, 24, 1, 1, 16)
				.addBuys(Items.GHAST_TEAR, 8, 16, 1, 1, 16)
				.addBuys(Items.BLAZE_ROD, 32, 36, 1, 1, 16)
				.addBuys(Items.CRYING_OBSIDIAN, 12, 16, 1, 1, 16)
				.setRequiredLevel(3)
				.addBuys(gaiaItem("fireshard"), 8, 16, 1, 2, 8)
				.addBuys(ModItems.FIRE_BOTTLE.get(), 8, 12, 1, 1, 8)
				//.addSells(8, 12, Items.ANCINET_DEBRIS, 1, 1, 16)
				.addSells(6, 8, ModItems.CRIMSON_CUTICULA.get(), 1, 1, 16)
				.addSells(6, 8, ModItems.BURNING_CORE.get(), 1, 1, 16)
				.setRequiredLevel(4)
				.addBuys(gaiaItem("soulfire"), 4, 8, 2, 3, 8)
				.addBuys(byKey("twilightforest:fiery_ingot"), 16, 24, 2, 3, 16)
				.addBuys(Items.MUSIC_DISC_PIGSTEP, 1, 1, 3, 4, 4)
				.addSells(10, 16, Items.NETHER_STAR, 1, 1, 4)
				.addSells(6, 8, ModItems.FORTUNE_CRYSTAL.get(), 1, 1, 8)
				.addSells(6, 8, ModItems.EVIL_FLAME.get(), 1, 1, 8)
				.setRequiredLevel(5)
				.addBuys(NFFGirlsItems.NETHERITE_FORK.get(), 1, 1, 6, 8, 2)
				.addSells(12, 32, ModItems.FORTUNE_CRYSTAL_PLUS.get(), 1, 1, 8)
				//.addSellsEnchantmentBook(96, 128, Enchantments.FORTUNE, 8, 2)

				.readData(jsonPath(NFFGirlsEntityTypes.)).get());*/

	public static NaUtilsRegistry.Accessor<VanillaTradeListingCollection<?>> HMAG_GLARYAD= TRADE_COLLECTIONS.register(
		NFFGirlsEntityTypes.HMAG_GLARYAD.getId().getPath(), () -> VanillaTradeListingCollectionHelper.newCollection()
			.setCurrency(NFFGirlsItems.EVIL_GEM.get())
			.readData(jsonPath(NFFGirlsEntityTypes.HMAG_GLARYAD)).get());
			/*	.setRequiredLevel(1)
				.addBuys(Items.AZALEA_LEAVES, 48, 56, 1, 1, 16)
				.addBuys(Items.MOSS_BLOCK, 56, 64, 1, 1, 16)
				.addBuys(Items.AZALEA, 24, 32, 1, 1, 16)
				.addBuys(Items.SMALL_DRIPLEAF, 16, 24, 1, 1, 16)
				.setRequiredLevel(2)
				.addBuys(byKey("twilightforest:torchberries"), 24, 32, 1, 1, 16)
				.addBuys(Items.FLOWERING_AZALEA_LEAVES, 36, 48, 1, 2, 16)
				.addBuys(Items.GLOW_BERRIES, 36, 42, 1, 1, 16)
				.addBuys(Items.FLOWERING_AZALEA, 16, 24, 1, 2, 16)
				.addBuys(Items.BIG_DRIPLEAF, 16, 24, 1, 1, 16)
				.setRequiredLevel(3)
				.addBuys(Items.HANGING_ROOTS, 24, 32, 1, 1, 16)
				.addBuys(gaiaItem("taproot"), 16, 24, 1, 1, 16)
				.addBuys(byKey("twilightforest:liveroot"), 24, 32, 1, 1, 16)
				.addBuys(byKey("twilightforest:steeleaf_ingot"), 16, 24, 1, 2, 8)
				.addSells(6, 10, Items.POINTED_DRIPSTONE, 3, 5, 8)
				.addSells(6, 8, ModItems.SOUL_APPLE.get(), 2, 4, 16)
				.setRequiredLevel(4)
				.addBuys(gaiaItem("mandrake"), 6, 12, 2, 3, 8)
				.addSells(6, 8, ModItems.MYSTERIOUS_PETAL.get(), 1, 1, 8)
				.addSells(4, 6, ModItems.CUREBERRY.get(), 1, 2, 16)
				.addSells(4, 6, ModItems.EXP_BERRY.get(), 1, 2, 16)
				.addSells(4, 6, ModItems.RANDOMBERRY.get(), 1, 2, 16)
				.setRequiredLevel(5)
				.addBuys(gaiaItem("deco_mandragora_pot"), 1, 1, 4, 6, 2)
				.addBuys(byKey("alexsmobs:potted_flutter"), 1, 1, 4, 6, 2)
				.addSells(10, 18, NFFGirlsItems.ENDERBERRY.get(), 1, 1, 8)
				.addSells(12, 32, ModItems.PURIFICATION_CLOTH.get(), 1, 1, 4)
				.addSellsEnchantmentBook(84, 112, Enchantments.UNBREAKING, 10, 4)

				.readData(jsonPath(NFFGirlsEntityTypes.)).get());*/

	public static NaUtilsRegistry.Accessor<VanillaTradeListingCollection<?>> HMAG_JIANGSHI= TRADE_COLLECTIONS.register(
		NFFGirlsEntityTypes.HMAG_JIANGSHI.getId().getPath(), () -> VanillaTradeListingCollectionHelper.newCollection()
			.setCurrency(NFFGirlsItems.EVIL_GEM.get())
			.readData(jsonPath(NFFGirlsEntityTypes.HMAG_JIANGSHI)).get());
			/*	.setRequiredLevel(1)
				.addBuys(Items.LILY_PAD, 48, 56, 1, 1, 16)
				.addBuys(Items.MANGROVE_ROOTS, 32, 48, 1, 1, 16)
				.addBuys(Items.BAMBOO, 48, 56, 1, 1, 16)
				.addBuys(Items.BLUE_ORCHID, 32, 48, 1, 1, 16)
				.setRequiredLevel(2)
				.addBuys(Items.CANDLE, 24, 36, 1, 2, 16)
				.addBuys(Items.COPPER_INGOT, 36, 56, 1, 1, 16)
				.addBuys(byKey("iceandfire:lightning_lily"), 16, 24, 1, 1, 8)
				.addSells(2, 4, ModItems.SOUL_POWDER.get(), 4, 8, 8)
				.setRequiredLevel(3)
				.addBuys(ModItems.SOUL_APPLE.get(), 3, 5, 1, 1, 8)
				.addBuys(Items.BELL, 1, 2, 3, 5, 4)
				.addBuys(Items.LIGHTNING_ROD, 24, 36, 1, 2, 16)
				.addBuys(byKey("iceandfire:lightning_dragon_blood"), 4, 8, 1, 2, 8)
				.addSells(6, 8, ModItems.LICH_CLOTH.get(), 1, 1, 8)
				.setRequiredLevel(4)
				.addBuys(byKey("iceandfire:lightning_dragon_heart"), 3, 5, 4, 6, 4)
				.addBuys(byKey("alexsmobs:unsettling_kimono"), 1, 1, 4, 6, 2)
				.addSells(4, 6, ModItems.SHARP_FANG.get(), 1, 2, 16)
				.setRequiredLevel(5)
				.addSells(10, 28, ModItems.PURIFICATION_CLOTH.get(), 1, 1, 8)
				.addSells(8, 10, Items.TOTEM_OF_UNDYING, 1, 1, 4)
				.addSellsEnchantmentBook(84, 112, Enchantments.SMITE, 10, 4)
				.addSellsEnchantmentBook(36, 48, Enchantments.CHANNELING, 1, 4)
				.addSells(16, 32, byKey("iceandfire:dragonforge_lightning_input"), 1, 1, 2)

				.readData(jsonPath(NFFGirlsEntityTypes.)).get());*/

	public static NaUtilsRegistry.Accessor<VanillaTradeListingCollection<?>> HMAG_NIGHTWALKER= TRADE_COLLECTIONS.register(
		NFFGirlsEntityTypes.HMAG_NIGHTWALKER.getId().getPath(), () -> VanillaTradeListingCollectionHelper.newCollection()
			.setCurrency(NFFGirlsItems.EVIL_GEM.get())
			.readData(jsonPath(NFFGirlsEntityTypes.HMAG_NIGHTWALKER)).get());
				/*.setRequiredLevel(1)
				.addBuys(Items.TERRACOTTA, 36, 48, 1, 1, 16)
				.addBuys(Items.BRICKS, 36, 48, 1, 1, 16)
				.addBuys(Items.NETHER_BRICKS, 48, 56, 1, 1, 16)
				.setRequiredLevel(2)
				.addSells(6, 8, Items.RAW_COPPER_BLOCK, 3, 5, 8)
				.addSells(6, 8, Items.RAW_IRON_BLOCK, 2, 4, 8)
				.addSells(6, 8, Items.RAW_GOLD_BLOCK, 1, 3, 8)
				.setRequiredLevel(3)
				.addBuys(Items.WHITE_GLAZED_TERRACOTTA, 16, 28, 1, 1, 16)
				.addBuys(Items.ORANGE_GLAZED_TERRACOTTA, 16, 28, 1, 1, 16)
				.addBuys(Items.MAGENTA_GLAZED_TERRACOTTA, 16, 28, 1, 1, 16)
				.addBuys(Items.LIGHT_BLUE_GLAZED_TERRACOTTA, 16, 28, 1, 1, 16)
				.addBuys(Items.YELLOW_GLAZED_TERRACOTTA, 16, 28, 1, 1, 16)
				.addBuys(Items.LIME_GLAZED_TERRACOTTA, 16, 28, 1, 1, 16)
				.addBuys(Items.PINK_GLAZED_TERRACOTTA, 16, 28, 1, 1, 16)
				.addBuys(Items.GRAY_GLAZED_TERRACOTTA, 16, 28, 1, 1, 16)
				.addBuys(Items.LIGHT_GRAY_GLAZED_TERRACOTTA, 16, 28, 1, 1, 16)
				.addBuys(Items.CYAN_GLAZED_TERRACOTTA, 16, 28, 1, 1, 16)
				.addBuys(Items.PURPLE_GLAZED_TERRACOTTA, 16, 28, 1, 1, 16)
				.addBuys(Items.BLUE_GLAZED_TERRACOTTA, 16, 28, 1, 1, 16)
				.addBuys(Items.BROWN_GLAZED_TERRACOTTA, 16, 28, 1, 1, 16)
				.addBuys(Items.GREEN_GLAZED_TERRACOTTA, 16, 28, 1, 1, 16)
				.addBuys(Items.RED_GLAZED_TERRACOTTA, 16, 28, 1, 1, 16)
				.addBuys(Items.BLACK_GLAZED_TERRACOTTA, 16, 28, 1, 1, 16)
				.setRequiredLevel(4)
				//.addBuys(NFFGirlsItems.LUMINOUS_TERRACOTTA.get(), 12, 16, 1, 2, 8)
				.addBuys(byKey("alexsmobs:straddlite"), 12, 16, 1, 2, 8)
				.addSells(4, 6, ModItems.TOTEM_OF_REPULSE.get(), 1, 1, 8)
				.addSells(12, 32, ModItems.REPULSION_GADGET.get(), 1, 1, 4)
				.setRequiredLevel(5)
				.addSells(24, 36, ModItems.NETHERITE_SCRAP_BLOCK.get(), 1, 1, 4)
				.addSells(24, 36, ModItems.ANCIENT_STONE_BLOCK.get(), 1, 1, 4)
			;*/

}
