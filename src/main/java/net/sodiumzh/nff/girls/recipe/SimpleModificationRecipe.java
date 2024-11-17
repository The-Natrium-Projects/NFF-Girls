package net.sodiumzh.nff.girls.recipe;

import java.util.Optional;

import com.mojang.logging.LogUtils;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.level.Level;

/**
 * A template for crafting recipes that modifies a given item with a modifier item.
 * For example, repairing an equipment item with an ingredient item.
 */

public abstract class SimpleModificationRecipe extends CustomRecipe
{

	public SimpleModificationRecipe(ResourceLocation pId)
	{
		super(pId);
	}

	/**
	 * Get if an item stack can be the modification subject.
	 */
	public abstract boolean isSubjectItem(ItemStack stack);
	
	/**
	 * Get if an item stack can be the modifier item.
	 */
	public abstract boolean isModifierItem(ItemStack stack);
	
	/**
	 * Get the crafting result.
	 */
	public abstract ItemStack getResult(ItemStack subject, ItemStack modifier);	
	
	/**
	 * Get the remaining item after crafting at the subject position.
	 */
	public abstract ItemStack getSubjectRemaining(ItemStack subject, ItemStack modifier);
	
	/**
	 * If true, it will check if an item stack is ambiguous i.e. can be both subject and modifier, and exclude the case. 
	 * Otherwise the role will be always regarded as the subject.
	 */
	public boolean checkAmbiguity()
	{
		return true;
	}
	
	protected Optional<SubjectAndModifier> getIngredients(CraftingContainer container)
	{
		ItemStack subject = null;
		ItemStack modifier = null;
		for (int i = 0; i < container.getContainerSize(); ++i)
		{
			if (isSubjectItem(container.getItem(i)))
			{
				if (checkAmbiguity() && isModifierItem(container.getItem(i)))
				{
					LogUtils.getLogger().error("SimpleModificationRecipe: Ambiguous item detected. This item can be either a subject or a modifier, so the role cannot be determined. This is probably a bug. Recipe ID: " 
							+ getId() + "; Item: " + container.getItem(i).toString());
					return Optional.empty();
				}
				else if (subject == null)
					subject = container.getItem(i);
				else return Optional.empty();
			}
			else if (isModifierItem(container.getItem(i)))
			{
				if (modifier == null)
					modifier = container.getItem(i);
				else return Optional.empty();
			}
			else if (!container.getItem(i).isEmpty())
				return Optional.empty();
		}
		return (subject != null && modifier != null) ? Optional.of(new SubjectAndModifier(subject, modifier)) : Optional.empty();
	}
	
	@Override
	public boolean matches(CraftingContainer container, Level level) {
		return getIngredients(container).isPresent();
	}
	
	@Override
	public ItemStack assemble(CraftingContainer container) {
		Optional<SubjectAndModifier> ingredients = getIngredients(container);
		if (ingredients.isEmpty())
			return ItemStack.EMPTY;
		else return getResult(ingredients.get().subject, ingredients.get().modifier);

	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
		NonNullList<ItemStack> nonnulllist = NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);
		SubjectAndModifier ingredients = getIngredients(container).get();
		for (int i = 0; i < nonnulllist.size(); ++i)
		{
			ItemStack item = container.getItem(i);
			if (item.equals(ingredients.subject))
				if (getSubjectRemaining(ingredients.subject, ingredients.modifier) != null && !getSubjectRemaining(ingredients.subject, ingredients.modifier).isEmpty())
					nonnulllist.set(i, getSubjectRemaining(ingredients.subject, ingredients.modifier));
			else if (item.hasCraftingRemainingItem())
				nonnulllist.set(i, item.getCraftingRemainingItem());
		}
		return nonnulllist;
	}
	
	@Override
	public boolean canCraftInDimensions(int pWidth, int pHeight) {
		return pWidth * pHeight >= 2;
	}
	
	protected static record SubjectAndModifier(ItemStack subject, ItemStack modifier) {}

}
