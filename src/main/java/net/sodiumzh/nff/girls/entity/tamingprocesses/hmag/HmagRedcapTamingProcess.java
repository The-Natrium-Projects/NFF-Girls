package net.sodiumzh.nff.girls.entity.tamingprocesses.hmag;

import net.sodiumzh.nautils.containers.MapPair;
import net.sodiumzh.nautils.entity.anger.MobAngerRules;
import net.sodiumzh.nautils.math.RndUtil;
import net.sodiumzh.nautils.statics.NaUtilsContainerStatics;
import net.sodiumzh.nff.girls.registry.NFFGirlsAngerRules;

import java.util.Map;
import java.util.function.Supplier;

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
