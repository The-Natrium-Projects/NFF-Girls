 package net.sodiumzh.nff.girls.eventlisteners;

import java.util.UUID;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.entity.INFFGirlTamed;
import net.sodiumzh.nff.girls.entity.capability.CNFFGirlsFavorabilityHandler;
import net.sodiumzh.nff.girls.entity.capability.CNFFGirlsLevelHandler;
import net.sodiumzh.nff.girls.registry.NFFGirlsCapabilities;
import net.sodiumzh.nff.girls.registry.NFFGirlsConfigs;
import net.sodiumzh.nff.services.entity.capability.CHealingHandler;

@Mod.EventBusSubscriber(modid = NFFGirls.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class NFFGirlsSubsystemEventListeners
{

	@Deprecated
	protected static final UUID FAV_ATK_MODIFIER_UUID = UUID.fromString("0e570979-9f96-4559-b31e-93500e69da07");
	
	
	@SubscribeEvent
	public static void onFavorabilityChangeValue(CNFFGirlsFavorabilityHandler.ChangeValueEvent event)
	{
		// Handle Favorability attack modifier
		// Inspired by *Girl's Frontline*
		event.mob.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(FAV_ATK_MODIFIER_UUID);
		double atkmod = 1;
		if (event.toValue < 5d)
			atkmod = 0.5;
		else if (event.toValue < 20d)
			atkmod = 0.7;
		else if (event.toValue < 50d)
			atkmod = 0.9;
		else if (event.toValue < 80d)
			atkmod = 1.0;
		else if (event.toValue < 99.9999d)
			atkmod = 1.1;
		else atkmod = 1.2;
		event.mob.getAttribute(Attributes.ATTACK_DAMAGE).addPermanentModifier(new AttributeModifier(FAV_ATK_MODIFIER_UUID,
				"favorability_atk", atkmod - 1, AttributeModifier.Operation.MULTIPLY_TOTAL));		
	}
	
	@SubscribeEvent
	public static void onHealingItemSucceed(CHealingHandler.HealingSucceededEvent event)
	{
		if (event.living instanceof INFFGirlTamed bm)
		{
			bm.getFavorabilityHandler().addFavorability(event.healedValue / 50);
		}
	}

	@Deprecated
	private static final UUID LVL_HP_MODIFIER_UUID = UUID.fromString("2cf793a5-f798-49b0-ba87-c196a2038d52");
	@Deprecated
	private static final UUID LVL_ATK_MODIFIER_UUID = UUID.fromString("8051fa50-f78c-4702-bb14-04e801a6ca33");
	
	@SubscribeEvent
	public static void onLevelChange(CNFFGirlsLevelHandler.LevelChangeEvent event)
	{
		// TODO This is for removing legacy attribute modifiers.
		event.mob.getAttribute(Attributes.MAX_HEALTH).removeModifier(LVL_HP_MODIFIER_UUID);
		event.mob.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(LVL_ATK_MODIFIER_UUID);
		
		
		int lv = event.levelAfter;
		int hpLevel = NFFGirlsConfigs.ValueCache.Combat.MAX_HEALTH_BOOST_BY_LEVEL <= 0 ?
				lv : Math.min((int)Math.round(NFFGirlsConfigs.ValueCache.Combat.MAX_HEALTH_BOOST_BY_LEVEL / NFFGirlsConfigs.ValueCache.Combat.HEALTH_BOOST_PER_LEVEL), lv);
		int atkLevel = NFFGirlsConfigs.ValueCache.Combat.MAX_ATK_BOOST_BY_LEVEL <= 0 ?
				lv : Math.min((int)Math.round(NFFGirlsConfigs.ValueCache.Combat.MAX_ATK_BOOST_BY_LEVEL / NFFGirlsConfigs.ValueCache.Combat.ATK_BOOST_PER_LEVEL), lv);
		if (!event.mob.level.isClientSide && event.mob instanceof INFFGirlTamed bm)
		{
			CNFFGirlsLevelHandler.LVL_HP_MODIFIER.apply(event.mob, Attributes.MAX_HEALTH, hpLevel);
			CNFFGirlsLevelHandler.LVL_ATK_MODIFIER.apply(event.mob, Attributes.ATTACK_DAMAGE, atkLevel);
		}
	}
	
	/**
	 * Update xplevel once for each mob.
	 * @deprecated This is only for porting old {@code onLevelChange} implementation to
	 * new {@code RepeatableAttributeModifier}-based impl.
	 */
	@SubscribeEvent
	@Deprecated
	public static void onLivingTick_LEGACY(LivingTickEvent event)
	{
		if (event.getEntity().tickCount == 20)
		{
			event.getEntity().getCapability(NFFGirlsCapabilities.CAP_LEVEL_HANDLER).ifPresent(cap -> 
			{
				// This is just a dummy event, not posted
				onLevelChange(new CNFFGirlsLevelHandler.LevelChangeEvent(cap, cap.getExpectedLevel(), cap.getExpectedLevel()));
			});
		}
	}
}
