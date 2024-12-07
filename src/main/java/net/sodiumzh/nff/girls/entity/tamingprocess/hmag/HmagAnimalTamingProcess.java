package net.sodiumzh.nff.girls.entity.tamingprocess.hmag;

import net.minecraft.nbt.DoubleTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.sodiumzh.nautils.statics.NaUtilsEntityStatics;
import net.sodiumzh.nff.services.entity.capability.CNFFTamable;

public class HmagAnimalTamingProcess extends NFFGirlsItemDroppingTamingProcess
{

	@Override
	public void initCap(CNFFTamable cap)
	{
		super.initCap(cap);
		cap.getNbt().put("strength", DoubleTag.valueOf(0));
	}

	@Override
	public int getHoldingItemTime() {
		return 5 * 20;
	}

	@Override
	public void serverTick(Mob mob)
	{
		if (CNFFTamable.getCap(mob) == null)
			return;
		super.serverTick(mob);
		if (CNFFTamable.getCapNbt(mob) == null)
			return;
		if (CNFFTamable.getCapNbt(mob).getDouble("strength") >= 1e-5d)
			NaUtilsEntityStatics.addEffectSafe(mob, new MobEffectInstance(MobEffects.DAMAGE_BOOST, 10, (int)(CNFFTamable.getCapNbt(mob).getDouble("strength") / 0.2)));
		CNFFTamable.getCapNbt(mob).put("strength", DoubleTag.valueOf(Math.max(CNFFTamable.getCapNbt(mob).getDouble("strength") - 5e-5d, 0d)));	// decrease by 0.001 per second
	}

	@Override
	public void onConsumeItem(Mob mob, ItemStack item, double deltaProc)
	{
		CNFFTamable.getCapNbt(mob).put("strength", DoubleTag.valueOf(CNFFTamable.getCapNbt(mob).getDouble("strength") + deltaProc));
	}
	
}
