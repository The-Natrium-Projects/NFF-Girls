package net.sodiumzh.nff.girls.registry;

import com.github.mechalopa.hmag.HMaG;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.services.event.setup.NFFTamingMappingRegisterEvent;

@Mod.EventBusSubscriber(modid = NFFGirls.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NFFGirlsBefriendingTypes {

	@SubscribeEvent
	public static void registerBefriendingType(NFFTamingMappingRegisterEvent event)
	{
		
		event.register(
				new ResourceLocation(HMaG.MODID, "zombie_girl"),
				new ResourceLocation(NFFGirls.MOD_ID, "hmag_zombie_girl"),
				NFFGirlsTamingProcesses.VANILLA_UNDEAD_A::get);
		event.register(
				new ResourceLocation(HMaG.MODID, "skeleton_girl"),
				new ResourceLocation(NFFGirls.MOD_ID, "hmag_skeleton_girl"),
				NFFGirlsTamingProcesses.VANILLA_UNDEAD_B::get);
		event.register(
				new ResourceLocation(HMaG.MODID, "husk_girl"),
				new ResourceLocation(NFFGirls.MOD_ID, "hmag_husk_girl"),
				NFFGirlsTamingProcesses.VANILLA_UNDEAD_B::get);
		event.register(
				new ResourceLocation(HMaG.MODID, "drowned_girl"),
				new ResourceLocation(NFFGirls.MOD_ID, "hmag_drowned_girl"),
				NFFGirlsTamingProcesses.VANILLA_UNDEAD_A::get);
		event.register(
				new ResourceLocation(HMaG.MODID, "creeper_girl"),
				new ResourceLocation(NFFGirls.MOD_ID, "hmag_creeper_girl"),
				NFFGirlsTamingProcesses.HMAG_CREEPER_GIRL::get);
		event.register(
				new ResourceLocation(HMaG.MODID, "ender_executor"),
				new ResourceLocation(NFFGirls.MOD_ID, "hmag_ender_executor"),
				NFFGirlsTamingProcesses.HMAG_ENDER_EXECUTOR::get);
		event.register(
				new ResourceLocation(HMaG.MODID, "stray_girl"),
				new ResourceLocation(NFFGirls.MOD_ID, "hmag_stray_girl"),
				NFFGirlsTamingProcesses.VANILLA_UNDEAD_B::get);
		event.register(
				new ResourceLocation(HMaG.MODID, "wither_skeleton_girl"),
				new ResourceLocation(NFFGirls.MOD_ID, "hmag_wither_skeleton_girl"),
				NFFGirlsTamingProcesses.VANILLA_UNDEAD_C::get);
		event.register(
				new ResourceLocation(HMaG.MODID, "hornet"),
				new ResourceLocation(NFFGirls.MOD_ID, "hmag_hornet"),
				NFFGirlsTamingProcesses.HMAG_HORNET::get);
		event.register(
				new ResourceLocation(HMaG.MODID, "necrotic_reaper"),
				new ResourceLocation(NFFGirls.MOD_ID, "hmag_necrotic_reaper"),
				NFFGirlsTamingProcesses.HMAG_NECROTIC_REAPER::get);
		event.register(
				new ResourceLocation(HMaG.MODID, "ghastly_seeker"),
				new ResourceLocation(NFFGirls.MOD_ID, "hmag_ghastly_seeker"),
				NFFGirlsTamingProcesses.HMAG_GHASTLY_SEEKER::get);
		event.register(
				new ResourceLocation(HMaG.MODID, "banshee"),
				new ResourceLocation(NFFGirls.MOD_ID, "hmag_banshee"),
				NFFGirlsTamingProcesses.HMAG_BANSHEE::get);
		event.register(
				new ResourceLocation(HMaG.MODID, "kobold"),
				new ResourceLocation(NFFGirls.MOD_ID, "hmag_kobold"),
				NFFGirlsTamingProcesses.HMAG_KOBOLD::get);
		event.register(
				new ResourceLocation(HMaG.MODID, "imp"),
				new ResourceLocation(NFFGirls.MOD_ID, "hmag_imp"),
				NFFGirlsTamingProcesses.HMAG_IMP::get);
		event.register(
				new ResourceLocation(HMaG.MODID, "harpy"),
				new ResourceLocation(NFFGirls.MOD_ID, "hmag_harpy"),
				NFFGirlsTamingProcesses.HMAG_ANIMAL_A::get);
		event.register(
				new ResourceLocation(HMaG.MODID, "snow_canine"),
				new ResourceLocation(NFFGirls.MOD_ID, "hmag_snow_canine"),
				NFFGirlsTamingProcesses.HMAG_ANIMAL_A::get);
		event.register(
				new ResourceLocation(HMaG.MODID, "slime_girl"),
				new ResourceLocation(NFFGirls.MOD_ID, "hmag_slime_girl"),
				NFFGirlsTamingProcesses.HMAG_SLIME_GIRL::get);
		event.register(
				new ResourceLocation(HMaG.MODID, "jiangshi"),
				new ResourceLocation(NFFGirls.MOD_ID, "hmag_jiangshi"),
				NFFGirlsTamingProcesses.HMAG_JIANGSHI::get);
		event.register(
				new ResourceLocation(HMaG.MODID, "dullahan"),
				new ResourceLocation(NFFGirls.MOD_ID, "hmag_dullahan"),
				NFFGirlsTamingProcesses.VANILLA_UNDEAD_B::get);
		event.register(
				new ResourceLocation(HMaG.MODID, "dodomeki"),
				new ResourceLocation(NFFGirls.MOD_ID, "hmag_dodomeki"),
				NFFGirlsTamingProcesses.VANILLA_UNDEAD_B::get);
		event.register(
				new ResourceLocation(HMaG.MODID, "alraune"),
				new ResourceLocation(NFFGirls.MOD_ID, "hmag_alraune"),
				NFFGirlsTamingProcesses.HMAG_ALRAUNE::get);
		event.register(
				new ResourceLocation(HMaG.MODID, "glaryad"),
				new ResourceLocation(NFFGirls.MOD_ID, "hmag_glaryad"),
				NFFGirlsTamingProcesses.HMAG_GLARYAD::get);
		event.register(
				new ResourceLocation(HMaG.MODID, "crimson_slaughterer"),
				new ResourceLocation(NFFGirls.MOD_ID, "hmag_crimson_slaughterer"),
				NFFGirlsTamingProcesses.HMAG_CRIMSON_SLAUGHTERER::get);
		event.register(
				new ResourceLocation(HMaG.MODID, "cursed_doll"),
				new ResourceLocation(NFFGirls.MOD_ID, "hmag_cursed_doll"),
				NFFGirlsTamingProcesses.HMAG_CURSED_DOLL::get);
		event.register(
				new ResourceLocation(HMaG.MODID, "redcap"),
				new ResourceLocation(NFFGirls.MOD_ID, "hmag_redcap"),
				NFFGirlsTamingProcesses.HMAG_REDCAP::get);
		event.register(
				new ResourceLocation(HMaG.MODID, "jack_frost"),
				new ResourceLocation(NFFGirls.MOD_ID, "hmag_jack_frost"),
				NFFGirlsTamingProcesses.HMAG_JACK_FROST::get);
		event.register(
				new ResourceLocation(HMaG.MODID, "melty_monster"),
				new ResourceLocation(NFFGirls.MOD_ID, "hmag_melty_monster"),
				NFFGirlsTamingProcesses.HMAG_MELTY_MONSTER::get);
		event.register(
				new ResourceLocation(HMaG.MODID, "nightwalker"),
				new ResourceLocation(NFFGirls.MOD_ID, "hmag_nightwalker"),
				NFFGirlsTamingProcesses.HMAG_NIGHTWALKER::get);
	}
}
