package net.sodiumzh.nff.girls.client.gui.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Inventory;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;

public class NFFGirlsHmagDodomekiGUI extends NFFGirlsGUIPreset0
{

	public NFFGirlsHmagDodomekiGUI(NFFTamedInventoryMenu menu, Inventory playerInventory, INFFTamed mob)
	{
		super(menu, playerInventory, mob);
	}

	@Override
	protected void renderBg(GuiGraphics graphics, float pPartialTick, int pMouseX, int pMouseY) {
		super.renderBg(graphics, pPartialTick, pMouseX, pMouseY);
		this.addMainScreen(graphics);
		this.addBaubleSlotBg(graphics, 0, leftRowPos());
		this.addBaubleSlotBg(graphics, 1, leftRowPos().slotBelow());
		this.addBaubleSlotBg(graphics, 2, leftRowPos().slotBelow(2));
		this.addBaubleSlotBg(graphics, 3, leftRowPos().slotBelow(3));
		/*this.addSlotBg(graphics, 4, rightRowPos(), 4, 0);
		this.addSlotBg(graphics, 5, rightRowPos().slotBelow(), 4, 0);
		this.addSlotBg(graphics, 6, rightRowPos().slotBelow(2), 4, 0);
		this.addSlotBg(graphics, 7, rightRowPos().slotBelow(3), 4, 0);*/
		this.addMobRenderBox(graphics, MobRenderBoxStyle.LIGHT);
		this.addInfoBox(graphics);
		this.addAttributeInfo(graphics, infoPos());
		this.renderMob(graphics);
	}
	
}
