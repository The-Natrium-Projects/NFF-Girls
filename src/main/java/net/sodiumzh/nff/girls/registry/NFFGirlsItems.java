package net.sodiumzh.nff.girls.registry;

import com.github.mechalopa.hmag.world.item.ModSwordItem;
import com.mojang.datafixers.util.Either;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.item.*;
import net.sodiumzh.nff.services.item.MobCatcherItem;
import net.sodiumzh.nff.services.item.NFFMobRespawnerItem;
import net.sodiumzh.nfu.compat.ModDependencyFallbackItem;
import net.sodiumzh.nfu.util.NFUCompatStatics;

import java.util.function.Supplier;

public class NFFGirlsItems {
	
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, NFFGirls.MOD_ID);
	public static final CreativeModeTab TAB = NFFGirlsTabs.MAIN_TAB;
	// General register function for items

	protected static <T extends Item> RegistryObject<T> register(String name, Supplier<T> itemSupplier)
	{
		return ITEMS.register(name, itemSupplier);
	}

	public static RegistryObject<Item> registerDefault(String name)
	{
		return register(name, () -> new Item(new Item.Properties().tab(TAB)));
	}

	public static RegistryObject<Item> registerDefaultNoTab(String name)
	{
		return register(name, () -> new Item(new Item.Properties()));
	}

	public static <T extends Item> Either<RegistryObject<T>, RegistryObject<ModDependencyFallbackItem>>
		registerDepending(boolean tab, String key, String dependingModId, Supplier<T> supplier)
	{
		Supplier<ModDependencyFallbackItem> fallback;
		if (tab) fallback = () -> new ModDependencyFallbackItem(dependingModId, new Item.Properties().tab(TAB));
		else fallback = () -> new ModDependencyFallbackItem(dependingModId, new Item.Properties().tab(TAB));
		Either<RegistryObject<T>, RegistryObject<ModDependencyFallbackItem>> res =
				NFUCompatStatics.registerModDependentOrElse(ITEMS, key, dependingModId, supplier, fallback);
		return res;
	}
	/************************************/
	/* Item Registering, with constants */ 
	/************************************/

	// Crafting intermediates
	public static final RegistryObject<Item> DEATH_CRYSTAL = register("death_crystal", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON).tab(TAB)));
	public static final RegistryObject<Item> DEATH_CRYSTAL_POWDER = registerDefault("death_crystal_powder");
	public static final RegistryObject<Item> SOUL_FLOUR = registerDefault("soul_flour");
	public static final RegistryObject<Item> SOUL_CLOTH = registerDefault("soul_cloth");
	public static final RegistryObject<Item> ENDER_FRUIT_JAM = register("ender_fruit_jam", () -> new Item(new Item.Properties().rarity(Rarity.RARE).craftRemainder(Items.GLASS_BOTTLE).tab(TAB)));
	public static final RegistryObject<Item> EVIL_GEM = registerDefault("evil_gem");
	
	// Foods
	public static final RegistryObject<Item> SOUL_CAKE_SLICE = register("soul_cake_slice", () -> new Item(new Item.Properties().food(NFFGirlsFoodProperties.SOUL_CAKE_SLICE).rarity(Rarity.UNCOMMON).tab(TAB)));
	public static final RegistryObject<Item> ENDERBERRY = register("enderberry", () -> new EnderberryItem(NFFGirlsBlocks.ENDERBERRY_BUSH.get(),
		new Item.Properties().food(NFFGirlsFoodProperties.ENDERBERRY).rarity(Rarity.UNCOMMON).tab(TAB)));
	public static final RegistryObject<Item> ENDER_PIE = ITEMS.register("ender_pie", () -> new Item(new Item.Properties().food(NFFGirlsFoodProperties.ENDER_PIE).rarity(Rarity.RARE).tab(TAB)));



	
	// Equipment & tools
	public static final RegistryObject<Item> NECROMANCER_HAT = register("necromancer_hat", () -> new NecromancerArmorItem(
			NFFGirlsArmorMaterials.NECROMANCER,
			EquipmentSlot.HEAD,
			new Item.Properties().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> SUNHAT = register("sunhat", () -> new ArmorItem(
			NFFGirlsArmorMaterials.SUNHAT,
			EquipmentSlot.HEAD,
			new Item.Properties().tab(TAB)));
	public static final RegistryObject<Item> NETHERITE_FORK = register("netherite_fork", () -> new ModSwordItem(Tiers.NETHERITE, 2.0F, -2.4F, new Item.Properties().fireResistant().tab(TAB)));
	public static final RegistryObject<Item> NECROMANCER_WAND = register("necromancer_wand", () -> new NecromancerWandItem(		new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1).tab(TAB)));
	public static final RegistryObject<CommandingWandItem> COMMANDING_WAND = register("commanding_wand", () ->
			new CommandingWandItem(new Item.Properties().stacksTo(1).tab(TAB))
					.descTranslatable("desc.nffgirls.item.commanding_wand").cast());
	public static final RegistryObject<CombatCommandingWandItem> COMBAT_COMMANDING_WAND = register("combat_commanding_wand",
			() -> new CombatCommandingWandItem(new Item.Properties().stacksTo(1).tab(TAB))
					.descTranslatable("desc.nffgirls.item.combat_commanding_wand").cast());
	@Deprecated
	public static final RegistryObject<EvilMagnetItem> EVIL_MAGNET = register("evil_magnet", () -> new EvilMagnetItem(new Item.Properties().stacksTo(1).tab(TAB))
			.descTranslatable("info.nffgirls.item.deprecated_recover_ingredients").cast());
	public static final RegistryObject<ReinforcedFishingRodItem> REINFORCED_FISHING_ROD = register("reinforced_fishing_rod", () -> new ReinforcedFishingRodItem(new Item.Properties().durability(256).tab(TAB)));
	
	// Utility items
	public static final RegistryObject<TransferringTagItem> TRANSFERRING_TAG = register("transferring_tag", () -> new TransferringTagItem(new Item.Properties().tab(TAB)));
	public static final RegistryObject<EmptyMagicalGelBottleItem> EMPTY_MAGICAL_GEL_BOTTLE = register("empty_magical_gel_bottle", () -> new EmptyMagicalGelBottleItem(new Item.Properties().tab(TAB)));
	public static final RegistryObject<MagicalGelBallItem> MAGICAL_GEL_BALL = register("magical_gel_ball", () -> new MagicalGelBallItem(new Item.Properties().tab(TAB)));
	public static final RegistryObject<MagicalGelBottleItem> MAGICAL_GEL_BOTTLE = register("magical_gel_bottle", () -> new MagicalGelBottleItem(new Item.Properties())
		.redirectDefaultInstance(EMPTY_MAGICAL_GEL_BOTTLE).setGiveCommandUsesDefaultInstance().cast());
	public static final RegistryObject<TradeIntroductionLetterItem> TRADE_INTRODUCTION_LETTER = register("trade_introduction_letter",
			() -> new TradeIntroductionLetterItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<AttackingStrategyListItem> ATTACKING_STRATEGY_LIST = register("attacking_strategy_list", () ->
		new AttackingStrategyListItem(new Item.Properties()).foilCondition(stack -> !AttackingStrategyListItem.getStrategy(stack).isEmpty()).cast());

	// Misc
	public static final RegistryObject<NFFMobRespawnerItem> MOB_RESPAWNER = register("mob_respawner", () -> new NFFGirlsRespawnerItem(new Item.Properties()).setRetainBefriendedMobInventory(false)
		.setRetainBefriendedMobInventory(false).noDefaultInstance(false).cast());
	public static final RegistryObject<NFFMobRespawnerItem> MOB_STORAGE_POD = register("mob_storage_pod", () -> new NFFGirlsRespawnerItem(new Item.Properties())
		.setGiveCommandUsesDefaultInstance()
		.redirectDefaultInstance(new ResourceLocation(NFFGirls.MOD_ID, "empty_mob_storage_pod")).cast());
	public static final RegistryObject<MobCatcherItem> EMPTY_MOB_STORAGE_POD = register("empty_mob_storage_pod", () -> new NFFGirlsMobCatcherItem(new Item.Properties().tab(TAB), MOB_STORAGE_POD.get())
		.canCatchCondition((m, p) -> (m instanceof INFFGirlsTamed bm && bm.getOwnerUUID().equals(p.getUUID()))));

	// Technical
	public static final RegistryObject<Item> TAB_ICON = registerDefaultNoTab("tab_icon");
	
	// Debug
	public static final RegistryObject<NFFTamingProgressProbeItem> BEFRIENDING_PROGRESS_PROBE =
			register("taming_progress_probe", () -> new NFFTamingProgressProbeItem(new Item.Properties().rarity(Rarity.EPIC)));
	public static final RegistryObject<XPModifierItem> EXP_MODIFIER = 
			register("exp_modifier", () -> new XPModifierItem(new Item.Properties().rarity(Rarity.EPIC)));
	public static final RegistryObject<FavorabilityModifierItem> FAVORABILITY_MODIFIER =

			register("favorability_modifier", () -> new FavorabilityModifierItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
	/* Item register end */

	// Other mod depending

	public static final Either<RegistryObject<CitadelBasedMobDictionaryItem>, RegistryObject<ModDependencyFallbackItem>> MOB_DICTIONARY =
			registerDepending(true,"mob_dictionary", "citadel",
			() -> new CitadelBasedMobDictionaryItem(new Item.Properties().stacksTo(1).tab(TAB),
				new ResourceLocation("nffgirls:book/mob_dictionary/root.json"),
				"dict.nffgirls.title", "nffgirls:book/mob_dictionary/"));

	/*static
	{
		CITADEL_MOB_DICT = Optional.ofNullable(ModList.get().isLoaded("citadel") ?
				ITEMS.register("mob_dictionary_citadel", () -> new CitadelBasedMobDictionaryItem(new Item.Properties().stacksTo(1).tab(TAB))) : null);
	}*/


	public static void register(IEventBus eventBus) {
		ITEMS.register(eventBus);
	}

}
