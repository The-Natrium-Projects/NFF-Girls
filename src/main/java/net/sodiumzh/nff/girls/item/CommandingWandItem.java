package net.sodiumzh.nff.girls.item;

import java.util.List;

import com.github.mechalopa.hmag.registry.ModBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.AABB;
import net.sodiumzh.nautils.statics.NaUtilsMathStatics;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.registry.NFFGirlsItems;
import net.sodiumzh.nff.services.entity.ai.NFFTamedMobAIState;
import net.sodiumzh.nff.services.entity.taming.NFFTamedStatics;
import net.sodiumzh.nff.services.item.NFFMobRespawnerInstance;

public class CommandingWandItem extends Item
{

	public CommandingWandItem(Properties pProperties)
	{
		super(pProperties);
	}
	
	@Override
	@SuppressWarnings("resource")
	public InteractionResult useOn(UseOnContext context)
	{
		if (context.getLevel().getBlockState(context.getClickedPos()).is(ModBlocks.EVIL_CRYSTAL_BLOCK.get()))
		{
			if (!context.getLevel().isClientSide && context.getLevel() instanceof ServerLevel sl)
			{
				BlockPos blockpos = context.getClickedPos();
				AABB bound = new AABB(blockpos.subtract(new Vec3i(16, 16, 16)), blockpos.offset(new Vec3i(16, 16, 16)));
				List<Entity> bmList = sl.getEntities(null, bound);
				for (Entity e: bmList)
				{
					if (e instanceof INFFGirlsTamed bm && bm.getOwnerUUID().equals(context.getPlayer().getUUID()))
						((INFFGirlsTamed)e).setAIState(NFFTamedMobAIState.FOLLOW, true);
					else if (e instanceof ItemEntity ie)
					{
						if (ie.getItem().is(NFFGirlsItems.MOB_RESPAWNER.get()))
						{
							NFFMobRespawnerInstance mr = NFFMobRespawnerInstance.create(ie.getItem());
							if (mr != null && NFFTamedStatics.getOwnerUUIDFromNbt(mr.getMobNbt()).equals(context.getPlayer().getUUID()))
							{
								ie.moveTo(blockpos.getX() + 0.5 + NaUtilsMathStatics.rndRangedDouble(-0.2,  0.2), blockpos.getY()+ 1.5, blockpos.getZ() + 0.5 + NaUtilsMathStatics.rndRangedDouble(-0.2,  0.2));
							}
						}
					}
				}
			}
			return InteractionResult.sidedSuccess(context.getLevel().isClientSide);
		}
		return InteractionResult.PASS;
	}

}
