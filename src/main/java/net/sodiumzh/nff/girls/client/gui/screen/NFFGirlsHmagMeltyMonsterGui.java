package net.sodiumzh.nff.girls.client.gui.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.sodiumzh.nautils.info.ComponentBuilder;
import net.sodiumzh.nautils.math.GuiPos;
import net.sodiumzh.nautils.statics.NaUtilsMiscStatics;
import net.sodiumzh.nff.girls.entity.hmag.HmagMeltyMonsterEntity;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;

public class NFFGirlsHmagMeltyMonsterGUI extends NFFGirlsGUIPreset0 {
	
	public NFFGirlsHmagMeltyMonsterGUI(NFFTamedInventoryMenu pMenu, Inventory pPlayerInventory,
			INFFTamed mob) {
		super(pMenu, pPlayerInventory, mob);
	}

	@Override
	protected void renderBg(GuiGraphics graphics, float pPartialTick, int pMouseX, int pMouseY) {			
		super.renderBg(graphics, pPartialTick, pMouseX, pMouseY);
		this.addMainScreen(graphics);
		this.addBaubleSlotBg(graphics, 0, leftRowPos().addY(4));
		this.addBaubleSlotBg(graphics, 1, leftRowPos().slotBelow(1).addY(8));
		this.addBaubleSlotBg(graphics, 2, rightRowPos().addY(4));
		this.addBaubleSlotBg(graphics, 3, rightRowPos().slotBelow().addY(8));
		this.addSlotBg(graphics, 4, leftRowPos().slotBelow(2).addY(12), 4, 2);
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
		return 18;
	}
	
	public void addStaminaInfo(GuiGraphics graphics, GuiPos position, int color, int textRowWidth)
	{
		Component staminaComp = ComponentBuilder.create().appendTranslatable("info.nffgirls.gui_melty_monster_stamina")
				.appendText(": ").appendText(Integer.toString(NaUtilsMiscStatics.cast(mob, HmagMeltyMonsterEntity.class).getStamina())).appendText(" / ")
				.appendText(Integer.toString(NaUtilsMiscStatics.cast(mob, HmagMeltyMonsterEntity.class).getMaxStamina())).build();
		graphics.drawString(font, staminaComp, position.x, position.y, color, false);

	}
	
	@Override
	public void addAttributeInfo(GuiGraphics graphics, GuiPos position, int color, int textRowWidth)
	{
		this.addBasicAttributeInfo(graphics, position, color, textRowWidth);
		position = position.addY(textRowWidth * 3);
		this.addStaminaInfo(graphics, position, color, textRowWidth);
		position = position.addY(textRowWidth);
		this.addFavorabilityAndLevelInfo(graphics, position, color, textRowWidth);
	}
	
	@Override
	public void addAttributeInfo(GuiGraphics graphics, GuiPos position)

	{
		addAttributeInfo(graphics, position, 0x404040, 9);
	}
	
}
