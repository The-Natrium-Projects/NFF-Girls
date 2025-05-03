package net.sodiumzh.nff.girls.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nfu.math.GuiPos;

import java.util.function.Predicate;

public class NFFGirlsHandItemsTwoBaublesInventoryMenu extends NFFGirlsInventoryMenuPreset0
{
	public NFFGirlsHandItemsTwoBaublesInventoryMenu(int containerId, Inventory playerInventory, Container container,
			INFFTamed mob) {
		super(containerId, playerInventory, container, mob);
	}

	@Override
	protected void addMenuSlots()
	{
		this.addGeneralSlot(0, leftRowPos().addY(10), getMainHandCondition());
		this.addGeneralSlot(1, leftRowPos().slotBelow().addY(20), null, getOffHandMaxAmount());
		this.addBaubleSlot(2, rightRowPos().addY(10), "0");
		this.addBaubleSlot(3, rightRowPos().slotBelow().addY(20), "1");
	}
	
	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		int[] order = {2, 3, 0, 1};
		return this.quickMovePreset(order.length, player, index, order);
	}
	
	@Override
	protected GuiPos getPlayerInventoryPosition()
	{
		return GuiPos.valueOf(32, 101);
	}

	protected Predicate<ItemStack> getMainHandCondition()
	{
		return null;
	}
	
	protected int getOffHandMaxAmount()
	{
		return 64;
	}
}
