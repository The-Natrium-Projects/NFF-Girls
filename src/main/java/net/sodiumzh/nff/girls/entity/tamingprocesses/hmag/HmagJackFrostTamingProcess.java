package net.sodiumzh.nff.girls.entity.tamingprocesses.hmag;

import net.sodiumzh.nautils.containers.MapPair;
import net.sodiumzh.nautils.entity.anger.MobAngerRules;
import net.sodiumzh.nautils.math.RndUtil;
import net.sodiumzh.nautils.statics.NaUtilsContainerStatics;
import net.sodiumzh.nff.girls.registry.NFFGirlsAngerRules;

import java.util.Map;
import java.util.function.Supplier;

public class HmagJackFrostTamingProcess extends NFFGirlsItemDroppingTamingProcess
{

	@Override
	public Map<String, Supplier<Double>> getDeltaProcMap() {
		return NaUtilsContainerStatics.<String, Supplier<Double>>mapOf(
				MapPair.of("minecraft:blue_ice", () -> RndUtil.rndRangedDouble(0.03, 0.06)),
				MapPair.of("minecraft:lapis_lazuli", () -> RndUtil.rndRangedDouble(0.03, 0.06)),
				MapPair.of("minecraft:emerald", () -> RndUtil.rndRangedDouble(0.04, 0.08)),
				MapPair.of("minecraft:diamond", () -> RndUtil.rndRangedDouble(0.06, 0.10)),
				MapPair.of("hmag:cureberry", () -> RndUtil.rndRangedDouble(0.08, 0.12)),
				MapPair.of("hmag:randomberry", () -> RndUtil.rndRangedDouble(0.08, 0.12)),
				MapPair.of("hmag:exp_berry", () -> RndUtil.rndRangedDouble(0.08, 0.12)),
				MapPair.of("minecraft:golden_apple", () -> RndUtil.rndRangedDouble(0.10, 0.15)),
				MapPair.of("hmag:golden_tropical_fish", () -> RndUtil.rndRangedDouble(0.10, 0.15)),
				MapPair.of("twilightforest:ice_bomb", () -> RndUtil.rndRangedDouble(0.10, 0.15))
				);
	}

	@Override
	public int getHoldingItemTime() {
		return 5 * 20;
	}

	@Override
	public MobAngerRules getAngerRules() {
		return NFFGirlsAngerRules.DEFAULT.get();
	}
}
