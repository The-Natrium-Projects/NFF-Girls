package net.sodiumzh.nff.girls.item;

import java.util.List;

import javax.annotation.Nullable;

import com.github.mechalopa.hmag.registry.ModItems;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.sodiumzh.nautils.statics.NaUtilsInfoStatics;
import net.sodiumzh.nautils.statics.NaUtilsItemStatics;

@Deprecated
public class EvilMagnetItem extends Item implements IWithDuration
{

	public EvilMagnetItem(Properties pProperties)
	{
		super(pProperties);
	}

	
	@Override
	@SuppressWarnings("resource")
	public InteractionResult useOn(UseOnContext context)
	{
		/*if (context.getLevel().getBlockState(context.getClickedPos()).is(ModBlocks.EVIL_CRYSTAL_BLOCK.get()))
		{
			if (!context.getLevel().isClientSide && context.getLevel() instanceof ServerLevel sl)
			{
				BlockPos blockpos = context.getClickedPos();
				Iterable<Entity> entities = sl.getAllEntities();
				List<ItemEntity> respawners = new ArrayList<>();
				for (Entity e: entities)
				{
					if (e instanceof ItemEntity ie)
					{
						if (ie.getItem().is(NFFGirlsItems.MOB_RESPAWNER.get()))
						{
							NFFMobRespawnerInstance mr = NFFMobRespawnerInstance.create(ie.getItem());
							if (mr != null 
									&& NFFTamedStatics.getModIdFromNbt(mr.getMobNbt()).equals(NFFGirls.MOD_ID)
									&& NFFTamedStatics.getOwnerUUIDFromNbt(mr.getMobNbt()).equals(context.getPlayer().getUUID()))
							{
								respawners.add(ie); 							
							}
						}
					}
				}
				respawners = respawners.stream().sorted(Comparator.comparingDouble((ItemEntity ie) -> ie.distanceToSqr(context.getPlayer()))).toList();
				for (ItemEntity ie: respawners)
				{
					if (consumeDuration(context.getItemInHand(), 1))
					{
						ie.moveTo(blockpos.getX() + 0.5 + RndUtil.rndRangedDouble(-0.2,  0.2), blockpos.getY()+ 1.5, blockpos.getZ() + 0.5 + RndUtil.rndRangedDouble(-0.2,  0.2));
					}
					else break;
				}
			}
			return InteractionResult.sidedSuccess(context.getLevel().isClientSide);
		}
		return InteractionResult.PASS;*/
		return super.useOn(context);
	}
	
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand pUsedHand)
	{
		if (!player.level.isClientSide)
		{
			player.getItemInHand(pUsedHand).shrink(1);
			NaUtilsItemStatics.giveOrDrop(player, new ItemStack(Items.NETHERITE_INGOT, 1));
			NaUtilsItemStatics.giveOrDrop(player, new ItemStack(ModItems.EVIL_CRYSTAL.get(), 4));
			NaUtilsItemStatics.giveOrDrop(player, new ItemStack(Items.IRON_INGOT, 2));
		}
		return InteractionResultHolder.sidedSuccess(player.getItemInHand(pUsedHand), player.level.isClientSide);
	}
	
	// ======== IWithDuration interface

	@Override
	public int getMaxDuration() {
		return 8;
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag)
	{
		super.appendHoverText(stack, level, list, tooltipFlag);
		//list.add(this.getDurationDescription(stack));
		list.add(NaUtilsInfoStatics.createText("Deprecated. Right click to uncraft to ingredients."));
	}

	
}
