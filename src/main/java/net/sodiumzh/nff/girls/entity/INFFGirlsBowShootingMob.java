package net.sodiumzh.nff.girls.entity;

import javax.annotation.Nullable;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;

public interface INFFGirlsBowShootingMob extends INFFGirlsTamed, INFFGirlsBowShootingMobUtils
{
	
	@Nullable
	public default AbstractArrow shoot(LivingEntity pTarget, float pVelocity) {
		
		// Filter again to prevent it from shooting without arrow
		if (this.getAdditionalInventory().getItem(8).isEmpty())
			return null;

		AbstractArrow arrowEntity = this.createArrowEntity(this.getAdditionalInventory().getItem(8));
		if (arrowEntity == null) return null;
		double d0 = pTarget.getX() - this.asMob().getX();
		double d1 = pTarget.getY(0.3333333333333333D) - arrowEntity.getY();
		double d2 = pTarget.getZ() - this.asMob().getZ();
		double d3 = Math.sqrt(d0 * d0 + d2 * d2);
		arrowEntity.setBaseDamage(arrowEntity.getBaseDamage() * this.asMob().getAttributeValue(Attributes.ATTACK_DAMAGE) / this.asMob().getAttributeBaseValue(Attributes.ATTACK_DAMAGE));
		boolean canPickUp = this.getAdditionalInventory().getItem(4).getEnchantmentLevel(Enchantments.INFINITY_ARROWS) <= 0
				|| this.getAdditionalInventory().getItem(8).is(Items.TIPPED_ARROW) || this.getAdditionalInventory().getItem(8).is(Items.SPECTRAL_ARROW);
		arrowEntity.pickup = canPickUp ? AbstractArrow.Pickup.ALLOWED : AbstractArrow.Pickup.DISALLOWED;
		arrowEntity.shoot(d0, d1 + d3 * (double) 0.2F, d2, 1.6F, 2.0F);
		this.asMob().playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.asMob().getRandom().nextFloat() * 0.4F + 0.8F));
		this.asMob().level().addFreshEntity(arrowEntity);
		return arrowEntity;
	}

	public default void postShoot()
	{
		if (this.getAdditionalInventory().getItem(4).getEnchantmentLevel(Enchantments.INFINITY_ARROWS) <= 0
				|| this.getAdditionalInventory().getItem(8).is(Items.TIPPED_ARROW) || this.getAdditionalInventory().getItem(8).is(Items.SPECTRAL_ARROW))
			this.getAdditionalInventory().consumeItem(8);
	}
	
	
	/**
	 * Optionally switch the main and backup weapons
	 */
	public default void checkSwitchingWeapons()
	{
		// When too close, switch to melee mode if possible
		if (this.asMob().distanceToSqr(this.asMob().getTarget()) < 6.25d) {
			if (isBow(this.getAdditionalInventory().getItem(4)) && isMeleeWeapon(this.getAdditionalInventory().getItem(7))) {
				this.getAdditionalInventory().swapItem(4, 7);
				updateFromInventory();
			}
		}
		// When run out arrows, try taking weapon from backup-weapon slot
		if (isBow(this.getAdditionalInventory().getItem(4)) && isMeleeWeapon(this.getAdditionalInventory().getItem(7))
				&& this.getAdditionalInventory().getItem(8).isEmpty()) {
			this.getAdditionalInventory().swapItem(4, 7);
			updateFromInventory();
		}
		// When too far and having a bow on backup-weapon, switch to bow mode
		// Don't switch if don't have arrows
		else if (this.asMob().distanceToSqr(this.asMob().getTarget()) > 16d) {
			if (!isBow(this.getAdditionalInventory().getItem(4)) && isBow(getAdditionalInventory().getItem(7))
					&& !this.getAdditionalInventory().getItem(8).isEmpty()) {
				this.getAdditionalInventory().swapItem(4, 7);
				updateFromInventory();
			}
		}
		// When in melee mode without a weapon but having one on backup slot, change to it
		else if (!isBow(this.getAdditionalInventory().getItem(4))
				&& !isBow(this.getAdditionalInventory().getItem(7))
				&& (this.getAdditionalInventory().getItem(4).isEmpty() || !isMeleeWeapon(this.getAdditionalInventory().getItem(4)))
				&& !this.getAdditionalInventory().getItem(7).isEmpty()
				&& isMeleeWeapon(this.getAdditionalInventory().getItem(7))
				)
		{
			this.getAdditionalInventory().swapItem(4, 7);
			updateFromInventory();
		}
	}
	
	@Override
	public default ItemStack getEquippingBow() {
		return this.getAdditionalInventory().getItem(4);
	}
}
