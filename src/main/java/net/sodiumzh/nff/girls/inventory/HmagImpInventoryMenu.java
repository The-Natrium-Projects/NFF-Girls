package net.sodiumzh.nff.girls.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;

public class HmagImpInventoryMenu extends NFFGirlsHandItemsTwoBaublesInventoryMenu
{

	public HmagImpInventoryMenu(int containerId, Inventory playerInventory, Container container, INFFTamed mob)
	{
		super(containerId, playerInventory, container, mob);
	}

	
}
