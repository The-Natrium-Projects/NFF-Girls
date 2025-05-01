package net.sodiumzh.nff.girls.entity.tamingprocess.hmag;

import net.sodiumzh.nff.girls.registry.NFFGirlsAngerRules;
import net.sodiumzh.nfu.entity.anger.MobAngerRules;

public class HmagImpTamingProcess extends NFFGirlsItemDroppingTamingProcess
{
	@Override
	public int getHoldingItemTime() {
		return 10 * 20;
	}

	@Override
	public MobAngerRules getAngerRules() {
		return NFFGirlsAngerRules.DEFAULT.get();
	}

}
