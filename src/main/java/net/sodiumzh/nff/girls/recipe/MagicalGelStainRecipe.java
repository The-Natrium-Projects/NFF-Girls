package net.sodiumzh.nff.girls.recipe;

import java.util.HashMap;
import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;

import com.mojang.logging.LogUtils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.sodiumzh.nautils.math.HtmlColors;
import net.sodiumzh.nautils.math.LinearColor;
import net.sodiumzh.nautils.statics.NaUtilsItemStatics;
import net.sodiumzh.nautils.statics.NaUtilsTagStatics;
import net.sodiumzh.nff.girls.registry.NFFGirlsItems;
import net.sodiumzh.nff.girls.registry.NFFGirlsRecipes;

public class MagicalGelStainRecipe extends SimpleModificationRecipe
{
	protected static final HashMap<String, StainInfo> STAINS = new HashMap<>();

	public MagicalGelStainRecipe(ResourceLocation id, CraftingBookCategory category)
	{
		super(id, category);
	}
	
	protected static void putDyeColorStain(String itemKey, DyeColor color, Supplier<Double> strength)
	{
		STAINS.put(itemKey, StainInfo.of(LinearColor.fromCode(color.getTextColor()), strength));
	}
	
	protected static void putDyeColorStain(String itemKey, DyeColor color, double strength)
	{
		STAINS.put(itemKey, StainInfo.of(LinearColor.fromCode(color.getTextColor()), () -> strength));
	}
	
	protected static void putHtmlColorStain(String itemKey, String colorKey, Supplier<Double> strength)
	{
		LinearColor color = HtmlColors.HTML_COLORS.get(colorKey);
		if (color == null)
		{
			LogUtils.getLogger().error("Missing HTML color \"" + colorKey + "\".");
			return;
		}
		STAINS.put(itemKey, StainInfo.of(color, strength));
	}
	
	protected static void putHtmlColorStain(String itemKey, String colorKey, double strength)
	{
		putHtmlColorStain(itemKey, colorKey, () -> strength);
	}
	
	// @see https://www.w3schools.cn/colors/colors_groups.asp
	static
	{
		// 16 minecraft standard colors
		// Vanilla / forge common items
		putDyeColorStain("hmag:sharp_fang",  DyeColor.WHITE, 0.5);
		putDyeColorStain("hmag:ogre_horn",  DyeColor.LIGHT_GRAY, 1.0);
		putDyeColorStain("hmag:evil_crystal_fragment", DyeColor.GRAY, 0.5);
		putDyeColorStain("hmag:ancient_stone", DyeColor.BLACK, 0.5);
		putDyeColorStain("hmag:crimson_cuticula", DyeColor.RED, 1.0);
		putDyeColorStain("hmag:cureberry", DyeColor.RED, 1.0);
		putDyeColorStain("hmag:kobold_leather", DyeColor.BLUE, 0.5);
		putDyeColorStain("hmag:dyssomnia_skin", DyeColor.BLUE, 1.2);
		putDyeColorStain("hmag:randomberry", DyeColor.PURPLE, 1.0);
		putDyeColorStain("hmag:necrofiber", DyeColor.PURPLE, 0.5);
		putDyeColorStain("hmag:lich_cloth", DyeColor.BROWN, 1.0);
		putDyeColorStain("hmag:soul_apple", DyeColor.LIGHT_BLUE, 0.5);
		putDyeColorStain("hmag:mysterious_petal", DyeColor.PINK, 0.5);
		putDyeColorStain("hmag:cubic_nucleus", DyeColor.PINK, 0.5);
		putDyeColorStain("hmag:ender_plasm", DyeColor.MAGENTA, 0.5);
		putDyeColorStain("hmag:exp_berry", DyeColor.LIME, 1.0);
		putDyeColorStain("hmag:burning_core", DyeColor.ORANGE, 0.5);
	}
	
	protected static Optional<StainInfo> getStainInfo(ItemStack stack)
	{
		if (stack.isEmpty())
			return Optional.empty();
		for (String key: STAINS.keySet())
		{
			if (key.charAt(0) == '#')
			{
				if (NaUtilsTagStatics.hasTag(stack, key.substring(1)))
					return Optional.of(STAINS.get(key));
			}
			else
			{
				if (NaUtilsItemStatics.getItem(key) == stack.getItem())
					return Optional.of(STAINS.get(key));
			}
		}
		return Optional.empty();
	}
	
	public MagicalGelStainRecipe(ResourceLocation pId)
	{
		super(pId);
	}
/*
	@Override
	public boolean matches(CraftingContainer ctn, Level level) {
		ItemStack bottle = null;
		ItemStack stain = null;
		MagicalGelBottleItem type = NFFGirlsItems.MAGICAL_GEL_BOTTLE.get();
		for (int i = 0; i < ctn.getContainerSize(); ++i)
		{
			if (ctn.getItem(i).is(NFFGirlsItems.MAGICAL_GEL_BOTTLE.get()))
			{
				// Empty gel bottle, fail
				if (type.getAmount(ctn.getItem(i)) == 0)
					return false;
				// Get bottle
				else if (bottle == null)
				{
					bottle = ctn.getItem(i);
				}
				// Duplicate bottles, fail
				else return false;
			}
			// No item, pass
			else if (ctn.getItem(i).isEmpty())
			{
				continue;
			}
			else
			{
				Optional<StainInfo> info = getStainInfo(ctn.getItem(i));
				// Non-stain detected, fail
				if (info.isEmpty())
					return false;
				else if (stain == null)
					stain = ctn.getItem(i);
				else return false;
			}
		}
		return bottle != null && stain != null;
	}

	@Override
	public ItemStack assemble(CraftingContainer ctn) {
		ItemStack bottle = null;
		StainInfo stainInfo = null;
		ItemStack stain = null;
		MagicalGelBottleItem type = NFFGirlsItems.MAGICAL_GEL_BOTTLE.get();
		for (int i = 0; i < ctn.getContainerSize(); ++i)
		{
			if (ctn.getItem(i).is(NFFGirlsItems.MAGICAL_GEL_BOTTLE.get()))
			{
				// Empty gel bottle, fail
				if (type.getAmount(ctn.getItem(i)) == 0)
					return ItemStack.EMPTY;
				// Get bottle
				else if (bottle == null)
				{
					bottle = ctn.getItem(i);
				}
				// Duplicate bottles, fail
				else return ItemStack.EMPTY;
			}
			// No item, pass
			else if (ctn.getItem(i).isEmpty())
			{
				continue;
			}
			else
			{
				Optional<StainInfo> info = getStainInfo(ctn.getItem(i));
				// Non-stain detected, fail
				if (info.isEmpty())
					return ItemStack.EMPTY;
				else if (stain == null)
				{
					stainInfo = info.get();
					stain = ctn.getItem(i);
				}
				// Duplicate stain, fail
				else return ItemStack.EMPTY;
			}
		}
		if (bottle == null || stain == null)
			return ItemStack.EMPTY;
		
		ItemStack res = bottle.copy();
		type.stain(res, stainInfo.color.get(), stainInfo.strength.get());
		return res;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= 2;
	}
*/
	
	@Override
	public boolean isSubjectItem(ItemStack stack) {
		return stack.is(NFFGirlsItems.MAGICAL_GEL_BOTTLE.get());
	}

	@Override
	public boolean isModifierItem(ItemStack stack) {
		return getStainInfo(stack).isPresent();
	}

	@Override
	public ItemStack getResult(ItemStack subject, ItemStack modifier) {
		ItemStack res = subject.copy();
		StainInfo stainInfo = getStainInfo(modifier).get();
		NFFGirlsItems.MAGICAL_GEL_BOTTLE.get().stain(res, stainInfo.color.get(), stainInfo.strength.get());
		return res;
	}

	@Override
	public ItemStack getSubjectRemaining(ItemStack subject, ItemStack modifier) {
		return ItemStack.EMPTY;
	}
	@Override
	public RecipeSerializer<?> getSerializer() {
		return NFFGirlsRecipes.MAGICAL_GEL_STAIN.get();
	}

	protected static record StainInfo(Supplier<LinearColor> color, Supplier<Double> strength, boolean isMutable)
	{
		public static StainInfo of(Supplier<LinearColor> color, Supplier<Double> strength)
		{
			return new StainInfo(color, strength, false);
		}
		
		public static StainInfo of(Supplier<LinearColor> color, double strength)
		{
			return new StainInfo(color, () -> strength, false);
		}
		
		public static StainInfo of(LinearColor color, Supplier<Double> strength)
		{
			return new StainInfo(() -> color, strength, false);
		}
		
		public static StainInfo of(LinearColor color, double strength)
		{
			return new StainInfo(() -> color, () -> strength, false);
		}
		
		public static StainInfo ofMutable(Supplier<LinearColor> color, Supplier<Double> strength)
		{
			return new StainInfo(color, strength, true);
		}
		
	}
	
	
	// ==== Utils ===
	
	protected static Random rnd = new Random();
	
	protected static LinearColor randomColorGaussian()
	{
		Supplier<Double> gen = () -> {
			while (true)
			{
				double val = rnd.nextGaussian() + 0.5d;
				if (val >= 0 && val <= 1)
					return val;
			}
		};
		return LinearColor.fromNormalized(gen.get(), gen.get(), gen.get());
	}


}
