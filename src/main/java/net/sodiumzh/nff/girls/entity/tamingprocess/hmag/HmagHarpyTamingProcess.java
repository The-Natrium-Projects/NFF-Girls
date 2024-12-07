package net.sodiumzh.nff.girls.entity.tamingprocess.hmag;

import com.github.mechalopa.hmag.world.entity.HarpyEntity;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;

public class HmagHarpyTamingProcess extends HmagSnowCanineTamingProcess
{

	@Override
	public INFFTamed doTaming(Player player, Mob target)
	{
		int variant = -1;
		if (target instanceof HarpyEntity h)
		{
			variant = h.getVariant();
		}
		INFFTamed mob = super.doTaming(player, target);
		return mob;
	}
	
}
