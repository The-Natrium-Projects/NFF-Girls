package net.sodiumzh.nff.girls.item.bauble;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.IModBusEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.sodiumzh.nff.girls.NFFGirls;

import java.util.HashMap;

/**
 * Registry of NFFGirls additional properties for bauble items.
 */
@EventBusSubscriber(modid = NFFGirls.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class NFFGirlsBaubleAdditionalRegistry
{
	// ResourceLocation key, tier
	private static final HashMap<INFFGirlsBauble, Tuple<ResourceLocation, Integer>> REGISTRY = new HashMap<>();

	/**
	 * Forge {@link FMLCommonSetupEvent} handler that fires {@link RegisterEvent} on the mod event bus,
	 * allowing baubles to populate {@link #getRegistry()} during common setup.
	 */
	@SubscribeEvent
	public static void fireRegisterEvent(FMLCommonSetupEvent event)
	{
		event.enqueueWork(() -> {
			ModLoader.get().postEvent(new RegisterEvent());
		});
	}
	
	/**
	 * Get the additional bauble registry, mapping each registered {@link INFFGirlsBauble} to a tuple of
	 * its category key and tier.
	 */
	public static HashMap<INFFGirlsBauble, Tuple<ResourceLocation, Integer>> getRegistry()
	{
		return REGISTRY;
	}
	
	/**
	 * Mod bus event fired during common setup, allowing baubles to register themselves (or be registered
	 * by others) into {@link NFFGirlsBaubleAdditionalRegistry#getRegistry()}.
	 */
	public static class RegisterEvent extends Event implements IModBusEvent
	{
		/**
		 * Register the given bauble under the given category key and tier.
		 */
		public void register(INFFGirlsBauble item, ResourceLocation key, Integer tier)
		{
			NFFGirlsBaubleAdditionalRegistry.REGISTRY.put(item, new Tuple<>(key, tier));
		}
		
		/**
		 * Register the given bauble using its own {@link INFFGirlsBauble#getCategoryKey()} and
		 * {@link INFFGirlsBauble#getTier()}.
		 */
		public void register(INFFGirlsBauble item)
		{
			NFFGirlsBaubleAdditionalRegistry.REGISTRY.put(item, new Tuple<>(item.getCategoryKey(), item.getTier()));
		}
	}
}
