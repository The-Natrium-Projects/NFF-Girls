package net.sodiumzh.nff.girls.registry;


import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.entity.capability.CNFFGirlsFavorabilityHandler;
import net.sodiumzh.nff.girls.entity.capability.CNFFGirlsLevelHandler;
import net.sodiumzh.nff.girls.entity.capability.CUndeadAffinityHandler;
import net.sodiumzh.nff.girls.entity.vanillatrade.CNFFGirlsTradeHandler;

@Mod.EventBusSubscriber(modid = NFFGirls.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NFFGirlsCapabilities
{

	public static final Capability<CUndeadAffinityHandler> CAP_UNDEAD_AFFINITY_HANDLER = CapabilityManager.get(new CapabilityToken<>(){});
	public static final Capability<CNFFGirlsFavorabilityHandler> CAP_FAVORABILITY_HANDLER = CapabilityManager.get(new CapabilityToken<>(){});
	public static final Capability<CNFFGirlsLevelHandler> CAP_LEVEL_HANDLER = CapabilityManager.get(new CapabilityToken<>(){});
	public static final Capability<CNFFGirlsTradeHandler> CAP_TRADE_HANDLER = CapabilityManager.get(new CapabilityToken<>(){});
	
	// Register capabilities
	@SubscribeEvent
	public static void register(RegisterCapabilitiesEvent event)
	{
		// Entities
		event.register(CUndeadAffinityHandler.class);
		event.register(CNFFGirlsFavorabilityHandler.class);
		event.register(CNFFGirlsLevelHandler.class);
		event.register(CNFFGirlsTradeHandler.class);
	}
	
	
}
