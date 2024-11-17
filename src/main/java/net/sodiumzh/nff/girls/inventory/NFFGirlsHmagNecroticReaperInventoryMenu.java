package net.sodiumzh.nff.girls.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;

public class NFFGirlsHmagNecroticReaperInventoryMenu extends NFFGirlsHandItemsFourBaublesInventoryMenu
{

	public NFFGirlsHmagNecroticReaperInventoryMenu(int containerId, Inventory playerInventory, Container container,
			INFFTamed mob)
	{
		super(containerId, playerInventory, container, mob);
	}
	
	@Override
	protected boolean shouldMainHandAccept(ItemStack stack)
	{
		return !(stack.getItem() instanceof TieredItem) || (stack.getItem() instanceof HoeItem);
	}
	
	protected boolean shouldOffHandAccept(ItemStack stack)
	{
		return !(stack.getItem() instanceof TieredItem) || (stack.getItem() instanceof HoeItem);
	}
}
