package net.sodiumzh.nff.girls.jei.item;

import com.google.common.collect.Multimap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import net.sodiumzh.nff.girls.jei.trade.NFFGirlsTradeJeiRecord;
import net.sodiumzh.nfu.entity.MobApplicableItemTable;
import net.sodiumzh.nfu.object.Validatable;

import java.util.List;
import java.util.Objects;

/**
 * Represents a MobApplicableItemTable (MAIT).
 */
public class MobApplicableItemTableJeiRecord {

    private final List<EntryRecord> entries;

    public static final Validatable<Multimap<Integer, NFFGirlsTradeJeiRecord>> ALL_ENTRIES =
        new Validatable<>(null);

    private MobApplicableItemTableJeiRecord(List<EntryRecord> entries) {
        this.entries = entries;
    }

    public static MobApplicableItemTableJeiRecord fromTable(MobApplicableItemTable source) {
        return new MobApplicableItemTableJeiRecord(source.getEntriesView().entrySet()
            .stream().map(entry -> EntryRecord.fromEntry(entry.getKey(), entry.getValue())).toList());
    }

    public List<EntryRecord> getEntries() {
        return entries;
    }

    /**
     * Represents an element of a MAIT.
     */
    public static class EntryRecord {
        private List<Item> applicableItems;
        private MobApplicableItemTable.OutputGetter outputGetter;

        private EntryRecord() {
            this.applicableItems = null;
            this.outputGetter = null;
        }

        public static EntryRecord fromEntry(MobApplicableItemTable.Input input, MobApplicableItemTable.OutputGetter outputGetter) {
            EntryRecord res = new EntryRecord();
            res.applicableItems = input.getAllItems();
            res.outputGetter = outputGetter;
            return res;
        }

        public List<Item> getApplicableItems() {
            return applicableItems;
        }

        public MobApplicableItemTable.OutputGetter getOutputGetter() {
            return outputGetter;
        }

        public void writeBuf(FriendlyByteBuf buf) {
            List<ResourceLocation> itemKeys = applicableItems.stream().map(ForgeRegistries.ITEMS::getKey)
                    .filter(Objects::nonNull).toList();
            buf.writeCollection(itemKeys, FriendlyByteBuf::writeResourceLocation);

        }


    }


}
