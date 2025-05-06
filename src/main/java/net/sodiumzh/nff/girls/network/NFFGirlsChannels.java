package net.sodiumzh.nff.girls.network;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.simple.SimpleChannel;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.entity.vanillatrade.ClientboundNFFGirlsTradeSyncPacket;
import net.sodiumzh.nfu.util.NFUNetworkStatics;

@Mod.EventBusSubscriber(modid = NFFGirls.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NFFGirlsChannels {
	public static SimpleChannel SYNC_CHANNEL;
    public static final String VERSION = "1.0";
    private static int ID = 0;

    public static int nextID() {
        return ID++;
    }

    @SuppressWarnings("resource")
	public static void registerMessage() {
    	SYNC_CHANNEL = NFUNetworkStatics.newChannel(NFFGirls.MOD_ID, "nffgirls_sync_channel");
    	//NetworkHelper.registerDefaultClientGamePacket(nextID(), SYNC_CHANNEL, CNFFGirlsFavorabilityHandler.ClientboundSyncPacket.class);
    	//NetworkHelper.registerDefaultClientGamePacket(nextID(), SYNC_CHANNEL, CNFFGirlsLevelHandler.ClientboundSyncPacket.class);
    	NFUNetworkStatics.registerDefaultClientGamePacket(nextID(), SYNC_CHANNEL, ClientboundNFFGirlsMobGeneralSyncPacket.class);
    	NFUNetworkStatics.registerDefaultClientGamePacket(nextID(), SYNC_CHANNEL, ClientboundNFFGirlsTradeSyncPacket.class);
    }
    
    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() ->
        {
        	registerMessage();
        });
    }
    
}