package net.sodiumzh.nff.girls.entity.capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.sodiumzh.nff.girls.registry.NFFGirlsCapabilities;

public class CUndeadAffinityHandlerProvider implements ICapabilitySerializable<CompoundTag>
{

	private CUndeadAffinityHandler capability = new CUndeadAffinityHandlerImpl();
	
	/* Interface implementations */
	
	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) 
	{
		if(cap == NFFGirlsCapabilities.CAP_UNDEAD_AFFINITY_HANDLER)
			return LazyOptional.of(() -> {return capability;}).cast();
		else
			return LazyOptional.empty();
	}

	@Override
	public CompoundTag serializeNBT() 
	{
		return capability.serializeNBT();
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) 
	{
		capability.deserializeNBT(nbt);
	}

}
