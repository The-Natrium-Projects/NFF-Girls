package net.sodiumzh.nff.girls.item.bauble.bauble;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.entity.hmag.HmagDrownedGirlEntity;
import net.sodiumzh.nff.girls.registry.NFFGirlsConfigs;
import net.sodiumzh.nff.girls.item.bauble.NFFGirlsDedicatedBaubleItem;
import net.sodiumzh.nfu.item.bauble.BaubleAttributeModifier;
import net.sodiumzh.nfu.item.bauble.BaubleEquippingCondition;
import net.sodiumzh.nfu.item.bauble.BaubleProcessingArgs;

public class AquaJadeBaubleItem extends NFFGirlsDedicatedBaubleItem
{

	public AquaJadeBaubleItem(String additionalKey, int tier, Item.Properties pProperties)
	{
		super(additionalKey, tier, pProperties);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void slotTick(BaubleProcessingArgs args) {
		if (args.user().isInWater())
			args.user().heal(NFFGirlsConfigs.ValueCache.Baubles.BAUBLE_HEALTH_RECOVERY_SCALE * 0.25f / 20f);
	}

	@Override
	public BaubleAttributeModifier[] getDuplicableModifiers(BaubleProcessingArgs args) {
		return new BaubleAttributeModifier[] {
				new BaubleAttributeModifier(Attributes.MOVEMENT_SPEED, 3.0d, AttributeModifier.Operation.MULTIPLY_BASE)
					.setAdditionalCondition(a -> a.user().isInWater())
				};
	}

	@Override
	public ResourceLocation getBaubleRegistryKeyUnsuffixed() {
		return new ResourceLocation(NFFGirls.MOD_ID, "aqua_jade");
	}

	@Override
	public BaubleEquippingCondition getEquippingCondition()
	{
		return BaubleEquippingCondition.of(args -> (args.user() instanceof HmagDrownedGirlEntity));
	}
	
}
