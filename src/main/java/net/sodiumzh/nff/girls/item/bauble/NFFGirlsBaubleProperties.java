package net.sodiumzh.nff.girls.item.bauble;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.registries.ForgeRegistries;
import net.sodiumzh.nfu.info.ComponentBuilder;
import net.sodiumzh.nfu.item.bauble.BaubleAttributeModifier;
import net.sodiumzh.nfu.item.bauble.BaubleEquippingCondition;
import net.sodiumzh.nfu.item.bauble.BaubleProcessingArgs;
import net.sodiumzh.nfu.util.NFUDataStatics;
import net.sodiumzh.nfu.util.NFUInfoStatics;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class NFFGirlsBaubleProperties {

    @Nullable
    private BaubleEquippingCondition equippingCondition;
    private final List<IProperty> properties = new ArrayList<>();
    private final List<DescriptionProvider> descriptions = new ArrayList<>();

    public NFFGirlsBaubleProperties setEquippingCondition(BaubleEquippingCondition condition) {
        this.equippingCondition = condition;
        return this;
    }

    /* Getters */

    /**
     * Get an immutable view of the property list.
     */
    public List<IProperty> propertiesView() {
        return Collections.unmodifiableList(properties);
    }

    public List<DescriptionProvider> descriptionProvidersView() {
        return Collections.unmodifiableList(this.descriptions);
    }

    @Nullable
    public BaubleEquippingCondition getEquippingCondition() {
        return this.equippingCondition;
    }

    public List<ModifierProperty> getModifierProperties(@Nullable BaubleProcessingArgs argsFilter) {
        return this.properties.stream()
            .filter(prop -> prop instanceof ModifierProperty erp && (argsFilter == null && erp.applyingCondition.test(argsFilter)))
            .map(prop -> (ModifierProperty)prop)
            .toList();
    }

    public List<ModifierProperty> getModifierProperties() {
        return getModifierProperties(null);
    }

    public List<TagProperty> getTagProperties(@Nullable BaubleProcessingArgs argsFilter) {
        return this.properties.stream()
            .filter(prop -> prop instanceof TagProperty p && (argsFilter == null && p.applyingCondition.test(argsFilter)))
            .map(prop -> (TagProperty)prop)
            .toList();
    }

    public List<TagProperty> getTagProperties() {
        return getTagProperties(null);
    }

    public boolean hasTag(String tag, BaubleProcessingArgs args) {
        return getTagProperties(args).stream().anyMatch(p -> p.name.equals(tag));
    }

    public boolean hasEnvironmentResistance(@Nullable BaubleProcessingArgs argsFilter) {
        return this.properties.stream().anyMatch(prop ->
            prop instanceof EnvironmentResistanceProperty erp && (argsFilter == null && erp.applyingCondition.test(argsFilter)));
    }

    public boolean hasEnvironmentResistance() {
        return hasEnvironmentResistance(null);
    }

    public List<TickActionProperty> getTickActionProperties(@Nullable BaubleProcessingArgs argsFilter) {
        return this.properties.stream()
            .filter(prop -> prop instanceof TickActionProperty p && (argsFilter == null && p.applyingCondition.test(argsFilter)))
            .map(prop -> (TickActionProperty)prop)
            .toList();
    }

    public List<TickActionProperty> getTickActionProperties() {
        return getTickActionProperties(null);
    }

    /* Adders */

    public NFFGirlsBaubleProperties addModifier(
        boolean repeatable,
        Attribute attribute,
        double amount,
        AttributeModifier.Operation operation,
        @Nullable ResourceLocation unrepeatableIdentifier,
        @Nullable String groupName,
        @Nullable PropertyApplyingCondition applyingCondition
    ) {
        ModifierProperty property = new ModifierProperty(repeatable, attribute, amount, operation, unrepeatableIdentifier, groupName, applyingCondition);
        properties.add(property);
        return this;
    }

    public NFFGirlsBaubleProperties addDefaultModifier (
        boolean repeatable,
        Attribute attribute,
        double amount,
        AttributeModifier.Operation operation
    ) {
        return addModifier(repeatable, attribute, amount, operation, null, null, null);
    }

    public NFFGirlsBaubleProperties addTag(
        String name,
        Component desc,
        @Nullable PropertyApplyingCondition applyingCondition
    ) {
        TagProperty property = new TagProperty(name, desc, applyingCondition);
        properties.add(property);
        return this;
    }

    public NFFGirlsBaubleProperties addTag(
        String name,
        Component desc) {
        return addTag(name, desc, null);
    }

    public NFFGirlsBaubleProperties addTag(
        String name) {
        return addTag(name, NFUInfoStatics.createTranslatable("tooltip.nffgirls.bauble.tag." + name), null);
    }

    public NFFGirlsBaubleProperties environmentResistance(@Nullable PropertyApplyingCondition applyingCondition) {
        properties.add(new EnvironmentResistanceProperty(applyingCondition));
        return this;
    }

    public NFFGirlsBaubleProperties environmentResistance() {
        return environmentResistance(null);
    }

    public NFFGirlsBaubleProperties addTickAction(Component desc, @Nullable String name, Consumer<BaubleProcessingArgs> action,
                                                  @Nullable PropertyApplyingCondition applyingCondition) {
        properties.add(new TickActionProperty(action, desc, name, applyingCondition));
        return this;
    }

    public NFFGirlsBaubleProperties addTickAction(Component desc, Consumer<BaubleProcessingArgs> action) {
        return addTickAction(desc, null, action, null);
    }
    
    /* Description adders */

    public NFFGirlsBaubleProperties desc(DescriptionProvider desc) {
        descriptions.add(desc);
        return this;
    }

    public NFFGirlsBaubleProperties desc(Component desc) {
        descriptions.add(() -> desc);
        return this;
    }

    public NFFGirlsBaubleProperties desc(String translationKey) {
        return desc(NFUInfoStatics.createTranslatable(translationKey));
    }

    public NFFGirlsBaubleProperties descEquippingCondition() {
        if (this.equippingCondition != null)
            descriptions.add(this.equippingCondition::getTranslation);
        return this;
    }

    private static int attributePriority(Attribute attr) {
        if (attr.equals(Attributes.MAX_HEALTH)) return 0;
        if (attr.equals(Attributes.ATTACK_DAMAGE)) return 1;
        if (attr.equals(Attributes.ARMOR)) return 2;
        else return 3;
    }

    public NFFGirlsBaubleProperties descModifiers(@Nullable List<String> groupFilter, boolean negate) {
        this.properties.stream().filter(p -> p instanceof ModifierProperty)
            .map(p -> (ModifierProperty)p)
            .filter(p -> groupFilter == null || (groupFilter.contains(p.groupName) ^ negate))
            .sorted(Comparator.comparingInt(p -> attributePriority(p.attribute())))
            .forEach(descriptions::add);
        return this;
    }

    public NFFGirlsBaubleProperties descAllModifiers() {
        return this.descModifiers(null, false);
    }

    public NFFGirlsBaubleProperties descTags(@Nullable List<String> keyFilter, boolean negate) {
        this.properties.stream().filter(p -> p instanceof TagProperty)
            .map(p -> (TagProperty)p)
            .filter(p -> keyFilter == null || (keyFilter.contains(p.name) ^ negate))
            .forEach(descriptions::add);
        return this;
    }

    public NFFGirlsBaubleProperties descTags() {
        return descTags(null, false);
    }

    public NFFGirlsBaubleProperties descEnvironmentResistance() {
        this.descriptions.add(EnvironmentResistanceProperty::getDescriptionStatic);
        return this;
    }

    public NFFGirlsBaubleProperties descEnvironmentResistanceIfPresent() {
        if (this.properties.stream().anyMatch(prop -> prop instanceof EnvironmentResistanceProperty))
            this.descEnvironmentResistance();
        return this;
    }

    public NFFGirlsBaubleProperties descTickActions(@Nullable List<String> nameFilter, boolean negate) {
        this.properties.stream().filter(p -> p instanceof TickActionProperty)
            .map(p -> (TickActionProperty)p)
            .filter(p -> nameFilter == null || (nameFilter.contains(p.name) ^ negate))
            .forEach(descriptions::add);
        return this;
    }

    public NFFGirlsBaubleProperties descTickActions() {
        return descTickActions(null, false);
    }
    
    public NFFGirlsBaubleProperties descAll() {
        this.descAllModifiers();
        this.descTags();
        this.descTickActions();
        this.descEnvironmentResistanceIfPresent();
        return this;
    }

    public List<Component> createTooltips() {
        return this.descriptions.stream().map(DescriptionProvider::getDescription).toList();
    }

    /**
     * Any interface that can provide a row of description. Including property entries and plain suppliers.
     * <p>Invoked when merging descriptions and sending to the client.
     */
    @FunctionalInterface
    public interface DescriptionProvider {
        Component getDescription();
    }

    /**
     * A predicate that tests if a property should be applied to the mob.
     */
    public interface PropertyApplyingCondition extends Predicate<BaubleProcessingArgs> {}

    /**
     * Any property entry.
     */
    public static interface IProperty extends DescriptionProvider {
        PropertyApplyingCondition getApplyingCondition();
    }

    /**
     * A property that provides an attribute modifier.
     * @param repeatable Whether this modifier is repeatable.
     * @param attribute Attribute type.
     * @param amount Value.
     * @param operation Modifier operation type.
     * @param unrepeatableIdentifier If non-null, when this modifier is unrepeatable, only the maximum value of all
     *                               modifiers with this identifier will be applied. Note that modifiers with the same
     *                               identifier must have identical attribute and operation, or it will throw an exception.
     * @param groupName A name to define groups for batch adding tooltips.
     * @param applyingCondition Predicate whether this condition can be applied.
     */
    public record ModifierProperty(
        boolean repeatable,
        Attribute attribute,
        double amount,
        AttributeModifier.Operation operation,
        @Nullable ResourceLocation unrepeatableIdentifier,
        @Nullable String groupName,
        @Nullable PropertyApplyingCondition applyingCondition) implements IProperty
    {

        @Override
        public Component getDescription() {
            ComponentBuilder builder = ComponentBuilder.create();
            builder.append(NFUInfoStatics.createTranslatable("attribute.name." + ForgeRegistries.ATTRIBUTES.getKey(this.attribute()).getPath())
                .withStyle(ChatFormatting.GRAY))
                .appendText(" ");
            // Format amount
            DecimalFormat df = new DecimalFormat("#.##");
            df.setDecimalSeparatorAlwaysShown(false);
            String operator = this.amount >= 0 ? "+" : "-";
            double amountShownValue = this.operation() == AttributeModifier.Operation.ADDITION ? Math.abs(this.amount) : Math.abs(this.amount * 100);
            String amountShown = df.format(amountShownValue);
            StringBuilder amountStringFinal = new StringBuilder().append(operator).append(amountShown);
            if (this.operation() != AttributeModifier.Operation.ADDITION)
                amountStringFinal.append("%");

            builder.append(NFUInfoStatics.createText(amountStringFinal.toString(), c -> c.withStyle(this.amount >= 0 ? ChatFormatting.BLUE : ChatFormatting.RED)));
            return builder.build();
        }

        @Override
        public PropertyApplyingCondition getApplyingCondition() {
            return applyingCondition;
        }

        public BaubleAttributeModifier createModifier() {
            BaubleAttributeModifier mod = new BaubleAttributeModifier(this.attribute(), this.amount(), this.operation());
            mod.setAdditionalCondition(this.applyingCondition());
            mod.setAdditionalID(this.unrepeatableIdentifier());
            return mod;
        }
    }

    /**
     * A tag described by a string. Implemented elsewhere.
     */
    public record TagProperty(String name, Component description, PropertyApplyingCondition applyingCondition) implements IProperty {

        public static TagProperty withPlainDesc(String name, String translationKey, @Nullable PropertyApplyingCondition applyingCondition) {
            return new TagProperty(name, NFUInfoStatics.createTranslatable(translationKey).withStyle(ChatFormatting.WHITE), applyingCondition);
        }

        public static TagProperty withDefaultDesc(String name, @Nullable PropertyApplyingCondition applyingCondition) {
            return withPlainDesc(name, "tooltip.nffgirls.bauble.tag." + name, applyingCondition);
        }

        @Override
        public PropertyApplyingCondition getApplyingCondition() {
            return applyingCondition();
        }

        @Override
        public Component getDescription() {
            return description();
        }
    }

    public record EnvironmentResistanceProperty(PropertyApplyingCondition applyingCondition) implements IProperty {

        @Override
        public PropertyApplyingCondition getApplyingCondition() {
            return applyingCondition();
        }

        @Override
        public Component getDescription() {
            return getDescriptionStatic();
        }

        public static Component getDescriptionStatic() {
            return NFUInfoStatics.createTranslatable("info.nffgirls.bauble.environment_immunity");
        }
    }

    public record TickActionProperty(
        Consumer<BaubleProcessingArgs> action,
        Component description,
        @Nullable String name,
        PropertyApplyingCondition applyingCondition) implements IProperty
    {

        @Override
        public PropertyApplyingCondition getApplyingCondition() {
            return applyingCondition;
        }

        @Override
        public Component getDescription() {
            return description;
        }
    }

}
