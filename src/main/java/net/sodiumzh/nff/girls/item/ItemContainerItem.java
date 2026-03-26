package net.sodiumzh.nff.girls.item;

import com.github.mechalopa.hmag.registry.ModItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.sodiumzh.nfu.item.NFUItem;
import net.sodiumzh.nfu.util.NFUItemStatics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * An item that provides other items on usage.
 */
public class ItemContainerItem extends NFUItem {

    private final List<ItemStack> contents = new ArrayList<>();

    public ItemContainerItem(Properties pProperties) {
        super(pProperties);
    }

    public ItemContainerItem addContents(ItemStack... contents) {
        this.contents.addAll(Arrays.asList(contents));
        return this;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand pUsedHand)
    {
        if (!player.level.isClientSide)
        {
            player.getItemInHand(pUsedHand).shrink(1);
            for (ItemStack content: this.contents) {
                NFUItemStatics.giveOrDrop(player, content.copy());
            }
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(pUsedHand), player.level.isClientSide);
    }
}
