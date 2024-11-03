package net.sodiumzh.nff.girls.entity.tamingprocesses.hmag;

import java.util.Map;
import java.util.function.Supplier;

import net.sodiumzh.nautils.containers.MapPair;
import net.sodiumzh.nautils.math.RandomSelection;
import net.sodiumzh.nautils.statics.NaUtilsMathStatics;
import net.sodiumzh.nautils.statics.NaUtilsContainerStatics;

public class HmagImpTamingProcess extends NFFGirlsItemDroppingTamingProcess
{
	@Override
	public int getHoldingItemTime() {
		return 10 * 20;
	}

}
