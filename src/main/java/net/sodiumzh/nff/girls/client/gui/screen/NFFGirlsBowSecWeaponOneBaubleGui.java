package net.sodiumzh.nff.girls.client.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.world.entity.player.Inventory;
import net.sodiumzh.nautils.math.GuiPos;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;

// Gui for mobs with bow shooting attack + secondary weapon + one bouble
public class NFFGirlsBowSecWeaponOneBaubleGui extends NFFGirlsGuiPreset0 {

	public NFFGirlsBowSecWeaponOneBaubleGui(NFFTamedInventoryMenu pMenu, Inventory pPlayerInventory,
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
		this.addBaubleSlotBg(pPoseStack, 6, rightRowPos());
		this.addSlotBg(pPoseStack, 7, rightRowPos().slotBelow(1), 2, 1);
		this.addMobRenderBox(pPoseStack, MobRenderBoxStyle.DARK);
		this.addSlotBg(pPoseStack, 8, rightRowPos().slotLeft(1).addX(-2), 2, 0);
		this.addInfoBox(pPoseStack);
		this.addAttributeInfo(pPoseStack, infoPos());
		this.renderMob(GuiPos.valueOf(0, 3));	
	}

}
