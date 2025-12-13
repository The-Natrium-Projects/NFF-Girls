package net.sodiumzh.nff.girls;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.sodiumzh.nff.girls.item.bauble.NFFGirlsBaubleBuilder;
import net.sodiumzh.nff.girls.registry.*;
import net.sodiumzh.nfu.savedata.redirector.SaveDataLocationRedirector;

@Mod(NFFGirls.MOD_ID)
public class NFFGirls
{
    public static final String MOD_ID = "nffgirls";
    private static final String MOD_ID_LEGACY = "dwmg";

    public NFFGirls()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        
        // Config
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, NFFGirlsConfigs.CONFIG);
        modEventBus.addListener(NFFGirlsConfigs::loadConfig);

        // Init NFU registries
        NFFGirlsBaubles.init();
        NFFGirlsHealingItems.init();
        NFFGirlsFriendingItems.init();

        // Set up registries
        NFFGirlsEffects.EFFECTS.register(modEventBus);
        NFFGirlsBlocks.BLOCKS.register(modEventBus);
        NFFGirlsItems.ITEMS.register(modEventBus);
        NFFGirlsBaubles.BAUBLE_ITEMS.register(modEventBus);
        NFFGirlsEntityTypes.ENTITY_TYPES.register(modEventBus);
        NFFGirlsRecipes.RECIPES.register(modEventBus);
        NFFGirlsParticleTypes.PARTICLE_TYPES.register(modEventBus);
        NFFGirlsPotions.POTIONS.register(modEventBus);
        NFFGirlsEntityAttributes.ATTRIBUTES.register(modEventBus);

        // NFU registries
        NFFGirlsHealingItems.HEALING_ITEMS_COLLECTION.merge();
        NFFGirlsFriendingItems.FRIENDING_ITEM_COLLECTION.merge();
        NFFGirlsFunctions.FUNCTIONS.merge();
        NFFGirlsPredicates.PREDICATES.merge();
        NFFGirlsTrades.TRADE_COLLECTIONS.merge();
        NFFGirlsTrades.TRADE_REGISTRIES.merge();
        NFFGirlsBaubles.BAUBLES.merge();
        NFFGirlsBaubles.BAUBLE_EFFECT_CONDITIONS.merge();
        NFFGirlsBaubles.BAUBLE_EQUIPPING_CONDITIONS.merge();
        NFFGirlsEntityAttributeProviders.ATTRIBUTE_PROVIDERS.merge();
        NFFGirlsBaubleBuilder.EQUIPPING_CONDITION_PRESETS.merge();


        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        redirectSaveDataLocations();
    }
    
    private static void redirectSaveDataLocations()
    {
		SaveDataLocationRedirector.get().redirectNamespace(MOD_ID_LEGACY, MOD_ID)
            // Below: since 0.x.26
            .redirectEntityCapability(new ResourceLocation(NFFGirls.MOD_ID_LEGACY, NFFGirlsCapabilityAttachment.KEY_FAVORABILITY_LEGACY), new ResourceLocation(NFFGirls.MOD_ID, NFFGirlsCapabilityAttachment.KEY_FAVORABILITY))
            .redirectEntityCapability(new ResourceLocation(NFFGirls.MOD_ID_LEGACY, NFFGirlsCapabilityAttachment.KEY_TRADE_LEGACY), new ResourceLocation(NFFGirls.MOD_ID, NFFGirlsCapabilityAttachment.KEY_TRADE))
            .redirectEntityCapability(new ResourceLocation(NFFGirls.MOD_ID_LEGACY, NFFGirlsCapabilityAttachment.KEY_UNDEAD_AFFINITY_HANDLER_LEGACY), new ResourceLocation(NFFGirls.MOD_ID, NFFGirlsCapabilityAttachment.KEY_UNDEAD_AFFINITY_HANDLER))
            .redirectEntityCapability(new ResourceLocation(NFFGirls.MOD_ID_LEGACY, NFFGirlsCapabilityAttachment.KEY_XP_LEVEL_LEGACY), new ResourceLocation(NFFGirls.MOD_ID, NFFGirlsCapabilityAttachment.KEY_XP_LEVEL))
            // Below: since 0.x.30
            .redirectItem(new ResourceLocation(MOD_ID, "poisonous_thorn"), new ResourceLocation(MOD_ID, "poison_jade"))
            .redirectItem(new ResourceLocation(MOD_ID, "courage_amulet"), new ResourceLocation(MOD_ID, "courage_badge"))
            .redirectItem(new ResourceLocation(MOD_ID, "courage_amulet_ii"), new ResourceLocation(MOD_ID, "courage_badge_ii"))
            .redirectItem(new ResourceLocation(MOD_ID, "natures_tenderness"), new ResourceLocation(MOD_ID, "natures_tenderness_badge"))
            .redirectItem(new ResourceLocation(MOD_ID, "natures_tenderness_ii"), new ResourceLocation(MOD_ID, "natures_tenderness_badge_ii"))
            .redirectItem(new ResourceLocation(MOD_ID, "natures_tenderness_iii"), new ResourceLocation(MOD_ID, "natures_tenderness_badge_iii"))
            .redirectItem(new ResourceLocation(MOD_ID, "natures_rage"), new ResourceLocation(MOD_ID, "natures_rage_badge"))
            .redirectItem(new ResourceLocation(MOD_ID, "natures_rage_ii"), new ResourceLocation(MOD_ID, "natures_rage_badge_ii"))
            .redirectItem(new ResourceLocation(MOD_ID, "natures_rage_iii"), new ResourceLocation(MOD_ID, "natures_rage_badge_iii"));

    }

}
