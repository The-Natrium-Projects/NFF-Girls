package net.sodiumzh.nff.girls.inventory;

import com.github.mechalopa.hmag.registry.ModItems;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.sodiumzh.nautils.math.GuiPos;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;

public class NFFGirlsHmagGhastlySeekerInventoryMenu extends NFFGirlsInventoryMenuPreset0
{

	public NFFGirlsHmagGhastlySeekerInventoryMenu(int containerId, Inventory playerInventory, Container container,
			INFFTamed mob) {
		super(containerId, playerInventory, container, mob);
	}

	@Override
	protected void addMenuSlots()
	{
		this.addBaubleSlot(0, leftRowPos().addY(10), "0");
		this.addBaubleSlot(1, leftRowPos().slotBelow().addY(20), "1");
		this.addBaubleSlot(2, rightRowPos().addY(4), "2");
		this.addBaubleSlot(3, rightRowPos().slotBelow().addY(8), "3");
		this.addGeneralSlot(4, rightRowPos().slotBelow(2).addY(12), s -> s.is(Items.FIRE_CHARGE) || s.is(ModItems.BLASTING_BOTTLE.get()));
	}
	
	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		int[] order = {0, 1, 2, 3, 4};
		return this.quickMovePreset(order.length, player, index, order);
	}
	
	@Override
	protected GuiPos getPlayerInventoryPosition()
	{
		return GuiPos.valueOf(32, 101);
	}

}
