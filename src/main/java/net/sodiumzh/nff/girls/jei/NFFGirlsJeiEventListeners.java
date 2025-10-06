package net.sodiumzh.nff.girls.jei;

import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.jei.trade.ClientboundNFFGirlsJeiDataSyncPacket;
import net.sodiumzh.nff.girls.network.NFFGirlsChannels;
import net.sodiumzh.nfu.registry.NFURegistries;
import net.sodiumzh.nfu.registry.NFURegistryGenerateValuesEvent;
import net.sodiumzh.nfu.util.NFUNetworkStatics;

import java.util.Optional;

@Mod.EventBusSubscriber(modid = NFFGirls.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class NFFGirlsJeiEventListeners {

    @SubscribeEvent
    public static void gatherJeiData(NFURegistryGenerateValuesEvent.ServerAfter event) {
        if (!ModList.get().isLoaded("jei")) return;
        // Collect entries on server side. Entries will be synched to client upon request (via sending
        // ServerboundNFFGirlsJeiDataSyncRequestPacket)
        if (event.registry.equals(NFURegistries.VANILLA_TRADE_REGISTRIES))
        {
            NFFGirlsJeiStatics.ALL_TRADE_ENTRIES.get().setAndValidate(NFFGirlsJeiStatics.gatherTradeEntries());
        }
    }

    @SubscribeEvent
    public static void syncJeiData(OnDatapackSyncEvent event) {
        if (!ModList.get().isLoaded("jei")) return;
        // An issue report said the entries might not be initialized here. Make a check before sending.
        if (!NFFGirlsJeiStatics.ALL_TRADE_ENTRIES.get().isValidated())
            NFFGirlsJeiStatics.ALL_TRADE_ENTRIES.get().setAndValidate(NFFGirlsJeiStatics.gatherTradeEntries());

        var packet = new ClientboundNFFGirlsJeiDataSyncPacket();
        Optional.ofNullable(event.getPlayer()).ifPresentOrElse(
            p ->
                NFUNetworkStatics.sendToPlayer(NFFGirlsChannels.SYNC_CHANNEL,
                    packet, event.getPlayer()),
            () ->
                event.getPlayerList().broadcastAll(packet));
    }

}
