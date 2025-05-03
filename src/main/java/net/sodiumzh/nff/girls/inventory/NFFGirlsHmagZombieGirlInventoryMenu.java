package net.sodiumzh.nff.girls.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nfu.math.GuiPos;

public class NFFGirlsHmagZombieGirlInventoryMenu extends NFFGirlsInventoryMenuPreset0
{

	public NFFGirlsHmagZombieGirlInventoryMenu(int containerId, Inventory playerInventory, Container container,
			INFFTamed mob)
	{
		super(containerId, playerInventory, container, mob);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void addMenuSlots()
	{
		this.addArmorSlot(0, leftRowPos(), HEAD, null);
		this.addArmorSlot(1, leftRowPos().slotBelow(), CHEST, null);
		this.addArmorSlot(2, leftRowPos().slotBelow(2), LEGS, null);
		this.addArmorSlot(3, leftRowPos().slotBelow(3), FEET, null);
		this.addGeneralSlot(4, rightRowPos().slotBelow(3), null);
		this.addGeneralSlot(5, rightRowPos().slotBelow(2), null);
		this.addBaubleSlot(6, rightRowPos(), "0");
		this.addBaubleSlot(7, rightRowPos().slotBelow(), "1");
	}
	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		int[] order = {6, 7, 0, 1, 2, 3, 4, 5};
		return this.quickMovePreset(order.length, player, index, order);
	}
	
	@Override
	protected GuiPos getPlayerInventoryPosition()
	{
		return GuiPos.valueOf(32, 101);
	}

	@Override
	public void removed(Player pPlayer) {
		super.removed(pPlayer);
	}

}
