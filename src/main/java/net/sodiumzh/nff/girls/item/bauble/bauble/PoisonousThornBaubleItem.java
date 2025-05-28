package net.sodiumzh.nff.girls.item.bauble.bauble;

import net.minecraft.resources.ResourceLocation;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.item.bauble.NFFGirlsDedicatedBaubleItem;
import net.sodiumzh.nff.girls.registry.NFFGirlsTags;
import net.sodiumzh.nff.services.subsystem.baublesystem.BaubleAttributeModifier;
import net.sodiumzh.nff.services.subsystem.baublesystem.BaubleEquippingCondition;
import net.sodiumzh.nff.services.subsystem.baublesystem.BaubleProcessingArgs;

public class PoisonousThornBaubleItem extends NFFGirlsDedicatedBaubleItem
{

	public PoisonousThornBaubleItem(int tier, Properties pProperties)
	{
		super(new ResourceLocation(NFFGirls.MOD_ID, "poisonous_thorn"), tier, pProperties);
	}

	@Override
	public void slotTick(BaubleProcessingArgs args) {
	}

	@Override
	public BaubleAttributeModifier[] getDuplicatableModifiers(BaubleProcessingArgs args) {
		return null;
	}

	@Override
	public BaubleEquippingCondition getEquippingCondition()
	{
		return BaubleEquippingCondition.of(args -> args.user().getType().is(NFFGirlsTags.CAN_EQUIP_POISONOUS_THORN));
	}

}
