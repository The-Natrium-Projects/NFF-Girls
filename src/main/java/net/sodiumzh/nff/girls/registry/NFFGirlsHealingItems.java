package net.sodiumzh.nff.girls.registry;

import net.minecraft.resources.ResourceLocation;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.data.NFFGirlsDataReaders;
import net.sodiumzh.nfu.entity.MobApplicableItemTable;
import net.sodiumzh.nfu.network.AvailableSide;
import net.sodiumzh.nfu.registry.NFURegistry;
import net.sodiumzh.nfu.registry.NFURegistryEntryCollection;

public class NFFGirlsHealingItems
{
    // For loading registry
    public static void init() {}

    public static final int DEFAULT_COOLDOWN = 40;

    public static final NFURegistry<MobApplicableItemTable> REGISTRY =
        new NFURegistry<MobApplicableItemTable>(new ResourceLocation(NFFGirls.MOD_ID, "healing_items"))
            .setSide(AvailableSide.SERVER)
            .setLoadTiming(NFURegistry.LoadTiming.SIDE_SETUP)
            .setAllowsAccessBeforeLoading(false);

    public static final NFURegistryEntryCollection<MobApplicableItemTable> HEALING_ITEMS_COLLECTION =
        NFURegistryEntryCollection.create(REGISTRY, NFFGirls.MOD_ID);

    public static final NFURegistry.Accessor<MobApplicableItemTable> NONE =
        HEALING_ITEMS_COLLECTION.register("none", () -> MobApplicableItemTable.builder().build());

    public static final NFURegistry.Accessor<MobApplicableItemTable> UNDEAD =
        HEALING_ITEMS_COLLECTION.register("undead", () -> MobApplicableItemTable.builder()
            //.add(ModItems.SOUL_POWDER.get(), 5f)
            //.add(ModItems.SOUL_APPLE.get(), 15f)
            .readData(new ResourceLocation(NFFGirls.MOD_ID, "healing/undead.json"), NFFGirlsDataReaders::readMobApplicableItemTable)
            .build());

    public static final NFURegistry.Accessor<MobApplicableItemTable> CREEPER =
        HEALING_ITEMS_COLLECTION.register("creeper", () -> MobApplicableItemTable.builder()
            /*.add(Items.GUNPOWDER, 5.0f)
            .add(Items.REDSTONE, 5.0f)
            .add(Items.REDSTONE_BLOCK, 15.0f)*/
            .readData(new ResourceLocation(NFFGirls.MOD_ID, "healing/creeper.json"), NFFGirlsDataReaders::readMobApplicableItemTable)
            .build());

    public static final NFURegistry.Accessor<MobApplicableItemTable> ENDERMAN =
        HEALING_ITEMS_COLLECTION.register("enderman", () -> MobApplicableItemTable.builder()
            /*.add(Items.ENDER_EYE, 5.0f)
            .add(ModItems.ANCIENT_STONE.get(), 15.0f)*/
            .readData(new ResourceLocation(NFFGirls.MOD_ID, "healing/enderman.json"), NFFGirlsDataReaders::readMobApplicableItemTable)
            .build());

    public static final NFURegistry.Accessor<MobApplicableItemTable> GHAST =
        HEALING_ITEMS_COLLECTION.register("ghast", () -> MobApplicableItemTable.builder()
            /*.add(Items.GUNPOWDER, 5.0f)
            .add(ModItems.SOUL_POWDER.get(), 5f)
            .add(ModItems.SOUL_APPLE.get(), 15f)*/
            .readData(new ResourceLocation(NFFGirls.MOD_ID, "healing/ghast.json"), NFFGirlsDataReaders::readMobApplicableItemTable)
            .build());

    public static final NFURegistry.Accessor<MobApplicableItemTable> PLANT =
        HEALING_ITEMS_COLLECTION.register("plant", () -> MobApplicableItemTable.builder()
            /*.add(Items.WHEAT_SEEDS, 2.0f)
            .add(Items.BONE_MEAL, 5.0f)
            .add(Items.SPORE_BLOSSOM, 15f)
            .add(ModItems.MYSTERIOUS_PETAL.get(), mob -> (double)(double)mob.getMaxHealth())*/
            .readData(new ResourceLocation(NFFGirls.MOD_ID, "healing/plant.json"), NFFGirlsDataReaders::readMobApplicableItemTable)
            .build());

    public static final NFURegistry.Accessor<MobApplicableItemTable> ANIMAL =
        HEALING_ITEMS_COLLECTION.register("animal", () -> MobApplicableItemTable.builder()
            /*.add(Items.COOKIE, 5f)
            .add(Items.COOKED_CHICKEN, 8f)
            .add(Items.COOKED_RABBIT, 8f)
            .add(Items.COOKED_MUTTON, 8f)
            .add(Items.COOKED_BEEF, 10f)
            .add(Items.COOKED_PORKCHOP, 10f)
            .add(ModItems.COOKED_RAVAGER_MEAT.get(), 30f)
            .add("twilightforest:cooked_meef", 8f)
            .add(Items.GOLDEN_APPLE, mob -> (double)mob.getMaxHealth())*/
            .readData(new ResourceLocation(NFFGirls.MOD_ID, "healing/animal.json"), NFFGirlsDataReaders::readMobApplicableItemTable)
            .build());

    public static final NFURegistry.Accessor<MobApplicableItemTable> BEE =
        HEALING_ITEMS_COLLECTION.register("bee", () -> MobApplicableItemTable.builder()
            /*.add(Items.HONEY_BOTTLE, 5.0f)
            .add(Items.HONEYCOMB, 10.0f)
            .add(Items.HONEY_BLOCK, 15.0f)
            .add(ModItems.MYSTERIOUS_PETAL.get(), mob -> (double)mob.getMaxHealth())*/
            .readData(new ResourceLocation(NFFGirls.MOD_ID, "healing/bee.json"), NFFGirlsDataReaders::readMobApplicableItemTable)
            .build());

    public static final NFURegistry.Accessor<MobApplicableItemTable>
        GENERAL_HUMANOID_0 = HEALING_ITEMS_COLLECTION.register("general_humanoid_0", () -> MobApplicableItemTable.builder()
        /*.add(Items.APPLE, 5f)
        .add(Items.COOKIE, 5f)
        .add(Items.PUMPKIN_PIE, 15f)
        .add(ModItems.LEMON.get(), 10f)
        .add(ModItems.LEMON_PIE.get(), 20f)
        .add(Items.GOLDEN_APPLE, mob -> (double)mob.getMaxHealth())*/
        .readData(new ResourceLocation(NFFGirls.MOD_ID, "healing/general_humanoid_0.json"), NFFGirlsDataReaders::readMobApplicableItemTable)
        .build());

    public static final NFURegistry.Accessor<MobApplicableItemTable>
        SNOWMAN = HEALING_ITEMS_COLLECTION.register("snowman", () -> MobApplicableItemTable.builder()
        /*.add(Items.SNOWBALL, 2f)
        .add(Items.SNOW_BLOCK, 5f)
        .add(Items.PUMPKIN_PIE, 15f)
        .add(Items.GOLDEN_APPLE, mob -> (double)mob.getMaxHealth())*/
        .readData(new ResourceLocation(NFFGirls.MOD_ID, "healing/snowman.json"), NFFGirlsDataReaders::readMobApplicableItemTable)
        .build());

    public static final NFURegistry.Accessor<MobApplicableItemTable>
        SLIME = HEALING_ITEMS_COLLECTION.register("slime", () -> MobApplicableItemTable.builder()
        /*.add(Items.SLIME_BALL, 5f)
        .add(NFFGirlsItems.MAGICAL_GEL_BALL.get(), 15f)
        .add(ModItems.CUBIC_NUCLEUS.get(), mob -> (double)mob.getMaxHealth())*/
        .readData(new ResourceLocation(NFFGirls.MOD_ID, "healing/slime.json"), NFFGirlsDataReaders::readMobApplicableItemTable)
        .build());

    public static final NFURegistry.Accessor<MobApplicableItemTable>
        CRIMSON = HEALING_ITEMS_COLLECTION.register("crimson", () -> MobApplicableItemTable.builder()
        /*.add(Items.CRIMSON_FUNGUS, 5f)
        .add(Items.NETHER_WART, 5f)
        .add(Items.SHROOMLIGHT, 15f)
        .add(Items.GOLDEN_APPLE, mob -> (double)mob.getMaxHealth())*/
        .readData(new ResourceLocation(NFFGirls.MOD_ID, "healing/crimson.json"), NFFGirlsDataReaders::readMobApplicableItemTable)
        .build());

    public static final NFURegistry.Accessor<MobApplicableItemTable>
        CLOTH_DOLL = HEALING_ITEMS_COLLECTION.register("cloth_doll", () -> MobApplicableItemTable.builder()
        /*.add(Items.STRING, 2f)
        .add(Items.WHITE_WOOL, 5f)
        .add(Items.LIGHT_GRAY_WOOL, 5f)
        .add(Items.GRAY_WOOL, 5f)
        .add(Items.BLACK_WOOL, 5f)
        .add(Items.BROWN_WOOL, 5f)
        .add(Items.LAPIS_LAZULI, 10f)
        .add(Items.EMERALD, 15f)
        .add(ModItems.CUBIC_NUCLEUS.get(), mob -> (double)mob.getMaxHealth())*/
        .readData(new ResourceLocation(NFFGirls.MOD_ID, "healing/cloth_doll.json"), NFFGirlsDataReaders::readMobApplicableItemTable)
        .build());

    public static final NFURegistry.Accessor<MobApplicableItemTable>
        BLAZE = HEALING_ITEMS_COLLECTION.register("blaze", () -> MobApplicableItemTable.builder()
        /*.add(Items.COAL, 5f)
        .add(Items.FIRE_CHARGE, 10f)
        .add(Items.BLAZE_POWDER, 15f)
        .add(ModItems.BURNING_CORE.get(), mob -> (double)mob.getMaxHealth())*/
        .readData(new ResourceLocation(NFFGirls.MOD_ID, "healing/blaze.json"), NFFGirlsDataReaders::readMobApplicableItemTable)
        .build());

    public static final NFURegistry.Accessor<MobApplicableItemTable>
        CLAY_DOLL = HEALING_ITEMS_COLLECTION.register("clay_doll", () -> MobApplicableItemTable.builder()
        /*.add(Items.CLAY_BALL, 2f)
        .add(Items.LAPIS_LAZULI, 5f)
        .add(ModItems.ANCIENT_STONE.get(), 15f)
        .add(Items.GOLDEN_APPLE, mob -> (double)mob.getMaxHealth())*/
        .readData(new ResourceLocation(NFFGirls.MOD_ID, "healing/clay_doll.json"), NFFGirlsDataReaders::readMobApplicableItemTable)
        .build());

}
