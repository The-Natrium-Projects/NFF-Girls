package net.sodiumzh.nff.girls.entity.tamingprocesses.hmag;

import java.util.Map;
import java.util.function.Supplier;

import net.sodiumzh.nautils.containers.MapPair;
import net.sodiumzh.nautils.math.RandomSelection;
import net.sodiumzh.nautils.statics.NaUtilsMathStatics;
import net.sodiumzh.nautils.statics.NaUtilsContainerStatics;

public class HmagImpTamingProcess extends NFFGirlsItemDroppingTamingProcess
{

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Supplier<Double>> getDeltaProc() {
		return NaUtilsContainerStatics.<String, Supplier<Double>>mapOf(
			MapPair.of("minecraft:apple", () -> NaUtilsMathStatics.rndRangedDouble(0.02, 0.04)),
			MapPair.of("minecraft:warped_fungus", () -> NaUtilsMathStatics.rndRangedDouble(0.03, 0.05)), 
			MapPair.of("minecraft:crimson_fungus", () -> NaUtilsMathStatics.rndRangedDouble(0.03, 0.05)), 
			MapPair.of("minecraft:golden_apple", () -> NaUtilsMathStatics.rndRangedDouble(0.12, 0.24)),
			MapPair.of("hmag:lemon", () -> NaUtilsMathStatics.rndRangedDouble(0.03, 0.05)),
			MapPair.of("hmag:golden_tropical_fish", () -> NaUtilsMathStatics.rndRangedDouble(0.08, 0.16)),			
			MapPair.of("minecraft:gilded_blackstone", () -> NaUtilsMathStatics.rndRangedDouble(0.04, 0.07)),
			MapPair.of("minecraft:enchanted_golden_apple", () -> NaUtilsMathStatics.rndRangedDouble(0.50, 1.00)),
			MapPair.of("minecraft:nether_star", () -> RandomSelection.createDouble(0.50d).add(1.01d, 0.20d).getDouble()));
	}

	@Override
	public int getHoldingItemTime() {
		return 10 * 20;
	}

}
