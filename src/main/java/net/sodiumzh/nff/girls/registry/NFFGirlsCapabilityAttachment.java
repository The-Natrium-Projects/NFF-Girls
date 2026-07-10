package net.sodiumzh.nff.girls.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.entity.vanillatrade.CNFFGirlsTradeHandler;

@Mod.EventBusSubscriber(modid = NFFGirls.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class NFFGirlsCapabilityAttachment {

	public static final String KEY_UNDEAD_AFFINITY_HANDLER = "undead_affinity_handler";
	public static final String KEY_FAVORABILITY = "favorability";
	public static final String KEY_XP_LEVEL = "xp_level";
	public static final String KEY_TRADE = "trade";
	public static final String KEY_DEFAULT_ANGER_HANDLER = "defaultAngerHandler";
	
	public static final String KEY_UNDEAD_AFFINITY_HANDLER_LEGACY = "cap_undead";
	public static final String KEY_FAVORABILITY_LEGACY = "cap_favorability";
	public static final String KEY_XP_LEVEL_LEGACY = "cap_level";
	public static final String KEY_TRADE_LEGACY = "cap_trade";

	// Attach capabilities
	@SubscribeEvent
	public static void attachLivingEntityCapabilities(AttachCapabilitiesEvent<Entity> event)
	{
		if(event.getObject() instanceof LivingEntity living)
		{
			INFFGirlsTamed bm;
			if ((bm = INFFGirlsTamed.get(living).orElse(null)) != null)
			{
				event.addCapability(new ResourceLocation(NFFGirls.MOD_ID, KEY_TRADE),
					new CNFFGirlsTradeHandler.Prvd(bm, NFFGirlsCapabilities.CAP_TRADE_HANDLER));
			}
		}
	}

}

