package net.sodiumzh.nff.girls.entity.ai.goal.target;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.services.entity.ai.goal.preset.target.NFFNearestAttackableTargetGoal;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nfu.util.NFUReflectionStatics;

import java.lang.reflect.Field;

public class NFFGirlsNearestPotentiallyHostileToSelfTargetGoal extends NFFNearestAttackableTargetGoal<Mob> {

    private static final Field NATG_TARGET_CLASS =
        NFUReflectionStatics.findFieldIfDeclared(NearestAttackableTargetGoal.class, "f_26048_")
            .orElseThrow();

    private static final Field NATG_TARGET_CONDITIONS =
        NFUReflectionStatics.findFieldIfDeclared(NearestAttackableTargetGoal.class, "f_26051_")
            .orElseThrow();

    @SuppressWarnings("unchecked")
    private static boolean isPotentiallyHostileToSelf(INFFTamed mob, LivingEntity target) {
        if (!(target instanceof Mob)) return false;
        return ((Mob)target).targetSelector.getAvailableGoals().stream().map(WrappedGoal::getGoal)
            .filter(goal -> goal instanceof NearestAttackableTargetGoal<?> natg)
            .anyMatch(goal ->
                NFUReflectionStatics.getValue(NATG_TARGET_CLASS, goal)
                .castTo(Class.class)
                .isAssignableFrom(mob.getClass())
                && NFUReflectionStatics.getValue(NATG_TARGET_CONDITIONS, goal)
                .castTo(TargetingConditions.class)
                .test(target, mob.asMob()));
    }

    public NFFGirlsNearestPotentiallyHostileToSelfTargetGoal(INFFTamed mob) {
        super(mob, Mob.class, 10, true, false, living -> isPotentiallyHostileToSelf(mob, living));
        stateConditions(bm -> INFFGirlsTamed.get(bm).filter(INFFGirlsTamed::shouldAttackMobsHostileToSelf).isPresent());
        allowAllStatesExceptWait();
    }

}
