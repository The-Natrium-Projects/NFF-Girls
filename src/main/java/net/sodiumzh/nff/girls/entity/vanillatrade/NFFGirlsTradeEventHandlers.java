package net.sodiumzh.nff.girls.entity.vanillatrade;

import net.minecraft.world.InteractionHand;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.nautils.mixin.events.entity.MobInteractEvent;
import net.sodiumzh.nautils.statics.NaUtilsInfoStatics;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.registry.NFFGirlsCapabilities;
import net.sodiumzh.nff.girls.registry.NFFGirlsItems;

@Mod.EventBusSubscriber(modid = NFFGirls.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class NFFGirlsTradeEventHandlers
{
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onMobInteract(EntityInteract event)
	{
		if (INFFGirlsTamed.isBM(event.getTarget())
				&& !event.getTarget().getLevel().isClientSide()
				&& INFFGirlsTamed.getBM(event.getTarget()).getOwnerUUID().equals(event.getEntity().getUUID())
				&& (event.getPlayer().getItemInHand(InteractionHand.MAIN_HAND).isEmpty() || event.getPlayer().getItemInHand(InteractionHand.MAIN_HAND).is(NFFGirlsItems.EVIL_GEM.get()))
				&& INFFGirlsTamed.getBM(event.getTarget()).asMob().getTarget() == null
				&& !event.getEntity().isShiftKeyDown()
				)
		{
			INFFGirlTamed.getBM(event.getTarget()).asMob().getCapability(NFFGirlsCapabilities.CAP_TRADE_HANDLER).ifPresent(cap -> {
				if (cap.isValidTrader()) {
					cap.openTradingScreen(event.getEntity(), NaUtilsInfoStatics.createTranslatable("info.nffgirls.open_trade"), 1);
					event.setCanceled(true);
				}
			});
		}
	}

	public static void beforeMobInteract(MobInteractEvent event)
	{

	}

	@SubscribeEvent
	public static void onDie(LivingDeathEvent event)
	{
		event.getEntity().getCapability(NFFGirlsCapabilities.CAP_TRADE_HANDLER).ifPresent(cap -> {
			cap.setTradingPlayer(null);
		});
	}
	
	@SuppressWarnings("resource")
	@SubscribeEvent
	public static void onTick(LivingUpdateEvent event)
	{
		if (!event.getEntity().getLevel().isClientSide)
			event.getEntity().getCapability(NFFGirlsCapabilities.CAP_TRADE_HANDLER).ifPresent(CNFFGirlsTradeHandler::serverTick);
	}
}
