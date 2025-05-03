package net.sodiumzh.nff.girls.entity.vanillatrade;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.sodiumzh.nff.girls.network.NFFGirlsClientGamePacketHandler;

import java.util.ArrayList;
import java.util.List;

public class ClientboundNFFGirlsTradeSyncPacket implements Packet<ClientGamePacketListener>
{
	public final int id;
	public final MerchantOffers offers;
	public final List<NFFGirlsTradeOfferMetaData> meta;
	public final int points;
	
	public ClientboundNFFGirlsTradeSyncPacket(CNFFGirlsTradeHandler handler)
	{
		this.id = handler.getMob().getId();
		this.offers = handler.getOffers();
		this.points = handler.getPoints();
		this.meta = handler.getMeta();
	}
	
	public ClientboundNFFGirlsTradeSyncPacket(FriendlyByteBuf buf)
	{
		this.id = buf.readInt();
		this.offers = MerchantOffers.createFromStream(buf);
		this.points = buf.readInt();
		this.meta = new ArrayList<>();
		int size = buf.readInt();
		for (int i = 0; i < size; ++i)
		{
			this.meta.add(NFFGirlsTradeOfferMetaData.readBuf(buf));
		}
	}
	
	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeInt(id);
		this.offers.writeToStream(buf);
		buf.writeInt(this.points);
		buf.writeInt(meta.size());
		for (int i = 0; i < meta.size(); ++i)
		{
			meta.get(i).writeToBuf(buf);
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void handle(ClientGamePacketListener pHandler) {
		NFFGirlsClientGamePacketHandler.handleTradeSync(this, pHandler);
	}

}
