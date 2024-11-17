package net.sodiumzh.nff.girls.client.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.world.entity.player.Inventory;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;

public class NFFGirlsSixBaublesGui extends NFFGirlsGuiPreset0
{

	public NFFGirlsSixBaublesGui(NFFTamedInventoryMenu menu, Inventory playerInventory, INFFTamed mob)
	{
		super(menu, playerInventory, mob);
	}

	@Override
	protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {			
		super.renderBg(pPoseStack, pPartialTick, pMouseX, pMouseY);
		this.addMainScreen(pPoseStack);
		this.addBaubleSlotBg(pPoseStack, 0, leftRowPos().addY(4));
		this.addBaubleSlotBg(pPoseStack, 1, leftRowPos().slotBelow(1).addY(8));
		this.addBaubleSlotBg(pPoseStack, 2, leftRowPos().slotBelow(2).addY(12));
		this.addBaubleSlotBg(pPoseStack, 3, rightRowPos().addY(4));
		this.addBaubleSlotBg(pPoseStack, 4, rightRowPos().slotBelow(1).addY(8));
		this.addBaubleSlotBg(pPoseStack, 5, rightRowPos().slotBelow(2).addY(12));
		this.addMobRenderBox(pPoseStack, MobRenderBoxStyle.DARK);
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
