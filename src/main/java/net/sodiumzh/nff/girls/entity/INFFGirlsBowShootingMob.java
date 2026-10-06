package net.sodiumzh.nff.girls.entity;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;

import javax.annotation.Nullable;

public interface INFFGirlsBowShootingMob extends INFFGirlsTamed, INFFGirlsBowShootingMobUtils
{
	
	@Nullable
	public default AbstractArrow shoot(LivingEntity pTarget, float pVelocity) {
		
		// Filter again to prevent it from shooting without arrow
		//if (this.getAdditionalInventory().orElseThrow().getItem(getArrowSlotIndex()).isEmpty())
		if (!this.canShoot())
			return null;

		AbstractArrow arrowEntity = this.createArrowEntity(this.getAdditionalInventory().orElseThrow().getItem(getArrowSlotIndex()));
		if (arrowEntity == null) return null;
		double d0 = pTarget.getX() - this.asMob().getX();
		double d1 = pTarget.getY(0.3333333333333333D) - arrowEntity.getY();
		double d2 = pTarget.getZ() - this.asMob().getZ();
		double d3 = Math.sqrt(d0 * d0 + d2 * d2);
		arrowEntity.setBaseDamage(arrowEntity.getBaseDamage() * this.asMob().getAttributeValue(Attributes.ATTACK_DAMAGE) / this.asMob().getAttributeBaseValue(Attributes.ATTACK_DAMAGE));
		boolean canPickUp = this.getAdditionalInventory().orElseThrow().getItem(getMainHandItemSlotIndex()).getEnchantmentLevel(Enchantments.INFINITY_ARROWS) <= 0
				|| this.getAdditionalInventory().orElseThrow().getItem(getArrowSlotIndex()).is(Items.TIPPED_ARROW) || this.getAdditionalInventory().orElseThrow().getItem(getArrowSlotIndex()).is(Items.SPECTRAL_ARROW);
		arrowEntity.pickup = canPickUp ? AbstractArrow.Pickup.ALLOWED : AbstractArrow.Pickup.DISALLOWED;
		arrowEntity.shoot(d0, d1 + d3 * (double) 0.2F, d2, 1.6F, 2.0F);
		this.asMob().playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.asMob().getRandom().nextFloat() * 0.4F + 0.8F));
		this.asMob().level().addFreshEntity(arrowEntity);
		return arrowEntity;
	}

	public default void postShoot()
	{
		if (this.getAdditionalInventory().orElseThrow().getItem(getMainHandItemSlotIndex()).getEnchantmentLevel(Enchantments.INFINITY_ARROWS) <= 0
				|| this.getAdditionalInventory().orElseThrow().getItem(getArrowSlotIndex()).is(Items.TIPPED_ARROW) || this.getAdditionalInventory().orElseThrow().getItem(getArrowSlotIndex()).is(Items.SPECTRAL_ARROW))
			this.getAdditionalInventory().orElseThrow().consumeItem(getArrowSlotIndex());
	}
	
	
	/**
	 * Optionally switch the main and backup weapons
	 */
	public default void checkSwitchingWeapons()
	{
		// When too close, switch to melee mode if possible
		if (this.asMob().distanceToSqr(this.asMob().getTarget()) < 6.25d) {
			if (isBow(this.getAdditionalInventory().orElseThrow().getItem(getMainHandItemSlotIndex())) && isMeleeWeapon(this.getAdditionalInventory().orElseThrow().getItem(getSecondaryWeaponSlotIndex()))) {
				this.getAdditionalInventory().orElseThrow().swapItem(getMainHandItemSlotIndex(), getSecondaryWeaponSlotIndex());
				this.getAdditionalInventory().orElseThrow().syncToMob(this.asMob());
			}
		}
		// When run out arrows, try taking weapon from backup-weapon slot
		if (isBow(this.getAdditionalInventory().orElseThrow().getItem(getMainHandItemSlotIndex())) && isMeleeWeapon(this.getAdditionalInventory().orElseThrow().getItem(getSecondaryWeaponSlotIndex()))
				&& this.getAdditionalInventory().orElseThrow().getItem(getArrowSlotIndex()).isEmpty()) {
			this.getAdditionalInventory().orElseThrow().swapItem(getMainHandItemSlotIndex(), getSecondaryWeaponSlotIndex());
			this.getAdditionalInventory().orElseThrow().syncToMob(this.asMob());
		}
		// When too far and having a bow on backup-weapon, switch to bow mode
		// Don't switch if don't have arrows
		else if (this.asMob().distanceToSqr(this.asMob().getTarget()) > 16d) {
			if (!isBow(this.getAdditionalInventory().orElseThrow().getItem(getMainHandItemSlotIndex())) && isBow(getAdditionalInventory().orElseThrow().getItem(getSecondaryWeaponSlotIndex()))
					&& !this.getAdditionalInventory().orElseThrow().getItem(getArrowSlotIndex()).isEmpty()) {
				this.getAdditionalInventory().orElseThrow().swapItem(getMainHandItemSlotIndex(), getSecondaryWeaponSlotIndex());
				this.getAdditionalInventory().orElseThrow().syncToMob(this.asMob());
			}
		}
		// When in melee mode without a weapon but having one on backup slot, change to it
		else if (!isBow(this.getAdditionalInventory().orElseThrow().getItem(getMainHandItemSlotIndex()))
				&& !isBow(this.getAdditionalInventory().orElseThrow().getItem(getSecondaryWeaponSlotIndex()))
				&& (this.getAdditionalInventory().orElseThrow().getItem(getMainHandItemSlotIndex()).isEmpty() || !isMeleeWeapon(this.getAdditionalInventory().orElseThrow().getItem(getMainHandItemSlotIndex())))
				&& !this.getAdditionalInventory().orElseThrow().getItem(getSecondaryWeaponSlotIndex()).isEmpty()
				&& isMeleeWeapon(this.getAdditionalInventory().orElseThrow().getItem(getSecondaryWeaponSlotIndex()))
				)
		{
			this.getAdditionalInventory().orElseThrow().swapItem(getMainHandItemSlotIndex(), getSecondaryWeaponSlotIndex());
			this.getAdditionalInventory().orElseThrow().syncToMob(this.asMob());
		}
	}
	
	@Override
	public default ItemStack getEquippingBow() {
		return this.getAdditionalInventory().orElseThrow().getItem(getMainHandItemSlotIndex());
	}

	public boolean canShoot();

    public default int getMainHandItemSlotIndex() {
        return 4;
    }

    public default int getSecondaryWeaponSlotIndex() {
        return 7;
    }

    public default int getArrowSlotIndex() {
        return 8;
    }


}
