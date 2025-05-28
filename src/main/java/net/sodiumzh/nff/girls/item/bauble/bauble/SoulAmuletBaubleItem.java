package net.sodiumzh.nff.girls.item.bauble.bauble;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamedSunSensitiveMob;
import net.sodiumzh.nff.girls.item.bauble.INFFGirlsBauble;
import net.sodiumzh.nff.girls.registry.NFFGirlsConfigs;
import net.sodiumzh.nff.girls.registry.NFFGirlsTags;
import net.sodiumzh.nff.girls.item.bauble.NFFGirlsDedicatedBaubleItem;
import net.sodiumzh.nfu.item.bauble.BaubleAttributeModifier;
import net.sodiumzh.nfu.item.bauble.BaubleEquippingCondition;
import net.sodiumzh.nfu.item.bauble.BaubleProcessingArgs;

public class SoulAmuletBaubleItem extends NFFGirlsDedicatedBaubleItem
{

	public SoulAmuletBaubleItem(int tier, Properties pProperties)
	{
		super(new ResourceLocation(NFFGirls.MOD_ID, "soul_amulet"), tier, pProperties);
		this.addBaubleTag(INFFGirlsBauble.TAG_ENVIRONMENT_IMMUNITY);
	}

	@Override
	public BaubleAttributeModifier[] getDuplicableModifiers(BaubleProcessingArgs arg0) {
		switch (this.getTier())
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
					Attributes.ATTACK_DAMAGE, 5d * NFFGirlsConfigs.ValueCache.Baubles.BAUBLE_ATK_BOOSTING_SCALE,
					Attributes.MOVEMENT_SPEED, 0.10d, "mb");
		}
		case 3:
		{
			return BaubleAttributeModifier.makeModifiers(
					Attributes.MAX_HEALTH, 25d * NFFGirlsConfigs.ValueCache.Baubles.BAUBLE_MAX_HP_BOOSTING_SCALE,
					Attributes.ATTACK_DAMAGE, 8d * NFFGirlsConfigs.ValueCache.Baubles.BAUBLE_ATK_BOOSTING_SCALE,
					Attributes.MOVEMENT_SPEED, 0.15d, "mb");
		}
		case 4:
		{
			return BaubleAttributeModifier.makeModifiers(
					Attributes.MAX_HEALTH, 35d * NFFGirlsConfigs.ValueCache.Baubles.BAUBLE_MAX_HP_BOOSTING_SCALE,
					Attributes.ATTACK_DAMAGE, 12d * NFFGirlsConfigs.ValueCache.Baubles.BAUBLE_ATK_BOOSTING_SCALE,
					Attributes.MOVEMENT_SPEED, 0.20d, "mb");
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
				args.user() instanceof INFFGirlsTamedSunSensitiveMob || args.user().getMobType().equals(MobType.UNDEAD)
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
		switch (this.getTier())
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
	public BaubleAttributeModifier[] getNonDuplicableModifiers(Mob mob) {
		return null;
	}

}
