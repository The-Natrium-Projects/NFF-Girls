package net.sodiumzh.nff.girls.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nfu.math.GuiPos;

public class NFFGirlsFourBaublesInventoryMenu extends NFFGirlsInventoryMenuPreset0
{
	public NFFGirlsFourBaublesInventoryMenu(int containerId, Inventory playerInventory, Container container,
			INFFTamed mob) {
		super(containerId, playerInventory, container, mob);
	}

	@Override
	protected void addMenuSlots()
	{
		this.addBaubleSlot(0, leftRowPos().addY(10), "0");
		this.addBaubleSlot(1, leftRowPos().slotBelow().addY(20), "1");
		this.addBaubleSlot(2, rightRowPos().addY(10), "2");
		this.addBaubleSlot(3, rightRowPos().slotBelow().addY(20), "3");
	}
	
	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		int[] order = {0, 1, 2, 3};
		return this.quickMovePreset(order.length, player, index, order);
	}
	
	@Override
	protected GuiPos getPlayerInventoryPosition()
	{
		return GuiPos.valueOf(32, 101);
	}
	
}