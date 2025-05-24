package net.sodiumzh.nff.girls.item.bauble.bauble;

import net.minecraft.resources.ResourceLocation;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.item.bauble.NFFGirlsDedicatedBaubleItem;
import net.sodiumzh.nff.girls.registry.NFFGirlsConfigs;
import net.sodiumzh.nfu.item.bauble.BaubleAttributeModifier;
import net.sodiumzh.nfu.item.bauble.BaubleProcessingArgs;

public class HealingJadeBaubleItem extends NFFGirlsDedicatedBaubleItem
{

	public HealingJadeBaubleItem(String additionalKey, int tier, Properties pProperties)
	{
		super(additionalKey, tier, pProperties);
	}

	@Override
	public void slotTick(BaubleProcessingArgs args) {
		args.user().heal(NFFGirlsConfigs.ValueCache.Baubles.BAUBLE_HEALTH_RECOVERY_SCALE * 0.1f / 20f);
	}

	@Override
	public BaubleAttributeModifier[] getDuplicatableModifiers(BaubleProcessingArgs args) {
		return null;
	}

	@Override
	public ResourceLocation getBaubleRegistryKeyUnsuffixed() {
		// TODO Auto-generated method stub
		return new ResourceLocation(NFFGirls.MOD_ID, "healing_jade");
	}

}
