package net.sodiumzh.nff.girls.registry;

import com.github.mechalopa.hmag.registry.ModItems;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
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
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nff.services.entity.taming.INFFTamedSunSensitiveMob;
import net.sodiumzh.nff.services.entity.taming.NFFTamingMapping;
import net.sodiumzh.nfu.container.Tuple2;
import net.sodiumzh.nfu.container.Tuple3;
import net.sodiumzh.nfu.item.bauble.BaubleEquippingCondition;
import net.sodiumzh.nfu.item.bauble.RegisterBaubleEquippableMobsEvent;
import net.sodiumzh.nfu.item.bauble.RegisterBaublesEvent;
import net.sodiumzh.nfu.object.Validatable;
import net.sodiumzh.nfu.registry.NFURegistry;
import net.sodiumzh.nfu.registry.NFURegistryEntryCollection;
import net.sodiumzh.nfu.registry.NFURegistryGenerateValuesEvent;
import net.sodiumzh.nfu.util.NFUInfoStatics;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

@Mod.EventBusSubscriber(modid = NFFGirls.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NFFGirlsBaubles {

    // Just for loading this class
    public static void init(){}

    /**
     * All NFF-Girls baubles, including dedicated items and behaviors.
     * <p>Do not manually register if the bauble is created from {@link NFFGirlsBaubleBuilder}
     * as it's registered during building. Baubles created by other ways still need to register.
     */
    public static final NFURegistry<INFFGirlsBauble> BAUBLE_REGISTRY =
        new NFURegistry<INFFGirlsBauble>(new ResourceLocation(NFFGirls.MOD_ID, "baubles"))
            .setShouldGenerateOnCommonSetup();

    public static final NFURegistryEntryCollection<INFFGirlsBauble> BAUBLES =
        NFURegistryEntryCollection.create(BAUBLE_REGISTRY, NFFGirls.MOD_ID);

    public static final DeferredRegister<Item> BAUBLE_ITEMS = DeferredRegister.create(
        ForgeRegistries.ITEMS, NFFGirls.MOD_ID);

    // SOME COMMON CONDITIONS

    public static final Tuple2<BaubleEquippingCondition, Component> CONDITION_UNDEAD = 
        Tuple2.of(BaubleEquippingCondition.of(args -> args.user().getMobType().equals(MobType.UNDEAD) || args.user().getType().is(NFFGirlsTags.EQUIPS_BAUBLES_AS_UNDEAD)),
            NFUInfoStatics.createTranslatable("tooltip.nffgirls.bauble.for_undead"));
    public static final Tuple2<BaubleEquippingCondition, Component> CONDITION_SUN_SENSITIVE =
        Tuple2.of(BaubleEquippingCondition.of(args -> args.user() instanceof INFFTamedSunSensitiveMob || args.user().getType().is(NFFGirlsTags.EQUIPS_BAUBLES_AS_SUN_SENSITIVE)),
            NFUInfoStatics.createTranslatable("tooltip.nffgirls.bauble.for_sun_sensitive"));
    public static final Tuple2<BaubleEquippingCondition, Component> CONDITION_UNDEAD_AND_SUN_SENSITIVE =
        Tuple2.of(BaubleEquippingCondition.of(args -> args.user().getMobType().equals(MobType.UNDEAD) || args.user().getType().is(NFFGirlsTags.EQUIPS_BAUBLES_AS_UNDEAD)
            || args.user() instanceof INFFTamedSunSensitiveMob || args.user().getType().is(NFFGirlsTags.EQUIPS_BAUBLES_AS_SUN_SENSITIVE)),
            NFUInfoStatics.createTranslatable("tooltip.nffgirls.bauble.for_undead_and_sun_sensitive"));
    public static final Tuple2<BaubleEquippingCondition, Component> CONDITION_ARTHROPOD = Tuple2.of(BaubleEquippingCondition.of(
        args -> args.user().getMobType().equals(MobType.ARTHROPOD)),
        NFUInfoStatics.createTranslatable("tooltip.nffgirls.bauble.for_arthropod"));
    public static final Tuple2<BaubleEquippingCondition, Component> CONDITION_AQUATIC = Tuple2.of(BaubleEquippingCondition.of(args -> 
        args.user().getType().is(NFFGirlsTags.AQUATIC_MOB) || args.user().getMobType().equals(MobType.WATER)
        || Optional.ofNullable(NFFTamingMapping.getTypeBefore(args.user())).filter(t -> t.is(NFFGirlsTags.AQUATIC_MOB)).isPresent()),
        NFUInfoStatics.createTranslatable("tooltip.nffgirls.bauble.for_aquatic"));
    public static final Tuple2<BaubleEquippingCondition, Component> CONDITION_NETHER = Tuple2.of(BaubleEquippingCondition.of(args ->
        args.user().getType().is(NFFGirlsTags.NETHER_MOB)), NFUInfoStatics.createTranslatable("tooltip.nffgirls.bauble.for_nether"));


    public static final Predicate<Mob> EFFECT_CONDITION_IN_WATER = Entity::isInWater;
    public static final Predicate<Mob> EFFECT_CONDITION_AT_NIGHT = mob -> mob.level().isNight();

    // Rarity name format

    public static final UnaryOperator<MutableComponent> RARITY_TIER_0 = c -> c.withStyle(ChatFormatting.WHITE);
    public static final MutableComponent RARITY_TIER_DISC_0 =
        NFUInfoStatics.createTranslatable("tooltip.nffgirls.bauble.rarity_tier", 0).withStyle(ChatFormatting.WHITE);
    public static final UnaryOperator<MutableComponent> RARITY_TIER_1 = c -> c.withStyle(ChatFormatting.YELLOW);
    public static final MutableComponent RARITY_TIER_DISC_1 =
        NFUInfoStatics.createTranslatable("tooltip.nffgirls.bauble.rarity_tier", 1).withStyle(ChatFormatting.YELLOW);
    public static final UnaryOperator<MutableComponent> RARITY_TIER_2 = c -> c.withStyle(ChatFormatting.GREEN);
    public static final MutableComponent RARITY_TIER_DISC_2 =
        NFUInfoStatics.createTranslatable("tooltip.nffgirls.bauble.rarity_tier", 2).withStyle(ChatFormatting.GREEN);
    public static final UnaryOperator<MutableComponent> RARITY_TIER_3 = c -> c.withStyle(s -> s.withColor(DyeColor.PINK.getTextColor()));
    public static final MutableComponent RARITY_TIER_DISC_3 =
        NFUInfoStatics.createTranslatable("tooltip.nffgirls.bauble.rarity_tier", 3).withStyle(s -> s.withColor(DyeColor.PINK.getTextColor()));
    public static final UnaryOperator<MutableComponent> RARITY_TIER_4 = c -> c.withStyle(ChatFormatting.BLUE);
    public static final MutableComponent RARITY_TIER_DISC_4 =
        NFUInfoStatics.createTranslatable("tooltip.nffgirls.bauble.rarity_tier", 4).withStyle(ChatFormatting.BLUE);
    public static final UnaryOperator<MutableComponent> RARITY_TIER_5 = c -> c.withStyle(s -> s.withColor(DyeColor.ORANGE.getTextColor()));
    public static final MutableComponent RARITY_TIER_DISC_5 =
        NFUInfoStatics.createTranslatable("tooltip.nffgirls.bauble.rarity_tier", 5).withStyle(s -> s.withColor(DyeColor.ORANGE.getTextColor()));
    public static final UnaryOperator<MutableComponent> RARITY_TIER_6 = c -> c.withStyle(ChatFormatting.RED);
    public static final MutableComponent RARITY_TIER_DISC_6 =
        NFUInfoStatics.createTranslatable("tooltip.nffgirls.bauble.rarity_tier", 6).withStyle(ChatFormatting.RED);
    public static final UnaryOperator<MutableComponent> RARITY_TIER_7 = c -> c.withStyle(s -> s.withColor(DyeColor.LIGHT_BLUE.getTextColor()));
    public static final MutableComponent RARITY_TIER_DISC_7 =
        NFUInfoStatics.createTranslatable("tooltip.nffgirls.bauble.rarity_tier", 7).withStyle(s -> s.withColor(DyeColor.LIGHT_BLUE.getTextColor()));
    public static final UnaryOperator<MutableComponent> RARITY_TIER_8 = c -> c.withStyle(ChatFormatting.DARK_PURPLE);
    public static final MutableComponent RARITY_TIER_DISC_8 =
        NFUInfoStatics.createTranslatable("tooltip.nffgirls.bauble.rarity_tier", 8).withStyle(ChatFormatting.DARK_PURPLE);

    // =========== AMULETS ============ //

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> CRUDE_AMULET = BAUBLE_ITEMS.register("crude_amulet", () -> new NFFGirlsBaubleBuilder()
        .repeatable(Attributes.MAX_HEALTH, 3d, AttributeModifier.Operation.ADDITION)
        .repeatable(Attributes.ARMOR, 1d, AttributeModifier.Operation.ADDITION)
        .addTooltip(RARITY_TIER_DISC_0)
        .addAllTooltips()
        .buildAsBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "crude_amulet"), 1, new Item.Properties())
        .setNameStyle(RARITY_TIER_0).cast());

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> REFINED_AMULET = BAUBLE_ITEMS.register("refined_amulet", () -> new NFFGirlsBaubleBuilder()
        .repeatable(Attributes.MAX_HEALTH, 5d, AttributeModifier.Operation.ADDITION)
        .repeatable(Attributes.ARMOR, 2d, AttributeModifier.Operation.ADDITION)
        .repeatable(Attributes.ATTACK_DAMAGE, 1d, AttributeModifier.Operation.ADDITION)
        .addTooltip(RARITY_TIER_DISC_1)
        .addAllTooltips()
        .buildAsBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "refined_amulet"), 1, new Item.Properties())
        .setNameStyle(RARITY_TIER_1).cast());

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> SOUL_AMULET = BAUBLE_ITEMS.register("soul_amulet", () -> new NFFGirlsBaubleBuilder()
        .equippingCondition(CONDITION_UNDEAD)
        .repeatable(Attributes.MAX_HEALTH, 10d, AttributeModifier.Operation.ADDITION)
        .repeatable(Attributes.ATTACK_DAMAGE, 3d, AttributeModifier.Operation.ADDITION)
        .environmentResistance()
        .addTooltip(RARITY_TIER_DISC_2)
        .addEquippingConditionTooltip()
        .addAllTooltips()
        .buildAsBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "soul_amulet"), 1, new Item.Properties().rarity(Rarity.UNCOMMON))
        .setNameStyle(RARITY_TIER_2).cast());

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> SOUL_AMULET_II = BAUBLE_ITEMS.register("soul_amulet_ii", () -> new NFFGirlsBaubleBuilder()
        .equippingCondition(CONDITION_UNDEAD)
        .repeatable(Attributes.MAX_HEALTH, 15d, AttributeModifier.Operation.ADDITION)
        .repeatable(Attributes.ATTACK_DAMAGE, 5d, AttributeModifier.Operation.ADDITION)
        .repeatable(Attributes.MOVEMENT_SPEED, 0.1d, AttributeModifier.Operation.MULTIPLY_BASE)
        .environmentResistance()
        .addTooltip(RARITY_TIER_DISC_3)
        .addEquippingConditionTooltip()
        .addAllTooltips()
        .buildAsBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "soul_amulet"), 2, new Item.Properties().rarity(Rarity.RARE))
        .setNameStyle(RARITY_TIER_3).alwaysFoil().cast());

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> SOUL_AMULET_III = BAUBLE_ITEMS.register("soul_amulet_iii", () -> new NFFGirlsBaubleBuilder()
        .repeatable(Attributes.MAX_HEALTH, 25d, AttributeModifier.Operation.ADDITION)
        .repeatable(Attributes.ATTACK_DAMAGE, 8d, AttributeModifier.Operation.ADDITION)
        .repeatable(Attributes.MOVEMENT_SPEED, 0.15d, AttributeModifier.Operation.MULTIPLY_BASE)
        .environmentResistance()
        .addTooltip(RARITY_TIER_DISC_4)
        .addEquippingConditionTooltip()
        .addAllTooltips()
        .buildAsBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "soul_amulet"), 3, new Item.Properties().rarity(Rarity.RARE))
        .setNameStyle(RARITY_TIER_4).cast());

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> SOUL_AMULET_IV = BAUBLE_ITEMS.register("soul_amulet_iv", () -> new NFFGirlsBaubleBuilder()
        .equippingCondition(CONDITION_UNDEAD)
        .repeatable(Attributes.MAX_HEALTH, 40d, AttributeModifier.Operation.ADDITION)
        .repeatable(Attributes.ATTACK_DAMAGE, 12d, AttributeModifier.Operation.ADDITION)
        .repeatable(Attributes.MOVEMENT_SPEED, 0.2d, AttributeModifier.Operation.MULTIPLY_BASE)
        .repeatable(NFFGirlsEntityAttributes.PERSISTENT_HEALING_PER_SECOND.get(), 0.1d, AttributeModifier.Operation.ADDITION)
        .environmentResistance()
        .addTooltip(RARITY_TIER_DISC_6)
        .addEquippingConditionTooltip()
        .addAllTooltips()
        .buildAsBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "soul_amulet"), 4, new Item.Properties().rarity(Rarity.EPIC))
        .setNameStyle(RARITY_TIER_6).alwaysFoil().cast());


   // AMULETS

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> RESISTANCE_AMULET = BAUBLE_ITEMS.register("resistance_amulet", () -> new NFFGirlsBaubleBuilder()
        .repeatable(Attributes.ARMOR, 4.0d, AttributeModifier.Operation.ADDITION)
        .repeatable(Attributes.MAX_HEALTH, 15.0d, AttributeModifier.Operation.ADDITION)
        .environmentResistance()
        .addTooltip(RARITY_TIER_DISC_2)
        .addAllTooltips()
        .buildAsBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "resistance_amulet"), 1, new Item.Properties().rarity(Rarity.UNCOMMON))
        .setNameStyle(RARITY_TIER_2).cast());

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> RESISTANCE_AMULET_II = BAUBLE_ITEMS.register("resistance_amulet_ii", () -> new NFFGirlsBaubleBuilder()
        .repeatable(Attributes.ARMOR, 6.0d, AttributeModifier.Operation.ADDITION)
        .repeatable(Attributes.MAX_HEALTH, 25.0d, AttributeModifier.Operation.ADDITION)
        .repeatable(Attributes.KNOCKBACK_RESISTANCE, 0.1d, AttributeModifier.Operation.MULTIPLY_BASE)
        .environmentResistance()
        .addTooltip(RARITY_TIER_DISC_3)
        .addAllTooltips()
        .buildAsBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "resistance_amulet"), 2, new Item.Properties().rarity(Rarity.RARE))
        .setNameStyle(RARITY_TIER_3).alwaysFoil().cast());

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> RESISTANCE_AMULET_III = BAUBLE_ITEMS.register("resistance_amulet_iii", () -> new NFFGirlsBaubleBuilder()
        .repeatable(Attributes.ARMOR, 8.0d, AttributeModifier.Operation.ADDITION)
        .repeatable(Attributes.MAX_HEALTH, 40.0d, AttributeModifier.Operation.ADDITION)
        .repeatable(Attributes.KNOCKBACK_RESISTANCE, 0.3d, AttributeModifier.Operation.MULTIPLY_BASE)
        .environmentResistance()
        .addTooltip(RARITY_TIER_DISC_4)
        .addAllTooltips()
        .buildAsBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "resistance_amulet"), 3, new Item.Properties().rarity(Rarity.RARE))
        .setNameStyle(RARITY_TIER_4).alwaysFoil().cast());

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> CORRUPTED_AMULET
        = BAUBLE_ITEMS.register("corrupted_amulet", () -> new NFFGirlsBaubleBuilder()
        .repeatable(NFFGirlsEntityAttributes.WITHER_ASPECT.get(), 1.0d, AttributeModifier.Operation.ADDITION)
        .addTooltip(RARITY_TIER_DISC_2)
        .addAllTooltips()
        .buildAsBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "corrupted_amulet"), 1, new Item.Properties().rarity(Rarity.RARE))
        .setNameStyle(RARITY_TIER_2).alwaysFoil().cast());

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> CORRUPTED_AMULET_II
        = BAUBLE_ITEMS.register("corrupted_amulet_ii", () -> new NFFGirlsBaubleBuilder()
        .repeatable(NFFGirlsEntityAttributes.WITHER_ASPECT.get(), 1.0d, AttributeModifier.Operation.ADDITION)
        .repeatable(Attributes.ATTACK_DAMAGE, 2.0d, AttributeModifier.Operation.ADDITION)
        .addTooltip(RARITY_TIER_DISC_2)
        .addAllTooltips()
        .buildAsBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "corrupted_amulet"), 2, new Item.Properties().rarity(Rarity.RARE))
        .setNameStyle(RARITY_TIER_2).alwaysFoil().cast());

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> WITHER_AMULET
        = BAUBLE_ITEMS.register("wither_amulet", () -> new NFFGirlsBaubleBuilder()
        .equippingCondition(CONDITION_NETHER)
        .repeatable(NFFGirlsEntityAttributes.WITHER_ASPECT.get(), 2.0d, AttributeModifier.Operation.ADDITION)
        .repeatable(Attributes.MAX_HEALTH, 10.0d, AttributeModifier.Operation.ADDITION)
        .addTooltip(RARITY_TIER_DISC_3)
        .addEquippingConditionTooltip()
        .addAllTooltips()
        .buildAsBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "wither_amulet"), 1, new Item.Properties().rarity(Rarity.RARE))
        .setNameStyle(RARITY_TIER_3).alwaysFoil().cast());

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> WITHER_AMULET_II
        = BAUBLE_ITEMS.register("wither_amulet_ii", () -> new NFFGirlsBaubleBuilder()
        .equippingCondition(CONDITION_NETHER)
        .repeatable(NFFGirlsEntityAttributes.WITHER_ASPECT.get(), 2.5d, AttributeModifier.Operation.ADDITION)
        .repeatable(Attributes.MAX_HEALTH, 20.0d, AttributeModifier.Operation.ADDITION)
        .repeatable(Attributes.ATTACK_DAMAGE, 3.0d, AttributeModifier.Operation.ADDITION)
        .addTooltip(RARITY_TIER_DISC_5)
        .addEquippingConditionTooltip()
        .addAllTooltips()
        .buildAsBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "wither_amulet"), 2, new Item.Properties().rarity(Rarity.RARE))
        .setNameStyle(RARITY_TIER_5).alwaysFoil().cast());

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> WITHER_AMULET_III
        = BAUBLE_ITEMS.register("wither_amulet_iii", () -> new NFFGirlsBaubleBuilder()
        .equippingCondition(CONDITION_NETHER)
        .repeatable(NFFGirlsEntityAttributes.WITHER_ASPECT.get(), 4.0d, AttributeModifier.Operation.ADDITION)
        .repeatable(Attributes.MAX_HEALTH, 30d, AttributeModifier.Operation.ADDITION)
        .repeatable(Attributes.ATTACK_DAMAGE, 5.0d, AttributeModifier.Operation.ADDITION)
        .addTooltip(RARITY_TIER_DISC_6)
        .addEquippingConditionTooltip()
        .addAllTooltips()
        .buildAsBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "wither_amulet"), 3, new Item.Properties().rarity(Rarity.RARE))
        .setNameStyle(RARITY_TIER_6).alwaysFoil().cast());

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> ABYSS_AMULET
        = BAUBLE_ITEMS.register("abyss_amulet", () -> new NFFGirlsBaubleBuilder()
        .equippingCondition(CONDITION_NETHER)
        .repeatable(NFFGirlsEntityAttributes.WITHER_ASPECT.get(), 5.0d, AttributeModifier.Operation.ADDITION)
        .repeatable(NFFGirlsEntityAttributes.POISON_ASPECT.get(), 5.0d, AttributeModifier.Operation.ADDITION)
        .repeatable(Attributes.ATTACK_DAMAGE, 5.0d, AttributeModifier.Operation.ADDITION)
        .repeatable(NFFGirlsEntityAttributes.PERSISTENT_HEALING_PER_SECOND.get(), 0.1d, AttributeModifier.Operation.ADDITION)
        .repeatable(Attributes.ARMOR, -0.2d, AttributeModifier.Operation.MULTIPLY_BASE)
        .repeatable(Attributes.MAX_HEALTH, -0.15D, AttributeModifier.Operation.MULTIPLY_TOTAL)
        .addTooltip(RARITY_TIER_DISC_5)
        .addEquippingConditionTooltip()
        .addAllTooltips()
        .buildAsBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "abyss_amulet"), 1, new Item.Properties().rarity(Rarity.RARE))
        .setNameStyle(RARITY_TIER_5).alwaysFoil().cast());

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> ABYSS_AMULET_II
        = BAUBLE_ITEMS.register("abyss_amulet_ii", () -> new NFFGirlsBaubleBuilder()
        .equippingCondition(CONDITION_NETHER)
        .repeatable(NFFGirlsEntityAttributes.WITHER_ASPECT.get(), 7.0d, AttributeModifier.Operation.ADDITION)
        .repeatable(NFFGirlsEntityAttributes.POISON_ASPECT.get(), 7.0d, AttributeModifier.Operation.ADDITION)
        .repeatable(Attributes.ATTACK_DAMAGE, 8.0d, AttributeModifier.Operation.ADDITION)
        .repeatable(NFFGirlsEntityAttributes.PERSISTENT_HEALING_PER_SECOND.get(), 0.15d, AttributeModifier.Operation.ADDITION)
        .repeatable(Attributes.ARMOR, -0.25d, AttributeModifier.Operation.MULTIPLY_BASE)
        .repeatable(Attributes.KNOCKBACK_RESISTANCE, -0.1d, AttributeModifier.Operation.MULTIPLY_BASE)
        .repeatable(Attributes.MAX_HEALTH, -0.25d, AttributeModifier.Operation.MULTIPLY_TOTAL)
        .addTooltip(RARITY_TIER_DISC_7)
        .addEquippingConditionTooltip()
        .addAllTooltips()
        .buildAsBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "abyss_amulet"), 2, new Item.Properties().rarity(Rarity.RARE))
        .setNameStyle(RARITY_TIER_7).alwaysFoil().cast());

    // =========== BADGE =========== //


    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> COURAGE_BADGE = BAUBLE_ITEMS.register("courage_badge", () -> new NFFGirlsBaubleBuilder()
        .repeatable(Attributes.ATTACK_DAMAGE, 4.0d, AttributeModifier.Operation.ADDITION)
        .repeatable(Attributes.MOVEMENT_SPEED, 0.2d, AttributeModifier.Operation.MULTIPLY_BASE)
        .addTag(INFFGirlsBauble.TAG_ACTIVE_ATTACK_1)
        .addTooltip(RARITY_TIER_DISC_0)
        .addTooltipTranslatable("tooltip.nffgirls.bauble.active_attack")
        .addAllTooltips()
        .buildAsBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "courage_badge"), 1, new Item.Properties().rarity(Rarity.UNCOMMON))
        .setNameStyle(RARITY_TIER_0).cast());

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> COURAGE_AMULET_II = BAUBLE_ITEMS.register("courage_badge_ii", () -> new NFFGirlsBaubleBuilder()
        .repeatable(Attributes.ATTACK_DAMAGE, 6.0d, AttributeModifier.Operation.ADDITION)
        .repeatable(Attributes.MOVEMENT_SPEED, 0.3d, AttributeModifier.Operation.MULTIPLY_BASE)
        .addTag(INFFGirlsBauble.TAG_ACTIVE_ATTACK_2)
        .addTooltipTranslatable("tooltip.nffgirls.bauble.active_attack")
        .addAllTooltips()
        .buildAsBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "courage_badge"), 2, new Item.Properties().rarity(Rarity.RARE)).alwaysFoil().cast());



    // =========== JADE ============ //

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> HEALING_JADE = BAUBLE_ITEMS.register("healing_jade", () -> new NFFGirlsBaubleBuilder()
        .repeatable(NFFGirlsEntityAttributes.PERSISTENT_HEALING_PER_SECOND.get(), 0.1d, AttributeModifier.Operation.ADDITION)
        .addAllTooltips()
        .buildAsBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "healing_jade"), 1, new Item.Properties().rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> LIFE_JADE = BAUBLE_ITEMS.register("life_jade", () -> new NFFGirlsBaubleBuilder()
        .repeatable(NFFGirlsEntityAttributes.PERSISTENT_HEALING_PER_SECOND.get(), 0.15d, AttributeModifier.Operation.ADDITION)
        .repeatable(Attributes.MAX_HEALTH, 5.0d, AttributeModifier.Operation.ADDITION)
        .environmentResistance()
        .addAllTooltips()
        .buildAsBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "life_jade"), 1, new Item.Properties().rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> LIFE_JADE_II = BAUBLE_ITEMS.register("life_jade_ii", () -> new NFFGirlsBaubleBuilder()
        .repeatable(NFFGirlsEntityAttributes.PERSISTENT_HEALING_PER_SECOND.get(), 0.2d, AttributeModifier.Operation.ADDITION)
        .repeatable(Attributes.MAX_HEALTH, 10.0d, AttributeModifier.Operation.ADDITION)
        .environmentResistance()
        .addAllTooltips()
        .buildAsBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "life_jade"), 2, new Item.Properties().rarity(Rarity.RARE)).alwaysFoil().cast());

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> AQUA_JADE = BAUBLE_ITEMS.register("aqua_jade", () -> new NFFGirlsBaubleBuilder()
        .equippingCondition(CONDITION_AQUATIC)
        .unrepeatable(Attributes.MOVEMENT_SPEED, 3.0d, AttributeModifier.Operation.MULTIPLY_BASE, EFFECT_CONDITION_IN_WATER)
        .repeatable(NFFGirlsEntityAttributes.PERSISTENT_HEALING_PER_SECOND.get(), 0.25d, AttributeModifier.Operation.ADDITION, EFFECT_CONDITION_IN_WATER)
        .repeatable(NFFGirlsEntityAttributes.WATER_ASPECT.get(), 0.2d, AttributeModifier.Operation.ADDITION)
        .environmentResistance()
        .addRepeatableModifierTooltips(() -> NFFGirlsBaubleBuilder.NO_CONDITION)
        .environmentResistance()
        .addTooltipTranslatable("tooltip.nffgirls.bauble.when_in_water")
        .addUnrepeatableModifierTooltips()
        .addAllTooltips(() -> EFFECT_CONDITION_IN_WATER)
        .buildAsBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "aqua_jade"), 1, new Item.Properties().rarity(Rarity.RARE)).cast());

    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> POISON_JADE = BAUBLE_ITEMS.register("poison_jade", () -> new NFFGirlsBaubleBuilder()
        .equippingCondition(CONDITION_ARTHROPOD)
        .repeatable(NFFGirlsEntityAttributes.POISON_ASPECT.get(), 1.0D, AttributeModifier.Operation.ADDITION)
        .addEquippingConditionTooltip()
        .addAllTooltips()
        .buildAsBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "poison_jade"), 1, new Item.Properties().rarity(Rarity.UNCOMMON)));

    // Misc
    public static final RegistryObject<NFFGirlsDedicatedBaubleItem> RESISTANCE_CORE = BAUBLE_ITEMS.register("resistance_core", () -> new NFFGirlsBaubleBuilder()
        .environmentResistance()
        .addTooltip(RARITY_TIER_DISC_0)
        .buildAsBaubleItem(new ResourceLocation(NFFGirls.MOD_ID, "resistance_core"), 1, new Item.Properties())
        .setNameStyle(RARITY_TIER_0).cast());

    public static final NFURegistry.Accessor<NFFGirlsBaubleBehavior> INSOMNIA_FRUIT =
        BAUBLES.register("insomnia_fruit", () -> new NFFGirlsBaubleBuilder()
        .repeatable(Attributes.ATTACK_DAMAGE, 5d, AttributeModifier.Operation.ADDITION)
        .repeatable(Attributes.MAX_HEALTH, 50d, AttributeModifier.Operation.ADDITION)
        .repeatable(NFFGirlsEntityAttributes.PERSISTENT_HEALING_PER_SECOND.get(), 0.1d, AttributeModifier.Operation.ADDITION)
        .addTooltipTranslatable("tooltip.nffgirls.bauble.when_at_night")
        .addAllTooltips()
        .buildAsBaubleBehavior(new ResourceLocation(NFFGirls.MOD_ID, "insomnia_fruit"), 1, ModItems.INSOMNIA_FRUIT.get(), false));

   /* static {
        BAUBLE_REGISTRY.register()
    }*/

    // Register slots

    public static Function<Mob, ItemStack> accessMobAdditionalInventory(int position)
    {
        return mob -> {
            if (mob instanceof INFFTamed bm)
                return bm.getAdditionalInventory().getItem(position);
            else throw new IllegalArgumentException("Input mob isn't INFFTamed.");
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
        NFFGirlsBaubles.BAUBLE_REGISTRY.keySet().stream().map(NFFGirlsBaubles.BAUBLE_REGISTRY::getValue)
            .forEach(event::register);
        event.register(new NecroticReaperHandHoeBaubleBehavior(new ResourceLocation(NFFGirls.MOD_ID, "necrotic_reaper_hoe")));
        event.register(new EnderManHandBlockBaubleBehavior(new ResourceLocation(NFFGirls.MOD_ID, "enderman_hand_block")));
    }

    @SubscribeEvent
    public static void baubleAdditionalRegistration(NFFGirlsBaubleAdditionalRegistry.RegisterEvent event)
    {
        NFFGirlsBaubles.BAUBLE_REGISTRY.keySet().stream().map(NFFGirlsBaubles.BAUBLE_REGISTRY::getValue)
            .forEach(event::register);
    }

}
