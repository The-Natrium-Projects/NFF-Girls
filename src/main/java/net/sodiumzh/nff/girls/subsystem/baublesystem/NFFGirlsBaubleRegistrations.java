package net.sodiumzh.nff.girls.subsystem.baublesystem;

import java.util.function.Function;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.entity.hmag.HmagAlrauneEntity;
import net.sodiumzh.nff.girls.entity.hmag.HmagBansheeEntity;
import net.sodiumzh.nff.girls.entity.hmag.HmagCrimsonSlaughtererEntity;
import net.sodiumzh.nff.girls.entity.hmag.HmagCursedDollEntity;
import net.sodiumzh.nff.girls.entity.hmag.HmagDodomekiEntity;
import net.sodiumzh.nff.girls.entity.hmag.HmagDrownedGirlEntity;
import net.sodiumzh.nff.girls.entity.hmag.HmagDullahanEntity;
import net.sodiumzh.nff.girls.entity.hmag.HmagEnderExecutorEntity;
import net.sodiumzh.nff.girls.entity.hmag.HmagGhastlySeekerEntity;
import net.sodiumzh.nff.girls.entity.hmag.HmagGlaryadEntity;
import net.sodiumzh.nff.girls.entity.hmag.HmagHarpyEntity;
import net.sodiumzh.nff.girls.entity.hmag.HmagHornetEntity;
import net.sodiumzh.nff.girls.entity.hmag.HmagHuskGirlEntity;
import net.sodiumzh.nff.girls.entity.hmag.HmagImpEntity;
import net.sodiumzh.nff.girls.entity.hmag.HmagJackFrostEntity;
import net.sodiumzh.nff.girls.entity.hmag.HmagJiangshiEntity;
import net.sodiumzh.nff.girls.entity.hmag.HmagKoboldEntity;
import net.sodiumzh.nff.girls.entity.hmag.HmagMeltyMonsterEntity;
import net.sodiumzh.nff.girls.entity.hmag.HmagNecroticReaperEntity;
import net.sodiumzh.nff.girls.entity.hmag.HmagNightwalkerEntity;
import net.sodiumzh.nff.girls.entity.hmag.HmagRedcapEntity;
import net.sodiumzh.nff.girls.entity.hmag.HmagSkeletonGirlEntity;
import net.sodiumzh.nff.girls.entity.hmag.HmagSlimeGirlEntity;
import net.sodiumzh.nff.girls.entity.hmag.HmagSnowCanineEntity;
import net.sodiumzh.nff.girls.entity.hmag.HmagStrayGirlEntity;
import net.sodiumzh.nff.girls.entity.hmag.HmagWitherSkeletonGirlEntity;
import net.sodiumzh.nff.girls.entity.hmag.HmagZombieGirlEntity;
import net.sodiumzh.nff.girls.registry.NFFGirlsItems;
import net.sodiumzh.nff.girls.subsystem.baublesystem.baubles.EnderManHandBlockBaubleBehavior;
import net.sodiumzh.nff.girls.subsystem.baublesystem.baubles.InsomniaFruitBaubleBehavior;
import net.sodiumzh.nff.girls.subsystem.baublesystem.baubles.NecroticReaperHandHoeBaubleBehavior;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nff.services.subsystem.baublesystem.BaubleEquippingCondition;
import net.sodiumzh.nff.services.subsystem.baublesystem.RegisterBaubleEquippableMobsEvent;
import net.sodiumzh.nff.services.subsystem.baublesystem.RegisterBaublesEvent;

@EventBusSubscriber(modid = NFFGirls.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NFFGirlsBaubleRegistrations
{
	
	@SubscribeEvent
	public static void baubleAdditionalRegistration(NFFGirlsBaubleAdditionalRegistry.RegisterEvent event)
	{
		event.register(NFFGirlsItems.SOUL_AMULET.get());
		event.register(NFFGirlsItems.SOUL_AMULET_II.get());
		event.register(NFFGirlsItems.SOUL_AMULET_III.get());
		event.register(NFFGirlsItems.SOUL_AMULET_IV.get());
		event.register(NFFGirlsItems.RESISTANCE_AMULET.get());
		event.register(NFFGirlsItems.RESISTANCE_AMULET_II.get());
		event.register(NFFGirlsItems.COURAGE_AMULET.get());
		event.register(NFFGirlsItems.COURAGE_AMULET_II.get());
		event.register(NFFGirlsItems.HEALING_JADE.get());
		event.register(NFFGirlsItems.LIFE_JADE.get());
		event.register(NFFGirlsItems.LIFE_JADE_II.get());
		event.register(NFFGirlsItems.AQUA_JADE.get());
		event.register(NFFGirlsItems.POISONOUS_THORN.get());
	}
	
	public static Function<Mob, ItemStack> accessMobAdditionalInventory(int position)
	{
		return mob -> {
			if (mob instanceof INFFTamed bm)
				return bm.getAdditionalInventory().getItem(position);
			else throw new IllegalArgumentException("Input mob isn't INFFTamed.");
		};
	}
	
	@SubscribeEvent
	public static void baubleEquippableRegistration(RegisterBaubleEquippableMobsEvent event)
	{
		registerWithContinuousSlotSequence(event, HmagDrownedGirlEntity.class, 6, 8);
		registerWithContinuousSlotSequence(event, HmagZombieGirlEntity.class, 6, 8);
		registerWithContinuousSlotSequence(event, HmagHuskGirlEntity.class, 6, 8);
		
		registerWithContinuousSlotSequence(event, HmagSkeletonGirlEntity.class, 6, 7);
		registerWithContinuousSlotSequence(event, HmagStrayGirlEntity.class, 6, 7);
		registerWithContinuousSlotSequence(event, HmagWitherSkeletonGirlEntity.class, 6, 7);
		
		registerWithContinuousSlotSequence(event, HmagEnderExecutorEntity.class, 3, 5)
		.addSpecialSlot("enderman_hand_block", accessMobAdditionalInventory(2)).addSpecialSlotItem("nffgirls:enderman_hand_block");
		
		registerWithContinuousSlotSequence(event, HmagAlrauneEntity.class, 0, 3);
		registerWithContinuousSlotSequence(event, HmagBansheeEntity.class, 2, 5);
		registerWithContinuousSlotSequence(event, HmagCrimsonSlaughtererEntity.class, 0, 4);
		registerWithContinuousSlotSequence(event, HmagCursedDollEntity.class, 2, 8);
		registerWithContinuousSlotSequence(event, HmagDodomekiEntity.class, 2, 6);
		registerWithContinuousSlotSequence(event, HmagDullahanEntity.class, 2, 6);
		registerWithContinuousSlotSequence(event, HmagGhastlySeekerEntity.class, 0, 4);
		registerWithContinuousSlotSequence(event, HmagGlaryadEntity.class, 0, 3);
		registerWithContinuousSlotSequence(event, HmagHarpyEntity.class, 0, 4);
		registerWithContinuousSlotSequence(event, HmagHornetEntity.class, 2, 4);
		registerWithContinuousSlotSequence(event, HmagImpEntity.class, 2, 4);
		registerWithContinuousSlotSequence(event, HmagJackFrostEntity.class, 0, 4);
		registerWithContinuousSlotSequence(event, HmagJiangshiEntity.class, 3, 7);
		registerWithContinuousSlotSequence(event, HmagKoboldEntity.class, 2, 4);
		registerWithContinuousSlotSequence(event, HmagMeltyMonsterEntity.class, 0, 4);
		registerWithContinuousSlotSequence(event, HmagNecroticReaperEntity.class, 2, 6)
			.addSpecialSlot("main_hand", accessMobAdditionalInventory(0)).addSpecialSlotItem("nffgirls:necrotic_reaper_hoe")
			.addSpecialSlot("off_hand", accessMobAdditionalInventory(1)).addSpecialSlotItem("nffgirls:necrotic_reaper_hoe");
		registerWithContinuousSlotSequence(event, HmagNightwalkerEntity.class, 0, 4);
		registerWithContinuousSlotSequence(event, HmagRedcapEntity.class, 6, 7);
		registerWithContinuousSlotSequence(event, HmagSlimeGirlEntity.class, 0, 4);
		registerWithContinuousSlotSequence(event, HmagSnowCanineEntity.class, 0, 4);
		
	}
	
	@SubscribeEvent
	public static void baubleRegistration(RegisterBaublesEvent event)
	{
		event.register(NFFGirlsItems.SOUL_AMULET.get());
		event.register(NFFGirlsItems.SOUL_AMULET_II.get());
		event.register(NFFGirlsItems.SOUL_AMULET_III.get());
		event.register(NFFGirlsItems.SOUL_AMULET_IV.get());
		event.register(new InsomniaFruitBaubleBehavior(new ResourceLocation(NFFGirls.MOD_ID, "insomnia_fruit"), BaubleEquippingCondition.always()));
		event.register(NFFGirlsItems.RESISTANCE_AMULET.get());
		event.register(NFFGirlsItems.RESISTANCE_AMULET_II.get());
		event.register(NFFGirlsItems.COURAGE_AMULET.get());
		event.register(NFFGirlsItems.COURAGE_AMULET_II.get());
		event.register(NFFGirlsItems.HEALING_JADE.get());
		event.register(NFFGirlsItems.LIFE_JADE.get());
		event.register(NFFGirlsItems.LIFE_JADE_II.get());
		event.register(NFFGirlsItems.AQUA_JADE.get());
		event.register(NFFGirlsItems.POISONOUS_THORN.get());
		event.register(new NecroticReaperHandHoeBaubleBehavior(new ResourceLocation(NFFGirls.MOD_ID, "necrotic_reaper_hoe")));
		event.register(new EnderManHandBlockBaubleBehavior(new ResourceLocation(NFFGirls.MOD_ID, "enderman_hand_block")));
	}
	/*
	public static void modifyBaubleEquippable(ModifyBaubleEquippableMobsEvent event)
	{
		event.addSpecialSlotAccepted(HmagNecroticReaperEntity.class, "main_hand", "dwmg:necrotic_reaper_hoe");
	}*/
	
	// ========= Utils ==============
	/**
	 * Register a befriended mob with a series of continuous bauble slots in the additional inventory.
	 * for example, using index range (3, 6) will register the additional inventory slot 3, 4, 5 with slot name "0", "1", "2" respectively. 
	 */
	public static RegisterBaubleEquippableMobsEvent.SlotRegisterer registerWithContinuousSlotSequence(RegisterBaubleEquippableMobsEvent event, 
			Class<? extends Mob> clazz, int minIndex, int maxIndexExclude)
	{
		RegisterBaubleEquippableMobsEvent.SlotRegisterer reg = event.register(clazz);
		for (int i = 0; i < maxIndexExclude - minIndex; ++i)
		{
			reg.addSlot(Integer.toString(i), accessMobAdditionalInventory(minIndex + i));
		}
		return reg;
	}
	
	
}
