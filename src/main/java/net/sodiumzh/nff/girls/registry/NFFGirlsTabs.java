package net.sodiumzh.nff.girls.registry;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class NFFGirlsTabs {

    public static final CreativeModeTab MAIN_TAB =
        tab("nffgirls_tab", () -> NFFGirlsItems.TAB_ICON.get().getDefaultInstance());
    public static final CreativeModeTab BAUBLE_TAB =
        tab("nffgirls_bauble_tab", () -> NFFGirlsBaubles.HEALING_JADE.get().getDefaultInstance());


    private static CreativeModeTab tab(String key, Supplier<ItemStack> icon) {
        return new CreativeModeTab(CreativeModeTab.TABS.length, key) {
            @Override
            public ItemStack makeIcon() {
                return icon.get();
            }
        };
    }
}
