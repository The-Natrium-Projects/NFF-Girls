package net.sodiumzh.nff.girls.entity.tamingprocess;

import com.github.mechalopa.hmag.registry.ModEntityTypes;
import com.github.mechalopa.hmag.world.entity.MagicalSlimeEntity;
import com.github.mechalopa.hmag.world.entity.SlimeGirlEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.sodiumzh.nff.girls.entity.NFFGirlsTamingRules;
import net.sodiumzh.nff.girls.item.MagicalGelBottleItem;
import net.sodiumzh.nff.girls.item.MagicalGelColorUtils;
import net.sodiumzh.nff.girls.registry.NFFGirlsItems;
import net.sodiumzh.nff.services.entity.taming.NFFTamableComponent;
import net.sodiumzh.nff.services.entity.taming.TamingProcessItemGivingProgress;
import net.sodiumzh.nfu.entity.taming.TamingInteractionResult;
import net.sodiumzh.nfu.math.LinearColor;
import net.sodiumzh.nfu.util.NFUMathStatics;
import net.sodiumzh.nfu.util.NFUParticleStatics;
import net.sodiumzh.nfu.util.NFUReflectionStatics;

import java.util.Random;

public class HmagSlimeGirlTamingProcess extends TamingProcessItemGivingProgress
{

	protected static final Random RND = new Random();
	
	protected double getColorDeltaProgress(LinearColor color1, LinearColor color2)
	{
		double colorDist = color1.toNormalized().distanceTo(color2.toNormalized()) / Math.sqrt(3d);
		double res = 0;
		if (colorDist < 0.5d)
		{
			// Close: 0-1, squared to make it more beneficial to make it very close
			res = (0.5d - colorDist) / 0.5d;
			res = res * res;
		}
		else res = (0.5d - colorDist) / 2d;	// Far : -0.25 ~ 0
		return res;
	}

	@Override
	public boolean additionalConditions(Player player, Mob mob) {
		return true;
	}

	@Override
	public int getItemGivingCooldownTicks() {
		return NFFGirlsTamingRules.COOLDOWN_MIDDLE;
	}

	@Override
	public ItemStack getReturnedItem(Player player, Mob mob, ItemStack itemGivenCopy, double procBefore, double procAfter)
	{
		if (itemGivenCopy.is(NFFGirlsItems.MAGICAL_GEL_BOTTLE.get()))
		{
			ItemStack cpy = itemGivenCopy.copy();
			// Don't consume if no change
			if (Math.abs(procAfter - procBefore) < 1e-8d)
				return cpy;
			if (NFFGirlsItems.MAGICAL_GEL_BOTTLE.get().getAmount(cpy) == 1)
				return NFFGirlsItems.EMPTY_MAGICAL_GEL_BOTTLE.get().getDefaultInstance();
			else
			{
				NFFGirlsItems.MAGICAL_GEL_BOTTLE.get().setAmount(cpy, NFFGirlsItems.MAGICAL_GEL_BOTTLE.get().getAmount(itemGivenCopy) - 1);
				return cpy;
			}
		}
		else return ItemStack.EMPTY;
	}
	
	@Override
	public void onItemGiven(Player player, Mob mob, ItemStack itemGivenCopy, double procBefore, double procAfter) 
	{
		if (itemGivenCopy.is(NFFGirlsItems.MAGICAL_GEL_BOTTLE.get()))
		{
			double delta = getColorDeltaProgress(NFFGirlsItems.MAGICAL_GEL_BOTTLE.get().getColor(itemGivenCopy), MagicalGelColorUtils.getSlimeColor((SlimeGirlEntity)mob));
			if (delta > 0)
			{
				// 20 glints at most
				int amount = (int) (delta / 0.025d) + 1;
				amount = Mth.clamp(amount, 1, 40);
				NFUParticleStatics.sendParticlesToEntity(mob, ParticleTypes.HAPPY_VILLAGER, mob.getBbHeight() - 0.2, 0.5d, amount, 1d);
			}
			else
			{
				int amount = (int) ((-delta) / 0.05d) + 1;
				amount = Mth.clamp(amount, 1, 5);
				NFUParticleStatics.sendParticlesToEntity(mob, ParticleTypes.ANGRY_VILLAGER, mob.getBbHeight() - 0.2, 0.3d, amount, 1d);
			}
		}
		else if (itemGivenCopy.is(NFFGirlsItems.MAGICAL_GEL_BALL.get()))
		{
			if (mob.getType() == ModEntityTypes.SLIME_GIRL.get() && mob instanceof SlimeGirlEntity sg && RND.nextDouble() < 0.25d)
            {
	            MagicalSlimeEntity slime = ModEntityTypes.MAGICAL_SLIME.get().create(mob.level);
	            try
	            {
	            	NFUReflectionStatics.forceInvoke(slime, MagicalSlimeEntity.class, "setSize", 1);
	            }
	            catch (Exception e)
	            {
	            	e.printStackTrace();
	            	NFUReflectionStatics.forceInvoke(slime, Slime.class, "m_7839_", 1);//setSize
	            }
            	LinearColor sgColorCompl = MagicalGelColorUtils.getSlimeColor(sg).getComplementary();
            	SlimeGirlEntity.ColorVariant v = MagicalGelColorUtils.closestVariant(sgColorCompl);
            	slime.setVariant(v.getId());
            	slime.moveTo(mob.getX() + NFUMathStatics.rndRangedDouble(-0.5, 0.5), mob.getY() + 0.5D, mob.getZ() + NFUMathStatics.rndRangedDouble(-0.5, 0.5), RND.nextFloat() * 360.0F, 0.0F);
            	mob.getLevel().addFreshEntity(slime);
            }
			NFUParticleStatics.sendGlintParticlesToEntityDefault(mob);
		}
	}
	
	@Override
	public void sendParticlesOnItemReceived(Mob target) {}
	
	@Override
	public TamingInteractionResult handleInteract(Player player, Mob mob, InteractionHand hand)
	{
		if (!player.getLevel().isClientSide() && hand.equals(InteractionHand.MAIN_HAND)
				&& player.getMainHandItem().is(NFFGirlsItems.MAGICAL_GEL_BALL.get()))
		{
			/*player.getCapability(NFFCapRegistry.CAP_BM_PLAYER).ifPresent((c) ->
			{
				c.getNbt().putBoolean("magical_gel_ball_no_use", true);
			});*/
		}
		return super.handleInteract(player, mob, hand);
	}

	@Override
	public void tamableInit(NFFTamableComponent NFFTamableComponent) {

	}

	@Override
	public boolean isItemAcceptable(ItemStack itemstack) {
		return itemstack.is(NFFGirlsItems.MAGICAL_GEL_BOTTLE.get()) || itemstack.is(NFFGirlsItems.MAGICAL_GEL_BALL.get());
	}

	@Override
	protected double getProgressToAdd(ItemStack item, Player player, Mob mob, double oldProc) {
		if (item.is(NFFGirlsItems.MAGICAL_GEL_BOTTLE.get())
				&& item.getItem() instanceof MagicalGelBottleItem bottle
				&& mob instanceof SlimeGirlEntity slime)
		{
			return this.getColorDeltaProgress(bottle.getColor(item), MagicalGelColorUtils.getSlimeColor(slime));
		}
		else if (item.is(NFFGirlsItems.MAGICAL_GEL_BALL.get()))
			return NFUMathStatics.rndRangedDouble(0.04d, 0.08d);
		else return 0d;
	}
}
