package net.sodiumzh.nff.girls.eventlistener;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.level.SleepFinishedTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;

import java.util.List;

@Mod.EventBusSubscriber(modid = NFFGirls.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class NFFGirlsLevelEventListeners
{
	
	@SubscribeEvent
	public static void onSleepFinished(SleepFinishedTimeEvent event)
	{
		if (event.getLevel() instanceof ServerLevel level)
		{
			for (Player player: level.players())
			{
				AABB bound = new AABB(player.position().add(-8, -8, -8), player.position().add(8, 8, 8));
				List<Entity> entities = level.getEntities(player, bound);
				for (Entity entity: entities)
				{
					if (INFFGirlsTamed.get(entity).filter(bm -> bm.getOwnerUUID().equals(player.getUUID())).isPresent()
							&& entity.distanceToSqr(player) < 64f)
					{
						INFFGirlsTamed.get(entity).orElseThrow().getFavorabilityHandler().addFavorability(2f);
					}
				}
			}
		}
	}

}
