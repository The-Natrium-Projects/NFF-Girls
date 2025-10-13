package net.sodiumzh.nff.girls.jei.item;

import com.google.common.collect.Multimap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import net.sodiumzh.nff.girls.jei.trade.NFFGirlsTradeJeiRecord;
import net.sodiumzh.nff.girls.registry.NFFGirlsHealingItems;
import net.sodiumzh.nfu.container.Tuple2;
import net.sodiumzh.nfu.entity.MobApplicableItemTable;
import net.sodiumzh.nfu.math.RangedRandomDouble;
import net.sodiumzh.nfu.object.Validatable;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.DoubleStream;

/**
 * Represents a MobApplicableItemTable.
 */
public class MobApplicableItemTableJeiRecord {

    private final List<EntryRecord> entries = new ArrayList<>();

    public static final Validatable<Multimap<Integer, NFFGirlsTradeJeiRecord>> ALL_ENTRIES =
        new Validatable<>(null);

    private MobApplicableItemTableJeiRecord(List<EntryRecord> entries) {
        this.entries.addAll(entries);
    }

    public static MobApplicableItemTableJeiRecord fromTable(MobApplicableItemTable source) {
        // Tolerate null
        if (source == null) return new MobApplicableItemTableJeiRecord(List.of());
        List<EntryRecord> entries = source.getEntriesView().entrySet().stream().map(entry ->
            MobApplicableItemTableJeiRecord.EntryRecord.fromEntry(entry.getKey(), entry.getValue()).orElse(null))
            .filter(Objects::nonNull).toList();
        return new MobApplicableItemTableJeiRecord(entries);
    }

    public List<EntryRecord> getEntries() {
        return entries;
    }

    public void writeBuf(FriendlyByteBuf buf) {
        buf.writeCollection(this.entries, (buffer, entry) -> entry.writeBuf(buffer));
    }

    public static MobApplicableItemTableJeiRecord readBuf(FriendlyByteBuf buf) {
        return new MobApplicableItemTableJeiRecord(buf.readList(EntryRecord::readBuf));
    }

    /**
     * Represents an element of a MAIT.
     */
    public static class EntryRecord {
        private final List<Item> applicableItems = new ArrayList<>();
        private double[] amountDescriptor = {0d};   // Length 0 = using function; 1 = single value; 2 = range; 3 = selector
        @Nullable private ResourceLocation amountProviderFunctionKey = null;
        private int cooldown = NFFGirlsHealingItems.DEFAULT_COOLDOWN;
        private boolean noConsume = false;
        private EntryRecord() {
        }

        public static Optional<EntryRecord> fromEntry(MobApplicableItemTable.ItemStackCriteria input, MobApplicableItemTable.OutcomeProvider outcomeProvider) {
            EntryRecord res = new EntryRecord();
            res.applicableItems.addAll(input.getAllUsableItems());
            // Parse amount provider
            if (outcomeProvider.getAmountProvider().getAsSingleNumber().isPresent()) {
                res.amountDescriptor = new double[]{outcomeProvider.getAmountProvider().getAsSingleNumber().orElseThrow()};
            }
            else if (outcomeProvider.getAmountProvider().getAsRange().isPresent()) {
                RangedRandomDouble range = outcomeProvider.getAmountProvider().getAsRange().orElseThrow();
                res.amountDescriptor = new double[]{range.getMinValue(), range.getMaxValue()};
            }
            else if (outcomeProvider.getAmountProvider().getAsSelection().isPresent()) {
                var probabilityMap = outcomeProvider.getAmountProvider().getAsSelection().orElseThrow().getProbabilityMap();
                double min = probabilityMap.entries().stream().map(Tuple2::getA).min(Comparator.comparingDouble(Double::doubleValue)).orElse(0d);
                double max = probabilityMap.entries().stream().map(Tuple2::getA).max(Comparator.comparingDouble(Double::doubleValue)).orElse(0d);
                res.amountDescriptor = new double[]{min, max, probabilityMap.expectation(Double::doubleValue)};
            }
            else if (outcomeProvider.getAmountProvider().getAsRegisteredFunction().isPresent()) {
                res.amountDescriptor = new double[]{};
                res.amountProviderFunctionKey = outcomeProvider.getAmountProvider().getAsRegisteredFunction()
                    .map(Tuple2::getA).orElseThrow();
            }
            else return Optional.empty();   // Skip unparseable entries
            // Parse cooldown
            if (outcomeProvider.getCooldownProvider().getAsSingleNumber().isPresent()) {
                res.cooldown = outcomeProvider.getCooldownProvider().getAsSingleNumber().orElseThrow();
            }
            else return Optional.empty();   // NFF: Girls doesn't allow variable cooldown
            res.noConsume = outcomeProvider.isNoConsume();
            // Extra action is not parseable
            return Optional.of(res);
        }

        public void writeBuf(FriendlyByteBuf buf) {
            List<ResourceLocation> itemKeys = applicableItems.stream().map(ForgeRegistries.ITEMS::getKey)
                    .filter(Objects::nonNull).toList();
            buf.writeCollection(itemKeys, FriendlyByteBuf::writeResourceLocation);
            buf.writeCollection(DoubleStream.of(amountDescriptor).boxed().toList(), FriendlyByteBuf::writeDouble);
            buf.writeOptional(Optional.ofNullable(amountProviderFunctionKey), FriendlyByteBuf::writeResourceLocation);
            buf.writeInt(cooldown);
            buf.writeBoolean(noConsume);
        }

        public static EntryRecord readBuf(FriendlyByteBuf buf) {
            EntryRecord res = new EntryRecord();
            List<Item> items = buf.readList(FriendlyByteBuf::readResourceLocation).stream()
                .map(ForgeRegistries.ITEMS::getValue).filter(Objects::nonNull).toList();
            res.applicableItems.addAll(items);
            res.amountDescriptor = buf.readList(FriendlyByteBuf::readDouble).stream().mapToDouble(Double::doubleValue)
                .toArray();
            res.amountProviderFunctionKey = buf.readOptional(FriendlyByteBuf::readResourceLocation).orElse(null);
            res.cooldown = buf.readInt();
            res.noConsume = buf.readBoolean();
            return res;
        }

        public List<Item> getApplicableItems() {
            return applicableItems;
        }

        public double[] getAmountDescriptor() {
            return amountDescriptor;
        }

        @Nullable
        public ResourceLocation getAmountProviderFunctionKey() {
            return amountProviderFunctionKey;
        }

        public int getCooldown() {
            return cooldown;
        }

        public boolean isNoConsume() {
            return noConsume;
        }
    }


}
