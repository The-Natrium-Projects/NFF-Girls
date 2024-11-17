package net.sodiumzh.nff.girls.subsystem.baublesystem.baubles;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.sodiumzh.nff.girls.entity.hmag.HmagNecroticReaperEntity;
import net.sodiumzh.nff.services.subsystems.baublesystem.BaubleAttributeModifier;
import net.sodiumzh.nff.services.subsystems.baublesystem.BaubleBehavior;
import net.sodiumzh.nff.services.subsystems.baublesystem.BaubleEquippingCondition;
import net.sodiumzh.nff.services.subsystems.baublesystem.BaubleProcessingArgs;

public class NecroticReaperHandHoeBaubleBehavior extends BaubleBehavior
{

	public NecroticReaperHandHoeBaubleBehavior(ResourceLocation key)
	{
		super((item, itemstack) -> (item instanceof HoeItem), key, BaubleEquippingCondition.of(
				args -> args.user() instanceof HmagNecroticReaperEntity && (args.slotKey().equals("main_hand") || args.slotKey().equals("off_hand"))));
	}

	@Override
	public void onEquipped(BaubleProcessingArgs args) {
	}

	@Override
	public void preSlotTick(BaubleProcessingArgs args) {
	}

	@Override
	public void postSlotTick(BaubleProcessingArgs args) {
		// TODO Auto-generated method stub

	}

	@Override
	public void slotTick(BaubleProcessingArgs args) {
	}

	@Override
	public BaubleAttributeModifier[] getNonDuplicatableModifiers(Mob mob) {
		return null;
	}

	/*@Override
	public BaubleAttributeModifier[] getDuplicatableModifiers(BaubleProcessingArgs args) {
		double atk = getHoeAtk(args.baubleItemStack().getItem())
		return BaubleAttributeModifier.makeModifiers(Attributes.ATTACK_DAMAGE, //getHoeAtk)
	}*/

	private static int getHoeAtk(int lv)
	{
		if (lv < 0)
			return 0;
		int[] seq = {1, 2, 3, 5, 8, 13, 21, 34};
		if (lv < 8)
			return seq[lv];
		else return seq[7];
	}

	@SuppressWarnings("deprecation")
	@Override
	public BaubleAttributeModifier[] getDuplicatableModifiers(BaubleProcessingArgs args) {
		int lv = Math.round(((HoeItem)(args.baubleItemStack().getItem())).getTier().getLevel());
		double atk = (double)getHoeAtk(lv) * (1d + (double)EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY, args.baubleItemStack()) * 0.1d);
		return new BaubleAttributeModifier[] {
				new BaubleAttributeModifier(Attributes.ATTACK_DAMAGE, atk, AttributeModifier.Operation.ADDITION)
		};
	}
	
}
