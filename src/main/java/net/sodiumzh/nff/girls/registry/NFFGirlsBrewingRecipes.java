package net.sodiumzh.nff.girls.registry;

import com.github.mechalopa.hmag.registry.ModItems;
import com.github.mechalopa.hmag.util.ModBrewingRecipe;
import com.github.mechalopa.hmag.util.ModUtils;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.sodiumzh.nff.girls.NFFGirls;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = NFFGirls.MOD_ID)
public class NFFGirlsBrewingRecipes {

	@SubscribeEvent
	public static void doRegisterBrewingRecipes(FMLCommonSetupEvent event)
	{
		event.enqueueWork(() -> {
			registerBrewingRecipe(Potions.NIGHT_VISION, ModItems.SOUL_APPLE.get(), NFFGirlsPotions.UNDEAD_AFFINITY.get());
			registerBrewingRecipe(NFFGirlsPotions.UNDEAD_AFFINITY.get(), NFFGirlsItems.DEATH_CRYSTAL_POWDER.get(), NFFGirlsPotions.UNDEAD_AFFINITY_LONG.get());
			registerBrewingRecipe(Potions.LONG_NIGHT_VISION, NFFGirlsItems.DEATH_CRYSTAL_POWDER.get(), NFFGirlsPotions.UNDEAD_AFFINITY_LONG.get());
			registerBrewingRecipe(NFFGirlsPotions.UNDEAD_AFFINITY_LONG.get(), ModItems.LICH_CLOTH.get(), NFFGirlsPotions.UNDEAD_AFFINITY_LONG_2.get());
			registerBrewingRecipe(Potions.SLOW_FALLING, ModItems.ENDER_PLASM.get(), NFFGirlsPotions.ENDER_PROTECTION.get());
			registerBrewingRecipe(NFFGirlsPotions.ENDER_PROTECTION.get(), NFFGirlsItems.ENDERBERRY.get(), NFFGirlsPotions.ENDER_PROTECTION_LONG.get());
			registerBrewingRecipe(Potions.LONG_SLOW_FALLING, NFFGirlsItems.ENDERBERRY.get(), NFFGirlsPotions.ENDER_PROTECTION_LONG.get());
			registerBrewingRecipe(NFFGirlsPotions.ENDER_PROTECTION_LONG.get(), ModItems.LIGHTNING_PARTICLE.get(), NFFGirlsPotions.ENDER_PROTECTION_LONG_2.get());
		});
	}



	// From HMaG
	private static void registerBrewingRecipe(Potion inputPotion, Item item, Potion outputPotion)
	{
		BrewingRecipeRegistry.addRecipe(new ModBrewingRecipe(Ingredient.of(ModUtils.getPotionStack(inputPotion)), Ingredient.of(item), ModUtils.getPotionStack(outputPotion)));
		BrewingRecipeRegistry.addRecipe(new ModBrewingRecipe(Ingredient.of(ModUtils.getPotionStack(inputPotion, Items.SPLASH_POTION)), Ingredient.of(item), ModUtils.getPotionStack(outputPotion, Items.SPLASH_POTION)));
		BrewingRecipeRegistry.addRecipe(new ModBrewingRecipe(Ingredient.of(ModUtils.getPotionStack(inputPotion, Items.LINGERING_POTION)), Ingredient.of(item), ModUtils.getPotionStack(outputPotion, Items.LINGERING_POTION)));
	}

}
