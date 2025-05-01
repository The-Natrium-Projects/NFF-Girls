package net.sodiumzh.nff.girls.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Mob;
import net.sodiumzh.nfu.entity.MobApplicableItemTable;
import net.sodiumzh.nfu.math.RandomSelection;
import net.sodiumzh.nfu.math.RangedRandomDouble;
import net.sodiumzh.nfu.registry.NFUFunctions;

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
                    JsonElement itemJson = obj.get("item");
                    String item = itemJson != null ? obj.get("item").getAsString() : null;
                    String tag = item != null ? null : obj.get("tag").getAsString();
                    String predicate = (item == null && tag == null) ? obj.get("item_predicate").getAsString() : null;
                    if (item == null && tag == null && predicate == null) {
                        LogUtils.getLogger().warn("Reading MobApplicableItemTable failed: Missing item info.");
                        continue;
                    }

                    /**
                     * Format of "amount":
                     * <p>[double] or [double, double] or [double, double, double]: {@link RangedRandomDouble#fromArrayRepresentation(double[])};
                     * <p>[double, [double, double], [double, double], ...]: {@link RandomSelection<Double>}, the first value is fallback value, and other pairs are [value, probability].
                     *
                     */
                    JsonElement amountJson = obj.get("amount");
                    JsonElement amountGetterJson = obj.get("amount_getter");
                    Function<Mob, Double> getter = null;
                    if (amountJson != null) {
                        getter = parseAmount(amountJson);
                    }
                    else if (amountGetterJson != null)
                    {
                        getter = mob -> NFUFunctions.invoke(new ResourceLocation(amountGetterJson.getAsString()), mob).castTo(Double.class).doubleValue();
                    }
                    else {
                        LogUtils.getLogger().warn(String.format("Reading MobApplicableItemTable failed: Missing amount info for %s \"%s\".",
                                item != null ? "item" : tag != null ? "tag" : "item predicate",
                                item != null ? item : tag != null ? tag : predicate));
                        continue;
                    }
                    if (item != null)
                        builder.add(new ResourceLocation(item), getter);
                    else if (tag != null)
                        builder.add(TagKey.create(Registries.ITEM, new ResourceLocation(tag)), getter);
                    else if (predicate != null)
                        builder.add(stack -> NFUFunctions.invoke(new ResourceLocation(predicate), stack)
                                .castTo(Boolean.class).booleanValue(), getter);
                    else {
                        LogUtils.getLogger().warn("Reading MobApplicableItemTable failed: Missing item info.");
                    }
                    if (element.getAsJsonObject().has("consume")
                        && !element.getAsJsonObject().get("consume").getAsBoolean())
                        builder.noConsume();
                }
                catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    private static Function<Mob, Double> parseAmount(JsonElement e)
    {
        JsonArray array = e.getAsJsonArray();
        switch (array.size())
        {
            case 0: throw new IllegalArgumentException("Illegal array size 0");
            case 1: {
                RangedRandomDouble supplier = RangedRandomDouble.fromArrayRepresentation(new double[]{array.get(0).getAsDouble()});
                return mob -> supplier.get();
            }
            default: {
                if (array.get(1).isJsonArray()) // For RandomSelection<Double>
                {
                    RandomSelection<Double> selection = new RandomSelection<>(array.get(0).getAsDouble());
                    for (int i = 1; i < array.size(); ++i)
                        selection.add(array.get(i).getAsJsonArray().get(0).getAsDouble(), array.get(i).getAsJsonArray().get(1).getAsDouble());
                    return mob -> selection.select(mob.getRandom());
                }
                else {  // For RangedRandomDouble
                    double[] doubleArray = new double[array.size()];
                    for (int i = 0; i < array.size(); ++i)
                        doubleArray[i] = array.get(i).getAsDouble();
                    RangedRandomDouble supplier = RangedRandomDouble.fromArrayRepresentation(doubleArray);
                    return mob -> supplier.get();
                }
            }
        }
    }

}

