package net.sodiumzh.nff.girls.client.gui.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.sodiumzh.nautils.info.ComponentBuilder;
import net.sodiumzh.nautils.math.GuiPos;
import net.sodiumzh.nautils.math.HtmlColors;
import net.sodiumzh.nautils.math.LinearColor;
import net.sodiumzh.nautils.statics.NaUtilsInfoStatics;
import net.sodiumzh.nff.girls.entity.hmag.HmagSlimeGirlEntity;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;

public class NFFGirlsHmagSlimeGirlGUI extends NFFGirlsGUIPreset0
{

	protected HmagSlimeGirlEntity sg;
	
	public NFFGirlsHmagSlimeGirlGUI(NFFTamedInventoryMenu menu, Inventory playerInventory, INFFTamed mob)
	{
		super(menu, playerInventory, mob);
		sg = (HmagSlimeGirlEntity) mob;
	}

	@Override
	protected void renderBg(GuiGraphics graphics, float pPartialTick, int pMouseX, int pMouseY) {			
		super.renderBg(graphics, pPartialTick, pMouseX, pMouseY);
		this.addMainScreen(graphics);
		this.addBaubleSlotBg(graphics, 0, leftRowPos());
		this.addBaubleSlotBg(graphics, 1, leftRowPos().slotBelow(1));
		this.addBaubleSlotBg(graphics, 2, leftRowPos().slotBelow(2));
		this.addBaubleSlotBg(graphics, 3, leftRowPos().slotBelow(3));
		this.addMobRenderBox(graphics, MobRenderBoxStyle.DARK);
		this.addInfoBox(graphics);
		this.addAttributeInfo(graphics, infoPos());
		this.renderMob(graphics);
	}
	
	// All inventory slots are on the left, and expand the info box
	@Override
	public void addInfoBox(GuiGraphics graphics)
	{
		//this.blit(graphics, absPos(99, 17), IntVec2.valueOf(0, 183), IntVec2.valueOf(120, 72));
		this.drawSprite(graphics, absPos(81, 17), GuiPos.valueOf(0, 183), GuiPos.valueOf(2, 72));
		this.drawSprite(graphics, absPos(83, 17), GuiPos.valueOf(2, 183), GuiPos.valueOf(18, 72));
		this.drawSprite(graphics, absPos(101, 17), GuiPos.valueOf(2, 183), GuiPos.valueOf(118, 72));
	}
	
	public MutableComponent getDefaultColorInfo()
	{
		
		Component colorKey = HtmlColors.getTranslationKey(HtmlColors.getNearestHtmlColor(sg.getColorLinear()));
		return ComponentBuilder.create().appendTranslatable("info.nffgirls.gui_slime_color").appendText(": ")
				.append(colorKey).build().withStyle(Style.EMPTY.withColor(sg.getColorLinear().toCode()));
	}
	
	public MutableComponent getColorRGBInfo()
	{
		LinearColor color = sg.getColorLinear();
		Vec3i rgb = color.toRGB();
		String rgbInfo = " (R" + Integer.toString(rgb.getX()) + ", G" + Integer.toString(rgb.getY()) + ", B" + Integer.toString(rgb.getZ()) +")";
		return NaUtilsInfoStatics.createText(rgbInfo).withStyle(Style.EMPTY.withColor(color.toCode()));
	}
	
	@Override
	public void addAttributeInfo(GuiGraphics graphics, GuiPos position, int color, int textRowWidth)
	{
		this.addBasicAttributeInfo(graphics, position, color, textRowWidth);
		position = position.addY(textRowWidth * 2);
		this.addFavorabilityAndLevelInfo(graphics, position, color, textRowWidth);
	}
	
	@Override
	public void addBasicAttributeInfo(GuiGraphics graphics, GuiPos position, int color, int textRowWidth)
	{
		GuiPos pos = position;
		String hp = Integer.toString(Math.round(mob.asMob().getHealth()));
		String maxHp = Long.toString(Math.round(mob.asMob().getAttributeValue(Attributes.MAX_HEALTH)));
		String atk = Long.toString(Math.round(mob.asMob().getAttributeValue(Attributes.ATTACK_DAMAGE)));
		String def = Long.toString(Math.round(mob.asMob().getAttributeValue(Attributes.ARMOR)));
		Component hpcomp = NaUtilsInfoStatics.createTranslatable("info.nffservices.gui_health")
				.append(NaUtilsInfoStatics.createText(": " + hp + " / " + maxHp));
		Component atkcomp = NaUtilsInfoStatics.createTranslatable("info.nffservices.gui_atk")
				.append(NaUtilsInfoStatics.createText(": " + atk));
		Component defcomp = NaUtilsInfoStatics.createTranslatable("info.nffservices.gui_armor")
				.append(NaUtilsInfoStatics.createText(": " + def));
		Component atkDefComp = ComponentBuilder.create().append(atkcomp).appendText(" | ")
				.append(defcomp).build();
		graphics.drawString(font, hpcomp, pos.x, pos.y, color);
		pos = pos.addY(textRowWidth);
		graphics.drawString(font, atkDefComp, pos.x, pos.y, color);
	}
	
	@Override
	public void addFavorabilityAndLevelInfo(GuiGraphics graphics, GuiPos position, int color, int textRowWidth)
	{
		graphics.drawString(font, getDefaultLevelAndExpInfo(), position.x, position.y, color, false);
		position = position.addY(textRowWidth);
		graphics.drawString(font, getDefaultFavInfo(), position.x, position.y, color, false);
		position = position.addY(textRowWidth);
		graphics.drawString(font, getDefaultColorInfo(), position.x, position.y, color, false);
	}

	@Override
	public GuiPos infoPos()
	{
		return absPos(85, 21);
	}

	
}
