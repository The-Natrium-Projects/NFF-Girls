package net.sodiumzh.nff.girls.jei;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerGamePacketListener;

import java.util.UUID;

public class ServerboundNFFGirlsJeiDataSyncRequestPacket implements Packet<ServerGamePacketListener> {

    public final int playerID;

    public ServerboundNFFGirlsJeiDataSyncRequestPacket(int playerID) {
        this.playerID = playerID;
    }

    public ServerboundNFFGirlsJeiDataSyncRequestPacket(FriendlyByteBuf pBuffer) {
        this.playerID = pBuffer.readInt();
    }

    @Override
    public void write(FriendlyByteBuf pBuffer) {
        pBuffer.writeInt(playerID);
    }

    @Override
    public void handle(ServerGamePacketListener pHandler) {
        NFFGirlsJeiStatics.syncJeiData(this.playerID, pHandler);
    }
}
