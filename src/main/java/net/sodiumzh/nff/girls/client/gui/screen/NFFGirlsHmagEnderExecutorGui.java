package net.sodiumzh.nff.girls.client.gui.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Inventory;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;

public class NFFGirlsHmagEnderExecutorGUI extends NFFGirlsGUIPreset0 {

	public NFFGirlsHmagEnderExecutorGUI(NFFTamedInventoryMenu pMenu, Inventory pPlayerInventory,
			INFFTamed mob) {
		super(pMenu, pPlayerInventory, mob);
	}

	@Override
	protected void renderBg(GuiGraphics graphics, float pPartialTick, int pMouseX, int pMouseY) {
		super.renderBg(graphics, pPartialTick, pMouseX, pMouseY);
		this.addMainScreen(graphics);
		this.addSlotBg(graphics, 0, leftRowPos().addY(4), 1, 1);
		this.addSlotBg(graphics, 1, leftRowPos().slotBelow().addY(8), 1, 0);
		this.addSlotBg(graphics, 2, leftRowPos().slotBelow(2).addY(12), 2, 2);
		this.addBaubleSlotBg(graphics, 3, rightRowPos().addY(10));
		this.addBaubleSlotBg(graphics, 4, rightRowPos().slotBelow().addY(20));
		this.addMobRenderBox(graphics, MobRenderBoxStyle.NORMAL);
		this.addInfoBox(graphics);
		this.addAttributeInfo(graphics, infoPos());
		this.renderMob(graphics);
	}
	
	@Override
	public int getMobRenderScale()
	{
		return 18;
	}

}
