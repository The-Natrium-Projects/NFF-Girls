package net.sodiumzh.nff.girls.registry;

import net.minecraft.resources.ResourceLocation;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.data.NFFGirlsDataReaders;
import net.sodiumzh.nfu.entity.MobApplicableItemTable;
import net.sodiumzh.nfu.network.AvailableSide;
import net.sodiumzh.nfu.registry.NFURegistry;
import net.sodiumzh.nfu.registry.NFURegistryEntryCollection;

public class NFFGirlsFriendingItems {
    // For loading registry
    public static void init() {}

    public static final NFURegistry<MobApplicableItemTable> REGISTRY =
        new NFURegistry<MobApplicableItemTable>(new ResourceLocation(NFFGirls.MOD_ID, "friending_items"))
            .setSide(AvailableSide.SERVER)
            .setLoadTiming(NFURegistry.LoadTiming.SIDE_SETUP)
            .setAllowsAccessBeforeLoading(false);

    public static final NFURegistryEntryCollection<MobApplicableItemTable> FRIENDING_ITEM_COLLECTION =
        NFURegistryEntryCollection.create(REGISTRY, NFFGirls.MOD_ID);

    // For Alraune
    public static final NFURegistry.Accessor<MobApplicableItemTable> PLANT_A =
        FRIENDING_ITEM_COLLECTION.register("plant_a", () -> MobApplicableItemTable.builder()
            .readData(new ResourceLocation(NFFGirls.MOD_ID, "friending/plant_a.json"), NFFGirlsDataReaders::readMobApplicableItemTable)
            .build());
    // For Glaryad
    public static final NFURegistry.Accessor<MobApplicableItemTable> PLANT_B =
        FRIENDING_ITEM_COLLECTION.register("plant_b", () -> MobApplicableItemTable.builder()
            .readData(new ResourceLocation(NFFGirls.MOD_ID, "friending/plant_b.json"), NFFGirlsDataReaders::readMobApplicableItemTable)
            .build());
    // For Zombie Girls etc.
    public static final NFURegistry.Accessor<MobApplicableItemTable> UNDEAD_A =
        FRIENDING_ITEM_COLLECTION.register("undead_a", () -> MobApplicableItemTable.builder()
            .readData(new ResourceLocation(NFFGirls.MOD_ID, "friending/undead_a.json"),NFFGirlsDataReaders::readMobApplicableItemTable)
            .build());
    // For Skeleton Girls etc.
    public static final NFURegistry.Accessor<MobApplicableItemTable> UNDEAD_B =
        FRIENDING_ITEM_COLLECTION.register("undead_b", () -> MobApplicableItemTable.builder()
            .readData(new ResourceLocation(NFFGirls.MOD_ID, "friending/undead_b.json"),NFFGirlsDataReaders::readMobApplicableItemTable)
            .build());
    // For Ghastly Seeker
    public static final NFURegistry.Accessor<MobApplicableItemTable> UNDEAD_NETHER_B =
        FRIENDING_ITEM_COLLECTION.register("undead_nether_b", () -> MobApplicableItemTable.builder()
            .readData(new ResourceLocation(NFFGirls.MOD_ID, "friending/undead_nether_b.json"),NFFGirlsDataReaders::readMobApplicableItemTable)
            .build());
    public static final NFURegistry.Accessor<MobApplicableItemTable> UNDEAD_C =
        FRIENDING_ITEM_COLLECTION.register("undead_c", () -> MobApplicableItemTable.builder()
            .readData(new ResourceLocation(NFFGirls.MOD_ID, "friending/undead_c.json"),NFFGirlsDataReaders::readMobApplicableItemTable)
            .build());
    // For Wither Skeleton Girl
    public static final NFURegistry.Accessor<MobApplicableItemTable> UNDEAD_NETHER_C =
        FRIENDING_ITEM_COLLECTION.register("undead_nether_c", () -> MobApplicableItemTable.builder()
            .readData(new ResourceLocation(NFFGirls.MOD_ID, "friending/undead_nether_c.json"),NFFGirlsDataReaders::readMobApplicableItemTable)
            .build());

    // For Creeper Girl
    public static final NFURegistry.Accessor<MobApplicableItemTable> CREEPER =
        FRIENDING_ITEM_COLLECTION.register("creeper", () -> MobApplicableItemTable.builder()
            .readData(new ResourceLocation(NFFGirls.MOD_ID, "friending/creeper.json"),NFFGirlsDataReaders::readMobApplicableItemTable)
            .build());
    // For Ender Executor
    public static final NFURegistry.Accessor<MobApplicableItemTable> ENDERMAN =
        FRIENDING_ITEM_COLLECTION.register("enderman", () -> MobApplicableItemTable.builder()
            .readData(new ResourceLocation(NFFGirls.MOD_ID, "friending/enderman.json"),NFFGirlsDataReaders::readMobApplicableItemTable)
            .build());
    // For Hornet
    public static final NFURegistry.Accessor<MobApplicableItemTable> BEE =
        FRIENDING_ITEM_COLLECTION.register("bee", () -> MobApplicableItemTable.builder()
            .readData(new ResourceLocation(NFFGirls.MOD_ID, "friending/bee.json"),NFFGirlsDataReaders::readMobApplicableItemTable)
            .build());
    // For Melty Monster
    public static final NFURegistry.Accessor<MobApplicableItemTable> BLAZE =
        FRIENDING_ITEM_COLLECTION.register("blaze", () -> MobApplicableItemTable.builder()
            .readData(new ResourceLocation(NFFGirls.MOD_ID, "friending/blaze.json"),NFFGirlsDataReaders::readMobApplicableItemTable)
            .build());
    // For Nightwalker
    public static final NFURegistry.Accessor<MobApplicableItemTable> CLAY_DOLL =
        FRIENDING_ITEM_COLLECTION.register("clay_doll", () -> MobApplicableItemTable.builder()
            .readData(new ResourceLocation(NFFGirls.MOD_ID, "friending/clay_doll.json"),NFFGirlsDataReaders::readMobApplicableItemTable)
            .build());
    // For Jack o'Frost
    public static final NFURegistry.Accessor<MobApplicableItemTable> SNOWMAN =
        FRIENDING_ITEM_COLLECTION.register("snowman", () -> MobApplicableItemTable.builder()
            .readData(new ResourceLocation(NFFGirls.MOD_ID, "friending/snowman.json"),NFFGirlsDataReaders::readMobApplicableItemTable)
            .build());
    // For Harpy and Snow Canine
    public static final NFURegistry.Accessor<MobApplicableItemTable> ANIMAL_A =
        FRIENDING_ITEM_COLLECTION.register("animal_a", () -> MobApplicableItemTable.builder()
            .readData(new ResourceLocation(NFFGirls.MOD_ID, "friending/animal_a.json"),NFFGirlsDataReaders::readMobApplicableItemTable)
            .build());

    // For Crimson Slaughterer
    public static final NFURegistry.Accessor<MobApplicableItemTable> CRIMSON =
        FRIENDING_ITEM_COLLECTION.register("crimson", () -> MobApplicableItemTable.builder()
            .readData(new ResourceLocation(NFFGirls.MOD_ID, "friending/crimson.json"),NFFGirlsDataReaders::readMobApplicableItemTable)
            .build());
    public static final NFURegistry.Accessor<MobApplicableItemTable> HMAG_IMP =
        FRIENDING_ITEM_COLLECTION.register("hmag_imp", () -> MobApplicableItemTable.builder()
            .readData(new ResourceLocation(NFFGirls.MOD_ID, "friending/hmag_imp.json"),NFFGirlsDataReaders::readMobApplicableItemTable)
            .build());
    public static final NFURegistry.Accessor<MobApplicableItemTable> HMAG_KOBOLD =
        FRIENDING_ITEM_COLLECTION.register("hmag_kobold", () -> MobApplicableItemTable.builder()
            .readData(new ResourceLocation(NFFGirls.MOD_ID, "friending/hmag_kobold.json"),NFFGirlsDataReaders::readMobApplicableItemTable)
            .build());
    public static final NFURegistry.Accessor<MobApplicableItemTable> HMAG_REDCAP =
        FRIENDING_ITEM_COLLECTION.register("hmag_redcap", () -> MobApplicableItemTable.builder()
            .readData(new ResourceLocation(NFFGirls.MOD_ID, "friending/hmag_redcap.json"),NFFGirlsDataReaders::readMobApplicableItemTable)
            .build());
}
