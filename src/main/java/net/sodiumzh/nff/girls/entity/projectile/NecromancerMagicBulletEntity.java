package net.sodiumzh.nff.girls.entity.projectile;

import com.github.mechalopa.hmag.registry.ModEntityTypes;
import com.github.mechalopa.hmag.world.entity.NecroticReaperEntity;
import com.github.mechalopa.hmag.world.entity.projectile.MagicBulletEntity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.PlayMessages;
import net.sodiumzh.nautils.statics.NaUtilsEntityStatics;
import net.sodiumzh.nautils.statics.NaUtilsReflectionStatics;
import net.sodiumzh.nff.girls.entity.tamingprocess.hmag.HmagNecroticReaperTamingProcess;
import net.sodiumzh.nff.girls.entity.hmag.HmagNecroticReaperEntity;
import net.sodiumzh.nff.girls.registry.NFFGirlsEntityTypes;
import net.sodiumzh.nff.services.entity.taming.NFFTamingMapping;

public class NecromancerMagicBulletEntity extends MagicBulletEntity
{

	public boolean hasNecromancerHat = false;
	
	public NecromancerMagicBulletEntity(EntityType<? extends NecromancerMagicBulletEntity> type, Level worldIn)
	{
		super(type, worldIn);
		this.setVariant(1);
	}

	public NecromancerMagicBulletEntity(Level worldIn, LivingEntity shooter, double accelX, double accelY, double accelZ)
	{
		super(worldIn, shooter, accelX, accelY, accelZ);
		NaUtilsReflectionStatics.forceSet(this, Entity.class, "f_19847_", NFFGirlsEntityTypes.NECROMANCER_MAGIC_BULLET.get());	// Entity.type
		this.setVariant(1);
	}

	@OnlyIn(Dist.CLIENT)
	public NecromancerMagicBulletEntity(Level worldIn, double x, double y, double z, double accelX, double accelY, double accelZ)
	{
		super(worldIn, x, y, z, accelX, accelY, accelZ);
		NaUtilsReflectionStatics.forceSet(this, Entity.class, "f_19847_", NFFGirlsEntityTypes.NECROMANCER_MAGIC_BULLET.get());	// Entity.type
		this.setVariant(1);
	}

	public NecromancerMagicBulletEntity(PlayMessages.SpawnEntity spawnEntity, Level level)
	{
		this(NFFGirlsEntityTypes.NECROMANCER_MAGIC_BULLET.get(), level);
		this.setVariant(1);
	}
	
	@Override
	protected void onHitEntity(EntityHitResult result)
	{
		Entity entity = result.getEntity();

		if (!this.level.isClientSide)
		{
			if (entity instanceof LivingEntity le && !(entity instanceof ArmorStand) && entity != this.getOwner())
			{
				applyDirectEffect(le);
			}
			this.blast(entity);
			this.level.broadcastEntityEvent(this, (byte)3);
		}		
	}
	
	@Override
	protected void onHitBlock(BlockHitResult result)
	{
		if (!this.level.isClientSide)
			this.blast(null);
	}
	
	protected void blast(Entity ignore)
	{
		AABB area = new AABB(this.position().subtract(new Vec3(1.5, 1.5, 1.5)), this.position().add(new Vec3(1.5, 1.5, 1.5)));
		this.level.getEntities(this, area).stream().filter((Entity e) -> 
		(e instanceof LivingEntity && !(e instanceof ArmorStand) && e != this.getOwner() && e != ignore))
		.forEach((Entity e) -> applyEffect(e));
		NaUtilsEntityStatics.sendParticlesToEntity(this, ParticleTypes.EXPLOSION, 0, 0, 2, 0);
		this.level.playSound(null, this, SoundEvents.GENERIC_EXPLODE, getSoundSource(), 3.0f, 0.7f);
	}
	
	protected void applyDirectEffect(Entity target)
	{
		if (target instanceof LivingEntity living)
		{
			if (!(living instanceof ArmorStand))
			{
				boolean shouldAddWither = true;
				if (living.getClass().equals(NecroticReaperEntity.class)
						&& this.getOwner() instanceof Player player)
				{
					NecroticReaperEntity nr = (NecroticReaperEntity) living;
					HmagNecroticReaperTamingProcess handler = (HmagNecroticReaperTamingProcess) NFFTamingMapping
							.getHandler(ModEntityTypes.NECROTIC_REAPER.get());
					shouldAddWither = !handler.onHit(player, nr);
				}
	
				// Befriended Necrotic Reapers are immune to this magic bullet;
				// If it's shot by the owner, it will heal the mob and optionally add strength
				// effect if having a Necromancer's Hat.
				else if (living instanceof HmagNecroticReaperEntity nr)
				{
					if (this.getOwner() instanceof Player player && nr.getOwnerUUID().equals(player.getUUID()))
					{
						if (hasNecromancerHat)
						{
							nr.heal(6);
							NaUtilsEntityStatics.addEffectSafe(nr, MobEffects.DAMAGE_BOOST, 300, 1);
						} else
						{
							nr.heal(4);
						}
						NaUtilsEntityStatics.sendGlintParticlesToLivingDefault(nr);
					}
					shouldAddWither = false;
				}
				if (shouldAddWither)
				{
					NaUtilsEntityStatics.addEffectSafe(living, MobEffects.WITHER, 200, hasNecromancerHat ? 4 : 3);
					if (this.getOwner() instanceof LivingEntity ownerLiving)
					{
						living.setLastHurtByMob(ownerLiving);
					}
				}
			}
		}
	}
	
	protected void applyEffect(Entity target)
	{
		if (target instanceof LivingEntity living)
		{
			if (!(living instanceof ArmorStand))
			{
				boolean shouldAddWither = true;
				if (living.getClass().equals(NecroticReaperEntity.class)
						&& this.getOwner() instanceof Player player)
				{
					NecroticReaperEntity nr = (NecroticReaperEntity) living;
					HmagNecroticReaperTamingProcess handler = (HmagNecroticReaperTamingProcess) NFFTamingMapping
							.getHandler(ModEntityTypes.NECROTIC_REAPER.get());
					shouldAddWither = !handler.onHit(player, nr);
				}
	
				// Befriended Necrotic Reapers are immune to this magic bullet;
				// If it's shot by the owner, it will heal the mob and optionally add strength
				// effect if having a Necromancer's Hat.
				else if (living instanceof HmagNecroticReaperEntity nr)
				{
					if (this.getOwner() instanceof Player player && nr.getOwnerUUID().equals(player.getUUID()))
					{
						if (hasNecromancerHat)
						{
							nr.heal(3);
							NaUtilsEntityStatics.addEffectSafe(nr, MobEffects.DAMAGE_BOOST, 200, 1);
						} else
						{
							nr.heal(2);
						}
						NaUtilsEntityStatics.sendGlintParticlesToLivingDefault(nr);
					}
					shouldAddWither = false;
				}
				if (shouldAddWither)
				{
					NaUtilsEntityStatics.addEffectSafe(living, MobEffects.WITHER, 200, hasNecromancerHat ? 3 : 2);
					if (this.getOwner() instanceof LivingEntity ownerLiving)
					{
						living.setLastHurtByMob(ownerLiving);
					}
				}
			}
		}
	}
	
	@Override
	public int getVariant()
	{
		return 1;
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag compound)
	{
		super.addAdditionalSaveData(compound);
		compound.putBoolean("has_necromancer_hat", hasNecromancerHat);
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag compound)
	{
		super.readAdditionalSaveData(compound);
		hasNecromancerHat = compound.getBoolean("has_necromancer_hat");
	}
}
