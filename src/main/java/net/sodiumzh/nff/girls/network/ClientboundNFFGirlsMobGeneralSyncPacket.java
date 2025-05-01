package net.sodiumzh.nff.girls.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.entity.capability.CNFFGirlsFavorabilityHandler;
import net.sodiumzh.nff.girls.entity.capability.CNFFGirlsLevelHandler;
import net.sodiumzh.nff.girls.registry.NFFGirlsCapabilities;
import net.sodiumzh.nfu.util.NFUMiscStatics;

public class ClientboundNFFGirlsMobGeneralSyncPacket implements Packet<ClientGamePacketListener>
{
	public final int entityId;
	public final int tradingPlayerId;	// -1 for null
	public final float favorability;
	public final float maxFavorability;
	public final long xp;
	
	public ClientboundNFFGirlsMobGeneralSyncPacket(INFFGirlsTamed mob)
	{
		this.entityId = mob.asMob().getId();
		this.tradingPlayerId = NFUMiscStatics.getValueFromCapability(mob.asMob(), NFFGirlsCapabilities.CAP_TRADE_HANDLER,
				cap -> (cap.getTradingPlayer() != null ? cap.getTradingPlayer().getId() : -1), -1);
		this.favorability = NFUMiscStatics.getValueFromCapability(mob.asMob(), NFFGirlsCapabilities.CAP_FAVORABILITY_HANDLER,
				CNFFGirlsFavorabilityHandler::getFavorability, 50f);
		this.maxFavorability = NFUMiscStatics.getValueFromCapability(mob.asMob(), NFFGirlsCapabilities.CAP_FAVORABILITY_HANDLER,
				CNFFGirlsFavorabilityHandler::getMaxFavorability, 100f);
		this.xp = NFUMiscStatics.getValueFromCapability(mob.asMob(), NFFGirlsCapabilities.CAP_LEVEL_HANDLER,
				CNFFGirlsLevelHandler::getExp, 0l);
	}
	
	public ClientboundNFFGirlsMobGeneralSyncPacket(FriendlyByteBuf buf)
	{
		this.entityId = buf.readInt();
		this.tradingPlayerId = buf.readInt();
		this.favorability = buf.readFloat();
		this.maxFavorability = buf.readFloat();
		this.xp = buf.readLong();
	}
	
	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeInt(entityId);
		buf.writeInt(tradingPlayerId);
		buf.writeFloat(favorability);
		buf.writeFloat(maxFavorability);
		buf.writeLong(xp);
	}

	@Override
	public void handle(ClientGamePacketListener handler) {
		NFFGirlsClientGamePacketHandler.handleBMGeneralSync(this, handler);
	}

}
