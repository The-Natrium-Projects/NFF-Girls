package net.sodiumzh.nff.girls.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;

import java.util.function.Predicate;

public class NFFGirlsHmagKoboldInventoryMenu extends NFFGirlsHandItemsTwoBaublesInventoryMenu
{

	public NFFGirlsHmagKoboldInventoryMenu(int containerId, Inventory playerInventory, Container container, INFFTamed mob)
	{
		super(containerId, playerInventory, container, mob);
	}

	@Override
	protected Predicate<ItemStack> getMainHandCondition()
	{
		return l -> l.getItem() instanceof PickaxeItem;
	}

	
}
