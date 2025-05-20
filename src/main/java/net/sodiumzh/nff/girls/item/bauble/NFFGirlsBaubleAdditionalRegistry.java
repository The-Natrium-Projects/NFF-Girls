package net.sodiumzh.nff.girls.item.bauble;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.IModBusEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.sodiumzh.nff.girls.NFFGirls;

import java.util.HashMap;

/**
 * Registry of DWMG additional properties for bauble items.
 */
@EventBusSubscriber(modid = NFFGirls.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NFFGirlsBaubleAdditionalRegistry
{
	// ResourceLocation key, tier
	private static final HashMap<Item, Tuple<ResourceLocation, Integer>> REGISTRY = new HashMap<>();

	@SubscribeEvent
	public static void fireRegisterEvent(FMLCommonSetupEvent event)
	{
		event.enqueueWork(() -> {
			ModLoader.get().postEvent(new NFFGirlsBaubleAdditionalRegistry.RegisterEvent());
		});
	}
	
	public static HashMap<Item, Tuple<ResourceLocation, Integer>> getRegistry()
	{
		return REGISTRY;
	}
	
	public static class RegisterEvent extends Event implements IModBusEvent
	{
		public void register(Item item, ResourceLocation key, Integer tier)
		{
			NFFGirlsBaubleAdditionalRegistry.REGISTRY.put(item, new Tuple<>(key, tier));
		}
		
		public void register(NFFGirlsDedicatedBaubleItem item)
		{
			NFFGirlsBaubleAdditionalRegistry.REGISTRY.put(item, new Tuple<>(item.additionalKey, item.tier));
		}
	}
}
