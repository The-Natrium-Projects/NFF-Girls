package net.sodiumzh.nff.girls.registry;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.block.EnderberryBushBlock;
import net.sodiumzh.nff.girls.block.SoulCakeBlock;
import net.sodiumzh.nff.girls.block.SoulCarpetBlock;
import net.sodiumzh.nfu.block.BlockMaterial;

@Mod.EventBusSubscriber(modid = NFFGirls.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NFFGirlsBlocks {

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, NFFGirls.MOD_ID);
	
	// General register function for blocks (for simplification)
	public static RegistryObject<Block> regBlock(String name, BlockBehaviour.Properties properties)
	{
		return NFFGirlsBlocks.BLOCKS.register(name, () -> new Block(properties));	
	}
	
	// Register block items. Must be called after the corresponding block is registered!!
	public static RegistryObject<BlockItem> regBlockItem(String name, RegistryObject<Block> block, Item.Properties properties)
	{
		return NFFGirlsItems.ITEMS.register(name, () -> new BlockItem(block.get(), properties));
	}
	
	
	/* Blocks */
	
	public static final RegistryObject<Block> SOUL_CARPET = NFFGirlsBlocks.BLOCKS.register("soul_carpet", () -> 
		new SoulCarpetBlock(BlockMaterial.WOOL.properties(MapColor.COLOR_PURPLE)
				.strength(0.1f)
				.sound(SoundType.WOOL)));
	public static final RegistryObject<Block> SOUL_CAKE = NFFGirlsBlocks.BLOCKS.register("soul_cake", () -> 
		new SoulCakeBlock(BlockMaterial.CAKE.properties()
				.strength(0.5F)
				.sound(SoundType.WOOL)));
	public static final RegistryObject<Block> LUMINOUS_TERRACOTTA = NFFGirlsBlocks.BLOCKS.register("luminous_terracotta", () -> 
		new Block(BlockMaterial.STONE.properties(MapColor.TERRACOTTA_WHITE)
				.requiresCorrectToolForDrops()
				.strength(1.25F, 4.2F)
				.lightLevel(bs -> 7)));
	
	public static final RegistryObject<Block> ENHANCED_LUMINOUS_TERRACOTTA = NFFGirlsBlocks.BLOCKS.register("enhanced_luminous_terracotta", () -> 
		new Block(BlockMaterial.STONE.properties(MapColor.TERRACOTTA_WHITE)
				.requiresCorrectToolForDrops()
				.strength(1.25F, 4.2F)
				.lightLevel(bs -> 15)));

	public static final RegistryObject<EnderberryBushBlock> ENDERBERRY_BUSH = NFFGirlsBlocks.BLOCKS.register("enderberry_bush", () ->
		new EnderberryBushBlock(BlockBehaviour.Properties.of()
			.mapColor(MapColor.PLANT).randomTicks()
			.noCollission().sound(SoundType.SWEET_BERRY_BUSH).pushReaction(PushReaction.DESTROY)
			.lightLevel(bs -> bs.getValue(EnderberryBushBlock.CAN_GROW_ENDERBERRY) && bs.getValue(EnderberryBushBlock.AGE) < EnderberryBushBlock.MAX_AGE ? 0 : 15)));

	/* Block Items */
	public static final RegistryObject<BlockItem> ITEM_SOUL_CARPET = regBlockItem("soul_carpet", SOUL_CARPET, new Item.Properties());
	public static final RegistryObject<BlockItem> ITEM_SOUL_CAKE = regBlockItem("soul_cake", SOUL_CAKE, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
	public static final RegistryObject<BlockItem> ITEM_LUMINOUS_TERRACOTTA = regBlockItem("luminous_terracotta", LUMINOUS_TERRACOTTA, new Item.Properties());
	public static final RegistryObject<BlockItem> ITEM_ENHANCED_LUMINOUS_TERRACOTTA = regBlockItem("enhanced_luminous_terracotta", ENHANCED_LUMINOUS_TERRACOTTA, new Item.Properties());
	
	// Register to event bus
	public static void register(IEventBus eventBus) {
	    BLOCKS.register(eventBus);
	}


}
