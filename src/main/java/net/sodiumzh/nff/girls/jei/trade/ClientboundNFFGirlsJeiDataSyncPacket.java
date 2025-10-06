package net.sodiumzh.nff.girls.jei.trade;

import com.google.common.collect.Multimap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;
import net.sodiumzh.nff.girls.jei.NFFGirlsJeiStatics;
import net.sodiumzh.nfu.util.NFUNetworkStatics;

import java.util.Map;

public class ClientboundNFFGirlsJeiDataSyncPacket implements Packet<ClientGamePacketListener> {

    public final Map<EntityType<?>, Multimap<Integer, NFFGirlsTradeJeiRecord>> tradeEntries;

    public ClientboundNFFGirlsJeiDataSyncPacket() {
        this.tradeEntries = NFFGirlsJeiStatics.ALL_TRADE_ENTRIES.get().get();
    }

    @Override
    public void write(FriendlyByteBuf pBuffer) {
        pBuffer.writeMap(tradeEntries,
            (buf, type) ->
                buf.writeUtf(ForgeRegistries.ENTITY_TYPES.getKey(type).toString()),
            (buf, multimap) ->
                NFUNetworkStatics.writeMultimap(buf, multimap, FriendlyByteBuf::writeInt,
                    (buf1, entry) -> entry.writeBuf(buf)));
    }

    public ClientboundNFFGirlsJeiDataSyncPacket(FriendlyByteBuf pBuffer) {
        this.tradeEntries = pBuffer.readMap(
            buf -> ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(buf.readUtf())),
            buf -> NFUNetworkStatics.readMultimap(buf, FriendlyByteBuf::readInt, NFFGirlsTradeJeiRecord::readBuf));
    }

    @Override
    public void handle(ClientGamePacketListener pHandler) {
        NFFGirlsJeiStatics.handleJeiDataSync(this, pHandler);
    }
}
