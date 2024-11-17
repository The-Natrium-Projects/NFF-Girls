package net.sodiumzh.nff.girls.client.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.world.entity.player.Inventory;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;

public class NFFGirlsHmagEnderExecutorGui extends NFFGirlsGuiPreset0 {

	public NFFGirlsHmagEnderExecutorGui(NFFTamedInventoryMenu pMenu, Inventory pPlayerInventory,
			INFFTamed mob) {
		super(pMenu, pPlayerInventory, mob);
	}

	@Override
	protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
		super.renderBg(pPoseStack, pPartialTick, pMouseX, pMouseY);
		this.addMainScreen(pPoseStack);
		this.addSlotBg(pPoseStack, 0, leftRowPos().addY(4), 1, 1);
		this.addSlotBg(pPoseStack, 1, leftRowPos().slotBelow().addY(8), 1, 0);
		this.addSlotBg(pPoseStack, 2, leftRowPos().slotBelow(2).addY(12), 2, 2);
		this.addBaubleSlotBg(pPoseStack, 3, rightRowPos().addY(10));
		this.addBaubleSlotBg(pPoseStack, 4, rightRowPos().slotBelow().addY(20));
		this.addMobRenderBox(pPoseStack, MobRenderBoxStyle.NORMAL);
		this.addInfoBox(pPoseStack);
		this.addAttributeInfo(pPoseStack, infoPos());
		this.renderMob();	
	}
	
	@Override
	public int getMobRenderScale()
	{
		return 18;
	}

}
