package net.sodiumzh.nff.girls.entity.vanillatrade;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.nautils.statics.NaUtilsInfoStatics;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.eventlisteners.NFFGirlsHooks;
import net.sodiumzh.nff.girls.registry.NFFGirlsItems;

@Mod.EventBusSubscriber(modid = NFFGirls.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class NFFGirlsTradeClientEventHandlers
{
	@SubscribeEvent
	public static void onMerchantOfferUnavailableInfo(NFFGirlsHooks.MerchantOfferUnavaliableInfoEvent event)
	{
		var tradingMob = event.searchOngoingDwmgTrader(16d);
		if (tradingMob != null)
		{
			if (tradingMob.isValidOffers() && tradingMob.getMeta().size() > event.activeOfferIndex)
			{
				if (event.activeOffer.getResult().is(NFFGirlsItems.TRADE_INTRODUCTION_LETTER.get()))
				{
					event.setInfo(NaUtilsInfoStatics.createTranslatable("info.nffgirls.introduction_unavailable", tradingMob.getPoints(), tradingMob.getPointsPerIntroduction()));
				}
				else if (tradingMob.getMeta().get(event.activeOfferIndex).requiredMerchantLevel > tradingMob.getMerchantLevel())
				{
					int lvlRequired = CNFFGirlsTradeHandler.LEVEL_REQUIREMENTS[tradingMob.getMeta().get(event.activeOfferIndex).requiredMerchantLevel - 1];
					event.setInfo(NaUtilsInfoStatics.createTranslatable("info.nffgirls.trade_level_not_satisfied", lvlRequired));
				}
				else event.setInfo(NaUtilsInfoStatics.createTranslatable("info.nffgirls.trade_out_of_stock"));
			}
			else event.noInfo();
		}
	}
}
