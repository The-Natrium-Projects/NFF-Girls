package net.sodiumzh.nff.girls.item.bauble;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nfu.item.bauble.RegisterBaublesEvent;

/**
 * Bauble registration listeners. {@link RegisterBaublesEvent} is a MOD-bus event posted once in common setup
 * on both sides, so the dedicated baubles are registered (and their properties loaded) on both client and server.
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = NFFGirls.MOD_ID)
public class NFFGirlsBaubleSetupEventListeners {

    @SubscribeEvent
    public static void registerDedicatedBaubles(RegisterBaublesEvent event) {
        // Load dedicated bauble properties from items, then register them to NFU bauble API
        ForgeRegistries.ITEMS.getValues().stream()
            .filter(item -> item instanceof NFFGirlsDedicatedBaubleItem)
            .map(item -> (NFFGirlsDedicatedBaubleItem) item)
            .forEach(item -> {
                item.loadProperties();
                event.register(item);
            });
    }

}
