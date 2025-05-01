package net.sodiumzh.nff.girls.entity.tamingprocess.hmag;

import com.github.mechalopa.hmag.world.entity.HarpyEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.sodiumzh.nff.girls.entity.hmag.HmagHarpyEntity;

public class HmagHarpyTamingProcess extends HmagAnimalTamingProcess
{

	@Override
	public Mob doTaming(Player player, Mob target)
	{
		HarpyEntity.Variant variant = HarpyEntity.Variant.byId(0);
		if (target instanceof HarpyEntity h)
		{
			variant = h.getVariant();
		}
		Mob mob = super.doTaming(player, target);
		if (mob instanceof HmagHarpyEntity h && variant.getId() >= 0)
		{
			h.setVariant(variant);
		}
		return mob;
	}
	
}
