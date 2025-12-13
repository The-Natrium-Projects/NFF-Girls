package net.sodiumzh.nff.girls.jei;

import com.google.common.collect.Multimap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.registries.ForgeRegistries;
import net.sodiumzh.nff.girls.jei.item.MobApplicableItemTableJeiRecord;
import net.sodiumzh.nff.girls.jei.trade.NFFGirlsTradeJeiRecord;
import net.sodiumzh.nfu.container.Tuple2;
import net.sodiumzh.nfu.util.NFUNetworkStatics;

import java.util.Map;

public class ClientboundNFFGirlsJeiDataSyncPacket implements Packet<ClientGamePacketListener> {

    public final Map<EntityType<?>, Multimap<Integer, NFFGirlsTradeJeiRecord>> tradeEntries;
    public final Map<EntityType<? extends Mob>, Tuple2<ResourceLocation, MobApplicableItemTableJeiRecord>> healingItems;
    public final Map<EntityType<? extends Mob>, Tuple2<ResourceLocation, MobApplicableItemTableJeiRecord>> friendingItems;

    public ClientboundNFFGirlsJeiDataSyncPacket() {
        this.tradeEntries = NFFGirlsJeiStatics.ALL_TRADE_ENTRIES.get().get();
        this.healingItems = NFFGirlsJeiStatics.ALL_HEALING_ITEM_TABLES.get().get();
        this.friendingItems = NFFGirlsJeiStatics.ALL_FRIENDING_ITEM_TABLES.get().get();
    }

    @Override
    public void write(FriendlyByteBuf pBuffer) {
        pBuffer.writeMap(healingItems, (buf, type) ->
                buf.writeResourceLocation(ForgeRegistries.ENTITIES.getKey(type)),
            (buf, keyAndTable) -> {
                buf.writeResourceLocation(keyAndTable.getA());
                keyAndTable.getB().writeBuf(buf);
            });
        pBuffer.writeMap(friendingItems, (buf, type) ->
                buf.writeResourceLocation(ForgeRegistries.ENTITIES.getKey(type)),
            (buf, keyAndTable) -> {
                buf.writeResourceLocation(keyAndTable.getA());
                keyAndTable.getB().writeBuf(buf);
            });
        pBuffer.writeMap(tradeEntries,
            (buf, type) ->
                buf.writeResourceLocation(ForgeRegistries.ENTITIES.getKey(type)),
            (buf, multimap) ->
                NFUNetworkStatics.writeMultimap(buf, multimap, FriendlyByteBuf::writeInt,
                    (buf1, entry) -> entry.writeBuf(buf)));

    }

    public ClientboundNFFGirlsJeiDataSyncPacket(FriendlyByteBuf pBuffer) {
        this.healingItems = pBuffer.readMap(
            buf -> (EntityType<? extends Mob>) ForgeRegistries.ENTITIES.getValue(buf.readResourceLocation()),
            buf -> Tuple2.of(buf.readResourceLocation(), MobApplicableItemTableJeiRecord.readBuf(buf)));
        this.friendingItems = pBuffer.readMap(
            buf -> (EntityType<? extends Mob>) ForgeRegistries.ENTITIES.getValue(buf.readResourceLocation()),
            buf -> Tuple2.of(buf.readResourceLocation(), MobApplicableItemTableJeiRecord.readBuf(buf)));
        this.tradeEntries = pBuffer.readMap(
            buf -> ForgeRegistries.ENTITIES.getValue(new ResourceLocation(buf.readUtf())),
            buf -> NFUNetworkStatics.readMultimap(buf, FriendlyByteBuf::readInt, NFFGirlsTradeJeiRecord::readBuf));
    }

    @Override
    public void handle(ClientGamePacketListener pHandler) {
        NFFGirlsJeiStatics.handleJeiDataSync(this, pHandler);
    }
}
