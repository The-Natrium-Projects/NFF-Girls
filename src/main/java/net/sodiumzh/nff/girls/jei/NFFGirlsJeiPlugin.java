package net.sodiumzh.nff.girls.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.jei.item.FriendingItemJeiCategory;
import net.sodiumzh.nff.girls.jei.item.FriendingItemJeiMobEntry;
import net.sodiumzh.nff.girls.jei.item.HealingItemJeiCategory;
import net.sodiumzh.nff.girls.jei.item.HealingItemJeiMobEntry;
import net.sodiumzh.nff.girls.jei.trade.NFFGirlsTradeJeiCategory;
import net.sodiumzh.nff.girls.jei.trade.NFFGirlsTradeJeiMobEntry;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.stream.Collectors;

@JeiPlugin
public class NFFGirlsJeiPlugin implements IModPlugin
{
    public static final RecipeType<NFFGirlsTradeJeiMobEntry> TRADES =
        RecipeType.create(NFFGirls.MOD_ID, "trades", NFFGirlsTradeJeiMobEntry.class);
    public static final RecipeType<HealingItemJeiMobEntry> HEALING_ITEMS =
        RecipeType.create(NFFGirls.MOD_ID, "healing_items", HealingItemJeiMobEntry.class);
    public static final RecipeType<FriendingItemJeiMobEntry> FRIENDING_ITEMS =
        RecipeType.create(NFFGirls.MOD_ID, "friending_items", FriendingItemJeiMobEntry.class);

    public NFFGirlsJeiPlugin(){}

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(NFFGirls.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerRecipes(@Nonnull IRecipeRegistration registration) {
        NFFGirlsJeiStatics.ALL_HEALING_ITEM_TABLES.get().getIfValidated().ifPresent(allEntries ->
            registration.addRecipes(HEALING_ITEMS, allEntries.keySet().stream()
                .sorted(Comparator.comparing(e -> e.getDescription().getString()))
                .map(HealingItemJeiMobEntry::new).toList()));
        NFFGirlsJeiStatics.ALL_FRIENDING_ITEM_TABLES.get().getIfValidated().ifPresent(allEntries ->
            registration.addRecipes(FRIENDING_ITEMS, allEntries.keySet().stream()
                .sorted(Comparator.comparing(e -> e.getDescription().getString()))
                .map(FriendingItemJeiMobEntry::new).toList()));
        NFFGirlsJeiStatics.ALL_TRADE_ENTRIES.get().getIfValidated().ifPresent(allEntries ->
            registration.addRecipes(TRADES, allEntries.keySet().stream()
                .sorted(Comparator.comparing(e -> e.getDescription().getString()))
                .map(NFFGirlsTradeJeiMobEntry::new).toList())
        );
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(
            new NFFGirlsTradeJeiCategory(registration.getJeiHelpers().getGuiHelper()),
            new HealingItemJeiCategory(registration.getJeiHelpers().getGuiHelper()),
            new FriendingItemJeiCategory(registration.getJeiHelpers().getGuiHelper())
        );
    }

}
