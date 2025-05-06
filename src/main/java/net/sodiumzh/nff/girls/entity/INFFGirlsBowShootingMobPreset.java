package net.sodiumzh.nff.girls.entity;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.sodiumzh.nff.girls.registry.NFFGirlsTags;

import javax.annotation.Nullable;

public interface INFFGirlsBowShootingMobPreset
{

	public ItemStack getEquippingBow();
	
	@Nullable
	public default BowItem getBowType()
	{
		ItemStack bow = getEquippingBow();
		if (bow == null || bow.isEmpty() || !(bow.getItem() instanceof BowItem))
			return null;
		return (BowItem)(bow.getItem());
	}
	
	/**
	 * Create an arrow entity from the current using bow and arrow. It just creates an entity object but doesn't add it to the level.
	 * Manually add it in {@code performRangedAttack}.
	 */
	@Nullable
	public default AbstractArrow createArrowEntity(ItemStack usingArrow)
	{
		Mob mob = (Mob)this;
		if (mob.getLevel().isClientSide)
			return null;
		if (getBowType() == null)
			return null;
		
        ArrowItem arrowItem = (ArrowItem) (getEquippingBow().is(NFFGirlsTags.USES_VANILLA_ARROWS) ? Items.ARROW : usingArrow.getItem());
        AbstractArrow arrowEntity = arrowItem.createArrow(mob.getLevel(), usingArrow, mob);	// TippedArrow is involved here
        
        if (!getEquippingBow().is(NFFGirlsTags.USES_VANILLA_ARROWS))
        {
        	arrowEntity = getBowType().customArrow(arrowEntity);
        }
        
        int j = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, getEquippingBow());
        if (j > 0) {
           arrowEntity.setBaseDamage(arrowEntity.getBaseDamage() + (double)j * 0.5D + 0.5D);
        }

        int k = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, getEquippingBow());
        if (k > 0) {
           arrowEntity.setKnockback(arrowEntity.getKnockback() + k);
        }

        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, getEquippingBow()) > 0) {
           arrowEntity.setSecondsOnFire(100);
        }
        
		return arrowEntity;
	}
	
}
