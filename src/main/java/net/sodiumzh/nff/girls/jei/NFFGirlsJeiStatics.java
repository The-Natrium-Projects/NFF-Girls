package net.sodiumzh.nff.girls.jei;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import net.sodiumzh.nff.girls.jei.item.MobApplicableItemTableJeiRecord;
import net.sodiumzh.nff.girls.jei.trade.NFFGirlsTradeJeiRecord;
import net.sodiumzh.nff.girls.network.NFFGirlsChannels;
import net.sodiumzh.nff.girls.registry.NFFGirlsHealingItemMappings;
import net.sodiumzh.nff.girls.registry.NFFGirlsTrades;
import net.sodiumzh.nff.services.entity.taming.NFFTamingMapping;
import net.sodiumzh.nff.services.entity.taming.TamingProcessItemGivingProgress;
import net.sodiumzh.nfu.container.Tuple2;
import net.sodiumzh.nfu.entity.vanillatrade.VanillaTradeListing;
import net.sodiumzh.nfu.entity.vanillatrade.VanillaTradeRegistry;
import net.sodiumzh.nfu.object.Validatable;
import net.sodiumzh.nfu.util.NFUMiscStatics;
import net.sodiumzh.nfu.util.NFUNetworkStatics;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class NFFGirlsJeiStatics {

    // Static Variables //

    public static final ThreadLocal<Validatable<Map<EntityType<? extends Mob>, MobApplicableItemTableJeiRecord>>>
        ALL_HEALING_ITEM_TABLES = ThreadLocal.withInitial(Validatable::new);
    public static final ThreadLocal<Validatable<Map<EntityType<? extends Mob>, MobApplicableItemTableJeiRecord>>>
        ALL_FRIENDING_ITEM_TABLES = ThreadLocal.withInitial(Validatable::new);
    /**
     * All NFF Girls trade entries. Gathered on registry creating values (in {@link NFFGirlsJeiEventListeners#gatherJeiData})
     * on server, and synched to client in {@link NFFGirlsJeiEventListeners#syncJeiData}.
     */
    public static final ThreadLocal<Validatable<Map<EntityType<?>, Multimap<Integer, NFFGirlsTradeJeiRecord>>>>
        ALL_TRADE_ENTRIES = ThreadLocal.withInitial(Validatable::new);

    // Static methods //

    // Server side
    public static Map<EntityType<? extends Mob>, MobApplicableItemTableJeiRecord> gatherHealing() {
        return NFFGirlsHealingItemMappings.getTable().entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getKey, entry -> MobApplicableItemTableJeiRecord.fromTable(entry.getValue())));
    }

    // Server side
    public static Map<EntityType<? extends Mob>, MobApplicableItemTableJeiRecord> gatherFriending() {
        return NFFTamingMapping.getAllTamableTypes().stream()
            .map(type -> Tuple2.of(type, NFUMiscStatics.cast(NFFTamingMapping.getProcess((EntityType<? extends Mob>) type), TamingProcessItemGivingProgress.class)))
            .filter(entry -> entry.getB() != null)
            .map(entry -> Tuple2.of(entry.getA(), entry.getB().getItemGivingTableOverride().get()))
            .filter(entry -> entry.getB() != null)
            .collect(Collectors.toMap(entry -> (EntityType<? extends Mob>)(entry.getA()), entry -> MobApplicableItemTableJeiRecord.fromTable(entry.getB())));
    }

    // Server side
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
        NFFGirlsJeiStatics.ALL_HEALING_ITEM_TABLES.get().setAndValidate(packet.healingItems);
        NFFGirlsJeiStatics.ALL_FRIENDING_ITEM_TABLES.get().setAndValidate(packet.friendingItems);
        NFFGirlsJeiStatics.ALL_TRADE_ENTRIES.get().setAndValidate(packet.tradeEntries);
    }
}
