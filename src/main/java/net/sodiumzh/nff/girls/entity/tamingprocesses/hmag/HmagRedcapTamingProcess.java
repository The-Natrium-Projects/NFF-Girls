package net.sodiumzh.nff.girls.entity.tamingprocesses.hmag;

import net.sodiumzh.nautils.entity.anger.MobAngerRules;
import net.sodiumzh.nff.girls.registry.NFFGirlsAngerRules;

public class HmagRedcapTamingProcess extends NFFGirlsItemDroppingTamingProcess
{

	@Override
	public int getHoldingItemTime() {
		return 5 * 20;
	}

	@Override
	public MobAngerRules getAngerRules() {
		return NFFGirlsAngerRules.DEFAULT.get();
	}
}
