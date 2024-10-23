package net.sodiumzh.nff.girls.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Mob;
import net.sodiumzh.nautils.entity.ItemApplyingToMobTable;
import net.sodiumzh.nautils.math.RangedRandomDouble;
import net.sodiumzh.nautils.registries.NaUtilsFunctions;

import java.util.function.Function;

public class NFFGirlsDataReaders {

    public static void readItemApplyingToMobTable(JsonElement json, ItemApplyingToMobTable.Builder builder)
    {
        try {
            for (JsonElement element: json.getAsJsonArray())
            {
                try {
                    JsonObject obj = element.getAsJsonObject();

                    JsonElement itemJson = obj.get("item");
                    String item = itemJson != null ? obj.get("item").getAsString() : null;
                    String tag = item != null ? null : obj.get("tag").getAsString();
                    if (item == null && tag == null) throw new RuntimeException();

                    JsonElement amountJson = obj.get("amount");
                    JsonElement amountGetterJson = obj.get("amount_getter");
                    Function<Mob, Double> getter = null;
                    if (amountJson != null) {
                        JsonArray amountJsonArray = obj.get("amount").getAsJsonArray();
                        double[] amountArray = new double[amountJsonArray.size()];
                        for (int i = 0; i < amountArray.length; ++i) amountArray[i] = amountJsonArray.get(i).getAsDouble();
                        getter = mob -> RangedRandomDouble.fromArrayRepresentation(amountArray).get();
                    }
                    else if (amountGetterJson != null)
                    {
                        getter = mob -> NaUtilsFunctions.invoke(new ResourceLocation(amountGetterJson.getAsString()), mob).castTo(Double.class).doubleValue();
                    }
                    else throw new IllegalStateException("Missing amount or getter");

                    if (item != null) builder.add(new ResourceLocation(item), getter);
                    else builder.add(TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(tag)), getter);
                }
                catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }
}

