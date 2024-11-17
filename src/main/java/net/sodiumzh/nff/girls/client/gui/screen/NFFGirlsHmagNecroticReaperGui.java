package net.sodiumzh.nff.girls.client.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.world.entity.player.Inventory;
import net.sodiumzh.nautils.math.GuiPos;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;

public class NFFGirlsHmagNecroticReaperGui extends NFFGirlsGuiPreset0
{
	public NFFGirlsHmagNecroticReaperGui(NFFTamedInventoryMenu menu, Inventory playerInventory, INFFTamed mob)
	{
		super(menu, playerInventory, mob);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
		super.renderBg(pPoseStack, pPartialTick, pMouseX, pMouseY);
		this.addMainScreen(pPoseStack);
		this.addSlotBg(pPoseStack, 0, rightRowPos().slotBelow(3), 1, 4);
		this.addSlotBg(pPoseStack, 1, leftRowPos().slotBelow(3), 2, 4);
		this.addBaubleSlotBg(pPoseStack, 2, leftRowPos());
		this.addBaubleSlotBg(pPoseStack, 3, leftRowPos().slotBelow());
		this.addBaubleSlotBg(pPoseStack, 4, rightRowPos());
		this.addBaubleSlotBg(pPoseStack, 5, rightRowPos().slotBelow());
		this.addMobRenderBox(pPoseStack, MobRenderBoxStyle.DARK);
		this.addInfoBox(pPoseStack);
		this.addAttributeInfo(pPoseStack, infoPos());
		this.renderMob();
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
