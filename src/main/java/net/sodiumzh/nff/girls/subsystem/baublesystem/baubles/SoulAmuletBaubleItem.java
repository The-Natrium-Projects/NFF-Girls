package net.sodiumzh.nff.girls.subsystem.baublesystem.baubles;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamedSunSensitiveMob;
import net.sodiumzh.nff.girls.registry.NFFGirlsConfigs;
import net.sodiumzh.nff.girls.registry.NFFGirlsTags;
import net.sodiumzh.nff.girls.subsystem.baublesystem.NFFGirlsDedicatedBaubleItem;
import net.sodiumzh.nff.services.subsystem.baublesystem.BaubleAttributeModifier;
import net.sodiumzh.nff.services.subsystem.baublesystem.BaubleEquippingCondition;
import net.sodiumzh.nff.services.subsystem.baublesystem.BaubleProcessingArgs;

public class SoulAmuletBaubleItem extends NFFGirlsDedicatedBaubleItem
{

	public SoulAmuletBaubleItem(String additionalKey, int tier, Properties pProperties)
	{
		super(additionalKey, tier, pProperties);
	}

	@Override
	public BaubleAttributeModifier[] getDuplicatableModifiers(BaubleProcessingArgs arg0) {
		switch (this.tier)
		{
		case 1:
		{
			return BaubleAttributeModifier.makeModifiers(
					Attributes.MAX_HEALTH, 10d * NFFGirlsConfigs.ValueCache.Baubles.BAUBLE_MAX_HP_BOOSTING_SCALE,
					Attributes.ATTACK_DAMAGE, 3d * NFFGirlsConfigs.ValueCache.Baubles.BAUBLE_ATK_BOOSTING_SCALE);
		}
		case 2:
		{
			return BaubleAttributeModifier.makeModifiers(
					Attributes.MAX_HEALTH, 15d * NFFGirlsConfigs.ValueCache.Baubles.BAUBLE_MAX_HP_BOOSTING_SCALE,
					Attributes.ATTACK_DAMAGE, 5d * NFFGirlsConfigs.ValueCache.Baubles.BAUBLE_ATK_BOOSTING_SCALE);
		}
		case 3:
		{
			return BaubleAttributeModifier.makeModifiers(
					Attributes.MAX_HEALTH, 25d * NFFGirlsConfigs.ValueCache.Baubles.BAUBLE_MAX_HP_BOOSTING_SCALE,
					Attributes.ATTACK_DAMAGE, 8d * NFFGirlsConfigs.ValueCache.Baubles.BAUBLE_ATK_BOOSTING_SCALE);
		}
		case 4:
		{
			return BaubleAttributeModifier.makeModifiers(
					Attributes.MAX_HEALTH, 35d * NFFGirlsConfigs.ValueCache.Baubles.BAUBLE_MAX_HP_BOOSTING_SCALE,
					Attributes.ATTACK_DAMAGE, 12d * NFFGirlsConfigs.ValueCache.Baubles.BAUBLE_ATK_BOOSTING_SCALE);
		}
		default:
		{
			throw this.unsupportedTier();
		}
		}
	}

	@Override
	public BaubleEquippingCondition getEquippingCondition() {
		return BaubleEquippingCondition.of(args -> (
				args.user() instanceof INFFGirlTamedSunSensitiveMob || args.user().getMobType().equals(MobType.UNDEAD)
				|| args.user().getType().is(NFFGirlsTags.CAN_EQUIP_SOUL_AMULET)));
	}

	@Override
	public void onEquipped(BaubleProcessingArgs arg0) {
	}

	@Override
	public void postSlotTick(BaubleProcessingArgs arg0) {
	}

	@Override
	public void preSlotTick(BaubleProcessingArgs arg0) {
	}

	@Override
	public void slotTick(BaubleProcessingArgs arg0) {
		switch (this.tier)
		{
		case 1:
		case 2:
		case 3: break;
		case 4:
		{
			arg0.user().heal(NFFGirlsConfigs.ValueCache.Baubles.BAUBLE_HEALTH_RECOVERY_SCALE * 0.1f / 20f);
			break;
		}
		default:
		{
			throw this.unsupportedTier();
		}
		}
	}

	@Override
	public ResourceLocation getBaubleRegistryKeyUnsuffixed() {
		return new ResourceLocation(NFFGirls.MOD_ID, "soul_amulet");
	}

	@Override
	public BaubleAttributeModifier[] getNonDuplicatableModifiers(Mob mob) {
		return null;
	}

}
