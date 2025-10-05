package net.sodiumzh.nff.girls.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.sodiumzh.nff.girls.NFFGirls;

import javax.annotation.Nonnull;

@JeiPlugin
public class NFFGirlsJeiPlugin implements IModPlugin
{
    public static final RecipeType<NFFGirlsTradeJeiInfo> TRADES =
        RecipeType.create(NFFGirls.MOD_ID, "trades", NFFGirlsTradeJeiInfo.class);
    /*public static final RecipeType<MobApplicableItemTable> HEALING_ITEMS =
        RecipeType.create(NFFGirls.MOD_ID, "healing_items", MobApplicableItemTable.class);
    public static final RecipeType<MobApplicableItemTable> TAMING_ITEMS =
        RecipeType.create(NFFGirls.MOD_ID, "taming_items", MobApplicableItemTable.class);*/

    public NFFGirlsJeiPlugin(){}

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(NFFGirls.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerRecipes(@Nonnull IRecipeRegistration registration) {
        NFFGirlsJeiStatics.ALL_TRADE_ENTRIES.get().getIfValidated().ifPresent(
            allEntries -> registration.addRecipes(TRADES, allEntries.keySet().stream().map(NFFGirlsTradeJeiInfo::new).toList())
        );
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new NFFGirlsTradeJeiCategory(registration.getJeiHelpers().getGuiHelper()));
    }

}
