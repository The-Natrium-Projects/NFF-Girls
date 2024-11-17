package net.sodiumzh.nff.girls.entity;

import javax.annotation.Nullable;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.sodiumzh.nff.girls.registry.NFFGirlsTags;

/**
 * Utilities for bow-shooting mob behaviors.
 */
public interface INFFGirlsBowShootingMobUtils
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
		if (mob.level().isClientSide)
			return null;
		if (getBowType() == null)
			return null;
		
        ArrowItem arrowItem = (ArrowItem) (getEquippingBow().is(NFFGirlsTags.USES_VANILLA_ARROWS) ? Items.ARROW : usingArrow.getItem());
        AbstractArrow arrowEntity = arrowItem.createArrow(mob.level(), usingArrow, mob);	// TippedArrow is involved here
        
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
	
	/** Check if an ItemStack is a bow. No need to perform null-check. */
	public default boolean isBow(ItemStack test)
	{
		return test != null && test.getItem() != null && test.getItem() instanceof BowItem;
	}
	
	/** Check if an ItemStack is a melee weapon (TieredItem). No need to perform null-check. */
	public default boolean isMeleeWeapon(ItemStack test)
	{
		return test != null && test.getItem() != null && test.getItem() instanceof TieredItem;
	}
	
}
