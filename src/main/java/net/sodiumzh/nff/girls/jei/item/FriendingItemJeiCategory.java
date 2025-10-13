package net.sodiumzh.nff.girls.jei.item;

import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.resources.ResourceLocation;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.jei.NFFGirlsJeiPlugin;
import net.sodiumzh.nfu.math.GuiPos;
import net.sodiumzh.nfu.util.NFUInfoStatics;

public class FriendingItemJeiCategory extends MobApplicableItemTableJeiCategory<FriendingItemJeiMobEntry> {

    public FriendingItemJeiCategory(IGuiHelper helper) {
        super(helper,
            helper.drawableBuilder(new ResourceLocation(NFFGirls.MOD_ID, "textures/gui/jei_icon_friending.png"),
                0, 0, 16, 16)
                .setTextureSize(16, 16)
                .build(),
            NFUInfoStatics.createTranslatable("jei.nffgirls.friending_item_title"));
    }

    @Override
    public RecipeType<FriendingItemJeiMobEntry> getRecipeType() {
        return NFFGirlsJeiPlugin.FRIENDING_ITEMS;
    }

    @Override
    public GuiPos getBackgroundSize() {
        return new GuiPos(166, 140);
    }



}
