package net.sodiumzh.nff.girls.inventory;

import java.util.function.Predicate;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;

public class NFFGirlsKoboldInventoryMenu extends NFFGirlsHandItemsTwoBaublesInventoryMenu
{

	public NFFGirlsKoboldInventoryMenu(int containerId, Inventory playerInventory, Container container, INFFTamed mob)
	{
		super(containerId, playerInventory, container, mob);
	}

	@Override
	protected Predicate<ItemStack> getMainHandCondition()
	{
		return l -> l.getItem() instanceof PickaxeItem;
	}
	
	@Override
	protected int getOffHandMaxAmount()
	{
		return 1;
	}
	
}
