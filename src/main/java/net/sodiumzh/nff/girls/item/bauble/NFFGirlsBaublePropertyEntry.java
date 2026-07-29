package net.sodiumzh.nff.girls.item.bauble;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.sodiumzh.nfu.item.bauble.BaubleEquippingCondition;

import javax.annotation.Nullable;
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

    public static enum Type {
        ATTRIBUTE("attribute"), TAG("tag"), ENVIRONMENT_RESISTANCE("environment_resistance");

        String name;

        Type(String name) {
            this.name = name;
        }
    }
}
