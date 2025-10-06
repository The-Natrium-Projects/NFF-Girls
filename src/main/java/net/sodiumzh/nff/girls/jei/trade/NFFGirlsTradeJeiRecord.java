package net.sodiumzh.nff.girls.jei.trade;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.sodiumzh.nfu.container.Tuple3;
import net.sodiumzh.nfu.entity.vanillatrade.VanillaTradeListing;
import net.sodiumzh.nfu.entity.vanillatrade.VanillaTradeListingEnchanted;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents an entry merged from a {@link VanillaTradeListing}.
 */
public class NFFGirlsTradeJeiRecord {

    public List<ItemStack> a = new ArrayList<>();
    public int[] aAmountRange = {1, 1};
    public boolean hasB = false;
    public List<ItemStack> b = new ArrayList<>();
    public int[] bAmountRange = {1, 1};
    public List<ItemStack> r = new ArrayList<>();
    public int[] rAmountRange = {1, 1};

    private NFFGirlsTradeJeiRecord() {
    }

    public void writeBuf(FriendlyByteBuf buf) {
        buf.writeCollection(a, (bytebuf, i) -> bytebuf.writeItemStack(i, true));
        buf.writeInt(aAmountRange[0]);
        buf.writeInt(aAmountRange[1]);
        buf.writeBoolean(hasB);
        buf.writeCollection(b, (bytebuf, i) -> bytebuf.writeItemStack(i, true));
        buf.writeInt(bAmountRange[0]);
        buf.writeInt(bAmountRange[1]);
        buf.writeCollection(r, (bytebuf, i) -> bytebuf.writeItemStack(i, true));
        buf.writeInt(rAmountRange[0]);
        buf.writeInt(rAmountRange[1]);
    }

    public static NFFGirlsTradeJeiRecord readBuf(FriendlyByteBuf buf) {
        NFFGirlsTradeJeiRecord res = new NFFGirlsTradeJeiRecord();
        res.a = buf.readList(FriendlyByteBuf::readItem);
        res.aAmountRange = new int[]{buf.readInt(), buf.readInt()};
        res.hasB = buf.readBoolean();
        res.b = buf.readList(FriendlyByteBuf::readItem);
        res.bAmountRange = new int[]{buf.readInt(), buf.readInt()};
        res.r = buf.readList(FriendlyByteBuf::readItem);
        res.rAmountRange = new int[]{buf.readInt(), buf.readInt()};
        return res;
    }

    public static Optional<NFFGirlsTradeJeiRecord> fromTradeListing(VanillaTradeListing from) {
        try {
            if (!from.isValid()) return Optional.empty();
            NFFGirlsTradeJeiRecord res = new NFFGirlsTradeJeiRecord();
            res.a.addAll(from.getBaseCostA());
            res.aAmountRange = new int[]{from.getACount().getMinValue(), from.getACount().getMaxValue()};
            if (from.shouldHaveB()) {
                res.hasB = true;
                res.b.addAll(from.getCostB());
                res.bAmountRange = new int[]{from.getBCount().getMinValue(), from.getBCount().getMaxValue()};
            }
            res.r.addAll(from.getResult());
            res.rAmountRange = new int[]{from.getResultCount().getMinValue(), from.getResultCount().getMaxValue()};
            if (from instanceof VanillaTradeListingEnchanted enchanted) {
                var encs = enchanted.getAllPossibleEnchantments();
                var items = List.copyOf(res.r);
                res.r.clear();
                items.forEach(itemStack -> {
                    res.r.addAll(encs.stream().map(tp -> {
                        ItemStack itemCopy = itemStack.copy();
                        itemCopy.enchant(tp.getA(), tp.getB());
                        return itemCopy;
                    }).toList());
                });
            }
            if (res.a.isEmpty())
                res.b.add(ItemStack.EMPTY);
            if (res.b.isEmpty())
                res.b.add(ItemStack.EMPTY);
            if (res.r.isEmpty())
                res.r.add(ItemStack.EMPTY);
            return Optional.of(res);
        }
        catch (RuntimeException e) {
            return Optional.empty();
        }
    }

    public List<ItemStack> getA() {
        return a;
    }

    public int[] getAAmountRange() {
        return aAmountRange;
    }

    public boolean hasB() {
        return hasB;
    }

    public List<ItemStack> getB() {
        return b;
    }

    public int[] getBAmountRange() {
        return bAmountRange;
    }

    public List<ItemStack> getR() {
        return r;
    }

    public int[] getRAmountRange() {
        return rAmountRange;
    }

    /**
     * Convert entry info to a JEI-displayable entry (represented by a Tuple3 of input A, input B and result).
     * As JEI displays an item of specific count instead of a count range, the item count is the average of the range.
     */
    private static Tuple3<ItemStack, ItemStack, ItemStack> toSimpleEntry(ItemStack a, int[] aAmount, ItemStack b, int[] bAmount, ItemStack r, int[] rAmount) {
        ItemStack acopy = a.copy();
        ItemStack bcopy = b.copy();
        ItemStack rcopy = r.copy();
        a.setCount((aAmount[0]+aAmount[1])/2);
        if (!b.isEmpty())
            b.setCount((bAmount[0]+bAmount[1])/2);
        r.setCount((rAmount[0]+rAmount[1])/2);
        return Tuple3.of(acopy, bcopy, rcopy);
    }

    /**
     * Convert to a list of JEI-displayable entries (represented by a Tuple3 of input A, input B and result).
     * As JEI displays an item of specific count instead of a count range, the item count is the average of the range.
     */
    public List<Tuple3<ItemStack, ItemStack, ItemStack>> toJeiDisplayEntries() {
        if (a.isEmpty() || b.isEmpty() || r.isEmpty())
            return List.of();
        // Simple
        else if (a.size() == 1 && b.size() == 1 && r.size() == 1) {
            return List.of(toSimpleEntry(a.get(0), aAmountRange, b.get(0), bAmountRange, r.get(0), rAmountRange));
        }
        // Multi A input
        else if (a.size() > 1 && b.size() == 1 && r.size() == 1) {
            return a.stream().map(i -> toSimpleEntry(i, aAmountRange, b.get(0), bAmountRange, r.get(0), rAmountRange))
                .toList();
        }
        // Multi B input
        else if (a.size() == 1 && b.size() > 1 && r.size() == 1) {
            return b.stream().map(i -> toSimpleEntry(a.get(0), aAmountRange, i, bAmountRange, r.get(0), rAmountRange))
                .toList();
        }
        // Else (including mapping A or B to result)
        else {
            List<Tuple3<ItemStack, ItemStack, ItemStack>> res = new ArrayList<>();
            for (int i = 0; i < Math.max(Math.max(a.size(), b.size()), r.size()); ++i) {
                res.add(toSimpleEntry(a.get(i % a.size()), aAmountRange, b.get(i % b.size()), bAmountRange,
                    r.get(i % r.size()), rAmountRange));
            }
            return res;
        }
    }
}
