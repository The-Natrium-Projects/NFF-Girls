package net.sodiumzh.nff.girls.registry;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nfu.registry.RegistryObjectPreConstruction;

@Mod.EventBusSubscriber(modid = NFFGirls.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NFFGirlsRegistryObjectPreConstruction {

    @SubscribeEvent
    public static void setPreConstruct(RegistryObjectPreConstruction.SetupEvent event) {
        event.preConstruct(NFFGirlsEntityAttributes.ATTRIBUTES);
        event.preConstruct(ForgeRegistries.ATTRIBUTES, "forge", "step_height_addition");
    }
}
