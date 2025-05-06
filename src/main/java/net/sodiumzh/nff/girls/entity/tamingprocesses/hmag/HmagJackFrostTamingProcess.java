package net.sodiumzh.nff.girls.entity.tamingprocesses.hmag;

import net.sodiumzh.nff.girls.registry.NFFGirlsAngerRules;
import net.sodiumzh.nfu.entity.anger.MobAngerRules;

public class HmagJackFrostTamingProcess extends NFFGirlsItemDroppingTamingProcess
{

	/*@Override
	public Map<String, Supplier<Double>> getDeltaProcMap() {
		return NFUContainerStatics.<String, Supplier<Double>>mapOf(
				MapPair.of("minecraft:blue_ice", () -> NFUMathStatics.rndRangedDouble(0.03, 0.06)),
				MapPair.of("minecraft:lapis_lazuli", () -> NFUMathStatics.rndRangedDouble(0.03, 0.06)),
				MapPair.of("minecraft:emerald", () -> NFUMathStatics.rndRangedDouble(0.04, 0.08)),
				MapPair.of("minecraft:diamond", () -> NFUMathStatics.rndRangedDouble(0.06, 0.10)),
				MapPair.of("hmag:cureberry", () -> NFUMathStatics.rndRangedDouble(0.08, 0.12)),
				MapPair.of("hmag:randomberry", () -> NFUMathStatics.rndRangedDouble(0.08, 0.12)),
				MapPair.of("hmag:exp_berry", () -> NFUMathStatics.rndRangedDouble(0.08, 0.12)),
				MapPair.of("minecraft:golden_apple", () -> NFUMathStatics.rndRangedDouble(0.10, 0.15)),
				MapPair.of("hmag:golden_tropical_fish", () -> NFUMathStatics.rndRangedDouble(0.10, 0.15)),
				MapPair.of("twilightforest:ice_bomb", () -> NFUMathStatics.rndRangedDouble(0.10, 0.15))
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
