package net.sodiumzh.nff.girls.registry;

import net.sodiumzh.nautils.registries.NaUtilsRegistry;
import net.sodiumzh.nautils.registries.RegistryEntryCollection;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.entity.tamingprocess.hmag.*;
import net.sodiumzh.nff.girls.entity.tamingprocesses.hmag.*;
import net.sodiumzh.nff.services.entity.taming.NFFTamingProcess;
import net.sodiumzh.nff.services.registry.NFFRegistries;

public class NFFGirlsTamingProcesses {

    public static final RegistryEntryCollection<NFFTamingProcess> TAMING_PROCESSES =
            RegistryEntryCollection.create(NFFRegistries.TAMING_PROCESSES, NFFGirls.MOD_ID);

    public static final NaUtilsRegistry.Accessor<NFFTamingProcess> HMAG_ALRAUNE = TAMING_PROCESSES.register(
            "hmag_alraune",  () -> new HmagAlrauneTamingProcess().setItemGivingTableOverride(NFFGirlsTamingItems.PLANT_A::get));
    public static final NaUtilsRegistry.Accessor<NFFTamingProcess> HMAG_BANSHEE = TAMING_PROCESSES.register(
            "hmag_banshee",  () -> new HmagBansheeTamingProcess().setItemGivingTableOverride(NFFGirlsTamingItems.UNDEAD_B::get));
    public static final NaUtilsRegistry.Accessor<NFFTamingProcess> HMAG_CREEPER_GIRL = TAMING_PROCESSES.register(
            "hmag_creeper_girl",  () -> new HmagCreeperGirlTamingProcess().setItemGivingTableOverride(NFFGirlsTamingItems.CREEPER::get));
    public static final NaUtilsRegistry.Accessor<NFFTamingProcess> HMAG_CRIMSON_SLAUGHTERER = TAMING_PROCESSES.register(
            "hmag_crimson_slaughterer",  () -> new HmagCreeperGirlTamingProcess().setItemGivingTableOverride(NFFGirlsTamingItems.CRIMSON::get));
    public static final NaUtilsRegistry.Accessor<NFFTamingProcess> HMAG_CURSED_DOLL = TAMING_PROCESSES.register(
            "hmag_cursed_doll",  () -> new HmagCursedDollTamingProcess());
    public static final NaUtilsRegistry.Accessor<NFFTamingProcess> HMAG_ENDER_EXECUTOR = TAMING_PROCESSES.register(
            "hmag_ender_executor",  () -> new HmagEnderExecutorTamingProcess().setItemGivingTableOverride(NFFGirlsTamingItems.ENDERMAN::get));
    public static final NaUtilsRegistry.Accessor<NFFTamingProcess> HMAG_GHASTLY_SEEKER = TAMING_PROCESSES.register(
            "hmag_ghastly_seeker",  () -> new HmagGhastlySeekerTamingProcess().setItemGivingTableOverride(NFFGirlsTamingItems.UNDEAD_B::get));
    public static final NaUtilsRegistry.Accessor<NFFTamingProcess> HMAG_GLARYAD = TAMING_PROCESSES.register(
            "hmag_glaryad",  () -> new HmagGlaryadTamingProcess().setItemGivingTableOverride(NFFGirlsTamingItems.PLANT_B::get));
    public static final NaUtilsRegistry.Accessor<NFFTamingProcess> HMAG_HORNET = TAMING_PROCESSES.register(
            "hmag_glaryad",  () -> new HmagHornetTamingProcess().setItemGivingTableOverride(NFFGirlsTamingItems.BEE::get));
    public static final NaUtilsRegistry.Accessor<NFFTamingProcess> HMAG_ANIMAL_A = TAMING_PROCESSES.register(
            "hmag_animal_a",  () -> new HmagAnimalTamingProcess().setItemGivingTableOverride(NFFGirlsTamingItems.ANIMAL_A::get));
    public static final NaUtilsRegistry.Accessor<NFFTamingProcess> HMAG_IMP = TAMING_PROCESSES.register(
            "hmag_imp",  () -> new HmagImpTamingProcess().setItemGivingTableOverride(NFFGirlsTamingItems.HMAG_IMP::get));
    public static final NaUtilsRegistry.Accessor<NFFTamingProcess> HMAG_JACK_FROST = TAMING_PROCESSES.register(
            "hmag_jack_frost",  () -> new HmagImpTamingProcess().setItemGivingTableOverride(NFFGirlsTamingItems.SNOWMAN::get));
    public static final NaUtilsRegistry.Accessor<NFFTamingProcess> HMAG_JIANGSHI = TAMING_PROCESSES.register(
            "hmag_jiangshi",  () -> new HmagJiangshiTamingProcess());
    public static final NaUtilsRegistry.Accessor<NFFTamingProcess> HMAG_KOBOLD = TAMING_PROCESSES.register(
            "hmag_kobold",  () -> new HmagImpTamingProcess().setItemGivingTableOverride(NFFGirlsTamingItems.HMAG_KOBOLD::get));
    public static final NaUtilsRegistry.Accessor<NFFTamingProcess> HMAG_MELTY_MONSTER = TAMING_PROCESSES.register(
            "hmag_melty_monster",  () -> new HmagMeltyMonsterTamingProcess().setItemGivingTableOverride(NFFGirlsTamingItems.BLAZE::get));
    // Use the old name of the mob as the entity type key didn't change
    public static final NaUtilsRegistry.Accessor<NFFTamingProcess> HMAG_NECROTIC_REAPER = TAMING_PROCESSES.register(
            "hmag_necrotic_reaper",  () -> new HmagNecroticReaperTamingProcess());
    public static final NaUtilsRegistry.Accessor<NFFTamingProcess> HMAG_NIGHTWALKER = TAMING_PROCESSES.register(
            "hmag_nightwalker",  () -> new HmagImpTamingProcess().setItemGivingTableOverride(NFFGirlsTamingItems.CLAY_DOLL::get));
    public static final NaUtilsRegistry.Accessor<NFFTamingProcess> HMAG_REDCAP = TAMING_PROCESSES.register(
            "hmag_redcap",  () -> new HmagImpTamingProcess().setItemGivingTableOverride(NFFGirlsTamingItems.HMAG_REDCAP::get));
    public static final NaUtilsRegistry.Accessor<NFFTamingProcess> HMAG_SLIME_GIRL = TAMING_PROCESSES.register(
            "hmag_slime_girl",  () -> new HmagSlimeGirlTamingProcess());
    // Previously for Zombie Girls etc.
    public static final NaUtilsRegistry.Accessor<NFFTamingProcess> VANILLA_UNDEAD_A = TAMING_PROCESSES.register(
            "vanilla_undead_a",  () -> new HmagVanillaUndeadTamingProcess().setItemGivingTableOverride(NFFGirlsTamingItems.UNDEAD_A::get));
    // Previously for Skeleton Girls etc.
    public static final NaUtilsRegistry.Accessor<NFFTamingProcess> VANILLA_UNDEAD_B = TAMING_PROCESSES.register(
            "vanilla_undead_b",  () -> new HmagVanillaUndeadTamingProcess().setItemGivingTableOverride(NFFGirlsTamingItems.UNDEAD_B::get));
    // Previously for Wither Skeleton Girl
    public static final NaUtilsRegistry.Accessor<NFFTamingProcess> VANILLA_UNDEAD_C = TAMING_PROCESSES.register(
            "vanilla_undead_c",  () -> new HmagVanillaUndeadTamingProcess().setItemGivingTableOverride(NFFGirlsTamingItems.UNDEAD_C::get));

}
