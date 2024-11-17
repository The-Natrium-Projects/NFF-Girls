package net.sodiumzh.nff.girls.client.gui.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Inventory;
import net.sodiumzh.nautils.math.GuiPos;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;

public class NFFGirlsHmagNecroticReaperGUI extends NFFGirlsGUIPreset0
{
	public NFFGirlsHmagNecroticReaperGUI(NFFTamedInventoryMenu menu, Inventory playerInventory, INFFTamed mob)
	{
		super(menu, playerInventory, mob);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void renderBg(GuiGraphics graphics, float pPartialTick, int pMouseX, int pMouseY) {
		super.renderBg(graphics, pPartialTick, pMouseX, pMouseY);
		this.addMainScreen(graphics);
		this.addSlotBg(graphics, 0, rightRowPos().slotBelow(3), 1, 4);
		this.addSlotBg(graphics, 1, leftRowPos().slotBelow(3), 2, 4);
		this.addBaubleSlotBg(graphics, 2, leftRowPos());
		this.addBaubleSlotBg(graphics, 3, leftRowPos().slotBelow());
		this.addBaubleSlotBg(graphics, 4, rightRowPos());
		this.addBaubleSlotBg(graphics, 5, rightRowPos().slotBelow());
		this.addMobRenderBox(graphics, MobRenderBoxStyle.DARK);
		this.addInfoBox(graphics);
		this.addAttributeInfo(graphics, infoPos());
		this.renderMob(graphics);
	}


	@Override
	public GuiPos getEntityRenderPosition()
	{
		return super.getEntityRenderPosition().addY(0);
	}
	
	@Override
	public int getMobRenderScale()
	{
		return 26;
	}
}
