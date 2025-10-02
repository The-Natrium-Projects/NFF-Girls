package net.sodiumzh.nff.girls.entity.ai.goal.target;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.services.entity.ai.goal.preset.target.NFFNearestAttackableTargetGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.target.NFFNearestUnfriendlyMobTargetGoal;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nfu.util.NFUReflectionStatics;

import java.lang.reflect.Field;
import java.util.function.Predicate;

public class NFFGirlsNearestPotentiallyHostileToOwnerTargetGoal extends NFFNearestAttackableTargetGoal<Mob> {

    private static final Field NATG_TARGET_CLASS =
        NFUReflectionStatics.findFieldIfDeclared(NearestAttackableTargetGoal.class, "f_26048_")
            .orElseThrow();

    private static final Field NATG_TARGET_CONDITIONS =
        NFUReflectionStatics.findFieldIfDeclared(NearestAttackableTargetGoal.class, "f_26051_")
            .orElseThrow();

    @SuppressWarnings("unchecked")
    private static boolean isPotentiallyHostileToOwner(INFFTamed mob, LivingEntity target) {
        Player owner = mob.getOwnerInDimension();
        if (owner == null) return false;
        if (!(target instanceof Mob)) return false;
        return ((Mob)target).targetSelector.getAvailableGoals().stream().map(WrappedGoal::getGoal)
            .filter(goal -> goal instanceof NearestAttackableTargetGoal<?> natg)
            .anyMatch(goal ->
                NFUReflectionStatics.getValue(NATG_TARGET_CLASS, goal)
                .castTo(Class.class)
                .isAssignableFrom(owner.getClass())
                && NFUReflectionStatics.getValue(NATG_TARGET_CONDITIONS, goal)
                .castTo(TargetingConditions.class)
                .test(target, owner));
    }

    public NFFGirlsNearestPotentiallyHostileToOwnerTargetGoal(INFFGirlsTamed mob, int randomInterval, boolean mustSee, boolean mustReach) {
        super(mob, Mob.class, randomInterval, mustSee, mustReach, living -> isPotentiallyHostileToOwner(mob, living));
        stateConditions(bm -> INFFGirlsTamed.get(bm).filter(INFFGirlsTamed::shouldAttackMobsHostileToOwner).isPresent());
    }

    public NFFGirlsNearestPotentiallyHostileToOwnerTargetGoal(INFFGirlsTamed mob, boolean mustSee, boolean mustReach) {
        this(mob, 10, mustSee, mustReach);
    }

    public NFFGirlsNearestPotentiallyHostileToOwnerTargetGoal(INFFGirlsTamed mob) {
        this(mob, 10, true, false);
    }

}
