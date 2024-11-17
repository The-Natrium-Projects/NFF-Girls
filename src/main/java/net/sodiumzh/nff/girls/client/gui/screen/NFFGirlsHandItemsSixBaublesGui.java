package net.sodiumzh.nff.girls.client.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.world.entity.player.Inventory;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;

public class NFFGirlsHandItemsSixBaublesGui extends NFFGirlsGuiPreset0
{

	public NFFGirlsHandItemsSixBaublesGui(NFFTamedInventoryMenu menu, Inventory playerInventory, INFFTamed mob)
	{
		super(menu, playerInventory, mob);
	}

	@Override
	protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {			
		super.renderBg(pPoseStack, pPartialTick, pMouseX, pMouseY);
		this.addMainScreen(pPoseStack);
		this.addSlotBg(pPoseStack, 0, rightRowPos().slotBelow(3), 1, 1);
		this.addSlotBg(pPoseStack, 1, leftRowPos().slotBelow(3), 1, 0);
		this.addBaubleSlotBg(pPoseStack, 2, leftRowPos());
		this.addBaubleSlotBg(pPoseStack, 3, leftRowPos().slotBelow(1));
		this.addBaubleSlotBg(pPoseStack, 4, leftRowPos().slotBelow(2));
		this.addBaubleSlotBg(pPoseStack, 5, rightRowPos());
		this.addBaubleSlotBg(pPoseStack, 6, rightRowPos().slotBelow(1));
		this.addBaubleSlotBg(pPoseStack, 7, rightRowPos().slotBelow(2));
		this.addMobRenderBox(pPoseStack);
		this.addInfoBox(pPoseStack);
		this.addAttributeInfo(pPoseStack, infoPos());
		this.renderMob();
	}
	
}
