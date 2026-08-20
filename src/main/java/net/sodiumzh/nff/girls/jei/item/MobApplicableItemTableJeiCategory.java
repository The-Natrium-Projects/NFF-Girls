package net.sodiumzh.nff.girls.jei.item;

import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.sodiumzh.nfu.math.GuiPos;
import net.sodiumzh.nfu.util.NFUInfoStatics;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class MobApplicableItemTableJeiCategory<T extends MobApplicableItemTableJeiMobEntry> implements IRecipeCategory<T> {

    protected final IDrawable background;
    protected final IDrawable icon;
    protected final Component localizedName;
    public static final GuiPos DEFAULT_BACKGROUND_SIZE = new GuiPos(164, 120);
    /** Position of the spawn egg input slot on the guide page. */
    protected static final GuiPos EGG_SLOT_POS = new GuiPos(6, 20);
    protected final IGuiHelper guiHelper;

    public MobApplicableItemTableJeiCategory(IGuiHelper helper, IDrawable icon, Component localizedName) {
        this.background = helper.createBlankDrawable(getBackgroundSize().x, getBackgroundSize().y);
        this.icon = icon;
        this.localizedName = localizedName;
        this.guiHelper = helper;
    }

    @Override
    public abstract RecipeType<T> getRecipeType();

    @Override
    public Component getTitle() {
        return this.localizedName;
    }

    public GuiPos getBackgroundSize() {
        return DEFAULT_BACKGROUND_SIZE;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void draw(T recipe, @Nonnull IRecipeSlotsView recipeSlotsView, @Nonnull PoseStack guiGraphics, double mouseX, double mouseY) {
        recipe.drawInfo(this.getBackground().getWidth(), this.getBackground().getHeight(), guiGraphics, mouseX, mouseY);
    }

    @Override
    public @Nonnull List<Component> getTooltipStrings(T recipe, @Nonnull IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        return recipe.getTooltipStrings(mouseX, mouseY);
    }


    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, T recipe, IFocusGroup focuses) {
        List<List<ItemStack>> itemList = recipe.getMergedEntries().orElse(List.of());
        if (itemList.isEmpty()) return;
        ItemStack focus = focuses.getItemStackFocuses().findFirst()
            .filter(f -> f.getRole().equals(RecipeIngredientRole.INPUT))
            .flatMap(f -> f.getTypedValue().getItemStack())
            .orElse(null);
        // Spawn egg input slot: focusing a mob's spawn egg in JEI shows this
        // mob's guide page (taming or healing), which is otherwise only found
        // by focusing the items themselves.
        ItemStack egg = recipe.getSpawnEggItem();
        if (!egg.isEmpty()) {
            builder.addSlot(RecipeIngredientRole.INPUT, EGG_SLOT_POS.x, EGG_SLOT_POS.y)
                .addItemStack(egg)
                .addTooltipCallback((view, tooltip) ->
                    tooltip.add(NFUInfoStatics.createTranslatable("jei.nffgirls.guide_egg_tip")));
        }
        if (focus != null &&
            (egg.isEmpty() || !focus.is(egg.getItem())) &&
            itemList.stream().flatMap(List::stream).noneMatch(item -> item.is(focus.getItem())))
            return;
        final GuiPos xy0 = new GuiPos(92, 18);
        for (int i = 0; i < itemList.size(); ++i) {
            GuiPos pos = xy0.add(new GuiPos(18 * (i % 4), 18 * (i / 4)));
            builder.addSlot(RecipeIngredientRole.INPUT, pos.x, pos.y)
                .addItemStacks(itemList.get(i)).addTooltipCallback(recipe);
        }
    }
}
