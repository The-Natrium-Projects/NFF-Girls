package net.sodiumzh.nff.girls.registry;

import com.github.mechalopa.hmag.registry.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.entity.hmag.*;
import net.sodiumzh.nff.girls.item.bauble.*;
import net.sodiumzh.nff.girls.item.bauble.bauble.EnderManHandBlockBaubleBehavior;
import net.sodiumzh.nff.girls.item.bauble.bauble.NecroticReaperHandHoeBaubleBehavior;
import net.sodiumzh.nff.girls.util.NFFGirlsPredicate;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nff.services.entity.taming.NFFTamingMapping;
import net.sodiumzh.nfu.function.NFUOperator;
import net.sodiumzh.nfu.item.bauble.BaubleEquippingCondition;
import net.sodiumzh.nfu.item.bauble.NFUBaubleAPI;
import net.sodiumzh.nfu.item.bauble.RegisterBaubleEquippableMobsEvent;
import net.sodiumzh.nfu.item.bauble.RegisterBaublesEvent;
import net.sodiumzh.nfu.registry.NFURegistries;
import net.sodiumzh.nfu.registry.NFURegistry;
import net.sodiumzh.nfu.registry.NFURegistryEntryCollection;
import net.sodiumzh.nfu.registry.NFURegistryGenerateValuesEvent;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Mod.EventBusSubscriber(modid = NFFGirls.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class NFFGirlsBaubles {

    // Just for loading this class
    public static void init(){}

    public static final DeferredRegister<Item> BAUBLE_ITEMS = DeferredRegister.create(
        ForgeRegistries.ITEMS, NFFGirls.MOD_ID);

    public static final NFURegistryEntryCollection<NFUOperator<?, ?>> BAUBLE_EFFECT_CONDITION_COLLECTION =
        NFURegistryEntryCollection.create(NFURegistries.OPERATORS, NFFGirls.MOD_ID);

    public static final NFURegistryEntryCollection<BaubleEquippingCondition> BAUBLE_EQUIPPING_CONDITION_COLLECTION =
        NFURegistryEntryCollection.create(NFUBaubleAPI.EQUIPPING_CONDITIONS, NFFGirls.MOD_ID);

    // SOME COMMON CONDITIONS
    public static final NFURegistry.Accessor<BaubleEquippingCondition> CONDITION_UNDEAD = BAUBLE_EQUIPPING_CONDITION_COLLECTION.register("undead", () ->
        BaubleEquippingCondition.of(args -> args.user().getMobType().equals(MobType.UNDEAD) || args.user().getType().is(NFFGirlsTags.EQUIPS_BAUBLES_AS_UNDEAD))
            .setTranslation("tooltip.nffgirls.bauble.for_undead"));
    public static final NFURegistry.Accessor<BaubleEquippingCondition> CONDITION_SUN_SENSITIVE = BAUBLE_EQUIPPING_CONDITION_COLLECTION.register("sun_sensitive", () ->
        BaubleEquippingCondition.of(args -> INFFTamed.get(args.user()).filter(INFFTamed::enableSunSensitivity).isPresent() || args.user().getType().is(NFFGirlsTags.EQUIPS_BAUBLES_AS_SUN_SENSITIVE))
            .setTranslation("tooltip.nffgirls.bauble.for_sun_sensitive"));
    public static final NFURegistry.Accessor<BaubleEquippingCondition> CONDITION_UNDEAD_AND_SUN_SENSITIVE = BAUBLE_EQUIPPING_CONDITION_COLLECTION.register("undead_and_sun_sensitive", () ->
        CONDITION_UNDEAD.get().or(CONDITION_SUN_SENSITIVE.get())
            .setTranslation("tooltip.nffgirls.bauble.for_undead_and_sun_sensitive"));
    public static final NFURegistry.Accessor<BaubleEquippingCondition> CONDITION_NOT_UNDEAD = BAUBLE_EQUIPPING_CONDITION_COLLECTION.register("not_undead", () ->
        CONDITION_UNDEAD.get().negate().setTranslation("tooltip.nffgirls.bauble.not_undead"));
    public static final NFURegistry.Accessor<BaubleEquippingCondition> CONDITION_ARTHROPOD =  BAUBLE_EQUIPPING_CONDITION_COLLECTION.register("arthropod", () ->
        BaubleEquippingCondition.of(args -> args.user().getMobType().equals(MobType.ARTHROPOD) || args.user().getType().is(NFFGirlsTags.EQUIPS_BAUBLES_AS_ARTHROPOD))
            .setTranslation("tooltip.nffgirls.bauble.for_arthropod"));
    public static final NFURegistry.Accessor<BaubleEquippingCondition> CONDITION_PLANT =  BAUBLE_EQUIPPING_CONDITION_COLLECTION.register("plant", () ->
        BaubleEquippingCondition.of(args -> args.user().getType().is(NFFGirlsTags.PLANT_MOB))
            .setTranslation("tooltip.nffgirls.bauble.for_plant"));
    public static final NFURegistry.Accessor<BaubleEquippingCondition> CONDITION_ARTHROPOD_AND_PLANT =  BAUBLE_EQUIPPING_CONDITION_COLLECTION.register("arthropod_and_plant", () ->
        CONDITION_ARTHROPOD.get().or(CONDITION_PLANT.get())
            .setTranslation("tooltip.nffgirls.bauble.for_arthropod_and_plant"));
    public static final NFURegistry.Accessor<BaubleEquippingCondition> CONDITION_AQUATIC =  BAUBLE_EQUIPPING_CONDITION_COLLECTION.register("aquatic", () ->
        BaubleEquippingCondition.of(args -> args.user().getType().is(NFFGirlsTags.AQUATIC_MOB) || args.user().getMobType().equals(MobType.WATER)
        || Optional.ofNullable(NFFTamingMapping.getTypeBefore(args.user())).filter(t -> t.is(NFFGirlsTags.AQUATIC_MOB)).isPresent())
        .setTranslation("tooltip.nffgirls.bauble.for_aquatic"));
    public static final NFURegistry.Accessor<BaubleEquippingCondition> CONDITION_NETHER =  BAUBLE_EQUIPPING_CONDITION_COLLECTION.register("nether", () ->
        BaubleEquippingCondition.of(args -> args.user().getType().is(NFFGirlsTags.NETHER_MOB))
            .setTranslation("tooltip.nffgirls.bauble.for_nether"));
    public static final NFURegistry.Accessor<BaubleEquippingCondition> CONDITION_ENDER =  BAUBLE_EQUIPPING_CONDITION_COLLECTION.register("ender", () ->
        BaubleEquippingCondition.of(args -> args.user().getType().is(NFFGirlsTags.ENDER_MOB))
        .setTranslation("tooltip.nffgirls.bauble.for_ender"));
    public static final NFURegistry.Accessor<BaubleEquippingCondition> CONDITION_NETHER_AND_ENDER =  BAUBLE_EQUIPPING_CONDITION_COLLECTION.register("nether_and_ender", () ->
        CONDITION_NETHER.get().or(CONDITION_ENDER.get()).setTranslation("tooltip.nffgirls.bauble.for_nether_and_ender"));

    public static final NFURegistry.Accessor<NFFGirlsPredicate<Entity>> EFFECT_CONDITION_IN_WATER = BAUBLE_EFFECT_CONDITION_COLLECTION.register("in_water", () ->
        NFFGirlsPredicate.simple(Entity.class, Entity::isInWaterOrBubble)
            .setName("in_water")
            .setTranslation("tooltip.nffgirls.bauble.in_water"));
    public static final NFURegistry.Accessor<NFFGirlsPredicate<Mob>> EFFECT_CONDITION_AT_NIGHT = BAUBLE_EFFECT_CONDITION_COLLECTION.register("at_night", () ->
        NFFGirlsPredicate.simple(Mob.class, (Mob m) -> m.level().isNight())
            .setName("at_night")
            .setTranslation("tooltip.nffgirls.bauble.at_night"));
    public static final NFURegistry.Accessor<NFFGirlsPredicate<Entity>> EFFECT_CONDITION_IN_WATER_OR_RAIN = BAUBLE_EFFECT_CONDITION_COLLECTION.register("in_water_or_rain", () ->
        NFFGirlsPredicate.simple(Entity.class, Entity::isInWaterRainOrBubble)
            .setName("in_water_or_rain")
            .setTranslation("tooltip.nffgirls.bauble.in_water_or_rain"));

    // =========== AMULETS ============ //

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> CRUDE_AMULET = BAUBLE_ITEMS.register("crude_amulet", () ->
        new NFFGirlsDedicatedBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "crude_amulet"), 1, 0, new Item.Properties(),
            () -> new NFFGirlsBaubleProperties()
                .addDefaultModifier(true, Attributes.MAX_HEALTH, 3d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.ARMOR, 1d, AttributeModifier.Operation.ADDITION)
                .descAllModifiers()));

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> REFINED_AMULET = BAUBLE_ITEMS.register("refined_amulet", () ->
        new NFFGirlsDedicatedBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "refined_amulet"), 1, 1, new Item.Properties(),
            () -> new NFFGirlsBaubleProperties()
                .addDefaultModifier(true, Attributes.MAX_HEALTH, 5d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.ARMOR, 2d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.ATTACK_DAMAGE, 1d, AttributeModifier.Operation.ADDITION)
                .descAllModifiers()));

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> SOUL_AMULET = BAUBLE_ITEMS.register("soul_amulet", () ->
        new NFFGirlsDedicatedBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "soul_amulet"), 1, 2, new Item.Properties(),
            () -> new NFFGirlsBaubleProperties()
                .setEquippingCondition(CONDITION_UNDEAD.get())
                .addDefaultModifier(true, Attributes.MAX_HEALTH, 10d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.ATTACK_DAMAGE, 3d, AttributeModifier.Operation.ADDITION)
                .environmentResistance()
                .descEquippingCondition()
                .descAllModifiers()));

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> SOUL_AMULET_II = BAUBLE_ITEMS.register("soul_amulet_ii", () ->
        new NFFGirlsDedicatedBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "soul_amulet"), 2, 3, new Item.Properties(),
            () -> new NFFGirlsBaubleProperties()
                .setEquippingCondition(CONDITION_UNDEAD.get())
                .addDefaultModifier(true, Attributes.MAX_HEALTH, 15d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.ATTACK_DAMAGE, 5d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.MOVEMENT_SPEED, 0.1d, AttributeModifier.Operation.MULTIPLY_BASE)
                .addDefaultModifier(true, NFFGirlsEntityAttributes.CRITICAL_RATE.get(), 0.05d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, NFFGirlsEntityAttributes.XP_GAIN_RATE.get(), 0.05d, AttributeModifier.Operation.ADDITION)
                .environmentResistance()
                .descEquippingCondition()
                .descAllModifiers()));

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> SOUL_AMULET_III = BAUBLE_ITEMS.register("soul_amulet_iii", () ->
        new NFFGirlsDedicatedBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "soul_amulet"), 3, 4, new Item.Properties(),
            () -> new NFFGirlsBaubleProperties()
                .setEquippingCondition(CONDITION_UNDEAD.get())
                .addDefaultModifier(true, Attributes.MAX_HEALTH, 25d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.ATTACK_DAMAGE, 8d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.MOVEMENT_SPEED, 0.15d, AttributeModifier.Operation.MULTIPLY_BASE)
                .addDefaultModifier(true, NFFGirlsEntityAttributes.CRITICAL_RATE.get(), 0.1d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, NFFGirlsEntityAttributes.XP_GAIN_RATE.get(), 0.1d, AttributeModifier.Operation.ADDITION)
                .environmentResistance()
                .descEquippingCondition()
                .descAllModifiers())
            .alwaysFoil().cast());

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> SOUL_AMULET_IV = BAUBLE_ITEMS.register("soul_amulet_iv", () ->
        new NFFGirlsDedicatedBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "soul_amulet"), 4, 6, new Item.Properties(),
            () -> new NFFGirlsBaubleProperties()
                .setEquippingCondition(CONDITION_UNDEAD.get())
                .addDefaultModifier(true, Attributes.MAX_HEALTH, 40d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.ATTACK_DAMAGE, 12d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.MOVEMENT_SPEED, 0.2d, AttributeModifier.Operation.MULTIPLY_BASE)
                .addDefaultModifier(true, NFFGirlsEntityAttributes.CRITICAL_RATE.get(), 0.15d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, NFFGirlsEntityAttributes.XP_GAIN_RATE.get(), 0.15d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, NFFGirlsEntityAttributes.PERSISTENT_HEALING.get(), 0.1d, AttributeModifier.Operation.ADDITION)
                .addModifier(false, NFFGirlsEntityAttributes.LOOTING_LEVEL.get(), 1d, AttributeModifier.Operation.ADDITION, new ResourceLocation(NFFGirls.MOD_ID, "soul_amulet_looting_level"), null, null)
                .environmentResistance()
                .descEquippingCondition()
                .descAllModifiers())
            .alwaysFoil().cast());

   // AMULETS

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> RESISTANCE_AMULET = BAUBLE_ITEMS.register("resistance_amulet", () ->
        new NFFGirlsDedicatedBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "resistance_amulet"), 1, 2, new Item.Properties(),
            () -> new NFFGirlsBaubleProperties()
                .addDefaultModifier(true, Attributes.ARMOR, 4.0d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.MAX_HEALTH, 15.0d, AttributeModifier.Operation.ADDITION)
                .environmentResistance()
                .descAllModifiers()));

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> RESISTANCE_AMULET_II = BAUBLE_ITEMS.register("resistance_amulet_ii", () ->
        new NFFGirlsDedicatedBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "resistance_amulet"), 2, 3, new Item.Properties(),
            () -> new NFFGirlsBaubleProperties()
                .addDefaultModifier(true, Attributes.ARMOR, 6.0d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.MAX_HEALTH, 25.0d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.KNOCKBACK_RESISTANCE, 0.1d, AttributeModifier.Operation.MULTIPLY_BASE)
                .environmentResistance()
                .descAllModifiers()));

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> RESISTANCE_AMULET_III = BAUBLE_ITEMS.register("resistance_amulet_iii", () ->
        new NFFGirlsDedicatedBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "resistance_amulet"), 3, 4, new Item.Properties(),
            () -> new NFFGirlsBaubleProperties()
                .addDefaultModifier(true, Attributes.ARMOR, 8.0d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.MAX_HEALTH, 40.0d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.KNOCKBACK_RESISTANCE, 0.3d, AttributeModifier.Operation.MULTIPLY_BASE)
                .environmentResistance()
                .descAllModifiers())
            .alwaysFoil().cast());

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> CORRUPTED_AMULET = BAUBLE_ITEMS.register("corrupted_amulet", () ->
        new NFFGirlsDedicatedBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "corrupted_amulet"), 1, 2, new Item.Properties(),
            () -> new NFFGirlsBaubleProperties()
                .addDefaultModifier(true, NFFGirlsEntityAttributes.WITHER_ASPECT.get(), 1.0d, AttributeModifier.Operation.ADDITION)
                .descAllModifiers()));

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> CORRUPTED_AMULET_II = BAUBLE_ITEMS.register("corrupted_amulet_ii", () ->
        new NFFGirlsDedicatedBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "corrupted_amulet"), 2, 2, new Item.Properties(),
            () -> new NFFGirlsBaubleProperties()
                .addDefaultModifier(true, NFFGirlsEntityAttributes.WITHER_ASPECT.get(), 1.0d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.ATTACK_DAMAGE, 2.0d, AttributeModifier.Operation.ADDITION)
                .descAllModifiers()));

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> WITHER_AMULET = BAUBLE_ITEMS.register("wither_amulet", () ->
        new NFFGirlsDedicatedBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "wither_amulet"), 1, 3, new Item.Properties(),
            () -> new NFFGirlsBaubleProperties()
                .setEquippingCondition(CONDITION_NETHER.get())
                .addDefaultModifier(true, NFFGirlsEntityAttributes.WITHER_ASPECT.get(), 2.0d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.MAX_HEALTH, 10.0d, AttributeModifier.Operation.ADDITION)
                .descEquippingCondition()
                .descAllModifiers()));

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> WITHER_AMULET_II = BAUBLE_ITEMS.register("wither_amulet_ii", () ->
        new NFFGirlsDedicatedBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "wither_amulet"), 2, 5, new Item.Properties(),
            () -> new NFFGirlsBaubleProperties()
                .setEquippingCondition(CONDITION_NETHER.get())
                .addDefaultModifier(true, NFFGirlsEntityAttributes.WITHER_ASPECT.get(), 2.5d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.MAX_HEALTH, 20.0d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.ATTACK_DAMAGE, 3.0d, AttributeModifier.Operation.ADDITION)
                .descEquippingCondition()
                .descAllModifiers()));

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> WITHER_AMULET_III = BAUBLE_ITEMS.register("wither_amulet_iii", () ->
        new NFFGirlsDedicatedBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "wither_amulet"), 3, 6, new Item.Properties(),
            () -> new NFFGirlsBaubleProperties()
                .setEquippingCondition(CONDITION_NETHER.get())
                .addDefaultModifier(true, NFFGirlsEntityAttributes.WITHER_ASPECT.get(), 4.0d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.MAX_HEALTH, 30d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.ATTACK_DAMAGE, 5.0d, AttributeModifier.Operation.ADDITION)
                .descEquippingCondition()
                .descAllModifiers())
            .alwaysFoil().cast());

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> ABYSS_AMULET = BAUBLE_ITEMS.register("abyss_amulet", () ->
        new NFFGirlsDedicatedBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "abyss_amulet"), 1, 5, new Item.Properties(),
            () -> new NFFGirlsBaubleProperties()
                .setEquippingCondition(CONDITION_NETHER_AND_ENDER.get())
                .addDefaultModifier(true, NFFGirlsEntityAttributes.WITHER_ASPECT.get(), 3.0d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, NFFGirlsEntityAttributes.POISON_ASPECT.get(), 3.0d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.ATTACK_DAMAGE, 5.0d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, NFFGirlsEntityAttributes.CRITICAL_RATE.get(), 0.25d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, NFFGirlsEntityAttributes.PERSISTENT_HEALING.get(), 0.1d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, NFFGirlsEntityAttributes.HEALTH_ABSORPTION.get(), 0.2d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.ARMOR, -0.2d, AttributeModifier.Operation.MULTIPLY_BASE)
                .addDefaultModifier(true, Attributes.MAX_HEALTH, -0.15D, AttributeModifier.Operation.MULTIPLY_TOTAL)
                .descEquippingCondition()
                .descAllModifiers()));

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> ABYSS_AMULET_II = BAUBLE_ITEMS.register("abyss_amulet_ii", () ->
        new NFFGirlsDedicatedBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "abyss_amulet"), 2, 7, new Item.Properties(),
            () -> new NFFGirlsBaubleProperties()
                .setEquippingCondition(CONDITION_NETHER_AND_ENDER.get())
                .addDefaultModifier(true, NFFGirlsEntityAttributes.WITHER_ASPECT.get(), 5.0d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, NFFGirlsEntityAttributes.POISON_ASPECT.get(), 5.0d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.ATTACK_DAMAGE, 8.0d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, NFFGirlsEntityAttributes.CRITICAL_RATE.get(), 0.4d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, NFFGirlsEntityAttributes.PERSISTENT_HEALING.get(), 0.15d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, NFFGirlsEntityAttributes.HEALTH_ABSORPTION.get(), 0.35d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.ARMOR, -0.25d, AttributeModifier.Operation.MULTIPLY_BASE)
                .addDefaultModifier(true, Attributes.KNOCKBACK_RESISTANCE, -0.1d, AttributeModifier.Operation.MULTIPLY_BASE)
                .addDefaultModifier(true, Attributes.MAX_HEALTH, -0.25d, AttributeModifier.Operation.MULTIPLY_TOTAL)
                .descEquippingCondition()
                .descAllModifiers())
            .alwaysFoil().cast());

    // =========== BADGE =========== //
    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> CRUDE_BADGE = BAUBLE_ITEMS.register("crude_badge", () ->
        new NFFGirlsDedicatedBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "crude_badge"), 1, 0, new Item.Properties(),
            () -> new NFFGirlsBaubleProperties()
                .addDefaultModifier(true, Attributes.MAX_HEALTH, 3.0d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.ATTACK_DAMAGE, 1.0d, AttributeModifier.Operation.ADDITION)
                .descAllModifiers()));

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> REFINED_BADGE = BAUBLE_ITEMS.register("refined_badge", () ->
        new NFFGirlsDedicatedBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "refined_badge"), 1, 1, new Item.Properties(),
            () -> new NFFGirlsBaubleProperties()
                .addDefaultModifier(true, Attributes.MAX_HEALTH, 5.0d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.ATTACK_DAMAGE, 2d, AttributeModifier.Operation.ADDITION)
                .descAllModifiers()));

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> COURAGE_BADGE = BAUBLE_ITEMS.register("courage_badge", () ->
        new NFFGirlsDedicatedBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "courage_badge"), 1, 2, new Item.Properties(),
            () -> new NFFGirlsBaubleProperties()
                .addDefaultModifier(true, Attributes.ATTACK_DAMAGE, 4.0d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.MOVEMENT_SPEED, 0.2d, AttributeModifier.Operation.MULTIPLY_BASE)
                .addDefaultModifier(true, NFFGirlsEntityAttributes.CRITICAL_RATE.get(), 0.1d, AttributeModifier.Operation.ADDITION)
                .addTag(INFFGirlsBauble.TAG_ACTIVE_ATTACK)
                .desc("tooltip.nffgirls.bauble.active_attack")
                .descAllModifiers()));

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> COURAGE_BADGE_II = BAUBLE_ITEMS.register("courage_badge_ii", () ->
        new NFFGirlsDedicatedBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "courage_badge"), 2, 3, new Item.Properties(),
            () -> new NFFGirlsBaubleProperties()
                .addDefaultModifier(true, Attributes.ATTACK_DAMAGE, 6.0d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.MOVEMENT_SPEED, 0.3d, AttributeModifier.Operation.MULTIPLY_BASE)
                .addDefaultModifier(true, NFFGirlsEntityAttributes.CRITICAL_RATE.get(), 0.15d, AttributeModifier.Operation.ADDITION)
                .addTag(INFFGirlsBauble.TAG_ACTIVE_ATTACK)
                .desc("tooltip.nffgirls.bauble.active_attack")
                .descAllModifiers()));

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> COURAGE_BADGE_III = BAUBLE_ITEMS.register("courage_badge_iii", () ->
        new NFFGirlsDedicatedBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "courage_badge"), 3, 5, new Item.Properties(),
            () -> new NFFGirlsBaubleProperties()
                .addDefaultModifier(true, Attributes.ATTACK_DAMAGE, 9d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.MOVEMENT_SPEED, 0.4d, AttributeModifier.Operation.MULTIPLY_BASE)
                .addDefaultModifier(true, NFFGirlsEntityAttributes.CRITICAL_RATE.get(), 0.25d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.ATTACK_KNOCKBACK, 0.5d, AttributeModifier.Operation.ADDITION)
                .addTag(INFFGirlsBauble.TAG_ACTIVE_ATTACK)
                .desc("tooltip.nffgirls.bauble.active_attack")
                .descAllModifiers())
            .alwaysFoil().cast());

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> TREASURE_HUNTER_BADGE = BAUBLE_ITEMS.register("treasure_hunter_badge", () ->
        new NFFGirlsDedicatedBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "treasure_hunter_badge"), 1, 3, new Item.Properties(),
            () -> new NFFGirlsBaubleProperties()
                .addDefaultModifier(true, ForgeMod.STEP_HEIGHT_ADDITION.get(), 0.5d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.MOVEMENT_SPEED, 0.3d, AttributeModifier.Operation.MULTIPLY_BASE)
                .addDefaultModifier(true, Attributes.LUCK, 1d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, NFFGirlsEntityAttributes.XP_GAIN_RATE.get(), 0.2d, AttributeModifier.Operation.ADDITION)
                .addTag(INFFGirlsBauble.TAG_ACTIVE_ATTACK)
                .descAllModifiers()
                .descTags()));

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> TREASURE_HUNTER_BADGE_II = BAUBLE_ITEMS.register("treasure_hunter_badge_ii", () ->
        new NFFGirlsDedicatedBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "treasure_hunter_badge"), 2, 5, new Item.Properties(),
            () -> new NFFGirlsBaubleProperties()
                .addDefaultModifier(true, ForgeMod.STEP_HEIGHT_ADDITION.get(), 0.5d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.MOVEMENT_SPEED, 0.4d, AttributeModifier.Operation.MULTIPLY_BASE)
                .addDefaultModifier(true, Attributes.LUCK, 2d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, NFFGirlsEntityAttributes.XP_GAIN_RATE.get(), 0.35d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, NFFGirlsEntityAttributes.LOOTING_LEVEL.get(), 1d, AttributeModifier.Operation.ADDITION)
                .addTag(INFFGirlsBauble.TAG_ACTIVE_ATTACK)
                .descAllModifiers()
                .descTags()));

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> TREASURE_HUNTER_BADGE_III = BAUBLE_ITEMS.register("treasure_hunter_badge_iii", () ->
        new NFFGirlsDedicatedBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "treasure_hunter_badge"), 3, 7, new Item.Properties(),
            () -> new NFFGirlsBaubleProperties()
                .addDefaultModifier(true, ForgeMod.STEP_HEIGHT_ADDITION.get(), 1d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.MOVEMENT_SPEED, 0.5d, AttributeModifier.Operation.MULTIPLY_BASE)
                .addDefaultModifier(true, Attributes.LUCK, 3d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, NFFGirlsEntityAttributes.XP_GAIN_RATE.get(), 0.5d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, NFFGirlsEntityAttributes.LOOTING_LEVEL.get(), 2d, AttributeModifier.Operation.ADDITION)
                .addTag(INFFGirlsBauble.TAG_ACTIVE_ATTACK)
                .descAllModifiers()
                .descTags())
            .alwaysFoil().cast());

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> NATURE_BADGE = BAUBLE_ITEMS.register("nature_badge", () ->
        new NFFGirlsDedicatedBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "nature_badge"), 1, 1, new Item.Properties(),
            () -> new NFFGirlsBaubleProperties()
                .setEquippingCondition(CONDITION_PLANT.get())
                .addDefaultModifier(true, NFFGirlsEntityAttributes.POISON_ASPECT.get(), 1d, AttributeModifier.Operation.ADDITION)
                .addModifier(true, NFFGirlsEntityAttributes.PERSISTENT_HEALING.get(), 0.1d, AttributeModifier.Operation.ADDITION,
                    null, "in_water", propertyConditionByOperator(EFFECT_CONDITION_IN_WATER_OR_RAIN.get()))
                .addModifier(false, Attributes.MOVEMENT_SPEED, 0.2d, AttributeModifier.Operation.ADDITION,
                    new ResourceLocation(NFFGirls.MOD_ID, "nature_derivatives_speed_boost"), "in_water", propertyConditionByOperator(EFFECT_CONDITION_IN_WATER_OR_RAIN.get()))
                .descEquippingCondition()
                .descModifiers(List.of(null), false)
                .desc(EFFECT_CONDITION_IN_WATER_OR_RAIN.get().getTranslation())
                .descModifiers(List.of("in_water"), false)));

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> NATURE_BADGE_II = BAUBLE_ITEMS.register("nature_badge_ii", () ->
        new NFFGirlsDedicatedBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "nature_badge"), 2, 2, new Item.Properties(),
            () -> new NFFGirlsBaubleProperties()
                .setEquippingCondition(CONDITION_PLANT.get())
                .addDefaultModifier(true, NFFGirlsEntityAttributes.POISON_ASPECT.get(), 1d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, NFFGirlsEntityAttributes.ANTI_ARTHROPOD.get(), 0.15d, AttributeModifier.Operation.ADDITION)
                .addModifier(true, NFFGirlsEntityAttributes.PERSISTENT_HEALING.get(), 0.15d, AttributeModifier.Operation.ADDITION,
                    null, "in_water",
                    propertyConditionByOperator(EFFECT_CONDITION_IN_WATER_OR_RAIN.get()))
                .addModifier(false, Attributes.MOVEMENT_SPEED, 0.2d, AttributeModifier.Operation.ADDITION,
                    new ResourceLocation(NFFGirls.MOD_ID, "nature_derivatives_speed_boost"), "in_water",
                    propertyConditionByOperator(EFFECT_CONDITION_IN_WATER_OR_RAIN.get()))
                .descEquippingCondition()
                .descModifiers(List.of(null), false)
                .desc(EFFECT_CONDITION_IN_WATER_OR_RAIN.get().getTranslation())
                .descModifiers(List.of("in_water"), false)));

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> NATURE_BADGE_III = BAUBLE_ITEMS.register("nature_badge_iii", () ->
        new NFFGirlsDedicatedBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "nature_badge"), 3, 3, new Item.Properties(),
            () -> new NFFGirlsBaubleProperties()
                .setEquippingCondition(CONDITION_PLANT.get())
                .addDefaultModifier(true, NFFGirlsEntityAttributes.POISON_ASPECT.get(), 2d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, NFFGirlsEntityAttributes.ANTI_ARTHROPOD.get(), 0.2d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.MAX_HEALTH, 10d, AttributeModifier.Operation.ADDITION)
                .addModifier(true, NFFGirlsEntityAttributes.PERSISTENT_HEALING.get(), 0.15d, AttributeModifier.Operation.ADDITION,
                    null, "in_water",
                    propertyConditionByOperator(EFFECT_CONDITION_IN_WATER_OR_RAIN.get()))
                .addModifier(false, Attributes.MOVEMENT_SPEED, 0.2d, AttributeModifier.Operation.ADDITION,
                    new ResourceLocation(NFFGirls.MOD_ID, "nature_derivatives_speed_boost"), "in_water",
                    propertyConditionByOperator(EFFECT_CONDITION_IN_WATER_OR_RAIN.get()))
                .descEquippingCondition()
                .descModifiers(List.of(null), false)
                .desc(EFFECT_CONDITION_IN_WATER_OR_RAIN.get().getTranslation())
                .descModifiers(List.of("in_water"), false))
            .alwaysFoil().cast());

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> NATURE_BADGE_IV = BAUBLE_ITEMS.register("nature_badge_iv", () ->
        new NFFGirlsDedicatedBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "nature_badge"), 4, 5, new Item.Properties(),
            () -> new NFFGirlsBaubleProperties()
                .setEquippingCondition(CONDITION_PLANT.get())
                .addDefaultModifier(true, NFFGirlsEntityAttributes.POISON_ASPECT.get(), 3d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, NFFGirlsEntityAttributes.ANTI_ARTHROPOD.get(), 0.3d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.MAX_HEALTH, 15d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, NFFGirlsEntityAttributes.XP_GAIN_RATE.get(), 0.2d, AttributeModifier.Operation.ADDITION)
                .addModifier(false, NFFGirlsEntityAttributes.PERSISTENT_HEALING.get(), 0.25d, AttributeModifier.Operation.ADDITION,
                    new ResourceLocation(NFFGirls.MOD_ID, "nature_derivatives_speed_boost"), "in_water",
                    propertyConditionByOperator(EFFECT_CONDITION_IN_WATER_OR_RAIN.get()))
                .addModifier(true, Attributes.MOVEMENT_SPEED, 0.3d, AttributeModifier.Operation.ADDITION,
                    null, "in_water",
                    propertyConditionByOperator(EFFECT_CONDITION_IN_WATER_OR_RAIN.get()))
                .descEquippingCondition()
                .descModifiers(List.of(null), false)
                .desc(EFFECT_CONDITION_IN_WATER_OR_RAIN.get().getTranslation())
                .descModifiers(List.of("in_water"), false))
            .alwaysFoil().cast());

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> NATURE_BADGE_V = BAUBLE_ITEMS.register("nature_badge_v", () ->
        new NFFGirlsDedicatedBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "nature_badge"), 5, 7, new Item.Properties(),
            () -> new NFFGirlsBaubleProperties()
                .setEquippingCondition(CONDITION_PLANT.get())
                .addDefaultModifier(true, NFFGirlsEntityAttributes.POISON_ASPECT.get(), 3d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, NFFGirlsEntityAttributes.ANTI_ARTHROPOD.get(), 0.5d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.MAX_HEALTH, 25d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, NFFGirlsEntityAttributes.XP_GAIN_RATE.get(), 0.35d, AttributeModifier.Operation.ADDITION)
                .addModifier(true, NFFGirlsEntityAttributes.PERSISTENT_HEALING.get(), 0.4d, AttributeModifier.Operation.ADDITION,
                    null, "in_water",
                    propertyConditionByOperator(EFFECT_CONDITION_IN_WATER_OR_RAIN.get()))
                .addModifier(false, Attributes.MOVEMENT_SPEED, 0.35d, AttributeModifier.Operation.ADDITION,
                    new ResourceLocation(NFFGirls.MOD_ID, "nature_derivatives_speed_boost"), "in_water",
                    propertyConditionByOperator(EFFECT_CONDITION_IN_WATER_OR_RAIN.get()))
                .addModifier(false, NFFGirlsEntityAttributes.LOOTING_LEVEL.get(), 0.1d, AttributeModifier.Operation.ADDITION,
                    new ResourceLocation(NFFGirls.MOD_ID, "nature_badge_looting_level"), "in_water",
                    propertyConditionByOperator(EFFECT_CONDITION_IN_WATER_OR_RAIN.get()))
                .descEquippingCondition()
                .descModifiers(List.of(null), false)
                .desc(EFFECT_CONDITION_IN_WATER_OR_RAIN.get().getTranslation())
                .descModifiers(List.of("in_water"), false))
            .alwaysFoil().cast());

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> NATURES_TENDERNESS_BADGE = BAUBLE_ITEMS.register("natures_tenderness_badge", () ->
        new NFFGirlsDedicatedBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "natures_tenderness_badge"), 1, 4, new Item.Properties(),
            () -> new NFFGirlsBaubleProperties()
                .setEquippingCondition(CONDITION_PLANT.get())
                .addDefaultModifier(true, Attributes.MAX_HEALTH, 20d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.ARMOR, 6d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, NFFGirlsEntityAttributes.XP_GAIN_RATE.get(), 0.3d, AttributeModifier.Operation.ADDITION)
                .addModifier(true, NFFGirlsEntityAttributes.PERSISTENT_HEALING.get(), 0.4d, AttributeModifier.Operation.ADDITION,
                    null, "in_water",
                    propertyConditionByOperator(EFFECT_CONDITION_IN_WATER_OR_RAIN.get()))
                .addModifier(false, Attributes.MOVEMENT_SPEED, 0.3d, AttributeModifier.Operation.ADDITION,
                    null, "in_water",
                    propertyConditionByOperator(EFFECT_CONDITION_IN_WATER_OR_RAIN.get()))
                .descEquippingCondition()
                .descModifiers(List.of(null), false)
                .desc(EFFECT_CONDITION_IN_WATER_OR_RAIN.get().getTranslation())
                .descModifiers(List.of("in_water"), false)));

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> NATURES_TENDERNESS_BADGE_II = BAUBLE_ITEMS.register("natures_tenderness_badge_ii", () ->
        new NFFGirlsDedicatedBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "natures_tenderness_badge"), 2, 6, new Item.Properties(),
            () -> new NFFGirlsBaubleProperties()
                .setEquippingCondition(CONDITION_PLANT.get())
                .addDefaultModifier(true, Attributes.MAX_HEALTH, 30d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.ARMOR, 8d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, NFFGirlsEntityAttributes.XP_GAIN_RATE.get(), 0.4d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, NFFGirlsEntityAttributes.PERSISTENT_RANGED_HEALING.get(), 0.05d, AttributeModifier.Operation.ADDITION)
                .addModifier(true, NFFGirlsEntityAttributes.PERSISTENT_HEALING.get(), 0.6d, AttributeModifier.Operation.ADDITION,
                    null, "in_water",
                    propertyConditionByOperator(EFFECT_CONDITION_IN_WATER_OR_RAIN.get()))
                .addModifier(false, Attributes.MOVEMENT_SPEED, 0.3d, AttributeModifier.Operation.ADDITION,
                    new ResourceLocation(NFFGirls.MOD_ID, "nature_derivatives_speed_boost"), "in_water",
                    propertyConditionByOperator(EFFECT_CONDITION_IN_WATER_OR_RAIN.get()))
                .descEquippingCondition()
                .descModifiers(List.of(null), false)
                .desc(EFFECT_CONDITION_IN_WATER_OR_RAIN.get().getTranslation())
                .descModifiers(List.of("in_water"), false))
            .alwaysFoil().cast());

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> NATURES_TENDERNESS_BADGE_III = BAUBLE_ITEMS.register("natures_tenderness_badge_iii", () ->
        new NFFGirlsDedicatedBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "natures_tenderness_badge"), 3, 8, new Item.Properties(),
            () -> new NFFGirlsBaubleProperties()
                .setEquippingCondition(CONDITION_PLANT.get())
                .addDefaultModifier(true, Attributes.MAX_HEALTH, 30d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.ARMOR, 8d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, NFFGirlsEntityAttributes.XP_GAIN_RATE.get(), 0.4d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, NFFGirlsEntityAttributes.PERSISTENT_RANGED_HEALING.get(), 0.1d, AttributeModifier.Operation.ADDITION)
                .addModifier(true, NFFGirlsEntityAttributes.PERSISTENT_HEALING.get(), 0.6d, AttributeModifier.Operation.ADDITION,
                    null, "in_water",
                    propertyConditionByOperator(EFFECT_CONDITION_IN_WATER_OR_RAIN.get()))
                .addModifier(false, Attributes.MOVEMENT_SPEED, 0.3d, AttributeModifier.Operation.ADDITION,
                    new ResourceLocation(NFFGirls.MOD_ID, "nature_derivatives_speed_boost"), "in_water",
                    propertyConditionByOperator(EFFECT_CONDITION_IN_WATER_OR_RAIN.get()))
                .addModifier(false, NFFGirlsEntityAttributes.LOOTING_LEVEL.get(), 1.0d, AttributeModifier.Operation.ADDITION,
                    new ResourceLocation(NFFGirls.MOD_ID, "natures_tenderness_looting_level"), null, null)
                .descEquippingCondition()
                .descModifiers(List.of(null), false)
                .desc(EFFECT_CONDITION_IN_WATER_OR_RAIN.get().getTranslation())
                .descModifiers(List.of("in_water"), false))
            .alwaysFoil().cast());

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> NATURES_RAGE_BADGE = BAUBLE_ITEMS.register("natures_rage_badge", () ->
        new NFFGirlsDedicatedBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "natures_rage_badge"), 1, 4, new Item.Properties(),
            () -> new NFFGirlsBaubleProperties()
                .setEquippingCondition(CONDITION_PLANT.get())
                .addDefaultModifier(true, Attributes.ATTACK_DAMAGE, 4d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.ATTACK_KNOCKBACK, 0.5d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, NFFGirlsEntityAttributes.POISON_ASPECT.get(), 3d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, NFFGirlsEntityAttributes.ANTI_ARTHROPOD.get(), 0.5d, AttributeModifier.Operation.ADDITION)
                .addModifier(false, Attributes.MOVEMENT_SPEED, 0.35d, AttributeModifier.Operation.ADDITION,
                    new ResourceLocation(NFFGirls.MOD_ID, "nature_derivatives_speed_boost"), "in_water",
                    propertyConditionByOperator(EFFECT_CONDITION_IN_WATER_OR_RAIN.get()))
                .descEquippingCondition()
                .descModifiers(List.of(null), false)
                .desc(EFFECT_CONDITION_IN_WATER_OR_RAIN.get().getTranslation())
                .descModifiers(List.of("in_water"), false)));

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> NATURES_RAGE_BADGE_II = BAUBLE_ITEMS.register("natures_rage_badge_ii", () ->
        new NFFGirlsDedicatedBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "natures_rage_badge"), 2, 6, new Item.Properties(),
            () -> new NFFGirlsBaubleProperties()
                .setEquippingCondition(CONDITION_PLANT.get())
                .addDefaultModifier(true, Attributes.ATTACK_DAMAGE, 6d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.ATTACK_KNOCKBACK, 0.7d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, NFFGirlsEntityAttributes.POISON_ASPECT.get(), 4d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, NFFGirlsEntityAttributes.ANTI_ARTHROPOD.get(), 0.75d, AttributeModifier.Operation.ADDITION)
                .addModifier(false, Attributes.MOVEMENT_SPEED, 0.35d, AttributeModifier.Operation.ADDITION,
                    new ResourceLocation(NFFGirls.MOD_ID, "nature_derivatives_speed_boost"), "in_water",
                    propertyConditionByOperator(EFFECT_CONDITION_IN_WATER_OR_RAIN.get()))
                .descEquippingCondition()
                .descModifiers(List.of(null), false)
                .desc(EFFECT_CONDITION_IN_WATER_OR_RAIN.get().getTranslation())
                .descModifiers(List.of("in_water"), false))
            .alwaysFoil().cast());

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> NATURES_RAGE_BADGE_III = BAUBLE_ITEMS.register("natures_rage_badge_iii", () ->
        new NFFGirlsDedicatedBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "natures_rage_badge"), 3, 8, new Item.Properties(),
            () -> new NFFGirlsBaubleProperties()
                .setEquippingCondition(CONDITION_PLANT.get())
                .addDefaultModifier(true, Attributes.ATTACK_DAMAGE, 9d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.ATTACK_KNOCKBACK, 1.0d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, NFFGirlsEntityAttributes.POISON_ASPECT.get(), 5d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, NFFGirlsEntityAttributes.ANTI_ARTHROPOD.get(), 1.0d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, NFFGirlsEntityAttributes.HEALTH_ABSORPTION.get(), 0.25d, AttributeModifier.Operation.ADDITION)
                .addModifier(false, Attributes.MOVEMENT_SPEED, 0.4d, AttributeModifier.Operation.ADDITION,
                    new ResourceLocation(NFFGirls.MOD_ID, "nature_derivatives_speed_boost"), "in_water",
                    propertyConditionByOperator(EFFECT_CONDITION_IN_WATER_OR_RAIN.get()))
                .addModifier(false, NFFGirlsEntityAttributes.LOOTING_LEVEL.get(), 1.0d, AttributeModifier.Operation.ADDITION,
                    new ResourceLocation(NFFGirls.MOD_ID, "natures_rage_looting_level"), null, null)
                .descEquippingCondition()
                .descModifiers(List.of(null), false)
                .desc(EFFECT_CONDITION_IN_WATER_OR_RAIN.get().getTranslation())
                .descModifiers(List.of("in_water"), false))
            .alwaysFoil().cast());

    // =========== JADE ============ //

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> RAW_JADE = BAUBLE_ITEMS.register("raw_jade", () ->
        new NFFGirlsDedicatedBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "raw_jade"), 1, 0, new Item.Properties(),
            () -> new NFFGirlsBaubleProperties()
                .addDefaultModifier(true, Attributes.MAX_HEALTH, 3d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.MOVEMENT_SPEED, 0.1d, AttributeModifier.Operation.MULTIPLY_BASE)
                .descAllModifiers()));

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> BURNISHED_JADE = BAUBLE_ITEMS.register("burnished_jade", () ->
        new NFFGirlsDedicatedBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "burnished_jade"), 1, 1, new Item.Properties(),
            () -> new NFFGirlsBaubleProperties()
                .addDefaultModifier(true, Attributes.MAX_HEALTH, 5d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.ARMOR, 1d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.MOVEMENT_SPEED, 0.1d, AttributeModifier.Operation.MULTIPLY_BASE)
                .descAllModifiers()));

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> HOLY_JADE = BAUBLE_ITEMS.register("holy_jade", () ->
        new NFFGirlsDedicatedBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "holy_jade"), 1, 2, new Item.Properties(),
            () -> new NFFGirlsBaubleProperties()
                .setEquippingCondition(CONDITION_NOT_UNDEAD.get())
                .addDefaultModifier(true, NFFGirlsEntityAttributes.ANTI_UNDEAD.get(), 0.2d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.ARMOR, 3d, AttributeModifier.Operation.ADDITION)
                .descEquippingCondition()
                .descAllModifiers()));

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> HOLY_JADE_II = BAUBLE_ITEMS.register("holy_jade_ii", () ->
        new NFFGirlsDedicatedBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "holy_jade"), 2, 3, new Item.Properties(),
            () -> new NFFGirlsBaubleProperties()
                .setEquippingCondition(CONDITION_NOT_UNDEAD.get())
                .addDefaultModifier(true, NFFGirlsEntityAttributes.ANTI_UNDEAD.get(), 0.35d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.ARMOR, 6d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.MAX_HEALTH, 10d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.KNOCKBACK_RESISTANCE, 0.15d, AttributeModifier.Operation.MULTIPLY_BASE)
                .addDefaultModifier(true, NFFGirlsEntityAttributes.PERSISTENT_HEALING.get(), 0.1d, AttributeModifier.Operation.ADDITION)
                .descEquippingCondition()
                .descAllModifiers()));

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> HOLY_JADE_III = BAUBLE_ITEMS.register("holy_jade_iii", () ->
        new NFFGirlsDedicatedBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "holy_jade"), 3, 5, new Item.Properties(),
            () -> new NFFGirlsBaubleProperties()
                .setEquippingCondition(CONDITION_NOT_UNDEAD.get())
                .addDefaultModifier(true, NFFGirlsEntityAttributes.ANTI_UNDEAD.get(), 0.6d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.ARMOR, 8d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.ARMOR_TOUGHNESS, 0.2d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.MAX_HEALTH, 20d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.KNOCKBACK_RESISTANCE, 0.25d, AttributeModifier.Operation.MULTIPLY_BASE)
                .addDefaultModifier(true, NFFGirlsEntityAttributes.PERSISTENT_HEALING.get(), 0.2d, AttributeModifier.Operation.ADDITION)
                .descEquippingCondition()
                .descAllModifiers())
            .alwaysFoil().cast());

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> HEALING_JADE = BAUBLE_ITEMS.register("healing_jade", () ->
        new NFFGirlsDedicatedBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "healing_jade"), 1, 1, new Item.Properties(),
            () -> new NFFGirlsBaubleProperties()
                .addDefaultModifier(true, NFFGirlsEntityAttributes.PERSISTENT_HEALING.get(), 0.1d, AttributeModifier.Operation.ADDITION)
                .descAllModifiers()));

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> HEALING_JADE_II = BAUBLE_ITEMS.register("healing_jade_ii", () ->
        new NFFGirlsDedicatedBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "healing_jade"), 2, 2, new Item.Properties(),
            () -> new NFFGirlsBaubleProperties()
                .addDefaultModifier(true, NFFGirlsEntityAttributes.PERSISTENT_HEALING.get(), 0.2d, AttributeModifier.Operation.ADDITION)
                .descAllModifiers()));

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> HEALING_JADE_III = BAUBLE_ITEMS.register("healing_jade_iii", () ->
        new NFFGirlsDedicatedBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "healing_jade"), 3, 4, new Item.Properties(),
            () -> new NFFGirlsBaubleProperties()
                .addDefaultModifier(true, NFFGirlsEntityAttributes.PERSISTENT_HEALING.get(), 0.25d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, NFFGirlsEntityAttributes.HEALTH_ABSORPTION.get(), 0.1d, AttributeModifier.Operation.ADDITION)
                .descAllModifiers())
            .alwaysFoil().cast());

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> LIFE_JADE = BAUBLE_ITEMS.register("life_jade", () ->
        new NFFGirlsDedicatedBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "life_jade"), 1, 2, new Item.Properties(),
            () -> new NFFGirlsBaubleProperties()
                .addDefaultModifier(true, NFFGirlsEntityAttributes.PERSISTENT_HEALING.get(), 0.15d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.MAX_HEALTH, 10d, AttributeModifier.Operation.ADDITION)
                .environmentResistance()
                .descAllModifiers()));

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> LIFE_JADE_II = BAUBLE_ITEMS.register("life_jade_ii", () ->
        new NFFGirlsDedicatedBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "life_jade"), 2, 3, new Item.Properties(),
            () -> new NFFGirlsBaubleProperties()
                .addDefaultModifier(true, NFFGirlsEntityAttributes.PERSISTENT_HEALING.get(), 0.2d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.MAX_HEALTH, 20d, AttributeModifier.Operation.ADDITION)
                .environmentResistance()
                .descAllModifiers()));

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> LIFE_JADE_III = BAUBLE_ITEMS.register("life_jade_iii", () ->
        new NFFGirlsDedicatedBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "life_jade"), 3, 5, new Item.Properties(),
            () -> new NFFGirlsBaubleProperties()
                .addDefaultModifier(true, NFFGirlsEntityAttributes.PERSISTENT_HEALING.get(), 0.25d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.MAX_HEALTH, 25d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.ARMOR, 3d, AttributeModifier.Operation.ADDITION)
                .environmentResistance()
                .descAllModifiers())
            .alwaysFoil().cast());

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> AQUA_JADE = BAUBLE_ITEMS.register("aqua_jade", () ->
        new NFFGirlsDedicatedBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "aqua_jade"), 1, 2, new Item.Properties(),
            () -> new NFFGirlsBaubleProperties()
                .setEquippingCondition(CONDITION_AQUATIC.get())
                .addModifier(false, Attributes.MOVEMENT_SPEED, 0.7d, AttributeModifier.Operation.ADDITION,
                    new ResourceLocation(NFFGirls.MOD_ID, "aqua_jade_swim_speed_boost"), "in_water",
                    propertyConditionByOperator(EFFECT_CONDITION_IN_WATER.get()))
                .addModifier(true, NFFGirlsEntityAttributes.PERSISTENT_HEALING.get(), 0.3d, AttributeModifier.Operation.ADDITION,
                    null, "in_water",
                    propertyConditionByOperator(EFFECT_CONDITION_IN_WATER.get()))
                .addDefaultModifier(true, NFFGirlsEntityAttributes.WATER_ASPECT.get(), 0.25d, AttributeModifier.Operation.ADDITION)
                .environmentResistance()
                .descEquippingCondition()
                .descModifiers(List.of(null), false)
                .descEnvironmentResistanceIfPresent()
                .desc("tooltip.nffgirls.bauble.in_water")
                .descModifiers(List.of("in_water"), false)));

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> AQUA_JADE_II = BAUBLE_ITEMS.register("aqua_jade_ii", () ->
        new NFFGirlsDedicatedBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "aqua_jade"), 2, 3, new Item.Properties(),
            () -> new NFFGirlsBaubleProperties()
                .setEquippingCondition(CONDITION_AQUATIC.get())
                .addModifier(false, Attributes.MOVEMENT_SPEED, 0.8d, AttributeModifier.Operation.ADDITION,
                    new ResourceLocation(NFFGirls.MOD_ID, "aqua_jade_swim_speed_boost"), "in_water",
                    propertyConditionByOperator(EFFECT_CONDITION_IN_WATER.get()))
                .addModifier(true, NFFGirlsEntityAttributes.PERSISTENT_HEALING.get(), 0.35d, AttributeModifier.Operation.ADDITION,
                    null, "in_water",
                    propertyConditionByOperator(EFFECT_CONDITION_IN_WATER.get()))
                .addDefaultModifier(true, NFFGirlsEntityAttributes.WATER_ASPECT.get(), 0.35d, AttributeModifier.Operation.ADDITION)
                .addModifier(true, Attributes.ATTACK_DAMAGE, 3d, AttributeModifier.Operation.ADDITION,
                    null, "in_water",
                    propertyConditionByOperator(EFFECT_CONDITION_IN_WATER.get()))
                .addModifier(true, Attributes.ARMOR, 3d, AttributeModifier.Operation.ADDITION,
                    null, "in_water",
                    propertyConditionByOperator(EFFECT_CONDITION_IN_WATER.get()))
                .environmentResistance()
                .descEquippingCondition()
                .descModifiers(List.of(null), false)
                .descEnvironmentResistanceIfPresent()
                .desc("tooltip.nffgirls.bauble.in_water")
                .descModifiers(List.of("in_water"), false)));

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> AQUA_JADE_III = BAUBLE_ITEMS.register("aqua_jade_iii", () ->
        new NFFGirlsDedicatedBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "aqua_jade"), 3, 5, new Item.Properties(),
            () -> new NFFGirlsBaubleProperties()
                .setEquippingCondition(CONDITION_AQUATIC.get())
                .addModifier(false, Attributes.MOVEMENT_SPEED, 1.0d, AttributeModifier.Operation.ADDITION,
                    new ResourceLocation(NFFGirls.MOD_ID, "aqua_jade_swim_speed_boost"), "in_water",
                    propertyConditionByOperator(EFFECT_CONDITION_IN_WATER.get()))
                .addModifier(true, NFFGirlsEntityAttributes.PERSISTENT_HEALING.get(), 0.4d, AttributeModifier.Operation.ADDITION,
                    null, "in_water",
                    propertyConditionByOperator(EFFECT_CONDITION_IN_WATER.get()))
                .addDefaultModifier(true, NFFGirlsEntityAttributes.WATER_ASPECT.get(), 0.5d, AttributeModifier.Operation.ADDITION)
                .addModifier(true, Attributes.ATTACK_DAMAGE, 5d, AttributeModifier.Operation.ADDITION,
                    null, "in_water",
                    propertyConditionByOperator(EFFECT_CONDITION_IN_WATER.get()))
                .addModifier(true, Attributes.ARMOR, 5d, AttributeModifier.Operation.ADDITION,
                    null, "in_water",
                    propertyConditionByOperator(EFFECT_CONDITION_IN_WATER.get()))
                .environmentResistance()
                .descEquippingCondition()
                .descModifiers(List.of(null), false)
                .descEnvironmentResistanceIfPresent()
                .desc("tooltip.nffgirls.bauble.in_water")
                .descModifiers(List.of("in_water"), false))
            .alwaysFoil().cast());

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> POISON_JADE = BAUBLE_ITEMS.register("poison_jade", () ->
        new NFFGirlsDedicatedBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "poison_jade"), 1, 1, new Item.Properties(),
            () -> new NFFGirlsBaubleProperties()
                .setEquippingCondition(CONDITION_ARTHROPOD_AND_PLANT.get())
                .addDefaultModifier(true, NFFGirlsEntityAttributes.POISON_ASPECT.get(), 1.0D, AttributeModifier.Operation.ADDITION)
                .descEquippingCondition()
                .descAllModifiers()));

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> POISON_JADE_II = BAUBLE_ITEMS.register("poison_jade_ii", () ->
        new NFFGirlsDedicatedBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "poison_jade"), 2, 2, new Item.Properties(),
            () -> new NFFGirlsBaubleProperties()
                .setEquippingCondition(CONDITION_ARTHROPOD_AND_PLANT.get())
                .addDefaultModifier(true, NFFGirlsEntityAttributes.POISON_ASPECT.get(), 1.5D, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.MOVEMENT_SPEED, 0.1d, AttributeModifier.Operation.MULTIPLY_BASE)
                .descEquippingCondition()
                .descAllModifiers()));

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> POISON_JADE_III = BAUBLE_ITEMS.register("poison_jade_iii", () ->
        new NFFGirlsDedicatedBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "poison_jade"), 3, 3, new Item.Properties(),
            () -> new NFFGirlsBaubleProperties()
                .setEquippingCondition(CONDITION_ARTHROPOD_AND_PLANT.get())
                .addDefaultModifier(true, NFFGirlsEntityAttributes.POISON_ASPECT.get(), 2.5D, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.MOVEMENT_SPEED, 0.1d, AttributeModifier.Operation.MULTIPLY_BASE)
                .addDefaultModifier(true, NFFGirlsEntityAttributes.HEALTH_ABSORPTION.get(), 0.1d, AttributeModifier.Operation.ADDITION)
                .descEquippingCondition()
                .descAllModifiers())
            .alwaysFoil().cast());

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> POISON_JADE_IV = BAUBLE_ITEMS.register("poison_jade_iv", () ->
        new NFFGirlsDedicatedBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "poison_jade"), 4, 5, new Item.Properties(),
            () -> new NFFGirlsBaubleProperties()
                .setEquippingCondition(CONDITION_ARTHROPOD_AND_PLANT.get())
                .addDefaultModifier(true, NFFGirlsEntityAttributes.POISON_ASPECT.get(), 3.5D, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.MOVEMENT_SPEED, 0.15d, AttributeModifier.Operation.MULTIPLY_BASE)
                .addDefaultModifier(true, Attributes.ATTACK_DAMAGE, 4d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, NFFGirlsEntityAttributes.HEALTH_ABSORPTION.get(), 0.15d, AttributeModifier.Operation.ADDITION)
                .descEquippingCondition()
                .descAllModifiers())
            .alwaysFoil().cast());

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> EVIL_JADE = BAUBLE_ITEMS.register("evil_jade", () ->
        new NFFGirlsDedicatedBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "evil_jade"), 1, 3, new Item.Properties(),
            () -> new NFFGirlsBaubleProperties()
                .addDefaultModifier(true, NFFGirlsEntityAttributes.POISON_ASPECT.get(), 1d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, NFFGirlsEntityAttributes.HEALTH_ABSORPTION.get(), 0.1d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, NFFGirlsEntityAttributes.CRITICAL_RATE.get(), 0.1d, AttributeModifier.Operation.ADDITION)
                .descAllModifiers()));

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> EVIL_JADE_II = BAUBLE_ITEMS.register("evil_jade_ii", () ->
        new NFFGirlsDedicatedBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "evil_jade"), 2, 4, new Item.Properties(),
            () -> new NFFGirlsBaubleProperties()
                .addDefaultModifier(true, NFFGirlsEntityAttributes.POISON_ASPECT.get(), 1.5d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.ATTACK_DAMAGE, 3d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, NFFGirlsEntityAttributes.HEALTH_ABSORPTION.get(), 0.15d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, NFFGirlsEntityAttributes.CRITICAL_RATE.get(), 0.15d, AttributeModifier.Operation.ADDITION)
                .descAllModifiers()));

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> EVIL_JADE_III = BAUBLE_ITEMS.register("evil_jade_iii", () ->
        new NFFGirlsDedicatedBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "evil_jade"), 3, 6, new Item.Properties(),
            () -> new NFFGirlsBaubleProperties()
                .addDefaultModifier(true, NFFGirlsEntityAttributes.POISON_ASPECT.get(), 2.5d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.ATTACK_DAMAGE, 6d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, NFFGirlsEntityAttributes.HEALTH_ABSORPTION.get(), 0.2d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, NFFGirlsEntityAttributes.CRITICAL_RATE.get(), 0.3d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, NFFGirlsEntityAttributes.PERSISTENT_HEALING.get(), 0.15d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, NFFGirlsEntityAttributes.XP_GAIN_RATE.get(), 0.15d, AttributeModifier.Operation.ADDITION)
                .descAllModifiers())
            .alwaysFoil().cast());

    /*public static final RegistryObject<NFFGirlsDedicatedBaubleItem> DECAY_JADE = BAUBLE_ITEMS.register("decay_jade", () ->
        new NFFGirlsDedicatedBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "decay_jade"), 1, new Item.Properties(),
        () -> new NFFGirlsBaubleProperties()
            .addDefaultModifier(true, NFFGirlsEntityAttributes.WITHER_ASPECT.get(), 2d, AttributeModifier.Operation.ADDITION)
            .addDefaultModifier(true, NFFGirlsEntityAttributes.POISON_ASPECT.get(), 2d, AttributeModifier.Operation.ADDITION)
            .addDefaultModifier(true, Attributes.ARMOR, 6d, AttributeModifier.Operation.ADDITION)
            .addDefaultModifier(true, Attributes.ARMOR_TOUGHNESS, 0.3d, AttributeModifier.Operation.ADDITION)
            .addDefaultModifier(true, Attributes.MOVEMENT_SPEED, -0.1d, AttributeModifier.Operation.MULTIPLY_BASE)
            .setRarityTier(3)
            .descAllModifiers()));
*//*
    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> DECAY_JADE_II = BAUBLE_ITEMS.register("decay_jade_ii", () ->
        new NFFGirlsDedicatedBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "decay_jade"), 2, new Item.Properties(),
            () -> new NFFGirlsBaubleProperties().addDefaultModifier(true, NFFGirlsEntityAttributes.WITHER_ASPECT.get(), 4d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, NFFGirlsEntityAttributes.POISON_ASPECT.get(), 3.5d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.ARMOR, 8d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.ARMOR_TOUGHNESS, 0.5d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.MOVEMENT_SPEED, -0.1d, AttributeModifier.Operation.MULTIPLY_BASE)
                .setRarityTier(4)
                .descAllModifiers()));
*//*
    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> DECAY_JADE_III = BAUBLE_ITEMS.register("decay_jade_iii", () ->
        new NFFGirlsDedicatedBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "decay_jade"), 3, new Item.Properties(),
            () -> new NFFGirlsBaubleProperties().addDefaultModifier(true, NFFGirlsEntityAttributes.WITHER_ASPECT.get(), 6d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, NFFGirlsEntityAttributes.POISON_ASPECT.get(), 5d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.ARMOR, 10d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.ARMOR_TOUGHNESS, 0.7d, AttributeModifier.Operation.ADDITION)
                .addDefaultModifier(true, Attributes.MOVEMENT_SPEED, -0.1d, AttributeModifier.Operation.MULTIPLY_BASE)
                .setRarityTier(6)
                .descAllModifiers()).alwaysFoil().cast());
*/
    // Misc
    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> RESISTANCE_CORE = BAUBLE_ITEMS.register("resistance_core", () ->
        new NFFGirlsDedicatedBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "resistance_core"), 1, 0, new Item.Properties(),
            () -> new NFFGirlsBaubleProperties()
                .environmentResistance()));

    public static final NFURegistry.Accessor<NFFGirlsBaubleBehavior> INSOMNIA_FRUIT =
        BAUBLE_COLLECTION.register("insomnia_fruit", () -> new NFFGirlsBaubleBehavior(ModItems.INSOMNIA_FRUIT.get(),
            new ResourceLocation(NFFGirls.MOD_ID, "insomnia_fruit"), 1, 1, 
            new NFFGirlsBaubleProperties()
                .addDefaultModifier(true, Attributes.ATTACK_DAMAGE, 5d, AttributeModifier.Operation.ADDITION, EFFECT_CONDITION_AT_NIGHT.get())
                .addDefaultModifier(true, Attributes.MAX_HEALTH, 50d, AttributeModifier.Operation.ADDITION, EFFECT_CONDITION_AT_NIGHT.get())
                .addDefaultModifier(true, NFFGirlsEntityAttributes.PERSISTENT_HEALING.get(), 0.1d, AttributeModifier.Operation.ADDITION, EFFECT_CONDITION_AT_NIGHT.get())
                .desc("tooltip.nffgirls.bauble.at_night")
                .descAllModifiers()));
    
    // Ingredients

    public static final RegistryObject<Item> JADE_MATERIAL = BAUBLE_ITEMS.register("jade_material", () -> new Item(new Item.Properties()));

    // Register slots

    public static Function<Mob, ItemStack> accessMobAdditionalInventory(int position)
    {
        return mob -> {
            INFFTamed tamed = INFFTamed.get(mob).orElseThrow(() -> new IllegalArgumentException("Input mob isn't INFFTamed."));
            return tamed.getAdditionalInventory().map(in -> in.getItem(position)).orElse(ItemStack.EMPTY);
        };
    }

    /**
     * Register a befriended mob with a series of continuous bauble slots in the additional inventory.
     * for example, using index range (3, 6) will register the additional inventory slot 3, 4, 5 with slot name "0", "1", "2" respectively.
     */
    public static RegisterBaubleEquippableMobsEvent.SlotRegisterer registerWithContinuousSlotSequence(
        RegisterBaubleEquippableMobsEvent event, Class<? extends Mob> clazz, int minIndex, int maxIndexExclude)
    {
        RegisterBaubleEquippableMobsEvent.SlotRegisterer reg = event.register(clazz);
        for (int i = 0; i < maxIndexExclude - minIndex; ++i)
        {
            reg.addSlot(Integer.toString(i), accessMobAdditionalInventory(minIndex + i));
        }
        return reg;
    }

    public static NFFGirlsBaubleProperties.PropertyApplyingCondition propertyConditionByOperator(NFUOperator<? super Mob, Boolean> operator) {
        return NFFGirlsBaubleProperties.PropertyApplyingCondition.byLivingOperator(operator);
    }

    @SubscribeEvent
    public static void baubleEquippableRegistration(RegisterBaubleEquippableMobsEvent event)
    {
        registerWithContinuousSlotSequence(event, HmagDrownedGirlEntity.class, 6, 8);
        registerWithContinuousSlotSequence(event, HmagZombieGirlEntity.class, 6, 8);
        registerWithContinuousSlotSequence(event, HmagHuskGirlEntity.class, 6, 8);

        registerWithContinuousSlotSequence(event, HmagSkeletonGirlEntity.class, 6, 7);
        registerWithContinuousSlotSequence(event, HmagStrayGirlEntity.class, 6, 7);
        registerWithContinuousSlotSequence(event, HmagWitherSkeletonGirlEntity.class, 6, 7);

        registerWithContinuousSlotSequence(event, HmagEnderExecutorEntity.class, 3, 5)
            .addSpecialSlot("enderman_hand_block", accessMobAdditionalInventory(2)).addSpecialSlotItem("nffgirls:enderman_hand_block");

        registerWithContinuousSlotSequence(event, HmagAlrauneEntity.class, 0, 3);
        registerWithContinuousSlotSequence(event, HmagBansheeEntity.class, 2, 5);
        registerWithContinuousSlotSequence(event, HmagCrimsonSlaughtererEntity.class, 0, 4);
        registerWithContinuousSlotSequence(event, HmagCursedDollEntity.class, 2, 8);
        registerWithContinuousSlotSequence(event, HmagDodomekiEntity.class, 2, 6);
        registerWithContinuousSlotSequence(event, HmagDullahanEntity.class, 2, 6);
        registerWithContinuousSlotSequence(event, HmagGhastlySeekerEntity.class, 0, 4);
        registerWithContinuousSlotSequence(event, HmagGlaryadEntity.class, 0, 3);
        registerWithContinuousSlotSequence(event, HmagHarpyEntity.class, 0, 4);
        registerWithContinuousSlotSequence(event, HmagHornetEntity.class, 2, 4);
        registerWithContinuousSlotSequence(event, HmagImpEntity.class, 2, 4);
        registerWithContinuousSlotSequence(event, HmagJackFrostEntity.class, 0, 4);
        registerWithContinuousSlotSequence(event, HmagJiangshiEntity.class, 3, 7);
        registerWithContinuousSlotSequence(event, HmagKoboldEntity.class, 2, 4);
        registerWithContinuousSlotSequence(event, HmagMeltyMonsterEntity.class, 0, 4);
        registerWithContinuousSlotSequence(event, HmagNecroticReaperEntity.class, 2, 6)
            .addSpecialSlot("main_hand", accessMobAdditionalInventory(0)).addSpecialSlotItem("nffgirls:necrotic_reaper_hoe")
            .addSpecialSlot("off_hand", accessMobAdditionalInventory(1)).addSpecialSlotItem("nffgirls:necrotic_reaper_hoe");
        registerWithContinuousSlotSequence(event, HmagNightwalkerEntity.class, 0, 4);
        registerWithContinuousSlotSequence(event, HmagRedcapEntity.class, 6, 7);
        registerWithContinuousSlotSequence(event, HmagSlimeGirlEntity.class, 0, 4);
        registerWithContinuousSlotSequence(event, HmagSnowCanineEntity.class, 0, 4);
    }

    @SubscribeEvent
    public static void baubleRegistration(RegisterBaublesEvent event)
    {
        // Load dedicated baubles from items, before registering NFF-Girls baubles to NFU bauble API
        ForgeRegistries.ITEMS.getEntries().stream()
            .filter(entry -> entry.getValue() instanceof INFFGirlsBauble)
            .forEach(entry -> {
                if (entry.getValue() instanceof NFFGirlsDedicatedBaubleItem item) {
                    item.loadProperties();
                    event.register(item);
                }
            });
        event.register(new NecroticReaperHandHoeBaubleBehavior(new ResourceLocation(NFFGirls.MOD_ID, "necrotic_reaper_hoe")));
        event.register(new EnderManHandBlockBaubleBehavior(new ResourceLocation(NFFGirls.MOD_ID, "enderman_hand_block")));
    }

}
