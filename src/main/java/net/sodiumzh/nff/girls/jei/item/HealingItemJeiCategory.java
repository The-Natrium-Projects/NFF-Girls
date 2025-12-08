package net.sodiumzh.nff.girls.jei.item;

import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.resources.ResourceLocation;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.jei.NFFGirlsJeiPlugin;
import net.sodiumzh.nfu.util.NFUInfoStatics;

public class HealingItemJeiCategory extends MobApplicableItemTableJeiCategory<HealingItemJeiMobEntry> {

    public HealingItemJeiCategory(IGuiHelper helper) {
        super(helper,
            helper.drawableBuilder(new ResourceLocation(NFFGirls.MOD_ID, "textures/gui/jei_icon_healing.png"),
                0, 0, 16, 16)
                .setTextureSize(16, 16)
                .build(),
            NFUInfoStatics.createTranslatable("jei.nffgirls.healing_item_title"));
    }

    @Override
    public RecipeType<HealingItemJeiMobEntry> getRecipeType() {
        return NFFGirlsJeiPlugin.HEALING_ITEMS;
    }

}
