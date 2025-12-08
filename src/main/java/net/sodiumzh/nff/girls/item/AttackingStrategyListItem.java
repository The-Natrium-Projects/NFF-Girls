package net.sodiumzh.nff.girls.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.entity.ai.NFFGirlsAttackingStrategy;
import net.sodiumzh.nfu.info.ComponentBuilder;
import net.sodiumzh.nfu.item.NFUItem;
import net.sodiumzh.nfu.util.NFUInfoStatics;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class AttackingStrategyListItem extends NFUItem {

    public AttackingStrategyListItem(Properties pProperties) {
        super(pProperties);
    }

    @SuppressWarnings("unchecked")
    @Override
    public InteractionResult interactLivingEntity(Player player, LivingEntity target, InteractionHand hand) {
        INFFGirlsTamed tamed = INFFGirlsTamed.get(target).orElse(null);
        if (player.level.isClientSide)
            return InteractionResult.sidedSuccess(player.level.isClientSide);
        // For tamed, merge strategy to mob
        if (tamed != null) {
            UUID ownerUUID = tamed.getOwnerUUID();
            // Other player's mob should not be set
            if (!player.getUUID().equals(ownerUUID))
                return InteractionResult.sidedSuccess(player.level.isClientSide);
            else if (player.isShiftKeyDown() && getStrategy(player.getItemInHand(hand)).isEmpty()) {
                setStrategy(player.getItemInHand(hand), tamed.getAttackingStrategy());
                NFUInfoStatics.printMessageTranslatable(player, "info.nffgirls.attacking_strategy.copied", target.getName().getString());
                return InteractionResult.sidedSuccess(player.level.isClientSide);
            }
            else if (!player.isShiftKeyDown()) {
                NFFGirlsAttackingStrategy strategy = getStrategy(player.getItemInHand(hand));
                tamed.setAttackingStrategy(getStrategy(player.getItemInHand(hand)));
                NFUInfoStatics.printMessageTranslatable(player, "info.nffgirls.attacking_strategy.specified", target.getName().getString());
                return InteractionResult.sidedSuccess(player.level.isClientSide);
            }
        }
        else if (target instanceof Mob targetMob) {
            EntityType<? extends Mob> type = (EntityType<? extends Mob>) (targetMob.getType());
            String typeName = type.getDescription().getString();
            NFFGirlsAttackingStrategy strategy = getStrategy(player.getItemInHand(hand));
            if (player.isShiftKeyDown()) {
                if (strategy.getNotAttackingList().contains(type)) {
                    strategy.getNotAttackingList().remove(type);
                    NFUInfoStatics.printMessageTranslatable(player, "info.nffgirls.attacking_strategy.not_attacking_removed", typeName);
                }
                else {
                    if (strategy.getActiveAttackingList().contains(type))
                        NFUInfoStatics.printMessageTranslatable(player, "info.nffgirls.attacking_strategy.active_attacking_removed", typeName);
                    strategy.getActiveAttackingList().remove(type);
                    strategy.getNotAttackingList().add(type);
                    NFUInfoStatics.printMessageTranslatable(player, "info.nffgirls.attacking_strategy.not_attacking_added", typeName);
                }
            }
            else {
                if (strategy.getActiveAttackingList().contains(type)) {
                    strategy.getActiveAttackingList().remove(type);
                    NFUInfoStatics.printMessageTranslatable(player, "info.nffgirls.attacking_strategy.active_attacking_removed", typeName);
                }
                else {
                    if (strategy.getNotAttackingList().contains(type))
                        NFUInfoStatics.printMessageTranslatable(player, "info.nffgirls.attacking_strategy.not_attacking_removed", typeName);
                    strategy.getNotAttackingList().remove(type);
                    strategy.getActiveAttackingList().add(type);
                    NFUInfoStatics.printMessageTranslatable(player, "info.nffgirls.attacking_strategy.active_attacking_added", typeName);
                }
            }
            setStrategy(player.getItemInHand(hand), strategy);
        }
        return InteractionResult.sidedSuccess(player.level.isClientSide);
    }

    @Nonnull
    public static NFFGirlsAttackingStrategy getStrategy(ItemStack itemStack) {
        if (!(itemStack.getItem() instanceof AttackingStrategyListItem))
            throw new IllegalArgumentException();
        if (!itemStack.hasTag()) return NFFGirlsAttackingStrategy.empty();
        return NFFGirlsAttackingStrategy.fromNBT(itemStack.getTag().getCompound("strategy"));
    }

    public static void setStrategy(ItemStack itemStack, @Nullable NFFGirlsAttackingStrategy strategy) {
        if (!(itemStack.getItem() instanceof AttackingStrategyListItem))
            throw new IllegalArgumentException();
        if (strategy != null) {
            itemStack.getOrCreateTag().put("strategy", strategy.toNBT());
        } else {
            itemStack.getOrCreateTag().put("strategy", NFFGirlsAttackingStrategy.empty().toNBT());
        }
    }

    @Override
    public void beforeAddingHoveringDescriptions(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.beforeAddingHoveringDescriptions(stack, level, list, tooltipFlag);
        list.add(NFUInfoStatics.createTranslatable("tooltip.nffgirls.attacking_strategy.add_active_attacking", c -> c.withStyle(ChatFormatting.GRAY)));
        list.add(NFUInfoStatics.createTranslatable("tooltip.nffgirls.attacking_strategy.add_not_attacking", c -> c.withStyle(ChatFormatting.GRAY)));
        list.add(NFUInfoStatics.createTranslatable("tooltip.nffgirls.attacking_strategy.specify_to", c -> c.withStyle(ChatFormatting.GRAY)));
        list.add(NFUInfoStatics.createTranslatable("tooltip.nffgirls.attacking_strategy.copy_from", c -> c.withStyle(ChatFormatting.GRAY)));
        list.add(NFUInfoStatics.createTranslatable("tooltip.nffgirls.attacking_strategy.crafting", c -> c.withStyle(ChatFormatting.GRAY)));
        NFFGirlsAttackingStrategy strategy = getStrategy(stack);
        if (!strategy.getActiveAttackingList().isEmpty()) {
            List<Component> names = strategy.getActiveAttackingList().stream()
                .map(EntityType::getDescription).toList();
            ComponentBuilder builder = ComponentBuilder.create();
            builder.appendTranslatable("tooltip.nffgirls.attacking_strategy.active_attacking_types");
            for (int i = 0; i < names.size(); ++i) {
                builder.append(names.get(i));
                if (i != names.size() - 1)
                    builder.appendTranslatable("tooltip.nffgirls.attacking_strategy.typename_separator");
            }
            list.add(builder.build());
        }
        if (!strategy.getNotAttackingList().isEmpty()) {
            List<Component> names = strategy.getNotAttackingList().stream()
                .map(EntityType::getDescription).toList();
            ComponentBuilder builder = ComponentBuilder.create();
            builder.appendTranslatable("tooltip.nffgirls.attacking_strategy.not_attacking_types");
            for (int i = 0; i < names.size(); ++i) {
                builder.append(names.get(i));
                if (i != names.size() - 1)
                    builder.appendTranslatable("tooltip.nffgirls.attacking_strategy.typename_separator");
            }
            list.add(builder.build().withStyle());
        }
    }

}
