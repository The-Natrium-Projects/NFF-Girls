package net.sodiumzh.nff.girls.entity.projectile;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.sodiumzh.nff.girls.registry.NFFGirlsEntityTypes;
import net.sodiumzh.nfu.registry.NFUEntityDataSerializers;
import net.sodiumzh.nfu.util.NFUParticleStatics;

import java.util.function.Supplier;

public class MobileParticleSourceEntity extends Entity implements ItemSupplier {

    private int maxLifetime = 10 * 20;
    private int remainingLifetime = 10 * 20;
    // Amount to send each tick. The fields amountPerTick and ticksPerParticle should be either 0 which means
    // disabled. This is for case that every tick should add particles and at least one.
    private static final EntityDataAccessor<Integer> PARTICLES_PER_TICK =
        SynchedEntityData.defineId(MobileParticleSourceEntity.class, EntityDataSerializers.INT);
    // This is for case that it should add a particle each several ticks.
    private static final EntityDataAccessor<Integer> TICKS_PER_PARTICLE =
        SynchedEntityData.defineId(MobileParticleSourceEntity.class, EntityDataSerializers.INT);

    private Vec3 posRndScale = new Vec3(0d, 0d, 0d);
    private Vec3 particleMaxVelocity = new Vec3(0d, 0d, 0d);
    private Vec3 particleVelocityRndScale = new Vec3(0d, 0d, 0d);
    private double speed = 1;
    private Supplier<Vec3> targetPosGetter = () -> new Vec3(0d, 0d, 0d);
    private static final EntityDataAccessor<Vec3> TARGET_POS =
        SynchedEntityData.defineId(MobileParticleSourceEntity.class, NFUEntityDataSerializers.VEC3.get());
    private static final EntityDataAccessor<ParticleOptions> PARTICLE_TYPE =
        SynchedEntityData.defineId(MobileParticleSourceEntity.class, EntityDataSerializers.PARTICLE);

    public MobileParticleSourceEntity(EntityType<? extends MobileParticleSourceEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.noPhysics = true;
    }

    public MobileParticleSourceEntity(Level pLevel, Supplier<Vec3> targetPosGetter) {
        this(NFFGirlsEntityTypes.MOBILE_PARTICLE_SOURCE.get(), pLevel);
        this.targetPosGetter = targetPosGetter;
    }
    @Override
    protected void defineSynchedData() {
        this.getEntityData().define(TARGET_POS, new Vec3(0d, 0d, 0d));
        this.getEntityData().define(PARTICLE_TYPE, ParticleTypes.ASH);
        this.getEntityData().define(PARTICLES_PER_TICK, 0);
        this.getEntityData().define(TICKS_PER_PARTICLE, 1);
    }

    public int getMaxLifetime() {
        return maxLifetime;
    }

    public MobileParticleSourceEntity setMaxLifetime(int value) {
        this.maxLifetime = value;
        return this;
    }

    public MobileParticleSourceEntity particlesPerTick(int val) {
        this.getEntityData().set(PARTICLES_PER_TICK, val);
        this.getEntityData().set(TICKS_PER_PARTICLE, 0);
        return this;
    }

    public MobileParticleSourceEntity ticksPerParticle(int val) {
        this.getEntityData().set(PARTICLES_PER_TICK, 0);
        this.getEntityData().set(TICKS_PER_PARTICLE, val);
        return this;
    }

    public ParticleOptions getParticleType() {

        return this.getEntityData().get(PARTICLE_TYPE);
    }

    public MobileParticleSourceEntity setParticleType(ParticleOptions type) {
        this.getEntityData().set(PARTICLE_TYPE, type);
        return this;
    }

    public Vec3 getPosRndScale() {
        return posRndScale;
    }

    public MobileParticleSourceEntity setPosRndScale(Vec3 posRndScale) {
        this.posRndScale = posRndScale;
        return this;
    }

    public Vec3 getParticleMaxVelocity() {
        return particleMaxVelocity;
    }

    public MobileParticleSourceEntity setParticleMaxVelocity(Vec3 particleMaxVelocity) {
        this.particleMaxVelocity = particleMaxVelocity;
        return this;
    }

    public Vec3 getParticleVelocityRndScale() {
        return particleVelocityRndScale;
    }

    public MobileParticleSourceEntity setParticleVelocityRndScale(Vec3 particleVelocityRndScale) {
        this.particleVelocityRndScale = particleVelocityRndScale;
        return this;
    }

    public Vec3 getCurrentTargetPos() {
        this.updateTargetPos();
        return this.getEntityData().get(TARGET_POS);
    }

    public MobileParticleSourceEntity setStartingPos(Vec3 pos) {
        this.setPos(pos);
        return this;
    }

    public MobileParticleSourceEntity setTargetPos(Supplier<Vec3> getter) {
        this.targetPosGetter = getter;
        return this;
    }

    public MobileParticleSourceEntity setTargetPos(Vec3 value) {
        this.targetPosGetter = () -> value;
        return this;
    }

    public void updateTargetPos() {
        if (!this.level().isClientSide)
            this.getEntityData().set(TARGET_POS, targetPosGetter.get());
    }

    /**
     * Speed in unit-per-second
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * Speed in unit-per-second
     */
    public MobileParticleSourceEntity setSpeed(double speed) {
        this.speed = speed;
        return this;
    }

    private int getParticleAmountThisTick() {
        int particlesPerTick = this.getEntityData().get(PARTICLES_PER_TICK);
        if (particlesPerTick != 0) return particlesPerTick;
        else return this.tickCount % this.getEntityData().get(TICKS_PER_PARTICLE) == 0 ? 1 : 0;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            if (remainingLifetime <= 0) {
                this.discard();
                return;
            }
            remainingLifetime--;
            try {
                updateTargetPos();
            } catch (RuntimeException e) {
                this.discard();
                return;
            }
            // In this case, it will reach the target within 2 ticks, remove it 1 tick in advance to reduce possible failures
            // of removal
            Vec3 currentTargetPos = this.getCurrentTargetPos();
            if (currentTargetPos.subtract(this.position()).lengthSqr() < speed * speed / 100d) {
                this.discard();
                return;
            }
            this.setDeltaMovement(currentTargetPos.subtract(this.position()).normalize().scale(speed / 20d));
            this.move(MoverType.SELF, this.getDeltaMovement());
        }
        if (this.isAlive() && this.level() instanceof ClientLevel)
            NFUParticleStatics.sendParticlesToEntity(this, this.getParticleType(), Vec3.ZERO, posRndScale,
                getParticleAmountThisTick(), speed);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {

    }

    @Override
    public ItemStack getItem() {
        return ItemStack.EMPTY;
    }
}
