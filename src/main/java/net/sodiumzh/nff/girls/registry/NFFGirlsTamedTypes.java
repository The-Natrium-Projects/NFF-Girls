package net.sodiumzh.nff.girls.registry;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.services.entity.taming.NFFTamedTypeRegistry;

@Mod.EventBusSubscriber(modid = NFFGirls.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NFFGirlsTamedTypes {

    @SubscribeEvent
    public static void registerTamedMobs(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> ForgeRegistries.ENTITY_TYPES.getKeys().stream()
            .filter(key -> key.getNamespace().equals(NFFGirls.MOD_ID) && key.getPath().startsWith("hmag_"))
            .forEach(key -> NFFTamedTypeRegistry.add(key, NFFTamedTypeRegistry.SELF)));
    }

}
