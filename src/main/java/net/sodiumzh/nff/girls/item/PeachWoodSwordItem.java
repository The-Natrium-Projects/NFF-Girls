package net.sodiumzh.nff.girls.item;

import com.github.mechalopa.hmag.registry.ModItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;

public class PeachWoodSwordItem extends UnsweepableSwordItem
{

	public PeachWoodSwordItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties)
	{
		super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
	}

	@Override
	public boolean isValidRepairItem(ItemStack pToRepair, ItemStack pRepair)
	{
		return pRepair.is(ModItems.PURIFICATION_CLOTH.get());
	}
	
}
