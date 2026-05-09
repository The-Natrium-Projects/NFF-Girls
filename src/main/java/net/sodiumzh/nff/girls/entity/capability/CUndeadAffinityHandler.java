package net.sodiumzh.nff.girls.entity.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashSet;
import java.util.UUID;

@Deprecated
public interface CUndeadAffinityHandler extends INBTSerializable<CompoundTag>{

	// Get this mob's neutral list
	public HashSet<UUID> getHatred();
	
	// Add a living entity to the neutral list
	// Forgive after 5 min by default
	public default void addHatred(LivingEntity entity)
	{
		addHatred(entity, 300 * 20);
	}
	
	/**
	 *  Add a living entity to the neutral list
	 * Param forgiveTime in tick, -1 means never forgive
	 * */
	public void addHatred(LivingEntity entity, int forgiveTime);

	public void updateForgivingTimers();
	
}
