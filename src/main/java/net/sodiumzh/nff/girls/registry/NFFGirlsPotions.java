package net.sodiumzh.nff.girls.registry;

import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.sodiumzh.nff.girls.NFFGirls;

public class NFFGirlsPotions
{
	public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, NFFGirls.MOD_ID);
	
	/*public static final RegistryObject<Potion> UNDEAD_AFFINITY = POTIONS.register("nffgirls.undead_affinity", 
			() -> new Potion(new MobEffectInstance(NFFGirlsEffects.UNDEAD_AFFINITY.get(), 180 * 20, 0)));
	public static final RegistryObject<Potion> UNDEAD_AFFINITY_LONG = POTIONS.register("nffgirls.undead_affinity_long", 
			() -> new Potion(new MobEffectInstance(NFFGirlsEffects.UNDEAD_AFFINITY.get(), 480 * 20, 0)));*/
}
