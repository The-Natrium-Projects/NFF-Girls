package net.sodiumzh.nff.girls.item.bauble;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.registry.NFFGirlsBaubles;
import net.sodiumzh.nff.girls.registry.NFFGirlsConfigs;
import net.sodiumzh.nff.girls.registry.NFFGirlsEntityAttributes;
import net.sodiumzh.nfu.function.ModifiableSupplier;
import net.sodiumzh.nfu.function.RegistrablePredicate;
import net.sodiumzh.nfu.info.ComponentBuilder;
import net.sodiumzh.nfu.item.bauble.BaubleAttributeModifier;
import net.sodiumzh.nfu.item.bauble.BaubleEquippingCondition;
import net.sodiumzh.nfu.item.bauble.BaubleEquippingConditions;
import net.sodiumzh.nfu.item.bauble.BaubleProcessingArgs;
import net.sodiumzh.nfu.object.LimitedMutable;
import net.sodiumzh.nfu.object.Validatable;
import net.sodiumzh.nfu.registry.NFURegistries;
import net.sodiumzh.nfu.registry.NFURegistry;
import net.sodiumzh.nfu.registry.NFURegistryEntryCollection;
import net.sodiumzh.nfu.util.NFUDebugStatics;
import net.sodiumzh.nfu.util.NFUInfoStatics;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.text.DecimalFormat;
import java.util.*;
import java.util.function.*;

/**
 * A utility for declaration of baubles on construction.
 */
public class NFFGirlsBaubleBuilder {

    public static NFURegistryEntryCollection<RegistrablePredicate<?>> EQUIPPING_CONDITION_PRESETS
        = NFURegistryEntryCollection.create(NFURegistries.PREDICATES, NFFGirls.MOD_ID);

    public static final NFURegistry.Accessor<RegistrablePredicate<Mob>> NO_CONDITION =
        EQUIPPING_CONDITION_PRESETS.register("bauble_no_condition", () ->
            new RegistrablePredicate<>(Mob.class, "nffgirls_bauble_no_condition", mob -> true));

    private final Map<Supplier<BaubleAttributeModifier>, Predicate<? super Mob>> repeatableModifierSuppliers
        = new HashMap<>();
    private final Map<Supplier<BaubleAttributeModifier>, Predicate<? super Mob>> unrepeatableModifierSuppliers
        = new HashMap<>();
    // Only for tooltips. Use suppliers for making effects.
    private final Validatable<Map<BaubleAttributeModifier, Predicate<? super Mob>>> repeatableModifiers
        = new Validatable<>(new HashMap<>());
    // Only for tooltips. Use suppliers for making effects.
    private final Validatable<Map<BaubleAttributeModifier, Predicate<? super Mob>>> unrepeatableModifiers
        = new Validatable<>(new HashMap<>());
    private final List<String> tags = new ArrayList<>();
    private BiFunction<ItemStack, MutableComponent, MutableComponent> nameStyle = (i, c) -> c;
    // Built bauble reference after building. Null before building.
    @Nullable
    private IBuiltBauble builtBauble = null;

    private final LimitedMutable<Boolean> validated = new LimitedMutable<>(false, 1);

    private boolean environmentImmune = false;
    private Consumer<BaubleProcessingArgs> onTick = args -> {};
    private BaubleEquippingCondition equippingCondition = BaubleEquippingConditions.CONDITION_ALWAYS.get();

    private static BaubleAttributeModifier getModifierFromProperties(Attribute attribute, double amount, AttributeModifier.Operation operation, @Nullable Predicate<? super Mob> condition, @Nullable ResourceLocation id) {
        double actualAmount = amount;
        if (operation.equals(AttributeModifier.Operation.ADDITION)) {
            if (attribute.equals(Attributes.MAX_HEALTH))
                actualAmount *= NFFGirlsConfigs.ValueCache.Baubles.BAUBLE_HEALTH_RECOVERY_SCALE;
            else if (attribute.equals(Attributes.ATTACK_DAMAGE))
                actualAmount *= NFFGirlsConfigs.ValueCache.Baubles.BAUBLE_ATK_BOOSTING_SCALE;
            else if (attribute.equals(Attributes.ARMOR))
                actualAmount *= NFFGirlsConfigs.ValueCache.Baubles.BAUBLE_ARMOR_BOOSTING_SCALE;
            else if (attribute.equals(NFFGirlsEntityAttributes.PERSISTENT_HEALING.get()))
                actualAmount *= NFFGirlsConfigs.ValueCache.Baubles.BAUBLE_HEALTH_RECOVERY_SCALE;
        }
        BaubleAttributeModifier res = new BaubleAttributeModifier(attribute, actualAmount, operation);
        Predicate<? super Mob> cond1 = condition == null ? NO_CONDITION.get() : condition;
        res.setAdditionalCondition(args -> cond1.test(args.user()));
        if (id != null)
            res.setAdditionalID(id);
        return res;
    }

    public NFFGirlsBaubleBuilder repeatable(Attribute attr, double amount, AttributeModifier.Operation operation, @Nullable Predicate<? super Mob> condition, @Nullable ResourceLocation id) {
        if (this.validated.get()) {
            NFUDebugStatics.errorOnce("Builder has been validated, no more modification. Skipped.");
            return this;
        }
        Predicate<? super Mob> cond1 = condition != null ? condition : NO_CONDITION.get();
        repeatableModifierSuppliers.put(() -> getModifierFromProperties(attr, amount, operation, cond1, id), cond1);
        return this;
    }

    public NFFGirlsBaubleBuilder repeatable(Attribute attr, double amount, AttributeModifier.Operation operation, @Nullable Predicate<? super Mob> condition) {
        return repeatable(attr, amount, operation, condition, null);
    }

    public NFFGirlsBaubleBuilder repeatable(Attribute attr, double amount, AttributeModifier.Operation operation, @Nullable ResourceLocation id) {
        return repeatable(attr, amount, operation, null, id);
    }

    public NFFGirlsBaubleBuilder repeatable(Attribute attr, double amount, AttributeModifier.Operation operation) {
        return repeatable(attr, amount, operation, null, null);
    }

    public NFFGirlsBaubleBuilder unrepeatable(Attribute attr, double amount, AttributeModifier.Operation operation, Predicate<? super Mob> condition, @Nullable ResourceLocation id) {
        if (this.validated.get()) {
            NFUDebugStatics.errorOnce("Builder has been validated, no more modification. Skipped.");
            return this;
        }
        Predicate<? super Mob> cond1 = condition != null ? condition : NO_CONDITION.get();
        unrepeatableModifierSuppliers.put(() -> getModifierFromProperties(attr, amount, operation, cond1, id), cond1);
        return this;
    }

    public NFFGirlsBaubleBuilder unrepeatable(Attribute attr, double amount, AttributeModifier.Operation operation, @Nullable Predicate<? super Mob> condition) {
        return unrepeatable(attr, amount, operation, condition, null);
    }

    public NFFGirlsBaubleBuilder unrepeatable(Attribute attr, double amount, AttributeModifier.Operation operation, @Nullable ResourceLocation id) {
        return unrepeatable(attr, amount, operation, null, id);
    }

    public NFFGirlsBaubleBuilder unrepeatable(Attribute attr, double amount, AttributeModifier.Operation operation) {
        return unrepeatable(attr, amount, operation, null, null);
    }

    public NFFGirlsBaubleBuilder environmentResistance() {
        this.environmentImmune = true;
        return this;
    }

    public NFFGirlsBaubleBuilder equippingCondition(@Nullable BaubleEquippingCondition condition, @Nullable Component info) {
        this.equippingCondition = Optional.ofNullable(condition).orElse(BaubleEquippingConditions.CONDITION_ALWAYS.get());
        this.equippingConditionTooltip = info;
        return this;
    }

    public NFFGirlsBaubleBuilder equippingCondition(@Nullable BaubleEquippingCondition condition) {
        return equippingCondition(condition, Optional.ofNullable(condition).map(BaubleEquippingCondition::getTranslation).orElse(null));
    }

    public NFFGirlsBaubleBuilder onTick(Consumer<BaubleProcessingArgs> action) {
        Consumer<BaubleProcessingArgs> oldAction = this.onTick;
        this.onTick = oldAction.andThen(action);
        return this;
    }

    public NFFGirlsBaubleBuilder addTag(String tag) {
        this.tags.add(tag);
        return this;
    }

    /**
     * Check if the builder is validated. As we will read configs which are loaded after items, the builder will
     * be validated on server start on both sides. Note that the item will not work correctly
     * if not validated. After validating, the builder will be no longer allowed to add attribute modifiers.
     */
    public boolean isValidated() {
        return validated.get();
    }

    /**
     * Validate the builder. Note that the item will not work correctly if not validated. After validating,
     * the builder will be no longer allowed to add attribute modifiers.
     */
    public void validate() {
        if (this.isValidated()) return;
        this.unrepeatableModifiers.modifyAndValidate(map -> {
            map.clear();
            this.unrepeatableModifierSuppliers.forEach((key, value) -> map.put(key.get(), value));
        });
        this.repeatableModifiers.modifyAndValidate(map -> {
            map.clear();
            this.repeatableModifierSuppliers.forEach((key, value) -> map.put(key.get(), value));
        });
        this.tooltips.modifyAndValidate(list -> {
            list.clear();
            list.addAll(this.tooltipsSupplier.get().stream().map(TooltipInfo::getComponentSupplier).toList());
        });
        if (this.builtBauble == null)
            throw new IllegalStateException("NFFGirlsBaubleBuilder: attempting to validate before building.");
        this.builtBauble.onValidate();
        this.validated.trySet(true);
    }

    // === Tooltip related === //

    private final ModifiableSupplier<List<TooltipInfo>> tooltipsSupplier = new ModifiableSupplier<>(ArrayList::new);
    private final Validatable<List<Supplier<? extends Component>>> tooltips = new Validatable<>(new ArrayList<>());
    private static final Comparator<BaubleAttributeModifier> COMPARING_ATTRIBUTE = (m1, m2) -> {
        if (m1.getAmount() * m2.getAmount() < 0)
            return m1.getAmount() < 0 ? 1 : -1;
        List<Attribute> reserved = List.of(Attributes.MAX_HEALTH, Attributes.ATTACK_DAMAGE,
            Attributes.ARMOR, NFFGirlsEntityAttributes.CRITICAL_RATE.get(),
            NFFGirlsEntityAttributes.ANTI_UNDEAD.get(), NFFGirlsEntityAttributes.ANTI_ARTHROPOD.get(),
            NFFGirlsEntityAttributes.WATER_ASPECT.get(), Attributes.MOVEMENT_SPEED,
            NFFGirlsEntityAttributes.PERSISTENT_HEALING.get(),
            NFFGirlsEntityAttributes.LOOTING_LEVEL.get());
        Attribute o1 = m1.getAttribute();
        Attribute o2 = m2.getAttribute();
        if (reserved.contains(o1) && reserved.contains(o2))
            return Integer.compare(reserved.indexOf(o1), reserved.indexOf(o2));
        else if (reserved.contains(o1)) return -1;
        else if (reserved.contains(o2)) return 1;
        else {
            ResourceLocation key1 = ForgeRegistries.ATTRIBUTES.getKey(o1);
            ResourceLocation key2 = ForgeRegistries.ATTRIBUTES.getKey(o2);
            assert key1 != null;
            assert key2 != null;
            if (key1.getNamespace().equals("minecraft") && !key2.getNamespace().equals("minecraft"))
                return -1;
            else if (key2.getNamespace().equals("minecraft") && !key1.getNamespace().equals("minecraft"))
                return 1;
            else return String.CASE_INSENSITIVE_ORDER.compare(key1.getPath(), key2.getPath());
        }
    };
    private static final Supplier<MutableComponent> DEFAULT_UNREPEATABLE_TIP = () ->
        NFUInfoStatics.createTranslatable("tooltip.nffgirls.bauble.unrepeatable").withStyle(ChatFormatting.GRAY);
    private static final Supplier<MutableComponent> ENVIRONMENT_IMMUNITY_TOOLTIP = () ->
        NFUInfoStatics.createTranslatable("tooltip.nffgirls.bauble.environment_immunity").withStyle(ChatFormatting.WHITE);

    private static final List<UnaryOperator<MutableComponent>> RARITY_FORMATS = List.of(
        m -> m.withStyle(ChatFormatting.WHITE),
        m -> m.withStyle(ChatFormatting.YELLOW),
        m -> m.withStyle(ChatFormatting.GREEN),
        m -> m.withStyle(ChatFormatting.LIGHT_PURPLE),
        m -> m.withStyle(ChatFormatting.BLUE),
        m -> m.withStyle(ChatFormatting.GOLD),
        m -> m.withStyle(ChatFormatting.RED),
        m -> m.withStyle(ChatFormatting.AQUA),
        m -> m.withStyle(ChatFormatting.DARK_PURPLE));

    private boolean showRarityTier = true;
    private boolean useRarityTierNameColor = true;

    // Rarity tier to display. -1 = no display.
    private int rarityTier = -1;

    private boolean isEnvironmentImmunityTooltipManuallyAdded = false;
    @Nullable
    private Component equippingConditionTooltip = null;

    public NFFGirlsBaubleBuilder setRarityTier(int value) {
        if (value < 0) rarityTier = -1;
        else rarityTier = value;
        return this;
    }

    public int getRarityTier() {
        return rarityTier;
    }

    public NFFGirlsBaubleBuilder setShowRarityTier(boolean value) {
        this.showRarityTier = value;
        return this;
    }

    /**
     * Set if this item should set the name color from rarity tier. Note: this only takes effect on dedicated items.
     */
    public NFFGirlsBaubleBuilder setUseRarityTierNameColor(boolean value) {
        this.useRarityTierNameColor = value;
        return this;
    }

    private Optional<UnaryOperator<MutableComponent>> getRarityTierFormat() {
        return rarityTier < 0 ? Optional.empty() :
            Optional.ofNullable(RARITY_FORMATS.get(Math.min(RARITY_FORMATS.size() - 1, rarityTier)));
    }

    private Optional<MutableComponent> getRarityTierDesc() {
        return getRarityTierFormat().map(format ->
            format.apply(NFUInfoStatics.createTranslatable("tooltip.nffgirls.bauble.rarity_tier", rarityTier)));
    }

    public NFFGirlsBaubleBuilder setNameStyle(BiFunction<ItemStack, MutableComponent, MutableComponent> style) {
        this.nameStyle = style;
        return this;
    }

    public NFFGirlsBaubleBuilder setNameStyle(BiConsumer<ItemStack, MutableComponent> style) {
        return setNameStyle((i, c) -> {
            style.accept(i, c);
            return c;
        });
    }

    public NFFGirlsBaubleBuilder setNameStyle(Consumer<MutableComponent> style) {
        return setNameStyle((i, c) -> {
            style.accept(c);
            return c;
        });
    }

    public NFFGirlsBaubleBuilder addRepeatableModifierTooltips(@Nullable Supplier<Predicate<? super Mob>> conditionFilter, Consumer<ModifierTooltipInfo> format) {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setDecimalSeparatorAlwaysShown(false);
        tooltipsSupplier.modify(list -> {
            list.addAll(this.repeatableModifiers.get().entrySet().stream()
                .filter(e -> conditionFilter == null || conditionFilter.get() == null || conditionFilter.get().equals(e.getValue()))
                .sorted(Map.Entry.comparingByKey(COMPARING_ATTRIBUTE))
                .map(e -> TooltipInfo.ofAttribute(e::getKey).modifierFormatter(format))
                .toList());
        });
        return this;
    }

    public NFFGirlsBaubleBuilder addRepeatableModifierTooltips(@Nullable Supplier<Predicate<? super Mob>> conditionFilter) {
        return addRepeatableModifierTooltips(conditionFilter, m -> {});
    }

    public NFFGirlsBaubleBuilder addRepeatableModifierTooltips() {
        return addRepeatableModifierTooltips(null);
    }

    public NFFGirlsBaubleBuilder addRepeatableModifierTooltipsUnconditional() {
        return addRepeatableModifierTooltips(NO_CONDITION::get);
    }

    public NFFGirlsBaubleBuilder addUnrepeatableModifierTooltips(@Nullable Supplier<Predicate<? super Mob>> conditionFilter, Consumer<ModifierTooltipInfo> format, boolean showUnrepeatableTooltip) {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setDecimalSeparatorAlwaysShown(false);
        tooltipsSupplier.modify(list -> {
            list.addAll(this.unrepeatableModifiers.get().entrySet().stream()
                .filter(e -> conditionFilter == null || conditionFilter.get() == null || conditionFilter.get().equals(e.getValue()))
                .sorted(Map.Entry.comparingByKey(COMPARING_ATTRIBUTE))
                .map(e -> {
                    TooltipInfo tooltipInfo = TooltipInfo.ofAttribute(e::getKey).modifierFormatter(format);
                    if (showUnrepeatableTooltip) tooltipInfo.append(DEFAULT_UNREPEATABLE_TIP.get());
                    return tooltipInfo;
                }).toList());
        });
        return this;
    }

    public NFFGirlsBaubleBuilder addUnrepeatableModifierTooltips(@Nullable Supplier<Predicate<? super Mob>> conditionFilter, Consumer<ModifierTooltipInfo> format) {
        return addUnrepeatableModifierTooltips(conditionFilter, format, true);
    }

    public NFFGirlsBaubleBuilder addUnrepeatableModifierTooltips(@Nullable Supplier<Predicate<? super Mob>> conditionFilter, boolean showUnrepeatableTooltip) {
        return addUnrepeatableModifierTooltips(conditionFilter, c -> {}, showUnrepeatableTooltip);
    }

    public NFFGirlsBaubleBuilder addUnrepeatableModifierTooltips(@Nullable Supplier<Predicate<? super Mob>> conditionFilter) {
        return addUnrepeatableModifierTooltips(conditionFilter, c -> {});
    }

    public NFFGirlsBaubleBuilder addUnrepeatableModifierTooltips() {
        return addUnrepeatableModifierTooltips(null);
    }

    public NFFGirlsBaubleBuilder addUnrepeatableModifierTooltipsUnconditional() {
        return addUnrepeatableModifierTooltips(NO_CONDITION::get);
    }

    public NFFGirlsBaubleBuilder addAllModifierTooltips(@Nullable Supplier<Predicate<? super Mob>> conditionFilter,
                                                        Consumer<ModifierTooltipInfo> format, boolean showUnrepeatableTip) {
        this.addRepeatableModifierTooltips(conditionFilter, format);
        this.addUnrepeatableModifierTooltips(conditionFilter, format, showUnrepeatableTip);
        return this;
    }

    public NFFGirlsBaubleBuilder addAllModifierTooltips(@Nullable Supplier<Predicate<? super Mob>> conditionFilter, Consumer<ModifierTooltipInfo> format) {
        return addAllModifierTooltips(conditionFilter, format, true);
    }

    public NFFGirlsBaubleBuilder addAllModifierTooltips(@Nullable Supplier<Predicate<? super Mob>> conditionFilter) {
        return addAllModifierTooltips(conditionFilter, c -> {});
    }

    public NFFGirlsBaubleBuilder addAllModifierTooltips() {
        return addAllModifierTooltips(null);
    }

    public NFFGirlsBaubleBuilder addAllModifierTooltipsUnconditional() {
        return addAllModifierTooltips(NO_CONDITION::get);
    }

    public NFFGirlsBaubleBuilder addEquippingConditionTooltip() {
        if (this.equippingConditionTooltip != null)
            return addTooltip(() -> TooltipInfo.ofComponent(equippingConditionTooltip));
        return this;
    }

    public NFFGirlsBaubleBuilder addTooltip(Supplier<TooltipInfo> tooltip) {
        this.tooltipsSupplier.modify(list -> {
            list.add(tooltip.get());
        });
        return this;
    }

    public NFFGirlsBaubleBuilder addTooltip(TooltipInfo tooltip) {
        return this.addTooltip(() -> tooltip);
    }

    public NFFGirlsBaubleBuilder addTooltip(Component cpn) {
        return this.addTooltip(() -> TooltipInfo.ofComponent(cpn));
    }

    public NFFGirlsBaubleBuilder addTooltipText(String text, Consumer<MutableComponent> format) {
        return addTooltip(() -> TooltipInfo.ofText(text, format));
    }

    public NFFGirlsBaubleBuilder addTooltipText(String text) {
        return addTooltipText(text, c -> {});
    }

    public NFFGirlsBaubleBuilder addTooltipTranslatable(Consumer<MutableComponent> format, String key, Object... args) {
        return addTooltip(() -> TooltipInfo.ofTranslatable(key, format, args));
    }

    public NFFGirlsBaubleBuilder addTooltipTranslatable(String key, Object... args) {
        return addTooltipTranslatable(c -> {}, key, args);
    }

    public NFFGirlsBaubleBuilder addEnvironmentImmunityTooltip() {
        isEnvironmentImmunityTooltipManuallyAdded = true;
        return addTooltipTranslatable("tooltip.nffgirls.bauble.environment_immunity");
    }

    /**
     * Describing a lazy-loaded bauble attribute modifier tooltip supplier.
     */
    public static class ModifierTooltipInfo implements Supplier<MutableComponent> {

        private static final BiConsumer<MutableComponent, ModifierTooltipInfo> DEFAULT_ATTRIBUTE_FORMAT
            = (c, i) -> c.withStyle(ChatFormatting.WHITE);
        private static final BiConsumer<MutableComponent, ModifierTooltipInfo> DEFAULT_AMOUNT_FORMAT
             = (c, i) -> {
            if (i.amount > 0) c.withStyle(ChatFormatting.BLUE);
            else if (i.amount < 0) c.withStyle(ChatFormatting.RED);
            else c.withStyle(ChatFormatting.WHITE);
        };

        private final Attribute attr;
        private final double amount;
        private AttributeModifier.Operation op;
        public BiConsumer<MutableComponent, ModifierTooltipInfo> attributeFormat = DEFAULT_ATTRIBUTE_FORMAT;
        public BiConsumer<MutableComponent, ModifierTooltipInfo> operatorFormat = DEFAULT_AMOUNT_FORMAT;
        public BiConsumer<MutableComponent, ModifierTooltipInfo> amountFormat = DEFAULT_AMOUNT_FORMAT;
        public BiConsumer<MutableComponent, ModifierTooltipInfo> percentFormat = DEFAULT_AMOUNT_FORMAT;
        @Nullable
        private ModifiableSupplier<MutableComponent> additionAtStart = null;
        @Nullable
        private ModifiableSupplier<MutableComponent> additionAtEnd = null;

        public ModifierTooltipInfo(Attribute attr, double amount, AttributeModifier.Operation op) {
            this.attr = attr;
            this.amount = amount;
            this.op = op;
        }

        public ModifierTooltipInfo format(@Nonnull BiConsumer<MutableComponent, ModifierTooltipInfo> formatting) {
            this.attributeFormat = formatting;
            this.operatorFormat = formatting;
            this.amountFormat = formatting;
            this.percentFormat = formatting;
            return this;
        }

        public ModifierTooltipInfo attributeFormat(@Nonnull BiConsumer<MutableComponent, ModifierTooltipInfo> formatting) {
            this.attributeFormat = formatting;
            return this;
        }

        public ModifierTooltipInfo operatorFormat(@Nonnull BiConsumer<MutableComponent, ModifierTooltipInfo> formatting) {
            this.operatorFormat = formatting;
            return this;
        }

        public ModifierTooltipInfo amountFormat(@Nonnull BiConsumer<MutableComponent, ModifierTooltipInfo> formatting) {
            this.amountFormat = formatting;
            return this;
        }

        public ModifierTooltipInfo percentFormat(@Nonnull BiConsumer<MutableComponent, ModifierTooltipInfo> formatting) {
            this.percentFormat = formatting;
            return this;
        }

        public ModifierTooltipInfo appendAtStart(@Nonnull Supplier<MutableComponent> componentSupplier) {
            if (this.additionAtStart == null)
                this.additionAtStart = new ModifiableSupplier<>(componentSupplier);
            else this.additionAtStart.modify(c -> {c.append(componentSupplier.get());});
            return this;
        }

        public ModifierTooltipInfo appendAtEnd(@Nonnull Supplier<MutableComponent> componentSupplier) {
            if (this.additionAtEnd == null)
                this.additionAtEnd = new ModifiableSupplier<>(componentSupplier);
            else this.additionAtEnd.modify(c -> {c.append(componentSupplier.get());});
            return this;
        }

        @Override
        public MutableComponent get() {
            if (Math.abs(amount) < 1e-12d) return null;
            MutableComponent attributeComponent = NFUInfoStatics.createTranslatable("attribute.name." + ForgeRegistries.ATTRIBUTES.getKey(attr).getPath()).withStyle(ChatFormatting.GRAY);
            attributeFormat.accept(attributeComponent, this);
            String operatorStr;
            String amountStr;
            String percentageStr = "";

            DecimalFormat df = new DecimalFormat("#.##");
            df.setDecimalSeparatorAlwaysShown(false);
            String operation = "";

            if (op.equals(AttributeModifier.Operation.ADDITION)) {
                if (NFFGirlsEntityAttributes.isRateAttribute(attr)) {
                    if (amount < 0) {
                        operatorStr = "-";
                        amountStr = df.format(-amount * 100d);
                        percentageStr = "%";
                    } else {
                        operatorStr = "+";
                        amountStr = df.format(amount * 100d);
                        percentageStr = "%";
                    }
                }
                else {
                    if (amount > 0) {
                        operatorStr = "+";
                        amountStr = df.format(amount);
                    } else {
                        operatorStr = "-";
                        amountStr = df.format(-amount);
                    }
                }
            } else {
                if (amount < 0) {
                    operatorStr = "-";
                    amountStr = df.format(-amount * 100d);
                    percentageStr = "%";
                } else if (amount < 1) {
                    operatorStr = "+";
                    amountStr = df.format(amount * 100d);
                    percentageStr = "%";
                } else {
                    operatorStr = "";
                    amountStr = df.format(amount + 1d) + "x";
                }
            }
            MutableComponent operatorComponent = NFUInfoStatics.createText(operatorStr).withStyle(ChatFormatting.GRAY);
            MutableComponent amountComponent = NFUInfoStatics.createText(amountStr).withStyle(ChatFormatting.GRAY);
            MutableComponent percentageComponent = NFUInfoStatics.createText(percentageStr).withStyle(ChatFormatting.GRAY);
            this.operatorFormat.accept(operatorComponent, this);
            this.amountFormat.accept(amountComponent, this);
            this.percentFormat.accept(percentageComponent, this);

            ComponentBuilder builder = ComponentBuilder.create()
                .append(attributeComponent).appendText(" ")
                .append(operatorComponent).append(amountComponent);
            if (!percentageStr.isEmpty())
                builder.append(percentageComponent);
            return builder.build();
        }
    }

    /**
     * Describing a lazy-loaded tooltip supplier.
     */
    public static class TooltipInfo {
        // Called only when validating the builder
        @Nullable
        private Supplier<BaubleAttributeModifier> modifierAccessor = null;
        @Nullable
        private ComponentBuilder fixedPart = null;
        private Supplier<Supplier<MutableComponent>> gettingMethod = this::getDefaultComponentSupplier;
        private Consumer<ModifierTooltipInfo> modifierFormatter = null;
        private Consumer<MutableComponent> fixedFormatter = null;


        private TooltipInfo(@Nonnull Supplier<BaubleAttributeModifier> modifierAccessor) {
            this.modifierAccessor = modifierAccessor;
        }

        private TooltipInfo(Consumer<ComponentBuilder> initializer) {
            this.fixedPart = ComponentBuilder.create();
            initializer.accept(this.fixedPart);
        }

        public static TooltipInfo ofAttribute(Supplier<BaubleAttributeModifier> accessor) {
            return new TooltipInfo(accessor);
        }

        public static TooltipInfo ofFixed(Consumer<ComponentBuilder> initializer) {
            return new TooltipInfo(initializer);
        }

        public static TooltipInfo ofComponent(Supplier<Component> componentSupplier) {
            return ofFixed(b -> b.append(componentSupplier.get()));
        }

        public static TooltipInfo ofComponent(Component component) {
            return ofFixed(b -> b.append(component));
        }

        public static TooltipInfo ofTranslatable(String key, Consumer<MutableComponent> format, Object... args) {
            MutableComponent res = NFUInfoStatics.createTranslatable(key, args);
            format.accept(res);
            return new TooltipInfo(builder -> builder.append(res));
        }

        public static TooltipInfo ofTranslatable(String key, Object... args) {
            return ofTranslatable(key, c -> c.withStyle(ChatFormatting.GRAY), args);
        }

        public static TooltipInfo ofText(String text, Consumer<MutableComponent> format) {
            MutableComponent res = NFUInfoStatics.createTranslatable(text);
            format.accept(res);
            return new TooltipInfo(builder -> builder.append(res));
        }

        public static TooltipInfo ofText(String text) {
            return ofText(text, c -> c.withStyle(ChatFormatting.GRAY));
        }

        /**
         * Format the whole fixed part.
         */
        public TooltipInfo fixedFormatter(Consumer<MutableComponent> formatter) {
            this.fixedFormatter = formatter;
            return this;
        }

        /**
         * Format the modifier part.
         */
        public TooltipInfo modifierFormatter(Consumer<ModifierTooltipInfo> formatter) {
            this.modifierFormatter = formatter;
            return this;
        }

        public TooltipInfo append(Component next) {
            if (this.fixedPart == null) this.fixedPart = ComponentBuilder.create();
            this.fixedPart.append(next);
            return this;
        }

        public TooltipInfo setTooltipCreationMethod(Function<TooltipInfo, Supplier<MutableComponent>> method) {
            this.gettingMethod = () -> method.apply(this);
            return this;
        }

        private Supplier<MutableComponent> getDefaultComponentSupplier() {
            return () -> {
                ComponentBuilder builder = ComponentBuilder.create();
                if (modifierAccessor != null) {
                    BaubleAttributeModifier modifier = modifierAccessor.get();
                    ModifierTooltipInfo modifierInfo = new ModifierTooltipInfo(modifier.getAttribute(),
                        modifier.getAmount(), modifier.getOperation());
                    if (modifierFormatter != null)
                        modifierFormatter.accept(modifierInfo);
                    MutableComponent c = modifierInfo.get();
                    if (c != null)
                        builder.append(modifierInfo.get());
                }
                if (fixedPart != null)
                    builder.append(fixedPart.build());
                MutableComponent res = builder.build();
                if (fixedFormatter != null)
                    fixedFormatter.accept(res);
                return res;
            };
        }

        public Supplier<MutableComponent> getComponentSupplier() {
            return this.gettingMethod.get();
        }

        public Optional<Supplier<BaubleAttributeModifier>> getModifierAccessor() {
            return Optional.ofNullable(this.modifierAccessor);
        }

        public Optional<ComponentBuilder> getFixedPartBuilder() {
            return Optional.ofNullable(this.fixedPart);
        }
    }

    // Building

    public NFFGirlsDedicatedBaubleItem buildAsBaubleItem(ResourceLocation key, int tier, Item.Properties properties, boolean autoRegister) {
        BuiltItem res = new BuiltItem(key, tier, this, properties);
        if (autoRegister)
            NFFGirlsBaubles.BAUBLE_REGISTRY.registerIfAbsent(NFFGirlsDedicatedBaubleItem.getBaubleRegistryKey(key, tier), () -> res);
        this.builtBauble = res;
        this.validate();
        return res;
    }

    public NFFGirlsDedicatedBaubleItem buildAsBaubleItem(ResourceLocation key, int tier, Item.Properties properties) {
        return buildAsBaubleItem(key, tier, properties, true);
    }

    public NFFGirlsBaubleBehavior buildAsBaubleBehavior(ResourceLocation categoryKey, int tier, Item item, boolean autoRegister) {
        BuiltBehavior res = new BuiltBehavior(this, item, categoryKey, tier);
        if (autoRegister)
            NFFGirlsBaubles.BAUBLE_REGISTRY.registerIfAbsent(NFFGirlsBaubleBehavior.getBaubleRegistryKey(categoryKey, tier), () -> res);
        this.builtBauble = res;
        this.validate();
        return res;
    }

    public NFFGirlsBaubleBehavior buildAsBaubleBehavior(ResourceLocation categoryKey, int tier, Item item) {
        return buildAsBaubleBehavior(categoryKey, tier, item, true);
    }

    private static interface IBuiltBauble extends INFFGirlsBauble {

        void onValidate();
        NFFGirlsBaubleBuilder getBuilder();
        default void validate() {
            getBuilder().validate();
        }
    }

    private static class BuiltItem extends NFFGirlsDedicatedBaubleItem implements IBuiltBauble {

        private final NFFGirlsBaubleBuilder builder;

        public BuiltItem(ResourceLocation categoryKey, int tier, NFFGirlsBaubleBuilder builder, Item.Properties properties) {
            super(categoryKey, tier, properties);
            this.builder = builder;
            if (builder.environmentImmune) {
                this.addBaubleTag(INFFGirlsBauble.TAG_ENVIRONMENT_IMMUNITY);
            }
            builder.tags.forEach(this::addBaubleTag);
            this.setNameStyle(builder.nameStyle);
        }

        @Override
        public void slotTick(BaubleProcessingArgs baubleProcessingArgs) {
            if (!builder.isValidated()) throw new IllegalStateException("NFFGirlsBaubleBuilder: Item calling before builder validation.");
            builder.onTick.accept(baubleProcessingArgs);
        }

        @Nullable
        @Override
        public BaubleAttributeModifier[] getRepeatableModifiers(BaubleProcessingArgs baubleProcessingArgs) {
            if (!builder.isValidated()) throw new IllegalStateException("NFFGirlsBaubleBuilder: Item calling before builder validation.");
            return builder.repeatableModifierSuppliers.keySet().stream()
                .map(Supplier::get).toArray(BaubleAttributeModifier[]::new);
        }

        @Nullable
        @Override
        public BaubleAttributeModifier[] getUnrepeatableModifiers(Mob mob) {
            if (!builder.isValidated()) throw new IllegalStateException("NFFGirlsBaubleBuilder: Item calling before builder validation.");
            return builder.unrepeatableModifierSuppliers.keySet().stream()
                .map(Supplier::get).toArray(BaubleAttributeModifier[]::new);
        }

        @Nonnull
        @Override
        public BaubleEquippingCondition getEquippingCondition() {
            return Optional.ofNullable(builder.equippingCondition).orElse(BaubleEquippingConditions.CONDITION_ALWAYS.get());
        }

        @Override
        public void onValidate() {
            this.description(() -> NFUInfoStatics.createTranslatable("tooltip.nffgirls.bauble.dedicated_item"));
            if (builder.showRarityTier)
                builder.getRarityTierDesc().ifPresent(this::description);
            builder.tooltips.get().forEach(this::description);
            if (builder.environmentImmune && !builder.isEnvironmentImmunityTooltipManuallyAdded)
                this.description(ENVIRONMENT_IMMUNITY_TOOLTIP);
            if (builder.useRarityTierNameColor)
                builder.getRarityTierFormat().ifPresent(this::setNameStyle);
        }

        @Override
        public NFFGirlsBaubleBuilder getBuilder(){
            return builder;
        }
    }

    private static class BuiltBehavior extends NFFGirlsBaubleBehavior implements IBuiltBauble {

        private NFFGirlsBaubleBuilder builder;

        public BuiltBehavior(NFFGirlsBaubleBuilder builder, @Nonnull Item item, ResourceLocation categoryKey, int tier) {
            super(item, builder.equippingCondition, categoryKey, tier);
            this.builder = builder;
            if (builder.environmentImmune)
                this.addBaubleTag(INFFGirlsBauble.TAG_ENVIRONMENT_IMMUNITY);
            builder.tags.forEach(this::addBaubleTag);
        }

        @Override
        public void onEquipped(BaubleProcessingArgs baubleProcessingArgs) {
        }

        @Override
        public void preSlotTick(BaubleProcessingArgs baubleProcessingArgs) {
        }

        @Override
        public void postSlotTick(BaubleProcessingArgs baubleProcessingArgs) {
        }

        @Override
        public void slotTick(BaubleProcessingArgs baubleProcessingArgs) {
            builder.onTick.accept(baubleProcessingArgs);
        }

        @Nullable
        @Override
        public BaubleAttributeModifier[] getRepeatableModifiers(BaubleProcessingArgs baubleProcessingArgs) {
            return builder.repeatableModifierSuppliers.keySet().stream()
                .map(Supplier::get).toArray(BaubleAttributeModifier[]::new);
        }

        @Nullable
        @Override
        public BaubleAttributeModifier[] getUnrepeatableModifiers(Mob mob) {
            return builder.unrepeatableModifierSuppliers.keySet().stream()
                .map(Supplier::get).toArray(BaubleAttributeModifier[]::new);
        }

        @Override
        public void onValidate() {
            this.addTooltip(() -> NFUInfoStatics.createTranslatable("tooltip.nffgirls.bauble.existing_item"));
            if (builder.showRarityTier)
                builder.getRarityTierDesc().ifPresent(m -> this.addTooltip(() -> m));
            builder.tooltips.get().forEach(this::addTooltip);
            if (builder.environmentImmune && !builder.isEnvironmentImmunityTooltipManuallyAdded)
                this.addTooltip(() -> NFUInfoStatics.createTranslatable("tooltip.nffgirls.bauble.environment_immunity").withStyle(ChatFormatting.WHITE));
        }

        @Override
        public NFFGirlsBaubleBuilder getBuilder(){
            return builder;
        }
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = NFFGirls.MOD_ID)
    public static class EventListeners {
        // Validate builders after config
        @SubscribeEvent(priority = EventPriority.LOWEST)
        public static void validateBaublesAfterConfig(ModConfigEvent.Loading event) {
            NFFGirlsBaubles.BAUBLE_REGISTRY.values().forEach(bauble -> {
                if (bauble instanceof IBuiltBauble b) {
                    b.validate();
                }
            });
        }

    }



}
