package net.sodiumzh.nff.girls.registry;

import java.util.HashMap;
import java.util.Map;

import com.github.mechalopa.hmag.registry.ModItems;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.item.Items;
import net.sodiumzh.nautils.registries.NaUtilsRegistries;
import net.sodiumzh.nautils.registries.NaUtilsRegistry;
import net.sodiumzh.nautils.registries.RegistryEntryCollection;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nautils.entity.ItemApplyingToMobTable;
import net.sodiumzh.nff.girls.data.NFFGirlsDataReaders;

public class NFFGirlsHealingItems
{
	public static final RegistryEntryCollection<ItemApplyingToMobTable> HEALING_ITEMS =
			RegistryEntryCollection.create(NaUtilsRegistries.ITEM_APPLYING_TO_MOB_TABLES, NFFGirls.MOD_ID);

	public static final NaUtilsRegistry.Accessor<ItemApplyingToMobTable> NONE =
			HEALING_ITEMS.register("healing_none", () -> ItemApplyingToMobTable.builder().build());
	
	public static final NaUtilsRegistry.Accessor<ItemApplyingToMobTable> UNDEAD =
			HEALING_ITEMS.register("healing_undead", () -> ItemApplyingToMobTable.builder()
			//.add(ModItems.SOUL_POWDER.get(), 5f)
			//.add(ModItems.SOUL_APPLE.get(), 15f)
			.readData(new ResourceLocation(NFFGirls.MOD_ID, "healing_items/undead.json"), NFFGirlsDataReaders::readItemApplyingToMobTable)
			.build());
	
	public static final NaUtilsRegistry.Accessor<ItemApplyingToMobTable> CREEPER =
			HEALING_ITEMS.register("healing_creeper", () -> ItemApplyingToMobTable.builder()
			/*.add(Items.GUNPOWDER, 5.0f)
			.add(Items.REDSTONE, 5.0f)
			.add(Items.REDSTONE_BLOCK, 15.0f)*/
			.readData(new ResourceLocation(NFFGirls.MOD_ID, "healing_items/creeper.json"), NFFGirlsDataReaders::readItemApplyingToMobTable)
			.build());
	
	public static final NaUtilsRegistry.Accessor<ItemApplyingToMobTable> ENDERMAN =
			HEALING_ITEMS.register("healing_enderman", () -> ItemApplyingToMobTable.builder()
			/*.add(Items.ENDER_EYE, 5.0f)
			.add(ModItems.ANCIENT_STONE.get(), 15.0f)*/
			.readData(new ResourceLocation(NFFGirls.MOD_ID, "healing_items/enderman.json"), NFFGirlsDataReaders::readItemApplyingToMobTable)
			.build());

	public static final NaUtilsRegistry.Accessor<ItemApplyingToMobTable> GHAST =
			HEALING_ITEMS.register("healing_ghast", () -> ItemApplyingToMobTable.builder()
			/*.add(Items.GUNPOWDER, 5.0f)
			.add(ModItems.SOUL_POWDER.get(), 5f)
			.add(ModItems.SOUL_APPLE.get(), 15f)*/
			.readData(new ResourceLocation(NFFGirls.MOD_ID, "healing_items/ghast.json"), NFFGirlsDataReaders::readItemApplyingToMobTable)
			.build());
	
	public static final NaUtilsRegistry.Accessor<ItemApplyingToMobTable> PLANT =
			HEALING_ITEMS.register("healing_plant", () -> ItemApplyingToMobTable.builder()
			/*.add(Items.WHEAT_SEEDS, 2.0f)
			.add(Items.BONE_MEAL, 5.0f)
			.add(Items.SPORE_BLOSSOM, 15f)
			.add(ModItems.MYSTERIOUS_PETAL.get(), mob -> (double)(double)mob.getMaxHealth())*/
			.readData(new ResourceLocation(NFFGirls.MOD_ID, "healing_items/plant.json"), NFFGirlsDataReaders::readItemApplyingToMobTable)
			.build());
	
	public static final NaUtilsRegistry.Accessor<ItemApplyingToMobTable> ANIMAL =
			HEALING_ITEMS.register("healing_animal", () -> ItemApplyingToMobTable.builder()
			/*.add(Items.COOKIE, 5f)
			.add(Items.COOKED_CHICKEN, 8f)
			.add(Items.COOKED_RABBIT, 8f)
			.add(Items.COOKED_MUTTON, 8f)
			.add(Items.COOKED_BEEF, 10f)
			.add(Items.COOKED_PORKCHOP, 10f)
			.add(ModItems.COOKED_RAVAGER_MEAT.get(), 30f)
			.add("twilightforest:cooked_meef", 8f)
			.add(Items.GOLDEN_APPLE, mob -> (double)mob.getMaxHealth())*/
			.readData(new ResourceLocation(NFFGirls.MOD_ID, "healing_items/animal.json"), NFFGirlsDataReaders::readItemApplyingToMobTable)
			.build());
	
	public static final NaUtilsRegistry.Accessor<ItemApplyingToMobTable> BEE =
			HEALING_ITEMS.register("healing_bee", () -> ItemApplyingToMobTable.builder()
			/*.add(Items.HONEY_BOTTLE, 5.0f)
			.add(Items.HONEYCOMB, 10.0f)
			.add(Items.HONEY_BLOCK, 15.0f)
			.add(ModItems.MYSTERIOUS_PETAL.get(), mob -> (double)mob.getMaxHealth())*/
			.readData(new ResourceLocation(NFFGirls.MOD_ID, "healing_items/bee.json"), NFFGirlsDataReaders::readItemApplyingToMobTable)
			.build());
	
	public static final NaUtilsRegistry.Accessor<ItemApplyingToMobTable>
			GENERAL_HUMANOID_0 = HEALING_ITEMS.register("healing_general_humanoid_0", () -> ItemApplyingToMobTable.builder()
			/*.add(Items.APPLE, 5f)
			.add(Items.COOKIE, 5f)
			.add(Items.PUMPKIN_PIE, 15f)
			.add(ModItems.LEMON.get(), 10f)
			.add(ModItems.LEMON_PIE.get(), 20f)
			.add(Items.GOLDEN_APPLE, mob -> (double)mob.getMaxHealth())*/
			.readData(new ResourceLocation(NFFGirls.MOD_ID, "healing_items/general_humanoid_0.json"), NFFGirlsDataReaders::readItemApplyingToMobTable)
			.build());
	
	public static final NaUtilsRegistry.Accessor<ItemApplyingToMobTable>
			SNOWMAN = HEALING_ITEMS.register("healing_snowman", () -> ItemApplyingToMobTable.builder()
			/*.add(Items.SNOWBALL, 2f)
			.add(Items.SNOW_BLOCK, 5f)
			.add(Items.PUMPKIN_PIE, 15f)
			.add(Items.GOLDEN_APPLE, mob -> (double)mob.getMaxHealth())*/
			.readData(new ResourceLocation(NFFGirls.MOD_ID, "healing_items/snowman.json"), NFFGirlsDataReaders::readItemApplyingToMobTable)
			.build());

	public static final NaUtilsRegistry.Accessor<ItemApplyingToMobTable>
			SLIME = HEALING_ITEMS.register("healing_slime", () -> ItemApplyingToMobTable.builder()
			/*.add(Items.SLIME_BALL, 5f)
			.add(NFFGirlsItems.MAGICAL_GEL_BALL.get(), 15f)
			.add(ModItems.CUBIC_NUCLEUS.get(), mob -> (double)mob.getMaxHealth())*/
			.readData(new ResourceLocation(NFFGirls.MOD_ID, "healing_items/slime.json"), NFFGirlsDataReaders::readItemApplyingToMobTable)
			.build());
	
	public static final NaUtilsRegistry.Accessor<ItemApplyingToMobTable>
			CRIMSON = HEALING_ITEMS.register("healing_crimson", () -> ItemApplyingToMobTable.builder()
			/*.add(Items.CRIMSON_FUNGUS, 5f)
			.add(Items.NETHER_WART, 5f)
			.add(Items.SHROOMLIGHT, 15f)
			.add(Items.GOLDEN_APPLE, mob -> (double)mob.getMaxHealth())*/
			.readData(new ResourceLocation(NFFGirls.MOD_ID, "healing_items/crimson.json"), NFFGirlsDataReaders::readItemApplyingToMobTable)
			.build());
	
	public static final NaUtilsRegistry.Accessor<ItemApplyingToMobTable>
			CLOTH_DOLL = HEALING_ITEMS.register("healing_cloth_doll", () -> ItemApplyingToMobTable.builder()
			/*.add(Items.STRING, 2f)
			.add(Items.WHITE_WOOL, 5f)
			.add(Items.LIGHT_GRAY_WOOL, 5f)
			.add(Items.GRAY_WOOL, 5f)
			.add(Items.BLACK_WOOL, 5f)
			.add(Items.BROWN_WOOL, 5f)
			.add(Items.LAPIS_LAZULI, 10f)
			.add(Items.EMERALD, 15f)
			.add(ModItems.CUBIC_NUCLEUS.get(), mob -> (double)mob.getMaxHealth())*/
			.readData(new ResourceLocation(NFFGirls.MOD_ID, "healing_items/cloth_doll.json"), NFFGirlsDataReaders::readItemApplyingToMobTable)
			.build());
	
	public static final NaUtilsRegistry.Accessor<ItemApplyingToMobTable>
			BLAZE = HEALING_ITEMS.register("healing_blaze", () -> ItemApplyingToMobTable.builder()
			/*.add(Items.COAL, 5f)
			.add(Items.FIRE_CHARGE, 10f)
			.add(Items.BLAZE_POWDER, 15f)
			.add(ModItems.BURNING_CORE.get(), mob -> (double)mob.getMaxHealth())*/
			.readData(new ResourceLocation(NFFGirls.MOD_ID, "healing_items/blaze.json"), NFFGirlsDataReaders::readItemApplyingToMobTable)
			.build());
	
	public static final NaUtilsRegistry.Accessor<ItemApplyingToMobTable>
			CLAY_DOLL = HEALING_ITEMS.register("healing_clay_doll", () -> ItemApplyingToMobTable.builder()
			.add(Items.CLAY_BALL, 2f)
			.add(Items.LAPIS_LAZULI, 5f)
			.add(ModItems.ANCIENT_STONE.get(), 15f)
			.add(Items.GOLDEN_APPLE, mob -> (double)mob.getMaxHealth())
			.readData(new ResourceLocation(NFFGirls.MOD_ID, "healing_items/clay_doll.json"), NFFGirlsDataReaders::readItemApplyingToMobTable)
			.build());
	
}
