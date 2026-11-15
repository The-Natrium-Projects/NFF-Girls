package net.sodiumzh.nff.girls.item.bauble;

import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.network.NFFGirlsChannels;
import net.sodiumzh.nff.girls.registry.NFFGirlsBaubles;
import net.sodiumzh.nfu.item.bauble.NFUBaubleAPI;
import net.sodiumzh.nfu.item.bauble.RegisterBaublesEvent;
import net.sodiumzh.nfu.util.NFUNetworkStatics;

@Mod.EventBusSubscriber(modid = NFFGirls.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class NFFGirlsBaubleEventListeners {

    @SubscribeEvent
    public static void sendTooltips(PlayerEvent.PlayerLoggedInEvent event) {
        NFUNetworkStatics.sendToPlayer(NFFGirlsChannels.SYNC_CHANNEL, new ClientboundNFFGirlsBaubleTooltipSyncPacket(), event.getEntity());
    }

    @SubscribeEvent
    public static void addTooltips(ItemTooltipEvent event) {
        if (INFFGirlsBaubleClientTooltipProvider.TOOLTIPS.get().containsKey(event.getItemStack().getItem())) {
            event.getToolTip().addAll(INFFGirlsBaubleClientTooltipProvider.TOOLTIPS.get().get(event.getItemStack().getItem()));
        }
    }


}
