package net.sodiumzh.nff.girls.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;

public class NFFGirlsThreeBaublesInventoryMenu extends NFFGirlsInventoryMenuPreset0
{

	public NFFGirlsThreeBaublesInventoryMenu(int containerId, Inventory playerInventory, Container container,
			INFFTamed mob)
	{
		super(containerId, playerInventory, container, mob);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void addMenuSlots() {
		this.addBaubleSlot(0, rightRowPos().addY(4), "0");
		this.addBaubleSlot(1, rightRowPos().slotBelow().addY(8), "1");
		this.addBaubleSlot(2, rightRowPos().slotBelow(2).addY(12), "2");
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		int[] order = {0, 1, 2};
		return this.quickMovePreset(order.length, player, index, order);
	}

}
