package net.sodiumzh.nff.girls.item;

import com.github.mechalopa.hmag.registry.ModEntityTypes;
import com.github.mechalopa.hmag.world.entity.MagicalSlimeEntity;
import com.github.mechalopa.hmag.world.entity.SlimeGirlEntity;
import com.mojang.logging.LogUtils;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.sodiumzh.nff.girls.entity.hmag.HmagSlimeGirlEntity;
import net.sodiumzh.nff.girls.registry.NFFGirlsItems;
import net.sodiumzh.nfu.item.NFUItem;
import net.sodiumzh.nfu.math.HtmlColors;
import net.sodiumzh.nfu.math.LinearColor;
import net.sodiumzh.nfu.util.NFUInfoStatics;
import net.sodiumzh.nfu.util.NFUNBTStatics;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class MagicalGelBottleItem extends NFUItem
{

	public static final LinearColor VANILLA_SLIME_COLOR = LinearColor.fromRGB(100, 172, 81);
	public static final LinearColor MAGMA_CUBE_COLOR = LinearColor.fromRGB(220, 114, 32);
	
	public MagicalGelBottleItem(Item.Properties pProperties)
	{
		super(pProperties.stacksTo(1));
	}

	protected void checkStackType(ItemStack stack)
	{
		if (!(stack.getItem() instanceof MagicalGelBottleItem))
			throw new IllegalArgumentException("Wrong item stack type!");
	}
	
	public static ItemStack create(MagicalGelBottleItem type)
	{
		ItemStack stack = new ItemStack(type);
		stack.getOrCreateTag().put("amount", IntTag.valueOf(1));
		stack.getTag().put("red", DoubleTag.valueOf(0));
		stack.getTag().put("green", DoubleTag.valueOf(0));
		stack.getTag().put("blue", DoubleTag.valueOf(0));
		stack.getTag().put("show_color", ByteTag.valueOf(true));
		return stack;
	}

	public static ItemStack create(MagicalGelBottleItem type, LinearColor color)
	{
		ItemStack stack = create(type);
		type.setColor(stack, color);
		return stack;
	}

	@Nonnull
	public Optional<ItemStack> getDefaultInstanceOverride() {
		return Optional.of(create(this));
	}

	protected boolean checkValid(ItemStack stack)
	{
		checkStackType(stack);
		return stack.hasTag() && stack.getTag().contains("amount", NFUNBTStatics.TAG_INT_ID)
				&& stack.getTag().contains("red", NFUNBTStatics.TAG_DOUBLE_ID)
				&& stack.getTag().contains("green", NFUNBTStatics.TAG_DOUBLE_ID)
				&& stack.getTag().contains("blue", NFUNBTStatics.TAG_DOUBLE_ID)
				&& stack.getTag().contains("show_color", NFUNBTStatics.TAG_BYTE_ID);
	}
		
	public int getAmount(ItemStack stack)
	{
		if (!checkValid(stack))
		{
			LogUtils.getLogger().error("MagicalGelBottleItem stack missing NBT. Always use create() to create MagicalGelBottleItem stack instead of ItemStack#new!");
			return 0;
		}
		return stack.getTag().getInt("amount");
	}
	
	public void setAmount(ItemStack stack, int value)
	{
		if (!checkValid(stack))
		{
			LogUtils.getLogger().error("MagicalGelBottleItem stack missing NBT. Always use create() to create MagicalGelBottleItem stack instead of ItemStack#new!");
			return;
		}
		if (value <= 0)
			throw new IllegalArgumentException("setAmount: must be positive.");
		stack.getOrCreateTag().put("amount", IntTag.valueOf(Math.max(value, 0)));
	}
	
	public LinearColor getColor(ItemStack stack)
	{
		if (!checkValid(stack))
		{
			LogUtils.getLogger().error("MagicalGelBottleItem stack missing NBT. Always use create() to create MagicalGelBottleItem stack instead of ItemStack#new!");
			return LinearColor.fromNormalized(0, 0, 0);
		}
		return LinearColor.fromNormalized(stack.getTag().getDouble("red"), stack.getTag().getDouble("green"), stack.getTag().getDouble("blue"));
	}

	public void setColor(ItemStack stack, LinearColor color)
	{
		if (!checkValid(stack))
		{
			LogUtils.getLogger().error("MagicalGelBottleItem stack missing NBT. Always use create() to create MagicalGelBottleItem stack instead of ItemStack#new!");
			return;
		}
		stack.getTag().put("red", DoubleTag.valueOf(color.r));
		stack.getTag().put("green", DoubleTag.valueOf(color.g));
		stack.getTag().put("blue", DoubleTag.valueOf(color.b));
	}

	public boolean getShowColor(ItemStack stack)
	{
		if (!checkValid(stack))
		{
			LogUtils.getLogger().error("MagicalGelBottleItem stack missing NBT. Always use create() to create MagicalGelBottleItem stack instead of ItemStack#new!");
			return true;
		}
		else return stack.getTag().getBoolean("show_color");
	}
	
	public void setShowColor(ItemStack stack, boolean value)
	{
		if (!checkValid(stack))
		{
			LogUtils.getLogger().error("MagicalGelBottleItem stack missing NBT. Always use create() to create MagicalGelBottleItem stack instead of ItemStack#new!");
			return;
		}
		stack.getTag().putBoolean("show_color", value);
	}
	
	public void stain(ItemStack stack, LinearColor newColor, double strength) 
	{
		if (strength <= 0d)
			return;
		double alpha = ((double) strength) / ((double)getAmount(stack) + strength);
		this.setColor(stack, LinearColor.lerp(this.getColor(stack), newColor, alpha));
	}
	
	public void blend(ItemStack stack, LinearColor newColor, int amount)
	{
		this.stain(stack, newColor, amount);
		this.setAmount(stack, this.getAmount(stack) + amount);
	}
	
	public boolean extract(ItemStack stack, LinearColor extractColor, int amount)
	{
		int oldAmount = getAmount(stack);
		LinearColor oldColor = getColor(stack);
		if (amount <= 0)
			return true;
		if (amount >= oldAmount)
		{
			throw new UnsupportedOperationException("Extract operation doesn't support taking all.");
		}
		else
		{
			double newR = (oldColor.r * oldAmount - extractColor.r * amount) / (double) (oldAmount - amount);
			double newG = (oldColor.g * oldAmount - extractColor.g * amount) / (double) (oldAmount - amount);
			double newB = (oldColor.b * oldAmount - extractColor.b * amount) / (double) (oldAmount - amount);
			if (newR < 0 || newG < 0 || newB < 0)
			{
				return false;
			}
			else
			{
				setAmount(stack, oldAmount - amount);
				setColor(stack, LinearColor.fromNormalized(newR, newG, newB));
				return true;
			}
		}
	}
	
	public boolean extract(ItemStack stack, SlimeGirlEntity.ColorVariant extractColor, int amount) 
	{
		return extract(stack, MagicalGelColorUtils.getSlimeColor(extractColor), amount);
	}
	
	public boolean extractClosestSlimeColor(ItemStack stack, int amount)
	{
		return extract(stack, MagicalGelColorUtils.closestVariant(getColor(stack)), amount);
	}
	
	@Override
	public InteractionResult interactLivingEntity(Player player, LivingEntity living, InteractionHand usedHand)
	{
		if (!player.level().isClientSide)
		{
			if (this.getAmount(player.getItemInHand(usedHand)) <= 0)
			{
				player.getItemInHand(usedHand).shrink(1);
				player.spawnAtLocation(NFFGirlsItems.EMPTY_MAGICAL_GEL_BOTTLE.get().getDefaultInstance()).setNoPickUpDelay();//NaUtilsItemStatics.giveOrDropDefault(player, NFFGirlsItems.EMPTY_MAGICAL_GEL_BOTTLE.get());
			}
			// Action type: 0 => no action; 1 => collecting; 2 => staining
			int action = 0;
			// Blend magical slime
			if (living instanceof MagicalSlimeEntity ms && living.getType() == ModEntityTypes.MAGICAL_SLIME.get())
			{
				if (ms.isTiny())
				{
					this.blend(player.getItemInHand(usedHand), MagicalGelColorUtils.getSlimeColor(ms), 1);
					ms.discard();
					action = 1;
				}
			}
			// Blend vanilla slime
			/*else if (living instanceof Slime sl && living.getType() == EntityType.SLIME)
			{
				if (sl.isTiny())
				{
					this.blend(stack, VANILLA_SLIME_COLOR, 1);
					sl.discard();
					action = 1;
				}
			}
			// Blend vanilla magma cube
			else if (living instanceof MagmaCube mc && living.getType() == EntityType.MAGMA_CUBE)
			{
				if (mc.isTiny())
				{
					this.blend(stack, MAGMA_CUBE_COLOR, 1);
					mc.discard();
					action = 1;
				}
			} */
			// Stain slime girl
			else if (living instanceof HmagSlimeGirlEntity sg && sg.isOwnerPresent() && sg.getOwner() == player)
			{
				sg.stain(this.getColor(player.getItemInHand(usedHand)));
				if (sg.getRandom().nextDouble() < 0.25d)
					sg.spawnAtLocation(NFFGirlsItems.MAGICAL_GEL_BALL.get());
				action = 2;
			}
			
			// Handle item change
			if (action == 1)
			{
				// The max volume is 6; if trying adding more, drop a magical gel ball after blending
				while (getAmount(player.getItemInHand(usedHand)) > 6)
				{
					setAmount(player.getItemInHand(usedHand), getAmount(player.getItemInHand(usedHand)) - 1);
					ItemStack ball = new ItemStack(NFFGirlsItems.MAGICAL_GEL_BALL.get());
					if (!player.addItem(ball))
						player.spawnAtLocation(ball);
				}
				ItemStack stack1 = player.getItemInHand(usedHand).copy();
				player.getItemInHand(usedHand).shrink(1);
				player.spawnAtLocation(stack1, 1f).setNoPickUpDelay();
				return InteractionResult.sidedSuccess(living.level().isClientSide);
			}
			else if (action == 2)
			{
				if (!player.isCreative())
				{
					if (getAmount(player.getItemInHand(usedHand)) == 1)
					{
						player.getItemInHand(usedHand).shrink(1);
						ItemStack stack1 = NFFGirlsItems.EMPTY_MAGICAL_GEL_BOTTLE.get().getDefaultInstance();
						player.spawnAtLocation(stack1, 1f).setNoPickUpDelay();
					}
					else
					{
						ItemStack stack1 = player.getItemInHand(usedHand).copy();
						this.setAmount(stack1, this.getAmount(stack1) - 1);
						player.getItemInHand(usedHand).shrink(1);
						player.spawnAtLocation(stack1, 1f).setNoPickUpDelay();
					}
				}
				return InteractionResult.sidedSuccess(living.level().isClientSide);
			}
			
		}
		return InteractionResult.PASS;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag)
	{
		super.appendHoverText(stack, level, list, tooltipFlag);
		list.add(NFUInfoStatics.createTranslatable("item.nffgirls.magical_gel_bottle.amount", Integer.toString(this.getAmount(stack))));
		if (this.getShowColor(stack))
		{
			Vec3i rgb = getColor(stack).toRGB();
			String rgbInfo = " (R" + Integer.toString(rgb.getX()) + ", G" + Integer.toString(rgb.getY()) + ", B" + Integer.toString(rgb.getZ()) +")";
			list.add(NFUInfoStatics.createTranslatable("item.nffgirls.magical_gel_bottle.color")
					.append(HtmlColors.getTranslationKey(HtmlColors.getNearestHtmlColor(this.getColor(stack)))
					.append(NFUInfoStatics.createText(rgbInfo))
					.withStyle(Style.EMPTY.withColor(this.getColor(stack).toCode()))));
		}
		else
		{
			list.add(NFUInfoStatics.createTranslatable("item.nffgirls.magical_gel_bottle.color_unknown"));
			list.add(NFUInfoStatics.createTranslatable("item.nffgirls.magical_gel_bottle.check_color"));
		}
	}
}
