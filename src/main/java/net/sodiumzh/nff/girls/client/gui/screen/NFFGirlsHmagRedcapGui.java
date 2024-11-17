package net.sodiumzh.nff.girls.client.gui.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Inventory;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;

public class NFFGirlsHmagRedcapGUI extends NFFGirlsGUIPreset0
{

	public NFFGirlsHmagRedcapGUI(NFFTamedInventoryMenu menu, Inventory playerInventory, INFFTamed mob)
	{
		super(menu, playerInventory, mob);
	}

	@Override
	protected void renderBg(GuiGraphics graphics, float pPartialTick, int pMouseX, int pMouseY) {
		super.renderBg(graphics, pPartialTick, pMouseX, pMouseY);
		this.addMainScreen(graphics);
		this.addSlotBg(graphics, 0, leftRowPos().addY(4), 0, 2);
		this.addSlotBg(graphics, 1, leftRowPos().slotBelow(1).addY(8), 0, 3);
		this.addSlotBg(graphics, 2, leftRowPos().slotBelow(2).addY(12), 0, 4);
		this.addSlotBg(graphics, 3, rightRowPos().slotBelow(2).addY(12), 4, 1);
		this.addSlotBg(graphics, 4, rightRowPos().slotBelow(1).addY(8), 1, 0);
		this.addBaubleSlotBg(graphics, 5, rightRowPos().addY(4));
		this.addMobRenderBox(graphics);
		this.addInfoBox(graphics);
		this.addAttributeInfo(graphics, infoPos());
		this.renderMob(graphics);
	}
	
}
