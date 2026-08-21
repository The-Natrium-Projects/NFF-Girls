package net.sodiumzh.nff.girls.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.registry.NFFGirlsCapabilities;
import net.sodiumzh.nfu.util.NFUMiscStatics;

public class ClientboundNFFGirlsMobGeneralSyncPacket implements Packet<ClientGamePacketListener>
{
	public final int entityId;
	public final int tradingPlayerId;	// -1 for null
	
	public ClientboundNFFGirlsMobGeneralSyncPacket(INFFGirlsTamed mob)
	{
		this.entityId = mob.asMob().getId();
		this.tradingPlayerId = NFUMiscStatics.getValueFromCapability(mob.asMob(), NFFGirlsCapabilities.CAP_TRADE_HANDLER,
				cap -> (cap.getTradingPlayer() != null ? cap.getTradingPlayer().getId() : -1), -1);
	}
	
	public ClientboundNFFGirlsMobGeneralSyncPacket(FriendlyByteBuf buf)
	{
		this.entityId = buf.readInt();
		this.tradingPlayerId = buf.readInt();
	}
	
	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeInt(entityId);
		buf.writeInt(tradingPlayerId);
	}

	@Override
	public void handle(ClientGamePacketListener handler) {
		NFFGirlsClientGamePacketHandler.handleBMGeneralSync(this, handler);
	}

}
