package net.sodiumzh.nff.girls.registry;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nfu.entity.MobApplicableItemTable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = NFFGirls.MOD_ID)
public class NFFGirlsHealingItemMappings {

    private static final Map<EntityType<? extends Mob>, Supplier<MobApplicableItemTable>> TABLE = new HashMap<>();

    public static Map<EntityType<? extends Mob>, Supplier<MobApplicableItemTable>> getTable() {
        return TABLE;
    }

    public static Optional<MobApplicableItemTable> get(EntityType<?> type) {
        return Optional.ofNullable(TABLE.get(type)).map(Supplier::get);
    }

    public static void put(EntityType<? extends Mob> type, Supplier<MobApplicableItemTable> table) {
        TABLE.put(type, table);
    }

    @SubscribeEvent
    public static void register(FMLCommonSetupEvent event) {
        TABLE.put(NFFGirlsEntityTypes.HMAG_ALRAUNE.get(), NFFGirlsHealingItems.PLANT);
        TABLE.put(NFFGirlsEntityTypes.HMAG_BANSHEE.get(), NFFGirlsHealingItems.UNDEAD);
        TABLE.put(NFFGirlsEntityTypes.HMAG_CREEPER_GIRL.get(), NFFGirlsHealingItems.CREEPER);
        TABLE.put(NFFGirlsEntityTypes.HMAG_CRIMSON_SLAUGHTERER.get(), NFFGirlsHealingItems.CRIMSON);
        TABLE.put(NFFGirlsEntityTypes.HMAG_CURSED_DOLL.get(), NFFGirlsHealingItems.CLOTH_DOLL);
        TABLE.put(NFFGirlsEntityTypes.HMAG_DODOMEKI.get(), NFFGirlsHealingItems.UNDEAD);
        TABLE.put(NFFGirlsEntityTypes.HMAG_DROWNED_GIRL.get(), NFFGirlsHealingItems.UNDEAD);
        TABLE.put(NFFGirlsEntityTypes.HMAG_DULLAHAN.get(), NFFGirlsHealingItems.UNDEAD);
        TABLE.put(NFFGirlsEntityTypes.HMAG_ENDER_EXECUTOR.get(), NFFGirlsHealingItems.ENDERMAN);
        TABLE.put(NFFGirlsEntityTypes.HMAG_GHASTLY_SEEKER.get(), NFFGirlsHealingItems.UNDEAD);
        TABLE.put(NFFGirlsEntityTypes.HMAG_GLARYAD.get(), NFFGirlsHealingItems.PLANT);
        TABLE.put(NFFGirlsEntityTypes.HMAG_HARPY.get(), NFFGirlsHealingItems.ANIMAL);
        TABLE.put(NFFGirlsEntityTypes.HMAG_HORNET.get(), NFFGirlsHealingItems.BEE);
        TABLE.put(NFFGirlsEntityTypes.HMAG_HUSK_GIRL.get(), NFFGirlsHealingItems.UNDEAD);
        TABLE.put(NFFGirlsEntityTypes.HMAG_IMP.get(), NFFGirlsHealingItems.GENERAL_HUMANOID_0);
        TABLE.put(NFFGirlsEntityTypes.HMAG_JACK_FROST.get(), NFFGirlsHealingItems.SNOWMAN);
        TABLE.put(NFFGirlsEntityTypes.HMAG_JIANGSHI.get(), NFFGirlsHealingItems.UNDEAD);
        TABLE.put(NFFGirlsEntityTypes.HMAG_KOBOLD.get(), NFFGirlsHealingItems.GENERAL_HUMANOID_0);
        TABLE.put(NFFGirlsEntityTypes.HMAG_MELTY_MONSTER.get(), NFFGirlsHealingItems.BLAZE);
        TABLE.put(NFFGirlsEntityTypes.HMAG_NECROTIC_REAPER.get(), NFFGirlsHealingItems.UNDEAD);
        TABLE.put(NFFGirlsEntityTypes.HMAG_NIGHTWALKER.get(), NFFGirlsHealingItems.CLAY_DOLL);
        TABLE.put(NFFGirlsEntityTypes.HMAG_REDCAP.get(), NFFGirlsHealingItems.GENERAL_HUMANOID_0);
        TABLE.put(NFFGirlsEntityTypes.HMAG_SKELETON_GIRL.get(), NFFGirlsHealingItems.UNDEAD);
        TABLE.put(NFFGirlsEntityTypes.HMAG_SLIME_GIRL.get(), NFFGirlsHealingItems.SLIME);
        TABLE.put(NFFGirlsEntityTypes.HMAG_SNOW_CANINE.get(), NFFGirlsHealingItems.ANIMAL);
        TABLE.put(NFFGirlsEntityTypes.HMAG_STRAY_GIRL.get(), NFFGirlsHealingItems.UNDEAD);
        TABLE.put(NFFGirlsEntityTypes.HMAG_WITHER_SKELETON_GIRL.get(), NFFGirlsHealingItems.UNDEAD);
        TABLE.put(NFFGirlsEntityTypes.HMAG_ZOMBIE_GIRL.get(), NFFGirlsHealingItems.UNDEAD);
    }


}
