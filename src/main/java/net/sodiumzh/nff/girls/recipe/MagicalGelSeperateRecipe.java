package net.sodiumzh.nff.girls.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.sodiumzh.nff.girls.registry.NFFGirlsItems;
import net.sodiumzh.nff.girls.registry.NFFGirlsRecipes;

public class MagicalGelSeperateRecipe extends SimpleModificationRecipe
{

	public MagicalGelSeperateRecipe(ResourceLocation id, CraftingBookCategory category)
	{
		super(id, category);
	}
	
	public MagicalGelSeperateRecipe(ResourceLocation pId)
	{
		super(pId);
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return NFFGirlsRecipes.MAGICAL_GEL_SEPERATE.get();
	}

	@Override
	public boolean isSubjectItem(ItemStack stack) {
		return stack.is(NFFGirlsItems.MAGICAL_GEL_BOTTLE.get()) && NFFGirlsItems.MAGICAL_GEL_BOTTLE.get().getAmount(stack) > 1;
	}

	@Override
	public boolean isModifierItem(ItemStack stack) {
		return stack.is(NFFGirlsItems.EMPTY_MAGICAL_GEL_BOTTLE.get());
	}

	@Override
	public ItemStack getResult(ItemStack subject, ItemStack modifier) {
		ItemStack res = subject.copy();
		NFFGirlsItems.MAGICAL_GEL_BOTTLE.get().setAmount(res, NFFGirlsItems.MAGICAL_GEL_BOTTLE.get().getAmount(subject) / 2);
		return res;
	}

	@Override
	public ItemStack getSubjectRemaining(ItemStack subject, ItemStack modifier)
	{
		ItemStack res = subject.copy();
		NFFGirlsItems.MAGICAL_GEL_BOTTLE.get().setAmount(res, NFFGirlsItems.MAGICAL_GEL_BOTTLE.get().getAmount(subject) - NFFGirlsItems.MAGICAL_GEL_BOTTLE.get().getAmount(subject) / 2);
		return res;
	}
}
