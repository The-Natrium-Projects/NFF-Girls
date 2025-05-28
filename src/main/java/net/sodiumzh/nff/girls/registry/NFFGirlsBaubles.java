package net.sodiumzh.nff.girls.registry;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.item.bauble.bauble.*;
import net.sodiumzh.nfu.util.NFUInfoStatics;

import java.util.function.Supplier;

public class NFFGirlsBaubles {

    /*public static void init(){}

    public static final NFURegistry<INFFGirlsBauble> NFFGIRLS_BAUBLES =
        new NFURegistry<>(new ResourceLocation(NFFGirls.MOD_ID, "baubles"));

    public static final NFURegistryEntryCollection<INFFGirlsBauble> NFFGIRLS_BAUBLES_COLLECTION
        = NFURegistryEntryCollection.create(NFFGIRLS_BAUBLES, NFFGirls.MOD_ID);
*/
    public static final DeferredRegister<Item> BAUBLE_ITEMS = DeferredRegister.create(
        ForgeRegistries.ITEMS, NFFGirls.MOD_ID);

    // Baubles
    // Desc utils
    public static Supplier<MutableComponent> baubleHPRecovery(double rawValue) {
        return () -> NFUInfoStatics.createTranslatable("info.nffgirls.bauble.healing_per_second",
            String.format("%.2f", NFFGirlsConfigs.ValueCache.Baubles.BAUBLE_HEALTH_RECOVERY_SCALE * rawValue)).withStyle(ChatFormatting.GRAY);
    }
    public static Supplier<MutableComponent> baubleHPMax(double rawValue) {
        return () -> NFUInfoStatics.createTranslatable("info.nffgirls.bauble.hpmax",
            String.format("+%.1f", NFFGirlsConfigs.ValueCache.Baubles.BAUBLE_MAX_HP_BOOSTING_SCALE * rawValue)).withStyle(ChatFormatting.GRAY);
    }
    public static Supplier<MutableComponent> baubleAtk(double rawValue) {
        return () -> NFUInfoStatics.createTranslatable("info.nffgirls.bauble.atk",
            String.format("+%.1f", NFFGirlsConfigs.ValueCache.Baubles.BAUBLE_ATK_BOOSTING_SCALE * rawValue)).withStyle(ChatFormatting.GRAY);
    }
    public static Supplier<MutableComponent> baubleArmor(double rawValue) {
        return () -> NFUInfoStatics.createTranslatable("info.nffgirls.bauble.armor",
            String.format("+%.1f", NFFGirlsConfigs.ValueCache.Baubles.BAUBLE_ARMOR_BOOSTING_SCALE * rawValue)).withStyle(ChatFormatting.GRAY);
    }

    // Registry
    public static final RegistryObject<SoulAmuletBaubleItem> SOUL_AMULET = BAUBLE_ITEMS.register("soul_amulet", () -> new SoulAmuletBaubleItem(
        1, new Item.Properties().rarity(Rarity.UNCOMMON))
        .description(NFUInfoStatics.createTranslatable("info.nffgirls.bauble.soul_amulet").withStyle(ChatFormatting.GRAY))
        .description(baubleHPMax(10.0))
        .description(baubleAtk(3.0))
        .description(NFUInfoStatics.createTranslatable("info.nffgirls.bauble.environment_immunity").withStyle(ChatFormatting.GRAY)).cast());
    public static final RegistryObject<SoulAmuletBaubleItem> SOUL_AMULET_II = BAUBLE_ITEMS.register("soul_amulet_ii", () -> new SoulAmuletBaubleItem(
        2, new Item.Properties().rarity(Rarity.RARE)).alwaysFoil()
        .description(NFUInfoStatics.createTranslatable("info.nffgirls.bauble.soul_amulet").withStyle(ChatFormatting.GRAY))
        .description(baubleHPMax(15.0))
        .description(baubleAtk(5.0))
        .description(NFUInfoStatics.createTranslatable("info.nffgirls.bauble.speed", "+10%").withStyle(ChatFormatting.GRAY))
        .description(NFUInfoStatics.createTranslatable("info.nffgirls.bauble.environment_immunity").withStyle(ChatFormatting.GRAY)).cast());
    public static final RegistryObject<SoulAmuletBaubleItem> SOUL_AMULET_III = BAUBLE_ITEMS.register("soul_amulet_iii", () -> new SoulAmuletBaubleItem(
        3, new Item.Properties().rarity(Rarity.RARE))
        .description(NFUInfoStatics.createTranslatable("info.nffgirls.bauble.soul_amulet").withStyle(ChatFormatting.GRAY))
        .description(baubleHPMax(25.0))
        .description(baubleAtk(8.0))
        .description(NFUInfoStatics.createTranslatable("info.nffgirls.bauble.speed", "+15%").withStyle(ChatFormatting.GRAY))
        .description(NFUInfoStatics.createTranslatable("info.nffgirls.bauble.environment_immunity").withStyle(ChatFormatting.GRAY)).cast());
    public static final RegistryObject<SoulAmuletBaubleItem> SOUL_AMULET_IV = BAUBLE_ITEMS.register("soul_amulet_iv", () -> new SoulAmuletBaubleItem(
        4, new Item.Properties().rarity(Rarity.EPIC)).alwaysFoil()
        .description(NFUInfoStatics.createTranslatable("info.nffgirls.bauble.soul_amulet").withStyle(ChatFormatting.GRAY))
        .description(baubleHPMax(40.0))
        .description(baubleAtk(12.0))
        .description(NFUInfoStatics.createTranslatable("info.nffgirls.bauble.speed", "+20%").withStyle(ChatFormatting.GRAY))
        .description(baubleHPRecovery(0.1))
        .description(NFUInfoStatics.createTranslatable("info.nffgirls.bauble.environment_immunity").withStyle(ChatFormatting.GRAY)).cast());
    public static final RegistryObject<CourageAmuletBaubleItem> COURAGE_AMULET = BAUBLE_ITEMS.register("courage_amulet", () -> new CourageAmuletBaubleItem(
        1, new Item.Properties().rarity(Rarity.UNCOMMON))
        .description(NFUInfoStatics.createTranslatable("info.nffgirls.bauble.active_attack").withStyle(ChatFormatting.GRAY))
        .description(baubleAtk(4.0))
        .description(NFUInfoStatics.createTranslatable("info.nffgirls.bauble.speed", "+20%").withStyle(ChatFormatting.GRAY)).cast());
    public static final RegistryObject<CourageAmuletBaubleItem> COURAGE_AMULET_II = BAUBLE_ITEMS.register("courage_amulet_ii", () -> new CourageAmuletBaubleItem(
        2, new Item.Properties().rarity(Rarity.RARE)).alwaysFoil()
        .description(NFUInfoStatics.createTranslatable("info.nffgirls.bauble.active_attack").withStyle(ChatFormatting.GRAY))
        .description(baubleAtk(6.0))
        .description(NFUInfoStatics.createTranslatable("info.nffgirls.bauble.speed", "+30%").withStyle(ChatFormatting.GRAY)).cast());
    public static final RegistryObject<ResistanceGemBaubleItem> RESISTANCE_GEM = BAUBLE_ITEMS.register("resistance_gem", () -> new ResistanceGemBaubleItem(
        1, new Item.Properties().rarity(Rarity.UNCOMMON))
        .description(NFUInfoStatics.createTranslatable("info.nffgirls.bauble.environment_immunity").withStyle(ChatFormatting.GRAY)).cast());
    public static final RegistryObject<ResistanceAmuletBaubleItem> RESISTANCE_AMULET = BAUBLE_ITEMS.register("resistance_amulet", () -> new ResistanceAmuletBaubleItem(
        1, new Item.Properties().rarity(Rarity.UNCOMMON))
        .description(baubleArmor(4.0))
        .description(baubleHPMax(15.0))
        .description(NFUInfoStatics.createTranslatable("info.nffgirls.bauble.environment_immunity").withStyle(ChatFormatting.GRAY)).cast());
    public static final RegistryObject<ResistanceAmuletBaubleItem> RESISTANCE_AMULET_II = BAUBLE_ITEMS.register("resistance_amulet_ii", () -> new ResistanceAmuletBaubleItem(
        2, new Item.Properties().rarity(Rarity.UNCOMMON)).alwaysFoil()
        .description(baubleArmor(6.0))
        .description(baubleHPMax(25.0))
        .description(NFUInfoStatics.createTranslatable("info.nffgirls.bauble.environment_immunity").withStyle(ChatFormatting.GRAY)).cast());
    public static final RegistryObject<HealingJadeBaubleItem> HEALING_JADE = BAUBLE_ITEMS.register("healing_jade", () -> new HealingJadeBaubleItem(
        1, new Item.Properties().rarity(Rarity.UNCOMMON))
        .description(baubleHPRecovery(0.1)).cast());
    public static final RegistryObject<LifeJadeBaubleItem> LIFE_JADE = BAUBLE_ITEMS.register("life_jade", () -> new LifeJadeBaubleItem(
        1, new Item.Properties().rarity(Rarity.UNCOMMON))
        .description(baubleHPRecovery(0.15))
        .description(baubleHPMax(5.0))
        .description(NFUInfoStatics.createTranslatable("info.nffgirls.bauble.environment_immunity").withStyle(ChatFormatting.GRAY)).cast());
    public static final RegistryObject<LifeJadeBaubleItem> LIFE_JADE_II = BAUBLE_ITEMS.register("life_jade_ii", () -> new LifeJadeBaubleItem(
        2, new Item.Properties().rarity(Rarity.RARE)).alwaysFoil()
        .description(baubleHPRecovery(0.2))
        .description(baubleHPMax(10.0))
        .description(NFUInfoStatics.createTranslatable("info.nffgirls.bauble.environment_immunity").withStyle(ChatFormatting.GRAY)).cast());

    public static final RegistryObject<AquaJadeBaubleItem> AQUA_JADE = BAUBLE_ITEMS.register("aqua_jade", () -> new AquaJadeBaubleItem(
        1, new Item.Properties().rarity(Rarity.UNCOMMON))
        .description(NFUInfoStatics.createTranslatable("info.nffgirls.bauble.aqua_jade").withStyle(ChatFormatting.GRAY))
        .description(NFUInfoStatics.createTranslatable("info.nffgirls.bauble.in_water").withStyle(ChatFormatting.GRAY))
        .description(NFUInfoStatics.createTranslatable("info.nffgirls.bauble.speed", "4x").withStyle(ChatFormatting.GRAY))
        .description(baubleHPRecovery(0.25))
        .description(NFUInfoStatics.createTranslatable("info.nffgirls.bauble.environment_immunity").withStyle(ChatFormatting.GRAY)).cast());
    public static final RegistryObject<PoisonousThornBaubleItem> POISONOUS_THORN = BAUBLE_ITEMS.register("poisonous_thorn", () -> new PoisonousThornBaubleItem(
        1, new Item.Properties().rarity(Rarity.UNCOMMON))
        .description(NFUInfoStatics.createTranslatable("info.nffgirls.bauble.poisonous_thorn").withStyle(ChatFormatting.GRAY))
        .description(NFUInfoStatics.createTranslatable("info.nffgirls.bauble.poisonous_thorn_1").withStyle(ChatFormatting.GRAY)).cast());


}
