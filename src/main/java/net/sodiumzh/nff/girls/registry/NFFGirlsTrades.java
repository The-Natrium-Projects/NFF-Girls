package net.sodiumzh.nff.girls.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.data.NFFGirlsDataReaders;
import net.sodiumzh.nfu.entity.vanillatrade.VanillaTradeListing;
import net.sodiumzh.nfu.entity.vanillatrade.VanillaTradeListingCollection;
import net.sodiumzh.nfu.entity.vanillatrade.VanillaTradeListingCollectionHelper;
import net.sodiumzh.nfu.entity.vanillatrade.VanillaTradeRegistry;
import net.sodiumzh.nfu.registry.NFURegistries;
import net.sodiumzh.nfu.registry.NFURegistry;
import net.sodiumzh.nfu.registry.NFURegistryEntryCollection;
import net.sodiumzh.nfu.registry.NFURegistryGenerateValuesEvent;
import net.sodiumzh.nfu.util.NFUDataStatics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = NFFGirls.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class NFFGirlsTrades
{

	// REGISTRIES
	
	public static NFURegistryEntryCollection<VanillaTradeRegistry> TRADE_REGISTRIES =
		NFURegistryEntryCollection.create(NFURegistries.VANILLA_TRADE_REGISTRIES, NFFGirls.MOD_ID);
	
	public static final NFURegistry.Accessor<VanillaTradeRegistry> TRADE_REGISTRY =
		TRADE_REGISTRIES.register("trade_registry", () -> 
		new VanillaTradeRegistry().readData(new ResourceLocation(NFFGirls.MOD_ID, "trades/trade_registry.json")));

	// COLLECTIONS
	
	
	public static NFURegistryEntryCollection<VanillaTradeListingCollection<?>> TRADE_COLLECTIONS =
		NFURegistryEntryCollection.create(NFURegistries.VANILLA_TRADE_LISTING_COLLECTIONS, NFFGirls.MOD_ID);

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


	@SubscribeEvent
	public static void readJsonTrades(NFURegistryGenerateValuesEvent.ServerBefore event) {
		// Collect separate trade entries
		if (event.registry.equals(NFURegistries.VANILLA_TRADE_LISTINGS)) {
			List<ResourceLocation> allLocations =
				NFUDataStatics.getJsonLocationsUnderPath(LogicalSide.SERVER, "trades",
					l -> l.getPath().equals("trades/entry_registry.json"));
			Map<String, NFURegistryEntryCollection<VanillaTradeListing>> collections = new HashMap<>();
			allLocations.stream().map(ResourceLocation::getNamespace).forEach(ns ->
				collections.put(ns, NFURegistryEntryCollection.create(NFURegistries.VANILLA_TRADE_LISTINGS, ns)));
			allLocations.forEach(l -> NFUDataStatics.readJsons(LogicalSide.SERVER, l, je -> {
					if (!je.isJsonArray()) return;
					NFFGirlsDataReaders.readTradeListings(l.getNamespace(), je.getAsJsonArray())
						.forEach((location, listing) ->
							collections.get(location.getNamespace()).register(location.getPath(), () -> listing));
			}));
			collections.values().forEach(NFURegistryEntryCollection::merge);
		}
		// Collect unregistered trade collections
		else if (event.registry.equals(NFURegistries.VANILLA_TRADE_LISTING_COLLECTIONS)) {
			// Get all collection ids that are not registered
			List<ResourceLocation> allKeys = NFUDataStatics.getJsonLocationsUnderPath(LogicalSide.SERVER, "trades").stream()
				.map(loc -> {
					if (loc.getPath().startsWith("trades/") && loc.getPath().endsWith(".json"))
						return new ResourceLocation(loc.getNamespace(), loc.getPath().substring(7, loc.getPath().length() - 5));
					else return null;
				})
				.filter(Objects::nonNull)
				.filter(str -> !str.getPath().equals("trade_registry") && !str.getPath().equals("entries"))
				.filter(key -> !TRADE_COLLECTIONS.hasKey(key))
				.toList();

			Map<String, NFURegistryEntryCollection<VanillaTradeListingCollection<?>>> collections = new HashMap<>();
			allKeys.stream().map(ResourceLocation::getNamespace).collect(Collectors.toSet())
				.forEach(key -> collections.put(key, NFURegistryEntryCollection.create(NFURegistries.VANILLA_TRADE_LISTING_COLLECTIONS, key)));
			allKeys.forEach((ResourceLocation key) -> collections.get(key.getNamespace()).register(key.getPath(),
					() -> VanillaTradeListingCollectionHelper.newCollection()
						.setCurrency(NFFGirlsItems.EVIL_GEM.get())
						.readData(jsonPath(key)).get()));
			collections.values().forEach(NFURegistryEntryCollection::merge);
		}
	}

}
