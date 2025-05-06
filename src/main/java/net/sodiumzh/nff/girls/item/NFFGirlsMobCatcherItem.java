package net.sodiumzh.nff.girls.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.sodiumzh.nff.services.item.MobCatcherItem;
import net.sodiumzh.nff.services.item.NFFMobRespawnerItem;
import net.sodiumzh.nfu.util.NFUInfoStatics;

public class NFFGirlsMobCatcherItem extends MobCatcherItem {

    public NFFGirlsMobCatcherItem(Properties pProperties, NFFMobRespawnerItem respawnerType) {
        super(pProperties, respawnerType);
    }

    @Override
    public InteractionResult interactLivingEntity(Player player, LivingEntity interactionTarget, InteractionHand usedHand) {
        // Prevent usage when inventory is full, to prevent respawner loss
        if (player.getInventory().getFreeSlot() == -1) {
            if (!player.level.isClientSide)
                NFUInfoStatics.printMessageTranslatable(player, "info.nffgirls.item.mob_catching_failed_inventory_full");
            return InteractionResult.sidedSuccess(player.level.isClientSide);
        }
        return super.interactLivingEntity(player, interactionTarget, usedHand);
    }
}
