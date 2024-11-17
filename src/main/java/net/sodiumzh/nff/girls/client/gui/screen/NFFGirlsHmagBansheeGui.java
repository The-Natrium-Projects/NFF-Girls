package net.sodiumzh.nff.girls.client.gui.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Inventory;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;

public class NFFGirlsHmagBansheeGUI extends NFFGirlsGUIPreset0
{

	public NFFGirlsHmagBansheeGUI(NFFTamedInventoryMenu menu, Inventory playerInventory, INFFTamed mob)
	{
		super(menu, playerInventory, mob);
	}
	
	@Override
	protected void renderBg(GuiGraphics graphics, float pPartialTick, int pMouseX, int pMouseY) {
		super.renderBg(graphics, pPartialTick, pMouseX, pMouseY);
		this.addMainScreen(graphics);
		this.addSlotBg(graphics, 0, leftRowPos().addY(10), 1, 1);
		this.addSlotBg(graphics, 1, leftRowPos().slotBelow().addY(20), 3, 0);
		this.addBaubleSlotBg(graphics, 2, rightRowPos().addY(4));
		this.addBaubleSlotBg(graphics, 3, rightRowPos().slotBelow().addY(8));
		this.addBaubleSlotBg(graphics, 4, rightRowPos().slotBelow(2).addY(12));
		this.addMobRenderBox(graphics, 1);
		this.addInfoBox(graphics);
		this.addAttributeInfo(graphics, infoPos());
		this.renderMob(graphics);
	}

}
