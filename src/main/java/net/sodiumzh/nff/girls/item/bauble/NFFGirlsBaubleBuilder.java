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
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.registry.NFFGirlsConfigs;
import net.sodiumzh.nfu.container.Tuple2;
import net.sodiumzh.nfu.container.Tuple3;
import net.sodiumzh.nfu.function.ModifiableSupplier;
import net.sodiumzh.nfu.info.ComponentBuilder;
import net.sodiumzh.nfu.item.bauble.BaubleAttributeModifier;
import net.sodiumzh.nfu.item.bauble.BaubleEquippingCondition;
import net.sodiumzh.nfu.item.bauble.BaubleProcessingArgs;
import net.sodiumzh.nfu.object.LimitedMutable;
import net.sodiumzh.nfu.object.Validatable;
import net.sodiumzh.nfu.util.NFUDebugStatics;
import net.sodiumzh.nfu.util.NFUInfoStatics;
import org.jetbrains.annotations.Nullable;
import net.sodiumzh.nff.girls.registry.*;

import javax.annotation.Nonnull;
import java.text.DecimalFormat;
import java.util.*;
import java.util.function.*;

@Mod.EventBusSubscriber(modid = NFFGirls.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NFFGirlsBaubleBuilder {

    public static final Predicate<Mob> NO_CONDITION = mob -> true;

    private final Map<Supplier<BaubleAttributeModifier>, Predicate<? super Mob>> repeatableModifierSuppliers
        = new HashMap<>();
    private final Map<Supplier<BaubleAttributeModifier>, Predicate<? super Mob>> unrepeatableModifierSuppliers
        = new HashMap<>();
    private final Validatable<Map<BaubleAttributeModifier, Predicate<? super Mob>>> repeatableModifiers
        = new Validatable<>(new HashMap<>());
    private final Validatable<Map<BaubleAttributeModifier, Predicate<? super Mob>>> unrepeatableModifiers
        = new Validatable<>(new HashMap<>());
    private final List<String> tags = new ArrayList<>();
    // Built bauble reference after building. Null before building.
    @Nullable
    private IBuiltBauble builtBauble = null;

    private final LimitedMutable<Boolean> validated = new LimitedMutable<>(false, 1);

    private boolean environmentImmune = false;
    private Consumer<BaubleProcessingArgs> onTick = args -> {};
    private BaubleEquippingCondition equippingCondition = BaubleEquippingCondition.always();

    private static BaubleAttributeModifier getModifierFromProperties(Attribute attribute, double amount, AttributeModifier.Operation operation) {
        if (!operation.equals(AttributeModifier.Operation.ADDITION))
            return new BaubleAttributeModifier(attribute, amount, operation);
        double actualAmount = amount;
        if (attribute.equals(Attributes.MAX_HEALTH))
            actualAmount *= NFFGirlsConfigs.ValueCache.Baubles.BAUBLE_HEALTH_RECOVERY_SCALE;
        else if (attribute.equals(Attributes.ATTACK_DAMAGE))
            actualAmount *= NFFGirlsConfigs.ValueCache.Baubles.BAUBLE_ATK_BOOSTING_SCALE;
        else if (attribute.equals(Attributes.ARMOR))
            actualAmount *= NFFGirlsConfigs.ValueCache.Baubles.BAUBLE_ARMOR_BOOSTING_SCALE;
        else if (attribute.equals(NFFGirlsEntityAttributes.PERSISTENT_HEALING_PER_SECOND.get()))
            actualAmount *= NFFGirlsConfigs.ValueCache.Baubles.BAUBLE_HEALTH_RECOVERY_SCALE;
        return new BaubleAttributeModifier(attribute, actualAmount, operation);
    }

    public NFFGirlsBaubleBuilder repeatable(Attribute attr, double amount, AttributeModifier.Operation operation, Predicate<? super Mob> condition) {
        if (this.validated.get()) {
            NFUDebugStatics.errorOnce("Builder has been validated, no more modification. Skipped.");
        }
        repeatableModifierSuppliers.put(() -> getModifierFromProperties(attr, amount, operation), condition);
        return this;
    }

    public NFFGirlsBaubleBuilder repeatable(Attribute attr, double amount, AttributeModifier.Operation operation) {
        return repeatable(attr, amount, operation, NO_CONDITION);
    }

    public NFFGirlsBaubleBuilder unrepeatable(Attribute attr, double amount, AttributeModifier.Operation operation, Predicate<? super Mob> condition) {
        unrepeatableModifierSuppliers.put(() -> getModifierFromProperties(attr, amount, operation), condition);
        return this;
    }

    public NFFGirlsBaubleBuilder unrepeatable(Attribute attr, double amount, AttributeModifier.Operation operation) {
        return unrepeatable(attr, amount, operation, NO_CONDITION);
    }

    public NFFGirlsBaubleBuilder environmentResistance() {
        this.environmentImmune = true;
        return this;
    }

    public NFFGirlsBaubleBuilder equippingCondition(@Nullable BaubleEquippingCondition condition, @Nullable Component info) {
        this.equippingCondition = Optional.ofNullable(condition).orElse(BaubleEquippingCondition.always());
        this.equippingConditionTooltip = info;
        return this;
    }

    public NFFGirlsBaubleBuilder equippingCondition(@Nullable BaubleEquippingCondition condition) {
        return equippingCondition(condition, null);
    }

    public NFFGirlsBaubleBuilder equippingCondition(@Nonnull Tuple2<BaubleEquippingCondition, Component> conditionAndInfo) {
        return equippingCondition(conditionAndInfo.getA(), conditionAndInfo.getB());
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
    private static final Comparator<Attribute> COMPARING_ATTRIBUTE = (o1, o2) -> {
        List<Attribute> reserved = List.of(Attributes.MAX_HEALTH, Attributes.ATTACK_DAMAGE,
            Attributes.ARMOR, NFFGirlsEntityAttributes.CRITICAL_RATE.get(),
            NFFGirlsEntityAttributes.ANTI_UNDEAD.get(), NFFGirlsEntityAttributes.ANTI_ARTHROPOD.get(),
            NFFGirlsEntityAttributes.WATER_ASPECT.get(), Attributes.MOVEMENT_SPEED,
            NFFGirlsEntityAttributes.PERSISTENT_HEALING_PER_SECOND.get(),
            NFFGirlsEntityAttributes.LOOTING_LEVEL.get());
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
        NFUInfoStatics.createTranslatable("tooltip.nffgirls.bauble.environment_immunity").withStyle(ChatFormatting.GRAY);

    private boolean isEnvironmentImmunityTooltipManuallyAdded = false;

    @Nullable
    private Component equippingConditionTooltip = null;

    public NFFGirlsBaubleBuilder addRepeatableModifierTooltips(@Nullable Supplier<Predicate<? extends Mob>> conditionFilter, Consumer<ModifierTooltipInfo> format) {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setDecimalSeparatorAlwaysShown(false);
        tooltipsSupplier.modify(list -> {
            list.addAll(this.repeatableModifiers.get().entrySet().stream()
                .filter(e -> conditionFilter == null || conditionFilter.get() == null || conditionFilter.get().equals(e.getValue()))
                .sorted(Comparator.comparing(e -> e.getKey().getAttribute(), COMPARING_ATTRIBUTE))
                .map(e -> TooltipInfo.ofAttribute(e::getKey).modifierFormatter(format))
                .toList());
        });
        return this;
    }

    public NFFGirlsBaubleBuilder addRepeatableModifierTooltips(@Nullable Supplier<Predicate<? extends Mob>> conditionFilter) {
        return addRepeatableModifierTooltips(conditionFilter, m -> {});
    }

    public NFFGirlsBaubleBuilder addRepeatableModifierTooltips() {
        return addRepeatableModifierTooltips(null);
    }

    public NFFGirlsBaubleBuilder addUnrepeatableModifierTooltips(@Nullable Supplier<Predicate<? extends Mob>> conditionFilter, Consumer<ModifierTooltipInfo> format, boolean showUnrepeatableTooltip) {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setDecimalSeparatorAlwaysShown(false);
        tooltipsSupplier.modify(list -> {
            list.addAll(this.unrepeatableModifiers.get().entrySet().stream()
                .filter(e -> conditionFilter == null || conditionFilter.get() == null || conditionFilter.get().equals(e.getValue()))
                .sorted(Comparator.comparing(e -> e.getKey().getAttribute(), COMPARING_ATTRIBUTE))
                .map(e -> {
                    TooltipInfo tooltipInfo = TooltipInfo.ofAttribute(e::getKey).modifierFormatter(format);
                    if (showUnrepeatableTooltip) tooltipInfo.append(DEFAULT_UNREPEATABLE_TIP.get());
                    return tooltipInfo;
                }).toList());
        });
        return this;
    }

    public NFFGirlsBaubleBuilder addUnrepeatableModifierTooltips(@Nullable Supplier<Predicate<? extends Mob>> conditionFilter, Consumer<ModifierTooltipInfo> format) {
        return addUnrepeatableModifierTooltips(conditionFilter, format, true);
    }

    public NFFGirlsBaubleBuilder addUnrepeatableModifierTooltips(@Nullable Supplier<Predicate<? extends Mob>> conditionFilter, boolean showUnrepeatableTooltip) {
        return addUnrepeatableModifierTooltips(conditionFilter, c -> {}, showUnrepeatableTooltip);
    }

        public NFFGirlsBaubleBuilder addUnrepeatableModifierTooltips(@Nullable Supplier<Predicate<? extends Mob>> conditionFilter) {
        return addUnrepeatableModifierTooltips(conditionFilter, c -> {});
    }

    public NFFGirlsBaubleBuilder addUnrepeatableModifierTooltips() {
        return addUnrepeatableModifierTooltips(null);
    }

    public NFFGirlsBaubleBuilder addAllTooltips(@Nullable Supplier<Predicate<? super Mob>> conditionFilter,
                                                Consumer<ModifierTooltipInfo> format, boolean showUnrepeatableTip) {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setDecimalSeparatorAlwaysShown(false);
        tooltipsSupplier.modify(list -> {
            var allModifiers = (List<Tuple3<BaubleAttributeModifier, Predicate<Mob>, Boolean>>)
                new ArrayList<>(this.unrepeatableModifiers.get().entrySet().stream().map(e -> Tuple3.of(e.getKey(), (Predicate<Mob>)e.getValue(), false)).toList());
            allModifiers.addAll(this.repeatableModifiers.get().entrySet().stream().map(e -> Tuple3.of(e.getKey(), (Predicate<Mob>)e.getValue(), true)).toList());
            list.addAll(allModifiers.stream()
                .filter(e -> conditionFilter == null || conditionFilter.equals(e.b))
                .sorted(Comparator.comparing(e -> e.a.getAttribute(), COMPARING_ATTRIBUTE))
                .map(e -> {
                    TooltipInfo tooltipInfo = TooltipInfo.ofAttribute(() -> e.a).modifierFormatter(format);
                    if (!e.c && showUnrepeatableTip)
                        tooltipInfo.append(DEFAULT_UNREPEATABLE_TIP.get());
                    return tooltipInfo;
                }).toList());
        });
        return this;
    }

    public NFFGirlsBaubleBuilder addAllTooltips(@Nullable Supplier<Predicate<? super Mob>> conditionFilter, Consumer<ModifierTooltipInfo> format) {
        return addAllTooltips(conditionFilter, format, true);
    }

    public NFFGirlsBaubleBuilder addAllTooltips(@Nullable Supplier<Predicate<? super Mob>> conditionFilter) {
        return addAllTooltips(conditionFilter, c -> {});
    }

    public NFFGirlsBaubleBuilder addAllTooltips() {
        return addAllTooltips(null);
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

        private static final Consumer<MutableComponent> DEFAULT_ATTRIBUTE_FORMAT
            = c -> c.withStyle(ChatFormatting.GRAY);

        private final Attribute attr;
        private final double amount;
        private AttributeModifier.Operation op;
        private Consumer<MutableComponent> attributeFormat = DEFAULT_ATTRIBUTE_FORMAT;
        private Consumer<MutableComponent> operatorFormat = DEFAULT_ATTRIBUTE_FORMAT;
        private Consumer<MutableComponent> amountFormat = DEFAULT_ATTRIBUTE_FORMAT;
        private Consumer<MutableComponent> percentFormat = DEFAULT_ATTRIBUTE_FORMAT;
        @Nullable
        private ModifiableSupplier<MutableComponent> additionAtStart = null;
        @Nullable
        private ModifiableSupplier<MutableComponent> additionAtEnd = null;


        public ModifierTooltipInfo(Attribute attr, double amount, AttributeModifier.Operation op) {
            this.attr = attr;
            this.amount = amount;
            this.op = op;
        }

        public ModifierTooltipInfo format(@Nonnull Consumer<MutableComponent> formatting) {
            this.attributeFormat = formatting;
            this.operatorFormat = formatting;
            this.amountFormat = formatting;
            this.percentFormat = formatting;
            return this;
        }

        public ModifierTooltipInfo attributeFormat(@Nonnull Consumer<MutableComponent> formatting) {
            this.attributeFormat = formatting;
            return this;
        }

        public ModifierTooltipInfo operatorFormat(@Nonnull Consumer<MutableComponent> formatting) {
            this.operatorFormat = formatting;
            return this;
        }

        public ModifierTooltipInfo amountFormat(@Nonnull Consumer<MutableComponent> formatting) {
            this.amountFormat = formatting;
            return this;
        }

        public ModifierTooltipInfo percentFormat(@Nonnull Consumer<MutableComponent> formatting) {
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
            attributeFormat.accept(attributeComponent);
            String operatorStr;
            String amountStr;
            String percentageStr = "";

            DecimalFormat df = new DecimalFormat("#.##");
            df.setDecimalSeparatorAlwaysShown(false);
            String operation = "";

            if (op.equals(AttributeModifier.Operation.ADDITION)) {
                if (amount > 0) {
                    operatorStr = "+";
                    amountStr = df.format(amount);
                } else {
                    operatorStr = "-";
                    amountStr = df.format(-amount);
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
                    operatorStr = "*";
                    amountStr = df.format(amount + 1d);
                }
            }
            MutableComponent operatorComponent = NFUInfoStatics.createText(operatorStr).withStyle(ChatFormatting.GRAY);
            MutableComponent amountComponent = NFUInfoStatics.createText(amountStr).withStyle(ChatFormatting.GRAY);
            MutableComponent percentageComponent = NFUInfoStatics.createText(percentageStr).withStyle(ChatFormatting.GRAY);
            this.operatorFormat.accept(operatorComponent);
            this.amountFormat.accept(amountComponent);
            this.percentFormat.accept(percentageComponent);

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
        private Consumer<ModifierTooltipInfo> modifierFormatter = m -> m.format(c -> c.withStyle(ChatFormatting.GRAY));
        private Consumer<MutableComponent> fixedFormatter = c -> c.withStyle(ChatFormatting.GRAY);


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
                    modifierFormatter.accept(modifierInfo);
                    builder.append(modifierInfo.get());
                }
                if (fixedPart != null)
                    builder.append(fixedPart.build());
                MutableComponent res = builder.build();
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
            NFFGirlsBaubles.BAUBLE_REGISTRY.register(NFFGirlsDedicatedBaubleItem.getBaubleRegistryKey(key, tier), () -> res);
        this.builtBauble = res;
        return res;
    }

    public NFFGirlsDedicatedBaubleItem buildAsBaubleItem(ResourceLocation key, int tier, Item.Properties properties) {
        return buildAsBaubleItem(key, tier, properties, true);
    }

    public NFFGirlsBaubleBehavior buildAsBaubleBehavior(ResourceLocation categoryKey, int tier, Item item, boolean autoRegister) {
        BuiltBehavior res = new BuiltBehavior(this, item, categoryKey, tier);
        if (autoRegister)
            NFFGirlsBaubles.BAUBLE_REGISTRY.register(NFFGirlsBaubleBehavior.getBaubleRegistryKey(categoryKey, tier), () -> res);
        this.builtBauble = res;
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
        }

        @Override
        public void slotTick(BaubleProcessingArgs baubleProcessingArgs) {
            if (!builder.isValidated()) throw new IllegalStateException("NFFGirlsBaubleBuilder: Item calling before builder validation.");
            builder.onTick.accept(baubleProcessingArgs);
        }

        @Nullable
        @Override
        public BaubleAttributeModifier[] getDuplicableModifiers(BaubleProcessingArgs baubleProcessingArgs) {
            if (!builder.isValidated()) throw new IllegalStateException("NFFGirlsBaubleBuilder: Item calling before builder validation.");
            return builder.repeatableModifiers.get().entrySet().stream().filter(entry -> entry.getValue().test(baubleProcessingArgs.user()))
                .map(Map.Entry::getKey).toArray(BaubleAttributeModifier[]::new);
        }

        @Nullable
        @Override
        public BaubleAttributeModifier[] getNonDuplicableModifiers(Mob mob) {
            if (!builder.isValidated()) throw new IllegalStateException("NFFGirlsBaubleBuilder: Item calling before builder validation.");
            return builder.unrepeatableModifiers.get().entrySet().stream().filter(entry -> entry.getValue().test(mob))
                .map(Map.Entry::getKey).toArray(BaubleAttributeModifier[]::new);
        }

        @Nonnull
        @Override
        public BaubleEquippingCondition getEquippingCondition() {
            return Optional.ofNullable(builder.equippingCondition).orElse(BaubleEquippingCondition.always());
        }

        @Override
        public void onValidate() {
            builder.tooltips.get().forEach(this::description);
            if (builder.environmentImmune && !builder.isEnvironmentImmunityTooltipManuallyAdded)
                this.description(ENVIRONMENT_IMMUNITY_TOOLTIP);
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
        public BaubleAttributeModifier[] getDuplicableModifiers(BaubleProcessingArgs baubleProcessingArgs) {
            return builder.repeatableModifiers.get().entrySet().stream()
                .filter(entry -> entry.getValue().test(baubleProcessingArgs.user()))
                .map(Map.Entry::getKey).toArray(BaubleAttributeModifier[]::new);
        }

        @Nullable
        @Override
        public BaubleAttributeModifier[] getNonDuplicableModifiers(Mob mob) {
            return builder.unrepeatableModifiers.get().entrySet().stream()
                .filter(entry -> entry.getValue().test(mob))
                .map(Map.Entry::getKey).toArray(BaubleAttributeModifier[]::new);
        }

        @Override
        public void onValidate() {
            builder.tooltips.get().forEach(this::addTooltip);
            if (builder.environmentImmune && !builder.isEnvironmentImmunityTooltipManuallyAdded)
                this.addTooltip(() -> NFUInfoStatics.createTranslatable("tooltip.nffgirls.bauble.environment_immunity").withStyle(ChatFormatting.GRAY));
        }

        @Override
        public NFFGirlsBaubleBuilder getBuilder(){
            return builder;
        }
    }

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
