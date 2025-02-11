package net.sodiumzh.nff.girls.entity.tamingprocesses.hmag;

import net.sodiumzh.nautils.entity.anger.MobAngerRules;
import net.sodiumzh.nff.girls.registry.NFFGirlsAngerRules;

public class HmagJackFrostTamingProcess extends NFFGirlsItemDroppingTamingProcess
{

	/*@Override
	public Map<String, Supplier<Double>> getDeltaProcMap() {
		return NaUtilsContainerStatics.<String, Supplier<Double>>mapOf(
				MapPair.of("minecraft:blue_ice", () -> NaUtilsMathStatics.rndRangedDouble(0.03, 0.06)),
				MapPair.of("minecraft:lapis_lazuli", () -> NaUtilsMathStatics.rndRangedDouble(0.03, 0.06)),
				MapPair.of("minecraft:emerald", () -> NaUtilsMathStatics.rndRangedDouble(0.04, 0.08)),
				MapPair.of("minecraft:diamond", () -> NaUtilsMathStatics.rndRangedDouble(0.06, 0.10)),
				MapPair.of("hmag:cureberry", () -> NaUtilsMathStatics.rndRangedDouble(0.08, 0.12)),
				MapPair.of("hmag:randomberry", () -> NaUtilsMathStatics.rndRangedDouble(0.08, 0.12)),
				MapPair.of("hmag:exp_berry", () -> NaUtilsMathStatics.rndRangedDouble(0.08, 0.12)),
				MapPair.of("minecraft:golden_apple", () -> NaUtilsMathStatics.rndRangedDouble(0.10, 0.15)),
				MapPair.of("hmag:golden_tropical_fish", () -> NaUtilsMathStatics.rndRangedDouble(0.10, 0.15)),
				MapPair.of("twilightforest:ice_bomb", () -> NaUtilsMathStatics.rndRangedDouble(0.10, 0.15))
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
