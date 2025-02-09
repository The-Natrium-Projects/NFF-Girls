package net.sodiumzh.nff.girls.entity.tamingprocesses.hmag;

import java.util.Random;

import com.github.mechalopa.hmag.registry.ModEntityTypes;
import com.github.mechalopa.hmag.world.entity.MagicalSlimeEntity;
import com.github.mechalopa.hmag.world.entity.SlimeGirlEntity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.sodiumzh.nautils.entity.anger.MobAngerRules;
import net.sodiumzh.nautils.entity.taming.TamingInteractionResult;
import net.sodiumzh.nautils.math.LinearColor;
import net.sodiumzh.nautils.math.RndUtil;
import net.sodiumzh.nautils.statics.NaUtilsEntityStatics;
import net.sodiumzh.nff.girls.entity.NFFGirlsTamingRules;
import net.sodiumzh.nff.girls.item.MagicalGelColorUtils;
import net.sodiumzh.nff.girls.registry.NFFGirlsAngerRules;
import net.sodiumzh.nff.girls.registry.NFFGirlsItems;
import net.sodiumzh.nff.services.entity.taming.CNFFTamable;
import net.sodiumzh.nff.services.entity.taming.TamingProcessItemGivingProgress;
import net.sodiumzh.nff.services.registry.NFFCapRegistry;

public class HmagSlimeGirlTamingProcess extends TamingProcessItemGivingProgress
{

	protected static Random rnd = new Random();
	
	protected double getDeltaProc(LinearColor color1, LinearColor color2)
	{
		double colorDist = color1.toNormalized().distanceTo(color2.toNormalized()) / Math.sqrt(3d);
		double res = 0;
		if (colorDist < 0.3d)
		{
			res = (0.5d - colorDist) / 0.5d;
			res = res * res;
			res = res * 0.5d;	// 0 ~ 0.5
		}
		else res = (0.5d - colorDist);	// -0.5 ~ 0
		return res;
	}
	
	@Override
	public MobAngerRules getAngerRules() {
		return NFFGirlsAngerRules.DEFAULT.get();
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
			double delta = getDeltaProc(NFFGirlsItems.MAGICAL_GEL_BOTTLE.get().getColor(itemGivenCopy), MagicalGelColorUtils.getSlimeColor((SlimeGirlEntity)mob));
			if (delta > 0)
			{
				// 20 glints at most
				int amount = (int) (delta / 0.0125d) + 1;
				amount = Mth.clamp(amount, 1, 40);
				NaUtilsEntityStatics.sendParticlesToEntity(mob, ParticleTypes.HAPPY_VILLAGER, mob.getBbHeight() - 0.2, 0.5d, amount, 1d);
			}
			else
			{
				int amount = (int) ((-delta) / 0.1d) + 1;
				amount = Mth.clamp(amount, 1, 5);
				NaUtilsEntityStatics.sendParticlesToEntity(mob, ParticleTypes.ANGRY_VILLAGER, mob.getBbHeight() - 0.2, 0.3d, amount, 1d);
			}
		}
		else if (itemGivenCopy.is(NFFGirlsItems.MAGICAL_GEL_BALL.get()))
		{
			if (mob.getType() == ModEntityTypes.SLIME_GIRL.get() && mob instanceof SlimeGirlEntity sg && rnd.nextDouble() < 0.25d)
            {
	            MagicalSlimeEntity slime = ModEntityTypes.MAGICAL_SLIME.get().create(mob.level);
	            slime.setSize(1, true);
            	LinearColor sgColorCompl = MagicalGelColorUtils.getSlimeColor(sg).getComplementary();
            	SlimeGirlEntity.ColorVariant v = MagicalGelColorUtils.closestVariant(sgColorCompl);
            	slime.setVariant(v.getId());
            	slime.moveTo(mob.getX() + RndUtil.rndRangedDouble(-0.5, 0.5), mob.getY() + 0.5D, mob.getZ() + RndUtil.rndRangedDouble(-0.5, 0.5), rnd.nextFloat() * 360.0F, 0.0F);
            	mob.level.addFreshEntity(slime);
            }
			NaUtilsEntityStatics.sendGlintParticlesToLivingDefault(mob);
		}
	}
	
	@Override
	public void sendParticlesOnItemReceived(Mob target) {}
	
	@Override
	public TamingInteractionResult handleInteract(Player player, Mob mob, InteractionHand hand)
	{
		if (!player.level.isClientSide() && hand.equals(InteractionHand.MAIN_HAND)
				&& player.getMainHandItem().is(NFFGirlsItems.MAGICAL_GEL_BALL.get()))
		{
			player.getCapability(NFFCapRegistry.CAP_BM_PLAYER).ifPresent((c) ->
			{
				c.getNbt().putBoolean("magical_gel_ball_no_use", true);
			});
		}
		return super.handleInteract(player, mob, hand);
	}

	@Override
	public void tamableInit(CNFFTamable cnffTamable) {

	}
}
