package net.sodiumzh.nff.girls.item.bauble.bauble;

import net.minecraft.resources.ResourceLocation;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.registry.NFFGirlsTags;
import net.sodiumzh.nff.girls.item.bauble.NFFGirlsDedicatedBaubleItem;
import net.sodiumzh.nfu.item.bauble.BaubleAttributeModifier;
import net.sodiumzh.nfu.item.bauble.BaubleEquippingCondition;
import net.sodiumzh.nfu.item.bauble.BaubleProcessingArgs;

public class PoisonousThornBaubleItem extends NFFGirlsDedicatedBaubleItem
{

	public PoisonousThornBaubleItem(String additionalKey, int tier, Properties pProperties)
	{
		super(additionalKey, tier, pProperties);
	}

	@Override
	public void slotTick(BaubleProcessingArgs args) {
	}

	@Override
	public BaubleAttributeModifier[] getDuplicableModifiers(BaubleProcessingArgs args) {
		return null;
	}

	@Override
	public ResourceLocation getBaubleRegistryKeyUnsuffixed() {
		return new ResourceLocation(NFFGirls.MOD_ID, "poisonous_thorn");
	}

	@Override
	public BaubleEquippingCondition getEquippingCondition()
	{
		return BaubleEquippingCondition.of(args -> args.user().getType().is(NFFGirlsTags.CAN_EQUIP_POISONOUS_THORN));
	}

}
