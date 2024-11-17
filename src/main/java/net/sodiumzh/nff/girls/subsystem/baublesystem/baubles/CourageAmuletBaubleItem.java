package net.sodiumzh.nff.girls.subsystem.baublesystem.baubles;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.registry.NFFGirlsConfigs;
import net.sodiumzh.nff.girls.subsystem.baublesystem.NFFGirlsDedicatedBaubleItem;
import net.sodiumzh.nff.services.subsystem.baublesystem.BaubleAttributeModifier;
import net.sodiumzh.nff.services.subsystem.baublesystem.BaubleProcessingArgs;

public class CourageAmuletBaubleItem extends NFFGirlsDedicatedBaubleItem
{

	public CourageAmuletBaubleItem(String additionalKey, int tier, Properties pProperties)
	{
		super(additionalKey, tier, pProperties);
	}

	@Override
	public void slotTick(BaubleProcessingArgs args) {
		// TODO Auto-generated method stub

	}
	@Override
	public BaubleAttributeModifier[] getDuplicatableModifiers(BaubleProcessingArgs args) {
		switch (this.tier)
		{
		case 1:
		{
			return BaubleAttributeModifier.makeModifiers(
					Attributes.ATTACK_DAMAGE, 4d * NFFGirlsConfigs.ValueCache.Baubles.BAUBLE_ATK_BOOSTING_SCALE,
					Attributes.MOVEMENT_SPEED, 0.2d, "mb");
		}
		case 2:
		{
			return BaubleAttributeModifier.makeModifiers(
					Attributes.ATTACK_DAMAGE, 6d * NFFGirlsConfigs.ValueCache.Baubles.BAUBLE_ATK_BOOSTING_SCALE,
					Attributes.MOVEMENT_SPEED, 0.3d, "mb");
		}
		default:
		{
			throw this.unsupportedTier();
		}
		}
	}

	@Override
	public ResourceLocation getBaubleRegistryKeyUnsuffixed() {
		return new ResourceLocation(NFFGirls.MOD_ID, "courage_amulet");
	}

}
