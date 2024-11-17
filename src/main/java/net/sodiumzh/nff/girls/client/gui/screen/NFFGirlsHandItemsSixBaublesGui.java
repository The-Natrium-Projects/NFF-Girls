package net.sodiumzh.nff.girls.client.gui.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Inventory;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;

public class NFFGirlsHandItemsSixBaublesGUI extends NFFGirlsGUIPreset0
{

	public NFFGirlsHandItemsSixBaublesGUI(NFFTamedInventoryMenu menu, Inventory playerInventory, INFFTamed mob)
	{
		super(menu, playerInventory, mob);
	}

	@Override
	protected void renderBg(GuiGraphics graphics, float pPartialTick, int pMouseX, int pMouseY) {			
		super.renderBg(graphics, pPartialTick, pMouseX, pMouseY);
		this.addMainScreen(graphics);
		this.addSlotBg(graphics, 0, rightRowPos().slotBelow(3), 1, 1);
		this.addSlotBg(graphics, 1, leftRowPos().slotBelow(3), 1, 0);
		this.addBaubleSlotBg(graphics, 2, leftRowPos());
		this.addBaubleSlotBg(graphics, 3, leftRowPos().slotBelow(1));
		this.addBaubleSlotBg(graphics, 4, leftRowPos().slotBelow(2));
		this.addBaubleSlotBg(graphics, 5, rightRowPos());
		this.addBaubleSlotBg(graphics, 6, rightRowPos().slotBelow(1));
		this.addBaubleSlotBg(graphics, 7, rightRowPos().slotBelow(2));
		this.addMobRenderBox(graphics);
		this.addInfoBox(graphics);
		this.addAttributeInfo(graphics, infoPos());
		this.renderMob(graphics);
	}
	
}
