package net.sodiumzh.nff.girls.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.sodiumzh.nff.girls.entity.INFFGirlTamed;
import net.sodiumzh.nff.girls.entity.capability.CNFFGirlsFavorabilityHandler;
import net.sodiumzh.nff.girls.entity.capability.CNFFGirlsLevelHandler;
import net.sodiumzh.nff.girls.entity.vanillatrade.ClientboundNFFGirlsTradeSyncPacket;
import net.sodiumzh.nff.girls.registry.NFFGirlsCapabilities;

@OnlyIn(Dist.CLIENT)
public class NFFGirlsClientGamePacketHandler
{

	@Deprecated
	public static void handleFavorabilityHandlerSync(CNFFGirlsFavorabilityHandler.ClientboundSyncPacket packet, ClientGamePacketListener listener)
	{
		@SuppressWarnings("resource")
		Minecraft mc = Minecraft.getInstance();
		PacketUtils.ensureRunningOnSameThread(packet, listener, mc);
		Entity entity = mc.level.getEntity(packet.entityId);
		// Needs a null check here as sometimes it may invoke on null??
		if (entity != null)
		{
			entity.getCapability(NFFGirlsCapabilities.CAP_FAVORABILITY_HANDLER).ifPresent((cap) ->
			{
				cap.setFavorability(packet.favorability);
				cap.setMaxFavorability(packet.maxFavorability);
			});	
		}
	}
	
	@Deprecated
	public static void handleLevelHandlerSync(CNFFGirlsLevelHandler.ClientboundSyncPacket packet, ClientGamePacketListener listener) 
	{
		@SuppressWarnings("resource")
		Minecraft mc = Minecraft.getInstance();
		PacketUtils.ensureRunningOnSameThread(packet, listener, mc);
		Entity entity = mc.level.getEntity(packet.entityId);
		// Needs a null check here as sometimes it may invoke on null??
		if (entity != null)
		{
			entity.getCapability(NFFGirlsCapabilities.CAP_LEVEL_HANDLER).ifPresent((cap) ->
			{
				cap.setExp(packet.exp);
			});	
		}
	}
	
	public static void handleBMGeneralSync(ClientboundNFFGirlsMobGeneralSyncPacket packet, ClientGamePacketListener listener)
	{
		Minecraft mc = Minecraft.getInstance();
		PacketUtils.ensureRunningOnSameThread(packet, listener, mc);
		INFFGirlTamed bm = INFFGirlTamed.getBM(mc.level.getEntity(packet.entityId));
		bm.asMob().getCapability(NFFGirlsCapabilities.CAP_TRADE_HANDLER).ifPresent(cap -> 
		{
			cap.setTradingPlayer(packet.tradingPlayerId == -1 ? null : (Player) mc.level.getEntity(packet.tradingPlayerId));
		});
		bm.asMob().getCapability(NFFGirlsCapabilities.CAP_FAVORABILITY_HANDLER).ifPresent(cap ->
		{
			cap.setFavorability(packet.favorability);
			cap.setMaxFavorability(packet.maxFavorability);
		});
		bm.asMob().getCapability(NFFGirlsCapabilities.CAP_LEVEL_HANDLER).ifPresent(cap ->
		{
			cap.setExp(packet.xp);
		});
	}
	
	public static void handleTradeSync(ClientboundNFFGirlsTradeSyncPacket packet, ClientGamePacketListener listener)
	{
		Minecraft mc = Minecraft.getInstance();
		PacketUtils.ensureRunningOnSameThread(packet, listener, mc);
		Entity entity = mc.level.getEntity(packet.id);
		if (entity != null)
		{
			entity.getCapability(NFFGirlsCapabilities.CAP_TRADE_HANDLER).ifPresent(cap -> {
				cap.getOffers().clear();
				cap.getMeta().clear();
				for (int i = 0; i < packet.offers.size(); ++i)
				{
					cap.getOffers().add(packet.offers.get(i));
					cap.getMeta().add(packet.meta.get(i));
					cap.setPoints(packet.points);
				}
			});
		}
	}
}
