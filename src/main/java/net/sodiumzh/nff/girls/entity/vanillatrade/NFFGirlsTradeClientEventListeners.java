package net.sodiumzh.nff.girls.entity.vanillatrade;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.registry.NFFGirlsItems;
import net.sodiumzh.nfu.mixin.event.client.entity.MerchantOfferUnavailableInfoEvent;
import net.sodiumzh.nfu.util.NFUInfoStatics;

@Mod.EventBusSubscriber(modid = NFFGirls.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class NFFGirlsTradeClientEventListeners
{
	@SubscribeEvent
	public static void onMerchantOfferUnavailableInfo(MerchantOfferUnavailableInfoEvent event)
	{
		CNFFGirlsTradeHandler tradingMob = CNFFGirlsTradeHandler.searchOngoingTrader(event.player, 16d);
		if (tradingMob != null)
		{
			if (tradingMob.isValidOffers() && tradingMob.getMeta().size() > event.activeOfferIndex)
			{
				if (event.activeOffer.getResult().is(NFFGirlsItems.TRADE_INTRODUCTION_LETTER.get()))
				{
					event.setInfo(NFUInfoStatics.createTranslatable("info.nffgirls.introduction_unavailable", tradingMob.getPoints(), tradingMob.getPointsPerIntroduction()));
				}
				else if (tradingMob.getMeta().get(event.activeOfferIndex).requiredMerchantLevel > tradingMob.getMerchantLevel())
				{
					int lvlRequired = CNFFGirlsTradeHandler.LEVEL_REQUIREMENTS[tradingMob.getMeta().get(event.activeOfferIndex).requiredMerchantLevel - 1];
					event.setInfo(NFUInfoStatics.createTranslatable("info.nffgirls.trade_level_not_satisfied", lvlRequired));
				}
				else event.setInfo(NFUInfoStatics.createTranslatable("info.nffgirls.trade_out_of_stock"));
			}
			else event.noInfo();
		}
	}
}
