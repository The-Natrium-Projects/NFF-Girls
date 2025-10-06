package net.sodiumzh.nff.girls.jei.trade;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import jeresources.util.Font;
import jeresources.util.RenderHelper;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.entity.vanillatrade.CNFFGirlsTradeHandler;
import net.sodiumzh.nff.girls.jei.NFFGirlsJeiStatics;
import net.sodiumzh.nfu.client.NFUGUIStatics;
import net.sodiumzh.nfu.container.Tuple2;
import net.sodiumzh.nfu.container.Tuple3;
import net.sodiumzh.nfu.math.GuiPos;
import net.sodiumzh.nfu.object.Validatable;
import net.sodiumzh.nfu.util.NFUDebugStatics;
import net.sodiumzh.nfu.util.NFUInfoStatics;
import net.sodiumzh.nfu.util.NFUMiscStatics;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.AbstractMap;
import java.util.List;
import java.util.Optional;

/**
 * Representing a page of NFF Trade tab, i.e. trades of a specific mob.
 */
public class NFFGirlsTradeJeiInfo implements IRecipeCategoryExtension {

    public static final ResourceLocation TEXTURE_PATH = new ResourceLocation(NFFGirls.MOD_ID, "textures/gui/trade_jei_screen.png");
    public static final GuiPos TEXTURE_SIZE = new GuiPos(256, 256);

    private final EntityType<?> entityType;
    // Mob instance created from entity type. For rendering in the GUI.
    private final Validatable<Entity> mobInstance = new Validatable<>(null);
    private final Validatable<Multimap<Integer, NFFGirlsTradeJeiRecord>> entries = new Validatable<>(null);
    private final Validatable<Multimap<Integer, Tuple3<ItemStack, ItemStack, ItemStack>>> mergedEntries = new Validatable<>(null);
    @Nullable
    private IFocus<ItemStack> focus = null;

    public NFFGirlsTradeJeiInfo(EntityType<?> entityType) {
        this.entityType = entityType;
    }

    public void tryInitialize() {
        try {
            Minecraft mc = Minecraft.getInstance();
            if (!mobInstance.isValidated()) {
                Optional.ofNullable(mc.level).map(entityType::create).ifPresent(mobInstance::setAndValidate);
            }
            if (!entries.isValidated()) {
                if (NFFGirlsJeiStatics.ALL_TRADE_ENTRIES.get().isValidated()) {
                    entries.setAndValidate(NFFGirlsJeiStatics.ALL_TRADE_ENTRIES.get().get().get(entityType));
                }
                else Optional.ofNullable(mc.player).ifPresent(NFFGirlsJeiStatics::requestJeiDataSync);
            }
        } catch (RuntimeException e) {
            NFUDebugStatics.errorOnce(NFFGirlsTradeJeiInfo.class, "Initialization error for type "
                + entityType.getDescriptionId() + "\n" + e.getMessage());
        }
    }

    @Nonnull
    public Tuple2<Optional<Entity>, Multimap<Integer, NFFGirlsTradeJeiRecord>> getEntityAndEntries() {
        this.tryInitialize();
        return Tuple2.of(mobInstance.getIfValidated(), entries.getIfValidated().orElseGet(HashMultimap::create));
    }

    public void drawInfo(int recipeWidth, int recipeHeight, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        Tuple2<Optional<Entity>, Multimap<Integer, NFFGirlsTradeJeiRecord>> entityAndEntries = this.getEntityAndEntries();
        int[] levelRequirements = getEntityAndEntries().getA()
            .flatMap(INFFGirlsTamed::get)
            .map(INFFGirlsTamed::getXpLevelRequirementsEachMerchantLevel)
            .orElse(CNFFGirlsTradeHandler.LEVEL_REQUIREMENTS);

        List<Integer> merchantLevels = this.getEntriesToDisplay().keySet().stream().sorted().limit(levelRequirements.length).toList();

        // Draw background
        NFUGUIStatics.drawSprite(guiGraphics, TEXTURE_PATH, new GuiPos(0, 0), 0,
            new GuiPos(0, 0), new GuiPos(162, 120), TEXTURE_SIZE);
        // Draw entity
        entityAndEntries.getA().map(e -> NFUMiscStatics.cast(e, LivingEntity.class)).ifPresent(e ->
            RenderHelper.renderEntity(guiGraphics, 33, 116,
                e.getBbHeight() > 2d ? 68d / e.getBbHeight() : 34d,
                38.0 - mouseX, 80.0 - mouseY, e));
        // Draw item frames and arrows
        int y0 = Math.max(0, 5 + getBgHeight(merchantLevels.size()) / 2 - 11 * merchantLevels.size());
        for (int i = 0; i < merchantLevels.size(); ++i) {
            NFUGUIStatics.setActiveTexture(TEXTURE_PATH);
            NFUGUIStatics.drawSprite(guiGraphics, TEXTURE_PATH, new GuiPos(126, y0 + i * 22), 0,
                new GuiPos(0, 120), new GuiPos(20, 20), TEXTURE_SIZE);
            NFUGUIStatics.drawSprite(guiGraphics, TEXTURE_PATH, new GuiPos(90, y0 + i * 22), 0,
                new GuiPos(22, 120), new GuiPos(18, 18), TEXTURE_SIZE);
            NFUGUIStatics.drawSprite(guiGraphics, TEXTURE_PATH, new GuiPos(109, y0 + i * 22), 0,
                new GuiPos(22, 120), new GuiPos(18, 18), TEXTURE_SIZE);
            NFUGUIStatics.drawSprite(guiGraphics, TEXTURE_PATH, new GuiPos(146, y0 + i * 22), 0,
                new GuiPos(22, 120), new GuiPos(18, 18), TEXTURE_SIZE);
        }
        // Draw level tips
        for (int i = 0; i < merchantLevels.size(); ++i) {
            Font.normal.print(guiGraphics, "lv. " + levelRequirements[merchantLevels.get(i) - 1], 66, y0 + 5 + i * 22);
        };
        // Draw mob name
        Font.normal.print(guiGraphics, entityAndEntries.getA().map(Entity::getName)
            .orElseGet(() -> NFUInfoStatics.createText("")).getVisualOrderText(), 5, 5);
    }

    public Optional<IFocus<ItemStack>> getFocus() {
        return Optional.ofNullable(focus);
    }

    public void setFocus(@Nullable IFocus<ItemStack> focus) {
        this.focus = focus;
    }

    public Multimap<Integer, Tuple3<ItemStack, ItemStack, ItemStack>> getEntriesToDisplay() {
        int[] levelRequirements = getEntityAndEntries().getA()
            .flatMap(INFFGirlsTamed::get)
            .map(INFFGirlsTamed::getXpLevelRequirementsEachMerchantLevel)
            .orElse(CNFFGirlsTradeHandler.LEVEL_REQUIREMENTS);
        return getEntityAndEntries().getB().entries().stream()
            // Remove level-out-of-bound entries
            .filter(entry -> entry.getKey() > 0 && entry.getKey() <= levelRequirements.length)
            // Convert to displayable entries and merge
            .flatMap(entryList -> entryList.getValue().toJeiDisplayEntries().stream().map(e -> new AbstractMap.SimpleEntry<>(entryList.getKey(), e)))
            // Filter if focus present
            .filter(elem -> getFocus().map(f -> {
                ItemStack itemStack = f.getTypedValue().getItemStack().orElse(ItemStack.EMPTY);
                if (itemStack.isEmpty()) return false;  // Ignore if missing focus item
                if (f.getRole().equals(RecipeIngredientRole.INPUT))
                    return elem.getValue().a.is(itemStack.getItem()) || elem.getValue().b.is(itemStack.getItem());
                else if (f.getRole().equals(RecipeIngredientRole.OUTPUT))
                    return elem.getValue().c.is(itemStack.getItem());
                else return false;  // Trade entries doesn't involve catalyst
            }).orElse(true))
            .collect(Multimaps.toMultimap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue, HashMultimap::create));
    }

    // The GUI background will adapt if the there are 6 rows or above. Calculate expected height here.
    // Usually this should not take effect. Only for future flexibility.
    private static int getBgHeight(int rows) {
        return 22 * Math.max(5, rows);
    }
}
