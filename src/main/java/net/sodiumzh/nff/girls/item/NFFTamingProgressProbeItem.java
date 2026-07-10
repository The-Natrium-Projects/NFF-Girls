package net.sodiumzh.nff.girls.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.sodiumzh.nff.girls.entity.tamingprocess.hmag.NFFGirlsItemDroppingTamingProcess;
import net.sodiumzh.nff.services.entity.taming.NFFTamableComponent;
import net.sodiumzh.nff.services.entity.taming.NFFTamingMapping;
import net.sodiumzh.nff.services.entity.taming.TamingProcessItemGivingProgress;
import net.sodiumzh.nff.services.registry.NFFCapRegistry;
import net.sodiumzh.nfu.util.NFUInfoStatics;

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
			if (target instanceof Mob mob && NFFTamableComponent.getOptional(target).isPresent())
			{
				if (NFFTamingMapping.getProcess(mob) instanceof TamingProcessItemGivingProgress prog)
				{
					NFUInfoStatics.printMessage(player, "Progress: " + prog.getProgressValue(mob, player.getUUID()).orElse(0d));
					return InteractionResult.CONSUME;
				}
				else if (NFFTamingMapping.getProcess(mob) instanceof NFFGirlsItemDroppingTamingProcess dropping)
				{
					NFUInfoStatics.printMessage(player, "Progress: " + dropping.getProgressValue(mob, player.getUUID()).orElse(0d));
					return InteractionResult.CONSUME;
				}
			}
		}
		return InteractionResult.PASS;
	}
}
