package net.sodiumzh.nff.girls.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.services.client.gui.screen.NFFTamedGui;
import net.sodiumzh.nff.services.entity.ai.NFFTamedMobAIState;
import net.sodiumzh.nfu.mixin.event.client.entity.LivingRendererCheckSitEvent;

@Mod.EventBusSubscriber(modid = NFFGirls.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class NFFGirlsClientEventListeners
{
	@SubscribeEvent
	public static void onClientTick(TickEvent.ClientTickEvent event) {
		
		if (event.side == LogicalSide.CLIENT)
		{
			@SuppressWarnings("resource")
			Minecraft mc = Minecraft.getInstance();
			if (mc.screen != null && mc.screen instanceof NFFTamedGui bgs)
			{
				if (bgs.mob.asMob().isAlive() 
						&& bgs.mob.asMob().isAddedToWorld() 
						&& (bgs.mob.getOwnerInDimension() == null || bgs.mob.asMob().distanceToSqr(bgs.mob.getOwnerInDimension()) > 64.d))
				{
					mc.setScreen(null);
				}
				
			}
		}
	}


	// MIXIN EVENTS BELOW

	@SubscribeEvent
	public static void onCheckSit(LivingRendererCheckSitEvent event) {
		INFFGirlsTamed.get(event.getEntity()).ifPresent(tamed -> {
			if (tamed.level() instanceof ClientLevel cl && cl.getEntity(tamed.asMob().getId()) != null	// Sit only on level, but not in GUI
				&& tamed.getAIState().equals(NFFTamedMobAIState.WAIT) && tamed.getDataAccessor().getAttackTarget() == null
				&& tamed.shouldSitOnWaiting() && tamed.asMob().getDeltaMovement().length() < 1e-8) {
				event.getPoseStack().translate(0, tamed.sitPositionOffset(), 0);
				event.setResult(Event.Result.ALLOW);
			}
		});
	}

}
