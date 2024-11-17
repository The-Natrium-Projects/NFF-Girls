package net.sodiumzh.nff.girls.client.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.sodiumzh.nautils.info.ComponentBuilder;
import net.sodiumzh.nautils.math.GuiPos;
import net.sodiumzh.nautils.statics.NaUtilsInfoStatics;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.entity.INFFGirlTamed;
import net.sodiumzh.nff.services.client.gui.screen.NFFTamedGui;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;

/** GUI template for all vanilla-undead-mob-like befriended mobs.
*/

@OnlyIn(Dist.CLIENT)
public class NFFGirlsGuiPreset0 extends NFFTamedGui {
	
	protected static final ResourceLocation DEFAULT_TEXTURE_LOC = new ResourceLocation(NFFGirls.MOD_ID, "textures/gui/container/gui_preset_0.png");
	protected static final GuiPos DEFAULT_TEXTURE_SIZE = new GuiPos(512, 256);
	protected int mobRenderScale = 25;
	@Deprecated
	protected MobRenderBoxStyle mobRenderBoxStyle = MobRenderBoxStyle.DARK;
	protected int page = 0;
	protected int maxPage = 1;
	
	public NFFGirlsGuiPreset0(NFFTamedInventoryMenu menu, Inventory playerInventory, INFFTamed mob) {
		super(menu, playerInventory, mob, true);
		imageWidth = 224;
		imageHeight = 183;
		inventoryLabelY = imageHeight - 93;
		this.setTexture(DEFAULT_TEXTURE_LOC, DEFAULT_TEXTURE_SIZE);
	}

	@Override
	protected void init() {
		super.init();
		//this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
	}

	public GuiPos getEntityRenderPosition()
	{
		return new GuiPos(52, 80);
	}
	
	public int getMobRenderScale()
	{
		return mobRenderScale;
	}
	
	public NFFGirlsGuiPreset0 setMobRenderScale(int value)
	{
		mobRenderScale = value;
		return this;
	}
	
	@Deprecated
	public NFFGirlsGuiPreset0 setMobRenderBoxStyle(MobRenderBoxStyle style)
	{
		this.mobRenderBoxStyle = style;
		return this;
	}
	
	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
		super.render(poseStack, mouseX, mouseY, partialTick);
	}

	public void renderItemSlots()
	{
		
	}
	
	// Add hp, atk and armor only
	public void addBasicAttributeInfo(PoseStack poseStack, GuiPos position, int color, int textRowWidth)
	{
		super.addAttributeInfo(poseStack, position, color, textRowWidth);
	}
	
	public void addFavorabilityAndLevelInfo(PoseStack poseStack, GuiPos position, int color, int textRowWidth)
	{
		GuiPos pos = position;
		font.draw(poseStack, getDefaultLevelInfo(), pos.x, pos.y, color);
		pos = pos.addY(textRowWidth);
		font.draw(poseStack, getDefaultExpInfo(), pos.x, pos.y, color);
		pos = pos.addY(textRowWidth);
		font.draw(poseStack, getDefaultFavInfo(), pos.x, pos.y, color);
	}
	
	protected MutableComponent getDefaultLevelAndExpInfo()
	{
		INFFGirlTamed bm = (INFFGirlTamed)mob;		
		String lv = Integer.toString(bm.getLevelHandler().getExpectedLevel());
		String exp = Long.toString(bm.getLevelHandler().getExpInThisLevel());
		String expup = Long.toString(bm.getLevelHandler().getRequiredExpInThisLevel());
		return ComponentBuilder.create().appendTranslatable("info.nffgirls.gui_level_and_exp")
				.appendText(": " + lv + " (" + exp + " / " + expup + ")").build();
	}
	
	protected MutableComponent getDefaultLevelInfo()
	{
		INFFGirlTamed bm = (INFFGirlTamed)mob;		
		String lv = Integer.toString(bm.getLevelHandler().getExpectedLevel());
		return NaUtilsInfoStatics.createTranslatable("info.nffgirls.gui_level")
				.append(NaUtilsInfoStatics.createText(": " + lv));	
	}
	
	protected MutableComponent getDefaultExpInfo()
	{
		INFFGirlTamed bm = (INFFGirlTamed)mob;
		String exp = Long.toString(bm.getLevelHandler().getExpInThisLevel());
		String expup = Long.toString(bm.getLevelHandler().getRequiredExpInThisLevel());
		return NaUtilsInfoStatics.createTranslatable("info.nffgirls.gui_exp")
				.append(NaUtilsInfoStatics.createText(": " + exp + " / " + expup));
		
	}
	
	protected MutableComponent getDefaultFavInfo()
	{
		INFFGirlTamed bm = (INFFGirlTamed)mob;	
		String fav = Integer.toString(Mth.floor(bm.getFavorabilityHandler().getFavorability()));
		String favmax = Integer.toString(Mth.floor(bm.getFavorabilityHandler().getMaxFavorability()));
		return NaUtilsInfoStatics.createTranslatable("info.nffgirls.gui_favorability")
				.append(NaUtilsInfoStatics.createText(": " + fav + " / " + favmax));
	}
	
	@Override
	public void addAttributeInfo(PoseStack poseStack, GuiPos position, int color, int textRowWidth)
	{
		this.addBasicAttributeInfo(poseStack, position, color, textRowWidth);
		position = position.addY(textRowWidth * 3);
		this.addFavorabilityAndLevelInfo(poseStack, position, color, textRowWidth);
	}

	@Override
	public void addAttributeInfo(PoseStack poseStack, GuiPos position)
	{
		addAttributeInfo(poseStack, position, 0x404040, 11);
	}
	
	/** Below are texture-specific, must using gui_preset_0.png */
	
	protected GuiPos screenSize()
	{
		return GuiPos.valueOf(224, 183);
	}
	
	protected GuiPos basePos()
	{
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		return GuiPos.valueOf(i, j); 
	}
	
	// Absolute position from relative position of screen
	protected GuiPos absPos(int x, int y)
	{
		return basePos().add(x, y);
	}
	
	protected GuiPos absPos(GuiPos xy)
	{
		return basePos().add(xy);
	}
	
	protected GuiPos leftRowPos()
	{
		return absPos(7, 17);
	}
	
	protected GuiPos rightRowPos()
	{
		return absPos(79, 17);
	}
	
	protected void addMainScreen(PoseStack poseStack)
	{
		this.drawSprite(poseStack, basePos(), GuiPos.zero(), screenSize());
	}
	
	protected void addSlotBg(PoseStack poseStack, int slotIndex, GuiPos pos, int slotIndexX, int slotIndexY)
	{
		this.addSlotBg(poseStack, slotIndex, pos, GuiPos.valueOf(256, 0).coord(slotIndexX, slotIndexY), GuiPos.valueOf(256, 0));
	}

	protected void addBaubleSlotBg(PoseStack poseStack, int slotIndex, GuiPos pos)
	{
		this.addSlotBg(poseStack, slotIndex, pos, 1, 2);
	}
	
	@Deprecated
	public void addMobRenderBox(PoseStack poseStack, int variation)
	{
		this.drawSprite(poseStack, absPos(27, 17), GuiPos.valueOf(120 + variation * 50, 183), GuiPos.valueOf(50, 72));
	}
	
	public void addMobRenderBox(PoseStack poseStack)
	{
		this.drawSprite(poseStack, absPos(27, 17), GuiPos.valueOf(120 + this.mobRenderBoxStyle.getIndex() * 50, 183), GuiPos.valueOf(50, 72));
	}
	
	public void addMobRenderBox(PoseStack poseStack, MobRenderBoxStyle style)
	{
		this.drawSprite(poseStack, absPos(27, 17), GuiPos.valueOf(120 + style.getIndex() * 50, 183), GuiPos.valueOf(50, 72));
	}
	
	public void addInfoBox(PoseStack poseStack)
	{
		this.drawSprite(poseStack, absPos(99, 17), GuiPos.valueOf(0, 183), GuiPos.valueOf(120, 72));
	}

	public GuiPos infoPos()
	{
		return absPos(103, 21);
	}
	
	public void renderMob(GuiPos offset)
	{
		GuiPos pos = absPos(getEntityRenderPosition().add(offset));
		InventoryScreen.renderEntityInInventory(pos.x, pos.y, getMobRenderScale(), 
				(float) pos.x - this.xMouse, (float) (pos.y - 50) - this.yMouse, mob.asMob());
	}
	
	public void renderMob()
	{
		renderMob(GuiPos.valueOf(0));
	}
	
	public static enum MobRenderBoxStyle
	{
		LIGHT(0),
		NORMAL(1),
		DARK(2);
		private int index;
		private MobRenderBoxStyle(int index)
		{
			this.index = index;
		}
		public int getIndex()
		{
			return index;
		}
		
	}
	
}

