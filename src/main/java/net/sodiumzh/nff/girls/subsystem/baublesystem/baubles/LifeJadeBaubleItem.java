package net.sodiumzh.nff.girls.subsystem.baublesystem.baubles;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.registry.NFFGirlsConfigs;
import net.sodiumzh.nff.girls.subsystem.baublesystem.NFFGirlsDedicatedBaubleItem;
import net.sodiumzh.nff.services.subsystem.baublesystem.BaubleAttributeModifier;
import net.sodiumzh.nff.services.subsystem.baublesystem.BaubleProcessingArgs;

public class LifeJadeBaubleItem extends NFFGirlsDedicatedBaubleItem
{

	public LifeJadeBaubleItem(String additionalKey, int tier, Properties pProperties)
	{
		super(additionalKey, tier, pProperties);
	}

	@Override
	public void slotTick(BaubleProcessingArgs args) {
		switch (this.tier)
		{
		case 1:
		{
			args.user().heal(NFFGirlsConfigs.ValueCache.Baubles.BAUBLE_HEALTH_RECOVERY_SCALE * 0.15f / 20f);
			break;
		}
		case 2:
		{
			args.user().heal(NFFGirlsConfigs.ValueCache.Baubles.BAUBLE_HEALTH_RECOVERY_SCALE * 0.2f / 20f);
			break;
		}
		default:
		{
			throw this.unsupportedTier();
		}
		}
	}

	@Override
	public BaubleAttributeModifier[] getDuplicatableModifiers(BaubleProcessingArgs args) {
		switch (this.tier)
		{
		case 1:
		{
			return BaubleAttributeModifier.makeModifiers(Attributes.MAX_HEALTH, 5d * NFFGirlsConfigs.ValueCache.Baubles.BAUBLE_MAX_HP_BOOSTING_SCALE);
		}
		case 2:
		{
			return BaubleAttributeModifier.makeModifiers(Attributes.MAX_HEALTH, 10d * NFFGirlsConfigs.ValueCache.Baubles.BAUBLE_MAX_HP_BOOSTING_SCALE);
		}
		default:
		{
			throw this.unsupportedTier();
		}
		}
		
	}

	@Override
	public ResourceLocation getBaubleRegistryKeyUnsuffixed() {
		// TODO Auto-generated method stub
		return new ResourceLocation(NFFGirls.MOD_ID, "life_jade");
	}

}
