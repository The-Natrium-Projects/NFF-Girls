package net.sodiumzh.nff.girls.registry;

import net.minecraft.resources.ResourceLocation;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.data.NFFGirlsDataReaders;
import net.sodiumzh.nfu.entity.MobApplicableItemTable;
import net.sodiumzh.nfu.registry.NFURegistries;
import net.sodiumzh.nfu.registry.NFURegistry;
import net.sodiumzh.nfu.registry.NFURegistryEntryCollection;

public class NFFGirlsTamingItems {
    public static final NFURegistryEntryCollection<MobApplicableItemTable> TAMING_ITEMS =
            NFURegistryEntryCollection.create(NFURegistries.MOB_APPLICABLE_ITEM_TABLES, NFFGirls.MOD_ID);

    // For Alraune
    public static final NFURegistry.Accessor<MobApplicableItemTable> PLANT_A =
            TAMING_ITEMS.register("taming_plant_a", () -> MobApplicableItemTable.builder()
                    .readData(new ResourceLocation(NFFGirls.MOD_ID, "taming_items/plant_a.json"), NFFGirlsDataReaders::readMobApplicableItemTable)
                    .build());
    // For Glaryad
    public static final NFURegistry.Accessor<MobApplicableItemTable> PLANT_B =
            TAMING_ITEMS.register("taming_plant_a", () -> MobApplicableItemTable.builder()
                    .readData(new ResourceLocation(NFFGirls.MOD_ID, "taming_items/plant_b.json"), NFFGirlsDataReaders::readMobApplicableItemTable)
                    .build());
    // For Zombie Girls etc.
    public static final NFURegistry.Accessor<MobApplicableItemTable> UNDEAD_A =
            TAMING_ITEMS.register("taming_undead_a", () -> MobApplicableItemTable.builder()
                    .readData(new ResourceLocation(NFFGirls.MOD_ID, "taming_items/undead_a.json"),NFFGirlsDataReaders::readMobApplicableItemTable)
                    .build());
    // For Skeleton Girls etc.
    public static final NFURegistry.Accessor<MobApplicableItemTable> UNDEAD_B =
            TAMING_ITEMS.register("taming_undead_b", () -> MobApplicableItemTable.builder()
                    .readData(new ResourceLocation(NFFGirls.MOD_ID, "taming_items/undead_b.json"),NFFGirlsDataReaders::readMobApplicableItemTable)
                    .build());
    // For Ghastly Seeker
    public static final NFURegistry.Accessor<MobApplicableItemTable> UNDEAD_NETHER_B =
            TAMING_ITEMS.register("taming_undead_nether_b", () -> MobApplicableItemTable.builder()
                    .readData(new ResourceLocation(NFFGirls.MOD_ID, "taming_items/undead_nether_b.json"),NFFGirlsDataReaders::readMobApplicableItemTable)
                    .build());
    public static final NFURegistry.Accessor<MobApplicableItemTable> UNDEAD_C =
            TAMING_ITEMS.register("taming_undead_c", () -> MobApplicableItemTable.builder()
                    .readData(new ResourceLocation(NFFGirls.MOD_ID, "taming_items/undead_c.json"),NFFGirlsDataReaders::readMobApplicableItemTable)
                    .build());
    // For Wither Skeleton Girl
    public static final NFURegistry.Accessor<MobApplicableItemTable> UNDEAD_NETHER_C =
            TAMING_ITEMS.register("taming_undead_nether_c", () -> MobApplicableItemTable.builder()
                    .readData(new ResourceLocation(NFFGirls.MOD_ID, "taming_items/undead_nether_c.json"),NFFGirlsDataReaders::readMobApplicableItemTable)
                    .build());

    // For Creeper Girl
    public static final NFURegistry.Accessor<MobApplicableItemTable> CREEPER =
            TAMING_ITEMS.register("taming_creeper", () -> MobApplicableItemTable.builder()
                    .readData(new ResourceLocation(NFFGirls.MOD_ID, "taming_items/creeper.json"),NFFGirlsDataReaders::readMobApplicableItemTable)
                    .build());
    // For Ender Executor
    public static final NFURegistry.Accessor<MobApplicableItemTable> ENDERMAN =
            TAMING_ITEMS.register("taming_enderman", () -> MobApplicableItemTable.builder()
                    .readData(new ResourceLocation(NFFGirls.MOD_ID, "taming_items/enderman.json"),NFFGirlsDataReaders::readMobApplicableItemTable)
                    .build());
    // For Hornet
    public static final NFURegistry.Accessor<MobApplicableItemTable> BEE =
            TAMING_ITEMS.register("taming_bee", () -> MobApplicableItemTable.builder()
                    .readData(new ResourceLocation(NFFGirls.MOD_ID, "taming_items/bee.json"),NFFGirlsDataReaders::readMobApplicableItemTable)
                    .build());
    // For Melty Monster
    public static final NFURegistry.Accessor<MobApplicableItemTable> BLAZE =
            TAMING_ITEMS.register("taming_blaze", () -> MobApplicableItemTable.builder()
                    .readData(new ResourceLocation(NFFGirls.MOD_ID, "taming_items/blaze.json"),NFFGirlsDataReaders::readMobApplicableItemTable)
                    .build());
    // For Nightwalker
    public static final NFURegistry.Accessor<MobApplicableItemTable> CLAY_DOLL =
            TAMING_ITEMS.register("taming_clay_doll", () -> MobApplicableItemTable.builder()
                    .readData(new ResourceLocation(NFFGirls.MOD_ID, "taming_items/clay_doll.json"),NFFGirlsDataReaders::readMobApplicableItemTable)
                    .build());
    // For Jack o'Frost
    public static final NFURegistry.Accessor<MobApplicableItemTable> SNOWMAN =
            TAMING_ITEMS.register("taming_snowman", () -> MobApplicableItemTable.builder()
                    .readData(new ResourceLocation(NFFGirls.MOD_ID, "taming_items/snowman.json"),NFFGirlsDataReaders::readMobApplicableItemTable)
                    .build());
    // For Harpy and Snow Canine
    public static final NFURegistry.Accessor<MobApplicableItemTable> ANIMAL_A =
            TAMING_ITEMS.register("taming_animal_a", () -> MobApplicableItemTable.builder()
                    .readData(new ResourceLocation(NFFGirls.MOD_ID, "taming_items/animal_a.json"),NFFGirlsDataReaders::readMobApplicableItemTable)
                    .build());

    // For Crimson Slaughterer
    public static final NFURegistry.Accessor<MobApplicableItemTable> CRIMSON =
            TAMING_ITEMS.register("taming_crimson", () -> MobApplicableItemTable.builder()
                    .readData(new ResourceLocation(NFFGirls.MOD_ID, "taming_items/crimson.json"),NFFGirlsDataReaders::readMobApplicableItemTable)
                    .build());
    public static final NFURegistry.Accessor<MobApplicableItemTable> HMAG_IMP =
            TAMING_ITEMS.register("taming_hmag_imp", () -> MobApplicableItemTable.builder()
                    .readData(new ResourceLocation(NFFGirls.MOD_ID, "taming_items/hmag_imp.json"),NFFGirlsDataReaders::readMobApplicableItemTable)
                    .build());
    public static final NFURegistry.Accessor<MobApplicableItemTable> HMAG_KOBOLD =
            TAMING_ITEMS.register("taming_hmag_kobold", () -> MobApplicableItemTable.builder()
                    .readData(new ResourceLocation(NFFGirls.MOD_ID, "taming_items/hmag_kobold.json"),NFFGirlsDataReaders::readMobApplicableItemTable)
                    .build());
    public static final NFURegistry.Accessor<MobApplicableItemTable> HMAG_REDCAP =
            TAMING_ITEMS.register("taming_hmag_redcap", () -> MobApplicableItemTable.builder()
                    .readData(new ResourceLocation(NFFGirls.MOD_ID, "taming_items/hmag_redcap.json"),NFFGirlsDataReaders::readMobApplicableItemTable)
                    .build());
}
