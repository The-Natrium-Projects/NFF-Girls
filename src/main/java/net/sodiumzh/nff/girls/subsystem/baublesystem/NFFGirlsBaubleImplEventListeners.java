package net.sodiumzh.nff.girls.subsystem.baublesystem;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.sodiumzh.nautils.statics.NaUtilsEntityStatics;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.entity.hmag.HmagCrimsonSlaughtererEntity;
import net.sodiumzh.nff.services.subsystem.baublesystem.BaubleSystem;
@EventBusSubscriber(modid = NFFGirls.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class NFFGirlsBaubleImplEventListeners
{
	@SubscribeEvent
	public static void poisonousThornImpl(LivingDamageEvent event)
	{
		if (event.getSource().getEntity() != null && event.getSource().getEntity() instanceof Mob mob && event.getAmount() >= 0.01)
		{
			BaubleSystem.ifCapabilityPresent(mob, () -> 
			{
				int poisonLevel = NFFGirlsBaubleStatics.countBaubles(mob, new ResourceLocation(NFFGirls.MOD_ID, "poisonous_thorn"));
				if (poisonLevel > 0)
				{
					if (poisonLevel > 0)
						NaUtilsEntityStatics.addEffectSafe(event.getEntity(), MobEffects.POISON, 5 * Math.min(poisonLevel, 3) * 20, Math.min(poisonLevel, 3) - 1);
					if (mob instanceof HmagCrimsonSlaughtererEntity cs)
						NaUtilsEntityStatics.addEffectSafe(event.getEntity(), MobEffects.MOVEMENT_SLOWDOWN, (20 + 10 * poisonLevel) * 20, 1);
				}
			});
		}
	}
	
	
}
