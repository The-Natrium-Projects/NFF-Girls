package net.sodiumzh.nff.girls.item.bauble;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
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
	private static final HashMap<INFFGirlsBauble, Tuple<ResourceLocation, Integer>> REGISTRY = new HashMap<>();

	/**
	 * Mod-lifecycle hook that fires the {@link RegisterEvent} on the mod event bus during
	 * common setup, so that baubles can register their category key and tier.
	 * @param event the common setup event
	 */
	@SubscribeEvent
	public static void fireRegisterEvent(FMLCommonSetupEvent event)
	{
		event.enqueueWork(() -> {
			ModLoader.get().postEvent(new NFFGirlsBaubleAdditionalRegistry.RegisterEvent());
		});
	}

	/**
	 * @return the backing registry mapping each bauble to its (category key, tier) pair.
	 */
	public static HashMap<INFFGirlsBauble, Tuple<ResourceLocation, Integer>> getRegistry()
	{
		return REGISTRY;
	}

	/**
	 * Event fired on the mod event bus allowing baubles to register their additional
	 * (category key, tier) properties into the registry.
	 */
	public static class RegisterEvent extends Event implements IModBusEvent
	{
		/**
		 * Register a bauble with an explicit category key and tier.
		 * @param item the bauble to register
		 * @param key  the category key to associate with it
		 * @param tier the tier to associate with it
		 */
		public void register(INFFGirlsBauble item, ResourceLocation key, Integer tier)
		{
			NFFGirlsBaubleAdditionalRegistry.REGISTRY.put(item, new Tuple<>(key, tier));
		}

		/**
		 * Register a bauble using its own {@link INFFGirlsBauble#getCategoryKey() category key}
		 * and {@link INFFGirlsBauble#getTier() tier}.
		 * @param item the bauble to register
		 */
		public void register(INFFGirlsBauble item)
		{
			NFFGirlsBaubleAdditionalRegistry.REGISTRY.put(item, new Tuple<>(item.getCategoryKey(), item.getTier()));
		}
	}
}
