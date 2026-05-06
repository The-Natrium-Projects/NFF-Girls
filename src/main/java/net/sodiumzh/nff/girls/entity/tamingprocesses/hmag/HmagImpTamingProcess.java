package net.sodiumzh.nff.girls.entity.tamingprocesses.hmag;

import net.sodiumzh.nff.girls.registry.NFFGirlsAngerRules;
import net.sodiumzh.nfu.entity.anger.MobAngerRules;

public class HmagImpTamingProcess extends NFFGirlsItemDroppingTamingProcess
{

/*	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Supplier<Double>> getDeltaProcMap() {
		return NFUContainerStatics.<String, Supplier<Double>>mapOf(
			MapPair.of("minecraft:apple", () -> RndUtil.rndRangedDouble(0.02, 0.04)),
			MapPair.of("minecraft:warped_fungus", () -> RndUtil.rndRangedDouble(0.03, 0.05)), 
			MapPair.of("minecraft:crimson_fungus", () -> RndUtil.rndRangedDouble(0.03, 0.05)), 
			MapPair.of("minecraft:golden_apple", () -> RndUtil.rndRangedDouble(0.12, 0.24)),
			MapPair.of("hmag:lemon", () -> RndUtil.rndRangedDouble(0.03, 0.05)),
			MapPair.of("hmag:golden_tropical_fish", () -> RndUtil.rndRangedDouble(0.08, 0.16)),			
			MapPair.of("minecraft:gilded_blackstone", () -> RndUtil.rndRangedDouble(0.04, 0.07)),
			MapPair.of("minecraft:enchanted_golden_apple", () -> RndUtil.rndRangedDouble(0.50, 1.00)),
			MapPair.of("minecraft:nether_star", () -> RandomSelection.createDouble(0.50d).add(1.01d, 0.20d).getDouble()));
	}*/

	@Override
	public int getHoldingItemTime() {
		return 10 * 20;
	}

}
