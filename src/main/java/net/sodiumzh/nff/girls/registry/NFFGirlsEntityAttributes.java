package net.sodiumzh.nff.girls.registry;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.eventlistener.NFFGirlsEntityEventListeners;
import org.w3c.dom.ranges.Range;

/**
 * Attributes to handle NFF-Girls-only mob mechanics. Note: these attributes only work on NFF-Girls mobs but not other
 * mobs.
 */
public class NFFGirlsEntityAttributes {

    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, NFFGirls.MOD_ID);

    /**
     * For additional loot level. Rounded to integer before applying.
     * Handled in {@link NFFGirlsEntityEventListeners#onGetLootingLevel}.
     */
    public static final RegistryObject<Attribute> LOOTING_LEVEL = ATTRIBUTES.register("nffgirls.looting_level",
        () -> new RangedAttribute("nffgirls.looting_level", 0d, 0d, 1024d));

    /**
     * Damage will be multiplied by this value when attacking water-sensitive mobs.
     * Handled in {@link NFFGirlsEntityEventListeners#handleAttributesOnFinalizingDamage}.
     */
    public static final RegistryObject<Attribute> WATER_ASPECT = ATTRIBUTES.register("nffgirls.water_aspect",
        () -> new RangedAttribute("nffgirls.water_aspect", 0d, 0d, 1024d));

    /**
     * Damage will be multiplied by this value when attacking undead mobs.
     * Handled in {@link NFFGirlsEntityEventListeners#handleAttributesOnFinalizingDamage}.
     */
    public static final RegistryObject<Attribute> ANTI_UNDEAD = ATTRIBUTES.register("nffgirls.anti_undead",
        () -> new RangedAttribute("nffgirls.anti_undead", 0d, 0d, 1024d));

    /**
     * Damage will be multiplied by this value when attacking arthropod mobs
     * Handled in {@link NFFGirlsEntityEventListeners#handleAttributesOnFinalizingDamage}.
     */
    public static final RegistryObject<Attribute> ANTI_ARTHROPOD = ATTRIBUTES.register("nffgirls.anti_arthropod",
        () -> new RangedAttribute("nffgirls.anti_arthropod", 0d, 0d, 1024d));

    /**
     *  Damage will be multiplied by this value when attacking aquatic mobs.
     *  Handled in {@link NFFGirlsEntityEventListeners#handleAttributesOnFinalizingDamage}.
     */
    public static final RegistryObject<Attribute> ANTI_AQUATIC = ATTRIBUTES.register("nffgirls.anti_aquatic",
        () -> new RangedAttribute("nffgirls.anti_aquatic", 0d, 0d, 1024d));

    /**
     * When critical, the damage will be 1.5x. This is multiplied separately from "anti-type" damage boosts.
     * Handled in {@link NFFGirlsEntityEventListeners#handleAttributesOnFinalizingDamage}.
     */
    public static final RegistryObject<Attribute> CRITICAL_RATE = ATTRIBUTES.register("nffgirls.critical_rate",
        () -> new RangedAttribute("nffgirls.critical_rate", 0d, 0d, 1024d));

    /**
     * Mob will be healed my this amount each second.
     */
    public static final RegistryObject<Attribute> PERSISTENT_HEALING_PER_SECOND = ATTRIBUTES.register("nffgirls.persistent_healing",
        () -> new RangedAttribute("nffgirls.persistent_healing", 0d, 0d, 1024d));

    /**
     * Mob will cause poison and slowness when hitting the enemy if having this attribute.
     */
    public static final RegistryObject<Attribute> POISON_ASPECT = ATTRIBUTES.register("nffgirls.poison_aspect",
        () -> new RangedAttribute("nffgirls.poison_aspect", 0d, 0d, 1024d));

    /**
     * Mob will cause wither when hitting the enemy if having this attribute.
     */
    public static final RegistryObject<Attribute> WITHER_ASPECT = ATTRIBUTES.register("nffgirls.wither_aspect",
        () -> new RangedAttribute("nffgirls.wither_aspect", 0d, 0d, 1024d));

}
