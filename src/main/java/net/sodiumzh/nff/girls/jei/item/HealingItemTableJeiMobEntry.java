package net.sodiumzh.nff.girls.jei.item;

import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.sodiumzh.nff.girls.jei.NFFGirlsJeiStatics;
import net.sodiumzh.nff.girls.jei.trade.NFFGirlsTradeJeiMobEntry;
import net.sodiumzh.nfu.container.Tuple4;
import net.sodiumzh.nfu.info.ComponentBuilder;
import net.sodiumzh.nfu.object.Validatable;
import net.sodiumzh.nfu.util.NFUDebugStatics;
import net.sodiumzh.nfu.util.NFUInfoStatics;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

public class HealingItemTableJeiMobEntry implements IRecipeCategoryExtension {

    private final EntityType<? extends Mob> entityType;
    private final Validatable<Mob> mobInstance = new Validatable<>(null);
    private final Validatable<MobApplicableItemTableJeiRecord> entries = new Validatable<>(null);
    private final Validatable<List<List<ItemStack>>> mergedEntries = new Validatable<>(null);

    public HealingItemTableJeiMobEntry(EntityType<? extends Mob> type) {
        this.entityType = type;
    }

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
            NFUDebugStatics.errorOnce(HealingItemTableJeiMobEntry.class, "Initialization error for type "
                + entityType.getDescriptionId() + "\n" + e.getMessage());
        }
    }

    private void mergeEntries() {
        if (entries.isValidated() && !mergedEntries.isValidated()) {
            var list = entries.get().getEntries().stream().map(record -> {
                try {
                    // Parse meta
                    MutableComponent amountDesc;
                    if (record.getAmountProviderFunctionKey() != null) {
                        ResourceLocation key = record.getAmountProviderFunctionKey();
                        amountDesc = NFUInfoStatics.createTranslatable("jei." + key.getNamespace()
                            + ".amount_getter." + key.getPath());
                    } else {
                        List<String> amountNumberStrings = DoubleStream.of(record.getAmountDescriptor()).mapToObj(val -> {
                            BigDecimal bd = BigDecimal.valueOf(val);
                            bd = bd.stripTrailingZeros();
                            return bd.toPlainString();
                        }).toList();
                        if (amountNumberStrings.size() == 1) {
                            amountDesc = NFUInfoStatics.createText(amountNumberStrings.get(0));
                        } else if (amountNumberStrings.size() == 2) {
                            amountDesc = NFUInfoStatics.createText(amountNumberStrings.get(0) + " - " + amountNumberStrings.get(1));
                        } else if (amountNumberStrings.size() == 3) {
                            amountDesc = ComponentBuilder.create().appendText(amountNumberStrings.get(0) + " - " + amountNumberStrings.get(1))
                                .appendTranslatable("jei.nffgirls.amount_expectation", amountNumberStrings.get(2)).build();
                        } else return List.<ItemStack>of();
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

}
