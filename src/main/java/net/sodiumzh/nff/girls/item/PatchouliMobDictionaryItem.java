package net.sodiumzh.nff.girls.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.sodiumzh.nfu.item.NFUItem;
import vazkii.patchouli.api.PatchouliAPI;

public class PatchouliMobDictionaryItem extends NFUItem {

    private final ResourceLocation bookId;

    public PatchouliMobDictionaryItem(Item.Properties pProperties, ResourceLocation bookId) {
        super(pProperties);
        this.bookId = bookId;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);
        if (playerIn instanceof ServerPlayer serverPlayer) {
            PatchouliAPI.get().openBookGUI(serverPlayer, bookId);
        }
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
    }
}
