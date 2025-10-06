package net.sodiumzh.nff.girls.jei;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import net.sodiumzh.nff.girls.jei.item.MobApplicableItemTableJeiRecord;
import net.sodiumzh.nff.girls.jei.trade.ClientboundNFFGirlsJeiDataSyncPacket;
import net.sodiumzh.nff.girls.jei.trade.NFFGirlsTradeJeiRecord;
import net.sodiumzh.nff.girls.jei.trade.ServerboundNFFGirlsJeiDataSyncRequestPacket;
import net.sodiumzh.nff.girls.network.NFFGirlsChannels;
import net.sodiumzh.nff.girls.registry.NFFGirlsTrades;
import net.sodiumzh.nfu.container.Tuple2;
import net.sodiumzh.nfu.entity.vanillatrade.VanillaTradeListing;
import net.sodiumzh.nfu.entity.vanillatrade.VanillaTradeRegistry;
import net.sodiumzh.nfu.object.Validatable;
import net.sodiumzh.nfu.util.NFUNetworkStatics;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class NFFGirlsJeiStatics {

    // Static Variables //

    /**
     * All NFF Girls trade entries. Gathered on registry creating values (in {@link NFFGirlsJeiEventListeners#gatherJeiData})
     * on server, and synched to client in {@link NFFGirlsJeiEventListeners#syncJeiData}.
     */
    public static final ThreadLocal<Validatable<Map<EntityType<?>, Multimap<Integer, NFFGirlsTradeJeiRecord>>>>
        ALL_TRADE_ENTRIES = ThreadLocal.withInitial(Validatable::new);

    // Static methods //

    public static Map<ResourceLocation, MobApplicableItemTableJeiRecord> gatherMAIT() {
        for ()
    }

    public static Map<EntityType<?>, Multimap<Integer, NFFGirlsTradeJeiRecord>> gatherTradeEntries() {
        Set<ResourceLocation> availableKeys = NFFGirlsTrades.TRADE_REGISTRY.get()
            .keySet().stream().map(Tuple2::getA)
            .filter(ForgeRegistries.ENTITY_TYPES::containsKey)
            .collect(Collectors.toSet());
        VanillaTradeRegistry.Collected regView = NFFGirlsTrades.TRADE_REGISTRY.get().collect();

        return availableKeys.stream().collect(Collectors.toMap(
            ForgeRegistries.ENTITY_TYPES::getValue,
            key -> {
                Multimap<Integer, NFFGirlsTradeJeiRecord> multimap = HashMultimap.create();
                regView.getForDefaultProfession(key)
                    .allLevelsAndListings().entries().stream()
                    // Only VanillaTradeListing is readable into record
                    .filter(entry -> entry.getValue().getOriginal() instanceof VanillaTradeListing)
                    // Read into records
                    .map(entry -> Tuple2.of(entry.getKey(), NFFGirlsTradeJeiRecord.fromTradeListing((VanillaTradeListing)(entry.getValue().getOriginal()))))
                    .filter(tp -> tp.getB().isPresent())
                    .forEach(tp -> multimap.put(tp.getA(), tp.getB().orElseThrow()));
                return multimap;
            }));
    }

    public static void requestJeiDataSync(Player player) {
        if(!player.level().isClientSide) return;
        NFUNetworkStatics.sendToServer(player, NFFGirlsChannels.SYNC_CHANNEL, new ServerboundNFFGirlsJeiDataSyncRequestPacket(player.getUUID()));
    }

    public static void handleJeiDataSync(ClientboundNFFGirlsJeiDataSyncPacket packet, ClientGamePacketListener pHandler) {
        if (!ModList.get().isLoaded("jei")) return;
        Minecraft mc = Minecraft.getInstance();
        PacketUtils.ensureRunningOnSameThread(packet, pHandler, mc);
        NFFGirlsJeiStatics.ALL_TRADE_ENTRIES.get().setAndValidate(packet.tradeEntries);
    }
}
