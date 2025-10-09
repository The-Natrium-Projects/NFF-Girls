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
import net.sodiumzh.nfu.util.NFUNetworkStatics;

import java.util.Map;

public class ClientboundNFFGirlsJeiDataSyncPacket implements Packet<ClientGamePacketListener> {

    public final Map<EntityType<?>, Multimap<Integer, NFFGirlsTradeJeiRecord>> tradeEntries;
    public final Map<EntityType<? extends Mob>, MobApplicableItemTableJeiRecord> healingItems;
    public final Map<EntityType<? extends Mob>, MobApplicableItemTableJeiRecord> friendingItems;

    public ClientboundNFFGirlsJeiDataSyncPacket() {
        this.tradeEntries = NFFGirlsJeiStatics.ALL_TRADE_ENTRIES.get().get();
        this.healingItems = NFFGirlsJeiStatics.ALL_HEALING_ITEM_TABLES.get().get();
        this.friendingItems = NFFGirlsJeiStatics.ALL_FRIENDING_ITEM_TABLES.get().get();
    }

    @Override
    public void write(FriendlyByteBuf pBuffer) {
        pBuffer.writeMap(healingItems, (buf, type) ->
                buf.writeResourceLocation(ForgeRegistries.ENTITY_TYPES.getKey(type)),
            (buf, table) -> table.writeBuf(buf));
        pBuffer.writeMap(friendingItems, (buf, type) ->
                buf.writeResourceLocation(ForgeRegistries.ENTITY_TYPES.getKey(type)),
            (buf, table) -> table.writeBuf(buf));
        pBuffer.writeMap(tradeEntries,
            (buf, type) ->
                buf.writeResourceLocation(ForgeRegistries.ENTITY_TYPES.getKey(type)),
            (buf, multimap) ->
                NFUNetworkStatics.writeMultimap(buf, multimap, FriendlyByteBuf::writeInt,
                    (buf1, entry) -> entry.writeBuf(buf)));

    }

    public ClientboundNFFGirlsJeiDataSyncPacket(FriendlyByteBuf pBuffer) {
        this.healingItems = pBuffer.readMap(buf ->
                (EntityType<? extends Mob>) ForgeRegistries.ENTITY_TYPES.getValue(buf.readResourceLocation()),
            MobApplicableItemTableJeiRecord::readBuf);
        this.friendingItems = pBuffer.readMap(buf ->
                (EntityType<? extends Mob>) ForgeRegistries.ENTITY_TYPES.getValue(buf.readResourceLocation()),
            MobApplicableItemTableJeiRecord::readBuf);
        this.tradeEntries = pBuffer.readMap(
            buf -> ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(buf.readUtf())),
            buf -> NFUNetworkStatics.readMultimap(buf, FriendlyByteBuf::readInt, NFFGirlsTradeJeiRecord::readBuf));
    }

    @Override
    public void handle(ClientGamePacketListener pHandler) {
        NFFGirlsJeiStatics.handleJeiDataSync(this, pHandler);
    }
}
