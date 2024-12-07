package net.sodiumzh.nff.girls.entity.tamingprocesses.hmag;

import java.util.Map;
import java.util.function.Supplier;

import net.minecraft.nbt.DoubleTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.sodiumzh.nautils.statics.NaUtilsEntityStatics;
import net.sodiumzh.nautils.statics.NaUtilsContainerStatics;
import net.sodiumzh.nautils.statics.NaUtilsMathStatics;
import net.sodiumzh.nautils.containers.MapPair;
import net.sodiumzh.nff.services.entity.capability.CNFFTamable;

public class HmagAnimalTamingProcess extends NFFGirlsItemDroppingTamingProcess
{

	@Override
	public void initCap(CNFFTamable cap)
	{
		super.initCap(cap);
		cap.getNbt().put("strength", DoubleTag.valueOf(0));
	}

	/*
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Supplier<Double>> getDeltaProcMap() {
		return NaUtilsContainerStatics.<String, Supplier<Double>>mapOf(
				MapPair.of("minecraft:cooked_chicken", () -> NaUtilsMathStatics.rndRangedDouble(0.04, 0.06)),
				MapPair.of("minecraft:cooked_rabbit", () -> NaUtilsMathStatics.rndRangedDouble(0.04, 0.06)),
				MapPair.of("minecraft:cooked_mutton", () -> NaUtilsMathStatics.rndRangedDouble(0.04, 0.06)),
				MapPair.of("minecraft:cooked_porkchop", () -> NaUtilsMathStatics.rndRangedDouble(0.05, 0.07)),
				MapPair.of("minecraft:cooked_beef", () -> NaUtilsMathStatics.rndRangedDouble(0.05, 0.07)),
				MapPair.of("hmag:golden_tropical_fish", () -> NaUtilsMathStatics.rndRangedDouble(0.07, 0.10)),
				MapPair.of("hmag:cooked_ravager_meat", () -> NaUtilsMathStatics.rndRangedDouble(0.08, 0.12)),
				MapPair.of("minecraft:golden_apple", () -> NaUtilsMathStatics.rndRangedDouble(0.12, 0.16)),
				MapPair.of("minecraft:enchanted_golden_apple", () -> NaUtilsMathStatics.rndRangedDouble(0.40, 0.70)));
	}
*/
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
