package net.sodiumzh.nff.girls.entity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nff.services.entity.taming.NFFTamedStatics;

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * A base interface for damage-dealing objects (e.g. projectiles) which should be limited to
 * target only allies or only enemies etc. Mainly for preventing friendly fire.
 */
public interface INFFSafeTarget<T extends INFFSafeTarget<T>> {

    public T setTargetType(INFFSafeTarget.TargetType type);

    public T setIntendedTarget(@Nullable LivingEntity intendedTarget);

    @Nullable
    public LivingEntity getIntendedTarget();

    public INFFSafeTarget.TargetType getTargetType();

    /**
     * When the attacker is an NFF mob, check if a target can be affected upon nff default rules
     */
    public default boolean canAffectNFF(LivingEntity target, INFFTamed source, TargetType targetType) {
        if (Objects.equals(target, this.getIntendedTarget())) return true;
        return switch (targetType) {
            case ALLY -> NFFTamedStatics.isLivingAlliedToBM(source, target);
            case NONE_ALLY -> !NFFTamedStatics.isLivingAlliedToBM(source, target);
            case ENEMY_ONLY -> {
                // Mob that is trying to attack shooter's ally
                if (target instanceof Mob targetMob
                    && targetMob.getTarget() instanceof Mob targetOfTarget
                    && NFFTamedStatics.isLivingAlliedToBM(source, targetOfTarget))
                    yield true;
                    // Mob that owner is attacking
                else if (target.getLastAttacker().equals(source.getOwnerInDimension()))
                    yield true;
                    // Mob that shooter's ally is attacking
                else if (source.asMob().level().getEntities(EntityTypeTest.forClass(Mob.class), source.asMob().getBoundingBox().inflate(8, 8, 8), mob -> NFFTamedStatics.isLivingAlliedToBM(source, mob))
                    .stream().anyMatch(mob -> mob.getTarget() != null && mob.getTarget().equals(target)))
                    yield true;
                yield false;
            }
            case TARGET_ONLY -> Objects.equals(source.asMob().getTarget(), target);
            case ALL -> true;
        };
    }

    public static enum TargetType {
        ALLY,
        NONE_ALLY,
        ENEMY_ONLY,
        TARGET_ONLY,
        ALL
    }
}
