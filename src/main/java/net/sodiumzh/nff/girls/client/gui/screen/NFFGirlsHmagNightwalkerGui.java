package net.sodiumzh.nff.girls.client.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.world.entity.player.Inventory;
import net.sodiumzh.nautils.math.GuiPos;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;

public class NFFGirlsHmagNightwalkerGui extends NFFGirlsGuiPreset0
{
	public NFFGirlsHmagNightwalkerGui(NFFTamedInventoryMenu pMenu, Inventory pPlayerInventory,
			INFFTamed mob) {
		super(pMenu, pPlayerInventory, mob);
	}

	@Override
	protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {			
		super.renderBg(pPoseStack, pPartialTick, pMouseX, pMouseY);
		this.addMainScreen(pPoseStack);
		this.addBaubleSlotBg(pPoseStack, 0, leftRowPos());
		this.addBaubleSlotBg(pPoseStack, 1, leftRowPos().slotBelow(1));
		this.addBaubleSlotBg(pPoseStack, 2, leftRowPos().slotBelow(2));
		this.addBaubleSlotBg(pPoseStack, 3, leftRowPos().slotBelow(3));
		this.addSlotBg(pPoseStack, 4, rightRowPos().slotBelow(3), 4, 3);
		this.addMobRenderBox(pPoseStack, MobRenderBoxStyle.LIGHT);
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
		return 18;
	}
}
