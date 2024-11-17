package net.sodiumzh.nff.girls.entity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.registry.NFFGirlsItems;
import net.sodiumzh.nff.girls.subsystem.baublesystem.NFFGirlsBaubleStatics;
import net.sodiumzh.nff.services.entity.taming.INFFTamedSunSensitiveMob;

public interface INFFGirlsTamedSunSensitiveMob extends INFFGirlsTamed, INFFTamedSunSensitiveMob
{
	@Override
	public default void setupSunImmunityRules()
	{
		this.getSunImmunity().putOptional("sunhat", mob -> mob.getMob().getItemBySlot(EquipmentSlot.HEAD).is(NFFGirlsItems.SUNHAT.get()));
		this.getSunImmunity().putOptional("soul_amulet", mob -> NFFGirlsBaubleStatics.countBaubles(asMob(), new ResourceLocation(NFFGirls.MOD_ID, "soul_amulet")) > 0);
		this.getSunImmunity().putOptional("resis_amulet", mob -> NFFGirlsBaubleStatics.countBaubles(asMob(), new ResourceLocation(NFFGirls.MOD_ID, "resistance_amulet")) > 0);
	}
}
