package net.sodiumzh.nff.girls.compat;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.entity.INFFGirlTamed;

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
		if (!event.getEntity().level.isClientSide && event.getEntity() instanceof INFFGirlTamed bm)
		{
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
	/*	It apprears that this is no longer needed after commanding wand is added
	@SuppressWarnings("unchecked")
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onEntityInteract_PriorityHigh(EntityInteract event)
	{
		Player player = event.getEntity();
		Entity entity = event.getTarget();
		InteractionHand hand = event.getHand();
		if (!player.level.isClientSide)
		{
			// Fix interaction conflict with FAA Quantum Catcher
			if (isUsingFAAQuantumCatcherOnOwningMob(player, entity, hand))
			{
				Item qcItem = player.getItemInHand(hand).getItem();	// Here the item should be QuantumCatcherItem
				InteractionResult result = (InteractionResult) NaUtilsReflectionStatics.forceInvokeRetVal(qcItem, (Class<Item>) qcItem.getClass(), "onEntityInteract", 
						ItemStack.class, Player.class, LivingEntity.class, InteractionHand.class, player.getItemInHand(hand), player, entity, hand);
				if (result.consumesAction())
				{
					event.setCanceled(true);
				}
			}		
		}
	}

	// Check if an interaction is player trying to use FAA quantum catcher to catch the owning BM

	public static boolean isUsingFAAQuantumCatcherOnOwningMob(Player player, Entity entity, InteractionHand hand)
	{
		return ForgeRegistries.ITEMS.getKey(player.getItemInHand(hand).getItem())
				.equals(new ResourceLocation(FAA_MOD_ID, "quantum_catcher")) 
				&& !entity.level.isClientSide
				&& entity instanceof INFFGirlTamed bm 
				&& bm.getOwner() == player;
	}
	
	*/
}
