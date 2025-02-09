package net.sodiumzh.nff.girls.entity.tamingprocesses.hmag;

import net.sodiumzh.nautils.containers.MapPair;
import net.sodiumzh.nautils.entity.anger.MobAngerRules;
import net.sodiumzh.nautils.math.RndUtil;
import net.sodiumzh.nautils.statics.NaUtilsContainerStatics;
import net.sodiumzh.nff.girls.registry.NFFGirlsAngerRules;

import java.util.Map;
import java.util.function.Supplier;

public class HmagKoboldTamingProcess extends NFFGirlsItemDroppingTamingProcess
{
/*
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Supplier<Double>> getDeltaProcMap() {
		return NaUtilsContainerStatics.<String, Supplier<Double>>mapOf(
				MapPair.of("minecraft:iron_ingot", () -> RndUtil.rndRangedDouble(0.02, 0.04)),
				MapPair.of("minecraft:gold_ingot", () -> RndUtil.rndRangedDouble(0.03, 0.05)),
				MapPair.of("minecraft:emerald", () -> RndUtil.rndRangedDouble(0.03, 0.05)),
				MapPair.of("minecraft:diamond", () -> RndUtil.rndRangedDouble(0.05, 0.10)),
				MapPair.of("minecraft:netherite_scrap", () -> RndUtil.rndRangedDouble(0.15, 0.25)),
				MapPair.of("minecraft:netherite_ingot", () -> RndUtil.rndRangedDouble(0.30, 0.50)),
				MapPair.of("minecraft:iron_pickaxe", () -> RndUtil.rndRangedDouble(0.05, 0.07)),
				MapPair.of("minecraft:golden_pickaxe", () -> RndUtil.rndRangedDouble(0.07, 0.10)),
				MapPair.of("minecraft:diamond_pickaxe", () -> RndUtil.rndRangedDouble(0.12, 0.18)),
				MapPair.of("minecraft:netherite_pickaxe", () -> RndUtil.rndRangedDouble(0.50, 1.00))
				);
	}
*/
	@Override
	public int getHoldingItemTime() {
		return 5 * 20;
	}

	@Override
	public MobAngerRules getAngerRules() {
		return NFFGirlsAngerRules.DEFAULT.get();
	}

}
