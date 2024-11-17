package net.sodiumzh.nff.girls.subsystem.baublesystem.baubles;

import com.github.mechalopa.hmag.registry.ModItems;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.sodiumzh.nff.girls.registry.NFFGirlsConfigs;
import net.sodiumzh.nff.services.subsystem.baublesystem.BaubleAttributeModifier;
import net.sodiumzh.nff.services.subsystem.baublesystem.BaubleBehavior;
import net.sodiumzh.nff.services.subsystem.baublesystem.BaubleEquippingCondition;
import net.sodiumzh.nff.services.subsystem.baublesystem.BaubleProcessingArgs;

public class InsomniaFruitBaubleBehavior extends BaubleBehavior
{

	public InsomniaFruitBaubleBehavior(ResourceLocation key, BaubleEquippingCondition equippingCondition)
	{
		super(ModItems.INSOMNIA_FRUIT.get(), key, equippingCondition);
	}

	@Override
	public void onEquipped(BaubleProcessingArgs args) {
	}

	@Override
	public void preSlotTick(BaubleProcessingArgs args) {
	}

	@Override
	public void postSlotTick(BaubleProcessingArgs args) {
	}

	@Override
	public void slotTick(BaubleProcessingArgs args) {
	}

	@Override
	public BaubleAttributeModifier[] getDuplicatableModifiers(BaubleProcessingArgs args) {
		return null;
	}

	@Override
	public BaubleAttributeModifier[] getNonDuplicatableModifiers(Mob mob) {
		return new BaubleAttributeModifier[] {
				new BaubleAttributeModifier(Attributes.ATTACK_DAMAGE, 8d * NFFGirlsConfigs.ValueCache.Baubles.BAUBLE_ATK_BOOSTING_SCALE,
						 AttributeModifier.Operation.ADDITION).setAdditionalCondition(args -> args.user().level().isNight()),
				new BaubleAttributeModifier(Attributes.MAX_HEALTH, 60d * NFFGirlsConfigs.ValueCache.Baubles.BAUBLE_MAX_HP_BOOSTING_SCALE,
						 AttributeModifier.Operation.ADDITION).setAdditionalCondition(args -> args.user().level().isNight())
		};
	}

}
