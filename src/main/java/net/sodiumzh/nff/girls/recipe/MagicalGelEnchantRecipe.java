package net.sodiumzh.nff.girls.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.sodiumzh.nff.girls.registry.NFFGirlsItems;
import net.sodiumzh.nff.girls.registry.NFFGirlsRecipes;

public class MagicalGelEnchantRecipe extends SimpleModificationRecipe
{

	public MagicalGelEnchantRecipe(ResourceLocation pId)
	{
		super(pId);
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return NFFGirlsRecipes.MAGICAL_GEL_ENCHANT.get();
	}

	@Override
	public boolean isSubjectItem(ItemStack stack) {
		return stack.is(NFFGirlsItems.MAGICAL_GEL_BOTTLE.get());
	}

	@Override
	public boolean isModifierItem(ItemStack stack) {
		return stack.is(Items.SLIME_BALL);
	}

	@Override
	public ItemStack getResult(ItemStack subject, ItemStack modifier) {
		return NFFGirlsItems.MAGICAL_GEL_BALL.get().getDefaultInstance();
	}

	@Override
	public ItemStack getSubjectRemaining(ItemStack subject, ItemStack modifier) {
		if (NFFGirlsItems.MAGICAL_GEL_BOTTLE.get().getAmount(subject) == 1)
			return NFFGirlsItems.EMPTY_MAGICAL_GEL_BOTTLE.get().getDefaultInstance();
		else
		{
			ItemStack res = subject.copy();
			NFFGirlsItems.MAGICAL_GEL_BOTTLE.get().setAmount(res, NFFGirlsItems.MAGICAL_GEL_BOTTLE.get().getAmount(subject) - 1);
			return res;
		}
	}

}
