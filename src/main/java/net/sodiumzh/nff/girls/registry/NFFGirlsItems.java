package net.sodiumzh.nff.girls.registry;

import java.util.HashSet;
import java.util.Optional;
import java.util.function.Supplier;

import com.github.mechalopa.hmag.registry.ModItems;
import com.github.mechalopa.hmag.world.item.ModSwordItem;

import com.mojang.datafixers.util.Either;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sodiumzh.nautils.compat.ModDependencyFallbackItem;
import net.sodiumzh.nautils.item.NaUtilsItem;
import net.sodiumzh.nautils.statics.NaUtilsCompatStatics;
import net.sodiumzh.nautils.statics.NaUtilsInfoStatics;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.item.CommandingWandItem;
import net.sodiumzh.nff.girls.item.EmptyMagicalGelBottleItem;
import net.sodiumzh.nff.girls.item.EvilMagnetItem;
import net.sodiumzh.nff.girls.item.FavorabilityModifierItem;
import net.sodiumzh.nff.girls.item.MagicalGelBallItem;
import net.sodiumzh.nff.girls.item.MagicalGelBottleItem;
import net.sodiumzh.nff.girls.item.NFFGirlsRespawnerItem;
import net.sodiumzh.nff.girls.item.NFFTamingProgressProbeItem;
import net.sodiumzh.nff.girls.item.NecromancerArmorItem;
import net.sodiumzh.nff.girls.item.NecromancerWandItem;
import net.sodiumzh.nff.girls.item.PeachWoodSwordItem;
import net.sodiumzh.nff.girls.item.ReinforcedFishingRodItem;
import net.sodiumzh.nff.girls.item.TaoistTalismanItem;
import net.sodiumzh.nff.girls.item.TradeIntroductionLetterItem;
import net.sodiumzh.nff.girls.item.TransferringTagItem;
import net.sodiumzh.nff.girls.item.XPModifierItem;
import net.sodiumzh.nff.girls.item.*;
import net.sodiumzh.nff.girls.subsystem.baublesystem.baubles.AquaJadeBaubleItem;
import net.sodiumzh.nff.girls.subsystem.baublesystem.baubles.CourageAmuletBaubleItem;
import net.sodiumzh.nff.girls.subsystem.baublesystem.baubles.HealingJadeBaubleItem;
import net.sodiumzh.nff.girls.subsystem.baublesystem.baubles.LifeJadeBaubleItem;
import net.sodiumzh.nff.girls.subsystem.baublesystem.baubles.PoisonousThornBaubleItem;
import net.sodiumzh.nff.girls.subsystem.baublesystem.baubles.ResistanceAmuletBaubleItem;
import net.sodiumzh.nff.girls.subsystem.baublesystem.baubles.SoulAmuletBaubleItem;
import net.sodiumzh.nff.services.item.MobCatcherItem;
import net.sodiumzh.nff.services.item.NFFMobRespawnerItem;

public class NFFGirlsItems {
	
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, NFFGirls.MOD_ID);
	public static final HashSet<RegistryObject<? extends Item>> NO_TAB = new HashSet<>();
	// General register function for items
	
	/** 
	 * Register basic item with properties, not supporting item subclasses
	 * @deprecated use {@code registerItem} instead
	 */
	@Deprecated
	public static RegistryObject<Item> regItem(String name, Item.Properties properties)
	{
		return ITEMS.register(name, ()->new Item(properties)); //Demos only
	}

	@Deprecated
	public static <T extends Item> RegistryObject<T> registerItem(String name, Class<T> clazz, boolean tab, Supplier<T> supplier)
	{
		var res = ITEMS.register(name, supplier);
		if (!tab)
			NO_TAB.add(res);
		return res;
	}
	
	@Deprecated
	public static <T extends Item> RegistryObject<T> registerItem(String name, Class<T> clazz, Supplier<T> supplier)
	{
		return registerItem(name, clazz, true, supplier);
	}

	protected static <T extends Item> RegistryObject<T> register(String name, Supplier<T> itemSupplier)
	{
		return ITEMS.register(name, itemSupplier);
	}
	
	protected static <T extends Item> RegistryObject<T> registerNoTab(String name, Supplier<T> itemSupplier)
	{
		RegistryObject<T> res = ITEMS.register(name, itemSupplier);
		NO_TAB.add(res);
		return res;
	}

	public static RegistryObject<Item> registerDefault(String name)
	{
		return register(name, () -> new Item(new Item.Properties()));
	}

	public static RegistryObject<Item> registerDefaultNoTab(String name)
	{
		return registerNoTab(name, () -> new Item(new Item.Properties()));
	}

	public static <T extends Item> Either<RegistryObject<T>, RegistryObject<ModDependencyFallbackItem>>
		registerDepending(boolean tab, String key, String dependingModId, Supplier<T> supplier)
	{
		Either<RegistryObject<T>, RegistryObject<ModDependencyFallbackItem>> res =
				NaUtilsCompatStatics.registerModDependentOrElse(ITEMS, key, dependingModId, supplier,
						() -> new ModDependencyFallbackItem(dependingModId, new Item.Properties()));
		res.ifLeft(obj -> {if (!tab) NO_TAB.add(obj);}).ifRight(obj -> {if (!tab) NO_TAB.add(obj);});
		return res;
	}
	/************************************/
	/* Item Registering, with constants */ 
	/************************************/

	// Crafting intermediates
	public static final RegistryObject<Item> DEATH_CRYSTAL = register("death_crystal", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> DEATH_CRYSTAL_POWDER = registerDefault("death_crystal_powder");
	public static final RegistryObject<Item> SOUL_FLOUR = registerDefault("soul_flour");
	public static final RegistryObject<Item> SOUL_CLOTH = registerDefault("soul_cloth");
	public static final RegistryObject<Item> ENDER_FRUIT_JAM = register("ender_fruit_jam", () -> new Item(new Item.Properties().rarity(Rarity.RARE).craftRemainder(Items.GLASS_BOTTLE)));
	public static final RegistryObject<Item> EVIL_GEM = registerDefault("evil_gem");
	
	// Foods
	public static final RegistryObject<Item> SOUL_CAKE_SLICE = register("soul_cake_slice", () -> new Item(new Item.Properties().food(NFFGirlsFoodProperties.SOUL_CAKE_SLICE).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> ENDERBERRY = register("enderberry", () -> new EnderberryItem(NFFGirlsBlocks.ENDERBERRY_BUSH.get(),
		new Item.Properties().food(NFFGirlsFoodProperties.ENDERBERRY).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> ENDER_PIE = ITEMS.register("ender_pie", () -> new Item(new Item.Properties().food(NFFGirlsFoodProperties.ENDER_PIE).rarity(Rarity.RARE)));

	// Baubles
	// Desc utils
	public static Supplier<MutableComponent> baubleHPRecovery(double rawValue) {
		return () -> NaUtilsInfoStatics.createTranslatable("info.nffgirls.bauble.healing_per_second", 
				String.format("%.2f", NFFGirlsConfigs.ValueCache.Baubles.BAUBLE_HEALTH_RECOVERY_SCALE * rawValue)).withStyle(ChatFormatting.GRAY); 
	}
	public static Supplier<MutableComponent> baubleHPMax(double rawValue) {
		return () -> NaUtilsInfoStatics.createTranslatable("info.nffgirls.bauble.hpmax", 
			String.format("+%.1f", NFFGirlsConfigs.ValueCache.Baubles.BAUBLE_MAX_HP_BOOSTING_SCALE * rawValue)).withStyle(ChatFormatting.GRAY); 
	}
	public static Supplier<MutableComponent> baubleAtk(double rawValue) {
		return () -> NaUtilsInfoStatics.createTranslatable("info.nffgirls.bauble.atk", 
				String.format("+%.1f", NFFGirlsConfigs.ValueCache.Baubles.BAUBLE_ATK_BOOSTING_SCALE * rawValue)).withStyle(ChatFormatting.GRAY); 
	}
	public static Supplier<MutableComponent> baubleArmor(double rawValue) {
		return () -> NaUtilsInfoStatics.createTranslatable("info.nffgirls.bauble.armor", 
				String.format("+%.1f", NFFGirlsConfigs.ValueCache.Baubles.BAUBLE_ARMOR_BOOSTING_SCALE * rawValue)).withStyle(ChatFormatting.GRAY); 
	}

	// Registry
	public static final RegistryObject<SoulAmuletBaubleItem> SOUL_AMULET = register("soul_amulet", () -> new SoulAmuletBaubleItem(
			NFFGirls.MOD_ID + ":soul_amulet", 1, new Item.Properties().rarity(Rarity.UNCOMMON))
			.description(NaUtilsInfoStatics.createTranslatable("info.nffgirls.bauble.soul_amulet").withStyle(ChatFormatting.GRAY))
			.description(baubleHPMax(10.0))
			.description(baubleAtk(3.0))
			.description(NaUtilsInfoStatics.createTranslatable("info.nffgirls.bauble.sun_immune").withStyle(ChatFormatting.GRAY)).cast());
	public static final RegistryObject<SoulAmuletBaubleItem> SOUL_AMULET_II = register("soul_amulet_ii", () -> new SoulAmuletBaubleItem(
			NFFGirls.MOD_ID + ":soul_amulet", 2, new Item.Properties().rarity(Rarity.RARE)).alwaysFoil()
			.description(NaUtilsInfoStatics.createTranslatable("info.nffgirls.bauble.soul_amulet").withStyle(ChatFormatting.GRAY))
			.description(baubleHPMax(15.0))
			.description(baubleAtk(5.0))
			.description(NaUtilsInfoStatics.createTranslatable("info.nffgirls.bauble.speed", "+10%").withStyle(ChatFormatting.GRAY))
			.description(NaUtilsInfoStatics.createTranslatable("info.nffgirls.bauble.sun_immune").withStyle(ChatFormatting.GRAY)).cast());
	public static final RegistryObject<SoulAmuletBaubleItem> SOUL_AMULET_III = register("soul_amulet_iii", () -> new SoulAmuletBaubleItem(
			NFFGirls.MOD_ID + ":soul_amulet", 3, new Item.Properties().rarity(Rarity.RARE))
			.description(NaUtilsInfoStatics.createTranslatable("info.nffgirls.bauble.soul_amulet").withStyle(ChatFormatting.GRAY))
			.description(baubleHPMax(25.0))
			.description(baubleAtk(8.0))
			.description(NaUtilsInfoStatics.createTranslatable("info.nffgirls.bauble.speed", "+15%").withStyle(ChatFormatting.GRAY))
			.description(NaUtilsInfoStatics.createTranslatable("info.nffgirls.bauble.sun_immune").withStyle(ChatFormatting.GRAY)).cast());
	public static final RegistryObject<SoulAmuletBaubleItem> SOUL_AMULET_IV = register("soul_amulet_iv", () -> new SoulAmuletBaubleItem(
			NFFGirls.MOD_ID + ":soul_amulet", 4, new Item.Properties().rarity(Rarity.EPIC)).alwaysFoil()
			.description(NaUtilsInfoStatics.createTranslatable("info.nffgirls.bauble.soul_amulet").withStyle(ChatFormatting.GRAY))
			.description(baubleHPMax(40.0))
			.description(baubleAtk(12.0))
			.description(NaUtilsInfoStatics.createTranslatable("info.nffgirls.bauble.speed", "+20%").withStyle(ChatFormatting.GRAY))
			.description(baubleHPRecovery(0.1))

			.description(NaUtilsInfoStatics.createTranslatable("info.nffgirls.bauble.sun_immune").withStyle(ChatFormatting.GRAY)).cast());
	public static final RegistryObject<CourageAmuletBaubleItem> COURAGE_AMULET = register("courage_amulet", () -> new CourageAmuletBaubleItem(
			NFFGirls.MOD_ID + ":courage_amulet", 1, new Item.Properties().rarity(Rarity.UNCOMMON))
			.description(NaUtilsInfoStatics.createTranslatable("info.nffgirls.bauble.proactive_attack").withStyle(ChatFormatting.GRAY))
			.description(baubleAtk(4.0))
			.description(NaUtilsInfoStatics.createTranslatable("info.nffgirls.bauble.speed", "+20%").withStyle(ChatFormatting.GRAY)).cast());
	public static final RegistryObject<CourageAmuletBaubleItem> COURAGE_AMULET_II = register("courage_amulet_ii", () -> new CourageAmuletBaubleItem(
			NFFGirls.MOD_ID + ":courage_amulet", 2, new Item.Properties().rarity(Rarity.RARE)).alwaysFoil()
			.description(NaUtilsInfoStatics.createTranslatable("info.nffgirls.bauble.proactive_attack").withStyle(ChatFormatting.GRAY))
			.description(baubleAtk(6.0))
			.description(NaUtilsInfoStatics.createTranslatable("info.nffgirls.bauble.speed", "+30%").withStyle(ChatFormatting.GRAY)).cast());
	public static final RegistryObject<ResistanceAmuletBaubleItem> RESISTANCE_AMULET = register("resistance_amulet", () -> new ResistanceAmuletBaubleItem(
			NFFGirls.MOD_ID + ":resistance_amulet", 1, new Item.Properties().rarity(Rarity.UNCOMMON))
			.description(baubleArmor(4.0))
			.description(baubleHPMax(15.0))
			.description(NaUtilsInfoStatics.createTranslatable("info.nffgirls.bauble.sun_immune").withStyle(ChatFormatting.GRAY)).cast());
	public static final RegistryObject<ResistanceAmuletBaubleItem> RESISTANCE_AMULET_II = register("resistance_amulet_ii", () -> new ResistanceAmuletBaubleItem(
			NFFGirls.MOD_ID + ":resistance_amulet", 2, new Item.Properties().rarity(Rarity.UNCOMMON)).alwaysFoil()
			.description(baubleArmor(6.0))
			.description(baubleHPMax(25.0))
			.description(NaUtilsInfoStatics.createTranslatable("info.nffgirls.bauble.sun_immune").withStyle(ChatFormatting.GRAY)).cast());
	public static final RegistryObject<HealingJadeBaubleItem> HEALING_JADE = register("healing_jade", () -> new HealingJadeBaubleItem(
			NFFGirls.MOD_ID + ":healing_jade", 1, new Item.Properties().rarity(Rarity.UNCOMMON))
			.description(baubleHPRecovery(0.1)).cast());
	public static final RegistryObject<LifeJadeBaubleItem> LIFE_JADE = register("life_jade", () -> new LifeJadeBaubleItem(
			NFFGirls.MOD_ID + ":life_jade", 1, new Item.Properties().rarity(Rarity.UNCOMMON))
			.description(baubleHPRecovery(0.15))
			.description(baubleHPMax(5.0)).cast());
	public static final RegistryObject<LifeJadeBaubleItem> LIFE_JADE_II = register("life_jade_ii", () -> new LifeJadeBaubleItem(
			NFFGirls.MOD_ID + ":life_jade", 2, new Item.Properties().rarity(Rarity.RARE)).alwaysFoil()
			.description(baubleHPRecovery(0.2))
			.description(baubleHPMax(10.0)).cast());
	public static final RegistryObject<AquaJadeBaubleItem> AQUA_JADE = register("aqua_jade", () -> new AquaJadeBaubleItem(
			NFFGirls.MOD_ID + ":aqua_jade", 1, new Item.Properties().rarity(Rarity.UNCOMMON))
			.description(NaUtilsInfoStatics.createTranslatable("info.nffgirls.bauble.aqua_jade").withStyle(ChatFormatting.GRAY))
			.description(NaUtilsInfoStatics.createTranslatable("info.nffgirls.bauble.in_water").withStyle(ChatFormatting.GRAY))
			.description(NaUtilsInfoStatics.createTranslatable("info.nffgirls.bauble.speed", "4x").withStyle(ChatFormatting.GRAY))
			.description(baubleHPRecovery(0.25)).cast());
	public static final RegistryObject<PoisonousThornBaubleItem> POISONOUS_THORN = register("poisonous_thorn", () -> new PoisonousThornBaubleItem(
			NFFGirls.MOD_ID + ":poisonous_thorn", 1, new Item.Properties().rarity(Rarity.UNCOMMON))
			.description(NaUtilsInfoStatics.createTranslatable("info.nffgirls.bauble.poisonous_thorn").withStyle(ChatFormatting.GRAY))
			.description(NaUtilsInfoStatics.createTranslatable("info.nffgirls.bauble.poisonous_thorn_1").withStyle(ChatFormatting.GRAY)).cast());


	
	// Equipment & tools
	public static final RegistryObject<Item> NECROMANCER_HAT = register("necromancer_hat", () -> new NecromancerArmorItem(
			NFFGirlsArmorMaterials.NECROMANCER,
			EquipmentSlot.HEAD,
			new Item.Properties().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> SUNHAT = register("sunhat", () -> new ArmorItem(
			NFFGirlsArmorMaterials.SUNHAT,
			ArmorItem.Type.HELMET, 
			new Item.Properties()));
	public static final RegistryObject<Item> NETHERITE_FORK = register("netherite_fork", () -> new ModSwordItem(Tiers.NETHERITE, 2.0F, -2.4F, new Item.Properties().fireResistant()));
	public static final RegistryObject<Item> NECROMANCER_WAND = register("necromancer_wand", () -> new NecromancerWandItem(		new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1)));
	public static final RegistryObject<CommandingWandItem> COMMANDING_WAND = register("commanding_wand", () ->
			new CommandingWandItem(new Item.Properties().stacksTo(1))
					.descTranslatable("desc.nffgirls.item.commanding_wand_0").cast());
	public static final RegistryObject<CombatCommandingWandItem> COMBAT_COMMANDING_WAND = register("combat_commanding_wand",
			() -> new CombatCommandingWandItem(new Item.Properties().stacksTo(1)));
	@Deprecated
	public static final RegistryObject<EvilMagnetItem> EVIL_MAGNET = register("evil_magnet", () -> new EvilMagnetItem(new Item.Properties().stacksTo(1))
			.descTranslatable("info.nffgirls.item.deprecated_recover_ingredients").cast());
	public static final RegistryObject<SwordItem> PEACH_WOOD_SWORD = register("peach_wood_sword", () -> new PeachWoodSwordItem(Tiers.WOOD, 0, -2.4F, (new Item.Properties()).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<ReinforcedFishingRodItem> REINFORCED_FISHING_ROD = register("reinforced_fishing_rod", () -> new ReinforcedFishingRodItem(new Item.Properties().durability(256)));
	
	// Utility items
	public static final RegistryObject<TransferringTagItem> TRANSFERRING_TAG = register("transferring_tag", () -> new TransferringTagItem(new Item.Properties()));
	public static final RegistryObject<EmptyMagicalGelBottleItem> EMPTY_MAGICAL_GEL_BOTTLE = register("empty_magical_gel_bottle", () -> new EmptyMagicalGelBottleItem(new Item.Properties()));
	public static final RegistryObject<MagicalGelBallItem> MAGICAL_GEL_BALL = register("magical_gel_ball", () -> new MagicalGelBallItem(new Item.Properties()));
	public static final RegistryObject<MagicalGelBottleItem> MAGICAL_GEL_BOTTLE = registerNoTab("magical_gel_bottle", () -> new MagicalGelBottleItem(new Item.Properties())
		.redirectDefaultInstance(EMPTY_MAGICAL_GEL_BOTTLE).setGiveCommandUsesDefaultInstance().cast());
	public static final RegistryObject<TaoistTalismanItem> TAOIST_TALISMAN = register("taoist_talisman", () -> new TaoistTalismanItem(new Item.Properties()));
	public static final RegistryObject<TradeIntroductionLetterItem> TRADE_INTRODUCTION_LETTER = register("trade_introduction_letter",
			() -> new TradeIntroductionLetterItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
	
	// Misc
	public static final RegistryObject<NFFMobRespawnerItem> MOB_RESPAWNER = registerNoTab("mob_respawner", () -> new NFFGirlsRespawnerItem(new Item.Properties()).setRetainBefriendedMobInventory(false)
		.setRetainBefriendedMobInventory(false).noDefaultInstance(false).cast());
	public static final RegistryObject<NFFMobRespawnerItem> MOB_STORAGE_POD = registerNoTab("mob_storage_pod", () -> new NFFGirlsRespawnerItem(new Item.Properties())
		.redirectDefaultInstance(new ResourceLocation(NFFGirls.MOD_ID, "empty_mob_storage_pod")).cast());
	public static final RegistryObject<MobCatcherItem> EMPTY_MOB_STORAGE_POD = register("empty_mob_storage_pod", () -> new MobCatcherItem(new Item.Properties(), MOB_STORAGE_POD.get())
		.canCatchCondition((m, p) -> (m instanceof INFFGirlsTamed bm && bm.getOwnerUUID().equals(p.getUUID()))));

	// Technical
	public static final RegistryObject<Item> TAB_ICON = registerDefaultNoTab("tab_icon");
	
	// Debug
	public static final RegistryObject<NFFTamingProgressProbeItem> BEFRIENDING_PROGRESS_PROBE =
			registerNoTab("taming_progress_probe", () -> new NFFTamingProgressProbeItem(new Item.Properties().rarity(Rarity.EPIC)));
	public static final RegistryObject<XPModifierItem> EXP_MODIFIER = 
			registerNoTab("exp_modifier", () -> new XPModifierItem(new Item.Properties().rarity(Rarity.EPIC)));
	public static final RegistryObject<FavorabilityModifierItem> FAVORABILITY_MODIFIER =
			registerNoTab("favorability_modifier", () -> new FavorabilityModifierItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
	/* Item register end */

	// Other mod depending

	public static final Either<RegistryObject<CitadelBasedMobDictionaryItem>, RegistryObject<ModDependencyFallbackItem>> MOB_DICTIONARY =
			registerDepending(true,"mob_dictionary", "citadel",
			() -> new CitadelBasedMobDictionaryItem(new Item.Properties().stacksTo(1)));

	/*static
	{
		CITADEL_MOB_DICT = Optional.ofNullable(ModList.get().isLoaded("citadel") ?
				ITEMS.register("mob_dictionary_citadel", () -> new CitadelBasedMobDictionaryItem(new Item.Properties().stacksTo(1).tab(TAB))) : null);
	}*/


	public static void register(IEventBus eventBus) {
		ITEMS.register(eventBus);
	}

}
