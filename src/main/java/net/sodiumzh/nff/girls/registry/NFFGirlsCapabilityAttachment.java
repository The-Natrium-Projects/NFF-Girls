package net.sodiumzh.nff.girls.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.entity.capability.CNFFGirlsFavorabilityHandler;
import net.sodiumzh.nff.girls.entity.capability.CNFFGirlsLevelHandler;
import net.sodiumzh.nff.girls.entity.capability.CUndeadAffinityHandlerProvider;
import net.sodiumzh.nff.girls.entity.vanillatrade.CNFFGirlsTradeHandler;
import net.sodiumzh.nff.services.entity.capability.CAttributeMonitor;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nff.services.item.capability.CItemStackMonitor;

@Mod.EventBusSubscriber(modid = NFFGirls.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class NFFGirlsCapabilityAttachment {

	public static final String KEY_UNDEAD_AFFINITY_HANDLER  = "undead_affinity_handler";
	public static final String KEY_FAVORABILITY = "favorability";
	public static final String KEY_XP_LEVEL = "xp_level";
	public static final String KEY_TRADE = "trade";
	public static final String KEY_DEFAULT_ANGER_HANDLER = "defaultAngerHandler";
	
	public static final String KEY_UNDEAD_AFFINITY_HANDLER_LEGACY  = "cap_undead";
	public static final String KEY_FAVORABILITY_LEGACY = "cap_favorability";
	public static final String KEY_XP_LEVEL_LEGACY = "cap_level";
	public static final String KEY_TRADE_LEGACY = "cap_trade";

	// Attach capabilities
	@SubscribeEvent
	public static void attachLivingEntityCapabilities(AttachCapabilitiesEvent<Entity> event)
	{
		if(event.getObject() instanceof LivingEntity living)
		{
			MobType mobtype = MobType.UNDEFINED;
			// Some mods may not be able to load mob type at this phase since many parameters are not initialized
			// e.g. Target Dummy. At this time ignore it.
			try {mobtype = living.getMobType();} catch (NullPointerException e) {}
			
			if ((mobtype == MobType.UNDEAD || living.getType().is(NFFGirlsTags.AFFECTED_BY_UNDEAD_AFFINITY))
					&& !(living instanceof INFFTamed)  // Befriended mobs aren't affected by Death Affinity
					&& !living.getType().is(NFFGirlsTags.IGNORES_UNDEAD_AFFINITY))	
			{
				event.addCapability(new ResourceLocation(NFFGirls.MOD_ID, KEY_UNDEAD_AFFINITY_HANDLER), new CUndeadAffinityHandlerProvider());
			}
			INFFGirlsTamed bm;
			if ((bm = INFFGirlsTamed.getBM(living)) != null)
			{
				event.addCapability(new ResourceLocation(NFFGirls.MOD_ID, KEY_FAVORABILITY), new CNFFGirlsFavorabilityHandler.Prvd(bm.asMob()));
				event.addCapability(new ResourceLocation(NFFGirls.MOD_ID, KEY_XP_LEVEL), new CNFFGirlsLevelHandler.Prvd(bm.asMob()));
				event.addCapability(new ResourceLocation(NFFGirls.MOD_ID, KEY_TRADE),
					new CNFFGirlsTradeHandler.Prvd(bm, NFFGirlsCapabilities.CAP_TRADE_HANDLER));
			}
		}
	}
	
	@SubscribeEvent
	public static void setupAttributeMonitor(CAttributeMonitor.SetupEvent event)
	{
		if (INFFGirlsTamed.isBM(event.living))
		{
			event.monitor.listen(Attributes.MAX_HEALTH);
		}
	}
	
	@SubscribeEvent
	public static void setupItemStackMonitor(CItemStackMonitor.SetupEvent event)
	{
		if (event.living instanceof INFFTamed b)
		{
			event.monitor.listen("main_hand", () -> event.living.getMainHandItem());
		}
	}
}

