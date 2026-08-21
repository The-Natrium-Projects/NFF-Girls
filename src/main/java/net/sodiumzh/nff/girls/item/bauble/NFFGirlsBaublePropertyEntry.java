package net.sodiumzh.nff.girls.item.bauble;

import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.registries.ForgeRegistries;
import net.sodiumzh.nfu.function.RegistrablePredicate;
import net.sodiumzh.nfu.item.bauble.BaubleEquippingCondition;
import net.sodiumzh.nfu.registry.NFURegistries;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.function.Predicate;

public class NFFGirlsBaublePropertyEntry {

    private final Type type;
    private final @Nullable Attribute attribute;
    private final double amount;    // Not valid if it's TAG
    private final AttributeModifier.Operation operation;
    private final @Nullable String tag;
    private final @Nullable Predicate<LivingEntity> effectCondition;


    private boolean isRepeatable = true;
    private ResourceLocation id = null;

    private NFFGirlsBaublePropertyEntry(Type type,
                                        @Nullable Attribute attribute,
                                        double amount,
                                        AttributeModifier.Operation operation,
                                        @Nullable String tag,
                                        @Nullable Predicate<LivingEntity> effectCondition) {
        this.type = type;
        this.attribute = attribute;
        this.amount = amount;
        this.operation = operation;
        this.tag = tag;
        this.effectCondition = effectCondition;
    }

    public static NFFGirlsBaublePropertyEntry byAttribute(Attribute attribute, double amount, AttributeModifier.Operation operation, Predicate<LivingEntity> effectCondition) {
        return new NFFGirlsBaublePropertyEntry(Type.ATTRIBUTE, attribute, amount, operation, null, effectCondition);
    }

    public static NFFGirlsBaublePropertyEntry byAttribute(Attribute attribute, double amount, AttributeModifier.Operation operation) {
        return byAttribute(attribute, amount, operation, null);
    }

    public static NFFGirlsBaublePropertyEntry byTag(String tag, Predicate<LivingEntity> effectCondition) {
        return new NFFGirlsBaublePropertyEntry(Type.TAG, null, 0, null, tag, effectCondition);
    }

    public static NFFGirlsBaublePropertyEntry byTag(String tag) {
        return byTag(tag, null);
    }

    public static NFFGirlsBaublePropertyEntry environmentResistance(@Nullable Predicate<LivingEntity> effectCondition) {
        return new NFFGirlsBaublePropertyEntry(Type.ENVIRONMENT_RESISTANCE, null, 0, null, null, effectCondition);
    }

    public static NFFGirlsBaublePropertyEntry environmentResistance() {
        return environmentResistance(null);
    }

    public void modifyBauble(NFFGirlsBaubleProperties properties) {
        switch (this.type) {
            case ATTRIBUTE: {
                if (this.isRepeatable)
                    properties.repeatable(attribute, amount, operation, effectCondition, id);
                else properties.unrepeatable(attribute, amount, operation, effectCondition, id);
                break;
            }
            case TAG: {
                if (tag.equals("environment_immunity"))
                    properties.environmentResistance();
                else properties.addTag(tag);
                break;
            }
            case ENVIRONMENT_RESISTANCE: {
                properties.environmentResistance();
            }
        }
    }

    public void setUnrepeatable() {
        this.isRepeatable = false;
    }

    public void apply(NFFGirlsBaubleProperties properties) {
        if (this.type.equals(Type.ENVIRONMENT_RESISTANCE)) {
            properties.environmentResistance();
        } else if (this.type.equals(Type.TAG)) {
            properties.addTag(this.tag);
        } else {
            if (this.isRepeatable) {
                properties.repeatable(this.attribute, this.amount, this.operation, this.effectCondition, this.id);
            } else {
                properties.unrepeatable(this.attribute, this.amount, this.operation, this.effectCondition, this.id);
            }
        }
    }

    @Nullable
    public static NFFGirlsBaublePropertyEntry byJson(JsonObject json) {
        try {
            // Switch type
            Type type = Type.byName(json.has("type") ? json.get("type").getAsString() : null);
            if (type == null) return null;
            // Get effect condition
            RegistrablePredicate<LivingEntity> condition = json.has("condition") ?
                NFURegistries.PREDICATES
                    .getOptionalValue(new ResourceLocation(json.get("condition").getAsString()))
                    .flatMap(p -> p.castInputType(LivingEntity.class))
                    .orElse(null)
                : null;
            // Construct property entry instance
            switch (type) {
                case ENVIRONMENT_RESISTANCE: {
                    return NFFGirlsBaublePropertyEntry.environmentResistance(condition);
                }
                case TAG: {
                    String tag = json.get("tag").getAsString();
                    return NFFGirlsBaublePropertyEntry.byTag(tag, condition);
                }
                case ATTRIBUTE: {
                    ResourceLocation keyAttr = new ResourceLocation(json.get("attribute").getAsString());
                    Attribute attr = ForgeRegistries.ATTRIBUTES.containsKey(keyAttr) ? ForgeRegistries.ATTRIBUTES.getValue(keyAttr) : null;
                    if (attr == null) return null;
                    double amount = json.get("amount").getAsDouble();
                    AttributeModifier.Operation operation =
                        switch (json.get("operation").getAsString()) {
                            case "add", "+", "addition" -> AttributeModifier.Operation.ADDITION;
                            case "multiply", "*", "multiply_base" -> AttributeModifier.Operation.MULTIPLY_BASE;
                            case "multiply_total", "**", "*+" -> AttributeModifier.Operation.MULTIPLY_TOTAL;
                            default -> AttributeModifier.Operation.ADDITION;
                        };
                    return NFFGirlsBaublePropertyEntry.byAttribute(attr, amount, operation, condition);
                }
                default: return null;
            }
        } catch (RuntimeException ex) {
            LogUtils.getLogger().error("Bauble property loading failed. Skipped.", ex);
            return null;
        }
    }

    public static enum Type {
        ATTRIBUTE("attribute"), TAG("tag"), ENVIRONMENT_RESISTANCE("environment_resistance");

        final String name;

        Type(String name) {
            this.name = name;
        }

        @Nullable
        public static Type byName(String name) {
            return Arrays.stream(Type.values())
                .filter(t -> t.name.equals(name))
                .findAny().orElse(null);
        }

    }
}
