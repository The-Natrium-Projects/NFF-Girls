package net.sodiumzh.nff.girls.item.bauble.bauble;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.BlockItem;
import net.sodiumzh.nfu.item.bauble.BaubleAttributeModifier;
import net.sodiumzh.nfu.item.bauble.BaubleBehavior;
import net.sodiumzh.nfu.item.bauble.BaubleEquippingCondition;
import net.sodiumzh.nfu.item.bauble.BaubleProcessingArgs;

public class EnderManHandBlockBaubleBehavior extends BaubleBehavior
{

	public EnderManHandBlockBaubleBehavior(ResourceLocation key)
	{
		super((item, itemstack) -> (item instanceof BlockItem), key, BaubleEquippingCondition.of(
				args -> args.slotKey().equals("enderman_hand_block")));
	}
	
	@Override
	public BaubleAttributeModifier[] getRepeatableModifiers(BaubleProcessingArgs arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaubleAttributeModifier[] getUnrepeatableModifiers(Mob arg0) {
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
