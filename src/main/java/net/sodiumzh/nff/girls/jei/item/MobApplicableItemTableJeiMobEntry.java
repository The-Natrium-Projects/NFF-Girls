package net.sodiumzh.nff.girls.jei.item;

import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.ingredient.IRecipeSlotTooltipCallback;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nfu.client.NFUGUIStatics;
import net.sodiumzh.nfu.info.ComponentBuilder;
import net.sodiumzh.nfu.math.GuiPos;
import net.sodiumzh.nfu.object.Validatable;
import net.sodiumzh.nfu.util.NFUInfoStatics;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.DoubleStream;

/**
 * Represents a healing item jei page.
 */
public abstract class MobApplicableItemTableJeiMobEntry implements IRecipeCategoryExtension, IRecipeSlotTooltipCallback {

    protected static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(NFFGirls.MOD_ID, "textures/gui/mob_applicable_item_table_screen.png");
    protected static final GuiPos TEXTURE_SIZE = new GuiPos(256, 256);

    protected final EntityType<? extends Mob> entityType;
    protected final Validatable<Mob> mobInstance = new Validatable<>(null);
    protected final Validatable<MobApplicableItemTableJeiRecord> entries = new Validatable<>(null);
    protected final Validatable<List<List<ItemStack>>> mergedEntries = new Validatable<>(null);

    public MobApplicableItemTableJeiMobEntry(EntityType<? extends Mob> type) {
        this.entityType = type;
    }

    public EntityType<? extends Mob> getEntityType() {
        return entityType;
    }

    /**
     * The spawn egg item of this mob, or empty if the mob has no spawn egg.
     * Used as an input slot on the guide page so that focusing a mob's spawn
     * egg in JEI shows this mob's taming (or healing) guide.
     */
    public ItemStack getSpawnEggItem() {
        ResourceLocation key = ForgeRegistries.ENTITIES.getKey(entityType);
        if (key == null) return ItemStack.EMPTY;
        Item egg = ForgeRegistries.ITEMS.getValue(
            new ResourceLocation(key.getNamespace(), key.getPath() + "_spawn_egg"));
        return egg != null ? egg.getDefaultInstance() : ItemStack.EMPTY;
    }

    public abstract void tryInitialize();

    /*
    public void tryInitialize() {
        try {
            Minecraft mc = Minecraft.getInstance();
            if (!mobInstance.isValidated()) {
                Optional.ofNullable(mc.level).map(entityType::create).ifPresent(mobInstance::setAndValidate);
            }
            if (!entries.isValidated()) {
                if (NFFGirlsJeiStatics.ALL_HEALING_ITEM_TABLES.get().isValidated()) {
                    entries.setAndValidate(NFFGirlsJeiStatics.ALL_HEALING_ITEM_TABLES.get().get().get(entityType));
                    mergeEntries();
                }
                else Optional.ofNullable(mc.player).ifPresent(NFFGirlsJeiStatics::requestJeiDataSync);
            }
        } catch (RuntimeException e) {
            NFUDebugStatics.errorOnce(MobApplicableItemTableJeiMobEntry.class, "Initialization error for type "
                + entityType.getDescriptionId() + "\n" + e.getMessage());
        }
    }*/

    protected void mergeEntries() {
        if (entries.isValidated() && !mergedEntries.isValidated()) {
            var list = entries.get().getEntries().stream()
                .sorted(Comparator.comparingDouble(record -> Optional.ofNullable(record.getAmountProviderFunctionKey())
                    .map(v -> Double.MAX_VALUE).orElseGet(() -> record.getAmountDescriptor()[0])))
                .map(record -> {
                try {
                    // Parse meta
                    MutableComponent amountDesc;
                    if (record.getAmountProviderFunctionKey() != null) {
                        ResourceLocation key = record.getAmountProviderFunctionKey();
                        amountDesc = NFUInfoStatics.createTranslatable("jei." + key.getNamespace()
                            + ".amount_getter." + key.getPath());
                    } else {
                        amountDesc = readAmount(record.getAmountDescriptor(), this.showAmountAsPercentage());
                        if (amountDesc == null) return List.<ItemStack>of();
                    }
                    int cooldown = record.getCooldown();
                    boolean noConsume = record.isNoConsume();
                    List<ItemStack> res = record.getApplicableItems().stream().map(item -> {
                        ItemStack itemStack = item.getDefaultInstance();
                        if (itemStack == null || itemStack.isEmpty())
                            return ItemStack.EMPTY;
                        itemStack.getOrCreateTag().putString("amount", Component.Serializer.toJson(amountDesc));
                        itemStack.getTag().putInt("cooldown", cooldown);
                        itemStack.getTag().putBoolean("noConsume", noConsume);
                        return itemStack;
                    }).filter(itemStack -> !itemStack.isEmpty()).toList();
                    return res;
                } catch (Exception e) {
                    return List.<ItemStack>of();
                }
            }).filter(l -> !l.isEmpty()).toList();
            this.mergedEntries.setAndValidate(list);
        }
    }

    @Override
    public void drawInfo(int recipeWidth, int recipeHeight, @NotNull PoseStack guiGraphics, double mouseX, double mouseY) {
        this.tryInitialize();
        NFUGUIStatics.setActiveTexture(TEXTURE_LOCATION);
        NFUGUIStatics.drawSprite(guiGraphics, GuiPos.ZERO, 0, new GuiPos(0, 20),
            new GuiPos(164, 120), TEXTURE_SIZE);
        this.drawAdditional(recipeWidth, recipeHeight, guiGraphics, mouseX, mouseY);
        this.mobInstance.getIfValidated().ifPresent(mob ->
            Optional.ofNullable(Minecraft.getInstance().screen).ifPresent(screen -> {
                int baseX = (screen.width - recipeWidth) / 2;
                int baseY = (screen.height - recipeHeight) / 2;
                InventoryScreen.renderEntityInInventory(baseX + 33, baseY + 116,
                    (int) (mob.getBbHeight() > 2 ? 68d / mob.getBbHeight() : 34d),
                    (float) (38.0 - mouseX), (float) (80.0 - mouseY), mob);
            })
        );
        Component mobName = this.entityType.getDescription();
        Minecraft.getInstance().font.draw(guiGraphics, mobName, 7, 2, 8);
    }

    @Override
    public void onTooltip(@NotNull IRecipeSlotView recipeSlotView, @NotNull List<Component> tooltip) {
        ItemStack itemStack = recipeSlotView.getIngredients(VanillaTypes.ITEM_STACK)
            .filter(i -> i.hasTag() && i.getTag().contains("amount", Tag.TAG_STRING)
                && i.getTag().contains("cooldown", Tag.TAG_INT) && i.getTag().contains("noConsume", Tag.TAG_BYTE))
            .findFirst().orElse(ItemStack.EMPTY);
        if (itemStack.isEmpty()) return;
        try {
            Component amountDesc = Component.Serializer.fromJson(itemStack.getTag().getString("amount"));
            tooltip.add(amountDesc);
            if (this.showCooldown()) {
                int cooldown = itemStack.getTag().getInt("cooldown");
                BigDecimal cooldownBD = BigDecimal.valueOf((double) cooldown / 20d);
                cooldownBD = cooldownBD.stripTrailingZeros();
                tooltip.add(NFUInfoStatics.createTranslatable("jei.nffgirls.cooldown", cooldownBD.toPlainString()));
            }
            if (itemStack.getTag().getBoolean("noConsume"))
                tooltip.add(NFUInfoStatics.createTranslatable("jei.nffgirls.no_consume"));
        } catch (Exception ignore) {
        }
    }

    public abstract void drawAdditional(int recipeWidth, int recipeHeight, @NotNull PoseStack guiGraphics, double mouseX, double mouseY);

    public abstract boolean showCooldown();

    public Optional<List<List<ItemStack>>> getMergedEntries() {
        this.tryInitialize();
        return mergedEntries.getIfValidated();
    }

    protected abstract boolean showAmountAsPercentage();

    protected static MutableComponent readAmount(double[] inArray, boolean asPercentage) {
        List<String> amountNumberStrings = DoubleStream.of(inArray).mapToObj(val -> {
            BigDecimal bd = BigDecimal.valueOf(val);
            if (val > 0.1d)
                bd = bd.setScale(3, RoundingMode.HALF_UP);
            else bd = bd.setScale(4, RoundingMode.HALF_UP);
            bd = bd.stripTrailingZeros();
            if (asPercentage) {
                bd = bd.multiply(BigDecimal.valueOf(100d)).stripTrailingZeros();
                return bd.toPlainString() + "%";
            }
            else return bd.toPlainString();
        }).toList();
        if (amountNumberStrings.size() == 1) {
            return NFUInfoStatics.createText(amountNumberStrings.get(0));
        } else if (amountNumberStrings.size() == 2) {
            return NFUInfoStatics.createText(amountNumberStrings.get(0) + " - " + amountNumberStrings.get(1));
        } else if (amountNumberStrings.size() == 3) {
            return ComponentBuilder.create().appendText(amountNumberStrings.get(0) + " - " + amountNumberStrings.get(1))
                .appendTranslatable("jei.nffgirls.amount_expectation", amountNumberStrings.get(2)).build();
        } else return null;
    }
}
