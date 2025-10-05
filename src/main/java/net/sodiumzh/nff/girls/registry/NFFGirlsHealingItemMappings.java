package net.sodiumzh.nff.girls.registry;

import net.minecraft.world.entity.EntityType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nfu.entity.MobApplicableItemTable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = NFFGirls.MOD_ID)
public class NFFGirlsHealingItemMappings {

    private static final Map<EntityType<?>, MobApplicableItemTable> TABLE = new HashMap<>();

    public static Map<EntityType<?>, MobApplicableItemTable> getTable() {
        return TABLE;
    }

    public static Optional<MobApplicableItemTable> get(EntityType<?> type) {
        return Optional.ofNullable(TABLE.get(type));
    }

    @SubscribeEvent
    public static void register(FMLCommonSetupEvent event) {
        TABLE.put(NFFGirlsEntityTypes.HMAG_ALRAUNE.get(), NFFGirlsHealingItems.PLANT.get());
        TABLE.put(NFFGirlsEntityTypes.HMAG_BANSHEE.get(), NFFGirlsHealingItems.UNDEAD.get());
        TABLE.put(NFFGirlsEntityTypes.HMAG_CREEPER_GIRL.get(), NFFGirlsHealingItems.CREEPER.get());
        TABLE.put(NFFGirlsEntityTypes.HMAG_CRIMSON_SLAUGHTERER.get(), NFFGirlsHealingItems.CRIMSON.get());
        TABLE.put(NFFGirlsEntityTypes.HMAG_CURSED_DOLL.get(), NFFGirlsHealingItems.CLOTH_DOLL.get());
        TABLE.put(NFFGirlsEntityTypes.HMAG_DODOMEKI.get(), NFFGirlsHealingItems.UNDEAD.get());
        TABLE.put(NFFGirlsEntityTypes.HMAG_DROWNED_GIRL.get(), NFFGirlsHealingItems.UNDEAD.get());
        TABLE.put(NFFGirlsEntityTypes.HMAG_DULLAHAN.get(), NFFGirlsHealingItems.UNDEAD.get());
        TABLE.put(NFFGirlsEntityTypes.HMAG_ENDER_EXECUTOR.get(), NFFGirlsHealingItems.ENDERMAN.get());
        TABLE.put(NFFGirlsEntityTypes.HMAG_GHASTLY_SEEKER.get(), NFFGirlsHealingItems.UNDEAD.get());
        TABLE.put(NFFGirlsEntityTypes.HMAG_GLARYAD.get(), NFFGirlsHealingItems.PLANT.get());
        TABLE.put(NFFGirlsEntityTypes.HMAG_HARPY.get(), NFFGirlsHealingItems.ANIMAL.get());
        TABLE.put(NFFGirlsEntityTypes.HMAG_HORNET.get(), NFFGirlsHealingItems.BEE.get());
        TABLE.put(NFFGirlsEntityTypes.HMAG_HUSK_GIRL.get(), NFFGirlsHealingItems.UNDEAD.get());
        TABLE.put(NFFGirlsEntityTypes.HMAG_IMP.get(), NFFGirlsHealingItems.GENERAL_HUMANOID_0.get());
        TABLE.put(NFFGirlsEntityTypes.HMAG_JACK_FROST.get(), NFFGirlsHealingItems.SNOWMAN.get());
        TABLE.put(NFFGirlsEntityTypes.HMAG_JIANGSHI.get(), NFFGirlsHealingItems.UNDEAD.get());
        TABLE.put(NFFGirlsEntityTypes.HMAG_KOBOLD.get(), NFFGirlsHealingItems.GENERAL_HUMANOID_0.get());
        TABLE.put(NFFGirlsEntityTypes.HMAG_MELTY_MONSTER.get(), NFFGirlsHealingItems.BLAZE.get());
        TABLE.put(NFFGirlsEntityTypes.HMAG_NECROTIC_REAPER.get(), NFFGirlsHealingItems.UNDEAD.get());
        TABLE.put(NFFGirlsEntityTypes.HMAG_NIGHTWALKER.get(), NFFGirlsHealingItems.CLAY_DOLL.get());
        TABLE.put(NFFGirlsEntityTypes.HMAG_REDCAP.get(), NFFGirlsHealingItems.GENERAL_HUMANOID_0.get());
        TABLE.put(NFFGirlsEntityTypes.HMAG_SKELETON_GIRL.get(), NFFGirlsHealingItems.UNDEAD.get());
        TABLE.put(NFFGirlsEntityTypes.HMAG_SLIME_GIRL.get(), NFFGirlsHealingItems.SLIME.get());
        TABLE.put(NFFGirlsEntityTypes.HMAG_SNOW_CANINE.get(), NFFGirlsHealingItems.ANIMAL.get());
        TABLE.put(NFFGirlsEntityTypes.HMAG_STRAY_GIRL.get(), NFFGirlsHealingItems.UNDEAD.get());
        TABLE.put(NFFGirlsEntityTypes.HMAG_WITHER_SKELETON_GIRL.get(), NFFGirlsHealingItems.UNDEAD.get());
        TABLE.put(NFFGirlsEntityTypes.HMAG_ZOMBIE_GIRL.get(), NFFGirlsHealingItems.UNDEAD.get());
    }


}
