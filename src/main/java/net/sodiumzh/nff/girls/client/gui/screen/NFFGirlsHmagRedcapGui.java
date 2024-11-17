package net.sodiumzh.nff.girls.client.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.world.entity.player.Inventory;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;

public class NFFGirlsHmagRedcapGui extends NFFGirlsGuiPreset0
{

	public NFFGirlsHmagRedcapGui(NFFTamedInventoryMenu menu, Inventory playerInventory, INFFTamed mob)
	{
		super(menu, playerInventory, mob);
	}

	@Override
	protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
		super.renderBg(pPoseStack, pPartialTick, pMouseX, pMouseY);
		this.addMainScreen(pPoseStack);
		this.addSlotBg(pPoseStack, 0, leftRowPos().addY(4), 0, 2);
		this.addSlotBg(pPoseStack, 1, leftRowPos().slotBelow(1).addY(8), 0, 3);
		this.addSlotBg(pPoseStack, 2, leftRowPos().slotBelow(2).addY(12), 0, 4);
		this.addSlotBg(pPoseStack, 3, rightRowPos().slotBelow(2).addY(12), 4, 1);
		this.addSlotBg(pPoseStack, 4, rightRowPos().slotBelow(1).addY(8), 1, 0);
		this.addBaubleSlotBg(pPoseStack, 5, rightRowPos().addY(4));
		this.addMobRenderBox(pPoseStack);
		this.addInfoBox(pPoseStack);
		this.addAttributeInfo(pPoseStack, infoPos());
		this.renderMob();
	}
	
}
