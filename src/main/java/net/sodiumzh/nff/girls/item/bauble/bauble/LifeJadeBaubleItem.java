package net.sodiumzh.nff.girls.item.bauble.bauble;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.item.bauble.INFFGirlsBauble;
import net.sodiumzh.nff.girls.registry.NFFGirlsConfigs;

public class LifeJadeBaubleItem extends NFFGirlsDedicatedBaubleItem
{

	public LifeJadeBaubleItem(int tier, Properties pProperties)
	{
		super(new ResourceLocation(NFFGirls.MOD_ID, "life_jade"), tier, pProperties);
		this.addBaubleTag(INFFGirlsBauble.TAG_ENVIRONMENT_IMMUNITY);
	}

	@Override
	public void slotTick(BaubleProcessingArgs args) {
		switch (this.getTier())
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
	public BaubleAttributeModifier[] getDuplicableModifiers(BaubleProcessingArgs args) {
		switch (this.getTier())
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

}
