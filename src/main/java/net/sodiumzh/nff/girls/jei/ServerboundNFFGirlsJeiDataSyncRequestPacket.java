package net.sodiumzh.nff.girls.jei;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerGamePacketListener;

import java.util.UUID;

public class ServerboundNFFGirlsJeiDataSyncRequestPacket implements Packet<ServerGamePacketListener> {

    public final UUID playerID;

    public ServerboundNFFGirlsJeiDataSyncRequestPacket(UUID playerID) {
        this.playerID = playerID;
    }

    public ServerboundNFFGirlsJeiDataSyncRequestPacket(FriendlyByteBuf pBuffer) {
        this.playerID = pBuffer.readUUID();
    }

    @Override
    public void write(FriendlyByteBuf pBuffer) {
        pBuffer.writeUUID(playerID);
    }



    @Override
    public void handle(ServerGamePacketListener pHandler) {

    }
}
