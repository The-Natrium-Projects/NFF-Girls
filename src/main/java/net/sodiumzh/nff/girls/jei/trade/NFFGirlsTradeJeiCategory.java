package net.sodiumzh.nff.girls.jei.trade;

import com.google.common.collect.Multimap;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.jei.NFFGirlsJeiPlugin;
import net.sodiumzh.nfu.container.Tuple3;
import net.sodiumzh.nfu.math.GuiPos;
import net.sodiumzh.nfu.util.NFUInfoStatics;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

/**
 * NFF: Girls Trade tab.
 */
public class NFFGirlsTradeJeiCategory implements IRecipeCategory<NFFGirlsTradeJeiMobEntry> {

    private final IDrawable defaultBackground;
    @Nullable
    private IDrawable backgroundCache = null;
    private final IDrawable icon;
    private final Component localizedName;
    public static final GuiPos DEFAULT_BACKGROUND_SIZE = new GuiPos(164, 120);
    private final IGuiHelper guiHelper;
    //private final IDrawable tagIcon;

    public NFFGirlsTradeJeiCategory(IGuiHelper helper) {
        this.defaultBackground = helper.createBlankDrawable(DEFAULT_BACKGROUND_SIZE.x, DEFAULT_BACKGROUND_SIZE.y);
        this.icon = helper.drawableBuilder(new ResourceLocation(NFFGirls.MOD_ID, "textures/item/evil_gem.png"),
            0, 0, 16, 16)
            .setTextureSize(16, 16)
            .build();
        this.localizedName = NFUInfoStatics.createTranslatable("jei.nffgirls.trade_title");
        this.guiHelper = helper;
    }

    @Override
    public RecipeType<NFFGirlsTradeJeiMobEntry> getRecipeType() {
        return NFFGirlsJeiPlugin.TRADES;
    }

    @Override
    public Component getTitle() {
        return this.localizedName;
    }

    @Override
    public IDrawable getBackground() {
        return Optional.ofNullable(backgroundCache).orElse(this.defaultBackground);
    }

    private void updateBackground(int rows) {
        // When default bg is enough, remove cache and use default
        if (22 * rows <= defaultBackground.getHeight())
            this.backgroundCache = null;
        else if (backgroundCache == null || 22 * rows != this.backgroundCache.getHeight())
            this.backgroundCache = guiHelper.createBlankDrawable(DEFAULT_BACKGROUND_SIZE.x, 22 * rows);
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    public void draw(NFFGirlsTradeJeiMobEntry recipe, @Nonnull IRecipeSlotsView recipeSlotsView, @Nonnull PoseStack guiGraphics, double mouseX, double mouseY) {
        recipe.drawInfo(this.getBackground().getWidth(), this.getBackground().getHeight(), guiGraphics, mouseX, mouseY);
    }

    public @Nonnull List<Component> getTooltipStrings(NFFGirlsTradeJeiMobEntry recipe, @Nonnull IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        return recipe.getTooltipStrings(mouseX, mouseY);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, NFFGirlsTradeJeiMobEntry recipe, IFocusGroup focuses) {
        recipe.tryInitialize();
        IFocus<ItemStack> focus = focuses.getFocuses(VanillaTypes.ITEM_STACK).findFirst().orElse(null);
        recipe.setFocus(focus);
        // Collect entries to display
        Multimap<Integer, Tuple3<ItemStack, ItemStack, ItemStack>> entriesToDisplay = recipe.getEntriesToDisplay();
        // Draw
        List<Integer> levels = entriesToDisplay.keySet().stream().sorted().toList();
        this.updateBackground(levels.size());
        GuiPos bgSize = new GuiPos(this.getBackground().getWidth(), this.getBackground().getHeight());
        int y0 = Math.max(1, bgSize.y / 2 - 11 * levels.size() + 1);
        for(int i = 0; i < levels.size(); ++i) {
            builder.addSlot(RecipeIngredientRole.INPUT, 91, y0 + i * 22)
                .addItemStacks(entriesToDisplay.get(levels.get(i)).stream().map(entry -> entry.a).toList());
            builder.addSlot(RecipeIngredientRole.INPUT, 110, y0 + i * 22)
                .addItemStacks(entriesToDisplay.get(levels.get(i)).stream().map(entry -> entry.b).toList());
            builder.addSlot(RecipeIngredientRole.OUTPUT, 147, y0 + i * 22)
                .addItemStacks(entriesToDisplay.get(levels.get(i)).stream().map(entry -> entry.c).toList());
        }
    }
}
