package net.sodiumzh.nff.girls.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.sodiumzh.nff.girls.registry.NFFGirlsHealingItems;
import net.sodiumzh.nff.girls.registry.NFFGirlsItems;
import net.sodiumzh.nfu.annotation.DontCallManually;
import net.sodiumzh.nfu.entity.MobApplicableItemTable;
import net.sodiumzh.nfu.entity.vanillatrade.VanillaTradeListing;
import net.sodiumzh.nfu.entity.vanillatrade.VanillaTradeListingCollectionHelper;
import net.sodiumzh.nfu.function.RegistrableFunction;
import net.sodiumzh.nfu.function.RegistrablePredicate;
import net.sodiumzh.nfu.math.RandomSelection;
import net.sodiumzh.nfu.math.RangedRandomDouble;
import net.sodiumzh.nfu.math.RangedRandomInt;
import net.sodiumzh.nfu.registry.NFUFunctions;
import net.sodiumzh.nfu.registry.NFURegistries;
import net.sodiumzh.nfu.util.NFUDataStatics;
import org.checkerframework.checker.units.qual.A;

import javax.swing.text.html.Option;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class NFFGirlsDataReaders {

    public static void readMobApplicableItemTable(JsonElement json, MobApplicableItemTable.Builder builder)
    {
        try {
            for (JsonElement element: json.getAsJsonArray())
            {
                try {
                    if (!element.isJsonObject()) continue; // This skips some random stuff like string descriptions.
                    JsonObject obj = element.getAsJsonObject();

                    // Get criteria
                    MobApplicableItemTable.ItemStackCriteria criteria = MobApplicableItemTable.ItemStackCriteria.create();
                    criteria.addItems(NFUDataStatics.getOptionalList(obj, "item", JsonElement::getAsString));
                    criteria.addItems(NFUDataStatics.getOptionalList(obj, "items", JsonElement::getAsString));
                    criteria.addTags(NFUDataStatics.getOptionalList(obj, "tag", JsonElement::getAsString));
                    criteria.addTags(NFUDataStatics.getOptionalList(obj, "tags", JsonElement::getAsString));
                    NFUDataStatics.getOptional(obj, "item_predicate", elem ->
                        new ResourceLocation(elem.getAsString())).ifPresent(criteria::setRegistrablePredicate);
                    if (criteria.getAllUsableItems().isEmpty())
                        continue;

                    // Get output info
                    /*
                     * Format of "amount":
                     * <p>[double] or [double, double] or [double, double, double]: {@link RangedRandomDouble#fromArrayRepresentation(double[])};
                     * <p>[double, [double, double], [double, double], ...]: {@link RandomSelection<Double>}, the first value is fallback value, and other pairs are [value, probability].
                     */
                    //JsonElement amountJson = obj.get("amount");
                    //JsonElement amountGetterJson = obj.get("amount_getter");
                    MobApplicableItemTable.DoubleValueProvider amountProvider =
                        NFUDataStatics.getOptional(obj, "amount", j -> j).map(NFFGirlsDataReaders::parseDoubleProvider).orElseGet(() ->
                            NFUDataStatics.getOptionalString(obj, "amount_getter").map(str -> MobApplicableItemTable.DoubleValueProvider.functionKey(new ResourceLocation(str)))
                                .orElse(MobApplicableItemTable.DoubleValueProvider.INVALID));
                    MobApplicableItemTable.IntValueProvider cooldown =
                        NFUDataStatics.getOptionalInt(obj, "cooldown")
                            .map(MobApplicableItemTable.IntValueProvider::singleNumber)
                            .filter(MobApplicableItemTable.IntValueProvider::isValid)
                            .orElse(MobApplicableItemTable.IntValueProvider.singleNumber(NFFGirlsHealingItems.DEFAULT_COOLDOWN));
                    if (!amountProvider.isValid())
                        continue;
                    boolean consume = NFUDataStatics.getOptional(obj, "consume", JsonElement::getAsBoolean).orElse(true);
                    builder.add(criteria, new MobApplicableItemTable.OutcomeProvider(amountProvider, cooldown).setNoConsume(!consume));
                }
                catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    private static MobApplicableItemTable.DoubleValueProvider parseDoubleProvider(JsonElement e)
    {
        JsonArray array = e.getAsJsonArray();
        switch (array.size())
        {
            case 0: throw new IllegalArgumentException("Illegal array size 0");
            case 1: {
                return MobApplicableItemTable.DoubleValueProvider.singleNumber(array.get(0).getAsDouble());
            }
            default: {
                if (array.get(1).isJsonArray()) // For RandomSelection<Double>
                {
                    RandomSelection<Double> selection = new RandomSelection<>(array.get(0).getAsDouble());
                    for (int i = 1; i < array.size(); ++i)
                        selection.add(array.get(i).getAsJsonArray().get(0).getAsDouble(), array.get(i).getAsJsonArray().get(1).getAsDouble());
                    return MobApplicableItemTable.DoubleValueProvider.randomSelection(selection);
                }
                else {  // For RangedRandomDouble
                    double[] doubleArray = new double[array.size()];
                    for (int i = 0; i < array.size(); ++i)
                        doubleArray[i] = array.get(i).getAsDouble();
                    RangedRandomDouble supplier = RangedRandomDouble.fromArrayRepresentation(doubleArray);
                    return MobApplicableItemTable.DoubleValueProvider.range(supplier);
                }
            }
        }
    }

    public static Map<ResourceLocation, VanillaTradeListing> readTradeListings(String namespace, JsonArray from) {
        Map<ResourceLocation, VanillaTradeListing> res = new HashMap<>();
        from.asList().stream().filter(JsonElement::isJsonObject).map(JsonElement::getAsJsonObject)
            .forEach(jo -> {
                try {
                    String key = NFUDataStatics.getOptionalString(jo, "key").orElse(null);
                    if (key == null) return;
                    if (key.contains(":")) {
                        String[] split = key.split(":");
                        key = split[split.length - 1];
                    }
                    String key1 = key;
                    VanillaTradeListingCollectionHelper.readListing(jo, NFFGirlsItems.EVIL_GEM.get().getDefaultInstance(),
                            false, 0.5d, 1)
                        .ifPresent(listing -> res.put(new ResourceLocation(namespace, key1), listing));
                } catch (RuntimeException ignore) {}
            });
        return res;
    }


}

