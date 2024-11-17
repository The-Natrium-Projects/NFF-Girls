package net.sodiumzh.nff.girls.client.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.world.entity.player.Inventory;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;

public class NFFGirlsHmagCreeperGirlGui extends NFFGirlsGuiPreset0
{

	public NFFGirlsHmagCreeperGirlGui(NFFTamedInventoryMenu pMenu, Inventory pPlayerInventory,
			INFFTamed mob) {
		super(pMenu, pPlayerInventory, mob);
	}

	@Override
	protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
		super.renderBg(pPoseStack, pPartialTick, pMouseX, pMouseY);
		this.addMainScreen(pPoseStack);
		this.addSlotBg(pPoseStack, 0, leftRowPos(), 0, 1);
		this.addSlotBg(pPoseStack, 1, leftRowPos().slotBelow(1), 0, 2);
		this.addSlotBg(pPoseStack, 2, leftRowPos().slotBelow(2), 0, 3);
		this.addSlotBg(pPoseStack, 3, leftRowPos().slotBelow(3), 0, 4);
		this.addSlotBg(pPoseStack, 4, rightRowPos().slotBelow(3), 1, 1);
		this.addSlotBg(pPoseStack, 5, rightRowPos().slotBelow(2), 1, 0);
		this.addSlotBg(pPoseStack, 6, rightRowPos(), 2, 3);
		this.addMobRenderBox(pPoseStack, MobRenderBoxStyle.DARK);
		this.addInfoBox(pPoseStack);
		this.addAttributeInfo(pPoseStack, infoPos());
		this.renderMob();		
	}
}
