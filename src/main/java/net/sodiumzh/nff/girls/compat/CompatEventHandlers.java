package net.sodiumzh.nff.girls.compat;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;

/**
 * Methods handling compatibility issues
 */
@Mod.EventBusSubscriber(modid = NFFGirls.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CompatEventHandlers
{
	protected static final String TF_MOD_ID = "twilightforest";
	protected static final String FAA_MOD_ID = "forbidden_arcanus";
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onLivingHurt_PriorityLowest(LivingHurtEvent event)
	{
		
		// Fix TF Seeker Arrow targeting BM
		// Now it's impossible to prevent the arrow from targeting BM w/o mixin, so now only damage can be removed
		// TODO: fully fix this after TF inserts event
		if (!event.getEntity().level().isClientSide && INFFGirlsTamed.get(event.getEntity()).isPresent())
		{
			INFFGirlsTamed bm = INFFGirlsTamed.get(event.getEntity()).get();
			if (event.getSource().getDirectEntity() != null
					&& EntityType.getKey(event.getSource().getDirectEntity().getType())
					.equals(new ResourceLocation(TF_MOD_ID, "seeker_arrow")))
			{
				Projectile proj = (Projectile) event.getSource().getDirectEntity();
				if (proj.getOwner() == bm.getOwner())
					event.setCanceled(true);
			}
		}
	}

}
