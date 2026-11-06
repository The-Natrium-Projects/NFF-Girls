package net.sodiumzh.nff.girls.item.bauble;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.sodiumzh.nfu.item.bauble.BaubleEquippingCondition;
import net.sodiumzh.nfu.util.NFUInfoStatics;

import javax.annotation.Nullable;
import java.util.function.BiPredicate;

public class NFFGirlsBaubleProperties {

    private BaubleEquippingCondition equippingCondition;


    public enum PropertyType {
        MODIFIER(0), TAG(1), ENVIRONMENT_RESISTANCE(2);

        private final int priority;

        PropertyType(int priority) {
            this.priority = priority;
        }

        public int getPriority() {
            return priority;
        }
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
    public interface PropertyApplyingCondition extends BiPredicate<Mob, ItemStack> {}

    /**
     * Any property entry.
     */
    public static interface IProperty extends DescriptionProvider {
        PropertyType getType();
        PropertyApplyingCondition getApplyingCondition();
    }

    /**
     * A property that provides an attribute modifier.
     */
    public record ModifierProperty(Attribute attribute,
                                 double amount,
                                 AttributeModifier.Operation operation,
                                 String groupName,
                                 PropertyApplyingCondition applyingCondition
                                 ) implements IProperty{

        @Override
        public PropertyType getType() {
            return PropertyType.MODIFIER;
        }

        @Override
        public Component getDescription() {
            // TODO Copy legacy format
        }

        @Override
        public PropertyApplyingCondition getApplyingCondition() {
            return applyingCondition;
        }
    }

    /**
     * A tag described by a string. Implemented elsewhere.
     */
    public record TagProperty(String key, Component description, PropertyApplyingCondition applyingCondition) implements IProperty {

        public static TagProperty withPlainDesc(String key, String translationKey, @Nullable PropertyApplyingCondition applyingCondition) {
            return new TagProperty(key, NFUInfoStatics.createTranslatable(translationKey).withStyle(ChatFormatting.WHITE), applyingCondition);
        }

        @Override
        public PropertyType getType() {
            return PropertyType.TAG;
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

}
