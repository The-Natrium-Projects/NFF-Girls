package net.sodiumzh.nff.girls.entity.tamingprocesses.hmag;

import java.util.Map;
import java.util.function.Supplier;

import net.sodiumzh.nautils.containers.MapPair;
import net.sodiumzh.nautils.statics.NaUtilsMathStatics;
import net.sodiumzh.nautils.statics.NaUtilsContainerStatics;

public class HmagKoboldTamingProcess extends NFFGirlsItemDroppingTamingProcess
{

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Supplier<Double>> getDeltaProc() {
		return NaUtilsContainerStatics.<String, Supplier<Double>>mapOf(
				MapPair.of("minecraft:iron_ingot", () -> NaUtilsMathStatics.rndRangedDouble(0.02, 0.04)),
				MapPair.of("minecraft:gold_ingot", () -> NaUtilsMathStatics.rndRangedDouble(0.03, 0.05)),
				MapPair.of("minecraft:emerald", () -> NaUtilsMathStatics.rndRangedDouble(0.03, 0.05)),
				MapPair.of("minecraft:diamond", () -> NaUtilsMathStatics.rndRangedDouble(0.05, 0.10)),
				MapPair.of("minecraft:netherite_scrap", () -> NaUtilsMathStatics.rndRangedDouble(0.15, 0.25)),
				MapPair.of("minecraft:netherite_ingot", () -> NaUtilsMathStatics.rndRangedDouble(0.30, 0.50)),
				MapPair.of("minecraft:iron_pickaxe", () -> NaUtilsMathStatics.rndRangedDouble(0.05, 0.07)),
				MapPair.of("minecraft:golden_pickaxe", () -> NaUtilsMathStatics.rndRangedDouble(0.07, 0.10)),
				MapPair.of("minecraft:diamond_pickaxe", () -> NaUtilsMathStatics.rndRangedDouble(0.12, 0.18)),
				MapPair.of("minecraft:netherite_pickaxe", () -> NaUtilsMathStatics.rndRangedDouble(0.50, 1.00))
				);
	}

	@Override
	public int getHoldingItemTime() {
		return 5 * 20;
	}

}
