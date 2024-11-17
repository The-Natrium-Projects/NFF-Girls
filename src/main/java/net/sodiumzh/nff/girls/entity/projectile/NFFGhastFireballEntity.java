package net.sodiumzh.nff.girls.entity.projectile;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.sodiumzh.nff.girls.registry.NFFGirlsEntityTypes;

public class NFFGhastFireballEntity extends Fireball
{
	public float explosionPower = 1f;
	public boolean breakBlocks = true;
	public boolean isFromMob = true;
	public boolean alwaysDrop = true;
	public float hitDamage = 6.0f;
	
	
	public NFFGhastFireballEntity(EntityType<? extends NFFGhastFireballEntity> pEntityType,
			Level pLevel)
	{
		super(pEntityType, pLevel);
	}

	public NFFGhastFireballEntity(Level pLevel, LivingEntity pShooter, double pOffsetX, double pOffsetY,
			double pOffsetZ, float pExplosionPower)
	{
		super(NFFGirlsEntityTypes.BEFRIENDED_GHAST_FIREBALL.get(), pShooter, pOffsetX, pOffsetY, pOffsetZ, pLevel);
		this.explosionPower = pExplosionPower;
	}

	/**
	 * Called when this EntityFireball hits a block or entity.
	 */
	@Override
	protected void onHit(HitResult pResult) {
		super.onHit(pResult);
		if (!this.level.isClientSide)
		{
			boolean allowDestroy;
			if (!breakBlocks)
				allowDestroy = false;
			else if (this.getOwner() != null && this.getOwner() instanceof Mob)
			{
				allowDestroy = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level,
									this.getOwner());
			}
			else allowDestroy = true;
			
			this.level.explode(this, this.getX(), this.getY(), this.getZ(), this.explosionPower, allowDestroy,
					allowDestroy ? (alwaysDrop ? Explosion.BlockInteraction.BREAK : Explosion.BlockInteraction.DESTROY) : Explosion.BlockInteraction.NONE);
			this.discard();
		}

	}

	/**
	 * Called when the arrow hits an entity
	 */
	@Override
	protected void onHitEntity(EntityHitResult pResult) {
		super.onHitEntity(pResult);
		if (!this.level.isClientSide)
		{
			Entity entity = pResult.getEntity();
			Entity entity1 = this.getOwner();
			
			entity.hurt(DamageSource.fireball(this, entity1), hitDamage);
			if (entity1 instanceof LivingEntity)
			{
				this.doEnchantDamageEffects((LivingEntity) entity1, entity);
			}

		}
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag pCompound) {
		super.addAdditionalSaveData(pCompound);
		pCompound.putByte("ExplosionPower", (byte) this.explosionPower);
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readAdditionalSaveData(CompoundTag pCompound) {
		super.readAdditionalSaveData(pCompound);
		if (pCompound.contains("ExplosionPower", 99))
		{
			this.explosionPower = pCompound.getByte("ExplosionPower");
		}

	}
}
