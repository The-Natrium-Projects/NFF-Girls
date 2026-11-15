package net.sodiumzh.nff.girls.item.bauble;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nfu.item.bauble.NFUBaubleAPI;
import net.sodiumzh.nfu.item.bauble.RegisterBaublesEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = NFFGirls.MOD_ID)
public class NFFGirlsBaubleSetupEventListeners {

    @SubscribeEvent
    public static void mergeBaubles(RegisterBaublesEvent event) {
        var allBaubles = NFUBaubleAPI.getSingleItemBaubles();
        // Load dedicated baubles from items, before registering NFF-Girls baubles to NFU bauble API
        ForgeRegistries.ITEMS.getEntries().stream()
            .filter(entry -> entry.getValue() instanceof INFFGirlsBauble)
            .filter(entry -> !allBaubles.containsKey(entry.getKey().location()))
            .forEach(entry -> {
                if (entry.getValue() instanceof NFFGirlsDedicatedBaubleItem item) {
                    item.loadProperties();
                    event.register((INFFGirlsBauble)(entry.getValue()));
                }
            });
    }

}
