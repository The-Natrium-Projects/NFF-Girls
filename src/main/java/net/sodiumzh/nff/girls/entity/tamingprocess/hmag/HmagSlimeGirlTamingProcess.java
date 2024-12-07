package net.sodiumzh.nff.girls.entity.tamingprocess.hmag;

import java.util.HashSet;
import java.util.Random;

import com.github.mechalopa.hmag.registry.ModEntityTypes;
import com.github.mechalopa.hmag.world.entity.MagicalSlimeEntity;
import com.github.mechalopa.hmag.world.entity.SlimeGirlEntity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.sodiumzh.nautils.statics.NaUtilsEntityStatics;
import net.sodiumzh.nautils.statics.NaUtilsReflectionStatics;
import net.sodiumzh.nautils.statics.NaUtilsMathStatics;
import net.sodiumzh.nautils.math.LinearColor;
import net.sodiumzh.nff.girls.item.MagicalGelColorUtils;
import net.sodiumzh.nff.girls.registry.NFFGirlsItems;
import net.sodiumzh.nff.services.entity.taming.TamableHatredReason;
import net.sodiumzh.nff.services.entity.taming.TamableInteractArguments;
import net.sodiumzh.nff.services.entity.taming.TamableInteractionResult;
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
	public HashSet<TamableHatredReason> getAddHatredReasons() {
		HashSet<TamableHatredReason> set = new HashSet<TamableHatredReason>();
		set.add(TamableHatredReason.ATTACKED);
		set.add(TamableHatredReason.HIT);
		return set;
	}
		
	@Override
	public int getHatredDurationTicks(TamableHatredReason reason)
	{
		switch (reason)
		{
		case ATTACKED:
			return 60 * 20;
		case HIT:
			return 15 * 20;
		default:
			return 0;				
		}
	}
	@Override
	protected double getProcValueToAdd(ItemStack item, Player player, Mob mob, double lastProc) {
		if (item.is(NFFGirlsItems.MAGICAL_GEL_BOTTLE.get()) && mob instanceof SlimeGirlEntity sg)
		{
			return getDeltaProc(NFFGirlsItems.MAGICAL_GEL_BOTTLE.get().getColor(item), MagicalGelColorUtils.getSlimeColor(sg))
					* Mth.clamp(rnd.nextGaussian() + 1d, 0.5d, 1.5d);
		}
		else if (item.is(NFFGirlsItems.MAGICAL_GEL_BALL.get()))
		{
			return NaUtilsMathStatics.rndRangedDouble(0.02, 0.05);
		}
			
		else return 0;
	}

	@Override
	public boolean isItemAcceptable(ItemStack item) {
		return item.is(NFFGirlsItems.MAGICAL_GEL_BOTTLE.get())
				|| item.is(NFFGirlsItems.MAGICAL_GEL_BALL.get());
	}

	@Override
	public boolean additionalConditions(Player player, Mob mob) {
		return true;
	}

	@Override
	public int getItemGivingCooldownTicks() {
		return 10 * 20;
	}
	
	@Override
	public void onAddingHatred(Mob mob, Player player, TamableHatredReason reason)
	{}
	
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
	            try
	            {
	            	NaUtilsReflectionStatics.forceInvoke(slime, MagicalSlimeEntity.class, "setSize", 1);
	            }
	            catch (Exception e)
	            {
	            	e.printStackTrace();
	            	NaUtilsReflectionStatics.forceInvoke(slime, Slime.class, "m_7839_", 1);//setSize
	            }
            	LinearColor sgColorCompl = MagicalGelColorUtils.getSlimeColor(sg).getComplementary();
            	SlimeGirlEntity.ColorVariant v = MagicalGelColorUtils.closestVariant(sgColorCompl);
            	slime.setVariant(v.getId());
            	slime.moveTo(mob.getX() + NaUtilsMathStatics.rndRangedDouble(-0.5, 0.5), mob.getY() + 0.5D, mob.getZ() + NaUtilsMathStatics.rndRangedDouble(-0.5, 0.5), rnd.nextFloat() * 360.0F, 0.0F);
            	mob.level.addFreshEntity(slime);
            }
			NaUtilsEntityStatics.sendGlintParticlesToLivingDefault(mob);
		}
	}
	
	@Override
	public void sendParticlesOnItemReceived(Mob target) {}
	
	@Override
	public TamableInteractionResult handleInteract(TamableInteractArguments args)
	{
		if (!args.isClient() && args.isMainHand() && args.getPlayer().getMainHandItem().is(NFFGirlsItems.MAGICAL_GEL_BALL.get()))
		{
			args.getPlayer().getCapability(NFFCapRegistry.CAP_BM_PLAYER).ifPresent((c) -> 
			{
				c.getNbt().putBoolean("magical_gel_ball_no_use", true);
			});
		}
		return super.handleInteract(args);
	}
}
