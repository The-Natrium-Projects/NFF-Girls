package net.sodiumzh.nff.girls.entity.tamingprocesses.hmag;

import java.util.Map;
import java.util.function.Supplier;

import net.sodiumzh.nautils.containers.MapPair;
import net.sodiumzh.nautils.entity.anger.MobAngerRules;
import net.sodiumzh.nautils.statics.NaUtilsMathStatics;
import net.sodiumzh.nautils.statics.NaUtilsContainerStatics;
import net.sodiumzh.nff.girls.registry.NFFGirlsAngerRules;

import javax.annotation.Nonnull;

public class HmagJackFrostTamingProcess extends NFFGirlsItemDroppingTamingProcess
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
