package net.sodiumzh.nff.girls.item.bauble.bauble;

import net.minecraft.resources.ResourceLocation;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.item.bauble.NFFGirlsDedicatedBaubleItem;
import net.sodiumzh.nff.girls.registry.NFFGirlsConfigs;
import net.sodiumzh.nff.services.subsystem.baublesystem.BaubleAttributeModifier;
import net.sodiumzh.nff.services.subsystem.baublesystem.BaubleProcessingArgs;

public class HealingJadeBaubleItem extends NFFGirlsDedicatedBaubleItem
{

	public HealingJadeBaubleItem(int tier, Properties pProperties)
	{
		super(new ResourceLocation(NFFGirls.MOD_ID, "healing_jade"), tier, pProperties);
	}

	@Override
	public void slotTick(BaubleProcessingArgs args) {
		args.user().heal(NFFGirlsConfigs.ValueCache.Baubles.BAUBLE_HEALTH_RECOVERY_SCALE * 0.1f / 20f);
	}

	@Override
	public BaubleAttributeModifier[] getDuplicatableModifiers(BaubleProcessingArgs args) {
		return null;
	}

}
