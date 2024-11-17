package net.sodiumzh.nff.girls.client.gui.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Inventory;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;

public class NFFGirlsEquipmentTwoBaublesGUI extends NFFGirlsGUIPreset0 {

	public NFFGirlsEquipmentTwoBaublesGUI(NFFTamedInventoryMenu pMenu, Inventory pPlayerInventory,
			INFFTamed mob) {
		super(pMenu, pPlayerInventory, mob);
	}

	@Override
	protected void renderBg(GuiGraphics graphics, float pPartialTick, int pMouseX, int pMouseY) {
		super.renderBg(graphics, pPartialTick, pMouseX, pMouseY);
		this.addMainScreen(graphics);
		this.addSlotBg(graphics, 0, leftRowPos(), 0, 1);
		this.addSlotBg(graphics, 1, leftRowPos().slotBelow(1), 0, 2);
		this.addSlotBg(graphics, 2, leftRowPos().slotBelow(2), 0, 3);
		this.addSlotBg(graphics, 3, leftRowPos().slotBelow(3), 0, 4);
		this.addSlotBg(graphics, 4, rightRowPos().slotBelow(3), 1, 1);
		this.addSlotBg(graphics, 5, rightRowPos().slotBelow(2), 1, 0);
		this.addSlotBg(graphics, 6, rightRowPos(), 1, 2);
		this.addSlotBg(graphics, 7, rightRowPos().slotBelow(1), 1, 2);
		this.addMobRenderBox(graphics, 2);
		this.addInfoBox(graphics);
		this.addAttributeInfo(graphics, infoPos());
		this.renderMob(graphics);
	}
	

}
