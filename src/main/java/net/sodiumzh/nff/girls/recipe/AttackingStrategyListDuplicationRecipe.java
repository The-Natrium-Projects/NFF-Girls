package net.sodiumzh.nff.girls.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.sodiumzh.nff.girls.item.AttackingStrategyListItem;
import net.sodiumzh.nff.girls.registry.NFFGirlsItems;
import net.sodiumzh.nff.girls.registry.NFFGirlsRecipes;

public class AttackingStrategyListDuplicationRecipe extends SimpleModificationRecipe{
    public AttackingStrategyListDuplicationRecipe(ResourceLocation id, CraftingBookCategory category) {
        super(id, category);
    }

    public AttackingStrategyListDuplicationRecipe(ResourceLocation id) {
        super(id);
    }

    @Override
    public boolean isSubjectItem(ItemStack stack) {
        return stack.is(NFFGirlsItems.ATTACKING_STRATEGY_LIST.get()) && !AttackingStrategyListItem.getStrategy(stack).isEmpty();
    }

    @Override
    public boolean isModifierItem(ItemStack stack) {
        return stack.is(NFFGirlsItems.ATTACKING_STRATEGY_LIST.get()) && AttackingStrategyListItem.getStrategy(stack).isEmpty();
    }

    @Override
    public ItemStack getResult(ItemStack subject, ItemStack modifier) {
        ItemStack res = new ItemStack(NFFGirlsItems.ATTACKING_STRATEGY_LIST.get(), 1);
        AttackingStrategyListItem.setStrategy(res, AttackingStrategyListItem.getStrategy(subject));
        return res;
    }

    @Override
    public ItemStack getSubjectRemaining(ItemStack subject, ItemStack modifier) {
        return subject.copy();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return NFFGirlsRecipes.ATTACKING_STRATEGY_LIST_DUPLICATE.get();
    }
}
