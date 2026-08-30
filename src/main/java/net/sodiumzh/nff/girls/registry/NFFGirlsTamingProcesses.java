package net.sodiumzh.nff.girls.registry;

import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.entity.NFFGirlsTamingRules;
import net.sodiumzh.nff.girls.entity.tamingprocess.*;
import net.sodiumzh.nff.services.entity.taming.NFFTamingProcess;
import net.sodiumzh.nff.services.registry.NFFRegistries;
import net.sodiumzh.nfu.entity.anger.MobAngerRules;
import net.sodiumzh.nfu.registry.NFURegistry;
import net.sodiumzh.nfu.registry.NFURegistryEntryCollection;

public class NFFGirlsTamingProcesses {

    public static final NFURegistryEntryCollection<NFFTamingProcess> TAMING_PROCESSES =
            NFURegistryEntryCollection.create(NFFRegistries.TAMING_PROCESSES, NFFGirls.MOD_ID);

    public static final NFURegistry.Accessor<NFFTamingProcess> HMAG_ALRAUNE = TAMING_PROCESSES.register(
            "hmag_alraune",  () -> new HmagAlrauneTamingProcess().setItemGivingTableOverride(NFFGirlsFriendingItems.PLANT_A::get)
            .setItemGivingCooldownTicks(NFFGirlsTamingRules.COOLDOWN_SHORT)
            .setAngerAndInterruptionRules(NFFGirlsAngerRules.DEFAULT.get(), MobAngerRules.ATTACKER.get()));
    public static final NFURegistry.Accessor<NFFTamingProcess> HMAG_BANSHEE = TAMING_PROCESSES.register(
            "hmag_banshee",  () -> new HmagBansheeTamingProcess().setItemGivingTableOverride(NFFGirlsFriendingItems.UNDEAD_B::get)
            .setAngerAndInterruptionRules(NFFGirlsAngerRules.DEFAULT.get(), MobAngerRules.ATTACKER.get()));
    public static final NFURegistry.Accessor<NFFTamingProcess> HMAG_CREEPER_GIRL = TAMING_PROCESSES.register(
            "hmag_creeper_girl",  () -> new HmagCreeperGirlTamingProcess().setItemGivingTableOverride(NFFGirlsFriendingItems.CREEPER::get)
            .setAngerAndInterruptionRules(NFFGirlsAngerRules.DEFAULT.get(), MobAngerRules.ATTACKER.get()));
    public static final NFURegistry.Accessor<NFFTamingProcess> HMAG_CRIMSON_SLAUGHTERER = TAMING_PROCESSES.register(
            "hmag_crimson_slaughterer",  () -> new HmagCrimsonSlaughtererTamingProcess().setItemGivingTableOverride(NFFGirlsFriendingItems.CRIMSON::get)
            .setAngerAndInterruptionRules(NFFGirlsAngerRules.DEFAULT.get(), MobAngerRules.ATTACKER.get()));
    public static final NFURegistry.Accessor<NFFTamingProcess> HMAG_CURSED_DOLL = TAMING_PROCESSES.register(
            "hmag_cursed_doll",  () -> new HmagCursedDollTamingProcess()
            .setAngerAndInterruptionRules(NFFGirlsAngerRules.DEFAULT.get(), MobAngerRules.ATTACKER.get()));
    public static final NFURegistry.Accessor<NFFTamingProcess> HMAG_ENDER_EXECUTOR = TAMING_PROCESSES.register(
            "hmag_ender_executor",  () -> new HmagEnderExecutorTamingProcess().setItemGivingTableOverride(NFFGirlsFriendingItems.ENDERMAN::get)
            .setAngerAndInterruptionRules(MobAngerRules.NO_ANGER.get()));
    public static final NFURegistry.Accessor<NFFTamingProcess> HMAG_GHASTLY_SEEKER = TAMING_PROCESSES.register(
            "hmag_ghastly_seeker",  () -> new HmagGhastlySeekerTamingProcess().setItemGivingTableOverride(NFFGirlsFriendingItems.UNDEAD_NETHER_B::get)
            .setAngerAndInterruptionRules(NFFGirlsAngerRules.DEFAULT.get(), MobAngerRules.ATTACKER.get()));
    public static final NFURegistry.Accessor<NFFTamingProcess> HMAG_GLARYAD = TAMING_PROCESSES.register(
            "hmag_glaryad",  () -> new HmagGlaryadTamingProcess().setItemGivingTableOverride(NFFGirlsFriendingItems.PLANT_B::get)
            .setAngerAndInterruptionRules(NFFGirlsAngerRules.DEFAULT.get(), MobAngerRules.ATTACKER.get()));
    public static final NFURegistry.Accessor<NFFTamingProcess> HMAG_HORNET = TAMING_PROCESSES.register(
            "hmag_glaryad",  () -> new HmagHornetTamingProcess().setItemGivingTableOverride(NFFGirlsFriendingItems.BEE::get)
            .setAngerAndInterruptionRules(NFFGirlsAngerRules.DEFAULT.get(), MobAngerRules.ATTACKER.get()));
    public static final NFURegistry.Accessor<NFFTamingProcess> HMAG_ANIMAL_A = TAMING_PROCESSES.register(
            "hmag_animal_a",  () -> new HmagAnimalTamingProcess().setItemGivingTableOverride(NFFGirlsFriendingItems.ANIMAL_A::get)
            .setAngerAndInterruptionRules(NFFGirlsAngerRules.DEFAULT.get(), MobAngerRules.ATTACKER.get()));
    public static final NFURegistry.Accessor<NFFTamingProcess> HMAG_IMP = TAMING_PROCESSES.register(
            "hmag_imp",  () -> new HmagImpTamingProcess().setItemGivingTableOverride(NFFGirlsFriendingItems.HMAG_IMP::get)
            .setWatchAndPickItemGoalPriorities(2, 4)
            .setAngerAndInterruptionRules(NFFGirlsAngerRules.DEFAULT.get(), MobAngerRules.ATTACKER.get()));
    public static final NFURegistry.Accessor<NFFTamingProcess> HMAG_JACK_FROST = TAMING_PROCESSES.register(
            "hmag_jack_frost",  () -> new HmagImpTamingProcess().setItemGivingTableOverride(NFFGirlsFriendingItems.SNOWMAN::get)
            .setWatchAndPickItemGoalPriorities(3, 4)
            .setAngerAndInterruptionRules(NFFGirlsAngerRules.DEFAULT.get(), MobAngerRules.ATTACKER.get()));
    public static final NFURegistry.Accessor<NFFTamingProcess> HMAG_JIANGSHI = TAMING_PROCESSES.register(
            "hmag_jiangshi",  () -> new HmagJiangshiTamingProcess()
            .setAngerAndInterruptionRules(MobAngerRules.NO_ANGER.get()));
    public static final NFURegistry.Accessor<NFFTamingProcess> HMAG_KOBOLD = TAMING_PROCESSES.register(
            "hmag_kobold",  () -> new HmagImpTamingProcess().setItemGivingTableOverride(NFFGirlsFriendingItems.HMAG_KOBOLD::get)
            .setWatchAndPickItemGoalPriorities(2, 4)
            .setAngerAndInterruptionRules(NFFGirlsAngerRules.DEFAULT.get(), MobAngerRules.ATTACKER.get()));
    public static final NFURegistry.Accessor<NFFTamingProcess> HMAG_MELTY_MONSTER = TAMING_PROCESSES.register(
            "hmag_melty_monster",  () -> new HmagMeltyMonsterTamingProcess()
            .setItemGivingTableOverride(NFFGirlsFriendingItems.BLAZE::get)
            .setAngerAndInterruptionRules(NFFGirlsAngerRules.DEFAULT.get(), MobAngerRules.ATTACKER.get()));
    // Use the old name of the mob as the entity type key didn't change
    public static final NFURegistry.Accessor<NFFTamingProcess> HMAG_NECROTIC_REAPER = TAMING_PROCESSES.register(
            "hmag_necrotic_reaper",  () -> new HmagNecroticReaperTamingProcess()
            .setAngerAndInterruptionRules(MobAngerRules.NO_ANGER.get()));
    public static final NFURegistry.Accessor<NFFTamingProcess> HMAG_NIGHTWALKER = TAMING_PROCESSES.register(
            "hmag_nightwalker",  () -> new HmagNightwalkerTamingProcess()
            .setItemGivingTableOverride(NFFGirlsFriendingItems.CLAY_DOLL::get)
            .setAngerAndInterruptionRules(NFFGirlsAngerRules.DEFAULT.get(), MobAngerRules.ATTACKER.get()));
    public static final NFURegistry.Accessor<NFFTamingProcess> HMAG_REDCAP = TAMING_PROCESSES.register(
            "hmag_redcap",  () -> new HmagImpTamingProcess()
            .setItemGivingTableOverride(NFFGirlsFriendingItems.HMAG_REDCAP::get)
            .setWatchAndPickItemGoalPriorities(3, 4)
            .setAngerAndInterruptionRules(NFFGirlsAngerRules.DEFAULT.get(), MobAngerRules.ATTACKER.get()));
    public static final NFURegistry.Accessor<NFFTamingProcess> HMAG_SLIME_GIRL = TAMING_PROCESSES.register(
            "hmag_slime_girl",  () -> new HmagSlimeGirlTamingProcess()
            .setAngerAndInterruptionRules(NFFGirlsAngerRules.DEFAULT.get(), MobAngerRules.ATTACKER.get()));
    // Previously for Zombie Girls etc.
    public static final NFURegistry.Accessor<NFFTamingProcess> VANILLA_UNDEAD_A = TAMING_PROCESSES.register(
            "vanilla_undead_a",  () -> new HmagVanillaUndeadTamingProcess()
            .setItemGivingTableOverride(NFFGirlsFriendingItems.UNDEAD_A::get)
            .setAngerAndInterruptionRules(NFFGirlsAngerRules.ZOMBIE_PIGLIN_LIKE.get(), MobAngerRules.ATTACKER.get()));
    // Previously for Skeleton Girls etc.
    public static final NFURegistry.Accessor<NFFTamingProcess> VANILLA_UNDEAD_B = TAMING_PROCESSES.register(
            "vanilla_undead_b",  () -> new HmagVanillaUndeadTamingProcess()
            .setItemGivingTableOverride(NFFGirlsFriendingItems.UNDEAD_B::get)
            .setAngerAndInterruptionRules(NFFGirlsAngerRules.ZOMBIE_PIGLIN_LIKE.get(), MobAngerRules.ATTACKER.get()));
    // Previously for Wither Skeleton Girl
    public static final NFURegistry.Accessor<NFFTamingProcess> VANILLA_UNDEAD_C = TAMING_PROCESSES.register(
            "vanilla_undead_c",  () -> new HmagVanillaUndeadTamingProcess()
            .setItemGivingTableOverride(NFFGirlsFriendingItems.UNDEAD_NETHER_C::get)
            .setAngerAndInterruptionRules(NFFGirlsAngerRules.ZOMBIE_PIGLIN_LIKE.get(), MobAngerRules.ATTACKER.get()));

}
