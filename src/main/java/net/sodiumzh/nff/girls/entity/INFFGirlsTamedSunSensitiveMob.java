package net.sodiumzh.nff.girls.entity;

import net.minecraft.world.entity.EquipmentSlot;
import net.sodiumzh.nff.girls.item.bauble.INFFGirlsBauble;
import net.sodiumzh.nff.girls.registry.NFFGirlsItems;
import net.sodiumzh.nff.services.entity.taming.INFFTamedSunSensitiveMob;

public interface INFFGirlsTamedSunSensitiveMob extends INFFGirlsTamed, INFFTamedSunSensitiveMob
{
	@Override
	public default void setupSunImmunityRules()
	{
		this.getSunImmunity().putOptional("sunhat", mob -> mob.getMob().getItemBySlot(EquipmentSlot.HEAD).is(NFFGirlsItems.SUNHAT.get()));
		this.getSunImmunity().putOptional("bauble", mob -> INFFGirlsBauble.isEnvironmentImmunized(mob.getMob()));
	}
}
