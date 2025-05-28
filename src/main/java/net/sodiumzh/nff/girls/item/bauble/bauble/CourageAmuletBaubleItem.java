package net.sodiumzh.nff.girls.item.bauble.bauble;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.registry.NFFGirlsConfigs;
import net.sodiumzh.nff.girls.item.bauble.NFFGirlsDedicatedBaubleItem;
import net.sodiumzh.nfu.item.bauble.BaubleAttributeModifier;
import net.sodiumzh.nfu.item.bauble.BaubleProcessingArgs;

public class CourageAmuletBaubleItem extends NFFGirlsDedicatedBaubleItem
{

	public CourageAmuletBaubleItem(int tier, Properties pProperties)
	{
		super(new ResourceLocation(NFFGirls.MOD_ID, "courage_amulet"), tier, pProperties);
	}

	@Override
	public void slotTick(BaubleProcessingArgs args) {
		// TODO Auto-generated method stub

	}
	@Override
	public BaubleAttributeModifier[] getDuplicableModifiers(BaubleProcessingArgs args) {
		switch (this.getTier())
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

}
