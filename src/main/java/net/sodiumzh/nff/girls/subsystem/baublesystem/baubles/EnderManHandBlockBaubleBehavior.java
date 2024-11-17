package net.sodiumzh.nff.girls.subsystem.baublesystem.baubles;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.BlockItem;
import net.sodiumzh.nff.services.subsystems.baublesystem.BaubleAttributeModifier;
import net.sodiumzh.nff.services.subsystems.baublesystem.BaubleBehavior;
import net.sodiumzh.nff.services.subsystems.baublesystem.BaubleEquippingCondition;
import net.sodiumzh.nff.services.subsystems.baublesystem.BaubleProcessingArgs;

public class EnderManHandBlockBaubleBehavior extends BaubleBehavior
{

	public EnderManHandBlockBaubleBehavior(ResourceLocation key)
	{
		super((item, itemstack) -> (item instanceof BlockItem), key, BaubleEquippingCondition.of(
				args -> args.slotKey().equals("enderman_hand_block")));
	}
	
	@Override
	public BaubleAttributeModifier[] getDuplicatableModifiers(BaubleProcessingArgs arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaubleAttributeModifier[] getNonDuplicatableModifiers(Mob arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onEquipped(BaubleProcessingArgs arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSlotTick(BaubleProcessingArgs arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void preSlotTick(BaubleProcessingArgs arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void slotTick(BaubleProcessingArgs arg0) {
		// TODO Auto-generated method stub

	}

}
