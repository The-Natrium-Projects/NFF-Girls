package net.sodiumzh.nff.girls.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.sodiumzh.nautils.statics.NaUtilsMiscStatics;
import net.sodiumzh.nff.girls.entity.tamingprocesses.hmag.NFFGirlsItemDroppingTamingProcess;
import net.sodiumzh.nff.services.entity.taming.NFFTamingMapping;
import net.sodiumzh.nff.services.entity.taming.TamingProcessItemGivingProgress;
import net.sodiumzh.nff.services.registry.NFFCapRegistry;

public class NFFTamingProgressProbeItem extends Item
{

	public NFFTamingProgressProbeItem(Properties pProperties)
	{
		super(pProperties);
	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) 
	{
		if (!player.level.isClientSide)
		{
			if (target instanceof Mob mob && target.getCapability(NFFCapRegistry.CAP_BEFRIENDABLE_MOB).isPresent())
			{
				if (NFFTamingMapping.getProcess(mob) instanceof TamingProcessItemGivingProgress prog)
				{
					NaUtilsMiscStatics.printToScreen("Progress: " + prog.getProgressValue(mob, player.getUUID()), player);
					return InteractionResult.CONSUME;
				}
				else if (NFFTamingMapping.getProcess(mob) instanceof NFFGirlsItemDroppingTamingProcess dropping)
				{
					NaUtilsMiscStatics.printToScreen("Progress: " + dropping.getProgressValue(mob, player.getUUID()), player);
					return InteractionResult.CONSUME;
				}
			}
		}
		return InteractionResult.PASS;
	}
}
