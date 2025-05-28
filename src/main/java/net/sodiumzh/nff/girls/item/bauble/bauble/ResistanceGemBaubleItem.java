package net.sodiumzh.nff.girls.item.bauble.bauble;

import net.minecraft.resources.ResourceLocation;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.item.bauble.INFFGirlsBauble;
import net.sodiumzh.nff.girls.item.bauble.NFFGirlsDedicatedBaubleItem;
import net.sodiumzh.nff.girls.registry.NFFGirlsConfigs;
import net.sodiumzh.nfu.item.bauble.BaubleAttributeModifier;
import net.sodiumzh.nfu.item.bauble.BaubleProcessingArgs;

public class ResistanceGemBaubleItem extends NFFGirlsDedicatedBaubleItem
{

	public ResistanceGemBaubleItem(int tier, Properties pProperties)
	{
		super(new ResourceLocation(NFFGirls.MOD_ID, "resistance_gem"), tier, pProperties);
		this.addBaubleTag(INFFGirlsBauble.TAG_ENVIRONMENT_IMMUNITY);
	}

	@Override
	public void slotTick(BaubleProcessingArgs args) {
	}

	@Override
	public BaubleAttributeModifier[] getDuplicableModifiers(BaubleProcessingArgs args) {
		return null;
	}

}
