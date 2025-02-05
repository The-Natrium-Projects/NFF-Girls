package net.sodiumzh.nff.girls.registry;

import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.sodiumzh.nautils.statics.NaUtilsTagStatics;
import net.sodiumzh.nff.girls.NFFGirls;

public class NFFGirlsTags
{
	public static final TagKey<Block> AFFECTS_CRIMSON_SLAUGHTERER = NaUtilsTagStatics.createBlockTag(
			NFFGirls.MOD_ID, "affects_crimson_slaughterer");
	public static final TagKey<Block> NIGHTWALKER_MAGIC_BALL_AFFECTS = NaUtilsTagStatics.createBlockTag
			(NFFGirls.MOD_ID, "nightwalker_magic_ball_affects");
	public static final TagKey<Block> CAN_BEFRIEND_NIGHTWALKERS_ON = NaUtilsTagStatics.createBlockTag(
			NFFGirls.MOD_ID, "can_tame_nightwalkers_on");
	public static final TagKey<Block> CAN_GROW_ENDERBERRY_ON = NaUtilsTagStatics.createBlockTag(
		NFFGirls.MOD_ID, "can_grow_enderberry_on");

	public static final TagKey<Item> DEATH_CRYSTAL_INGREDIENTS = NaUtilsTagStatics.createItemTag(
			NFFGirls.MOD_ID, "death_crystal_ingredients");
	public static final TagKey<Item> DEATH_CRYSTAL_INGREDIENTS_B = NaUtilsTagStatics.createItemTag(
			NFFGirls.MOD_ID, "death_crystal_ingredients_b");
	public static final TagKey<Item> ENDER_FRUIT_JAM_OPTIONAL_FRUITS = NaUtilsTagStatics.createItemTag(
			NFFGirls.MOD_ID, "ender_fruit_jam_optional_fruits");
	public static final TagKey<Item> ENDERBERRY_CRAFTING_INGREDIENTS_A = NaUtilsTagStatics.createItemTag(
			NFFGirls.MOD_ID, "enderberry_crafting_ingredients_a");
	public static final TagKey<Item> ENDERBERRY_CRAFTING_INGREDIENTS_B = NaUtilsTagStatics.createItemTag(
			NFFGirls.MOD_ID, "enderberry_crafting_ingredients_b");
	public static final TagKey<Item> HMAG_BERRIES = NaUtilsTagStatics.createItemTag(NFFGirls.MOD_ID, "hmag_berries");
	public static final TagKey<Item> SOUL_CLOTH_INGREDIENTS = NaUtilsTagStatics.createItemTag(
			NFFGirls.MOD_ID, "soul_cloth_ingredients");
	/** Bow-shooting mobs shoot vanilla arrows instead of custom arrows when using bow items with this tag. */
	public static final TagKey<Item> USES_VANILLA_ARROWS = NaUtilsTagStatics.createItemTag(
			NFFGirls.MOD_ID, "uses_vanilla_arrows");

	public static final TagKey<EntityType<?>> IGNORES_UNDEAD_AFFINITY = NaUtilsTagStatics.createEntityTypeTag(
			NFFGirls.MOD_ID, "ignores_undead_affinity");
	public static final TagKey<EntityType<?>> IGNORES_MAGICAL_GEL_SLOWNESS = NaUtilsTagStatics.createEntityTypeTag(
			NFFGirls.MOD_ID, "ignores_magical_gel_slowness");
	public static final TagKey<EntityType<?>> USES_FORTUNE_AS_LOOTING = NaUtilsTagStatics.createEntityTypeTag(
			NFFGirls.MOD_ID, "uses_fortune_as_looting");
	public static final TagKey<EntityType<?>> AFFECTED_BY_UNDEAD_AFFINITY = NaUtilsTagStatics.createEntityTypeTag(
			NFFGirls.MOD_ID, "affected_by_undead_affinity");
	public static final TagKey<EntityType<?>> CAN_EQUIP_SOUL_AMULET = NaUtilsTagStatics.createEntityTypeTag(
			NFFGirls.MOD_ID, "can_equip_soul_amulet");
	public static final TagKey<EntityType<?>> CAN_EQUIP_POISONOUS_THORN = NaUtilsTagStatics.createEntityTypeTag(
			NFFGirls.MOD_ID, "can_equip_poisonous_thorn");
	public static final TagKey<EntityType<?>> NEUTRAL_ON_HIGH_PROGRESS = NaUtilsTagStatics.createEntityTypeTag(
		NFFGirls.MOD_ID, "neutral_on_high_progress");
	/*
	protected static TagKey<Block> blockTag(String name)
	{
		return TagKey.create(Registries.BLOCK, new ResourceLocation(NFFGirls.MOD_ID, name));
	}
	
	protected static TagKey<Item> itemTag(String name)
	{
		return TagKey.create(Registries.ITEM, new ResourceLocation(NFFGirls.MOD_ID, name));
	}
	
	protected static TagKey<EntityType<?>> entityTag(String name)
	{
		return TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(NFFGirls.MOD_ID, name));
	}*/
	
}
