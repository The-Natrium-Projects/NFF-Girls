package net.sodiumzh.nff.girls.registry;

import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.recipe.MagicalGelEnchantRecipe;
import net.sodiumzh.nff.girls.recipe.MagicalGelSeperateRecipe;
import net.sodiumzh.nff.girls.recipe.MagicalGelStainRecipe;

public class NFFGirlsRecipes
{
	public static final DeferredRegister<RecipeSerializer<?>> RECIPES = DeferredRegister.create(Registry.RECIPE_SERIALIZER_REGISTRY, NFFGirls.MOD_ID);
	
	public static final RegistryObject<RecipeSerializer<?>> MAGICAL_GEL_STAIN = RECIPES.register("magical_gel_stain", () -> new SimpleRecipeSerializer<>(MagicalGelStainRecipe::new));
	public static final RegistryObject<RecipeSerializer<?>> MAGICAL_GEL_SEPERATE = RECIPES.register("magical_gel_seperate", () -> new SimpleRecipeSerializer<>(MagicalGelSeperateRecipe::new));
	public static final RegistryObject<RecipeSerializer<?>> MAGICAL_GEL_ENCHANT = RECIPES.register("magical_gel_enchant", () -> new SimpleRecipeSerializer<>(MagicalGelEnchantRecipe::new));
}
