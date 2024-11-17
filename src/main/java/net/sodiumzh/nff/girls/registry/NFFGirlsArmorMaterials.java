package net.sodiumzh.nff.girls.registry;

import com.google.common.base.Supplier;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.sodiumzh.nff.girls.NFFGirls;

@SuppressWarnings("deprecation")
public enum NFFGirlsArmorMaterials implements ArmorMaterial
{
	NECROMANCER(NFFGirls.MOD_ID + ":necromancer", 15, new int[] { 1, 2, 3, 1 }, 15, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0.0F, () -> Ingredient.of(Items.LEATHER)),
	SUNHAT(NFFGirls.MOD_ID + ":sunhat", 5, new int[] { 0, 2, 3, 1 }, 15, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0.0F, () -> Ingredient.of(Items.LEATHER));

	protected static final int[] HEALTH_PER_SLOT = new int[]
	{ 13, 15, 16, 11 };
	protected final String name;
	protected final int durabilityMultiplier;
	protected final int[] slotProtections;
	protected final int enchantmentValue;
	protected final SoundEvent sound;
	protected final float toughness;
	protected final float knockbackResistance;
	protected final LazyLoadedValue<Ingredient> repairIngredient;

	private NFFGirlsArmorMaterials(String pName, int pDurabilityMultiplier, int[] pSlotProtections, int pEnchantmentValue,
			SoundEvent pSound, float pToughness, float pKnockbackResistance, Supplier<Ingredient> pRepairIngredient)
	{
		this.name = pName;
		this.durabilityMultiplier = pDurabilityMultiplier;
		this.slotProtections = pSlotProtections;
		this.enchantmentValue = pEnchantmentValue;
		this.sound = pSound;
		this.toughness = pToughness;
		this.knockbackResistance = pKnockbackResistance;
		this.repairIngredient = new LazyLoadedValue<>(pRepairIngredient);
	}

	protected int getTypeIndex(ArmorItem.Type type)
	{
		return type == ArmorItem.Type.HELMET ? 0 : (
				type == ArmorItem.Type.CHESTPLATE ? 1 : (
				type == ArmorItem.Type.LEGGINGS ? 2 : 3));
	}
	
	@Override
	public int getDurabilityForType(ArmorItem.Type type) {
		return HEALTH_PER_SLOT[getTypeIndex(type)] * this.durabilityMultiplier;
	}

	@Override
	public int getDefenseForType(ArmorItem.Type type) {
		return this.slotProtections[getTypeIndex(type)];
	}

	@Override
	public int getEnchantmentValue() {
		return this.enchantmentValue;
	}

	@Override
	public SoundEvent getEquipSound() {
		return this.sound;
	}

	@Override
	public Ingredient getRepairIngredient() {
		return this.repairIngredient.get();
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public float getToughness() {
		return this.toughness;
	}

	/**
	 * Gets the percentage of knockback resistance provided by armor of the
	 * material.
	 */
	@Override
	public float getKnockbackResistance() {
		return this.knockbackResistance;
	}
}
