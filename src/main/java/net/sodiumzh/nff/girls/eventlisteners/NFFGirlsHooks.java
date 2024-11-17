package net.sodiumzh.nff.girls.eventlisteners;

import java.util.List;

import javax.annotation.Nullable;

import com.github.mechalopa.hmag.world.entity.JackFrostEntity;
import com.github.mechalopa.hmag.world.entity.MeltyMonsterEntity;

import net.minecraft.client.gui.screens.inventory.MerchantScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.npc.ClientSideMerchant;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import net.sodiumzh.nautils.events.NaUtilsLivingEvent;
import net.sodiumzh.nautils.statics.NaUtilsMiscStatics;
import net.sodiumzh.nautils.statics.NaUtilsReflectionStatics;
import net.sodiumzh.nff.girls.entity.vanillatrade.CNFFGirlsTradeHandler;
import net.sodiumzh.nff.girls.mixins.NFFGirlsJackFrostEntityMixin;
import net.sodiumzh.nff.girls.mixins.NFFGirlsMeltyMonsterEntityMixin;
import net.sodiumzh.nff.girls.registry.NFFGirlsCapabilities;

public class NFFGirlsHooks
{

	/** Fired on Peach Wood Sword damaging by percentage */
	public static class PeachWoodSwordForceHurtEvent extends Event
	{
		public final Player player;
		public final Mob target;
		public float newDamage;
		public final float oldDamage;
		public PeachWoodSwordForceHurtEvent(Player player, Mob mob, float oldDamage, float expectedNewDamage)
		{
			this.player = player;
			this.target = mob;
			this.oldDamage = oldDamage;
			this.newDamage = expectedNewDamage;
		}
	}
	
	/** Fired on Jack o'Frost check melting biome. Implemented via {@link NFFGirlsJackFrostEntityMixin}. 
	 * If cancelled it will not be regarded as a melting biome.
	 */
	@Cancelable
	public static class JackFrostCheckMeltingBiomeEvent extends NaUtilsLivingEvent<JackFrostEntity>
	{
		public JackFrostCheckMeltingBiomeEvent(JackFrostEntity entity) {
			super(entity);
		}
	}
	
	/** Fired on Melty Monster setting fire. Implemented via {@link NFFGirlsMeltyMonsterEntityMixin}.
	 */
	@Cancelable
	public static class MeltyMonsterSetFireEvent extends NaUtilsLivingEvent<MeltyMonsterEntity>
	{
		public final BlockPos blockpos; 
		public final BlockState blockstate;
		
		public MeltyMonsterSetFireEvent(MeltyMonsterEntity entity, BlockPos pos, BlockState blockstate)
		{
			super(entity);
			this.blockpos = pos;
			this.blockstate = blockstate;
		}
	}
	
	/**
	 * Posted on the player's mouse pointing on merchant menu's crossed arrow when the offer is not available.
	 * By default it's "Villagers restock up to two times per day." ("merchant.deprecated").
	 */
	@OnlyIn(Dist.CLIENT)
	public static class MerchantOfferUnavaliableInfoEvent extends Event
	{
		public final Player player;
		/**
		 * Usually this is a {@link ClientSideMerchant}. To search for the trading
		 */
		public final Merchant tradingMerchant;
		public final MerchantScreen screen;
		public final int activeOfferIndex;
		public final MerchantOffer activeOffer;
		private Component info;
		private boolean noInfo = false;
		public MerchantOfferUnavaliableInfoEvent(MerchantScreen screen, Component original)
		{
			this.screen = screen;
			MerchantMenu menu = screen.getMenu();
			this.tradingMerchant = NaUtilsReflectionStatics.forceGet(menu, MerchantMenu.class, "f_40027_").cast() ;	// trader
			this.player = this.tradingMerchant.getTradingPlayer();
			this.activeOfferIndex = NaUtilsReflectionStatics.forceGet(screen, MerchantScreen.class, "f_99117_").cast();	// shopItem
			this.activeOffer = menu.getOffers().get(activeOfferIndex);
			this.info = original;
		}
		
		@Nullable
		public Component getInfo() {
			return info;
		}
		
		public void setInfo(Component info) {
			this.info = info;
		}
		
		public void noInfo()
		{
			this.noInfo = true;
		}
		
		public boolean isNoInfo()
		{
			return this.noInfo;
		}
		
		/**
		 * Search for merchant using {@code CNFFGirlsTradeHandler} that's trading with the player around the mob.
		 * If not found (not existing or not using {@code CNFFGirlsTradeHandler} e.g. vanilla villager), returns null.
		 */
		@Nullable
		public CNFFGirlsTradeHandler searchOngoingDwmgTrader(double range)
		{
			List<Entity> list = this.player.level().getEntities(player, player.getBoundingBox().inflate(range)).stream().filter(entity -> 
				NaUtilsMiscStatics.getValueFromCapability(entity, NFFGirlsCapabilities.CAP_TRADE_HANDLER, CNFFGirlsTradeHandler::getTradingPlayer) == player
			).toList();
			return list.isEmpty() ? null : NaUtilsMiscStatics.getValueFromCapability(list.get(0), NFFGirlsCapabilities.CAP_TRADE_HANDLER, cap -> cap);
		}
	}
	
	
}
