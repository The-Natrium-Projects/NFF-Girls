package net.sodiumzh.nff.girls.item.bauble;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.sodiumzh.nfu.item.bauble.NFUBaubleAPI;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientboundNFFGirlsBaubleTooltipSyncPacket implements Packet<ClientGamePacketListener> {

    @ApiStatus.Internal
    Map<ResourceLocation, List<Component>> tooltipMap = new HashMap<>();

    public ClientboundNFFGirlsBaubleTooltipSyncPacket() {
        NFUBaubleAPI.getSingleItemBaubles().values().stream()
            .filter(entry -> entry.bauble() instanceof INFFGirlsBauble)
            .forEach(entry -> {
                INFFGirlsBauble nffBauble = (INFFGirlsBauble)(entry.bauble());
                tooltipMap.put(ForgeRegistries.ITEMS.getKey(entry.item()), nffBauble.getProperties().createTooltips());
            });
    }

    public ClientboundNFFGirlsBaubleTooltipSyncPacket(FriendlyByteBuf buf) {
        Map<ResourceLocation, List<Component>> map = buf.readMap(FriendlyByteBuf::readResourceLocation,
            buffer -> buffer.readList(FriendlyByteBuf::readComponent));
        tooltipMap.putAll(map);
    }

    @Override
    public void write(FriendlyByteBuf pBuffer) {
        pBuffer.writeMap(tooltipMap, FriendlyByteBuf::writeResourceLocation, (buf, list) ->
            buf.writeCollection(list, FriendlyByteBuf::writeComponent));
    }

    @Override
    public void handle(ClientGamePacketListener pHandler) {
        NFFGirlsBaubleStatics.handleTooltipSync(this, pHandler);
    }
}
